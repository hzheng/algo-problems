import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/binary-tree-postorder-traversal/
//
// Given a binary tree, return the postorder traversal of its nodes' values.
public class TreePostorderTraversal {
    // Recursion
    // beats 71.07%(1 ms)
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

    // Stack
    // beats 7.00%(2 ms)
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

    // Stack(modify input)
    // beats 7.00%(2 ms)
    public List<Integer> postorderTraversal3(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode n = stack.peek();
            if (n.left == null && n.right == null) {
                res.add(stack.pop().val);
            } else {
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

    // Stack
    // beats 52.08%(1 ms)
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

    // Stack
    // beats 52.08%(1 ms)
    public List<Integer> postorderTraversal5(TreeNode root) {
        LinkedList<Integer> res = new LinkedList<>();
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode node = root; node != null; ) {
            res.addFirst(node.val); // or: res.add(0, node.val);
            if (node.left != null) {
                stack.push(node.left);
            }
            node = node.right;
            if (node == null && !stack.isEmpty()) {
                node = stack.pop();
            }
        }
        return res;
    }

    // Solution of Choice
    // Stack(weakness: not actually postorder, just reverse list)
    // beats 52.08%(1 ms)
    public List<Integer> postorderTraversal6(TreeNode root) {
        LinkedList<Integer> res = new LinkedList<>();
        Stack<TreeNode> stack = new Stack<>();
        for (TreeNode n = root; n != null || !stack.empty(); ) {
            if (n != null) {
                stack.push(n);
                res.addFirst(n.val);
                n = n.right;
            } else {
                n = stack.pop().left;
            }
        }
        return res;
    }

    // Stack
    // beats 7.00%(2 ms)
    public List<Integer> postorderTraversal7(TreeNode root) {
        LinkedList<Integer> res = new LinkedList<>();
        if (root == null) return res;

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.empty()) {
            TreeNode n = stack.pop();
            res.addFirst(n.val);
            if (n.left != null) {
                stack.push(n.left);
            }
            if (n.right != null) {
                stack.push(n.right);
            }
        }
        return res;
    }

    // Solution of Choice
    // Morris traversal
    // beats 52.08%(1 ms)
    public List<Integer> postorderTraversal8(TreeNode root) {
        List<Integer> res = new LinkedList<>();
        TreeNode dummy = new TreeNode(0);
        dummy.left = root;
        for (TreeNode cur = dummy, prev; cur != null; ) {
            if (cur.left == null) {
                cur = cur.right;
            } else {
                for (prev = cur.left; prev.right != null && prev.right != cur;
                     prev = prev.right) {}
                if (prev.right == null) {
                    prev.right = cur; // create threaded link
                    cur = cur.left;
                } else {
                    addReversely(cur.left, prev, res);
                    prev.right = null; // recover the tree
                    cur = cur.right;
                }
            }
        }
        return res;
    }

    private void reverse(TreeNode start, TreeNode end) {
        for (TreeNode p = start, q = start.right, next; p != end; p = q, q = next) {
            next = q.right;
            q.right = p;
        }
    }

    private void addReversely(TreeNode start, TreeNode end, List<Integer> res) {
        reverse(start, end);
        for (TreeNode node = end; ; node = node.right) {
            res.add(node.val);
            if (node == start) break;
        }
        reverse(end, start);
    }

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
        test(t::postorderTraversal6, s, expected);
        test(t::postorderTraversal7, s, expected);
        test(t::postorderTraversal8, s, expected);
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
