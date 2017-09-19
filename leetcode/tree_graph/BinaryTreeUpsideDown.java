import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC156: https://leetcode.com/problems/binary-tree-upside-down/
//
// Given a binary tree where all the right nodes are either leaf nodes with a sibling
// or empty, flip it upside down and turn it into a tree where the original right
// nodes turned into left leaf nodes. Return the new root.
public class BinaryTreeUpsideDown {
    // Recursion
    // beats 60.04%(0 ms for 144 tests)
    public TreeNode upsideDownBinaryTree(TreeNode root) {
        if (root == null || root.left == null) return root;

        TreeNode newRoot = upsideDownBinaryTree(root.left);
        root.left.right = root;
        root.left.left = root.right;
        root.right = null;
        root.left = null;
        return newRoot;
    }

    // beats 60.04%(0 ms for 144 tests)
    public TreeNode upsideDownBinaryTree2(TreeNode root) {
        TreeNode prev = null;
        for (TreeNode cur = root, right = null; cur != null; ) {
            // zigzag assignments
            TreeNode next = cur.left;
            cur.left = right;
            right = cur.right;
            cur.right = prev;
            prev = cur;
            cur = next;
        }
        return prev;
    }

    void test(Function<TreeNode, TreeNode> upsideDown, String s, String expected) {
        assertEquals(TreeNode.of(expected), upsideDown.apply(TreeNode.of(s)));
    }

    void test(String s, String expected) {
        BinaryTreeUpsideDown b = new BinaryTreeUpsideDown();
        test(b::upsideDownBinaryTree, s, expected);
        test(b::upsideDownBinaryTree2, s, expected);
    }

    @Test
    public void test1() {
        test("1", "1");
        test("2,4,5", "4,5,2");
        test("1,2,3,4,5", "4,5,2,#,#,3,1");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BinaryTreeUpsideDown");
    }
}
