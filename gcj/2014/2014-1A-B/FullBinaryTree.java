import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/2984486/dashboard#s=p1
// Round 1A 2014: Problem B - Full Binary Tree
//
// Given a tree G with N nodes, you are allowed to delete some of the nodes.
// When a node is deleted, the edges connected to the deleted node are also
// deleted. Your task is to delete as few nodes as possible so that the
// remaining nodes form a full binary tree for some choice of the root from the
// remaining nodes.
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. The first line of each test case contains a single integer N, the
// number of nodes in the tree. The following N-1 lines each one will contain
// two space-separated integers: Xi Yi, indicating that G contains an undirected
// edge between Xi and Yi.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number (starting from 1) and y is the minimum number of nodes to
// delete from G to make a full binary tree.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ Xi, Yi ≤ N
// Small dataset
// 2 ≤ N ≤ 15.
// Large dataset
// 2 ≤ N ≤ 1000.
public class FullBinaryTree {
    // Hash Table + Dynamic Programming + DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    public static int deleteToFullBinaryTree(int[][] edges) {
        if (edges.length == 1) return 1;

        List<Integer>[] graph = createGraph(edges);
        Map<Integer, Integer> dels = new HashMap<>();
        Map<Integer, Integer> childCounts = new HashMap<>();
        int res = edges.length;
        int i = -1;
        outer : for (List<Integer> nei : graph) {
            if (++i == 0 || nei.size() == 1) continue;

            res = Math.min(res, delete(i, 0, graph, dels, childCounts));
        }
        return res;
    }

    private static List<Integer>[] createGraph(int[][] edges) {
        int n = edges.length + 1;
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            int a = edge[0];
            int b = edge[1];
            graph[a].add(b);
            graph[b].add(a);
        }
        return graph;
    }

    private static int delete(int cur, int parent, List<Integer>[] graph,
                              Map<Integer, Integer> dels,
                              Map<Integer, Integer> childCount) {
        List<Integer> nei = graph[cur];
        int size = nei.size();
        if (size <= 1) return 0; // leaf

        int key = parent * graph.length + cur;
        int res = dels.getOrDefault(key, Integer.MAX_VALUE);
        if (res != Integer.MAX_VALUE) return res; // cached

        int kids = countChild(cur, parent, graph, childCount);
        if (size == 2 && parent > 0) { // remove all children
            dels.put(key, kids);
            return kids;
        }
        for (int i = 0; i < size; i++) {
            int a = nei.get(i);
            if (a == parent) continue;

            for (int j = i + 1; j < size; j++) {
                int b = nei.get(j);
                if (b == parent) continue;
                // choose 2 children and discard all others
                int del = delete(a, cur, graph, dels, childCount)
                          + delete(b, cur, graph, dels, childCount);
                int delChild = kids - countChild(a, cur, graph, childCount)
                               - countChild(b, cur, graph, childCount) - 2;
                res = Math.min(res, del + delChild);
            }
        }
        dels.put(key, res);
        return res;
    }

    private static int countChild(int cur, int parent, List<Integer>[] graph,
                                  Map<Integer, Integer> childCount) {
        int key = parent * graph.length + cur;
        int count = childCount.getOrDefault(key, -1);
        if (count >= 0) return count;

        count = 0;
        for (int nei : graph[cur]) {
            if (nei == parent) continue;

            count += 1 + countChild(nei, cur, graph, childCount);
        }
        childCount.put(key, count);
        return count;
    }

    // Dynamic Programming + DFS + Recursion
    // time complexity: O(N), space complexity: O(N ^ 2)
    public static int deleteToFullBinaryTree_2(int[][] edges) {
        int n = edges.length + 1;
        if (n == 2) return 1;

        List<Integer>[] graph = createGraph(edges);
        int[][] dels = new int[n + 1][n + 1];
        for (int[] a : dels) {
            Arrays.fill(a, Integer.MAX_VALUE);
        }
        int[][] childCounts = new int[n + 1][n + 1];
        for (int[] a : childCounts) {
            Arrays.fill(a, -1);
        }
        int res = edges.length;
        int i = -1;
        outer : for (List<Integer> nei : graph) {
            if (++i == 0 || nei.size() == 1) continue;

            res = Math.min(res, delete(i, 0, graph, dels, childCounts));
        }
        return res;
    }

    private static int delete(int cur, int parent, List<Integer>[] graph,
                              int[][] dels, int[][] childCount) {
        List<Integer> nei = graph[cur];
        int size = nei.size();
        if (size <= 1) return 0; // leaf

        int res = dels[cur][parent];
        if (res != Integer.MAX_VALUE) return res; // cached

        int kids = countChild(cur, parent, graph, childCount);
        if (size == 2 && parent > 0) return dels[cur][parent] = kids;

        for (int i = 0; i < size; i++) {
            int a = nei.get(i);
            if (a == parent) continue;

            for (int j = i + 1; j < size; j++) {
                int b = nei.get(j);
                if (b == parent) continue;
                // choose 2 children and discard all others
                int del = delete(a, cur, graph, dels, childCount)
                          + delete(b, cur, graph, dels, childCount);
                int delChild = kids - countChild(a, cur, graph, childCount)
                               - countChild(b, cur, graph, childCount) - 2;
                res = Math.min(res, del + delChild);
            }
        }
        return dels[cur][parent] = res;
    }

    private static int countChild(int cur, int parent, List<Integer>[] graph,
                                  int[][] childCount) {
        int count = childCount[cur][parent];
        if (count >= 0) return count;

        count = 0;
        for (int nei : graph[cur]) {
            if (nei == parent) continue;

            count += 1 + countChild(nei, cur, graph, childCount);
        }
        return childCount[cur][parent] = count;
    }

    // Dynamic Programming + DFS + Recursion
    // time complexity: O(N), space complexity: O(N ^ 2)
    public static int deleteToFullBinaryTree2(int[][] edges) {
        List<Integer>[] graph = createGraph(edges);
        int n = edges.length + 1;
        int[][] dp = new int[n + 1][n + 1];
        for (int[] a : dp) {
            Arrays.fill(a, -1);
        }
        int max = 0;
        for (int i = 1; i <= n; i++) {
            max = Math.max(max, maxSubtree(i, 0, dp, graph));
        }
        return n - max;
    }

    private static int maxSubtree(int cur, int parent, int[][] dp,
                                  List<Integer>[] graph) {
        if (dp[cur][parent] >= 0) return dp[cur][parent];

        int max1 = -1;
        int max2 = -1;
        for (int nei : graph[cur]) {
            if (nei == parent) continue;

            int v = maxSubtree(nei, cur, dp, graph);
            if (v > max1) {
                max2 = max1;
                max1 = v;
            } else if (v > max2) {
                max2 = v;
            }
        }
        return dp[cur][parent] = 1 + (max2 < 0 ? 0 : max1 + max2);
    }

    // TODO: top3 algorithm(linear complexity) as in
    // https://code.google.com/codejam/contest/2984486/dashboard#s=a&a=1

    void test(int[][] edges, int expected) {
        assertEquals(expected, deleteToFullBinaryTree(edges));
        assertEquals(expected, deleteToFullBinaryTree_2(edges));
        assertEquals(expected, deleteToFullBinaryTree2(edges));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2}}, 1);
        test(new int[][] {{4, 5}, {4, 2}, {1, 2}, {3, 1}, {6, 4}, {3, 7}}, 2);
        test(new int[][] {{2, 1}, {1, 3}}, 0);
        test(new int[][] {{1, 2}, {2, 3}, {3, 4}}, 1);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n",
                       clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int[][] edges = new int[N - 1][2];
        for (int i = 0; i < N - 1; i++) {
            edges[i] = new int[] {in.nextInt(), in.nextInt()};
        }
        out.println(deleteToFullBinaryTree2(edges));
    }
}
