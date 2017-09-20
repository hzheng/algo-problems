import org.junit.Test;

import common.TreeNode2;

/**
 * Cracking the Coding Interview(5ed) Problem 4.3:
 * Given an increasing order array with unique integer elements, create a
 * binary search tree with minimal height.
 */
public class MinBST {
    private static TreeNode2 createMinBST(int[] n, int start, int end) {
        if (start > end) return null;

        int middle = (end + start) / 2;
        TreeNode2 node = new TreeNode2(n[middle]);
        node.left = createMinBST(n, start, middle - 1);
        node.right = createMinBST(n, middle + 1, end);
        return node;
    }

    public static TreeNode2 createMinBST(int[] n) {
        return createMinBST(n, 0, n.length - 1);
    }

    void test(int[] n, int[] expected) {
        // TreeNode2 tree = createMinBST(n);
        // assertArrayEquals(expected, treeArray);
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[] {1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinBST");
    }
}
