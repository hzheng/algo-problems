import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC312: https://leetcode.com/problems/burst-balloons/
//
// Given n balloons, indexed from 0 to n-1. Each balloon is painted with a
// number on it represented by array nums. You are asked to burst all the
// balloons. If you burst balloon i you will get nums[left] * nums[i] * nums[right]
// coins. Here left and right are adjacent indices of i. After the burst, the
// left and right then becomes adjacent.
// Find the maximum coins you can collect by bursting the balloons wisely.
// Note:
// (1) You may imagine nums[-1] = nums[n] = 1. They are not real therefore you
// can not burst them.
// (2) 0 <= n <= 500, 0 <= nums[i] <= 100
public class BurstBalloons {
    // Recursion + Divide & Conquer + Dynamic Programming(Top-Down)
    // beats 25.14%(18 ms for 70 tests)
    public int maxCoins(int[] nums) {
        int n = nums.length;
        return maxCoins(nums, -1, n, new int[n + 1][n + 1]);
    }

    private int maxCoins(int[] nums, int start, int end, int[][] cache) {
        // String key = start + "," + end; // beats 0.93%(340 ms)
        // Pair key = Pair.create(start, end);
        // long key = (((long)end) << 32) + start; (even slower?)
        // if (cache.containsKey(key)) return cache.get(key);
        if (start + 1 == end) return 0; // for performance reason

        if (cache[start + 1][end] > 0) return cache[start + 1][end];

        int max = 0;
        for (int i = start + 1; i < end; i++) {
            int cur = maxCoins(nums, start, i, cache) + maxCoins(nums, i, end, cache);
            cur += nums[i] * (start >= 0 ? nums[start] : 1)
                   * (end < nums.length ? nums[end] : 1);
            max = Math.max(max, cur);
        }
        // cache.put(key, max);
        cache[start + 1][end] = max;
        return max;
    }

    // Recursion + Divide & Conquer + Dynamic Programming(Top-Down)
    // beats 3.71%(34 ms)
    public int maxCoins2(int[] nums) {
        Integer[] cache = new Integer[1 << 18];
        return maxCoins(nums, -1, nums.length, cache);
    }

    private int maxCoins(int[] nums, int start, int end, Integer[] cache) {
        int key = (end << 9) | (start + 1);
        if (cache[key] != null) return cache[key];

        int max = 0;
        for (int i = start + 1; i < end; i++) {
            int cur = maxCoins(nums, start, i, cache) + maxCoins(nums, i, end, cache);
            cur += nums[i] * (start >= 0 ? nums[start] : 1)
                   * (end < nums.length ? nums[end] : 1);
            max = Math.max(max, cur);
        }
        cache[key] = max;
        return max;
    }

    // Recursion + Divide & Conquer + Dynamic Programming(Top-Down)
    // beats 97.29%(9 ms for 70 tests)
    public int maxCoins3(int[] nums) {
        int[] nums2 = new int[nums.length + 2];
        int n = 1;
        for (int i : nums) {
            if (i > 0) {
                nums2[n++] = i;
            }
        }
        nums2[0] = nums2[n++] = 1;
        return burst(nums2, 0, n - 1, new int[n][n]);
    }

    private int burst(int[] nums, int start, int end, int[][] dp) {
        if (start + 1 == end) return 0;

        if (dp[start][end] > 0) return dp[start][end];

        int max = 0;
        for (int i = start + 1; i < end; ++i) {
            max = Math.max(max, nums[start] * nums[i] * nums[end]
                           + burst(nums, start, i, dp) + burst(nums, i, end, dp));
        }
        return dp[start][end] = max;
    }

    // Solution of Choice
    // Dynamic programming(Bottom-Up)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 88.28%(6 ms for 70 tests)
    public int maxCoins4(int[] nums) {
        int[] nums2 = new int[nums.length + 2];
        int n = 1;
        for (int i : nums) {
            if (i > 0) {
                nums2[n++] = i;
            }
        }
        nums2[0] = nums2[n++] = 1;
        int[][] dp = new int[n - 1][n];
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                for (int k = i + 1; k < j; k++) {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j]
                                        + nums2[k] * nums2[i] * nums2[j]);
                }
            }
        }
        // or:
        // for (int i = 2; i < n; i++) {
        //     for (int left = 0; left < n - i; left++) {
        //         int right = left + i;
        //         for (int j = left + 1; j < right; j++) {
        //             dp[left][right] = Math.max(dp[left][right],
        //                                        nums2[left] * nums2[j] * nums2[right]
        //                                        + dp[left][j] + dp[j][right]);
        //         }
        //     }
        // }
        // or:
        // for (int j = 2; j < n; j++) {
        //     for (int i = j - 2; i >= 0; i--) { // must be in reverse order!
        //         for (int k = i + 1; k < j; k++) {
        //             dp[i][j] = Math.max(dp[i][j],
        //                                 nums2[i] * nums2[k] * nums2[j]
        //                                 + dp[i][k] + dp[k][j]);
        //
        //         }
        //     }
        // }
        return dp[0][n - 1];
    }

    void test(Function<int[], Integer> maxCoins, String name,
              int expected, int ... nums) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)maxCoins.apply(nums));
        if (nums.length > 10) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int expected, int ... nums) {
        BurstBalloons b = new BurstBalloons();
        test(b::maxCoins, "maxCoins", expected, nums);
        test(b::maxCoins2, "maxCoins2", expected, nums);
        test(b::maxCoins3, "maxCoins3", expected, nums);
        test(b::maxCoins4, "maxCoins4", expected, nums);
    }

    @Test
    public void test1() {
        test(5, 5);
        test(167, 3, 1, 5, 8);
        test(932, 3, 1, 5, 8, 0, 9, 7);
        test(3446, 8, 2, 6, 8, 9, 8, 1, 4, 1, 5, 3, 0, 7, 7, 0, 4, 2, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BurstBalloons");
    }
}
