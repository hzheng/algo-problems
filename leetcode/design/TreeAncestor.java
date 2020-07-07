import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1483: https://leetcode.com/problems/kth-ancestor-of-a-tree-node/
//
// You are given a tree with n nodes numbered from 0 to n-1 in the form of a parent array where
// parent[i] is the parent of node i. The root of the tree is node 0.
// Implement the function getKthAncestor(int node, int k) to return the k-th ancestor of the given
// node. If there is no such ancestor, return -1.
// The k-th ancestor of a tree node is the k-th node in the path from that node to the root.
public class TreeAncestor {
    // Recursion + DFS + Brute Force
    // 2505 ms(0%), 75.8 MB(100.00%) for 10 tests
    static class TreeAncestor1 {
        static class Node {
            int val;
            Node parent;
            int level;

            public Node(int val) {
                this.val = val;
            }

            public Node(int val, Node parent) {
                this.val = val;
                this.parent = parent;
                this.level = parent.level + 1;
            }
        }

        private Node[] nodes;

        public TreeAncestor1(int n, int[] parent) {
            nodes = new Node[n];
            nodes[0] = new Node(0);
            for (int i = 1; i < n; i++) {
                getNode(parent, i);
            }
        }

        private Node getNode(int[] parent, int cur) {
            if (nodes[cur] != null) { return nodes[cur]; }

            Node parentNode = getNode(parent, parent[cur]);
            return nodes[cur] = new Node(cur, parentNode);
        }

        public int getKthAncestor(int node, int k) {
            Node cur = nodes[node];
            if (cur.level < k) { return -1; }

            Node p = cur;
            for (int i = k; i > 0; i--) {
                p = p.parent;
            }
            return p.val;
        }
    }

    // Binary Lifting + Dynamic Programming
    // 93 ms(100%), 86 MB(100.00%) for 10 tests
    static class TreeAncestor2 {
        private final int[][] ancestors;

        public TreeAncestor2(int n, int[] parent) {
            final int MAX_LEVEL = (int)(Math.log(n) / Math.log(2)) + 1;
            ancestors =
                    new int[MAX_LEVEL][n]; // ancestors(i, j) means ancestor of 2^i for j-th node
            ancestors[0] = parent;
            for (int i = 1; i < MAX_LEVEL; i++) {
                for (int j = 0; j < n; j++) {
                    int prevLevel = ancestors[i - 1][j];
                    ancestors[i][j] = (prevLevel == -1) ? -1 : ancestors[i - 1][prevLevel];
                }
            }
        }

        // time complexity: O(log(K)), space complexity: O(N * log(N))
        public int getKthAncestor(int node, int k) {
            for (int i = 0, j = k; j > 0 && node >= 0; j >>>= 1, i++) {
                if ((j & 1) == 1) {
                    node = ancestors[i][node];
                }
            }
            return node;
        }

        public int getKthAncestor2(int node, int k) {
            for (int i = this.ancestors.length, step = k; step > 0 && node >= 0; ) {
                int jump = 1 << i;
                if (step >= jump) {
                    node = ancestors[i][node];
                    step -= jump;
                } else {
                    i--;
                }
            }
            return node;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "getKthAncestor", "getKthAncestor", "getKthAncestor"},
             new Object[][] {new Object[] {7, new int[] {-1, 0, 0, 1, 1, 2, 2}}, {3, 1}, {5, 2},
                             {6, 3}}, new Integer[] {null, 1, 0, -1});

        test(new String[] {className, "getKthAncestor", "getKthAncestor", "getKthAncestor",
                           "getKthAncestor", "getKthAncestor", "getKthAncestor", "getKthAncestor",
                           "getKthAncestor", "getKthAncestor", "getKthAncestor"},
             new Object[][] {new Object[] {10, new int[] {-1, 0, 0, 1, 2, 0, 1, 3, 6, 1}}, {8, 6},
                             {9, 7}, {1, 1}, {2, 5}, {4, 2}, {7, 3}, {3, 7}, {9, 6}, {3, 5},
                             {8, 8}}, new Integer[] {null, -1, -1, 0, -1, 0, 0, -1, -1, -1, -1});
    }

    void test(String[] methods, Object[][] args, Integer[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("TreeAncestor1");
            test1("TreeAncestor2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
