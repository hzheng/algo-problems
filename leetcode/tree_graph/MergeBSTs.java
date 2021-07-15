import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.TreeNode;

// LC1932: https://leetcode.com/problems/merge-bsts-to-create-single-bst/
//
// You are given n BST (binary search tree) root nodes for n separate BSTs stored in an array trees
// (0-indexed). Each BST in trees has at most 3 nodes, and no two roots have the same value. In one
// operation, you can:
// Select two distinct indices i and j such that the value stored at one of the leaves of trees[i]
// is equal to the root value of trees[j].
// Replace the leaf node in trees[i] with trees[j].
// Remove trees[j] from trees.
// Return the root of the resulting BST if it is possible to form a valid BST after performing n - 1
// operations, or null if it is impossible to create a valid BST.
//
// Constraints:
// n == trees.length
// 1 <= n <= 5 * 10^4
// The number of nodes in each tree is in the range [1, 3].
// No two roots of trees have the same value.
// All the trees in the input are valid BSTs.
// 1 <= TreeNode.val <= 5 * 10^4.
public class MergeBSTs {
    // Recursion + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 61 ms(81.21%), 64.4 MB(89.31%) for 474 tests
    public TreeNode canMerge(List<TreeNode> trees) {
        Map<Integer, TreeNode> roots = new HashMap<>();
        Map<Integer, Integer> cnt = new HashMap<>();
        for (TreeNode root : trees) {
            roots.put(root.val, root);
            cnt.put(root.val, cnt.getOrDefault(root.val, 0) + 1);
            if (root.left != null) {
                cnt.put(root.left.val, cnt.getOrDefault(root.left.val, 0) + 1);
            }
            if (root.right != null) {
                cnt.put(root.right.val, cnt.getOrDefault(root.right.val, 0) + 1);
            }
        }
        for (TreeNode root : trees) {
            if (cnt.get(root.val) != 1) { continue; }

            return check(root, roots, 0, Integer.MAX_VALUE) && roots.size() == 1 ? root : null;
        }
        return null;
    }

    private boolean check(TreeNode node, Map<Integer, TreeNode> roots, int min, int max) {
        if (node == null) { return true; }
        if (node.val <= min || node.val >= max) { return false; }

        if (node.left == node.right) { // leaf
            TreeNode root = roots.get(node.val);
            if (root != null && root != node) { // merge point found
                node.left = root.left;
                node.right = root.right;
                roots.remove(node.val);
            }
        }
        return check(node.left, roots, min, node.val) && check(node.right, roots, node.val, max);
    }

    // Recursion + Hash Table + Set
    // time complexity: O(N), space complexity: O(N)
    // 66 ms(73.41%), 66.4 MB(77.17%) for 474 tests
    public TreeNode canMerge2(List<TreeNode> trees) {
        Map<Integer, TreeNode> roots = new HashMap<>();
        Set<Integer> vals = new HashSet<>();
        for (TreeNode root : trees) {
            roots.put(root.val, root);
            vals.add(root.val);
        }
        List<TreeNode> leaves = new ArrayList<>();
        for (TreeNode root : trees) {
            addLeaf(root.left, leaves, roots, vals);
            addLeaf(root.right, leaves, roots, vals);
        }
        for (TreeNode leaf : leaves) {
            TreeNode root = roots.get(leaf.val);
            if (root == null) { return null; }

            leaf.left = root.left;
            leaf.right = root.right;
            roots.remove(leaf.val);
        }
        if (roots.size() != 1) { return null; }

        TreeNode root = roots.values().iterator().next();
        return validNodes(root, 0, Integer.MAX_VALUE) == vals.size() ? root : null;
    }

    private void addLeaf(TreeNode node, List<TreeNode> leaves, Map<Integer, TreeNode> roots,
                         Set<Integer> vals) {
        if (node != null) {
            vals.add(node.val);
            if (roots.containsKey(node.val)) {
                leaves.add(node);
            }
        }
    }

    private int validNodes(TreeNode node, int min, int max) {
        if (node == null || node.val <= min || node.val >= max) { return 0; }

        return 1 + validNodes(node.left, min, node.val) + validNodes(node.right, node.val, max);
    }

    private void test(String[] treeStrings, String expected) {
        List<TreeNode> trees = new ArrayList<>();
        for (String s : treeStrings) {
            trees.add(TreeNode.of(s));
        }
        TreeNode expectedNode = TreeNode.of(expected);
        assertEquals(expectedNode, canMerge(trees));
        assertEquals(expectedNode, canMerge2(trees));
    }

    @Test public void test() {
        test(new String[] {"2,1", "3,2,5", "5,4"}, "3,2,5,1,#,4");
        test(new String[] {"5,3,8", "3,2,6"}, null);
        test(new String[] {"5,4", "3"}, null);
        test(new String[] {"2,1,3"}, "2,1,3");
        test(new String[] {"8,7,100", "100,99"}, "8,7,100,#,#,99");
        test(new String[] {"3,1", "1", "2,1"}, null);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
