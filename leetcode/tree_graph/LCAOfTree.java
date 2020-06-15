import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC236: https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
//
// Given a binary tree, find the lowest common ancestor of two nodes.
public class LCAOfTree {
    // Stack
    // beats 55.04%(8 ms for 31 tests)
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (true) {
            if (cur != null) {
                stack.push(cur);
                if (cur == p || cur == q) { break; }

                cur = cur.left;
            } else {
                cur = stack.pop().right;
            }
        }
        TreeNode other = (cur == p) ? q : p;
        while (true) {
            cur = stack.pop();
            if (cur == root) { return root; }

            if (search(cur, other)) { return cur; }
        }
    }

    private boolean search(TreeNode root, TreeNode p) {
        return root != null && (root == p || search(root.left, p) || search(root.right, p));
    }

    // Solution of Choice
    // Recursion
    // beats 99.87%(6 ms for 31 tests)
    public TreeNode lowestCommonAncestor2(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) { return root; }

        TreeNode left = lowestCommonAncestor2(root.left, p, q);
        TreeNode right = lowestCommonAncestor2(root.right, p, q);
        return (left == null) ? right : ((right == null) ? left : root);
    }

    // Stack + Hash Table
    // beats 9.26%(22 ms for 31 tests)
    public TreeNode lowestCommonAncestor3(TreeNode root, TreeNode p, TreeNode q) {
        Stack<TreeNode> stack = new Stack<>();
        Map<TreeNode, TreeNode> parent = new HashMap<>();
        parent.put(root, null);
        stack.push(root);
        while (!parent.containsKey(p) || !parent.containsKey(q)) {
            TreeNode node = stack.pop();
            if (node.left != null) {
                parent.put(node.left, node);
                stack.push(node.left);
            }
            if (node.right != null) {
                parent.put(node.right, node);
                stack.push(node.right);
            }
        }
        Set<TreeNode> ancestors = new HashSet<>();
        for (; p != null; p = parent.get(p)) {
            ancestors.add(p);
        }
        for (; !ancestors.contains(q); q = parent.get(q)) {}
        return q;
    }

    // Binary Lifting + Dynamic Programming
    // https://cp-algorithms.com/graph/lca_binary_lifting.html
    // good for repetitive query
    // 48 ms(5.79%), 44 MB(17.38%) for 31 tests
    public TreeNode lowestCommonAncestor4(TreeNode root, TreeNode p, TreeNode q) {
        int total = count(root);
        int level = (int)(Math.log(total) / Math.log(2)) + 1;
        Map<TreeNode, Integer> inOrder = new HashMap<>();
        Map<TreeNode, Integer> outOrder = new HashMap<>();
        Map<TreeNode, List<TreeNode>> ancestors = new HashMap<>();
        dfs(root, root, level, ancestors, inOrder, outOrder, new int[1]);
        return lca(p, q, level, ancestors, inOrder, outOrder);
    }

    private int count(TreeNode node) {
        if (node == null) { return 0; }

        return 1 + count(node.left) + count(node.right);
    }

    private TreeNode lca(TreeNode p, TreeNode q, int level, Map<TreeNode, List<TreeNode>> ancestors,
                         Map<TreeNode, Integer> inOrder, Map<TreeNode, Integer> outOrder) {
        if (isAncestor(p, q, inOrder, outOrder)) { return p; }
        if (isAncestor(q, p, inOrder, outOrder)) { return q; }

        for (int i = level; i >= 0; i--) {
            TreeNode ancestor = ancestors.get(p).get(i);
            if (!isAncestor(ancestor, q, inOrder, outOrder)) {
                p = ancestor;
            }
        }
        return ancestors.get(p).get(0);
    }

    boolean isAncestor(TreeNode p, TreeNode q, Map<TreeNode, Integer> inOrder,
                       Map<TreeNode, Integer> outOrder) {
        return inOrder.get(p) <= inOrder.get(q) && outOrder.get(p) >= outOrder.get(q);
    }

    private void dfs(TreeNode node, TreeNode parent, int level,
                     Map<TreeNode, List<TreeNode>> ancestors, Map<TreeNode, Integer> inOrder,
                     Map<TreeNode, Integer> outOrder, int[] order) {
        inOrder.put(node, ++order[0]);
        List<TreeNode> ancestor = ancestors.computeIfAbsent(node, x -> new ArrayList<>());
        ancestor.add(parent);
        for (int i = 1; i <= level; i++) {
            ancestor.add(ancestors.get(ancestor.get(i - 1)).get(i - 1));
        }
        if (node.left != null) {
            dfs(node.left, node, level, ancestors, inOrder, outOrder, order);
        }
        if (node.right != null) {
            dfs(node.right, node, level, ancestors, inOrder, outOrder, order);
        }
        outOrder.put(node, ++order[0]);
    }

    @FunctionalInterface interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    TreeNode find(TreeNode root, int x) {
        if (root == null) { return null; }
        if (root.val == x) { return root; }

        TreeNode res = find(root.left, x);
        return (res != null) ? res : find(root.right, x);
    }

    void test(Function<TreeNode, TreeNode, TreeNode, TreeNode> lca, String s, int p, int q,
              int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, lca.apply(root, find(root, p), find(root, q)).val);
    }

    void test(String s, int p, int q, int expected) {
        LCAOfTree l = new LCAOfTree();
        test(l::lowestCommonAncestor, s, p, q, expected);
        test(l::lowestCommonAncestor2, s, p, q, expected);
        test(l::lowestCommonAncestor3, s, p, q, expected);
        test(l::lowestCommonAncestor4, s, p, q, expected);
    }

    @Test public void test1() {
        test("3,5,1,6,2,0,8,#,#,7,4", 5, 1, 3);
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
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
