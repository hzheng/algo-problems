import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1782: https://leetcode.com/problems/count-pairs-of-nodes/
//
// You are given an undirected graph represented by an integer n, which is the number of nodes, and
// edges, where edges[i] = [ui, vi] which indicates that there is an undirected edge between ui and
// vi. You are also given an integer array queries.
// The answer to the jth query is the number of pairs of nodes (a, b) that satisfy the following
// conditions:
// a < b
// cnt is strictly greater than queries[j], where cnt is the number of edges incident to a or b.
// Return an array answers such that answers.length == queries.length and answers[j] is the answer
// of the jth query.
// Note that there can be repeated edges.
//
// Constraints:
// 2 <= n <= 2 * 10^4
// 1 <= edges.length <= 10^5
// 1 <= ui, vi <= n
// ui != vi
// 1 <= queries.length <= 20
// 0 <= queries[j] < edges.length
public class CountPairs {
    // Sort + Hash Table
    // time complexity: O(N*log(N)+Q*(N+E)), space complexity: O(N+Q)
    // 196 ms(88.89%), 89.1 MB(88.89%) for 46 tests
    public int[] countPairs(int n, int[][] edges, int[] queries) {
        int[] degree = new int[n];
        int[] sortedDegree = new int[n];
        Map<Integer, Integer> graph = new HashMap<>();
        for (int[] e : edges) {
            int u = Math.min(e[0], e[1]) - 1;
            int v = Math.max(e[0], e[1]) - 1;
            sortedDegree[u] = ++degree[u];
            sortedDegree[v] = ++degree[v];
            int key = u * n + v;
            graph.put(key, graph.getOrDefault(key, 0) + 1);
        }
        Arrays.sort(sortedDegree);
        int[] res = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int threshold = queries[q];
            for (int i = 0, j = n - 1; i < j; ) {
                if (sortedDegree[i] + sortedDegree[j] > threshold) {
                    res[q] += (j--) - i;
                } else {
                    i++;
                }
            }
            for (int k : graph.keySet()) {
                int u = k / n;
                int v = k % n;
                if (degree[u] + degree[v] > threshold
                    && degree[u] + degree[v] - graph.get(k) <= threshold) {
                    res[q]--;
                }
            }
        }
        return res;
    }

    // Binary Indexed Tree + Hash Table
    // time complexity: O(N*log(E)+Q*(N+E)), space complexity: O(N+Q)
    // 455 ms(33.33%), 101.3 MB(33.33%) for 46 tests
    public int[] countPairs2(int n, int[][] edges, int[] queries) {
        Map<Integer, Integer> graph = new HashMap<>();
        int[] degree = new int[n];
        for (int[] e : edges) {
            int u = e[0] - 1;
            int v = e[1] - 1;
            degree[u]++;
            degree[v]++;
            int key = u * n + v;
            graph.put(key, graph.getOrDefault(key, 0) + 1);
            key = v * n + u;
            graph.put(key, graph.getOrDefault(key, 0) + 1);
        }
        int m = edges.length;
        int[] bit = new int[m + 1];
        for (int d : degree) {
            for (int j = d + 1; j <= m; j += (j & -j)) {
                bit[j]++;
            }
        }
        int[] res = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int threshold = queries[q];
            for (int i = 0; i < n; i++) {
                res[q] += n;
                for (int j = Math.max(0, threshold + 1 - degree[i]); j > 0; j -= (j & -j)) {
                    res[q] -= bit[j];
                }
                if (degree[i] * 2 > threshold) { // exclude self
                    res[q]--;
                }
            }
            for (int k : graph.keySet()) {
                int u = k / n;
                int v = k % n;
                if (degree[u] + degree[v] > threshold
                    && degree[u] + degree[v] - graph.get(k) <= threshold) {
                    res[q]--;
                }
            }
            res[q] /= 2;
        }
        return res;
    }

    private void test(int n, int[][] edges, int[] queries, int[] expected) {
        assertArrayEquals(expected, countPairs(n, edges, queries));
        assertArrayEquals(expected, countPairs2(n, edges, queries));
    }

    @Test public void test() {
        test(4, new int[][] {{1, 2}, {2, 4}, {1, 3}, {2, 3}, {2, 1}}, new int[] {2, 3},
             new int[] {6, 5});
        test(5, new int[][] {{1, 5}, {1, 5}, {3, 4}, {2, 5}, {1, 3}, {5, 1}, {2, 3}, {2, 5}},
             new int[] {1, 2, 3, 4, 5}, new int[] {10, 10, 9, 8, 6});
        test(5, new int[][] {{4, 5}, {1, 3}, {1, 4}},
             new int[] {0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1, 1, 2},
             new int[] {10, 8, 10, 10, 8, 8, 10, 10, 10, 10, 8, 10, 10, 8, 10, 8, 8, 3});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
