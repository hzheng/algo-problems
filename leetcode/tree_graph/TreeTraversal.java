import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree, return the inorder traversal of its nodes' values.
public class TreeTraversal {
    // beats 60.33%
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

    // beats 3.26%
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

    // beats 3.26%
    // http://www.jiuzhang.com/solutions/binary-tree-inorder-traversal/
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

    // beats 3.26%
    // http://www.programcreek.com/2012/12/leetcode-solution-of-binary-tree-inorder-traversal-in-java/
    public List<Integer> inorderTraversal4(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> res = new ArrayList<>();
        for (TreeNode n = root; n != null || !stack.empty(); ) {
            if (n != null) {
                stack.push(n);
                n = n.left;
            } else {
                TreeNode top = stack.pop();
                res.add(top.val);
                n = top.right;
            }
        }
        return res;
    }

    public List<Integer> inorderTraversal5(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<TreeNode>();
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
    }

    @Test
    public void test1() {
        test("1,#,2,3", 1, 3, 2);
        test("1,2,3,#,#,4,#,#,5", 2, 1, 4, 5, 3);
        test("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15",
             8, 4, 9, 2, 10, 5, 11, 1, 12, 6, 13, 3, 14, 7, 15);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeTraversal");
    }
}
