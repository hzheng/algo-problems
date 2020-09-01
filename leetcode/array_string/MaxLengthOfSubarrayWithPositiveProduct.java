import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1567: https://leetcode.com/problems/maximum-length-of-subarray-with-positive-product/
//
// Given an array of integers nums, find the maximum length of a subarray where the product of all
// its elements is positive.
// A subarray of an array is a consecutive sequence of zero or more values taken out of that array.
// Return the maximum length of a subarray with positive product.
public class MaxLengthOfSubarrayWithPositiveProduct {
    // time complexity: O(N), space complexity: O(1)
    // 4 ms(44.00%), 56.1 MB(60%) for 112 tests
    public int getMaxLen(int[] nums) {
        int res = 0;
        int n = nums.length;
        for (int i = 0, posStart = -1, negStart = -1, sign = 1; i < n; i++) {
            if (nums[i] == 0) {
                sign = 1;
                posStart = negStart = i;
                continue;
            }
            if ((nums[i] < 0) && (negStart <= posStart)) {
                negStart = i;
            }
            if ((nums[i] > 0) ^ (sign > 0)) {
                res = Math.max(res, i - negStart);
                sign = -1;
            } else {
                res = Math.max(res, i - posStart);
                sign = 1;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 4 ms(44.00%), 56.1 MB(60%) for 112 tests
    public int getMaxLen2(int[] nums) {
        int res = 0;
        int n = nums.length;
        for (int i = 0, start = -1, firstNeg = 0, lastNeg = 0, negCount = 0; i < n; i++) {
            if (nums[i] == 0) {
                start = i;
                negCount = 0;
                continue;
            }
            if (nums[i] < 0) {
                if (negCount++ == 0) {
                    firstNeg = i;
                }
                lastNeg = i;
            }
            if (negCount % 2 == 0) {
                res = Math.max(res, i - start);
            } else {
                res = Math.max(res, Math.max(i - firstNeg, lastNeg - start - 1));
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 3 ms(99.65%), 54.5 MB(87.96%) for 112 tests
    public int getMaxLen3(int[] nums) {
        int res = 0;
        for (int i = 0, start = -1, firstNeg = -1, negCount = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                start = i;
                negCount = 0;
                continue;
            }
            if (nums[i] < 0 && (negCount++ == 0)) {
                firstNeg = i;
            }
            res = Math.max(i - ((negCount % 2 == 0) ? start : firstNeg), res);
        }
        return res;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, getMaxLen(nums));
        assertEquals(expected, getMaxLen2(nums));
        assertEquals(expected, getMaxLen3(nums));
    }

    @Test public void test() {
                test(new int[] {1, -2, -3, 4}, 4);
                test(new int[] {0, 1, -2, -3, -4}, 3);
                test(new int[] {-1, -2, -3, 0, 1}, 2);
                test(new int[] {-1, 2}, 1);
                test(new int[] {1, 2, 3, 5, -6, 4, 0, 10}, 4);
                test(new int[] {1, -2, 3, 5, -6, 4, -1, 10}, 6);
        test(new int[] {1, 2, -3, 5, -6, 4, 1, -10, 0, 1}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
