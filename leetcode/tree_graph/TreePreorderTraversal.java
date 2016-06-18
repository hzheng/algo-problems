import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/binary-tree-preorder-traversal/
//
// Given a binary tree, return the preorder traversal of its nodes' values.
public class TreePreorderTraversal {
    // beats 55.79%
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        preorderTraversal(root, res);
        return res;
    }

    private void preorderTraversal(TreeNode root, List<Integer> res) {
        if (root == null) return;

        res.add(root.val);
        preorderTraversal(root.left, res);
        preorderTraversal(root.right, res);
    }

    // beats 1.61%
    public List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode n = stack.pop();
            res.add(n.val);
            if (n.right != null) {
                stack.push(n.right);
            }
            if (n.left != null) {
                stack.push(n.left);
            }
        }
        return res;
    }

    // beats 1.61%
    public List<Integer> preorderTraversal3(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> res = new ArrayList<>();
        for (TreeNode n = root; n != null || !stack.empty(); ) {
            if (n != null) {
                stack.push(n);
                res.add(n.val);
                n = n.left;
            } else {
                n = stack.pop().right;
            }
        }
        return res;
    }

    // beats 55.79%
    public List<Integer> preorderTraversal4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        List<Integer> left = preorderTraversal4(root.left);
        List<Integer> right = preorderTraversal4(root.right);
        res.add(root.val);
        res.addAll(left);
        res.addAll(right);
        return res;
    }

    // TODO: Morris traversal

    void test(Function<TreeNode, List<Integer> > traversal,
              String s, Integer ... expected) {
        TreeNode root = TreeNode.of(s);
        assertArrayEquals(expected,
                          traversal.apply(root).toArray(new Integer[0]));
    }

    void test(String s, Integer ... expected) {
        TreePreorderTraversal t = new TreePreorderTraversal();
        test(t::preorderTraversal, s, expected);
        test(t::preorderTraversal2, s, expected);
        test(t::preorderTraversal3, s, expected);
        test(t::preorderTraversal4, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", 1, 2, 3);
        test("1,2,3,#,#,4,#,#,5", 1, 2, 3, 4, 5);
        test("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15",
             1, 2, 4, 8, 9, 5, 10, 11, 3, 6, 12, 13, 7, 14, 15);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreePreorderTraversal");
    }
}
