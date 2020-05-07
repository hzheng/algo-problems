import common.TreeNode;

import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1377: https://leetcode.com/problems/frog-position-after-t-seconds/
//
// Given an undirected tree consisting of n vertices numbered from 1 to n. A frog starts jumping
// from the vertex 1. In one second, the frog jumps from its current vertex to another unvisited
// vertex if they are directly connected. The frog can not jump back to a visited vertex. In case
// the frog can jump to several vertices it jumps randomly to one of them with the same probability,
// otherwise, when it can not jump to any unvisited vertex it jumps forever on the same vertex.
// The edges of the undirected tree are given in the array edges, where edges[i] = [fromi, toi]
// means that exists an edge connecting directly the vertices fromi and toi.
//
// Return the probability that after t seconds the frog is on the vertex target.
// 1 <= n <= 100
// edges.length == n-1
// edges[i].length == 2
// 1 <= edges[i][0], edges[i][1] <= n
// 1 <= t <= 50
// 1 <= target <= n
//Answers within 10^-5 of the actual value will be accepted as correct.
public class FrogPosition {
    // Queue + BFS + Hash Table + Set
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(51.21%), 40.0 MB(100%) for 203 tests
    public double frogPosition(int n, int[][] edges, int t, int target) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new ArrayList<>()).add(e[1]);
            graph.computeIfAbsent(e[1], x -> new ArrayList<>()).add(e[0]);
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {1, 1});
        Set<Integer> visited = new HashSet<>();
        visited.add(1);
        for (int rest = t; rest >= 0; rest--) {
            for (int i = queue.size(); i > 0; i--) {
                int[] cur = queue.poll();
                int node = cur[0];
                int possibilities = cur[1];
                List<Integer> children = graph.getOrDefault(node, Collections.emptyList());
                int branches = 0;
                for (int child : children) {
                    branches += visited.contains(child) ? 0 : 1;
                }
                if (node == target && (branches == 0 || rest == 0)) { return 1d / possibilities; }

                for (int child : children) {
                    if (visited.add(child)) {
                        queue.offer(new int[] {child, possibilities * branches});
                    }
                }
            }
        }
        return 0;
    }

    // Queue + BFS + Set
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(90.16%), 39.3 MB(100%) for 203 tests
    public double frogPosition2(int n, int[][] edges, int t, int target) {
        @SuppressWarnings("unchecked") List<Integer>[] graph = new List[n + 1];
        for (int i = n; i > 0; i--) {
            graph[i] = new ArrayList<>();
        }
        for (int[] e : edges) {
            graph[e[0]].add(e[1]);
            graph[e[1]].add(e[0]);
        }
        boolean[] visited = new boolean[n + 1];
        visited[1] = true;
        double[] probabilities = new double[n + 1];
        probabilities[1] = 1d;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        outer:
        for (int rest = t; !queue.isEmpty() && rest > 0; rest--) {
            for (int i = queue.size(); i > 0; i--) {
                int u = queue.poll();
                int branches = 0;
                for (int v : graph[u]) {
                    branches += visited[v] ? 0 : 1;
                }
                for (int v : graph[u]) {
                    if (!visited[v]) {
                        visited[v] = true;
                        queue.offer(v);
                        probabilities[v] = probabilities[u] / branches;
                    }
                }
                if (branches > 0) {
                    probabilities[u] = 0;
                }
                if (u == target) { break outer; }
            }
        }
        return probabilities[target];
    }

    // DFS + Recursion + Set
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(99.20%), 39.3 MB(100%) for 203 tests
    public double frogPosition3(int n, int[][] edges, int t, int target) {
        if (n == 1) {return 1.0; }

        @SuppressWarnings("unchecked") List<Integer>[] graph = new List[n + 1];
        for (int i = n; i > 0; i--) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }
        return dfs(graph, 1, t, target, new boolean[n + 1]);
    }

    private double dfs(List<Integer>[] graph, int node, int t, int target, boolean[] visited) {
        if (t == 0 || node != 1 && graph[node].size() == 1) {
            return (node == target) ? 1 : 0;
        }

        visited[node] = true;
        double res = 0.0;
        for (int child : graph[node]) {
            if (!visited[child]) {
                res += dfs(graph, child, t - 1, target, visited);
            }
        }
        return res / (graph[node].size() - (node == 1 ? 0 : 1));
    }

    @Test public void test() {
        test(1, new int[][] {}, 1, 1, 1);
        test(8, new int[][] {{2, 1}, {3, 2}, {4, 1}, {5, 1}, {6, 4}, {7, 1}, {8, 7}}, 7, 7,
             0.00000);
        test(3, new int[][] {{2, 1}, {3, 2}}, 1, 2, 1.0000);
        test(7, new int[][] {{1, 2}, {1, 3}, {1, 7}, {2, 4}, {2, 6}, {3, 5}}, 2, 4,
             0.16666666666666666);
        test(7, new int[][] {{1, 2}, {1, 3}, {1, 7}, {2, 4}, {2, 6}, {3, 5}}, 1, 7,
             0.3333333333333333);
        test(7, new int[][] {{1, 2}, {1, 3}, {1, 7}, {2, 4}, {2, 6}, {3, 5}}, 20, 6,
             0.16666666666666666);
    }

    private void test(int n, int[][] edges, int t, int target, double expected) {
        assertEquals(expected, frogPosition(n, edges, t, target), 0.00001);
        assertEquals(expected, frogPosition2(n, edges, t, target), 0.00001);
        assertEquals(expected, frogPosition3(n, edges, t, target), 0.00001);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
