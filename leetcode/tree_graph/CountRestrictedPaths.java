import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1786: https://leetcode.com/problems/number-of-restricted-paths-from-first-to-last-node/
//
// There is an undirected weighted connected graph. You are given a positive integer n which denotes
// that the graph has n nodes labeled from 1 to n, and an array edges where each
// edges[i] = [ui, vi, weighti] denotes that there is an edge between nodes ui and vi with weight
// equal to weighti.
// A path from node start to node end is a sequence of nodes [z0, z1, z2, ..., zk] such that
// z0 = start and zk = end and there is an edge between zi and zi+1 where 0 <= i <= k-1.
// The distance of a path is the sum of the weights on the edges of the path. Let
// distanceToLastNode(x) denote the shortest distance of a path between node n and node x. A
// restricted path is a path that also satisfies that
// distanceToLastNode(zi) > distanceToLastNode(zi+1) where 0 <= i <= k-1.
// Return the number of restricted paths from node 1 to node n. Since that number may be too large,
// return it modulo 10^9 + 7.
//
// Constraints:
// 1 <= n <= 2 * 10^4
// n - 1 <= edges.length <= 4 * 10^4
// edges[i].length == 3
// 1 <= ui, vi <= n
// ui != vi
// 1 <= weighti <= 10^5
// There is at most one edge between any two nodes.
// There is at least one path between any two nodes.
public class CountRestrictedPaths {
    private final static int MOD = 1_000_000_007;

