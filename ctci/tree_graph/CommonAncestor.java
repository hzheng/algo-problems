import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode2;
import static common.TreeUtils.*;

/**
 * Cracking the Coding Interview(5ed) Problem 4.5:
 * Find the first common ancestor of two nodes in a binary tree.
 * Avoid storing additional nodes in a data structure.
 */
public class CommonAncestor {
    // assume each node has link to its parent
    public static TreeNode2 commonAncestorWithParentLink(
        TreeNode2 n1, TreeNode2 n2) {
        if (n1 == null || n2 == null) return null;

        int depth = 0;
        TreeNode2 n = n1;
        for (; n != n2 && n != null; n = n.parent) {
            depth++;
        }
        if (n == n2) return n2;

        for (n = n2; n != n1 && n != null; n = n.parent) {
            depth--;
        }
        if (n == n1) return n1;

        for (; depth > 0; depth--) {
            n1 = n1.parent;
        }
        for (; depth < 0; depth++) {
            n2 = n2.parent;
        }
        // now n1 and n2 are of the same depth
        while (n1 != n2) {
            n1 = n1.parent;
            n2 = n2.parent;
        }
        return n1;
    }

    // assume each node doesn't have link to its parent
    public static TreeNode2 commonAncestor(TreeNode2 root,
                                          TreeNode2 n1, TreeNode2 n2) {
        if (!contains(root, n1) || !contains(root, n2)) return null;

        return commonAncestor_(root, n1, n2);
    }

    private static TreeNode2 commonAncestor_(TreeNode2 root,
                                            TreeNode2 n1, TreeNode2 n2) {
        if (root == null) return null;

        if (n1 == root || n2 == root) return root;

        boolean left1 = contains(root.left, n1);
        boolean left2 = contains(root.left, n2);
        if (left1 != left2) return root;

        root = left1 ? root.left : root.right;
        return commonAncestor_(root, n1, n2);
    }

    private static boolean contains(TreeNode2 root, TreeNode2 node) {
        if (root == null) return false;
        if (root == node) return true;
        return contains(root.left, node) || contains(root.right, node);
    }

    public static TreeNode2 commonAncestor2(TreeNode2 root,
                                            TreeNode2 n1, TreeNode2 n2) {
        if (!contains(root, n1) || !contains(root, n2)) return null;

        return commonAncestor2_(root, n1, n2);
    }

    private static TreeNode2 commonAncestor2_(TreeNode2 root,
                                              TreeNode2 n1, TreeNode2 n2) {
        if (root == null) return null;

        if (n1 == root || n2 == root) return root;

        TreeNode2 left = commonAncestor2_(root.left, n1, n2);
        TreeNode2 right = commonAncestor2_(root.right, n1, n2);
        if (left != null && right != null) return root;
        return (left != null) ? left : right;
    }

    void test(Integer[] array, int[] n1, int[] n2, int[] expected) {
        TreeNode2 tree = createTree(array, true);
        print(tree);
        for (int i = 0; i < n1.length; i++) {
            TreeNode2 p = find(tree, n1[i]);
            TreeNode2 q = find(tree, n2[i]);
            assertEquals(expected[i], commonAncestorWithParentLink(p, q).data);
            assertEquals(expected[i], commonAncestor(tree, p, q).data);
            assertEquals(expected[i], commonAncestor2(tree, p, q).data);
        }
    }

    @Test
    public void test1() {
        test(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
        new int[]{8, 4, 5}, new int[]{5, 2, 6}, new int[]{2, 2, 1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CommonAncestor");
    }
}
