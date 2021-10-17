import org.junit.Test;

import static org.junit.Assert.*;

// LC198: https://leetcode.com/problems/house-robber/
//
// You are a professional robber planning to rob houses along a street. Each
// house has a certain amount of money stashed, the only constraint stopping you
// from robbing each of them is that adjacent houses have security system
// connected and it will automatically contact the police if two adjacent houses
// were broken into on the same night.
// Given a list of non-negative integers representing the amount of money of
// each house, determine the maximum amount of money you can rob tonight without
// alerting the police.
//
// Constraints:
// 1 <= nums.length <= 100
// 0 <= nums[i] <= 400
public class HouseRobber {
    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats 41.08%(0 ms)
    public int rob(int[] nums) {
        int maxLastTaken = 0;
        int maxLastNotTaken = 0;
        for (int num : nums) {
            int tmp = maxLastNotTaken;
            // maxLastNotTaken = Math.max(maxLastTaken, maxLastNotTaken);
            //
            // the following is simpler than the last if maxLastTaken includes
            // the case that last one is not taken(or beat rate drops to 3.25%)
            maxLastNotTaken = maxLastTaken;
            maxLastTaken = Math.max(maxLastTaken, tmp + num);
        }
        // return Math.max(maxLastTaken, maxLastNotTaken);
        return maxLastTaken;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.5 MB(17.24%) for 68 tests
    public int rob2(int[] nums) {
        int even = 0;
        int odd = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i % 2 == 0) {
                even = Math.max(even + nums[i], odd);
            } else {
                odd = Math.max(even, odd + nums[i]);
            }
        }
        return Math.max(even, odd);
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.3 MB(23.58%) for 68 tests
    public int rob3(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n + 1];
        dp[1] = nums[0];
        for (int i = 1; i < n; i++) {
            dp[i + 1] = Math.max(dp[i - 1] + nums[i], dp[i]);
        }
        return dp[n];
    }

    void test(int expected, int... nums) {
        assertEquals(expected, rob(nums));
        assertEquals(expected, rob2(nums));
        assertEquals(expected, rob3(nums));
    }

    @Test public void test1() {
        test(3, 3);
        test(4, 1, 4);
        test(5, 1, 4, 4);
        test(6, 2, 1, 2, 4);
        test(25, 1, 3, 6, 8, 2, 9, 7, 4, 5);
        test(27, 5, 3, 6, 8, 2, 9, 7, 4, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
