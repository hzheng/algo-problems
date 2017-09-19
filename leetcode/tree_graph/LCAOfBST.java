import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC235: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
//
// Given a binary search tree, find the lowest common ancestor of two nodes.
public class LCAOfBST {
    // beats 44.22%(10 ms)
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // if (root == p || root == q) return root;
        if (root.val < p.val && root.val < q.val) {
            return lowestCommonAncestor(root.right, p, q);
        }
        if (root.val > p.val && root.val > q.val) {
            return lowestCommonAncestor(root.left, p, q);
        }
        return root;
    }

    // Solution of Choice
    // beats 44.22%(10 ms)
    // non-recursion
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        int min = Math.min(p.val, q.val);
        int max = Math.max(p.val, q.val);
        TreeNode n = root;
        while (true) {
            if (n.val < min) {
                n = n.right;
            } else if (n.val > max) {
                n = n.left;
            } else return n;
        }
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    TreeNode find(TreeNode root, int x) {
        if (root == null) return null;
        if (root.val == x) return root;

        TreeNode res = find(root.left, x);
        return (res != null) ? res : find(root.right, x);
    }

    void test(Function<TreeNode, TreeNode, TreeNode, TreeNode> lca,
              String s, int p, int q, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, lca.apply(root, find(root, p), find(root, q)).val);
    }

    void test(String s, int p, int q, int expected) {
        LCAOfBST l = new LCAOfBST();
        test(l::lowestCommonAncestor, s, p, q, expected);
        test(l::lowestCommonAncestor2, s, p, q, expected);
    }

    @Test
    public void test1() {
        test("6,2,8,0,4,7,9,#,#,3,5", 2, 8, 6);
        test("6,2,8,0,4,7,9,#,#,3,5", 2, 4, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LCAOfBST");
    }
}
