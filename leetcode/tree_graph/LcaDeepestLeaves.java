import java.util.*;
import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1123: https://leetcode.com/problems/lowest-common-ancestor-of-deepest-leaves/
//
// Given the root of a binary tree, return the lowest common ancestor of its deepest leaves.
// Note: This question is the same as 865:
// https://leetcode.com/problems/smallest-subtree-with-all-the-deepest-nodes/
//
// Constraints:
// The number of nodes in the tree will be in the range [1, 1000].
// 0 <= Node.val <= 1000
// The values of the nodes in the tree are unique.
public class LcaDeepestLeaves {
    // BFS + Queue + Hash Table + Set
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(6.84%), 39.2 MB(14.90%) for 81 tests
    public TreeNode lcaDeepestLeaves(TreeNode root) {
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<TreeNode> leaves = null;
        while (!queue.isEmpty()) {
            leaves = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode cur = queue.poll();
                leaves.add(cur);
                if (cur.left != null) {
                    queue.offer(cur.left);
                    parentMap.put(cur.left, cur);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                    parentMap.put(cur.right, cur);
                }
            }
        }
        if (leaves.size() == 1) { return leaves.get(0); }

        for (Set<TreeNode> nodes = new HashSet<>(leaves), next; ; nodes = next) {
            if (nodes.size() == 1) { return nodes.iterator().next(); }

            next = new HashSet<>();
            for (TreeNode node : nodes) {
                next.add(parentMap.get(node));
            }
        }
    }

    // DFS + Recursion + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(22.35%), 38.8 MB(40.40%) for 81 tests
    public TreeNode lcaDeepestLeaves2(TreeNode root) {
        Map<TreeNode, Integer> map = new HashMap<>();
        int depth = getDepth(root, 0, map);
        return lca(root, depth, map);
    }

    private TreeNode lca(TreeNode cur, int depth, Map<TreeNode, Integer> map) {
        boolean left = (map.getOrDefault(cur.left, 0) == depth);
        boolean right = (map.getOrDefault(cur.right, 0) == depth);
        if (left == right) { return cur; }

        return left ? lca(cur.left, depth, map) : lca(cur.right, depth, map);
    }

    private int getDepth(TreeNode cur, int depth, Map<TreeNode, Integer> map) {
        if (cur == null) { return depth - 1; }

        int leftDepth = getDepth(cur.left, depth + 1, map);
        int rightDepth = getDepth(cur.right, depth + 1, map);
        int res = Math.max(leftDepth, rightDepth);
        map.put(cur, res);
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 38.6 MB(81.16%) for 81 tests
    public TreeNode lcaDeepestLeaves3(TreeNode root) {
        int depth = getDepth(root, 0);
        return lca(root, 0, depth);
    }

    private TreeNode lca(TreeNode cur, int curDepth, int depth) {
        if (cur == null || curDepth == depth) { return cur; }

        TreeNode left = lca(cur.left, curDepth + 1, depth);
        TreeNode right = lca(cur.right, curDepth + 1, depth);
        return (left != null && right != null) ? cur : (left != null ? left : right);
    }

    private int getDepth(TreeNode cur, int depth) {
        if (cur == null) { return depth - 1; }

        int leftDepth = getDepth(cur.left, depth + 1);
        int rightDepth = getDepth(cur.right, depth + 1);
        return Math.max(leftDepth, rightDepth);
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 38.5 MB(89.52%) for 81 tests
    public TreeNode lcaDeepestLeaves4(TreeNode root) {
        TreeNode[] res = new TreeNode[1];
        lca(root, 0, new int[1], res);
        return res[0];
    }

    private int lca(TreeNode cur, int depth, int[] maxDepth, TreeNode[] res) {
        if (cur == null) { return depth; }

        int leftDepth = lca(cur.left, depth + 1, maxDepth, res);
        int rightDepth = lca(cur.right, depth + 1, maxDepth, res);
        int d = Math.max(leftDepth, rightDepth);
        maxDepth[0] = Math.max(maxDepth[0], d);
        if (maxDepth[0] == leftDepth && leftDepth == rightDepth) {
            res[0] = cur;
        }
        return d;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(H)
    // 0 ms(100.00%), 38.8 MB(40.40%) for 81 tests
    public TreeNode lcaDeepestLeaves5(TreeNode root) {
        return lca(root, 0).node;
    }

    private static class NodeWithDepth {
        TreeNode node;
        int depth;

        NodeWithDepth(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    private NodeWithDepth lca(TreeNode root, int depth) {
        if (root == null) { return new NodeWithDepth(null, depth); }

        NodeWithDepth left = lca(root.left, depth + 1);
        NodeWithDepth right = lca(root.right, depth + 1);
        if (left.depth == right.depth) { return new NodeWithDepth(root, left.depth); }

        return left.depth > right.depth ? left : right;
    }

    // DFS + Recursion + Hash Table
    // time complexity: O(N), space complexity: O(H)
    // 2 ms(22.35%), 38.9 MB(40.40%) for 81 tests
    public TreeNode lcaDeepestLeaves6(TreeNode root) {
        return dfs(root, new HashMap<>());
    }

    private TreeNode dfs(TreeNode cur, Map<TreeNode, Integer> heights) {
        if (cur == null) { return null; }

        int diff = height(cur.left, heights) - height(cur.right, heights);
        if (diff == 0) { return cur; }

        return dfs(diff > 0 ? cur.left : cur.right, heights);
    }

    private int height(TreeNode root, Map<TreeNode, Integer> heights) {
        if (root == null) { return 0; }

        int res = heights.getOrDefault(root, 0);
        if (res > 0) { return res; }

        res = 1 + Math.max(height(root.left, heights), height(root.right, heights));
        heights.put(root, res);
        return res;
    }

    void test(Function<TreeNode, TreeNode> lcaDeepestLeaves, String s, String expected) {
        assertEquals(TreeNode.of(expected), lcaDeepestLeaves.apply(TreeNode.of(s)));
    }

    private void test(String s, String expected) {
        LcaDeepestLeaves l = new LcaDeepestLeaves();
        test(l::lcaDeepestLeaves, s, expected);
        test(l::lcaDeepestLeaves2, s, expected);
        test(l::lcaDeepestLeaves3, s, expected);
        test(l::lcaDeepestLeaves4, s, expected);
        test(l::lcaDeepestLeaves5, s, expected);
        test(l::lcaDeepestLeaves6, s, expected);
    }

    @Test public void test() {
        test("3,5,1,6,2,0,8,#,#,7,4", "2,7,4");
        test("1", "1");
        test("0,1,3,#,2", "2");
        test("1,2,#,3,4,#,6,#,5", "2,3,4,#,6,#,5");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
