import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1938: https://leetcode.com/problems/maximum-genetic-difference-query/
//
// There is a rooted tree consisting of n nodes numbered 0 to n - 1. Each node's number denotes its
// unique genetic value (i.e. the genetic value of node x is x). The genetic difference between two
// genetic values is defined as the bitwise-XOR of their values. You are given the integer array
// parents, where parents[i] is the parent for node i. If node x is the root of the tree, then
// parents[x] == -1.
// You are also given the array queries where queries[i] = [nodei, vali]. For each query i, find
// the maximum genetic difference between vali and pi, where pi is the genetic value of any node
// that is on the path between nodei and the root (including nodei and the root). More formally,
// you want to maximize vali XOR pi.
// Return an array ans where ans[i] is the answer to the ith query.
//
// Constraints:
// 2 <= parents.length <= 10^5
// 0 <= parents[i] <= parents.length - 1 for every node i that is not the root.
// parents[root] == -1
// 1 <= queries.length <= 3 * 10^4
// 0 <= nodei <= parents.length - 1
// 0 <= vali <= 2 * 10^5
public class MaxGeneticDifference {
    private static final int MAX_BITS = (int)(Math.log10(100_000) / Math.log10(2)) + 1;

    // Trie + Recursion + DFS
    // time complexity: O((N+Q)*log(N)), space complexity: O(N)
    // 274 ms(34.76%), 146 MB(89.27%) for 58 tests
    public int[] maxGeneticDifference(int[] parents, int[][] queries) {
        List<List<Integer>> tree = new ArrayList<>();
        int n = parents.length;
        for (int i = 0; i < n; i++) {
            tree.add(new ArrayList<>());
        }
        int root = 0;
        for (int i = 0; i < n; i++) {
            if (parents[i] != -1) {
                tree.get(parents[i]).add(i);
            } else {
                root = i;
            }
        }
        int[] res = new int[queries.length];
        List<List<int[]>> queryList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            queryList.add(new ArrayList<>());
        }
        for (int i = queries.length - 1; i >= 0; i--) {
            queryList.get(queries[i][0]).add(new int[] {i, queries[i][1]});
        }
        dfs(res, root, tree, queryList, new Trie());
        return res;
    }

    private void dfs(int[] res, int cur, List<List<Integer>> tree,
                     List<List<int[]>> queries, Trie trie) {
        trie.change(cur, 1);
        for (int[] q : queries.get(cur)) {
            res[q[0]] = trie.maxXor(q[1]);
        }
        for (int nei : tree.get(cur)) {
            dfs(res, nei, tree, queries, trie);
        }
        trie.change(cur, -1);
    }

    private static class Trie {
        private final TrieNode root = new TrieNode();

        private static class TrieNode {
            int count;
            TrieNode[] child = new TrieNode[2];
        }

        public void change(int num, int delta) {
            TrieNode cur = root;
            for (int i = MAX_BITS; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (cur.child[bit] == null) {
                    cur.child[bit] = new TrieNode();
                }
                cur = cur.child[bit];
                cur.count += delta;
            }
        }

        public int maxXor(int num) {
            int res = 0;
            TrieNode cur = root;
            for (int i = MAX_BITS; i >= 0; --i) {
                int bit = (num >> i) & 1;
                TrieNode next = cur.child[bit ^ 1];
                if (next != null && next.count > 0) {
                    cur = next;
                    res |= (1 << i);
                } else {
                    cur = cur.child[bit];
                }
            }
            return res;
        }
    }

    private void test(int[] parents, int[][] queries, int[] expected) {
        assertArrayEquals(expected, maxGeneticDifference(parents, queries));
    }

    @Test public void test() {
        test(new int[] {-1, 0, 1, 1}, new int[][] {{0, 2}, {3, 2}, {2, 5}}, new int[] {2, 3, 7});
        test(new int[] {3, 7, -1, 2, 0, 7, 0, 2}, new int[][] {{4, 6}, {1, 15}, {0, 5}},
             new int[] {6, 14, 7});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
