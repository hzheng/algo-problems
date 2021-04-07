import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1636: https://leetcode.com/problems/sort-array-by-increasing-frequency/
//
// Given an array of integers nums, sort the array in increasing order based on the frequency of the
// values. If multiple values have the same frequency, sort them in decreasing order.
// Return the sorted array.
//
// Constraints:
// 1 <= nums.length <= 100
// -100 <= nums[i] <= 100
public class FrequencySort2 {
    // Heap
    // time complexity: O(N*log(N)), space complexity: O(MAX-MIN)
    // 2 ms(99.08%), 38.9 MB(82.31%) for 180 tests
    public int[] frequencySort(int[] nums) {
        int max = nums[0];
        int min = nums[0];
        for (int num : nums) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        int[] freq = new int[max - min + 1];
        for (int num : nums) {
            freq[num - min]++;
        }
        PriorityQueue<int[]> pq =
                new PriorityQueue<>((a, b) -> (a[0] == b[0]) ? a[1] - b[1] : b[0] - a[0]);
        for (int i = min; i <= max; i++) {
            int count = freq[i - min];
            if (count > 0) {
                pq.offer(new int[] {count, i});
            }
        }
        int[] res = new int[nums.length];
        for (int i = nums.length - 1; i >= 0; ) {
            int[] cur = pq.poll();
            for (int k = cur[0]; k > 0; k--) {
                res[i--] = cur[1];
            }
        }
        return res;
    }

    // Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 9 ms(25.03%), 39 MB(82.31%) for 180 tests
    public int[] frequencySort2(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, 1 + freq.getOrDefault(num, 0));
        }
        var pq = new PriorityQueue<>(
                Comparator.<Integer, Integer>comparing(freq::get).thenComparing(i -> -i));
        for (int num : nums) {
            pq.offer(num);
        }
        int[] res = new int[nums.length];
        for (int i = 0, n = nums.length; i < n; i++) {
            res[i] = pq.poll();
        }
        return res;
    }

    // Sort + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 11 ms(17.76%), 39.1 MB(66.97%) for 180 tests
    public int[] frequencySort3(int[] nums) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        return IntStream.of(nums).boxed().sorted((a, b) -> {
            int diff = freq.get(a) - freq.get(b);
            return (diff == 0) ? b - a : diff;
        }).mapToInt(Integer::intValue).toArray();
    }

    private void test(int[] nums, int[] expected) {
        assertArrayEquals(expected, frequencySort(nums));
        assertArrayEquals(expected, frequencySort2(nums));
        assertArrayEquals(expected, frequencySort3(nums));
    }

    @Test public void test() {
        test(new int[] {1, 1, 2, 2, 2, 3}, new int[] {3, 1, 1, 2, 2, 2});
        test(new int[] {2, 3, 1, 3, 2}, new int[] {1, 3, 3, 2, 2});
        test(new int[] {-1, 1, -6, 4, 5, -6, 1, 4, 1}, new int[] {5, -1, 4, 4, -6, -6, 1, 1, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
