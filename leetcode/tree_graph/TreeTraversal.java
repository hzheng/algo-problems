import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC094: https://leetcode.com/problems/binary-tree-inorder-traversal/
//
// Given a binary tree, return the inorder traversal of its nodes' values.
public class TreeTraversal {
    // Recursion
    // beats 60.33%(1 ms)
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        inorderTraversal(root, res);
        return res;
    }

    private void inorderTraversal(TreeNode root, List<Integer> res) {
        if (root == null) return;

        inorderTraversal(root.left, res);
        res.add(root.val);
        inorderTraversal(root.right, res);
    }

    // beats 3.26%(2 ms)
    // shortcoming: changed input tree
    public List<Integer> inorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            if (node.left == null && node.right == null) {
                res.add(node.val);
                continue;
            }
            if (node.right != null) {
                stack.push(node.right);
                node.right = null;
            }
            stack.push(node);
            if (node.left != null) {
                stack.push(node.left);
                node.left = null;
            }
        }
        return res;
    }

    // beats 3.26%(2 ms)
    public List<Integer> inorderTraversal3(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> res = new ArrayList<>();
        for (TreeNode n = root; n != null || !stack.empty(); n = n.right) {
            while (n != null) {
                stack.push(n);
                n = n.left;
            }
            n = stack.pop();
            res.add(n.val);
        }
        return res;
    }

    // Stack
    // Solution of Choice
    // beats 56.06%(1 ms for 68 tests)
    public List<Integer> inorderTraversal4(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> res = new ArrayList<>();
        for (TreeNode cur = root; cur != null || !stack.empty(); ) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                res.add(cur.val);
                cur = cur.right;
            }
        }
        return res;
    }

    // shortcoming: changed input tree
    public List<Integer> inorderTraversal5(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode top = stack.peek();
            if (top.left != null) {
                stack.push(top.left);
                top.left = null;
            } else {
                res.add(top.val);
                stack.pop();
                if (top.right != null) {
                    stack.push(top.right);
                }
            }
        }
        return res;
    }

    // Morris traversal
    // time complexity: O(N), space complexity: O(1)
    // beats 60.33%(1 ms)
    public List<Integer> inorderTraversal6(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        for (TreeNode cur = root, prev; cur != null; ) {
            if (cur.left == null) {
                res.add(cur.val);
                cur = cur.right;
            } else {
                for (prev = cur.left; prev.right != null && prev.right != cur;
                     prev = prev.right) {}
                if (prev.right == null) {
                    prev.right = cur; // create threaded link
                    cur = cur.left;
                } else {
                    prev.right = null; // recover the tree
                    res.add(cur.val);
                    cur = cur.right;
                }
            }
        }
        return res;
    }

    void test(Function<TreeNode, List<Integer> > traversal,
              String s, Integer ... expected) {
        TreeNode root = TreeNode.of(s);
        assertArrayEquals(expected,
                          traversal.apply(root).toArray(new Integer[0]));
    }

    void test(String s, Integer ... expected) {
        TreeTraversal t = new TreeTraversal();
        test(t::inorderTraversal, s, expected);
        test(t::inorderTraversal2, s, expected);
        test(t::inorderTraversal3, s, expected);
        test(t::inorderTraversal4, s, expected);
        test(t::inorderTraversal5, s, expected);
        test(t::inorderTraversal6, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", 1, 3, 2);
        test("1,2,3,#,#,4,#,#,5", 2, 1, 4, 5, 3);
        test("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15",
             8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
