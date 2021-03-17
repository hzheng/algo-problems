import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1296: https://leetcode.com/problems/divide-array-in-sets-of-k-consecutive-numbers/
//
// Given an array of integers nums and a positive integer k, find whether it's possible to divide
// this array into sets of k consecutive numbers.
// Return True if it is possible. Otherwise, return False.
//
// Constraints:
// 1 <= k <= nums.length <= 10^5
// 1 <= nums[i] <= 10^9
public class DivideArrayInSetsOfKConsecutiveNumbers {
    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 117 ms(66.05%), 50.6 MB(61.23%) for 47 tests
    public boolean isPossibleDivide(int[] nums, int k) {
        if (nums.length % k != 0) { return false; }

        TreeMap<Integer, Integer> freq = new TreeMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        while (!freq.isEmpty()) {
            var entry = freq.pollFirstEntry();
            int num = entry.getKey();
            int count = entry.getValue();
            for (int a = num + 1; a < num + k; a++) {
                int cnt = freq.getOrDefault(a, 0);
                if (cnt < count) { return false; }

                if (cnt == count) {
                    freq.remove(a);
                } else {
                    freq.put(a, cnt - count);
                }
            }
        }
        return true;
    }

    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 146 ms(49.24%), 49.4 MB(69.61%) for 47 tests
    public boolean isPossibleDivide2(int[] nums, int k) {
        TreeMap<Integer, Integer> freq = new TreeMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        for (int num : freq.keySet()) {
            int count = freq.get(num);
            if (count == 0) { continue; }

            for (int i = k - 1; i >= 0; i--) {
                int diff = freq.getOrDefault(num + i, 0) - count;
                if (diff < 0) { return false; }

                freq.put(num + i, diff);
            }
        }
        return true;
    }

    // Hash Table + Heap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 63 ms(81.03%), 48.2 MB(87.82%) for 47 tests
    public boolean isPossibleDivide3(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        pq.addAll(freq.keySet());
        while (!pq.isEmpty()) {
            int cur = pq.poll();
            int count = freq.get(cur);
            if (count == 0) { continue; }

            for (int i = 0; i < k; i++) {
                int diff = freq.getOrDefault(cur + i, 0) - count;
                if (diff < 0) { return false; }

                freq.put(cur + i, diff);
            }
        }
        return true;
    }

    private void test(int[] nums, int k, boolean expected) {
        assertEquals(expected, isPossibleDivide(nums, k));
        assertEquals(expected, isPossibleDivide2(nums, k));
        assertEquals(expected, isPossibleDivide3(nums, k));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 3, 4, 4, 5, 6}, 4, true);
        test(new int[] {3, 2, 1, 2, 3, 4, 3, 4, 5, 9, 10, 11}, 3, true);
        test(new int[] {3, 3, 2, 2, 1, 1}, 3, true);
        test(new int[] {1, 2, 3, 4}, 3, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
