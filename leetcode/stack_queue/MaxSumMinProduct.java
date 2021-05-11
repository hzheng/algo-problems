import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1856: https://leetcode.com/problems/maximum-subarray-min-product/
//
// The min-product of an array is equal to the minimum value in the array multiplied by the array's
// sum. Given an array of integers nums, return the maximum min-product of any non-empty subarray of
// nums. Since the answer may be large, return it modulo 10^9 + 7.
// Note that the min-product should be maximized before performing the modulo operation. Testcases
// are generated such that the maximum min-product without modulo will fit in a 64-bit signed
// integer.
// A subarray is a contiguous part of an array.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^7
public class MaxSumMinProduct {
    private static final long MOD = 1_000_000_007;

    // Monotonic Stack
    // time complexity: O(N), space complexity: O(N)
    // 10 ms(100.00%), 56.5 MB(55.63%) for 41 tests
    public int maxSumMinProduct(int[] nums) {
        int n = nums.length;
        long[] sum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
        }
        long maxArea = 0;
        int[] stack = new int[n + 2];
        for (int i = 0, index = -1; i <= n; ) {
            int height = (i == n) ? 0 : nums[i];
            if (index < 0 || height > nums[stack[index]]) { // keep stack increasing
                stack[++index] = i++;
            } else {
                int last = stack[index--];
                long width = (index < 0) ? sum[i] : sum[i] - sum[stack[index] + 1];
                maxArea = Math.max(maxArea, nums[last] * width);
            }
        }
        return (int)(maxArea % MOD);
    }

    // Sort + SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 492 ms(11.26%), 61.7 MB(52.48%) for 41 tests
    public int maxSumMinProduct2(int[] nums) {
        int n = nums.length;
        long[] sum = new long[n + 1];
        int[][] numsWithIndex = new int[n][];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + nums[i];
            numsWithIndex[i] = new int[] {nums[i], i + 1};
        }
        Arrays.sort(numsWithIndex, (x, y) -> (x[0] != y[0]) ? x[0] - y[0] : x[1] - y[1]);
        TreeSet<Integer> indices = new TreeSet<>();
        indices.add(0);
        indices.add(n + 1);
        long res = 0;
        for (int[] ni : numsWithIndex) {
            int index = ni[1];
            int right = indices.ceiling(index);
            int left = indices.floor(index);
            res = Math.max(res, ni[0] * (sum[right - 1] - sum[left]));
            indices.add(index);
        }
        return (int)(res % MOD);
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, maxSumMinProduct(nums));
        assertEquals(expected, maxSumMinProduct2(nums));
    }

    @Test public void test1() {
        test(new int[] {1, 2, 3, 2}, 14);
        test(new int[] {2, 3, 3, 1, 2}, 18);
        test(new int[] {3, 1, 5, 6, 4, 2}, 60);
        test(new int[] {2, 5, 4, 2, 4, 5, 3, 1, 2, 4}, 50);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
