import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1814: https://leetcode.com/problems/count-nice-pairs-in-an-array/
//
// You are given an array nums that consists of non-negative integers. Let us define rev(x) as the
// reverse of the non-negative integer x. For example, rev(123) = 321, and rev(120) = 21. A pair of
// indices (i, j) is nice if it satisfies all of the following conditions:
// 0 <= i < j < nums.length
// nums[i] + rev(nums[j]) == nums[j] + rev(nums[i])
// Return the number of nice pairs of indices. Since that number can be too large, return it modulo
// 10^9 + 7.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 0 <= nums[i] <= 10^9
public class CountNicePairs {
    private static final int MOD = 1_000_000_007;

    // Hash Table
    // time complexity: O(N*log(MAX)), space complexity: O(N)
    // 33 ms(%), 48.8 MB(%) for 82 tests
    public int countNicePairs(int[] nums) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int a : nums) {
            int diff = a - reverse(a);
            cnt.put(diff, cnt.getOrDefault(diff, 0) + 1);
        }
        long res = 0;
        for (long c : cnt.values()) {
            res = (res + c * (c - 1) / 2) % MOD;
        }
        return (int)res;
    }

    private int reverse(int a) {
        int res = 0;
        for (int x = a; x > 0; x /= 10) {
            res = res * 10 + x % 10;
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N*log(MAX)), space complexity: O(N)
    // 33 ms(%), 48.8 MB(%) for 82 tests
    public int countNicePairs2(int[] nums) {
        Map<Integer, Integer> cnt = new HashMap<>();
        int res = 0;
        for (int a : nums) {
            int diff = a - reverse(a);
            int count = cnt.getOrDefault(diff, 0);
            cnt.put(diff, count + 1);
            res = (res + count) % MOD;
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, countNicePairs(nums));
        assertEquals(expected, countNicePairs2(nums));
    }

    @Test public void test() {
        test(new int[] {42, 11, 1, 97}, 2);
        test(new int[] {13, 10, 35, 24, 76}, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
