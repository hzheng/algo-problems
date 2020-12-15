import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC998: https://leetcode.com/problems/maximum-binary-tree-ii/
//
// We are given the root node of a maximum tree: a tree where every node has a value greater than
// any other value in its subtree. Just as in the previous problem, the given tree was constructed
// from an list A (root = Construct(A)) recursively with the following Construct(A) routine:
// If A is empty, return null.
// Otherwise, let A[i] be the largest element of A.  Create a root node with value A[i].
// The left child of root will be Construct([A[0], A[1], ..., A[i-1]])
// The right child of root will be Construct([A[i+1], A[i+2], ..., A[A.length - 1]])
// Return root.
// Note that we were not given A directly, only a root node root = Construct(A).
// Suppose B is a copy of A with the value val appended to it. B has unique values.
// Return Construct(B).
//
// Constraints:
// 1 <= B.length <= 100
public class MaxBinaryTreeII {
    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 38 MB(6.32%) for 75 tests
    public TreeNode insertIntoMaxTree(TreeNode root, int val) {
        List<Integer> vals = unconstruct(root);
        vals.add(val);
        return construct(vals, 0, vals.size(), Math.max(root.val, val));
    }

    private TreeNode construct(List<Integer> vals, int start, int end, int max) {
        if (start == end || max == Integer.MIN_VALUE) { return null; }

        int i = start;
        TreeNode root;
        for (int curMax = Integer.MIN_VALUE; ; i++) {
            int val = vals.get(i);
            if (val == max) {
                root = new TreeNode(val);
                root.left = construct(vals, start, i, curMax);
                break;
            }
            curMax = Math.max(curMax, val);
        }
        int curMax = Integer.MIN_VALUE;
        for (int j = i + 1; j < end; j++) {
            curMax = Math.max(curMax, vals.get(j));
        }
        root.right = construct(vals, i + 1, end, curMax);
        return root;
    }

    private List<Integer> unconstruct(TreeNode root) {
        if (root == null) { return new ArrayList<>(); }

        List<Integer> res = unconstruct(root.left);
        res.add(root.val);
        res.addAll(unconstruct(root.right));
        return res;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 37 MB(32.71%) for 75 tests
    public TreeNode insertIntoMaxTree2(TreeNode root, int val) {
        TreeNode res = new TreeNode(val);
        if (root.val < val) {
            res.left = root;
            return res;
        }
        if (root.right == null) {
            root.right = res;
        } else {
            root.right = insertIntoMaxTree2(root.right, val);
        }
        return root;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 37 MB(32.71%) for 75 tests
    public TreeNode insertIntoMaxTree3(TreeNode root, int val) {
        if (root != null && root.val > val) {
            root.right = insertIntoMaxTree3(root.right, val);
            return root;
        }
        TreeNode res = new TreeNode(val);
        res.left = root;
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 37.3 MB(8.18%) for 75 tests
    public TreeNode insertIntoMaxTree4(TreeNode root, int val) {
        TreeNode res = new TreeNode(val);
        if (root.val < val) {
            res.left = root;
            return res;
        }

        TreeNode cur = root;
        for (; cur.right != null && cur.right.val > val; cur = cur.right) {}
        res.left = cur.right;
        cur.right = res;
        return root;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    void test(Function<TreeNode, Integer, TreeNode> insertIntoMaxTree, String s, int val,
              String expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(TreeNode.of(expected), insertIntoMaxTree.apply(root, val));
    }

    private void test(String s, int val, String expected) {
        MaxBinaryTreeII m = new MaxBinaryTreeII();
        test(m::insertIntoMaxTree, s, val, expected);
        test(m::insertIntoMaxTree2, s, val, expected);
        test(m::insertIntoMaxTree3, s, val, expected);
        test(m::insertIntoMaxTree4, s, val, expected);
    }

    @Test public void test() {
        test("4,1,3,#,#,2", 5, "5,4,#,1,3,#,#,2");
        test("5,2,4,#,1", 3, "5,2,4,#,1,#,3");
        test("5,2,3,#,1", 4, "5,2,4,#,1,3");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
