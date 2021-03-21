import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// 1799: https://leetcode.com/problems/maximize-score-after-n-operations/
//
// You are given nums, an array of positive integers of size 2 * n. You must perform n operations on
// this array.
// In the ith operation (1-indexed), you will:
// Choose two elements, x and y.
// Receive a score of i * gcd(x, y).
// Remove x and y from nums.
// Return the maximum score you can receive after performing n operations.
// The function gcd(x, y) is the greatest common divisor of x and y.
//
// Constraints:
// 1 <= n <= 7
// nums.length == 2 * n
// 1 <= nums[i] <= 10^6
public class MaxScoreAfterNOperations {
    // DFS + Recursion + 2-D Dynamic Programming(Top-Down) + Bit Manipulation
    // time complexity: O(N^2*(2^N)), space complexity: O(N*(2^N))
    // 138 ms(%), 39.7 MB(%) for 66 tests
    public int maxScore(int[] nums) {
        int n = nums.length;
        return dfs(nums, 1, 0, new int[n / 2 + 1][1 << n]);
    }

    private int dfs(int[] nums, int cur, int mask, int[][] dp) {
        int n = nums.length;
        if (cur > n / 2) { return 0; }

        int res = dp[cur][mask];
        if (res > 0) { return res; }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int newMask = (1 << i) | (1 << j);
                if ((newMask & mask) != 0) { continue; }

                int v = cur * gcd(nums[i], nums[j]);
                res = Math.max(res, v + dfs(nums, cur + 1, mask | newMask, dp));
            }
        }
        return dp[cur][mask] = res;
    }

    // 1-D Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(N^2*(2^N)), space complexity: O(2^N)
    // 102 ms(%), 38.4 MB(%) for 66 tests
    public int maxScore2(int[] nums) {
        int n = nums.length;
        Map<Integer, Integer> gcds = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                gcds.put((1 << i) | (1 << j), gcd(nums[i], nums[j]));
            }
        }
        int[] dp = new int[1 << n];
        for (int mask = 0; mask < dp.length; mask++) {
            int bits = Integer.bitCount(mask);
            if (bits % 2 != 0) { continue; }

            for (int k : gcds.keySet()) {
                if ((k & mask) == 0) {
                    dp[k | mask] = Math.max(dp[k | mask], dp[mask] + gcds.get(k) * (bits / 2 + 1));
                }
            }
        }
        return dp[dp.length - 1];
    }

    // 1-D Dynamic Programming(Bottom-Up) + Bit Manipulation
    // time complexity: O(N^2*(2^N)), space complexity: O(2^N)
    // 258 ms(%), 36.9 MB(%) for 66 tests
    public int maxScore3(int[] nums) {
        int n = nums.length;
        int[] dp = new int[1 << n];
        for (int mask = dp.length - 1; mask >= 0; mask--) {
            for (int i = 0; i < n; i++) {
                if (mask << ~i >= 0) { continue; }

                for (int j = i + 1; j < n; j++) {
                    if (mask << ~j >= 0) { continue; }

                    int k = n / 2 - Integer.bitCount(mask) / 2 + 1;
                    int newMask = mask ^ (1 << i) ^ (1 << j);
                    dp[newMask] = Math.max(dp[newMask], dp[mask] + k * gcd(nums[i], nums[j]));
                }
            }
        }
        return dp[0];
    }

    private int gcd(int a, int b) {
        return (b == 0) ? a : gcd(b, a % b);
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, maxScore(nums));
        assertEquals(expected, maxScore2(nums));
        assertEquals(expected, maxScore3(nums));
    }

    @Test public void test() {
        test(new int[] {1, 2}, 1);
        test(new int[] {3, 4, 6, 8}, 11);
        test(new int[] {1, 2, 3, 4, 5, 6}, 14);
        test(new int[] {415, 230, 471, 705, 902, 87}, 23);
        test(new int[] {95, 859, 906, 227, 1000, 135, 82, 858, 211, 858, 697, 923}, 5385);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
