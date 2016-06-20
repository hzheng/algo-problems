import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/binary-tree-postorder-traversal/
//
// Given a binary tree, return the postorder traversal of its nodes' values.
public class TreePostorderTraversal {
    // beats 71.07%
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        postorderTraversal(root, res);
        return res;
    }

    private void postorderTraversal(TreeNode root, List<Integer> res) {
        if (root == null) return;

        postorderTraversal(root.left, res);
        postorderTraversal(root.right, res);
        res.add(root.val);
    }

    // beats 7.51%
    public List<Integer> postorderTraversal2(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        List<Integer> res = new ArrayList<>();
        for (TreeNode n = root; n != null || !stack.isEmpty(); ) {
            if (n != null) {
                stack.push(n);
                n = n.left;
            } else {
                n = stack.peek();
                if (n.right != null) {
                    n = n.right;
                    continue;
                }
                res.add(n.val);
                stack.pop();
                while (!stack.isEmpty()) {
                    if (stack.peek().right != n) {
                        n = stack.peek().right;
                        break;
                    }
                    n = stack.pop();
                    res.add(n.val);
                }
                n = null;
            }
        }
        return res;
    }

    // beats 7.51%
    // modify input
    public List<Integer> postorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode n = stack.peek();
            if (n.left == null && n.right == null) {
                res.add(stack.pop().val);
            }
            else {
                if (n.right != null) {
                    stack.push(n.right);
                    n.right = null;
                }
                if (n.left != null) {
                    stack.push(n.left);
                    n.left = null;
                }
            }
        }
        return res;
    }

    // beats 7.51%
    public List<Integer> postorderTraversal4(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        TreeNode prev = null;
        while (!stack.isEmpty()) {
            TreeNode cur = stack.peek();
            if (cur.left == null && cur.right == null
                || prev != null && (prev == cur.left || prev == cur.right)) {
                res.add(stack.pop().val);
                prev = cur;
            } else {
                if (cur.right != null) {
                    stack.push(cur.right);
                }
                if (cur.left != null) {
                    stack.push(cur.left);
                }
            }
        }
        return res;
    }

    // beats 71.01%
    public List<Integer> postorderTraversal5(TreeNode root) {
        LinkedList<Integer> res = new LinkedList<>();
        Stack<TreeNode> leftChildren = new Stack<>();
        for (TreeNode node = root; node != null; ) {
            // res.add(0, node.val); // beat rate will drop to 7.51%
            res.addFirst(node.val);
            if (node.left != null) {
                leftChildren.push(node.left);
            }
            node = node.right;
            if (node == null && !leftChildren.isEmpty()) {
                node = leftChildren.pop();
            }
        }
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
        TreePostorderTraversal t = new TreePostorderTraversal();
        test(t::postorderTraversal, s, expected);
        test(t::postorderTraversal2, s, expected);
        test(t::postorderTraversal3, s, expected);
        test(t::postorderTraversal4, s, expected);
        test(t::postorderTraversal5, s, expected);
    }

    @Test
    public void test1() {
        test("1,2,3,4,5,6,7", 4, 5, 2, 6, 7, 3, 1);
        test("1,#,2,3", 3, 2, 1);
        test("1", 1);
        test("1,2,3,#,#,4,#,#,5", 2, 5, 4, 3, 1);
        test("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15",
             8, 9, 4, 10, 11, 5, 2, 12, 13, 6, 14, 15, 7, 3, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreePostorderTraversal");
    }
}
