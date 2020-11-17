import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1658: https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/
//
// You are given an integer array nums and an integer x. In one operation, you can either remove the
// leftmost or the rightmost element from the array nums and subtract its value from x. Note that
// this modifies the array for future operations. Return the minimum number of operations to reduce
// x to exactly 0 if it's possible, otherwise, return -1.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^4
// 1 <= x <= 10^9
public class MinOperationsReduceToZero {
    // Hash Table + Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 197 ms(20.00%), 72.8 MB(20.00%) for 86 tests
    public int minOperations(int[] nums, int x) {
        int n = nums.length;
        int[] left = new int[n + 1];
        int[] right = new int[n + 1];
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(0, 0);
        for (int i = 0; i < n; i++) {
            left[i + 1] = left[i] + nums[i];
            map1.put(left[i + 1], i + 1);
        }
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(0, 0);
        for (int i = n - 1, j = 1; i >= 0; i--, j++) {
            right[i] = right[i + 1] + nums[i];
            map2.put(right[i], j);
        }
        int res = n + 1;
        for (int k : map1.keySet()) {
            int index = map1.get(k);
            int other = map2.getOrDefault(x - k, n + 1);
            res = Math.min(res, index + other);
        }
        return (res > n) ? -1 : res;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(N)
    // 8 ms(20.00%), 52.9 MB(20.00%) for 86 tests
    public int minOperations2(int[] nums, int x) {
        int target = Arrays.stream(nums).sum() - x;
        if (target < 0) { return -1; }

        int n = nums.length;
        if (target == 0) { return n; }

        int len = -1;
        for (int start = 0, end = 0, cur = 0; end < n; end++) {
            for (cur += nums[end]; cur >= target; cur -= nums[start++]) {
                if (cur == target) {
                    len = Math.max(len, end - start + 1);
                }
            }
        }
        return len < 0 ? -1 : n - len;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 73 ms(20.00%), 56.9 MB(20.00%) for 86 tests
    public int minOperations3(int[] nums, int x) {
        int target = Arrays.stream(nums).sum() - x;
        int n = nums.length;
        if (target == 0) { return n; }

        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int res = Integer.MIN_VALUE;
        for (int i = 0, sum = 0; i < n; i++) {
            sum += nums[i];
            int index = map.getOrDefault(sum - target, -2);
            if (index >= -1) {
                res = Math.max(res, i - index);
            }
            map.put(sum, i);
        }
        return res == Integer.MIN_VALUE ? -1 : n - res;
    }

    private void test(int[] nums, int x, int expected) {
        assertEquals(expected, minOperations(nums, x));
        assertEquals(expected, minOperations2(nums, x));
        assertEquals(expected, minOperations3(nums, x));
    }

    @Test public void test() {
        test(new int[] {1, 1, 4, 2, 3}, 5, 2);
        test(new int[] {5, 6, 7, 8, 9}, 4, -1);
        test(new int[] {3, 2, 20, 1, 1, 3}, 10, 5);
        test(new int[] {1, 1}, 3, -1);
        test(new int[] {5, 2, 3, 1, 1}, 5, 1);
        test(new int[] {8828, 9581, 49, 9818, 9974, 9869, 9991, 10000, 10000, 10000, 9999, 9993,
                        9904, 8819, 1231, 6309}, 134365, 16);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
