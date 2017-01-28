import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC213: https://leetcode.com/problems/house-robber-ii/
//
// Note: This is an extension of House Robber.
// After robbing those houses on that street, the thief has found himself a new
// place for his thievery so that he will not get too much attention. This time,
// all houses at this place are arranged in a circle. That means the first house
// is the neighbor of the last one. Meanwhile, the security system for these
// houses remain the same as for those in the previous street.
// Given a list of non-negative integers representing the amount of money of
// each house, determine the maximum amount of money you can rob tonight without
// alerting the police.
public class HouseRobber2 {
    // Dynamic Programming
    // scan array twice
    // time complexity: O(N), space complexity: O(1)
    // beats 56.07%(0 ms for 74 tests)
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        return Math.max(rob(nums, 0, n - 1), rob(nums, 1, n));
    }

    private int rob(int[] nums, int start, int end) {
        int maxLastTaken = 0;
        int maxLastNotTaken = 0;
        for (int i = start; i < end; i++) {
            int tmp = maxLastNotTaken;
            maxLastNotTaken = maxLastTaken;
            maxLastTaken = Math.max(maxLastTaken, tmp + nums[i]);
        }
        return maxLastTaken;
    }

    // Solution of Choice
    // Dynamic Programming
    // scan array once
    // time complexity: O(N), space complexity: O(1)
    // beats 56.07%(0 ms for 74 tests)
    public int rob2(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        int[] lastMax = {nums[0], 0};
        int[] lastLastMax = {0, 0};
        for (int i = 1; i < n - 1; i++) {
            for (int j = 0; j < 2; j++) {
                int tmp = lastLastMax[j];
                lastLastMax[j] = lastMax[j];
                lastMax[j] = Math.max(lastMax[j], tmp + nums[i]);
            }
        }
        return Math.max(lastMax[0], lastLastMax[1] + nums[n - 1]);
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 3.90%(1 ms)
    public int rob3(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];

        return Math.max(rob3(nums, 0, n - 1), rob3(nums, 1, n));
    }

    private int rob3(int[] nums, int start, int end) {
        int[] dp = new int[end - start + 1];
        dp[1] = nums[start];
        for (int i = 2; i <= end - start; i++) {
            dp[i] = Math.max(dp[i - 2] + nums[start + i - 1], dp[i - 1]);
        }
        return dp[end - start];
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, rob(nums));
        assertEquals(expected, rob2(nums));
        assertEquals(expected, rob3(nums));
    }

    @Test
    public void test1() {
        test(0);
        test(3, 3);
        test(4, 1, 4);
        test(4, 1, 4, 4);
        test(5, 2, 1, 2, 4);
        test(25, 1, 3, 6, 8, 2, 9, 7, 4, 5);
        test(26, 5, 3, 6, 8, 2, 9, 7, 4, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HouseRobber2");
    }
}
