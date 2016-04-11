import org.junit.Test;
import static org.junit.Assert.*;

import tree_graph.TreeNode;
import static tree_graph.TreeUtils.createTree;

/**
 * Cracking the Coding Interview(5ed) Problem 4.5:
 * Check if a binary tree is a binary search tree.
 */
public class CheckBST {
    public static boolean isBST(TreeNode root) {
        return isBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static boolean isBST(TreeNode root, int min, int max) {
        if (root == null) return true;

        int data = root.data;
        if (data < min || data > max) return false;

        return isBST(root.left, min, data) && isBST(root.right, data, max);
    }

    public static boolean isBST2(TreeNode root) {
        return isBST2(root, new int[] {Integer.MIN_VALUE});
    }

    private static boolean isBST2(TreeNode root, int[] last) {
        if (root == null) return true;

        if (!isBST2(root.left, last)) return false;
        if (root.data < last[0]) return false;
        last[0] = root.data;

        return isBST2(root.right, last);
    }

    void test(Integer[] array, boolean expected) {
        assertEquals(expected, isBST(createTree(array)));
        assertEquals(expected, isBST2(createTree(array)));
    }

    @Test
    public void test1() {
        test(new Integer[] {3}, true);
        test(new Integer[] {3, 1}, true);
        test(new Integer[] {3, 4}, false);
        test(new Integer[] {3, 1, 5}, true);
        test(new Integer[] {3, 1, 2}, false);
    }

    @Test
    public void test3() {
        test(new Integer[] {20, 10, 30}, true);
        test(new Integer[] {20, 10, 30, null, 15}, true);
        test(new Integer[] {20, 10, 30, null, 25}, false);
        test(new Integer[] {10, 5, 15, 3, 4, 9, 20}, false);
        test(new Integer[] {10, 5, 15, 3, 7, 11, 20}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CheckBST");
    }
}
