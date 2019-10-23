import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1224: https://leetcode.com/problems/maximum-equal-frequency/
//
// Given an array nums of positive integers, return the longest possible length of an array prefix
// of nums, such that it is possible to remove exactly one element from this prefix so that every
// number that has appeared in it will have the same number of occurrences.
// Constraints:
// 2 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^5
public class MaxEqualFrequency {
    // time complexity: O(N), space complexity: O(N)
    // 65 ms(50.50%), 48.9 MB(100%) for 42 tests
    // Hashtable
    public int maxEqualFreq(int[] nums) {
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        Map<Integer, Set<Integer>> freqMap = new HashMap<>();
        for (int num : countMap.keySet()) {
            freqMap.computeIfAbsent(countMap.get(num), x -> new HashSet<>()).add(num);
        }
        for (int i = nums.length - 1; ; i--) {
            int num = nums[i];
            int c = countMap.get(num);
            if (freqMap.size() == 1) {
                if (c == 1 || i == c - 1) {
                    return i + 1;
                }
            } else if (freqMap.size() == 2) {
                Iterator<Integer> itr = freqMap.keySet().iterator();
                Integer c1 = itr.next();
                Integer c2 = itr.next();
                if ((freqMap.get(c1).size() == 1 && (c1 == 1 || c1 == c2 + 1))
                    || (freqMap.get(c2).size() == 1 && (c2 == 1 || c2 == c1 + 1))) {
                    return i + 1;
                }
            }
            countMap.put(num, c - 1);
            if (c > 1) {
                freqMap.computeIfAbsent(c - 1, x -> new HashSet<>()).add(num);
            } else {
                countMap.remove(num);
            }
            freqMap.get(c).remove(num);
            if (freqMap.get(c).isEmpty()) {
                freqMap.remove(c);
            }
        }
    }

    // time complexity: O(N), space complexity: O(|Set(nums)|)
    // 59 ms(58.57%), 51.6 MB(100%) for 42 tests
    // Hashtable
    public int maxEqualFreq2(int[] nums) {
        Map<Integer, Integer> countMap = new HashMap<>();
        Map<Integer, Integer> freqMap = new HashMap<>();
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            countMap.put(nums[i], countMap.getOrDefault(nums[i], 0) + 1);
            int freq = countMap.get(nums[i]);
            freqMap.put(freq, freqMap.getOrDefault(freq, 0) + 1);

            int count = freqMap.get(freq) * freq;
            if (count == i + 1 && i != nums.length - 1) {
                res = i + 2;
            } else if (count == i) {
                res = i + 1;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(|Set(nums)|)
    // 6 ms(99.75%), 52.1 MB(100%) for 42 tests
    // Hashtable
    public int maxEqualFreq2_2(int[] nums) {
        final int MAX = 100001;
        int[] countMap = new int[MAX];
        int[] freqMap = new int[MAX];
        int res = 0;
        for (int i = 0, N = nums.length; i < N; i++) {
            int num = nums[i];
            int curCount = ++countMap[num];
            ++freqMap[countMap[num]];

            int count = freqMap[curCount] * curCount;
            if (count == i + 1 && i + 1 < N) {
                res = i + 2;
            } else if (count == i) {
                res = i + 1;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(|Set(nums)|)
    // 7 ms(97.90%), 50.51 MB(100%) for 42 tests
    // Hashtable
    public int maxEqualFreq3(int[] nums) {
        final int MAX = 100001;
        int[] countMap = new int[MAX];
        int[] freqMap = new int[MAX];
        int res = 0;
        for (int i = 0, N = nums.length; i < N; i++) {
            int num = nums[i];
            --freqMap[countMap[num]];
            int curCount = ++countMap[num];
            ++freqMap[curCount];

            int toDelete = i + 1 - freqMap[curCount] * curCount;
            if (toDelete == 0 && i + 1 < N) {
                res = i + 2;
            } else if ((toDelete == curCount + 1 || toDelete == 1) && freqMap[toDelete] == 1) {
                res = i + 1;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(|Set(nums)|)
    // 7 ms(97.90%), 50.5 MB(100%) for 42 tests
    // Hashtable
    public int maxEqualFreq4(int[] nums) {
        final int MAX = 100001;
        int[] countMap = new int[MAX];
        int[] freqMap = new int[MAX];
        int res = 0;
        for (int i = 0, N = nums.length, maxFreq = 0; i < N; i++) {
            int num = nums[i];
            --freqMap[countMap[num]];
            int curCount = ++countMap[num];
            ++freqMap[curCount];

            maxFreq = Math.max(maxFreq, curCount);
            if (maxFreq == 1 // all exactly once
                || maxFreq * freqMap[maxFreq] == i // all maxFreq except 1 once
                || (maxFreq - 1) * (freqMap[maxFreq - 1] + 1) == i // all maxFreq-1 except 1 maxFreq
            ) {
                res = i + 1;
            }
        }
        return res;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, maxEqualFreq(nums));
        assertEquals(expected, maxEqualFreq2(nums));
        assertEquals(expected, maxEqualFreq2_2(nums));
        assertEquals(expected, maxEqualFreq3(nums));
        assertEquals(expected, maxEqualFreq4(nums));
    }

    @Test
    public void test() {
        test(new int[]{2, 2, 1, 1, 5, 3, 3, 5}, 7);
        test(new int[]{1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5}, 13);
        test(new int[]{1, 1, 1, 2, 2, 2}, 5);
        test(new int[]{10, 2, 8, 9, 3, 8, 1, 5, 2, 3, 7, 6}, 8);
        test(new int[]{1, 2, 3, 1, 2, 3, 4, 4, 4, 4, 1, 2, 3, 5, 6}, 13);
        test(new int[]{1, 2, 3, 1, 2, 3, 4, 4, 4, 4, 1, 2, 3, 5, 6}, 13);
        test(new int[]{1, 1}, 2);
        test(new int[]{1, 2, 1, 2, 3, 3}, 5);
        test(new int[]{1, 3, 2, 3, 1, 2}, 5);
        test(new int[]{1, 1, 1, 1, 1, 1}, 6);
        test(new int[]{1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8, 42, 21,
                       45, 27, 78, 39, 78, 24, 47, 60, 22, 33, 45, 18, 56, 91, 93, 66, 79, 65, 43,
                       7, 57, 63, 74, 25, 11, 14, 100, 95, 19, 3, 22, 18, 94, 52, 91, 33, 95, 16,
                       93, 63, 65, 8, 88, 51, 47, 7, 51, 77, 36, 48, 89, 72, 81, 75, 13, 69, 9, 31,
                       16, 38, 34, 76, 7, 82, 10, 90, 64, 28, 22, 99, 40, 88, 27, 94, 85, 43, 75,
                       95, 86, 82, 46, 9, 74, 67, 51, 93, 97, 35, 2, 49}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
