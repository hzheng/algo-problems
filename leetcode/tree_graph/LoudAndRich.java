import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC851: https://leetcode.com/problems/loud-and-rich/
//
// In a group of N people, each person has different amounts of money, and
// different levels of quietness. We'll say that richer[i] = [x, y] if person x
// definitely has more money than person y. We'll say quiet[x] = q if person x
// has quietness q. Now, return answer, where answer[x] = y if y is the least
// quiet person, among all people who have equal to or more money than person x.
public class LoudAndRich {
    // DFS + Recursion
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 8.21%(225 ms for 86 tests)
    public int[] loudAndRich(int[][] richer, int[] quiet) {
        int n = quiet.length;
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i <= n; i++) {
            graph.put(i, new ArrayList<>());
        }
        for (int[] r : richer) {
            graph.get(r[1]).add(r[0]);
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int[] ans = new int[]{i};
            boolean[] visited = new boolean[n];
            visited[i] = true;
            dfs(graph, i, quiet, ans, visited);
            res[i] = ans[0];
        }
        return res;
    }

    private void dfs(Map<Integer, List<Integer>> graph, int start, int[] quiet,
                     int[] res, boolean[] visited) {
        if (quiet[start] <= quiet[res[0]]) {
            res[0] = start;
        }
        for (int r : graph.get(start)) {
            if (!visited[r]) {
                visited[r] = true;
                dfs(graph, r, quiet, res, visited);
                // visited[r] = false;
            }
        }
    }

    // Topological Sort + DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 54.21%(22 ms for 86 tests)
    public int[] loudAndRich2(int[][] richer, int[] quiet) {
        int n = quiet.length;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] r : richer) {
            graph[r[1]].add(r[0]);
        }
        int[] res = new int[n];
        Arrays.fill(res, -1);
        for (int i = 0; i < n; i++) {
            dfs(i, graph, quiet, res);
        }
        return res;
    }

    private int dfs(int cur, List<Integer>[] graph, int[] quiet, int[] res) {
        if (res[cur] < 0) {
            res[cur] = cur;
            for (int child : graph[cur]) {
                int cand = dfs(child, graph, quiet, res);
                if (quiet[cand] < quiet[res[cur]]) {
                    res[cur] = cand;
                }
            }
        }
        return res[cur];
    }

    // BFS + Queue
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 4.32%(387 ms for 86 tests)
    public int[] loudAndRich3(int[][] richer, int[] quiet) {
        int n = quiet.length;
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i <= n; i++) {
            graph.put(i, new ArrayList<>());
        }
        for (int[] r : richer) {
            graph.get(r[1]).add(r[0]);
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = bfs(graph, i, quiet);
        }
        return res;
    }

    private int bfs(Map<Integer, List<Integer>> graph, int start, int[] quiet) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        boolean[] visited = new boolean[quiet.length];
        visited[start] = true;
        int res = start;
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (quiet[cur] <= quiet[res]) {
                res = cur;
            }
            for (int r : graph.get(cur)) {
                if (!visited[r]) {
                    visited[r] = true;
                    queue.offer(r);
                }
            }
        }
        return res;
    }

    // Topological Sort + BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // beats 44.92%(25 ms for 86 tests)
    public int[] loudAndRich4(int[][] richer, int[] quiet) {
        int n = quiet.length;
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i <= n; i++) {
            graph.put(i, new ArrayList<>());
        }
        int[] indegrees = new int[n];
        for (int[] r : richer) {
            graph.get(r[0]).add(r[1]);
            indegrees[r[1]]++;
        }
        Queue<Integer> queue = new LinkedList<>();
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            if (indegrees[i] == 0) {
                queue.offer(i);
            }
            res[i] = i;
        }
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            for (int poorer : graph.get(cur)) {
                if (quiet[res[poorer]] > quiet[res[cur]]) {
                    res[poorer] = res[cur];
                }
                if (--indegrees[poorer] == 0) {
                    queue.offer(poorer);
                }
            }
        }
        return res;
    }

    void test(int[][] richer, int[] quiet, int[] expected) {
        assertArrayEquals(expected, loudAndRich(richer, quiet));
        assertArrayEquals(expected, loudAndRich2(richer, quiet));
        assertArrayEquals(expected, loudAndRich3(richer, quiet));
        assertArrayEquals(expected, loudAndRich4(richer, quiet));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 0 }, { 2, 1 }, { 3, 1 }, { 3, 7 }, { 4, 3 }, { 5, 3 }, { 6, 3 } },
                new int[] { 3, 2, 5, 4, 6, 1, 7, 0 }, new int[] { 5, 5, 2, 5, 4, 5, 6, 7 });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
