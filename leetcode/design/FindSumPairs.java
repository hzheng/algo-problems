import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1865: https://leetcode.com/problems/finding-pairs-with-a-certain-sum/
//
// You are given two integer arrays nums1 and nums2. You are tasked to implement a data structure
// that supports queries of two types:
// Add a positive integer to an element of a given index in the array nums2.
// Count the number of pairs (i, j) such that nums1[i] + nums2[j] equals a given value
// (0 <= i < nums1.length and 0 <= j < nums2.length).
// Implement the FindSumPairs class:
// FindSumPairs(int[] nums1, int[] nums2) Initializes the FindSumPairs object with two integer
// arrays nums1 and nums2.
// void add(int index, int val) Adds val to nums2[index], i.e., apply nums2[index] += val.
// int count(int tot) Returns the number of pairs (i, j) such that nums1[i] + nums2[j] == tot.
//
// Constraints:
// 1 <= nums1.length <= 1000
// 1 <= nums2.length <= 10^5
// 1 <= nums1[i] <= 10^9
// 1 <= nums2[i] <= 10^5
// 0 <= index < nums2.length
// 1 <= val <= 10^5
// 1 <= tot <= 10^9
// At most 1000 calls are made to add and count each.
public class FindSumPairs {
    // Hash Table
    // 224 ms(%), 72.8 MB(%) for 25 tests
    static class FindSumPairs1 {
        private final Map<Integer, Integer> map1 = new HashMap<>();
        private final Map<Integer, Integer> map2 = new HashMap<>();
        private final int[] arr2;

        public FindSumPairs1(int[] nums1, int[] nums2) {
            for (int a : nums1) {
                map1.put(a, map1.getOrDefault(a, 0) + 1);
            }
            for (int a : nums2) {
                map2.put(a, map2.getOrDefault(a, 0) + 1);
            }
            arr2 = nums2;
        }

        public void add(int index, int val) {
            int oldVal = arr2[index];
            int newVal = oldVal + val;
            arr2[index] = newVal;
            map2.put(oldVal, map2.get(oldVal) - 1);
            map2.put(newVal, map2.getOrDefault(newVal, 0) + 1);
        }

        public int count(int tot) {
            int res = 0;
            for (int a : map1.keySet()) {
                res += map1.get(a) * map2.getOrDefault(tot - a, 0);
            }
            return res;
        }
    }

    // Hash Table
    // 153 ms(%), 67.8 MB(%) for 25 tests
    static class FindSumPairs2 {
        private final int[] nums1;
        private final int[] nums2;
        private final Map<Integer, Integer> freq = new HashMap<>();

        public FindSumPairs2(int[] nums1, int[] nums2) {
            this.nums1 = nums1;
            this.nums2 = nums2;
            for (int x : nums2) {
                updateFreq(x, 1);
            }
        }

        private void updateFreq(int key, int delta) {
            freq.put(key, freq.getOrDefault(key, 0) + delta);
        }

        public void add(int index, int val) {
            updateFreq(nums2[index], -1);
            nums2[index] += val;
            updateFreq(nums2[index], 1);
        }

        public int count(int tot) {
            int res = 0;
            for (int a : nums1) {
                res += freq.getOrDefault(tot - a, 0);
            }
            return res;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "count", "add", "count", "count", "add", "add", "count"},
             new Object[][] {{new int[] {1, 1, 2, 2, 2, 3}, new int[] {1, 4, 5, 2, 5, 4}}, {7},
                             {3, 2}, {8}, {4}, {0, 1}, {1, 1}, {7}},
             new Object[] {null, 8, null, 2, 1, null, null, 11});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("FindSumPairs1");
            test1("FindSumPairs2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
