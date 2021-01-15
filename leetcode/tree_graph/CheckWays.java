import java.util.*;
import java.util.function.Function;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1719: https://leetcode.com/problems/number-of-ways-to-reconstruct-a-tree/
//
// You are given an array pairs, where pairs[i] = [xi, yi], and:
// There are no duplicates.
// xi < yi
// Let ways be the number of rooted trees that satisfy the following conditions:
// The tree consists of nodes whose values appeared in pairs.
// A pair [xi, yi] exists in pairs if and only if xi is an ancestor of yi or yi is an ancestor of xi.
// Note: the tree does not have to be a binary tree.
//
// Two ways are considered to be different if there is at least one node that has different parents
// in both ways.
// Return:
// 0 if ways == 0
// 1 if ways == 1
// 2 if ways > 1
//
// A rooted tree is a tree that has a single root node, and all edges are oriented to be outgoing
// from the root.
// An ancestor of a node is any node on the path from the root to that node (excluding the node
// itself). The root has no ancestors.
//
// Constraints:
// 1 <= pairs.length <= 10^5
// 1 <= xi < yi <= 500
// The elements in pairs are unique.
public class CheckWays {
    // Heap + Hash Table + Set
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 113 ms(83.93%), 91.2 MB(68.75%) for 81 tests
    public int checkWays(int[][] pairs) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int[] e : pairs) {
            graph.computeIfAbsent(e[0], x -> new HashSet<>()).add(e[1]);
            graph.computeIfAbsent(e[1], x -> new HashSet<>()).add(e[0]);
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (b[1] - a[1]));
        for (int v : graph.keySet()) {
            pq.offer(new int[] {v, graph.get(v).size()});
        }
        Set<Integer> visited = new HashSet<>();
        int res = 1;
        for (int totalNodes = pq.size(); !pq.isEmpty(); ) {
            int[] cur = pq.poll();
            int node = cur[0];
            int degree = cur[1];
            int parent = -1;
            int parentDegree = Integer.MAX_VALUE;
            for (int nei : graph.get(node)) {
                Set<Integer> nn = graph.get(nei);
                if (visited.contains(nei) && nn.size() < parentDegree && nn.size() >= degree) {
                    parent = nei;
                    parentDegree = nn.size();
                }
            }
            visited.add(node);
            if (parent < 0) {
                if (degree != totalNodes - 1) { return 0; }
                continue;
            }
            for (int nei : graph.get(node)) {
                if (nei != parent && !graph.get(parent).contains(nei)) { return 0; }
            }
            if (degree == parentDegree) {
                res = 2;
            }
        }
        return res;
    }

    // DFS + Recursion + Hash Table + Set
    // time complexity: O(N^2), space complexity: O(N)
    // 560 ms(36.48%), 83.2 MB(82.39%) for 81 tests
    public int checkWays2(int[][] pairs) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int[] e : pairs) {
            graph.computeIfAbsent(e[0], v -> new HashSet<>()).add(e[1]);
            graph.computeIfAbsent(e[1], v -> new HashSet<>()).add(e[0]);
        }
        return checkWays(graph, graph.keySet());
    }

    private int checkWays(Map<Integer, Set<Integer>> graph, Set<Integer> nodes) {
        List<Integer> roots = new ArrayList<>();
        for (int node : nodes) {
            if (graph.get(node).size() == nodes.size() - 1) {
                roots.add(node);
            }
        }
        int res = Math.min(roots.size(), 2);
        if (res == 0) { return 0; }

        for (int root : roots) { // remove all root candidates
            for (int node : graph.get(root)) {
                graph.get(node).remove(root);
            }
            nodes.remove(root);
        }
        Set<Integer> visited = new HashSet<>();
        for (int node : nodes) {
            if (!visited.add(node)) { continue; }

            Set<Integer> component = new HashSet<>(); // connected component
            dfs(component, node, graph, visited);
            int ways = checkWays(graph, component);
            if (ways == 0) { return 0; }

            res = Math.max(res, ways);
        }
        return res;
    }

    private void dfs(Set<Integer> component, int node, Map<Integer, Set<Integer>> graph,
                     Set<Integer> visited) {
        component.add(node);
        for (int nei : graph.get(node)) {
            if (visited.add(nei)) {
                dfs(component, nei, graph, visited);
            }
        }
    }

    private void test(int[][] pairs, int expected) {
        assertEquals(expected, checkWays(pairs));
        assertEquals(expected, checkWays2(pairs));
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {2, 3}}, 1);
        test(new int[][] {{1, 2}, {2, 3}, {1, 3}}, 2);
        test(new int[][] {{1, 2}, {2, 3}, {2, 4}, {1, 5}}, 0);
        test(new int[][] {{1, 2}}, 2);
        test(new int[][] {{3, 5}, {4, 5}, {2, 5}, {1, 5}, {1, 4}, {2, 4}}, 1);
        test(new int[][] {{5, 7}, {11, 12}, {2, 9}, {8, 10}, {1, 4}, {3, 6}}, 0);
    }

    private void test(Function<int[][], Integer> checkWays) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int[][] pairs = Utils.readInt2Array(scanner.nextLine());
                int res = checkWays.apply(pairs);
                int expected = Integer.parseInt(scanner.nextLine());
                assertEquals(expected, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test2() {
        CheckWays c = new CheckWays();
        test(c::checkWays);
        test(c::checkWays2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
