import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC669: https://leetcode.com/problems/trim-a-binary-search-tree/
//
// Given a binary search tree and the lowest and highest boundaries as L and R,
// trim the tree so that all its elements lies in [L, R] (R >= L). You might
// need to change the root of the tree, so the result should return the new root
// of the trimmed binary search tree.
public class TrimBST {
    // Recursion
    // beats 83.01%(7 ms for 77 tests)
    public TreeNode trimBST(TreeNode root, int L, int R) {
        if (root == null) return null;

        if (root.val < L) return trimBST(root.right, L, R);

        if (root.val > R) return trimBST(root.left, L, R);

        root.left = trimBST(root.left, L, R);
        root.right = trimBST(root.right, L, R);
        return root;
    }

    // Iteration
    // beats 83.01%(7 ms for 77 tests)
    public TreeNode trimBST2(TreeNode root, int L, int R) {
        while (root.val < L || root.val > R) {
            root = (root.val < L) ? root.right : root.left;
        }
        for (TreeNode node = root; node != null; node = node.left) {
            while (node.left != null && node.left.val < L) {
                node.left = node.left.right;
            }
        }
        for (TreeNode node = root; node != null; node = node.right) {
            while (node.right != null && node.right.val > R) {
                node.right = node.right.left;
            }
        }
        return root;
    }

    void test(String tree, int L, int R, String expected) {
        expected = "{" + expected + "}";
        assertEquals(expected, trimBST(TreeNode.of(tree), L, R).toString());
        assertEquals(expected, trimBST2(TreeNode.of(tree), L, R).toString());
    }

    @Test
    public void test() {
        test("1,0,2", 1, 2, "1,#,2");
        test("3,0,4,#,2,#,#,1", 1, 3, "3,2,#,1");
        test("3,2,4", 1, 2, "2");
        test("45,30,46,10,36,#,49,8,24,34,42,48,#,4,9,14,25,31,35,41,43,47,#,"
             + "0,6,#,#,11,20,#,28,#,33,#,#,37,#,#,44,#,#,#,1,5,7,#,12,19,21,"
             + "26,29,32,#,#,38,#,#,#,3,#,#,#,#,#,13,18,#,#,22,#,27,#,#,#,#,#,"
             + "39,2,#,#,#,15,#,#,23,#,#,#,40,#,#,#,16,#,#,#,#,#,17", 32, 44,
             "36,34,42,33,35,41,43,32,#,#,#,37,#,#,44,#,#,#,38,#,#,#,39,#,40");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
