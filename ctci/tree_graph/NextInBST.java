import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode2;
import static common.TreeUtils.*;

/**
 * Cracking the Coding Interview(5ed) Problem 4.6:
 * Find the 'next' node (in-order successor) of a given node in a binary search
 * tree. You may assume that each node has a link to its parent.
 */
public class NextInBST {
    private static TreeNode2 leftmostDescendant(TreeNode2 node) {
        if (node == null) return null;

        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public static TreeNode2 nextNode(TreeNode2 node) {
        if (node == null) return null;

        if (node.right != null) {
            return leftmostDescendant(node.right);
        }

        // search the first right ancestor
        for (; node != null && node.parent != null; node = node.parent) {
            if (node.parent.left == node) {
                return node.parent;
            }
        }
        return null;
    }

    void test(Integer[] bst, int[] n, Integer[] expected) {
        TreeNode2 tree = createTree(bst, true);
        print(tree);
        // assertTrue(CheckBST.isBST(tree));
        for (int i = 0; i < n.length; i++) {
            TreeNode2 node = findBST(tree, n[i]);
            if (expected[i] != null) {
                assertEquals((int)expected[i], nextNode(node).data);
            } else {
                assertNull(nextNode(node));
            }
        }
    }

    @Test
    public void test1() {
        test(new Integer[] {10, 6, 15, 2, 8, 12, 20, 1, 3, 7, 9},
             new int[] {1, 2, 3, 6, 7, 8, 9, 10, 12, 15, 20},
             new Integer[] {2, 3, 6, 7, 8, 9, 10, 12, 15, 20, null});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NextInBST");
    }
}
