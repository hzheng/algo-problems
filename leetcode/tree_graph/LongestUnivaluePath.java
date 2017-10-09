import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC687: https ://leetcode.com/problems/longest-univalue-path/description/
//
// Given a binary tree, find the length of the longest path where each node in
// the path has the same value.This path may or may not pass through the root.
// Note: The length of path between two nodes is represented by the number of
// edges between them .
public class LongestUnivaluePath {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // beats 35.72%(18 ms for 68 tests)
    public int longestUnivaluePath(TreeNode root) {
        if (root == null) return 0;

        int[] res = longestPath(root);
        return Math.max(res[0] + res[1], res[2]);
    }

    private int[] longestPath(TreeNode root) {
        int[] res = new int[3]; // max left, max right, max
        if (root.left != null) {
            int[] left = longestPath(root.left);
            if (root.left.val == root.val) {
                res[0] = Math.max(left[0], left[1]) + 1;
            }
            res[2] = Math.max(left[0] + left[1], left[2]);
        }
        if (root.right != null) {
            int[] right = longestPath(root.right);
            if (root.right.val == root.val) {
                res[1] = Math.max(right[0], right[1]) + 1;
            }
            res[2] = Math.max(res[2], Math.max(right[0] + right[1], right[2]));
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // beats 76.29%(14 ms for 68 tests)
    public int longestUnivaluePath2(TreeNode root) {
        int[] res = new int[1];
        arrowLength(root, res);
        return res[0];
    }

    private int arrowLength(TreeNode node, int[] res) {
        if (node == null) return 0;

        int left = arrowLength(node.left, res);
        int arrowLeft = (node.left != null && node.left.val == node.val)
                        ? left + 1 : 0;
        int right = arrowLength(node.right, res);
        int arrowRight = (node.right != null && node.right.val == node.val)
                         ? right + 1 : 0;
        res[0] = Math.max(res[0], arrowLeft + arrowRight);
        return Math.max(arrowLeft, arrowRight);
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // beats 6.54%(31 ms for 68 tests)
    public int longestUnivaluePath3(TreeNode root) {
        if (root == null) return 0;

        int maxSide = Math.max(longestUnivaluePath3(root.left),
                               longestUnivaluePath3(root.right));
        return Math.max(maxSide,
                        extend(root.left, root.val)
                        + extend(root.right, root.val));
    }

    private int extend(TreeNode root, int val) {
        return (root == null || root.val != val)
               ? 0
               : 1 + Math.max(extend(root.left, val), extend(root.right, val));
    }

    void test(String tree, int expected) {
        TreeNode root = TreeNode.of(tree);
        assertEquals(expected, longestUnivaluePath(root));
        assertEquals(expected, longestUnivaluePath2(root));
        assertEquals(expected, longestUnivaluePath3(root));
    }

    @Test
    public void test() {
        test("5,4,5,1,1,#,5", 2);
        test("1,4,5,4,4,#,5", 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
