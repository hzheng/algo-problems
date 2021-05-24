import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1590: https://leetcode.com/problems/make-sum-divisible-by-p/
//
// Given an array of positive integers nums, remove the smallest subarray (possibly empty) such
// that the sum of the remaining elements is divisible by p. It is not allowed to remove the whole
// array.
// Return the length of the smallest subarray that you need to remove, or -1 if it's impossible.
// A subarray is defined as a contiguous block of elements in the array.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^9
// 1 <= p <= 10^9
public class MinSubarray {
    // Binary Search + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 45 ms(8.95%), 58.4 MB(41.67%) for 142 tests
    public int minSubarray(int[] nums, int p) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int mod = sum[i + 1] = (nums[i] + sum[i]) % p;
            map.computeIfAbsent(mod, x -> new ArrayList<>()).add(i);
        }
        if (sum[n] == 0) { return 0; }

        int res = n;
        for (int i = 0; i <= n; i++) {
            List<Integer> cand = map.get(((sum[i] + sum[n]) % p));
            if (cand == null) { continue; }

            int j = -Collections.binarySearch(cand, i) - 1;
            if (j < 0) { return 1; }

            if (j < cand.size()) {
                res = Math.min(res, cand.get(j) - i + 1);
            }
        }
        return (res == n) ? -1 : res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 34 ms(37.65%), 61 MB(13.58%) for 142 tests
    public int minSubarray2(int[] nums, int p) {
        int mod = 0;
        for (int num : nums) {
            mod = (mod + num) % p;
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int n = nums.length;
        int res = n;
        for (int i = 0, cur = 0; i < n; i++) {
            cur = (cur + nums[i]) % p;
            map.put(cur, i);
            int target = (cur - mod + p) % p;
            res = Math.min(res, i - map.getOrDefault(target, -n));
        }
        return res < n ? res : -1;
    }

    private void test(int[] nums, int p, int expected) {
        assertEquals(expected, minSubarray(nums, p));
        assertEquals(expected, minSubarray2(nums, p));
    }

    @Test public void test1() {
        test(new int[] {3, 1, 4, 2}, 6, 1);
        test(new int[] {6, 3, 5, 2}, 9, 2);
        test(new int[] {1, 2, 3}, 3, 0);
        test(new int[] {1, 2, 3}, 7, -1);
        test(new int[] {1000000000, 1000000000, 1000000000}, 3, 0);
        test(new int[] {8, 32, 31, 18, 34, 20, 21, 13, 1, 27, 23, 22, 11, 15, 30, 4, 2}, 148, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
