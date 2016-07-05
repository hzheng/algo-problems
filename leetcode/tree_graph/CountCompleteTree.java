import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/count-complete-tree-nodes/
//
// Given a complete binary tree, count the number of nodes.
public class CountCompleteTree {
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

    // time complexity: O(log(N) ^ 2), space complexity: O(1)
    // beats 98.76%(48 ms)
    public int countNodes2(TreeNode root) {
        if (root == null) return 0;

        int h = height(root);
        int count = (1 << h);
        TreeNode parent = root;
        TreeNode node = root.right;
        for (int i = 1; node != null; i++, node = node.right) {
            if (height(node) + i == h) {
                count += (1 << (h - i));
            } else {
                node = parent.left;
            }
            parent = node;
        }
        return count;
    }

    private int height(TreeNode root) {
        int height = 0;
        for (TreeNode n = root.left; n != null; n = n.left) {
            height++;
        }
        return height;
    }

    // recursion
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
        for (TreeNode n = root.left; n != null; n = n.left) {
            height++;
        }
        return height;
    }

    private int rightHeight(TreeNode root) {
        if (root == null) return 0;

        int height = 0;
        for (TreeNode n = root.right; n != null; n = n.right) {
            height++;
        }
        return height;
    }

    // binary search
    // beats 60.88%(107 ms)
    public int countNodes4(TreeNode root) {
        if (root == null) return 0;

        int h = height(root);
        int low = 0;
        for (int high = 1 << h; low < high; ) {
            int mid = low + (high - low) / 2;
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
