import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC101: https://leetcode.com/problems/symmetric-tree/
//
// Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
public class SymmetricTree {
    // Recursion
    // beats 23.80%(1 ms)
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;

        return isSymmetric(root.left, root.right);
        // or just: return isSymmetric(root, root);
    }

    private boolean isSymmetric(TreeNode p, TreeNode q) {
        if (p == null || q == null)  return p == q;

        return p.val == q.val && isSymmetric(p.right, q.left)
               && isSymmetric(p.left, q.right);
    }

    // Solution of Choice
    // from leetcode solution
    // beats 6.29%(3 ms)
    public boolean isSymmetric2(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode n1 = queue.poll();
            TreeNode n2 = queue.poll();
            if (n1 == null && n2 == null) continue;

            if (n1 == null || n2 == null) return false;

            if (n1.val != n2.val) return false;

            queue.offer(n1.left);
            queue.offer(n2.right);
            queue.offer(n1.right);
            queue.offer(n2.left);
        }
        return true;
    }

    // Solution of Choice
    // Stack
    // beats 6.29%(3 ms)
    public boolean isSymmetric3(TreeNode root) {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode n1 = stack.pop();
            TreeNode n2 = stack.pop();
            if (n1 == null && n2 == null) continue;

            if (n1 == null || n2 == null) return false;

            if (n1.val != n2.val) return false;

            stack.push(n1.left);
            stack.push(n2.right);
            stack.push(n1.right);
            stack.push(n2.left);
        }
        return true;
    }

    void test(Function<TreeNode, Boolean> sym, String s, boolean expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, sym.apply(root));
    }

    void test(String s, boolean expected) {
        SymmetricTree t = new SymmetricTree();
        test(t::isSymmetric, s, expected);
        test(t::isSymmetric2, s, expected);
        test(t::isSymmetric3, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", false);
        test("1,2,2", true);
        test("1,2,2,3", false);
        test("1,2,2,3,4,4,3", true);
        test("1,2,2,3,4,4,3,1", false);
        test("1,2,3", false);
        test("1,2,2,3,4,4,3",  true);
        test("1,2,2,3,4,4,3,#,#,5,#,#,5", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SymmetricTree");
    }
}
