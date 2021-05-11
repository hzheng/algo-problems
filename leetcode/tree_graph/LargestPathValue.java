import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1857: https://leetcode.com/problems/largest-color-value-in-a-directed-graph/
//
// There is a directed graph of n colored nodes and m edges. The nodes are numbered from 0 to n - 1.
// You are given a string colors where colors[i] is a lowercase English letter representing the
// color of the ith node in this graph (0-indexed). You are also given a 2D array edges where
// edges[j] = [aj, bj] indicates that there is a directed edge from node aj to node bj.
// A valid path in the graph is a sequence of nodes x1 -> x2 -> x3 -> ... -> xk such that there is
// a directed edge from xi to xi+1 for every 1 <= i < k. The color value of the path is the number
// of nodes that are colored the most frequently occurring color along that path.
// Return the largest color value of any valid path in the given graph, or -1 if the graph contains
// a cycle.
//
// Constraints:
// n == colors.length
// m == edges.length
// 1 <= n <= 10^5
// 0 <= m <= 10^5
// colors consists of lowercase English letters.
// 0 <= aj, bj < n
public class LargestPathValue {
    // Recursion + DFS + Dynamic Programming(Top-Down) + Hash Table
    // time complexity: O(V+E), space complexity: O(V)
    // 117 ms(79.91%), 147.7 MB(66.66%) for 79 tests
    public int largestPathValue(String colors, int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        char[] color = colors.toCharArray();
        int n = color.length;
        int[] indegree = new int[n];
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            indegree[e[1]]++;
        }
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                graph.computeIfAbsent(n, x -> new ArrayList<>()).add(i);
            }
        }
        int[] visited = new int[n];
        int[] res = dfs(color, graph, n, visited, new HashMap<>());
        if (res == null || !Arrays.stream(visited).allMatch(v -> v == 1)) { return -1; }

        return Arrays.stream(res).max().getAsInt();
    }

    private int[] dfs(char[] colors, Map<Integer, List<Integer>> graph, int cur, int[] visited,
                      Map<Integer, int[]> dp) {
        int[] count = dp.get(cur);
        if (count != null) { return count; }

        int n = colors.length;
        int[] res = new int[26];
        for (int nei : graph.getOrDefault(cur, Collections.emptyList())) {
            if (visited[nei] == 2) { return null; }

            visited[nei] = 2;
            int[] ans = dfs(colors, graph, nei, visited, dp);
            if (ans == null) { return null; }

            for (int i = 0; i < res.length; i++) {
                res[i] = Math.max(res[i], ans[i]);
            }
            visited[nei] = 1;
        }
        if (cur < n) {
            res[colors[cur] - 'a']++;
        }
        dp.put(cur, res);
        return res;
    }

    // Topological Sort + BFS + Queue + Hash Table
    // time complexity: O(V+E), space complexity: O(V)
    // 279 ms(22.63%), 163.8 MB(44.65%) for 79 tests
    public int largestPathValue2(String colors, int[][] edges) {
        int n = colors.length();
        char[] c = colors.toCharArray();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] indegree = new int[n];
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            indegree[e[1]]++;
        }
        int[][] map = new int[n][26];
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
                map[i][c[i] - 'a'] = 1;
            }
        }
        int visited = 0;
        int res = 0;
        for (; !queue.isEmpty(); visited++) {
            int cur = queue.poll();
            int max = Arrays.stream(map[cur]).max().getAsInt();
            res = Math.max(res, max);
            for (int nei : graph.getOrDefault(cur, Collections.emptyList())) {
                for (int i = 0; i < 26; i++) {
                    map[nei][i] = Math.max(map[nei][i], map[cur][i] + (c[nei] - 'a' == i ? 1 : 0));
                }
                if (--indegree[nei] == 0) {
                    queue.offer(nei);
                }
            }
        }
        return (visited == n) ? res : -1;
    }

    private void test(String colors, int[][] edges, int expected) {
        assertEquals(expected, largestPathValue(colors, edges));
        assertEquals(expected, largestPathValue2(colors, edges));
    }

    @Test public void test() {
        test("abaca", new int[][] {{0, 1}, {0, 2}, {2, 3}, {3, 4}}, 3);
        test("a", new int[][] {{0, 0}}, -1);
        test("hhqhuqhqff",
             new int[][] {{0, 1}, {0, 2}, {2, 3}, {3, 4}, {3, 5}, {5, 6}, {2, 7}, {6, 7}, {7, 8},
                          {3, 8}, {5, 8}, {8, 9}, {3, 9}, {6, 9}}, 3);
        test("iiiiii", new int[][] {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}}, 6);
        test("aaa", new int[][] {{1, 2}, {2, 1}}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
