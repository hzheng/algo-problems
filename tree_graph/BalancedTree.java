import tree_graph.TreeNode;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 4.1:
 * Implement a function to check if a binary tree is balanced, i.e. the heights
 * of the two subtrees of any node never differ by more than one.
 */
public class BalancedTree {
    public static int getHeight(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int leftHeight = getHeight(node.left);
        if (leftHeight < 0) {
            return -1;
        }

        int rightHeight = getHeight(node.right);
        if (leftHeight < 0) {
            return -1;
        }

        switch (leftHeight - rightHeight) {
        case 0:
        case 1:
            return leftHeight + 1;
        case -1: return rightHeight + 1;
        default: return -1;
        }
    }

    public static boolean isBalanced(TreeNode node) {
        return getHeight(node) >= 0;
    }

    void test(int[] n, boolean expected) {
    }

    @Test
    public void test1() {
        test(new int[] {1}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BalancedTree");
    }
}
