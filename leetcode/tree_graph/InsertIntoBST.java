import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC701: https://leetcode.com/problems/insert-into-a-binary-search-tree/
//
// Given the root node of a binary search tree and a value to be inserted into 
// the tree, insert the value into the BST. Return the root node of the BST 
// after the insertion. It is guaranteed that the new value does not exist in
// the original BST.
// Note that there may exist multiple valid ways for the insertion, as long as
// the tree remains a BST after insertion. You can return any of them.
public class InsertIntoBST {
    // beats 27.83%(3 ms for 34 tests)
    public TreeNode insertIntoBST(TreeNode root, int val) {
        for (TreeNode cur = root; cur != null; ) {
            if (val > cur.val) {
                if (cur.right == null) {
                    cur.right = new TreeNode(val);
                    return root;
                }
                cur = cur.right;
            } else {
                if (cur.left == null) {
                    cur.left = new TreeNode(val);
                    return root;
                }
                cur = cur.left;
            }
        }
        return root;
    }

    // Recursion
    // beats 27.83%(3 ms for 34 tests)
    public TreeNode insertIntoBST2(TreeNode root, int val) {
        if (root == null) {
        } else if (val > root.val) {
            if (root.right == null) {
                root.right = new TreeNode(val);
            } else {
                insertIntoBST2(root.right, val);
            }
        } else {
            if (root.left == null) {
                root.left = new TreeNode(val);
            } else {
                insertIntoBST2(root.left, val);
            }
        }
        return root;
    }

    // Recursion
    // beats 99.90%(2 ms for 34 tests)
    public TreeNode insertIntoBST3(TreeNode root, int val) {
        return insertIntoBST(root, val, new TreeNode(val));
    }

    private TreeNode insertIntoBST(TreeNode root, int val, TreeNode node) {
        if (root == null) return node;
        
        if (val > root.val) {
            root.right = insertIntoBST(root.right, val, node);
        } else {
            root.left = insertIntoBST(root.left, val, node);
        }
        return root;
    }

    void test(String root, int val, String expected) {
        assertEquals(TreeNode.of(expected), insertIntoBST(TreeNode.of(root), val));
        assertEquals(TreeNode.of(expected), insertIntoBST2(TreeNode.of(root), val));
        assertEquals(TreeNode.of(expected), insertIntoBST3(TreeNode.of(root), val));
    }

    @Test
    public void test() {
        test("4", 5, "4,#,5");
        test("4", 2, "4,2,#");
        test("4,2,7,1,3", 5, "4,2,7,1,3,5");
        test("4,2,7,1,3,5", 8, "4,2,7,1,3,5,8");
        test("4,2,7,1,3,5,8", 6, "4,2,7,1,3,5,8,#,#,#,#,#,6");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
