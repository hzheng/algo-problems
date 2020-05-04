import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

// LC1438: https://leetcode.com/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
//
// Given an array of integers nums and an integer limit, return the size of the longest continuous
// subarray such that the absolute difference between any two elements is less than or equal to
// limit. In case there is no subarray satisfying the given condition return 0.
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^9
// 0 <= limit <= 10^9
public class LongestSubarrayWithLimitDiff {
    // time complexity: O(N^2), space complexity: O(1)
    // 4 ms(100%), 47.9 MB(100%) for 54 tests
    public int longestSubarray(int[] nums, int limit) {
        int maxLen = 1;
        for (int i = 1, start = 0, max = nums[0], min = max; i < nums.length; i++) {
            int cur = nums[i];
            int oldLen = maxLen;
            maxLen = Math.max(maxLen, i - start + 1);
            if (cur >= min && cur <= max) { continue; }

            if ((cur < min) && (max - cur <= limit) || ((cur > max) && (cur - min <= limit))) {
                max = Math.max(max, cur);
                min = Math.min(min, cur);
                continue;
            }
            maxLen = oldLen; // restore
            for (max = min = cur, start = i - 1; ; start--) {
                if (start < 0 || Math.abs(nums[start] - cur) > limit) {
                    start++;
                    break;
                }
                max = Math.max(max, nums[start]);
                min = Math.min(min, nums[start]);
            }
        }
        return maxLen;
    }

    // Deque (monoqueue)
    // time complexity: O(N), space complexity: O(N)
    // 17 ms(100%), 48.1 MB(100%) for 54 tests
    public int longestSubarray2(int[] nums, int limit) {
        Deque<Integer> maxQ = new ArrayDeque<>();
        Deque<Integer> minQ = new ArrayDeque<>();
        int head = 0;
        int tail = 0;
        for (; tail < nums.length; tail++) {
            int cur = nums[tail];
            for (; !maxQ.isEmpty() && cur > maxQ.peekLast(); maxQ.pollLast()) {}
            for (; !minQ.isEmpty() && cur < minQ.peekLast(); minQ.pollLast()) {}
            maxQ.offerLast(cur);
            minQ.offerLast(cur);
            int max = maxQ.peekFirst();
            int min = minQ.peekFirst();
            if (max - min > limit) {
                if (max == nums[head]) {
                    maxQ.pollFirst();
                }
                if (min == nums[head]) {
                    minQ.pollFirst();
                }
                head++;
            }
        }
        return tail - head;
    }

    // SortedMap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 42 ms(52.47%), 48.1 MB(100%) for 54 tests
    public int longestSubarray3(int[] nums, int limit) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        int head = 0;
        int tail = 0;
        for (; tail < nums.length; tail++) {
            int cur = nums[tail];
            map.put(cur, map.getOrDefault(cur, 0) + 1);
            if (map.lastKey() - map.firstKey() > limit) {
                decreaseCount(map, nums[head++]);
            }
        }
        return tail - head;
    }

    private void decreaseCount(Map<Integer, Integer> map, int key) {
        int count = map.get(key) - 1;
        if (count == 0) {
            map.remove(key);
        } else {
            map.put(key, count);
        }
    }

    // SortedSet
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 56 ms(36.16%), 54.8 MB(100%) for 54 tests
    public int longestSubarray4(int[] nums, int limit) {
        SortedSet<Integer> set =
                new TreeSet<>((a, b) -> nums[a] == nums[b] ? a - b : nums[a] - nums[b]);
        int head = 0;
        int tail = 0;
        for (; tail < nums.length; tail++) {
            set.add(tail);
            if (nums[set.last()] - nums[set.first()] > limit) {
                set.remove(head++);
            }
        }
        return tail - head;
    }

    // TODO: heap
    // TODO: RMQ

    @Test public void test() {
        test(new int[] {8, 2, 4, 7}, 4, 2);
        test(new int[] {10, 1, 2, 4, 7, 2}, 5, 4);
        test(new int[] {10, 1, 2, 4, 7, 2}, 5, 4);
        test(new int[] {4, 2, 2, 2, 4, 4, 2, 2}, 0, 3);
        test(new int[] {8, 4, 0}, 3, 1);
        test(new int[] {10, 8, 6, 7, 5, 4, 1, 6, 4, 4}, 4, 5);
        test(new int[] {10, 8, 6, 7, 5, 4, 1, 6, 4, 1, 1, 1, 1, 1, 10, 10, 10, 10}, 4, 6);
        test(new int[] {10, 8, 6, 7, 5, 4, 1, 6, 4, 1, 1, 1, 1, 1}, 4, 6);
        test(new int[] {2, 2, 3, 3, 4, 4, 8, 8, 8}, 2, 6);
    }

    private void test(int[] nums, int limit, int expected) {
        assertEquals(expected, longestSubarray(nums, limit));
        assertEquals(expected, longestSubarray2(nums, limit));
        assertEquals(expected, longestSubarray3(nums, limit));
        assertEquals(expected, longestSubarray4(nums, limit));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
