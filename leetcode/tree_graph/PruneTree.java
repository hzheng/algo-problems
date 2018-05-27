import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC814: https://leetcode.com/problems/binary-tree-pruning/
//
// We are given the head node root of a binary tree, where additionally every
// node's value is either a 0 or a 1.
// Return the same tree where every subtree not containing a 1 has been removed.
public class PruneTree {
    // Recursion + DFS
    // beats %(3 ms for 28 tests)
    public TreeNode pruneTree(TreeNode root) {
        if (root == null) return null;

        root.left = pruneTree(root.left);
        root.right = pruneTree(root.right);
        return (root.left == null && root.right == null && root.val == 0)
               ? null : root;
    }

    void test(String root, String expected) {
        assertEquals(TreeNode.of(expected), pruneTree(TreeNode.of(root)));
    }

    @Test
    public void test() {
        test("1,0,1,0,0,0,1", "1,#,1,#,1");
        test("1,#,0,0,1", "1,#,0,#,1");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
