import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1664: https://leetcode.com/problems/ways-to-make-a-fair-array/
//
// You are given an integer array nums. You can choose exactly one index (0-indexed) and remove the
// element. Notice that the index of the elements may change after the removal.
// For example, if nums = [6,1,7,4,1]:
// Choosing to remove index 1 results in nums = [6,7,4,1].
// Choosing to remove index 2 results in nums = [6,1,4,1].
// Choosing to remove index 4 results in nums = [6,1,7,4].
// An array is fair if the sum of the odd-indexed values equals the sum of the even-indexed values.
// Return the number of indices that you could choose such that after the removal, nums is fair.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^4
public class WaysToMakeFair {
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(68.64%), 49.5 MB(96.84%) for 105 tests
    public int waysToMakeFair(int[] nums) {
        int n = nums.length;
        int[] evenSum = new int[n + 2];
        int[] oddSum = new int[n + 2];
        int total = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                evenSum[i + 2] += nums[i] + evenSum[i];
                oddSum[i + 2] = oddSum[i + 1];
            } else {
                evenSum[i + 2] = evenSum[i + 1];
                oddSum[i + 2] += nums[i] + oddSum[i];
            }
            total += nums[i];
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            int curEvenSum = evenSum[i + 2] + (oddSum[n + 1] - oddSum[i + 2]);
            if (i % 2 == 0) {
                curEvenSum -= nums[i];
            }
            if (curEvenSum * 2 == total - nums[i]) {
                res++;
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 6 ms(97.43%), 53.5 MB(46.05%) for 105 tests
    public int waysToMakeFair2(int[] nums) {
        int n = nums.length;
        int[] sums = new int[n + 2];
        for (int i = 0; i < n; i++) {
            sums[i + 2] += nums[i] + sums[i];
        }
        int res = 0;
        for (int i = 0, sum = sums[n] + sums[n + 1], oddSum = sums[n + 1 - (n & 1)]; i < n; i++) {
            int curEvenSum = sums[i + 2 - (i & 1)] + oddSum - sums[i + 1 + (i & 1)];
            if (i % 2 == 0) {
                curEvenSum -= nums[i];
            }
            if (curEvenSum * 2 == sum - nums[i]) {
                res++;
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 6 ms(97.43%), 52.8 MB(61.53%) for 105 tests
    public int waysToMakeFair3(int[] nums) {
        int n = nums.length;
        int[] sum = new int[2];
        for (int i = 0; i < n; i++) {
            sum[i & 1] += nums[i];
        }
        int[] cur = new int[2];
        int res = 0;
        for (int i = 0, total = sum[0] + sum[1]; i < n; i++) {
            cur[i & 1] += nums[i];
            int curEvenSum = cur[0] + sum[1] - cur[1];
            if (i % 2 == 0) {
                curEvenSum -= nums[i];
            }
            if (curEvenSum * 2 == total - nums[i]) {
                res++;
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 4 ms(99.80%), 52.4 MB(80.24%) for 105 tests
    public int waysToMakeFair4(int[] nums) {
        int n = nums.length;
        int res = 0;
        int[] left = new int[2];
        int[] right = new int[2];
        for (int i = 0; i < n; i++) {
            right[i & 1] += nums[i];
        }
        for (int i = 0; i < n; i++) {
            right[i & 1] -= nums[i];
            if (left[0] + right[1] == left[1] + right[0]) {
                res++;
            }
            left[i & 1] += nums[i];
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(99.80%), 53.2 MB(49.74%) for 105 tests
    public int waysToMakeFair5(int[] nums) {
        int n = nums.length;
        int res = 0;
        int[] s = new int[n + 1]; // alternate sum
        for (int i = 0; i < n; i++) {
            s[i + 1] = -s[i] + nums[i];
        }
        for (int i = 0; i < n; i++) {
            if (s[i] + ((((n - i) & 1) << 1) - 1) * s[n] - s[i + 1] == 0) {
                res++;
            }
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, waysToMakeFair(nums));
        assertEquals(expected, waysToMakeFair2(nums));
        assertEquals(expected, waysToMakeFair3(nums));
        assertEquals(expected, waysToMakeFair4(nums));
        assertEquals(expected, waysToMakeFair5(nums));
    }

    @Test public void test() {
        test(new int[] {2, 1, 6, 4}, 1);
        test(new int[] {1, 1, 1}, 3);
        test(new int[] {1, 2, 3}, 0);
        test(new int[] {9, 3, 12, 6, 2, 7, 4, 2, 3, 8, 9, 10, 4, 9, 4, 6, 12, 3, 1, 4, 3, 1, 2, 5,
                        8, 10, 7, 3, 5, 6, 9, 12, 5, 2, 3, 2, 1, 7, 3}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
