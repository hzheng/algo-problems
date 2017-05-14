import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC581: https://leetcode.com/problems/shortest-unsorted-continuous-subarray/
//
// Given an integer array, you need to find one continuous subarray that if you only
// sort this subarray in ascending order, then the whole array will be sorted in ascending
// order, too. You need to find the shortest such subarray and output its length.
public class ShortestUnsortedSubarray {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 57.14%(38 ms for 307 tests)
    public int findUnsortedSubarray(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int start = 0;
        for (; start < nums.length && nums[start] == sorted[start]; start++) {}
        int end = nums.length - 1;
        for (; end >= start && nums[end] == sorted[end]; end--) {}
        return end - start + 1;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 50.00%(40 ms for 307 tests)
    public int findUnsortedSubarray2(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int start = nums.length;
        int end = 0;
        for (int i = 0; i < sorted.length; i++) {
            if (sorted[i] != nums[i]) {
                start = Math.min(start, i);
                end = Math.max(end, i);
            }
        }
        return (end >= start) ? end - start + 1 : 0;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 50.00%(78 ms for 307 tests)
    public int findUnsortedSubarray3(int[] nums) {
        Stack<Integer> stack = new Stack<>();
        int start = nums.length;
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                start = Math.min(start, stack.pop());
            }
            stack.push(i);
        }
        int end = 0;
        stack.clear();
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                end = Math.max(end, stack.pop());
            }
            stack.push(i);
        }
        return Math.max(0, end - start + 1);
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 85.71%(28 ms for 307 tests)
    public int findUnsortedSubarray4(int[] nums) {
        int start = 0;
        int end = -1;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int i = 0, j = nums.length - 1; i < nums.length; i++, j--) {
            if (nums[i] < max) {
                end = i;
            } else {
                max = nums[i];
            }
            if (nums[j] > min) {
                start = j;
            } else {
                min = nums[j];
            }
        }
        return end - start + 1;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 85.71%(25 ms for 307 tests)
    public int findUnsortedSubarray5(int[] nums) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        boolean flag = false;
        for (int i = 1; i < nums.length; i++) {
            if (flag |= (nums[i] < nums[i - 1])) {
                min = Math.min(min, nums[i]);
            }
        }
        flag = false;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (flag |= (nums[i] > nums[i + 1])) {
                max = Math.max(max, nums[i]);
            }
        }
        int start = 0;
        int end = nums.length - 1;
        for (; start < nums.length && min >= nums[start]; start++) {}
        for (; end >= 0 && max <= nums[end]; end--) {}
        return Math.max(0, end - start + 1);
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, findUnsortedSubarray(nums));
        assertEquals(expected, findUnsortedSubarray2(nums));
        assertEquals(expected, findUnsortedSubarray3(nums));
        assertEquals(expected, findUnsortedSubarray4(nums));
        assertEquals(expected, findUnsortedSubarray5(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4}, 0);
        test(new int[] {2, 6, 4, 8, 10, 9, 15}, 5);
        test(new int[] {1, 2, 6, 4, 8, 10, 9, 13, 15}, 5);
        test(new int[] {1, 2, 6, 4, 8, 10, 9, -1, 13, 15}, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
