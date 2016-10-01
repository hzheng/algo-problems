import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC098: https://leetcode.com/problems/validate-binary-search-tree/
//
// Given a binary tree, determine if it is a valid binary search tree (BST).
public class ValidBst {
    // Divide & Conquer(Recursion)
    // beats 31.71%(1 ms)
    public boolean isValidBST(TreeNode root) {
        if (root == null) return true;

        if (root.left != null) {
            TreeNode n = root.left;
            while (n.right != null) {
                n = n.right;
            }
            if (n != null && n.val >= root.val) return false;
        }

        if (root.right != null) {
            TreeNode n = root.right;
            while (n.left != null) {
                n = n.left;
            }
            if (n != null && n.val <= root.val) return false;
        }
        return isValidBST(root.left) && isValidBST(root.right);
    }

    // Solution of Choice
    // Divide & Conquer(Recursion)
    // beats 31.71%(1 ms)
    public boolean isValidBST2(TreeNode root) {
        // return isValidBST2(root, null, null);
        // return isValidBST2(root, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        return isValidBST2(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValidBST2(TreeNode root, long min, long max) {
        if (root == null) return true;

        if (root.val <= min || root.val >= max) return false;

        return (isValidBST2(root.left, min, root.val))
               && isValidBST2(root.right, root.val, max);
    }

    // Queue
    // http://www.programcreek.com/2012/12/leetcode-validate-binary-search-tree-java/
    // beats 6.33%(5 ms)
    public boolean isValidBST3(TreeNode root) {
        if (root == null) return true;

        Queue<BoundedNode> queue = new LinkedList<>();
        queue.add(new BoundedNode(root, Long.MIN_VALUE, Long.MAX_VALUE));
        while (!queue.isEmpty()) {
            BoundedNode n = queue.poll();
            if (n.node.val <= n.min || n.node.val >= n.max) return false;

            if (n.node.left != null) {
                queue.offer(new BoundedNode(n.node.left, n.min, n.node.val));
            }
            if (n.node.right != null) {
                queue.offer(new BoundedNode(n.node.right, n.node.val, n.max));
            }
        }
        return true;
    }

    static class BoundedNode {
        TreeNode node;
        long min;
        long max;
        BoundedNode(TreeNode node, long min, long max) {
            this.node = node;
            this.min = min;
            this.max = max;
        }
    }

    // Stack
    // beats 6.33%(5 ms)
    public boolean isValidBST4(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode last = null;
        for (TreeNode n = root; n != null || !stack.empty(); ) {
            if (n != null) {
                stack.push(n);
                n = n.left;
            } else {
                TreeNode top = stack.pop();
                if (last != null && last.val >= top.val) return false;

                last = top;
                n = top.right;
            }
        }
        return true;
    }

    // Divide & Conquer(Recursion)
    // beats 23.49%(3 ms)
    public boolean isValidBST5(TreeNode root) {
        return validate(root, new TreeNode[1]);
    }

    private boolean validate(TreeNode node, TreeNode[] prev) {
        if (node == null) return true;

        if (!validate(node.left, prev)
            || (prev[0] != null && prev[0].val >= node.val)) {
            return false;
        }

        prev[0] = node;
        return validate(node.right, prev);
    }

    void test(Function<TreeNode, Boolean> valid, String name,
              String s, boolean expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, valid.apply(TreeNode.of(s)));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(String s, boolean expected) {
        ValidBst v = new ValidBst();
        test(v::isValidBST, "isValidBST", s, expected);
        test(v::isValidBST2, "isValidBST2", s, expected);
        test(v::isValidBST3, "isValidBST3", s, expected);
        test(v::isValidBST4, "isValidBST4", s, expected);
        test(v::isValidBST5, "isValidBST5", s, expected);
    }

    @Test
    public void test1() {
        test("0,#,1", true);
        test("1,#,3,2", true);
        test("1,#,2,3", false);
        test("1,1", false);
        test("1,#,1", false);
        test("10,5,15,#,#,6,20", false);
        test("2147483647", true);
        test("-2147483648", true);
        test("2147483647,2147483647", false);
        test("6,4,8,3,5,7,9,2", true);
        test("6,4,8,3,5,7,9,2,#,1", false);
        test("6,4,8,3,5,7,9,2,#,#,#,#,#,#,#,1", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidBst");
    }
}
