import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC236: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
//
// Given a binary tree, find the lowest common ancestor of two nodes.
public class LCAOfTree {
    // beats 18.09%(16 ms)
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (true) {
            if (cur != null) {
                stack.push(cur);
                if (cur == p || cur == q) break;

                cur = cur.left;
            } else {
                cur = stack.pop().right;
            }
        }
        TreeNode other = (cur == p) ? q : p;
        while (true) {
            cur = stack.pop();
            if (cur == root) return root;

            if (search(cur, other)) return cur;
        }
    }

    private boolean search(TreeNode root, TreeNode p) {
        if (root == null) return false;

        return root == p || search(root.left, p) || search(root.right, p);
    }

    // Solution of Choice
    // recursion
    // https://discuss.leetcode.com/topic/18561/4-lines-c-java-python-ruby/51
    // beats 99.14%(11 ms)
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;

        TreeNode left = lowestCommonAncestor2(root.left, p, q);
        TreeNode right = lowestCommonAncestor2(root.right, p, q);
        return (left == null) ? right : ((right == null) ? left : root);
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
        LCAOfTree l = new LCAOfTree();
        test(l::lowestCommonAncestor, s, p, q, expected);
        test(l::lowestCommonAncestor2, s, p, q, expected);
    }

    @Test
    public void test1() {
        test("37,-34,-48,#,-100,-100,48,#,#,#,#,-54,#,-71,-22,#,#,#,8", -71, 48, 48);
        test("9,-1,-4,10,3,#,#,#,5", 3, 5, -1);
        test("1,#,2,#,3", 1, 3, 1);
        test("2,#,1", 1, 2, 2);
        test("2,#,1", 2, 1, 2);
        test("6,2,8,0,4,7,9,#,#,3,5", 2, 4, 2);
        test("6,2,8,0,4,7,9,#,#,3,5", 2, 8, 6);
        test("-1,0,3,-2,4,#,#,8", 8, 4, 0);
        test("-64,12,18,-4,-53,#,76,#,-51,#,#,-93,3,#,-31,47,#,3,53,-81,33,4,"
             + "#,-51,-44,-60,11,#,#,#,#,78,#,-35,-64,26,-81,-31,27,60,74,#,#,8,"
             + "-38,47,12,-24,#,-59,-49,-11,-51,67,#,#,#,#,#,#,#,-67,#,-37,-19,"
             + "10,-55,72,#,#,#,-70,17,-4,#,#,#,#,#,#,#,3,80,44,-88,-91,#,48,-90,"
             + "-30,#,#,90,-34,37,#,#,73,-38,-31,-85,-31,-96,#,#,-18,67,34,72,#,"
             + "-17,-77,#,56,-65,-88,-53,#,#,#,-33,86,#,81,-42,#,#,98,-40,70,-26,"
             + "24,#,#,#,#,92,72,-27,#,#,#,#,#,#,-67,#,#,#,#,#,#,#,-54,-66,-36,#,"
             + "-72,#,#,43,#,#,#,-92,-1,-98,#,#,#,#,#,#,#,39,-84,#,#,#,#,#,#,#,#,"
             + "#,#,#,#,#,-93,#,#,#,98", 74, -40, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LCAOfTree");
    }
}
