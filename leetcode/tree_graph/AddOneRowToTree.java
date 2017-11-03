import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC623: https://leetcode.com/problems/add-one-row-to-tree/
//
// Given the root of a binary tree, then value v and depth d, you need to add a
// row of nodes with value v at the given depth d. The root node is at depth 1.
// The adding rule is: given a positive integer depth d, for each NOT null tree
// nodes N in depth d-1, create two tree nodes with value v as N's left subtree
// root and right subtree root. And N's original left subtree should be the left
// subtree of the new left subtree root, its original right subtree should be
// the right subtree of the new right subtree root. If depth d is 1 that means
// there is no depth d-1 at all, then create a tree node with value v as the new
// root of the whole original tree, and the original tree is the new root's left
// subtree.
// Note:
// The given d is in range [1, maximum depth of the given tree + 1].
// The given binary tree has at least one tree node.
public class AddOneRowToTree {
    // BFS + Queue
    // time complexity: O(N), space complexity: O(|max # of first-d level|)
    // beats 22.53%(11 ms for 109 tests)
    public TreeNode addOneRow(TreeNode root, int v, int d) {
        if (d == 1) {
            TreeNode res = new TreeNode(v);
            res.left = root;
            return res;
        }

        TreeNode res = new TreeNode(root.val);
        Queue<TreeNode> q1 = new LinkedList<>();
        Queue<TreeNode> q2 = new LinkedList<>();
        q1.offer(root);
        q2.offer(res);
        for (int i = 2; i < d; i++) {
            for (int j = q1.size(); j > 0; j--) {
                TreeNode n1 = q1.poll();
                TreeNode n2 = q2.poll();
                if (n1 == null) continue;

                q1.offer(n1.left);
                q1.offer(n1.right);
                n2.left = (n1.left == null) ? null : new TreeNode(n1.left.val);
                n2.right =
                    (n1.right == null) ? null : new TreeNode(n1.right.val);
                q2.offer(n2.left);
                q2.offer(n2.right);
            }
        }
        while (!q1.isEmpty()) {
            TreeNode n1 = q1.poll();
            TreeNode n2 = q2.poll();
            if (n1 == null) continue;

            n2.left = new TreeNode(v);
            n2.left.left = n1.left;
            n2.right = new TreeNode(v);
            n2.right.right = n1.right;
        }
        return res;
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(|max # of first-d level|)
    // beats 22.53%(11 ms for 109 tests)
    public TreeNode addOneRow2(TreeNode root, int v, int d) {
        if (d == 1) {
            TreeNode res = new TreeNode(v);
            res.left = root;
            return res;
        }
 
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (int i = 2; i < d; i++) {
            Queue<TreeNode> tmp = new LinkedList<>();
            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    tmp.offer(node.left);
                }
                if (node.right != null) {
                    tmp.offer(node.right);
                }
            }
            queue = tmp;
        }
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            TreeNode tmp = node.left;
            node.left = new TreeNode(v);
            node.left.left = tmp;
            tmp = node.right;
            node.right = new TreeNode(v);
            node.right.right = tmp;
        }
        return root;
    }

    // DFS + Recursive
    // time complexity: O(N), space complexity: O(N)
    // beats 76.71%(9 ms for 109 tests)
    public TreeNode addOneRow3(TreeNode root, int v, int d) {
        if (d == 1) {
            TreeNode res = new TreeNode(v);
            res.left = root;
            return res;
        }
        insert(root, v, d - 2);
        return root;
    }

    private void insert(TreeNode node, int v, int depth) {
        if (node == null) return;

        if (depth == 0) {
            TreeNode tmp = node.left;
            node.left = new TreeNode(v);
            node.left.left = tmp;
            tmp = node.right;
            node.right = new TreeNode(v);
            node.right.right = tmp;
        } else {
            insert(node.left, v, depth - 1);
            insert(node.right, v, depth - 1);
        }
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(String root, int v, int d, String expected,
              Function<TreeNode, Integer, Integer, TreeNode> addOneRow) {
        assertEquals("{" + expected + "}",
                     addOneRow.apply(TreeNode.of(root), v, d).toString());
    }

    void test(String root, int v, int d, String expected) {
        AddOneRowToTree a = new AddOneRowToTree();
        test(root, v, d, expected, a::addOneRow);
        test(root, v, d, expected, a::addOneRow2);
        test(root, v, d, expected, a::addOneRow3);
    }

    @Test
    public void test() {
        test("2,#,3,4,#,1", 1, 3, "2,#,3,1,1,4,#,#,#,1");
        test("4,2,6,3,1,5", 7, 1, "7,4,#,2,6,3,1,5");
        test("4,2,6,3,1,5", 1, 2, "4,1,1,2,#,#,6,3,1,5");
        test("4,2,#,3,1", 1, 3, "4,2,#,1,1,3,#,#,1");
        test("5,3,#,4,2,#,1", 1, 4, "5,3,#,4,2,1,1,1,1,#,#,#,1");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
