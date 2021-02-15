import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1761: https://leetcode.com/problems/minimum-degree-of-a-connected-trio-in-a-graph/
//
// You are given an undirected graph. You are given an integer n which is the number of nodes in the
// graph and an array edges, where each edges[i] = [ui, vi] indicates that there is an undirected
// edge between ui and vi. A connected trio is a set of three nodes where there is an edge between
// every pair of them. The degree of a connected trio is the number of edges where one endpoint is
// in the trio, and the other is not. Return the minimum degree of a connected trio in the graph, or
// -1 if the graph has no connected trios.
//
// Constraints:
// 2 <= n <= 400
// edges[i].length == 2
// 1 <= edges.length <= n * (n-1) / 2
// 1 <= ui, vi <= n
// ui != vi
// There are no repeated edges.
public class MinTrioDegree {
    // time complexity: O(N^3), space complexity: O(N^2)
    // 28 ms(100.00%), 64.3 MB(100.00%) for 68 tests
    public int minTrioDegree(int n, int[][] edges) {
        boolean[][] mat = new boolean[n][n];
        int[] degree = new int[n];
        for (int[] e : edges) {
            int u = e[0] - 1;
            int v = e[1] - 1;
            mat[u][v] = true;
            mat[v][u] = true;
            degree[u]++;
            degree[v]++;
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (!mat[i][j]) { continue; }

                for (int k = j + 1; k < n; k++) {
                    if (mat[i][k] && mat[j][k]) {
                        res = Math.min(res, degree[i] + degree[j] + degree[k]);
                    }
                }
            }
        }
        return res == Integer.MAX_VALUE ? -1 : res - 6;
    }

    void test(int n, int[][] edges, int expected) {
        assertEquals(expected, minTrioDegree(n, edges));
    }

    @Test public void test() {
        test(6, new int[][] {{1, 2}, {1, 3}, {3, 2}, {4, 1}, {5, 2}, {3, 6}}, 3);
        test(7, new int[][] {{1, 3}, {4, 1}, {4, 3}, {2, 5}, {5, 6}, {6, 7}, {7, 5}, {2, 6}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
