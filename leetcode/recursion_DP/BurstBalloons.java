// https://leetcode.com/problems/burst-balloons/

import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

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
    // beats 4.17%(32 ms)
    public int maxCoins(int[] nums) {
        Integer[][] cache = new Integer[501][501];
        return maxCoins(nums, -1, nums.length, cache);
    }

    private int maxCoins(int[] nums, int start, int end, Integer[][] cache) {
        // String key = start + "," + end; // beats 0.93%(340 ms)
        // Pair key = Pair.create(start, end);
        // long key = (((long)end) << 32) + start; (even slower?)
        // if (cache.containsKey(key)) return cache.get(key);
        if (cache[start + 1][end] != null) return cache[start + 1][end];

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
