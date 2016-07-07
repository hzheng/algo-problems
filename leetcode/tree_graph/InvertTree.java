import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/invert-binary-tree/
//
// Invert a binary tree.
public class InvertTree {
    // beats 18.38%(0 ms)
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;

        TreeNode tmp = root.left;
        root.left = invertTree(root.right);
        root.right = invertTree(tmp);
        return root;
    }

    // beats 0.64%(1 ms)
    public TreeNode invertTree2(TreeNode root) {
        if (root == null) return null;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            TreeNode tmp = node.left;
            node.left = node.right;
            node.right = tmp;
            if (node.right != null) {
                queue.offer(node.right);
            }
            if (node.left != null) {
                queue.offer(node.left);
            }
        }
        return root;
    }

    void test(Function<TreeNode, TreeNode> invert, String s, String expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, invert.apply(root).toString());
    }

    void test(String s, String expected) {
        InvertTree i = new InvertTree();
        test(i::invertTree, s, expected);
        test(i::invertTree2, s, expected);
    }

    @Test
    public void test1() {
        test("1", "{1}");
        test("4,2,7,1,3,6,9", "{4,7,2,9,6,3,1}");
        test("1,2,3,4,#,5,6", "{1,3,2,6,5,#,4}");
        test("1,2,3,4,5,6,7,8,9,10,11,12,13",
             "{1,3,2,7,6,5,4,#,#,13,12,11,10,9,8}");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("InvertTree");
    }
}
