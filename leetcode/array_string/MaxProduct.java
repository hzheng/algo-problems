import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1464: https://leetcode.com/problems/maximum-product-of-two-elements-in-an-array/
//
// Given the array of integers nums, you will choose two different indices i and j of that array.
// Return the maximum value of (nums[i]-1)*(nums[j]-1).
// Constraints:
//
// 2 <= nums.length <= 500
// 1 <= nums[i] <= 10^3
public class MaxProduct {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 3 ms(45.88%), 40.6 MB(100.00%) for 103 tests
    public int maxProduct(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        return (nums[n - 1] - 1) * (nums[n - 2] - 1);
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.8 MB(100.00%) for 103 tests
    public int maxProduct2(int[] nums) {
        int max = Integer.MIN_VALUE;
        int max2 = max;
        for (int num : nums) {
            if (num >= max) {
                max2 = max;
                max = num;
            } else if (num > max2) {
                max2 = num;
            }
        }
        return (max - 1) * (max2 - 1);
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, maxProduct(nums));
        assertEquals(expected, maxProduct2(nums));
    }

    @Test public void test() {
        test(new int[] {3, 4, 5, 2}, 12);
        test(new int[] {1, 5, 4, 5}, 16);
        test(new int[] {3, 7}, 12);
        test(new int[] {3, 8, 9, 11, 2, 16, 9, 7, 7}, 150);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
