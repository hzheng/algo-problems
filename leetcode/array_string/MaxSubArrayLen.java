import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC325: https://leetcode.com/problems/maximum-size-subarray-sum-equals-k
//
// Given an array nums and a target value k, find the maximum length of a subarray
// that sums to k. If there isn't one, return 0 instead.
// Follow Up:
// Can you do it in O(n) time?
public class MaxSubArrayLen {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 96.39%(26 ms for 35 tests)
    public int maxSubArrayLen(int[] nums, int k) {
        int n = nums.length;
        int[] sum = new int[n + 1];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(sum[i + 1] = sum[i] + nums[i], i);
        }
        int max = 0;
        for (int i = 0; i < n; i++) { // ignore i == n
            int index = map.getOrDefault(k + sum[i], -1);
            max = Math.max(max, index - i + 1);
        }
        return max;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats 88.23%(28 ms for 35 tests)
    public int maxSubArrayLen2(int[] nums, int k) {
        int sum = 0;
        int max = 0;
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        for (int i = 0; i < nums.length; i++) {
            map.putIfAbsent(sum += nums[i], i);
            max = Math.max(max, i - map.getOrDefault(sum - k, i));
        }
        return max;
    }

    void test(int[] nums, int k, int expected) {
        assertEquals(expected, maxSubArrayLen(nums, k));
        assertEquals(expected, maxSubArrayLen2(nums, k));
    }

    @Test
    public void test() {
        test(new int[] {1, 0, -1}, -1, 2);
        test(new int[] {1, 1, 3}, 3, 1);
        test(new int[] {1, -1, 5, -2, 3}, 3, 4);
        test(new int[] {-2, -1, 2, 1}, 1, 2);
        test(new int[] {-2, 1, -3, 4, -1, 2, 1, -5, 4}, 6, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSubArrayLen");
    }
}
