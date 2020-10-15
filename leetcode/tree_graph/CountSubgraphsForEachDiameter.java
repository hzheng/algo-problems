import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1617: https://leetcode.com/problems/count-subtrees-with-max-distance-between-cities/
//
// There are n cities numbered from 1 to n. You are given an array edges of size n-1, where
// edges[i] = [ui, vi] represents a bidirectional edge between cities ui and vi. There exists a
// unique path between each pair of cities. In other words, the cities form a tree.
// A subtree is a subset of cities where every city is reachable from every other city in the
// subset, where the path between each pair passes through only the cities from the subset. Two
// subtrees are different if there is a city in one subtree that is not present in the other.
// For each d from 1 to n-1, find the number of subtrees in which the maximum distance between any
// two cities in the subtree is equal to d.
// Return an array of size n-1 where the dth element (1-indexed) is the number of subtrees in which
// the maximum distance between any two cities is equal to d.
// Notice that the distance between the two cities is the number of edges in the path between them.
// Constraints:
// 2 <= n <= 15
// edges.length == n-1
// edges[i].length == 2
// 1 <= ui, vi <= n
// All pairs (ui, vi) are distinct.
public class CountSubgraphsForEachDiameter {
    // Bit Manipulation + Queue + BFS
    // time complexity: O(2^N*N^2), space complexity: O(N^2)
    // 100 ms(53.15%), 39.4 MB(5.16%) for 31 tests
    public int[] countSubgraphsForEachDiameter(int n, int[][] edges) {
        @SuppressWarnings("unchecked") List<Integer>[] graph = new List[n + 1];
        for (int i = n; i > 0; i--) {
            graph[i] = new ArrayList<>();
        }
        for (int[] e : edges) {
            graph[e[0]].add(e[1]);
            graph[e[1]].add(e[0]);
        }
        int[] res = new int[n - 1];
        for (int mask = (1 << n) - 1; mask > 1; mask--) {
            int d = diameter(graph, mask);
            if (d > 0) {
                res[d - 1]++;
            }
        }
        return res;
    }

    private int diameter(List<Integer>[] graph, int mask) {
        int count = Integer.bitCount(mask);
        int res = 0;
        for (int i = 1; i <= graph.length; i++) {
            if ((mask << -i) < 0) {
                int h = height(graph, mask, i, count);
                if (h <= 0) { return 0; }

                res = Math.max(res, h);
            }
        }
        return res;
    }

    private int height(List<Integer>[] graph, int mask, int start, int total) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        boolean[] visited = new boolean[graph.length];
        int level = 0;
        int count = 0;
        while (!queue.isEmpty()) {
            boolean hasNew = false;
            for (int size = queue.size(); size > 0; size--) {
                int cur = queue.poll();
                if (visited[cur]) { continue; }

                visited[cur] = true;
                count++;
                hasNew = true;
                for (int next : graph[cur]) {
                    if ((mask << -next) < 0) {
                        queue.offer(next);
                    }
                }
            }
            if (hasNew) {
                level++;
            }
        }
        return (count == total) ? level - 1 : 0;
    }

    // Bit Manipulation + DFS + Recursion
    // time complexity: O(2^N*N), space complexity: O(N^2)
    // 26 ms(91.15%), 39.6 MB(6.63%) for 31 tests
    public int[] countSubgraphsForEachDiameter2(int n, int[][] edges) {
        @SuppressWarnings("unchecked") List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] e : edges) {
            graph[e[0] - 1].add(e[1] - 1);
            graph[e[1] - 1].add(e[0] - 1);
        }
        int[] res = new int[n - 1];
        for (int mask = (1 << n) - 1; mask > 2; mask--) {
            for (int i = 0; ; i++)
                if ((mask & (1 << i)) != 0) {
                    int[] count = new int[2]; // count[0]: diameter; count[1]: node count
                    dfs(graph, mask ^ (1 << i), i, count);
                    if (count[0] > 0 && count[1] == Integer.bitCount(mask)) {
                        res[count[0] - 1]++;
                    }
                    break;
                }
        }
        return res;
    }

    private int dfs(List<Integer>[] graph, int mask, int cur, int[] res) {
        int maxPathLenFromCur = 0;
        res[1]++;
        for (int next : graph[cur]) {
            if ((mask & (1 << next)) != 0) {
                int pathLen = 1 + dfs(graph, mask ^ (1 << next), next, res);
                res[0] = Math.max(res[0], pathLen + maxPathLenFromCur);
                maxPathLenFromCur = Math.max(maxPathLenFromCur, pathLen);
            }
        }
        return maxPathLenFromCur;
    }

    // TODO: Union Find

    private void test(int n, int[][] edges, int[] expected) {
        assertArrayEquals(expected, countSubgraphsForEachDiameter(n, edges));
        assertArrayEquals(expected, countSubgraphsForEachDiameter2(n, edges));
    }

    @Test public void test() {
        test(4, new int[][] {{1, 2}, {2, 3}, {2, 4}}, new int[] {3, 4, 0});
        test(2, new int[][] {{1, 2}}, new int[] {1});
        test(3, new int[][] {{1, 2}, {2, 3}}, new int[] {2, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
