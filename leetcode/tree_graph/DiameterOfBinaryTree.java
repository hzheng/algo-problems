import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC543: https://leetcode.com/problems/diameter-of-binary-tree/
//
// Given a binary tree, you need to compute the length of the diameter of the tree.
// The diameter of a binary tree is the length of the longest path between any
// two nodes in a tree. This path may or may not pass through the root.
public class DiameterOfBinaryTree {
    // DFS + Recursion
    // beats N/A(10 ms for 106 tests)
    public int diameterOfBinaryTree(TreeNode root) {
        int[] res = new int[1];
        depth(root, res);
        return res[0];
    }

    private int depth(TreeNode root, int[] res) {
        if (root == null) return 0;

        int left = depth(root.left, res);
        int right = depth(root.right, res);
        res[0] = Math.max(res[0], left + right);
        return Math.max(left, right) + 1;
    }

    // DFS + Recursion
    // beats N/A(24 ms for 106 tests)
    public int diameterOfBinaryTree2(TreeNode root) {
        if (root == null) return 0;

        return Math.max(depth2(root.left) + depth2(root.right),
                        Math.max(diameterOfBinaryTree2(root.left),
                                 diameterOfBinaryTree2(root.right)));
    }

    private int depth2(TreeNode root) {
        if (root == null) return 0;

        return 1 + Math.max(depth2(root.left), depth2(root.right));
    }

    // DFS + Recursion + Hash Table
    // beats N/A(15 ms for 106 tests)
    public int diameterOfBinaryTree3(TreeNode root) {
        return diameter(root, new HashMap<>());
    }

    private int diameter(TreeNode root, Map<TreeNode, Integer> map) {
        if (root == null) return 0;

        return Math.max(depth(root.left, map) + depth(root.right, map),
                        Math.max(diameter(root.left, map),
                                 diameter(root.right, map)));
    }

    private int depth(TreeNode root, Map<TreeNode, Integer> map) {
        if (root == null) return 0;

        Integer cached = map.get(root);
        if (cached != null) return cached;

        int res = 1 + Math.max(depth(root.left, map), depth(root.right, map));
        map.put(root, res);
        return res;
    }


    void test(String s, int expected) {
        assertEquals(expected, diameterOfBinaryTree(TreeNode.of(s)));
        assertEquals(expected, diameterOfBinaryTree2(TreeNode.of(s)));
        assertEquals(expected, diameterOfBinaryTree3(TreeNode.of(s)));
    }

    @Test
    public void test() {
        test("1", 0);
        test("1,2", 1);
        test("1,2,3,4,5", 3);
        test("1,2,3,#,4,5,6,#,#,7,8,9", 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DiameterOfBinaryTree");
    }
}
