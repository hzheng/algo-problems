import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1192: https://leetcode.com/problems/critical-connections-in-a-network/
//
// There are n servers numbered from 0 to n-1 connected by undirected server-to-server connections
// forming a network where connections[i] = [a, b] represents a connection between servers a and b.
// Any server can reach any other server directly or indirectly through the network.
// A critical connection is a connection that, if removed, will make some server unable to reach
// some other server.
// Return all critical connections in the network in any order.
//
// Constraints:
// 1 <= n <= 10^5
// n-1 <= connections.length <= 10^5
// connections[i][0] != connections[i][1]
// There are no repeated connections.
public class CriticalConnections {
    // DFS + Recursion
    // time complexity: O(|E|), space complexity: O(|E|)
    // 151 ms(40.01%), 108.6 MB(56.59%) for 12 tests
    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (List<Integer> conn : connections) {
            graph[conn.get(0)].add(conn.get(1));
            graph[conn.get(1)].add(conn.get(0));
        }
        Set<List<Integer>> connectionsSet = new HashSet<>(connections);
        dfs(graph, 0, 2, new int[n], connectionsSet);
        return new ArrayList<>(connectionsSet);
    }

    private int dfs(List<Integer>[] graph, int node, int depth, int[] rank,
                    Set<List<Integer>> connectionsSet) {
        if (rank[node] > 0) { return rank[node]; }

        rank[node] = depth;
        int res = depth;
        for (int nei : graph[node]) {
            if (rank[nei] == depth - 1) { continue; } // skip parent

            int minDepth = dfs(graph, nei, depth + 1, rank, connectionsSet);
            if (minDepth <= depth) {
                connectionsSet.remove(Arrays.asList(node, nei));
                connectionsSet.remove(Arrays.asList(nei, node));
            }
            res = Math.min(res, minDepth);
        }
        return res;
    }

    // https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm
    // DFS + Recursion
    // time complexity: O(|E|), space complexity: O(|E|)
    // 84 ms(95.01%), 101.7 MB(87.06%) for 12 tests
    public List<List<Integer>> criticalConnections2(int n, List<List<Integer>> connections) {
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (List<Integer> conn : connections) {
            graph[conn.get(0)].add(conn.get(1));
            graph[conn.get(1)].add(conn.get(0));
        }
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, graph, 0, -1, 1, new int[n]);
        return res;
    }

    private void dfs(List<List<Integer>> res, List<Integer>[] graph, int cur, int parent,
                     int curTime, int[] rank) {
        rank[cur] = curTime;
        for (int nei : graph[cur]) {
            if (nei == parent) { continue; }

            if (rank[nei] == 0) {
                dfs(res, graph, nei, cur, curTime + 1, rank);
            }
            rank[cur] = Math.min(rank[cur], rank[nei]);
            if (curTime < rank[nei]) {
                res.add(Arrays.asList(cur, nei));
            }
        }
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(
            Function<Integer, List<List<Integer>>, List<List<Integer>>> criticalConnections, int n,
            Integer[][] connections, Integer[][] expected) {
        List<List<Integer>> connectionList = Utils.toList(connections);
        List<List<Integer>> expectedList = Utils.toList(expected);
        List<List<Integer>> res = criticalConnections.apply(n, connectionList);
        for (List<Integer> conn : res) {
            Collections.sort(conn);
        }
        Comparator<Object> cmp = Comparator.comparing(a -> ((List<Integer>)a).get(0))
                                           .thenComparing(b -> ((List<Integer>)b).get(1));
        res.sort(cmp);
        expectedList.sort(cmp);
        assertEquals(expectedList, res);
    }

    private void test(int n, Integer[][] connections, Integer[][] expected) {
        CriticalConnections c = new CriticalConnections();
        test(c::criticalConnections, n, connections, expected);
        test(c::criticalConnections2, n, connections, expected);
    }

    private void test(
            Function<Integer, List<List<Integer>>, List<List<Integer>>> criticalConnections) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int n = Integer.parseInt(scanner.nextLine());
                Integer[][] connections =
                        Utils.toIntegerArray(Utils.readInt2Array(scanner.nextLine()));
                Integer[][] expected =
                        Utils.toIntegerArray(Utils.readInt2Array(scanner.nextLine()));
                test(criticalConnections, n, connections, expected);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test() {
        test(5, new Integer[][] {{1, 0}, {2, 1}, {3, 2}, {4, 2}, {2, 0}, {3, 0}, {4, 0}},
             new Integer[][] {});
        test(5, new Integer[][] {{1, 0}, {2, 0}, {3, 2}, {4, 2}, {4, 3}, {3, 0}, {4, 0}},
             new Integer[][] {{0, 1}});
        test(6, new Integer[][] {{0, 1}, {1, 2}, {2, 0}, {1, 3}, {3, 4}, {4, 5}, {5, 3}, {2, 5}},
             new Integer[][] {});
        test(4, new Integer[][] {{0, 1}, {1, 2}, {2, 0}, {1, 3}}, new Integer[][] {{1, 3}});
        test(6, new Integer[][] {{0, 1}, {1, 2}, {2, 0}, {1, 3}, {3, 4}, {4, 5}, {5, 3}},
             new Integer[][] {{1, 3}});
        test(10, new Integer[][] {{1, 0}, {2, 0}, {3, 0}, {4, 1}, {5, 3}, {6, 1}, {7, 2}, {8, 1},
                                  {9, 6}, {9, 3}, {3, 2}, {4, 2}, {7, 4}, {6, 2}, {8, 3}, {4, 0},
                                  {8, 6}, {6, 5}, {6, 3}, {7, 5}, {8, 0}, {8, 5}, {5, 4}, {2, 1},
                                  {9, 5}, {9, 7}, {9, 4}, {4, 3}}, new Integer[][] {});
    }

    @Test public void test2() {
        CriticalConnections c = new CriticalConnections();
        test(c::criticalConnections);
        test(c::criticalConnections2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