    // Dijkstra's algorithm + DFS + Dynamic Programming(Top-Down)
    // time complexity: O(E*log(N)), space complexity: O(N+E)
    // 77 ms(%), 76.8 MB(%) for 77 tests
    public int countRestrictedPaths(int n, int[][] edges) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] e : edges) {
            add(e[0], e[1], e[2], graph);
            add(e[1], e[0], e[2], graph);
        }
        int[] dist = new int[n + 1];
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(a -> dist[a]));
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[n] = 0;
        for (pq.offer(n); !pq.isEmpty(); ) {
            int u = pq.poll();
            for (int[] v : graph.getOrDefault(u, Collections.emptyList())) {
                if (dist[v[0]] > dist[u] + v[1]) {
                    dist[v[0]] = dist[u] + v[1];
                    pq.offer(v[0]);
                }
            }
        }
        return dfs(1, graph, dist, new Integer[n + 1]);
    }

    private int dfs(int cur, Map<Integer, List<int[]>> graph, int[] dist, Integer[] dp) {
        if (dp[cur] != null) { return dp[cur]; }
        if (cur == dp.length - 1) { return 1; }

        int res = 0;
        for (int[] nei : graph.getOrDefault(cur, Collections.emptyList())) {
            int v = nei[0];
            if (dist[cur] > dist[v]) {
                res = (res + dfs(v, graph, dist, dp)) % MOD;
            }
        }
        return dp[cur] = res;
    }

    private void add(int u, int v, int w, Map<Integer, List<int[]>> graph) {
        List<int[]> list = graph.computeIfAbsent(u, x -> new ArrayList<>());
        list.add(new int[] {v, w});
    }

    private void test(int n, int[][] edges, int expected) {
        assertEquals(expected, countRestrictedPaths(n, edges));
    }

    @Test public void test() {
        test(5, new int[][] {{1, 2, 3}, {1, 3, 3}, {2, 3, 1}, {1, 4, 2}, {5, 2, 2}, {3, 5, 1},
                             {5, 4, 10}}, 3);
        test(7, new int[][] {{1, 3, 1}, {4, 1, 2}, {7, 3, 4}, {2, 5, 3}, {5, 6, 1}, {6, 7, 2},
                             {7, 5, 3}, {2, 6, 4}}, 1);
        test(220, new int[][] {{2, 220, 1}, {3, 220, 1}, {2, 4, 1}, {3, 4, 1}, {5, 4, 1}, {6, 4, 1},
                               {5, 7, 1}, {6, 7, 1}, {8, 7, 1}, {9, 7, 1}, {8, 10, 1}, {9, 10, 1},
                               {11, 10, 1}, {12, 10, 1}, {11, 13, 1}, {12, 13, 1}, {14, 13, 1},
                               {15, 13, 1}, {14, 16, 1}, {15, 16, 1}, {17, 16, 1}, {18, 16, 1},
                               {17, 19, 1}, {18, 19, 1}, {20, 19, 1}, {21, 19, 1}, {20, 22, 1},
                               {21, 22, 1}, {23, 22, 1}, {24, 22, 1}, {23, 25, 1}, {24, 25, 1},
                               {26, 25, 1}, {27, 25, 1}, {26, 28, 1}, {27, 28, 1}, {29, 28, 1},
                               {30, 28, 1}, {29, 31, 1}, {30, 31, 1}, {32, 31, 1}, {33, 31, 1},
                               {32, 34, 1}, {33, 34, 1}, {35, 34, 1}, {36, 34, 1}, {35, 37, 1},
                               {36, 37, 1}, {38, 37, 1}, {39, 37, 1}, {38, 40, 1}, {39, 40, 1},
                               {41, 40, 1}, {42, 40, 1}, {41, 43, 1}, {42, 43, 1}, {44, 43, 1},
                               {45, 43, 1}, {44, 46, 1}, {45, 46, 1}, {47, 46, 1}, {48, 46, 1},
                               {47, 49, 1}, {48, 49, 1}, {50, 49, 1}, {51, 49, 1}, {50, 52, 1},
                               {51, 52, 1}, {53, 52, 1}, {54, 52, 1}, {53, 55, 1}, {54, 55, 1},
                               {56, 55, 1}, {57, 55, 1}, {56, 58, 1}, {57, 58, 1}, {59, 58, 1},
                               {60, 58, 1}, {59, 61, 1}, {60, 61, 1}, {62, 61, 1}, {63, 61, 1},
                               {62, 64, 1}, {63, 64, 1}, {65, 64, 1}, {66, 64, 1}, {65, 67, 1},
                               {66, 67, 1}, {68, 67, 1}, {69, 67, 1}, {68, 70, 1}, {69, 70, 1},
                               {71, 70, 1}, {72, 70, 1}, {71, 73, 1}, {72, 73, 1}, {74, 73, 1},
                               {75, 73, 1}, {74, 76, 1}, {75, 76, 1}, {77, 76, 1}, {78, 76, 1},
                               {77, 79, 1}, {78, 79, 1}, {80, 79, 1}, {81, 79, 1}, {80, 82, 1},
                               {81, 82, 1}, {83, 82, 1}, {84, 82, 1}, {83, 85, 1}, {84, 85, 1},
                               {86, 85, 1}, {87, 85, 1}, {86, 88, 1}, {87, 88, 1}, {89, 88, 1},
                               {90, 88, 1}, {89, 91, 1}, {90, 91, 1}, {92, 88, 100000},
                               {92, 85, 100000}, {92, 82, 100000}, {92, 76, 100000},
                               {92, 73, 100000}, {92, 70, 100000}, {92, 61, 100000},
                               {92, 58, 100000}, {92, 52, 100000}, {92, 46, 100000},
                               {92, 43, 100000}, {92, 34, 100000}, {92, 28, 100000},
                               {92, 7, 100000}, {92, 4, 100000}, {92, 220, 100000}, {93, 92, 1},
                               {94, 92, 1}, {93, 95, 1}, {94, 95, 1}, {96, 95, 1}, {97, 95, 1},
                               {96, 98, 1}, {97, 98, 1}, {99, 98, 1}, {100, 98, 1}, {99, 101, 1},
                               {100, 101, 1}, {102, 101, 1}, {103, 101, 1}, {102, 104, 1},
                               {103, 104, 1}, {105, 104, 1}, {106, 104, 1}, {105, 107, 1},
                               {106, 107, 1}, {108, 107, 1}, {109, 107, 1}, {108, 110, 1},
                               {109, 110, 1}, {111, 110, 1}, {112, 110, 1}, {111, 113, 1},
                               {112, 113, 1}, {114, 113, 1}, {115, 113, 1}, {114, 116, 1},
                               {115, 116, 1}, {117, 116, 1}, {118, 116, 1}, {117, 119, 1},
                               {118, 119, 1}, {120, 119, 1}, {121, 119, 1}, {120, 122, 1},
                               {121, 122, 1}, {123, 122, 1}, {124, 122, 1}, {123, 125, 1},
                               {124, 125, 1}, {126, 125, 1}, {127, 125, 1}, {126, 128, 1},
                               {127, 128, 1}, {129, 128, 1}, {130, 128, 1}, {129, 131, 1},
                               {130, 131, 1}, {132, 131, 1}, {133, 131, 1}, {132, 134, 1},
                               {133, 134, 1}, {135, 134, 1}, {136, 134, 1}, {135, 137, 1},
                               {136, 137, 1}, {138, 137, 1}, {139, 137, 1}, {138, 140, 1},
                               {139, 140, 1}, {141, 140, 1}, {142, 140, 1}, {141, 143, 1},
                               {142, 143, 1}, {144, 143, 1}, {145, 143, 1}, {144, 146, 1},
                               {145, 146, 1}, {147, 146, 1}, {148, 146, 1}, {147, 149, 1},
                               {148, 149, 1}, {150, 149, 1}, {151, 149, 1}, {150, 152, 1},
                               {151, 152, 1}, {153, 152, 1}, {154, 152, 1}, {153, 155, 1},
                               {154, 155, 1}, {156, 155, 1}, {157, 155, 1}, {156, 158, 1},
                               {157, 158, 1}, {159, 158, 1}, {160, 158, 1}, {159, 161, 1},
                               {160, 161, 1}, {162, 161, 1}, {163, 161, 1}, {162, 164, 1},
                               {163, 164, 1}, {165, 164, 1}, {166, 164, 1}, {165, 167, 1},
                               {166, 167, 1}, {168, 167, 1}, {169, 167, 1}, {168, 170, 1},
                               {169, 170, 1}, {171, 170, 1}, {172, 170, 1}, {171, 173, 1},
                               {172, 173, 1}, {174, 173, 1}, {175, 173, 1}, {174, 176, 1},
                               {175, 176, 1}, {177, 176, 1}, {178, 176, 1}, {177, 179, 1},
                               {178, 179, 1}, {180, 179, 1}, {181, 179, 1}, {180, 182, 1},
                               {181, 182, 1}, {183, 182, 1}, {184, 182, 1}, {183, 185, 1},
                               {184, 185, 1}, {186, 185, 1}, {187, 185, 1}, {186, 188, 1},
                               {187, 188, 1}, {189, 188, 1}, {190, 188, 1}, {189, 191, 1},
                               {190, 191, 1}, {192, 191, 1}, {193, 191, 1}, {192, 194, 1},
                               {193, 194, 1}, {195, 194, 1}, {196, 194, 1}, {195, 197, 1},
                               {196, 197, 1}, {198, 197, 1}, {199, 197, 1}, {198, 200, 1},
                               {199, 200, 1}, {201, 200, 1}, {202, 200, 1}, {201, 203, 1},
                               {202, 203, 1}, {204, 203, 1}, {205, 203, 1}, {204, 206, 1},
                               {205, 206, 1}, {207, 206, 1}, {208, 206, 1}, {207, 209, 1},
                               {208, 209, 1}, {210, 209, 1}, {211, 209, 1}, {210, 212, 1},
                               {211, 212, 1}, {1, 212, 1}, {213, 220, 1}, {214, 220, 1},
                               {215, 220, 1}, {216, 220, 1}, {217, 220, 1}, {218, 220, 1},
                               {219, 220, 1}}, 0);
    }

    @Test public void test2() {
        CountRestrictedPaths c = new CountRestrictedPaths();
        test(c::countRestrictedPaths);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<Integer, int[][], Integer> countRestrictedPaths) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int n = Integer.parseInt(scanner.nextLine());
                int[][] edges = Utils.readInt2Array(scanner.nextLine());
                int res = countRestrictedPaths.apply(n, edges);
                int expected = Integer.parseInt(scanner.nextLine());
                assertEquals(expected, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
