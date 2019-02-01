import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC968: https://leetcode.com/problems/binary-tree-cameras/
//
// Given a binary tree, we install cameras on the nodes of the tree. Each camera
// at a node can monitor its parent, itself, and its immediate children.
// Calculate the minimum number of cameras needed to monitor all nodes.
public class MinCameraCover {
    // Recursion
    // time complexity: O(N), space complexity: O(H)
    // beats 88.36%(6 ms for 170 tests)
    public int minCameraCover(TreeNode root) {
        int[] res = minCover(root);
        return Math.min(res[1], res[2]);
    }

    // 0: All nodes below this are covered, but not this one
    // 1: All nodes below and including this are covered - no camera
    // 2: All nodes below this are covered, with a camera on this node
    private int[] minCover(TreeNode node) {
        if (node == null) return new int[] {0, 0, Integer.MAX_VALUE / 2};

        int[] L = minCover(node.left);
        int[] R = minCover(node.right);
        int minL12 = Math.min(L[1], L[2]);
        int minR12 = Math.min(R[1], R[2]);
        return new int[] {L[1] + R[1], Math.min(L[2] + minR12, R[2] + minL12),
                          1 + Math.min(L[0], minL12) + Math.min(R[0], minR12)};
    }

    // Recursion
    // time complexity: O(N), space complexity: O(H)
    // beats 88.36%(6 ms for 170 tests)
    public int minCameraCover2(TreeNode root) {
        int[] res = new int[1];
        return (dfs(root, res) < 1 ? 1 : 0) + res[0];
    }

    // 0: All nodes below this are covered, but not this one
    // 1: All nodes below and including this are covered - no camera
    // 2: All nodes below this are covered, with a camera on this node
    private int dfs(TreeNode root, int[] res) {
        if (root == null) return 1;

        int left = dfs(root.left, res);
        int right = dfs(root.right, res);
        if (left == 0 || right == 0) {
            res[0]++;
            return 2;
        }
        return (left == 2 || right == 2) ? 1 : 0;
    }

    // Recursion + Greedy + Set
    // time complexity: O(N), space complexity: O(H)
    // beats 21.92%(9 ms for 170 tests)
    public int minCameraCover3(TreeNode root) {
        Set<TreeNode> covered = new HashSet<>();
        covered.add(null);
        int[] res = new int[1];
        dfs(root, null, covered, res);
        return res[0];
    }

    private void dfs(TreeNode node, TreeNode parent, Set<TreeNode> covered, int[] res) {
        if (node == null) return;

        dfs(node.left, node, covered, res);
        dfs(node.right, node, covered, res);
        if (!covered.contains(node.left) || !covered.contains(node.right)
            || parent == null && !covered.contains(node)) {
            res[0]++;
            covered.add(node);
            covered.add(parent);
            covered.add(node.left);
            covered.add(node.right);
        }
    }

    void test(String tree, int expected) {
        assertEquals(expected, minCameraCover(TreeNode.of(tree)));
        assertEquals(expected, minCameraCover2(TreeNode.of(tree)));
        assertEquals(expected, minCameraCover3(TreeNode.of(tree)));
    }

    @Test
    public void test() {
        test("0", 1);
        test("1,2,#,3,4", 1);
        test("1,2,#,3,#,4,#,#,5", 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
