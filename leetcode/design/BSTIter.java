import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/binary-search-tree-iterator/
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

    // beats 15.82%
    static class BSTIterator implements BSTIterable {
        private TreeNode cur;
        private Stack<TreeNode> stack = new Stack<>();

        public BSTIterator(TreeNode root) {
            cur = root;
        }

        public boolean hasNext() {
            return cur != null || !stack.empty();
        }

        public int next() {
            while (cur != null) {
                stack.push(cur);
                cur = cur.left;
            }
            TreeNode top = stack.pop();
            cur = top.right;
            return top.val;
        }
    }

    void test1(BSTIterable iter, int... expected) {
        for (int n : expected) {
            assertEquals(n, iter.next());
        }
        assertEquals(false, iter.hasNext());
    }

    public void test(String s, int... expected) {
        test1(new BSTIterator(TreeNode.of(s)), expected);
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
