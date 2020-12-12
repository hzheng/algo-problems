import java.util.*;

import common.TreeNode;

import org.junit.Test;

import static org.junit.Assert.*;

// LC993: https://leetcode.com/problems/cousins-in-binary-tree/
//
// In a binary tree, the root node is at depth 0, and children of each depth k node are at depth k+1.
// Two nodes of a binary tree are cousins if they have the same depth, but have different parents.
// We are given the root of a binary tree with unique values, and the values x and y of two
// different nodes in the tree.
// Return true if and only if the nodes corresponding to the values x and y are cousins.
//
// Constraints:
// The number of nodes in the tree will be between 2 and 100.
// Each node has a unique integer value from 1 to 100.
public class CousinsInBinaryTree {
    private static final int MAX = 1000;

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.8 MB(46.05%) for 181 tests
    public boolean isCousins(TreeNode root, int x, int y) {
        int p1 = getParent(null, root, x, 0);
        int p2 = getParent(null, root, y, 0);
        return (p1 / MAX != p2 / MAX) && (p1 % MAX == p2 % MAX);
    }

    private int getParent(TreeNode parent, TreeNode cur, int x, int level) {
        if (cur == null) { return -1; }
        if (cur.val == x) {
            return (parent == null ? 0 : parent.val) * MAX + level;
        }
        int res = getParent(cur, cur.left, x, level + 1);
        if (res < 0) {
            res = getParent(cur, cur.right, x, level + 1);
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.4 MB(6.53%) for 181 tests
    public boolean isCousins2(TreeNode root, int x, int y) {
        int[] res = new int[2];
        parentWithDepth(res, root, x, y, 1);
        return (res[0] / MAX != res[1] / MAX) && (res[0] % MAX == res[1] % MAX);
    }

    private void parentWithDepth(int[] res, TreeNode cur, int x, int y, int code) {
        if (cur == null) { return; }

        if (cur.val == x) {
            res[0] = code;
        } else if (cur.val == y) {
            res[1] = code;
        }
        if (res[0] == 0 || res[1] == 0) {
            parentWithDepth(res, cur.left, x, y, cur.val * MAX + code % MAX + 1);
            parentWithDepth(res, cur.right, x, y, cur.val * MAX + code % MAX + 1);
        }
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.8 MB(46.05%) for 181 tests
    public boolean isCousins3(TreeNode root, int x, int y) {
        Queue<TreeNode[]> queue = new LinkedList<>();
        queue.offer(new TreeNode[] {null, root});
        for (TreeNode parent = null; parent == null && !queue.isEmpty(); ) {
            for (int size = queue.size(); size > 0; size--) {
                TreeNode[] head = queue.poll();
                TreeNode cur = head[1];
                if (cur.val == x || cur.val == y) {
                    if (parent != null) { return parent != head[0]; }

                    parent = head[0];
                }
                if (cur.left != null) {
                    queue.offer(new TreeNode[] {cur, cur.left});
                }
                if (cur.right != null) {
                    queue.offer(new TreeNode[] {cur, cur.right});
                }
            }
        }
        return false;
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.8 MB(46.05%) for 181 tests
    public boolean isCousins4(TreeNode root, int x, int y) {
        Queue<TreeNode> queue = new LinkedList<>();
        int found = 0;
        for (queue.offer(root); found == 0 && !queue.isEmpty(); ) {
            for (int size = queue.size(); size > 0; size--) {
                TreeNode cur = queue.poll();
                if (cur.val == x || cur.val == y) {
                    found++;
                }
                if (cur.left != null && cur.right != null && (
                        (cur.left.val == x && cur.right.val == y) || (cur.left.val == y
                                                                      && cur.right.val == x))) {
                    return false;
                }
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
        }
        return found == 2;
    }

    private void test(String s, int x, int y, boolean expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, isCousins(root, x, y));
        assertEquals(expected, isCousins2(root, x, y));
        assertEquals(expected, isCousins3(root, x, y));
        assertEquals(expected, isCousins4(root, x, y));
    }

    @Test public void test() {
        test("1,2,3,#,4,7,#,5,8,#,#,#,6,#,#,#,9,#,10", 8, 2, false);
        test("1,2,3,4", 4, 3, false);
        test("1,2,3,#,4,#,5", 5, 4, true);
        test("1,2,3,#,4", 2, 3, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
