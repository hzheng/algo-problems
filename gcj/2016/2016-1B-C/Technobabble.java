import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/11254486/dashboard#s=p2
// Round 1B 2016: Problem C - Technobabble
//
// Student choose a two-word topic that is not already on the sheet. Some students
// try to fake their way into the conference. They choose the first word of some topic
// already on the sheet and the second word of some topic already on the sheet, and
// combine them (putting the first word first, and the second word second) to create a
// new "topic". The fakers can't come up with any new first or second words on their own;
// they must use existing ones from the sheet. Moreover, they won't try to use an
// existing first word as their own second word (unless the word also already exists
// on the sheet as a second word), or vice versa. You have a list of all N of the
// submitted topics. What's the largest number of them that could have been faked?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with an integer N, followed by N lines, each of which
// represents a different topic and has two strings of uppercase English letters:
// the two words of the topic, in order.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// and y is an integer: the largest number of topics that could have possibly been faked.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ length of each word ≤ 20.
// No topic is repeated within a case.
// Small dataset
// 1 ≤ N ≤ 16.
// Large dataset
// 1 ≤ N ≤ 1000.
public class Technobabble {
    private static final int INF = Integer.MAX_VALUE;
    private static final String NIL = null;

    // Hopcroft–Karp Algorithm
    // time complexity: O(E * (V ^ 1/2))
    public static int maxFakes(String[][] topics) {
        Map<String, List<String> > adjList = new HashMap<>();
        Map<String, String> uPairs = new HashMap<>();
        Map<String, String> vPairs = new HashMap<>();
        Map<String, Integer> dist = new HashMap<>();
        for (String[] topic : topics) {
            String u = topic[0] + "_"; // in case a word appears both sides
            String v = topic[1];
            uPairs.put(u, NIL);
            vPairs.put(v, NIL);
            addArrow(adjList, u, v);
            addArrow(adjList, v, u);
        }
        int res = topics.length - uPairs.size() - vPairs.size();
        while (bfs(adjList, uPairs, vPairs, dist)) { // augmented paths available
            for (String u : uPairs.keySet()) {
                if (uPairs.get(u) == NIL && dfs(adjList, uPairs, vPairs, dist, u)) {
                    res++;
                }
            }
        }
        return res;
    }

    private static void addArrow(Map<String, List<String> > adjList, String u, String v) {
        List<String> neigbors = adjList.get(u);
        if (neigbors == null) {
            adjList.put(u, neigbors = new ArrayList<>());
        }
        neigbors.add(v);
    }

    private static boolean bfs(Map<String, List<String> > adjList, Map<String, String> uPairs,
                               Map<String, String> vPairs, Map<String, Integer> dist) {
        Queue<String> queue = new LinkedList<>();
        for (String u : uPairs.keySet()) {
            if (uPairs.get(u) == NIL) {
                dist.put(u, 0);
                queue.offer(u);
            } else {
                dist.put(u, INF);
            }
        }
        for (dist.put(NIL, INF); !queue.isEmpty(); ) {
            String u = queue.poll();
            if (dist.get(u) < dist.get(NIL)) {
                for (String v : adjList.get(u)) {
                    String vPair = vPairs.get(v);
                    if (dist.get(vPair) == INF) {
                        dist.put(vPair, dist.get(u) + 1);
                        queue.offer(vPair);
                    }
                }
            }
        }
        return dist.get(NIL) != INF;
    }

    private static boolean dfs(Map<String, List<String> > adjList,
                               Map<String, String> uPairs, Map<String, String> vPairs,
                               Map<String, Integer> dist, String u) {
        if (u == NIL) return true;

        for (String v : adjList.get(u)) {
            if ((dist.get(vPairs.get(v)) == dist.get(u) + 1) &&
                dfs(adjList, uPairs, vPairs, dist, vPairs.get(v))) {
                vPairs.put(v, u);
                uPairs.put(u, v);
                return true;
            }
        }
        dist.put(u, Integer.MAX_VALUE);
        return false;
    }

    private static final int CAPACITY = 1;
    private static class FlowArrow {
        int flow;
        int head;   // head of arrow(end of the edge)
        int prev;   // previous arrow with the same start node

        FlowArrow(int flow, int head, int prev) {
            this.flow = flow;
            this.head = head;
            this.prev = prev;
        }
    }

    private static class NodeState {
        int level;           // assigned level
        int lastArrow = -1; // last arrow id of the current node
        int curArrow;       // current working arrow
    }

