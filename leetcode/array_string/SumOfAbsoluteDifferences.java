import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1685: https://leetcode.com/problems/sum-of-absolute-differences-in-a-sorted-array/
//
// You are given an integer array nums sorted in non-decreasing order.
// Build and return an integer array result with the same length as nums such that result[i] is
// equal to the summation of absolute differences between nums[i] and all the other elements in the
// array. In other words, result[i] is equal to sum(|nums[i]-nums[j]|) where 0 <= j < nums.length
// and j != i (0-indexed).
//
// Constraints:
// 2 <= nums.length <= 10^5
// 1 <= nums[i] <= nums[i + 1] <= 10^4
public class SumOfAbsoluteDifferences {
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(72.01%), 52.5 MB(79.39%) for 59 tests
    public int[] getSumAbsoluteDifferences(int[] nums) {
        int n = nums.length;
        int[] right = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            right[i] = right[i + 1] + nums[i];
        }
        int[] res = new int[n];
        for (int i = 0, leftSum = 0; i < n; i++) {
            res[i] = right[i + 1] - leftSum + (i * 2 + 1 - n) * nums[i];
            leftSum += nums[i];
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 3 ms(72.01%), 54.3 MB(30.79%) for 59 tests
    public int[] getSumAbsoluteDifferences2(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        for (int i = 1; i < n; i++) {
            res[0] += nums[i] - nums[0];
        }
        for (int i = 1; i < n; i++) {
            res[i] = res[i - 1] + (nums[i] - nums[i - 1]) * (i * 2 - n);
        }
        return res;
    }

    private void test(int[] nums, int[] expected) {
        assertArrayEquals(expected, getSumAbsoluteDifferences(nums));
        assertArrayEquals(expected, getSumAbsoluteDifferences2(nums));
    }

    @Test public void test() {
        test(new int[] {2, 3, 5}, new int[] {4, 3, 5});
        test(new int[] {1, 4, 6, 8, 10}, new int[] {24, 15, 13, 15, 21});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
