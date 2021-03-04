import org.junit.Test;

import static org.junit.Assert.*;

// LC1250: https://leetcode.com/problems/check-if-it-is-a-good-array/
//
// Given an array nums of positive integers. Your task is to select some subset of nums, multiply
// each element by an integer and add all these numbers. The array is said to be good if you can
// obtain a sum of 1 from the array by any possible subset and multiplicand.
//
// Return True if the array is good otherwise return False.
//
// Constraints:
//
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^9
public class CheckGoodArray {
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(50.83%), 57.9 MB(10.00%) for 47 tests
    public boolean isGoodArray(int[] nums) {
        int factor = nums[0];
        for (int i = nums.length - 1; i > 0 && factor > 1; i--) {
            factor = gcd(factor, nums[i]);
        }
        return factor == 1;
    }

    private int gcd(int a, int b) {
        if (a < b) { return gcd(b, a); }

        return (b == 0) ? a : gcd(a % b, b);
    }

    // time complexity: O(N), space complexity: O(1)
    // 3 ms(50.83%), 58.1 MB(8.33%) for 47 tests
    public boolean isGoodArray2(int[] nums) {
        int gcd = nums[0];
        for (int a : nums) {
            while (a > 0 && gcd > 1) {
                int b = gcd % a;
                gcd = a;
                a = b;
            }
        }
        return gcd == 1;
    }

    private void test(int[] nums, boolean expected) {
        assertEquals(expected, isGoodArray(nums));
        assertEquals(expected, isGoodArray2(nums));
    }

    @Test public void test() {
        test(new int[] {12, 5, 7, 23}, true);
        test(new int[] {29, 6, 10}, true);
        test(new int[] {3, 6}, false);
        test(new int[] {2, 8, 6}, false);
        test(new int[] {6, 10, 15}, true);
        test(new int[] {6, 9, 15}, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
