import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1679: https://leetcode.com/problems/max-number-of-k-sum-pairs/
//
// You are given an integer array nums and an integer k. In one operation, you can pick two numbers
// from the array whose sum equals k and remove them from the array.
// Return the maximum number of operations you can perform on the array.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^9
// 1 <= k <= 10^9
public class MaxNumberOfKSumPairs {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 30 ms(25.00%), 40.1 MB(25.00%) for 51 tests
    public int maxOperations(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int a : nums) {
            map.put(a, map.getOrDefault(a, 0) + 1);
        }
        int res = 0;
        for (int a : map.keySet()) {
            int count = map.get(a);
            if (count == 0) { continue; }

            int b = k - a;
            if (a == b) {
                res += count / 2;
                continue;
            }
            int pair = map.getOrDefault(b, 0);
            if (pair > 0) {
                map.put(b, 0);
                res += Math.min(count, pair);
            }
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 29 ms(25.00%), 52.9 MB(25.00%) for 51 tests
    public int maxOperations2(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        int res = 0;
        for (int a : nums) {
            int complement = map.getOrDefault(k - a, 0);
            if (complement > 0) {
                map.put(k - a, complement - 1);
                res++;
            } else {
                map.put(a, map.getOrDefault(a, 0) + 1);
            }
        }
        return res;
    }

    // Sort + Two Pointers
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 26 ms(25.00%), 83.4 MB(25.00%) for 51 tests
    public int maxOperations3(int[] nums, int k) {
        Arrays.sort(nums);
        int res = 0;
        for (int i = 0, j = nums.length - 1; i < j; i++, j--) {
            if (nums[i] + nums[j] < k) {
                j++;
            } else if (nums[i] + nums[j] > k) {
                i--;
            } else {
               res++;
            }
        }
        return res;
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, maxOperations(nums, k));
        assertEquals(expected, maxOperations2(nums, k));
        assertEquals(expected, maxOperations3(nums, k));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4}, 5, 2);
        test(new int[] {3, 1, 3, 4, 3}, 6, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
