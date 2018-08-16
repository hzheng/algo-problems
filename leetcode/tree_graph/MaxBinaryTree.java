import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC654: https://leetcode.com/problems/maximum-binary-tree/
//
// Given an integer array with no duplicates. A maximum tree building on this
// array is defined as follow:
// The root is the maximum number in the array.
// The left subtree is the maximum tree constructed from left part subarray
// divided by the maximum number.
// The right subtree is the maximum tree constructed from right part subarray
// divided by the maximum number.
// Construct the maximum tree by the given array.
public class MaxBinaryTree {
    // Recursion
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 33.17%(10 ms for 107 tests)
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        return construct(nums, 0, nums.length);
    }

    private TreeNode construct(int[] nums, int start, int end) {
        if (start >= end) return null;

        int maxIndex = start;
        for (int i = start + 1; i < end; i++) {
            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }
        TreeNode res = new TreeNode(nums[maxIndex]);
        res.left = construct(nums, start, maxIndex);
        res.right = construct(nums, maxIndex + 1, end);
        return res;
    }

    // Deque
    // time complexity: O(N), space complexity: O(N)
    // beats 5.88%(23 ms for 107 tests)
    public TreeNode constructMaximumBinaryTree2(int[] nums) {
        Deque<TreeNode> stack = new LinkedList<>();
        for (int num : nums) {
            TreeNode cur = new TreeNode(num);
            while (!stack.isEmpty() && stack.peek().val < num) {
                cur.left = stack.pop();
            }
            if (!stack.isEmpty()) {
                stack.peek().right = cur;
            }
            stack.push(cur);
        }
        return stack.peekLast();
    }

    void test(int[] nums, String expected) {
        assertEquals(TreeNode.of(expected), constructMaximumBinaryTree(nums));
        assertEquals(TreeNode.of(expected), constructMaximumBinaryTree2(nums));
    }

    @Test
    public void test() {
        test(new int[] { 3, 2, 1, 6, 0, 5 }, "6,3,5,#,2,0,#,#,1");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
