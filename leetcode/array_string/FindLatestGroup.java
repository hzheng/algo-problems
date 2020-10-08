import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1562: https://leetcode.com/problems/find-latest-group-of-size-m/
//
// Given an array arr that represents a permutation of numbers from 1 to n. You have a binary string
// of size n that initially has all its bits set to zero. At each step i (assuming both the binary
// string and arr are 1-indexed) from 1 to n, the bit at position arr[i] is set to 1. You are given
// an integer m and you need to find the latest step at which there exists a group of ones of length
// m. A group of ones is a contiguous substring of 1s such that it cannot be extended in either
// direction. Return the latest step at which there exists a group of ones of length exactly m. If
// no such group exists, return -1.
// Constraints:
// n == arr.length
// 1 <= n <= 10^5
// 1 <= arr[i] <= n
// All integers in arr are distinct.
// 1 <= m <= arr.length
public class FindLatestGroup {
    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 257 ms(21.48%), 58.6 MB(49.93%) for 114 tests
    public int findLatestStep(int[] arr, int m) {
        int n = arr.length;
        if (m == n) { return n; }

        NavigableMap<Integer, Integer> ranges = new TreeMap<>();
        ranges.put(0, n);
        for (int i = n - 1; i >= 0; i--) {
            int pos = arr[i] - 1;
            int start = ranges.floorKey(pos);
            int end = ranges.put(start, pos);
            ranges.put(pos + 1, end);
            if ((pos - start == m) || (end - pos - 1) == m) { return i; }
        }
        return -1;
    }

    // SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 263 ms(20.94%), 51 MB(85.99%) for 114 tests
    public int findLatestStep2(int[] arr, int m) {
        int n = arr.length;
        if (m == n) { return n; }

        NavigableSet<Integer> zeros = new TreeSet<Integer>();
        zeros.add(0);
        zeros.add(n + 1);
        for (int i = n - 1; i >= 0; i--) {
            int left = zeros.floor(arr[i]);
            int right = zeros.ceiling(arr[i]);
            if (arr[i] - left - 1 == m || right - arr[i] - 1 == m) { return i; }

            zeros.add(arr[i]);
        }

        return -1;
    }

    // time complexity: O(N), space complexity: O(N)
    // 5 ms(99.42%), 47.7 MB(99.15%) for 114 tests
    public int findLatestStep3(int[] arr, int m) {
        int n = arr.length;
        if (m == n) { return n; }

        int res = -1;
        int[] ones = new int[n + 2];
        for (int i = 0; i < n; i++) {
            int a = arr[i];
            int left = ones[a - 1];
            int right = ones[a + 1];
            ones[a - left] = ones[a + right] = left + right + 1;
            if (left == m || right == m) {
                res = i;
            }
        }
        return res;
    }

    // TODO: Union Find

    void test(int[] arr, int m, int expected) {
        assertEquals(expected, findLatestStep(arr, m));
        assertEquals(expected, findLatestStep2(arr, m));
        assertEquals(expected, findLatestStep3(arr, m));
    }

    @Test public void test() {
        test(new int[] {3, 5, 1, 2, 4}, 1, 4);
        test(new int[] {3, 1, 5, 4, 2}, 2, -1);
        test(new int[] {1}, 1, 1);
        test(new int[] {2, 1}, 2, 2);
        test(new int[] {3, 1, 5, 4, 2}, 5, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
