import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC965: https://leetcode.com/problems/univalued-binary-tree/
//
// A binary tree is univalued if every node in the tree has the same value.
// Return true if and only if the given tree is univalued.
public class UnivaluedBinaryTree {
    // Recursion
    // beats 94.28%(4 ms for 72 tests)
    public boolean isUnivalTree(TreeNode root) {
        return root == null || isUnival(root, root.val);
    }

    private boolean isUnival(TreeNode root, int val) {
        if (root == null) return true;
        if (root.val != val) return false;
        return isUnival(root.left, val) && isUnival(root.right, val);
    }

    // Recursion
    // beats 67.22%(5 ms for 72 tests)
    public boolean isUnivalTree2(TreeNode root) {
        return (root.left == null || (root.val == root.left.val && isUnivalTree2(root.left)))
               && (root.right == null || (root.val == root.right.val && isUnivalTree2(root.right)));
    }

    // Queue
    // beats 15.25%(6 ms for 72 tests)
    public boolean isUnivalTree3(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        for (int v = root.val; !q.isEmpty();) {
            TreeNode n = q.poll();
            if (n.val != v) return false;
            if (n.left != null) {
                q.offer(n.left);
            }
            if (n.right != null) {
                q.offer(n.right);
            }
        }
        return true;
    }

    void test(String tree, boolean expected) {
        assertEquals(expected, isUnivalTree(TreeNode.of(tree)));
        assertEquals(expected, isUnivalTree2(TreeNode.of(tree)));
    }

    @Test
    public void test() {
        test("1,1,1,1,1,#,1", true);
        test("2,2,2,5,2", false);
        test("1", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
