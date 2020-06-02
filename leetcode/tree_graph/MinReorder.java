import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1466: https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero/
//
// There are n cities numbered from 0 to n-1 and n-1 roads such that there is only one way to travel
// between two different cities (this network form a tree). Last year, The ministry of transport
// decided to orient the roads in one direction because they are too narrow. Roads are represented
// by connections where connections[i] = [a, b] represents a road from city a to b.
// This year, there will be a big event in the capital (city 0), and many people want to travel to
// this city. Your task consists of reorienting some roads such that each city can visit the city 0.
// Return the minimum number of edges changed.
// It's guaranteed that each city can reach the city 0 after reorder.
// Constraints:
//
// 2 <= n <= 5 * 10^4
// connections.length == n-1
// connections[i].length == 2
// 0 <= connections[i][0], connections[i][1] <= n-1
// connections[i][0] != connections[i][1]
public class MinReorder {
    // BFS + Hash Table + Set
    // time complexity: O(N), space complexity: O(N)
    // 60 ms(60.07%), 61.1 MB(100%) for 69 tests
    public int minReorder(int n, int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            graph.computeIfAbsent(e[1], x -> new ArrayList<>()).add(-e[0]);
        }
        boolean[] visited = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        int res = 0;
        for (q.offer(0); !q.isEmpty(); ) {
            int cur = Math.abs(q.poll());
            visited[cur] = true;
            List<Integer> next = graph.getOrDefault(cur, Collections.emptyList());
            for (int node : next) {
                if (!visited[Math.abs(node)]) {
                    res += (node > 0) ? 1 : 0;
                    q.offer(node);
                }
            }
        }
        return res;
    }

    // DFS + Recursion + Hash Table + Set
    // time complexity: O(N), space complexity: O(N)
    // 51 ms(68.06%), 63.3 MB(100%) for 54 tests
    public int minReorder2(int n, int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            graph.computeIfAbsent(e[1], x -> new ArrayList<>()).add(-e[0]);
        }
        return dfs(graph, 0, new boolean[n]);
    }

    private int dfs(Map<Integer, List<Integer>> graph, int cur, boolean[] visited) {
        visited[cur] = true;
        int total = 0;
        List<Integer> next = graph.getOrDefault(cur, Collections.emptyList());
        for (int node : next) {
            if (!visited[Math.abs(node)]) {
                total += dfs(graph, Math.abs(node), visited) + (node > 0 ? 1 : 0);
            }
        }
        return total;
    }

    // DFS + Recursion + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 58 ms(65.01%), 62.6 MB(100%) for 54 tests
    public int minReorder3(int n, int[][] edges) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            graph.computeIfAbsent(e[1], x -> new ArrayList<>()).add(-e[0]);
        }
        return dfs(graph, 0, 0);
    }

    private int dfs(Map<Integer, List<Integer>> graph, int cur, int prev) {
        int total = 0;
        List<Integer> next = graph.getOrDefault(cur, Collections.emptyList());
        for (int node : next) {
            if (Math.abs(node) != prev) {
                total += dfs(graph, Math.abs(node), cur) + (node > 0 ? 1 : 0);
            }
        }
        return total;
    }

    @Test public void test() {
        test(6, new int[][] {{0, 1}, {1, 3}, {2, 3}, {4, 0}, {4, 5}}, 3);
        test(5, new int[][] {{1, 0}, {1, 2}, {3, 2}, {3, 4}}, 2);
        test(3, new int[][] {{1, 0}, {2, 0}}, 0);
    }

    private void test(int n, int[][] edges, int expected) {
        assertEquals(expected, minReorder(n, edges));
        assertEquals(expected, minReorder2(n, edges));
        assertEquals(expected, minReorder3(n, edges));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
