import java.util.LinkedList;

import org.junit.Test;
import static org.junit.Assert.*;

import static common.TreeUtils.*;
import common.TreeNode2;

/**
 * Cracking the Coding Interview(5ed) Problem 4.8:
 * Given two very large binary trees: Tl(millions of nodes) and T2(hundreds of
 * nodes), check if T2 is a subtree of T1.
 */
public class CheckSubtree {
    // time complexity: O(M * N), space complexity: O(log(M) + log(N))
    public static boolean isSubtree(TreeNode2 t1, TreeNode2 t2) {
        if (t1 == null || t2 == null) return false;

        LinkedList<TreeNode2> queue = new LinkedList<>();
        queue.add(t1);
        while (!queue.isEmpty()) {
            TreeNode2 node = queue.removeFirst();
            if (match(node, t2)) return true;

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return false;
    }

    public static boolean isSubtreeRecursive(TreeNode2 t1, TreeNode2 t2) {
        if (t2 == null) return true;
        return isSubtreeRecursive_(t1, t2);
    }

    public static boolean isSubtreeRecursive_(TreeNode2 t1, TreeNode2 t2) {
        if (t1 == null) return false;

        if ((t1.data == t2.data) && match(t1, t2)) return true;

        return isSubtreeRecursive_(t1.left, t2)
               || isSubtreeRecursive_(t1.right, t2);
    }

    private static boolean match(TreeNode2 t1, TreeNode2 t2) {
        if (t1 == null && t2 == null) return true;
        if (t1 == null || t2 == null) return false;

        if (t1.data != t2.data) return false;
        return match(t1.left, t2.left) && match(t1.right, t2.right);
    }

    void test(Integer[] n1, Integer[] n2, boolean expected) {
        TreeNode2 t1 = createTree(n1);
        print(t1);
        TreeNode2 t2 = createTree(n2);
        print(t2);
        isSubtree(t1, t2);
        assertEquals(expected, isSubtree(t1, t2));
        assertEquals(expected, isSubtreeRecursive(t1, t2));
    }

    @Test
    public void test1() {
        test(new Integer[] {3, 1, 5, 6, 9, 8, 7, 2, 10, 12},
             new Integer[] {5, 8, 7}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CheckSubtree");
    }
}
