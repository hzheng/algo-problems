import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1448: https://leetcode.com/problems/count-good-nodes-in-binary-tree/
//
// Given a binary tree root, a node X in the tree is named good if in the path from root to X there
// are no nodes with a value greater than X.
// Return the number of good nodes in the binary tree.
//
// Constraints:
// The number of nodes in the binary tree is in the range [1, 10^5].
// Each node's value is between [-10^4, 10^4].
public class GoodNodes {
    // Recursion
    // 3 ms(16.50%), 56.3 MB(5.89%) for 63 tests
    public int goodNodes(TreeNode root) {
        int[] res = new int[1];
        dfs(root, root.val, res);
        return res[0];
    }

    private void dfs(TreeNode root, int max, int[] res) {
        if (root == null) {return;}

        if (max <= root.val) {
            res[0]++;
            max = root.val;
        }
        dfs(root.left, max, res);
        dfs(root.right, max, res);
    }

    // Recursion
    // 2 ms(96.54%), 47.9 MB(55.03%) for 63 tests
    public int goodNodes2(TreeNode root) {
        return dfs(root, Integer.MIN_VALUE);
    }

    private int dfs(TreeNode root, int max) {
        if (root == null) {return 0;}

        int nextMax = Math.max(max, root.val);
        return ((max <= root.val) ? 1 : 0) + dfs(root.left, nextMax) + dfs(root.right, nextMax);
    }

    void test(String s, int expected) {
        assertEquals(expected, goodNodes(TreeNode.of(s)));
        assertEquals(expected, goodNodes2(TreeNode.of(s)));
    }

    @Test public void test() {
        test("3,1,4,3,#,1,5", 4);
        test("3,3,#,4,2", 3);
        test("1", 1);
        test("3,11,4,3,8,#,9,6,2,#,1,5,7", 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
