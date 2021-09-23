import java.util.Arrays;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1955: https://leetcode.com/problems/count-number-of-special-subsequences/
//
// A sequence is special if it consists of a positive number of 0s, followed by a positive number of
// 1s, then a positive number of 2s.
// For example, [0,1,2] and [0,0,1,1,1,2] are special.
// In contrast, [2,1,0], [1], and [0,1,2,0] are not special.
// Given an array nums (consisting of only integers 0, 1, and 2), return the number of different
// subsequences that are special. Since the answer may be very large, return it modulo 10^9 + 7.
//
// A subsequence of an array is a sequence that can be derived from the array by deleting some or no
// elements without changing the order of the remaining elements. Two subsequences are different if
// the set of indices chosen are different.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 0 <= nums[i] <= 2
public class CountSpecialSubsequences {
    private static final int MOD = 1000_000_000 + 7;

    // time complexity: O(N), space complexity: O(1)
    // 16 ms(95.54%), 52.5 MB(59.64%) for 99 tests
    public int countSpecialSubsequences(int[] nums) {
        int[] dp = new int[3];
        for (int num : nums) {
            dp[num] = (dp[num] * 2 % MOD + ((num == 0) ? 1 : dp[num - 1])) % MOD;
        }
        return dp[2];
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, countSpecialSubsequences(nums));
    }

    @Test public void test() {
        test(new int[] {0, 1, 2, 2}, 3);
        test(new int[] {2, 2, 0, 0}, 0);
        test(new int[] {0, 1, 2, 0, 1, 2}, 7);
        test(new int[] {2, 0, 0, 1, 2, 0, 2, 1, 2, 2, 2, 2, 2, 2, 1, 0, 2, 0, 2, 2, 1, 1, 2, 1, 1,
                        0, 1, 0, 2, 1, 2, 0, 1, 2, 1, 2, 0, 0, 2, 0, 2, 1, 0, 0, 0, 1, 1, 0, 1, 2,
                        1, 2, 2, 0, 1, 2, 0, 0, 1, 2, 1, 1, 2, 0, 1, 2, 1, 0, 1, 0, 1, 1, 2, 0, 0,
                        0, 1, 1, 1, 2, 1, 0, 0, 0, 0, 2, 2, 1, 1, 1, 2, 1, 2, 0, 1, 0, 0, 1, 0, 1,
                        1, 0, 0, 1, 1, 1, 1, 1, 0, 2, 1, 1, 0, 0, 2, 0, 2, 2, 1, 0, 2, 2, 2, 0, 0,
                        1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 0, 2, 0, 1, 0, 2, 0, 2, 0, 0, 1, 2, 1,
                        1, 0, 2, 0, 1, 0, 2, 1, 1, 0, 0, 1, 1, 0, 1, 0, 2, 1, 2, 0, 0, 0, 1, 1, 1,
                        1, 1, 1, 2, 2, 0, 2, 2, 2, 1, 0, 0, 0, 1, 1, 0, 2, 1, 1, 2, 1, 0, 1, 0, 0,
                        2, 1, 0, 2, 1, 1, 1, 0, 2, 2, 2, 1, 1, 0, 2, 1, 0, 1, 0, 0, 2, 1, 2, 0, 0,
                        2, 1, 1, 0, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 0, 0, 2, 0, 1, 0, 0, 0, 2, 1,
                        1, 2, 1, 1, 0, 0, 2, 0, 1, 2, 1, 1, 1, 0, 2, 1, 1, 0, 0, 1, 2, 1, 1, 1, 2,
                        0, 1, 0, 2, 2, 2, 2, 0, 2, 2, 2, 2, 0, 1, 0, 2, 1, 1, 2, 1, 2, 1, 2, 2, 1,
                        2, 0, 2, 2, 0, 0, 2, 0, 1, 2, 2, 2, 2, 0, 1, 1, 0, 0, 1, 2, 0, 2, 2, 1, 1,
                        1, 0, 1, 1, 0, 1, 1, 2, 0, 2, 0, 1, 1, 0, 0, 2, 0, 2, 1, 2, 1, 1, 0, 0, 2,
                        2, 1, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 1, 1, 1, 1, 2, 0, 1, 1, 0, 0, 0, 1, 0,
                        2, 1, 0, 2, 0, 1, 0, 2, 0, 1, 1, 2, 2, 0, 1, 0, 0, 2, 1, 1, 2, 1, 1, 2, 2,
                        0, 1, 2, 0, 2, 2, 0, 0, 2, 0, 0, 2, 2, 2, 2, 2, 0, 1, 2, 0, 1, 0, 1, 1, 0,
                        1, 1, 2, 1, 0, 1, 2, 1, 2, 1, 1, 1, 2, 2, 1, 0, 1, 1, 2, 2, 2, 0, 0, 1, 2,
                        1, 2, 2, 1, 1, 0, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 0, 0, 0, 2, 2, 0, 1, 1, 1,
                        1, 0, 1, 0, 0, 1, 2, 0}, 782041255);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
