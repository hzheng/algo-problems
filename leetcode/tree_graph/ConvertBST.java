import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
// LC538: https://leetcode.com/problems/convert-bst-to-greater-tree/
//
// Given a Binary Search Tree (BST), convert it to a Greater Tree such that every
// key of the original BST is changed to the original key plus sum of all keys
// greater than the original key in BST.
public class ConvertBST {
    // DFS + Recursion
    // beats N/A(18 ms for 212 tests)
    public TreeNode convertBST(TreeNode root) {
        dfs(root, new int[1]);
        return root;
    }

    private void dfs(TreeNode root, int[] sum) {
        if (root == null) return;

        dfs(root.right, sum);
        sum[0] = root.val += sum[0];
        dfs(root.left, sum);
    }

    // DFS + Recursion
    // beats N/A(20 ms for 212 tests)
    public TreeNode convertBST2(TreeNode root) {
        dfs2(root, 0);
        return root;
    }

    private int dfs2(TreeNode root, int sum) {
        if (root == null) return sum;

        root.val += dfs2(root.right, sum);
        return dfs2(root.left, root.val);
    }

    void test(String s, String expected) {
        assertEquals(TreeNode.of(expected), convertBST(TreeNode.of(s)));
        assertEquals(TreeNode.of(expected), convertBST2(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("5,2,13", "18,20,13");
        test("5,2,13,1,4,10,20", "48,54,33,55,52,43,20");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConvertBST");
    }
}
