import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

import common.TreeNode;

// LC450: https://leetcode.com/problems/delete-node-in-a-bst/
//
// Given a root node reference of a BST and a key, delete the node with the
// given key in the BST. Return the root node (possibly updated) of the BST.
public class DeleteNodeInBst {
    // Recursion
    // beats 28.62%(8 ms for 84 tests)
    public TreeNode deleteNode(TreeNode root, int key) {
        TreeNode[] found = find(root, key, null);
        if (found == null) return root;

        TreeNode target = found[0];
        TreeNode parent = found[1];
        TreeNode next = target.left;
        if (target.right != null) {
            TreeNode leftmost = target.right;
            while (leftmost.left != null) {
                leftmost = leftmost.left;
            }
            leftmost.left = target.left;
            next = target.right;
        }
        if (parent == null) { // target == root
            return (root.right == null) ? root.left : root.right;
        }
        if (parent.left == target) {
            parent.left = next;
        } else {
            parent.right = next;
        }
        return root;
    }

    private TreeNode[] find(TreeNode node, int key, TreeNode parent) {
        if (node == null) return null;

        if (node.val == key) return new TreeNode[] {node, parent};

        return (node.val > key) ? find(node.left, key, node)
               : find(node.right, key, node);
    }

    // beats 60.16%(7 ms for 84 tests)
    public TreeNode deleteNode2(TreeNode root, int key) {
        if (root == null) return null;

        if (key == root.val) {
            if (root.right == null) return root.left;

            TreeNode leftmost = root.right;
            while (leftmost.left != null) {
                leftmost = leftmost.left;
            }
            // it's not good to change value of root
            // root.val = leftmost.val;
            // root.right = deleteNode2(root.right, root.val);
            leftmost.left = root.left;
            return root.right;
        }
        if (key < root.val) {
            root.left = deleteNode2(root.left, key);
        } else {
            root.right = deleteNode2(root.right, key);
        }
        return root;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<TreeNode, Integer, TreeNode> deleteNode,
              String s, int key, String ... expected) {
        TreeNode root = TreeNode.of(s);
        TreeNode res = deleteNode.apply(root, key);
        assertThat(res.toString(), in(expected));
    }

    void test(String s, int key, String ... expected) {
        DeleteNodeInBst d = new DeleteNodeInBst();
        test(d::deleteNode, s, key, expected);
        test(d::deleteNode2, s, key, expected);
    }

    @Test
    public void test1() {
        test("5,3,6,2,4,#,7", 5, "{6,3,7,2,4}");
        test("5,3,6,2,4,#,7", 3, "{5,2,6,#,4,#,7}", "{5,4,6,2,#,#,7}");
        test("5,3,6,2,4,#,7", 1, "{5,3,6,2,4,#,7}");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
