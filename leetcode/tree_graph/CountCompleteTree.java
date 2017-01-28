import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC222: https://leetcode.com/problems/count-complete-tree-nodes/
//
// Given a complete binary tree, count the number of nodes.
public class CountCompleteTree {
    // Queue
    // Time Limit Exceeded
    // time complexity: O(N), space complexity: O(N)
    public int countNodes(TreeNode root) {
        if (root == null) return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int count = 0;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            count++;
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        return count;
    }

    // Solution of Choice
    // time complexity: O(log(N) ^ 2), space complexity: O(1)
    // beats 95.23%(56 ms for 18 tests)
    public int countNodes2(TreeNode root) {
        if (root == null) return 0;

        int h = getHeight(root);
        int count = 1 << h;
        for (TreeNode cur = root; cur.right != null; h--) {
            if (getHeight(cur.right) == h - 1) {
                count += (1 << (h - 1));
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return count;
    }

    private int getHeight(TreeNode root) {
        if (root == null) return -1;

        int height = 0;
        for (TreeNode n = root.left; n != null; n = n.left, height++) {}
        return height;
    }

    // Recursion
    // time complexity: O(log(N) ^ 2), space complexity: O(log(N))
    // beats 20.71%(122 ms)
    public int countNodes3(TreeNode root) {
        if (root == null) return 0;

        // one more line will work, but is slow
        // return countNodes3(root.left) + countNodes3(root.right) + 1;

        int left = leftHeight(root);
        int right = rightHeight(root);
        if (left == right) return (2 << left) - 1;

        return countNodes3(root.left) + countNodes3(root.right) + 1;
    }

    private int leftHeight(TreeNode root) {
        if (root == null) return 0;

        int height = 0;
        for (TreeNode n = root.left; n != null; n = n.left, height++) {}
        return height;
    }

    private int rightHeight(TreeNode root) {
        if (root == null) return 0;

        int height = 0;
        for (TreeNode n = root.right; n != null; n = n.right, height++) {}
        return height;
    }

    // Recursion
    // beats 84.80%(71 ms for 18 tests)
    public int countNodes4(TreeNode root) {
        if (root == null) return 0;

        int h = getHeight(root);
        return getHeight(root.right) == (h - 1)
               ? (1 << h) + countNodes4(root.right)
               : (1 << h - 1) + countNodes4(root.left);
    }

    // Binary Search
    // time complexity: O(log(N) ^ 2), space complexity: O(1)
    // beats 60.88%(107 ms)
    public int countNodes5(TreeNode root) {
        if (root == null) return 0;

        int h = getHeight(root);
        int low = 0;
        for (int high = 1 << h; low < high; ) {
            int mid = (low + high) >>> 1;
            if (isFull(root, h, mid)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return (1 << h) + low - 1;
    }

    private boolean isFull(TreeNode node, int level, int path) {
        for (int i = level - 1; i >= 0; i--) {
            if (((path >> i) & 1) == 0) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return node != null;
    }

    void test(String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, countNodes(root));
        assertEquals(expected, countNodes2(root));
        assertEquals(expected, countNodes3(root));
        assertEquals(expected, countNodes4(root));
        assertEquals(expected, countNodes5(root));
    }

    @Test
    public void test1() {
        String s = "1";
        test(s, 1);
        for (int i = 2; i < 17; i++) {
            s += "," + i;
            test(s, i);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountCompleteTree");
    }
}
