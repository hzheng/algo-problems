import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1080: https://leetcode.com/problems/insufficient-nodes-in-root-to-leaf-paths/
//
// Given the root of a binary tree, consider all root to leaf paths. A node is insufficient if every
// such root to leaf path intersecting this node has sum strictly less than limit.
// Delete all insufficient nodes simultaneously, and return the root of the resulting binary tree.
public class SufficientSubset {
    // Recursion
    // time complexity: O(N), space complexity: O(H)
    // 1 ms(100.00%), 38.8 MB(100%) for 116 tests
    public TreeNode sufficientSubset(TreeNode root, int limit) {
        TreeNode dummy = new TreeNode(0);
        dummy.left = root;
        dfs(root, dummy, limit, 0);
        return dummy.left;
    }

    private int dfs(TreeNode cur, TreeNode parent, int limit, int sum) {
        int res = cur.val;
        if (cur.left == null && cur.right == null) {
        } else if (cur.left == null && cur.right != null) {
            res += dfs(cur.right, cur, limit, sum + cur.val);
        } else if (cur.right == null && cur.left != null) {
            res += dfs(cur.left, cur, limit, sum + cur.val);
        } else {
            int leftVal = dfs(cur.left, cur, limit, sum + cur.val);
            int rightVal = dfs(cur.right, cur, limit, sum + cur.val);
            res += Math.max(leftVal, rightVal);
        }
        if (parent != null && res + sum < limit) {
            if (parent.left == cur) {
                parent.left = null;
            } else {
                parent.right = null;
            }
        }
        return res;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(H)
    // 1 ms(100.00%), 38.6 MB(100%) for 116 tests
    public TreeNode sufficientSubset2(TreeNode root, int limit) {
        return dfs(root, limit, 0) ? root : null;
    }

    private boolean dfs(TreeNode cur, int limit, int sum) {
        if (cur == null) {
            return false;
        }
        sum += cur.val;
        if (cur.left == null && cur.right == null) {
            return sum >= limit;
        }

        boolean leftRes = dfs(cur.left, limit, sum);
        boolean rightRes = dfs(cur.right, limit, sum);
        if (!leftRes) {
            cur.left = null;
        }
        if (!rightRes) {
            cur.right = null;
        }
        return leftRes || rightRes;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(H)
    // 1 ms(100.00%), 38.2 MB(100%) for 116 tests
    public TreeNode sufficientSubset3(TreeNode root, int limit) {
        if (root == null) {
            return null;
        }
        if (root.left == null && root.right == null) {
            return root.val < limit ? null : root;
        }
        root.left = sufficientSubset3(root.left, limit - root.val);
        root.right = sufficientSubset3(root.right, limit - root.val);
        return (root.left == root.right) ? null : root;
    }

    void test(String node, int limit, String expected) {
        assertEquals(TreeNode.of(expected), sufficientSubset(TreeNode.of(node), limit));
        assertEquals(TreeNode.of(expected), sufficientSubset2(TreeNode.of(node), limit));
        assertEquals(TreeNode.of(expected), sufficientSubset3(TreeNode.of(node), limit));
    }

    @Test
    public void test() {
        test("1,2,3,4,-99,-99,7,8,9,-99,-99,12,13,-99,14", 1, "1,2,3,4,#,#,7,8,9,#,14");
        test("5,4,8,11,#,17,4,7,1,#,#,5,3", 22, "5,4,8,11,#,17,4,7,#,#,#,5");
        test("1,2,-3,-5,#,4,#", -1, "1,#,-3,4");
        test("1,2,-3,-5,#,4,#", -1, "1,#,-3,4");
        test("2,7,2,#,8,#,#,#,4", 15, "2,7,#,#,8,#,4");
        test("10,5,10", 21, null);
        test("0,1,2,#,-1", 1, "0,#,2");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
