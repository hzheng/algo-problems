import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC173: https://leetcode.com/problems/binary-search-tree-iterator/
//
// Implement an iterator over a binary search tree (BST). Your iterator will be
// initialized with the root node of a BST.
// Calling next() will return the next smallest number in the BST.
// Note: next() and hasNext() should run in average O(1) time and uses O(h)
// memory, where h is the height of the tree.
public class BSTIter {
    static interface BSTIterable {
        /** @return whether we have a next smallest number */
        public boolean hasNext();

        /** @return the next smallest number */
        public int next();
    }

    // Solution of Choice
    // space complexity: O(h)
    // beats 15.82%(7 ms)
    static class BSTIterator implements BSTIterable {
        private Stack<TreeNode> stack = new Stack<>();

        public BSTIterator(TreeNode root) {
            pushAllLeft(root);
        }

        // time complexity: O(1)
        public boolean hasNext() {
            return !stack.empty();
        }

        // *average* time complexity: O(1)
        public int next() {
            TreeNode top = stack.pop();
            pushAllLeft(top.right);
            return top.val;
        }

        private void pushAllLeft(TreeNode cur) {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
        }
    }

    // destructive solution base on Day–Stout–Warren (DSW) algorithm
    // time complexity: O(1), space complexity: O(1)
    // beats 93.59%(5 ms)
    static class BSTIterator2 implements BSTIterable {
        private TreeNode cur;

        public BSTIterator2(TreeNode root) {
            // tree-to-vine
            TreeNode dummy = new TreeNode(0);
            dummy.right = root;
            TreeNode tail = dummy;
            TreeNode rest = tail.right;
            while (rest != null) {
                if (rest.left == null) {
                    tail = rest;
                    rest = rest.right;
                } else {
                    TreeNode tmp = rest.left;
                    rest.left = tmp.right;
                    tmp.right = rest;
                    rest = tmp;
                    tail.right = tmp;
                }
            }
            cur = dummy.right;
        }

        public boolean hasNext() {
            return cur != null;
        }

        public int next() {
            int val = cur.val;
            cur = cur.right;
            return val;
        }
    }

    // beats 40.01%(6 ms for 61 tests)
    // Morris traversal
    static class BSTIterator3 implements BSTIterable {
        private TreeNode cur;

        public BSTIterator3(TreeNode root) {
            TreeNode prev;
            for (cur = root; cur != null; ) {
                if (cur.left == null) {
                    cur = cur.right;
                } else {
                    for (prev = cur.left; prev.right != null && prev.right != cur;
                         prev = prev.right) {}
                    if (prev.right == null) {
                        prev.right = cur; // create threaded link
                        cur = cur.left;
                    } else {
                        cur = cur.right;
                    }
                }
            }
            for (cur = root; cur != null && cur.left != null; cur = cur.left) {}
        }

        public boolean hasNext() {
            return cur != null;
        }

        public int next() {
            int res = cur.val;
            TreeNode next = cur.right;
            if (next == null || next.left == null) {
                cur = next;
            } else if (next.left.val > cur.val) {
                for (cur = next; cur.left != null; cur = cur.left) {}
            } else {
                cur.right = null; // recover the tree
                cur = next;
            }
            return res;
        }
    }

    void test1(BSTIterable iter, int ... expected) {
        for (int n : expected) {
            assertEquals(n, iter.next());
        }
        assertEquals(false, iter.hasNext());
    }

    public void test(String s, int ... expected) {
        test1(new BSTIterator(TreeNode.of(s)), expected);
        test1(new BSTIterator2(TreeNode.of(s)), expected);
        test1(new BSTIterator3(TreeNode.of(s)), expected);
    }

    @Test
    public void test1() {
        test("3,2,4,1", 1, 2, 3, 4);
        test("5,3,#,2,4,1", 1, 2, 3, 4, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BSTIter");
    }
}
