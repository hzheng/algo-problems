import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC108: https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/
//
// Given an array where elements are sorted in ascending order, convert it to a
// height balanced BST.
public class ArrayToBst {
    // Solution of Choice
    // Divide & Conquer/Recursion
    // beats 6.91%(1 ms)
    public TreeNode sortedArrayToBST(int[] nums) {
        return sortedArrayToBST(nums, 0, nums.length);
    }

    private TreeNode sortedArrayToBST(int[] nums, int start, int end) {
        if (start >= end) return null;

        int mid = (start + end) >>> 1;
        TreeNode parent = new TreeNode(nums[mid]);
        parent.left = sortedArrayToBST(nums, start, mid);
        parent.right = sortedArrayToBST(nums, mid + 1, end);
        return parent;
    }

    // Queue
    // beats 2.27%(4 ms)
    public TreeNode sortedArrayToBST2(int[] nums) {
        if (nums.length == 0) return null;

        Node root = new Node(0, nums.length, nums);
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            Node left = node.leftChild(nums);
            if (left != null) {
                queue.offer(left);
            }
            Node right = node.rightChild(nums);
            if (right != null) {
                queue.offer(right);
            }
        }
        return root.node;
    }

    private static class Node {
        int start, end;
        TreeNode node;

        Node(int start, int end, int[] nums) {
            this.start = start;
            this.end = end;
            node = new TreeNode(nums[(start + end) >>> 1]);
        }

        Node leftChild(int[] nums) {
            int mid = (start + end) >>> 1;
            if (start >= mid) return null;

            Node left = new Node(start, mid, nums);
            node.left = left.node;
            return left;
        }

        Node rightChild(int[] nums) {
            int mid = (start + end) >>> 1;
            if (mid + 1 >= end) return null;

            Node right = new Node(mid + 1, end, nums);
            node.right = right.node;
            return right;
        }
    }

    void test(Function<int[], TreeNode> convert, int[] nums, String expected) {
        assertEquals(TreeNode.of(expected), convert.apply(nums));
    }

    void test(int[] nums, String expected) {
        ArrayToBst a = new ArrayToBst();
        test(a::sortedArrayToBST, nums, expected);
        test(a::sortedArrayToBST2, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3}, "2,1,3");
        test(new int[] {1, 2, 3, 4}, "3,2,4,1");
        test(new int[] {1, 2, 3, 4, 5}, "3,2,5,1,#,4");
        test(new int[] {1, 2, 3, 4, 5, 6}, "4,2,6,1,3,5");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArrayToBst");
    }
}
