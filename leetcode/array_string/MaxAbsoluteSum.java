import org.junit.Test;

import static org.junit.Assert.*;

// LC1749: https://leetcode.com/problems/maximum-absolute-sum-of-any-subarray/
//
// You are given an integer array nums. The absolute sum of a subarray [numsl, numsl+1, ...,
// numsr-1, numsr] is abs(numsl + numsl+1 + ... + numsr-1 + numsr).
// Return the maximum absolute sum of any (possibly empty) subarray of nums.
//
// Constraints:
// 1 <= nums.length <= 10^5
// -10^4 <= nums[i] <= 10^4
public class MaxAbsoluteSum {
    // Kadane's Algorithm
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(99.83%), 47.3 MB(91.72%) for 66 tests
    public int maxAbsoluteSum(int[] nums) {
        int max = 0;
        int sum = 0;
        for (int num : nums) {
            sum = Math.max(0, sum + num);
            max = Math.max(max, sum);
        }
        int min = max;
        sum = 0;
        for (int num : nums) {
            sum = Math.min(0, sum + num);
            min = Math.min(min, sum);
        }
        return Math.max(max, -min);
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(99.83%), 47.3 MB(91.72%) for 66 tests
    public int maxAbsoluteSum2(int[] nums) {
        int sum = 0;
        int min = 0;
        int max = 0;
        for (int num : nums) {
            sum += num;
            min = Math.min(min, sum);
            max = Math.max(max, sum);
        }
        return max - min;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, maxAbsoluteSum(nums));
        assertEquals(expected, maxAbsoluteSum2(nums));
    }

    @Test public void test() {
        test(new int[] {1, -3, 2, 3, -4}, 5);
        test(new int[] {2, -5, 1, -4, 3, -2}, 8);
        test(new int[] {-3, -5, -3, -2, -6, 3, 10, -10, -8, -3, 0, 10, 3, -5, 8, 7, -9, -9, 5, -8},
             27);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
