import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC700: https://leetcode.com/problems/search-in-a-binary-search-tree/
//
// Given the root node of a binary search tree (BST) and a value. You need to
// find the node in the BST that the node's value equals the given value. Return
// the subtree rooted with that node. 
public class SearchBST {
    // Recursion
    // beats 35.76%(3 ms for 36 tests)
    public TreeNode searchBST(TreeNode root, int val) {
        if (root == null) return null;
        if (root.val == val) return root;

        return (root.val < val) ? searchBST(root.right, val) 
                                : searchBST(root.left, val);
    }

    // beats 100.00%(2 ms for 36 tests)
    public TreeNode searchBST2(TreeNode root, int val) {
        for (TreeNode cur = root; cur != null; ) {
            if (cur.val == val) return cur;

            cur = cur.val < val ? cur.right : cur.left;
        }
        return null;
    }

    void test(String root, int val, String expected) {
        TreeNode exp = (expected == null) ? null : TreeNode.of(expected);
        assertEquals(exp, searchBST(TreeNode.of(root), val));
        assertEquals(exp, searchBST2(TreeNode.of(root), val));
    }

    @Test
    public void test() {
        test("4,2,7,1,3", 2, "2,1,3");
        test("4,2,7,1,3,5,8", 5, "5");
        test("4,2,7,1,3,5,8", 6, null);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
