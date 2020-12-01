import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1675: https://leetcode.com/problems/minimize-deviation-in-array/
//
// You are given an array nums of n positive integers. You can perform two types of operations on
// any element of the array any number of times:
// If the element is even, divide it by 2.
// If the element is odd, multiply it by 2.
// The deviation of the array is the maximum difference between any two elements in the array.
// Return the minimum deviation the array can have after performing some number of operations.
//
// Constraints:
// n == nums.length
// 2 <= n <= 10^5
// 1 <= nums[i] <= 10^9
public class MinDeviation {
    // SortedSet + Bit Manipulation
    // time complexity: O(N*log(N)*log(MAX)), space complexity: O(N)
    // 137 ms(52.41%), 57.2 MB(47.59%) for 76 tests
    public int minimumDeviation(int[] nums) {
        SortedSet<Long> set = new TreeSet<>();
        for (long num : nums) {
            long bound = ((num & 1) == 0 ? num : (num << 1));
            num /= (num & -num); // or: for (; (num & 1) == 0; num >>=1) {}
            set.add((num << 32) | bound);
        }
        for (int res = Integer.MAX_VALUE; ; ) {
            long min = set.first();
            int minVal = (int)(min >> 32);
            int minBound = (int)(min & Integer.MAX_VALUE);
            int maxVal = (int)(set.last() >> 32);
            res = Math.min(res, maxVal - minVal);
            if (res == 0 || minVal == minBound) { return res; }

            set.remove(min);
            set.add(((long)minVal << 33) | minBound);
        }
    }

    // SortedSet
    // time complexity: O(N*log(N)*log(MAX)), space complexity: O(N)
    // 159 ms(43.45%), 53.9 MB(77.24%) for 76 tests
    public int minimumDeviation2(int[] nums) {
        SortedSet<Integer> set = new TreeSet<>();
        for (int num : nums) {
            set.add((num & 1) == 0 ? num : num * 2);
        }
        for (int res = Integer.MAX_VALUE; ; ) {
            int max = set.last();
            res = Math.min(res, max - set.first());
            if (res == 0 || (max & 1) != 0) { return res; }

            set.remove(max);
            set.add(max / 2);
        }
    }

    // Heap
    // time complexity: O(N*log(N)*log(MAX)), space complexity: O(N)
    // 93 ms(68.28%), 54.9 MB(66.90%) for 76 tests
    public int minimumDeviation3(int[] nums) {
        int min = Integer.MAX_VALUE;
        PriorityQueue<Integer> maxQ = new PriorityQueue<>(Collections.reverseOrder());
        for (int num : nums) {
            num = (num & 1) == 0 ? num : num * 2;
            maxQ.offer(num);
            min = Math.min(min, num);
        }
        for (int res = Integer.MAX_VALUE; ; ) {
            int max = maxQ.poll();
            res = Math.min(res, max - min);
            if (res == 0 || (max & 1) != 0) { return res; }

            maxQ.offer(max / 2);
            min = Math.min(min, max / 2);
        }
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, minimumDeviation(nums));
        assertEquals(expected, minimumDeviation2(nums));
        assertEquals(expected, minimumDeviation3(nums));
    }

    @Test public void test() {
        test(new int[] {10, 4, 3}, 2);
        test(new int[] {9, 4, 3, 6, 2}, 7);
        test(new int[] {8, 1, 2, 1}, 0);
        test(new int[] {3, 5}, 1);
        test(new int[] {1, 2, 3, 4}, 1);
        test(new int[] {4, 1, 5, 20, 3}, 3);
        test(new int[] {2, 10, 8}, 3);
        test(new int[] {165, 319, 305}, 25);
        test(new int[] {610, 778, 846, 733, 395}, 236);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
