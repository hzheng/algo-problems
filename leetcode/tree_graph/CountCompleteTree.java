import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/count-complete-tree-nodes/
//
// Given a complete binary tree, count the number of nodes.
public class CountCompleteTree {
    //  Time Limit Exceeded
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
        TreeNode lastNode = root;
        TreeNode node = root.right;
        for (int i = 1; node != null; i++, node = node.right) {
            if (height(node) + i == h) {
                lastNode = node;
                count += (1 << (h - i));
            } else {
                lastNode = node = lastNode.left;
            }
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

    void test(String s, int expected) {
        TreeNode root = TreeNode.of(s);
        assertEquals(expected, countNodes(root));
        assertEquals(expected, countNodes2(root));
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
