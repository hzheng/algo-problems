import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1615: https://leetcode.com/problems/maximal-network-rank/
//
// There is an infrastructure of n cities with some number of roads connecting these cities. Each
// roads[i] = [ai, bi] indicates that there is a bidirectional road between cities ai and bi.
// The network rank of two different cities is defined as the total number of directly connected
// roads to either city. If a road is directly connected to both cities, it is only counted once.
// The maximal network rank of the infrastructure is the maximum network rank of all pairs of
// different cities. Given the integer n and the array roads, return the maximal network rank of the
// entire infrastructure.
// Constraints:
// 2 <= n <= 100
// 0 <= roads.length <= n * (n - 1) / 2
// roads[i].length == 2
// 0 <= ai, bi <= n-1
// ai != bi
// Each pair of cities has at most one road connecting them.
public class MaximalNetworkRank {
    // Set
    // time complexity: O(N^2), space complexity: O(N^2)
    // 11 ms(71.00%), 40.0 MB(5.24%) for 81 tests
    public int maximalNetworkRank(int n, int[][] roads) {
        Set[] graph = new Set[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new HashSet<>();
        }
        for (int[] path : roads) {
            int a = path[0];
            int b = path[1];
            graph[a].add(b);
            graph[b].add(a);
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                res = Math.max(res, rank(i, j, graph));
            }
        }
        return res;
    }

    private int rank(int a, int b, Set[] graph) {
        return graph[a].size() + graph[b].size() - (graph[a].contains(b) ? 1 : 0);
    }

    // time complexity: O(N^2), space complexity: O(N^2)
    // 3 ms(99.79%), 40.0 MB(5.24%) for 81 tests
    public int maximalNetworkRank2(int n, int[][] roads) {
        boolean[][] connected = new boolean[n][n];
        int[] degree = new int[n];
        for (int[] road : roads) {
            degree[road[0]]++;
            degree[road[1]]++;
            connected[road[0]][road[1]] = true;
            connected[road[1]][road[0]] = true;
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                res = Math.max(res, degree[i] + degree[j] - (connected[i][j] ? 1 : 0));
            }
        }
        return res;
    }

    private void test(int n, int[][] roads, int expected) {
        assertEquals(expected, maximalNetworkRank(n, roads));
        assertEquals(expected, maximalNetworkRank2(n, roads));
    }

    @Test public void test() {
        test(4, new int[][] {{0, 1}, {0, 3}, {1, 2}, {1, 3}}, 4);
        test(5, new int[][] {{0, 1}, {0, 3}, {1, 2}, {1, 3}, {2, 3}, {2, 4}}, 5);
        test(8, new int[][] {{0, 1}, {1, 2}, {2, 3}, {2, 4}, {5, 6}, {5, 7}}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
