import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
public class SymmetricTree {
    // beats 23.80%
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;

        return isSymmetric(root.left, root.right);
        // or just: return isSymmetric(root, root);
    }

    private boolean isSymmetric(TreeNode p, TreeNode q) {
        if (p == null) return q == null;

        if (q == null) return p == null;

        return p.val == q.val && isSymmetric(p.right, q.left)
               && isSymmetric(p.left, q.right);
    }

    // from leetcode solution
    // beats 6.29%
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

    // beats 6.29%
    public boolean isSymmetric3(TreeNode root) {
        if (root == null) return true;

        Queue<TreeNode> left = new LinkedList<>();
        left.offer(root.left);

        Queue<TreeNode> right = new LinkedList<>();
        right.offer(root.right);

        while (!left.isEmpty() && !right.isEmpty()) {
            TreeNode l = left.poll();
            TreeNode r = right.poll();
            if (l == null && r == null) continue;

            if (l == null || r == null) return false;

            if (l.val != r.val) return false;

            left.add(l.left);
            left.add(l.right);
            right.add(r.right);
            right.add(r.left);
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
