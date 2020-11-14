import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC802: https://leetcode.com/problems/find-eventual-safe-states/
//
// In a directed graph, we start at some node and every turn, walk along a directed edge of the
// graph.  If we reach a node that is terminal (i.e. it has no outgoing directed edges), we stop.
// Now, say our starting node is eventually safe if and only if we must eventually walk to a
// terminal node.  More specifically, there exists a natural number K so that for any choice of
// where to walk, we must have stopped at a terminal node in less than K steps.
// Which nodes are eventually safe?  Return them as an array in sorted order.
// The directed graph has N nodes with labels 0, 1, ..., N-1, where N is the length of graph. The
// graph is given in the following form: graph[i] is a list of labels j such that (i, j) is a
// directed edge of the graph.
//
// Note:
// graph will have length at most 10000.
// The number of edges in the graph will not exceed 32000.
// Each graph[i] will be a sorted list of different integers.
public class EventualSafeNodes {
    // DFS + Recursion
    // time complexity: O(N+E), space complexity: O(N)
    // 5 ms(62.29%), 48.9 MB(55.44%) for 111 tests
    public List<Integer> eventualSafeNodes(int[][] graph) {
        int n = graph.length;
        State[] state = new State[n];
        Arrays.fill(state, State.UNVISITED);
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (isSafe(i, graph, state)) {
                res.add(i);
            }
        }
        return res;
    }

    private enum State {
        UNVISITED, VISITING, SAFE, UNSAFE
    }

    private boolean isSafe(int cur, int[][] graph, State[] state) {
        switch (state[cur]) {
        case SAFE:
            return true;

        case UNSAFE:
        case VISITING:
            state[cur] = State.UNSAFE;
            return false;
        }
        state[cur] = State.VISITING;
        for (int nei : graph[cur]) {
            if (!isSafe(nei, graph, state)) {
                state[cur] = State.UNSAFE;
                return false;
            }
        }
        state[cur] = State.SAFE;
        return true;
    }

    // Queue + Set
    // time complexity: O(N+E), space complexity: O(N)
    // 66 ms(14.66%), 53.4 MB(8.87%) for 111 tests
    public List<Integer> eventualSafeNodes2(int[][] graph) {
        int n = graph.length;
        boolean[] safe = new boolean[n];
        List<Set<Integer>> outEdges = new ArrayList<>();
        List<Set<Integer>> inEdges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            outEdges.add(new HashSet<>());
            inEdges.add(new HashSet<>());
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int u = 0; u < n; u++) {
            if (graph[u].length == 0) {
                queue.offer(u);
            }
            for (int v : graph[u]) {
                outEdges.get(u).add(v);
                inEdges.get(v).add(u);
            }
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            safe[u] = true;
            for (int v : inEdges.get(u)) {
                Set<Integer> outEdgeSet = outEdges.get(v);
                outEdgeSet.remove(u);
                if (outEdgeSet.isEmpty()) {
                    queue.offer(v);
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (safe[i]) {
                res.add(i);
            }
        }
        return res;
    }

    // Queue + Set (Topological Sort)
    // time complexity: O(N+E), space complexity: O(N)
    // 55 ms(18.56%), 49.8 MB(13.71%) for 111 tests
    public List<Integer> eventualSafeNodes3(int[][] graph) {
        int n = graph.length;
        int[] outdegrees = new int[n];
        Map<Integer, Set<Integer>> inEdges = new HashMap<>();
        for (int u = 0; u < n; u++) {
            for (int v : graph[u]) {
                inEdges.computeIfAbsent(v, x -> new HashSet<>()).add(u);
                outdegrees[u]++;
            }
        }
        Set<Integer> res = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (outdegrees[i] == 0) {
                res.add(i);
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int v = queue.poll();
            res.add(v);
            for (int u : inEdges.getOrDefault(v, Collections.emptySet())) {
                if (--outdegrees[u] == 0) {
                    queue.offer(u);
                }
            }
        }
        return new ArrayList<>(new TreeSet<>(res));
    }

    private void test(int[][] graph, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, eventualSafeNodes(graph));
        assertEquals(expectedList, eventualSafeNodes2(graph));
        assertEquals(expectedList, eventualSafeNodes3(graph));
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}}, new Integer[] {2, 4, 5, 6});
        test(new int[][] {{}, {0, 2, 3, 4}, {3}, {4}, {}}, new Integer[] {0, 1, 2, 3, 4});
        test(new int[][] {{1, 14}, {6, 9, 11, 16}, {1, 3, 4, 5, 13, 16, 17},
                          {9, 10, 12, 13, 17, 18}, {9, 10, 13, 18}, {7, 9, 11, 19},
                          {2, 8, 14, 15, 17, 18}, {8, 12, 15, 16, 18, 19}, {10, 12, 14, 19},
                          {10, 12, 14, 15, 19}, {12, 16}, {12, 16, 17, 18, 19},
                          {13, 14, 16, 18, 19}, {10, 14, 15, 16, 17, 19}, {}, {17, 19}, {},
                          {18, 19}, {8, 19}, {}}, new Integer[] {14, 16, 19});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