    // Dinic's algorithm
    // time complexity: O(E * (V ^ 1/2))
    public static int maxFakes2(String[][] topics) {
        int[] nodeCount = new int[2];
        int n = topics.length;
        int MAX_NODES = n * 2 + 2;
        FlowArrow[] arrows = new FlowArrow[n * 6];
        NodeState[] states = new NodeState[MAX_NODES];
        for (int i = 0; i < MAX_NODES; i++) {
            states[i] = new NodeState();
        }
        @SuppressWarnings("unchecked")
        Map<String, Integer>[] idMap = new Map[2];
        idMap[0] = new HashMap<>();
        idMap[1] = new HashMap<>();
        int arrowCount = 0;
        for (String[] topic : topics) {
            addArrow(arrows, states, getId(topic[0], idMap, nodeCount, 0),
                    getId(topic[1], idMap, nodeCount, 1) + n, arrowCount);
            arrowCount += 2;
        }
        int source = MAX_NODES - 1;
        int sink = MAX_NODES - 2;
        for (int i = 0; i < nodeCount[0]; i++, arrowCount += 2) {
            addArrow(arrows, states, source, i, arrowCount);
        }
        for (int i = 0; i < nodeCount[1]; i++, arrowCount += 2) {
            addArrow(arrows, states, i + n, sink, arrowCount);
        }
        int res = n - nodeCount[0] - nodeCount[1];
        while (bfs(arrows, states, source, sink)) { // level graph built
            for (NodeState state : states) {
                state.curArrow = state.lastArrow;
            }
            for (int more; (more = dfs(arrows, states, source, sink, INF)) > 0; res += more) {}
        }
        return res;
    }

    private static int getId(String s, Map<String, Integer>[] idMap, int[] nodeCount, int index) {
        Integer id = idMap[index].putIfAbsent(s, nodeCount[index]);
        return (id != null) ? id : nodeCount[index]++;
    }

    private static void addArrow(FlowArrow[] arrows, NodeState[] states, int u, int v, int arrowCount) {
        arrows[arrowCount] = new FlowArrow(0, v, states[u].lastArrow);
        states[u].lastArrow = arrowCount;
        arrows[++arrowCount] = new FlowArrow(CAPACITY, u, states[v].lastArrow);
        states[v].lastArrow = arrowCount;
    }

    private static boolean bfs(FlowArrow[] arrows, NodeState[] states, int source, int sink) {
        for (NodeState state : states) {
            state.level = 0;
        }
        states[source].level = 1;
        int[] queue = new int[states.length];
        queue[0] = source;
        for (int head = 0, tail = 1; head < tail; head++) {
            for (int cur = queue[head], i = states[cur].lastArrow; i >= 0; ) {
                FlowArrow arrow = arrows[i];
                int next = arrow.head;
                if (states[next].level == 0 && arrow.flow < CAPACITY) {
                    states[next].level = states[cur].level + 1;
                    queue[tail++] = next;
                }
                i = arrow.prev;
            }
        }
        return states[sink].level > 0; // sink is reachable
    }

    private static int dfs(FlowArrow[] arrows, NodeState[] states, int from, int to, int maxFlow) {
        if (from == to) return maxFlow;

        for (int i = states[from].curArrow; i >= 0; ) {
            FlowArrow arrow = arrows[i];
            if (states[arrow.head].level == states[from].level + 1 && arrow.flow < CAPACITY) {
                int res = dfs(arrows, states, arrow.head, to, Math.min(maxFlow, CAPACITY - arrow.flow));
                if (res > 0) {
                    arrow.flow += res;
                    arrows[i ^ 1].flow -= res; // deduct from the reverse arrow
                    return res;
                }
            }
            states[from].curArrow = i = arrow.prev;
        }
        return 0;
    }

    // TODO: Ford-Fulkerson algorithm

    void test(String[][] topics, int expected) {
        assertEquals(expected, maxFakes(topics));
        assertEquals(expected, maxFakes2(topics));
    }

    @Test
    public void test() {
        test(new String[][] { {"R", "D"}, {"C", "F"}, {"R", "F"}, {"M", "B"},
                              {"R", "B"}, {"M", "G"}, {"B", "G"} }, 3);
        test(new String[][] {{"CODE", "JAM"}, {"SPACE", "JAM"}, {"PEARL", "JAM"}}, 0);
        test(new String[][] {{"A", "B"}, {"C", "D"}, {"A", "D"}, {"C", "B"}}, 2);
        test(new String[][] {{"HYDROCARBON", "COMBUSTION"},
                             {"QUAIL", "BEHAVIOR"},
                             {"QUAIL", "COMBUSTION"}}, 1);
        test(new String[][] {{"INTERGALACTIC", "PLANETARY"}, {"PLANETARY", "INTERGALACTIC"}}, 0);
        test(new String[][] {{"GE", "IV"}, {"GE", "VV"}, {"GE", "XG"},
                             {"HQ", "AD"}, {"HQ", "QI"}, {"IB", "TX"}, {"NF", "YV"}, {"OZ", "QI"},
                             {"OZ", "YC"}, {"YW", "IV"}, {"YW", "VV"}}, 3);
        // https://en.wikipedia.org/wiki/Hopcroft%E2%80%93Karp_algorithm#/media/File:HopcroftKarpExample.png
        test(new String[][] { {"U0", "V0"}, {"U0", "V1"}, {"U1", "V0"}, {"U1", "V4"},
                              {"U2", "V2"}, {"U2", "V3"}, {"U3", "V0"}, {"U3", "V4"},
                              {"U4", "V1"}, {"U4", "V3"}}, 5);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int n = in.nextInt();
        String[][] topics = new String[n][2];
        for (int i = 0; i < n; i++) {
            topics[i] = new String[] {in.next(), in.next()};
        }
        out.println(maxFakes(topics));
        // out.println(maxFakes2(topics));
    }
}
