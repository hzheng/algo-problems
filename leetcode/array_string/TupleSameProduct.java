import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1726: https://leetcode.com/problems/tuple-with-same-product/
//
// Given an array nums of distinct positive integers, return the number of tuples (a, b, c, d) such
// that a * b = c * d where a, b, c, and d are elements of nums, and a != b != c != d.
//
// Constraints:
// 1 <= nums.length <= 1000
// 1 <= nums[i] <= 10^4
// All elements in nums are distinct.
public class TupleSameProduct {
    // Map
    // time complexity: O(N^2), space complexity: O(N^2)
    // 204 ms(100.00%), 61.8 MB(100.00%) for 37 tests
    public int tupleSameProduct(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0, n = nums.length; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                map.merge(nums[i] * nums[j], 1, Integer::sum);
            }
        }
        int res = 0;
        for (int v : map.values()) {
            res += v * (v - 1);
        }
        return res * 4;
    }

    // Map
    // time complexity: O(N^2), space complexity: O(N^2)
    // 194 ms(100.00%), 72 MB(100.00%) for 37 tests
    public int tupleSameProduct2(int[] nums) {
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0, n = nums.length; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int prod = nums[i] * nums[j];
                int count = map.getOrDefault(prod, 0);
                map.put(prod, count + 1);
                res += count;
            }
        }
        return res * 8;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, tupleSameProduct(nums));
        assertEquals(expected, tupleSameProduct2(nums));
    }

    @Test public void test() {
        test(new int[] {2, 3, 4, 6}, 8);
        test(new int[] {1, 2, 4, 5, 10}, 16);
        test(new int[] {2, 3, 4, 6, 8, 12}, 40);
        test(new int[] {2, 3, 5, 7}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
