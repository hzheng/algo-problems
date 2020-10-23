import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1519: https://leetcode.com/problems/number-of-nodes-in-the-sub-tree-with-the-same-label/
//
// Given a tree consisting of n nodes numbered from 0 to n - 1 and exactly n - 1 edges. The root of
// the tree is the node 0, and each node of the tree has a label which is a lower-case character
// given in the string labels (i.e. The node with the number i has the label labels[i]).
// The edges array is given on the form edges[i] = [ai, bi], which means there is an edge between
// nodes ai and bi in the tree.
// Return an array of size n where ans[i] is the number of nodes in the subtree of the ith node
// which have the same label as node i.
// A subtree of a tree T is the tree consisting of a node in T and all of its descendant nodes.
// Constraints:
// 1 <= n <= 10^5
// edges.length == n - 1
// edges[i].length == 2
// 0 <= ai, bi < n
// ai != bi
// labels.length == n
// labels is consisting of only of lower-case English letters.
public class CountSubTrees {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 100 ms(60.36%), 102.8 MB(5.46%) for 59 tests
    public int[] countSubTrees(int n, int[][] edges, String labels) {
        @SuppressWarnings("unchecked") List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }
        int[][] count = new int[26][n];
        dfs(0, graph, labels, new boolean[n], count);
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = count[labels.charAt(i) - 'a'][i];
        }
        return res;
    }

    private void dfs(int cur, List<Integer>[] graph, String labels, boolean[] visited,
                     int[][] counts) {
        visited[cur] = true;
        counts[labels.charAt(cur) - 'a'][cur]++;
        for (int child : graph[cur]) {
            if (visited[child]) { continue; }

            dfs(child, graph, labels, visited, counts);
            for (int[] cnt : counts) {
                cnt[cur] += cnt[child];
            }
        }
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 106 ms(57.09%), 547.7 MB(5.46%) for 59 tests
    public int[] countSubTrees2(int n, int[][] edges, String labels) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], l -> new ArrayList<>()).add(e[1]);
            graph.computeIfAbsent(e[1], l -> new ArrayList<>()).add(e[0]);
        }
        int[] res = new int[n];
        dfs(graph, 0, labels, res, new boolean[n]);
        return res;
    }

    private int[] dfs(Map<Integer, List<Integer>> graph, int cur, String labels, int[] res,
                      boolean[] visited) {
        int[] count = new int[26];
        if (!visited[cur]) {
            visited[cur] = true;
            for (int child : graph.getOrDefault(cur, Collections.emptyList())) {
                int[] subRes = dfs(graph, child, labels, res, visited);
                for (int i = 0; i < 26; ++i) {
                    count[i] += subRes[i];
                }
            }
            res[cur] = ++count[labels.charAt(cur) - 'a'];
        }
        return count;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 97 ms(64.00%), 103.1 MB(5.46%) for 59 tests
    public int[] countSubTrees3(int n, int[][] edges, String labels) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], l -> new ArrayList<>()).add(e[1]);
            graph.computeIfAbsent(e[1], l -> new ArrayList<>()).add(e[0]);
        }
        int[] res = new int[n];
        dfs(graph, 0, -1, labels, res);
        return res;
    }

    private int[] dfs(Map<Integer, List<Integer>> graph, int cur, int parent, String labels,
                      int[] res) {
        int[] count = new int[26];
        for (int child : graph.getOrDefault(cur, Collections.emptyList())) {
            if (child == parent) { continue; }

            int[] subCount = dfs(graph, child, cur, labels, res);
            for (int i = 0; i < 26; ++i) {
                count[i] += subCount[i];
            }
        }
        res[cur] = ++count[labels.charAt(cur) - 'a'];
        return count;
    }

    private void test(int n, int[][] edges, String labels, int[] expected) {
        assertArrayEquals(expected, countSubTrees(n, edges, labels));
        assertArrayEquals(expected, countSubTrees2(n, edges, labels));
        assertArrayEquals(expected, countSubTrees3(n, edges, labels));
    }

    @Test public void test() {
        test(7, new int[][] {{0, 1}, {0, 2}, {1, 4}, {1, 5}, {2, 3}, {2, 6}}, "abaedcd",
             new int[] {2, 1, 1, 1, 1, 1, 1});
        test(4, new int[][] {{0, 1}, {1, 2}, {0, 3}}, "bbbb", new int[] {4, 2, 1, 1});
        test(5, new int[][] {{0, 1}, {0, 2}, {1, 3}, {0, 4}}, "aabab", new int[] {3, 2, 1, 1, 1});
        test(6, new int[][] {{0, 1}, {0, 2}, {1, 3}, {3, 4}, {4, 5}}, "cbabaa",
             new int[] {1, 2, 1, 1, 2, 1});
        test(7, new int[][] {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}}, "aaabaaa",
             new int[] {6, 5, 4, 1, 3, 2, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
