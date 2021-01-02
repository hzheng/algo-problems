import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

// LC1122: https://leetcode.com/problems/relative-sort-array/
//
// Given two arrays arr1 and arr2, the elements of arr2 are distinct, and all elements in arr2 are
// also in arr1. Sort the elements of arr1 such that the relative ordering of items in arr1 are the
// same as in arr2.  Elements that don't appear in arr2 should be placed at the end of arr1 in
// ascending order.
//
// Constraints:
// 1 <= arr1.length, arr2.length <= 1000
// 0 <= arr1[i], arr2[i] <= 1000
// All the elements of arr2 are distinct.
// Each arr2[i] is in arr1.
public class RelativeSortArray {
    // time complexity: O(N+MAX), space complexity: O(MAX)
    // 1 ms(76.29%), 38.7 MB(88.93%) for 16 tests
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int max = Arrays.stream(arr1).max().getAsInt();
        int[] count = new int[max + 1];
        for (int a : arr1) {
            count[a]++;
        }
        int[] res = new int[arr1.length];
        int i = 0;
        for (int a : arr2) {
            for (; count[a]-- > 0; res[i++] = a) {}
        }
        for (int a = 0; a <= max; a++) {
            for (; count[a]-- > 0; res[i++] = a) {}
        }
        return res;
    }

    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 4 ms(37.04%), 38.9 MB(83.78%) for 16 tests
    public int[] relativeSortArray2(int[] arr1, int[] arr2) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int n : arr1) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }
        int i = 0;
        for (int a : arr2) {
            for (int j = map.get(a); j > 0; j--) {
                arr1[i++] = a;
            }
            map.remove(a);
        }
        for (int a : map.keySet()) {
            for (int j = map.get(a); j > 0; j--) {
                arr1[i++] = a;
            }
        }
        return arr1;
    }

    // Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 4 ms(37.04%), 38.9 MB(83.78%) for 16 tests
    public int[] relativeSortArray3(int[] arr1, int[] arr2) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = arr2.length - 1; i >= 0; i--) {
            map.put(arr2[i], i);
        }
        int n = arr1.length;
        PriorityQueue<Integer> pq = new PriorityQueue<>(n, Comparator
                .comparingInt(a -> map.getOrDefault(a, n + a)));
        int[] res = new int[n];
        for (int a : arr1) {
            pq.offer(a);
        }
        for (int i = 0; i < n; i++) {
            res[i] = pq.poll();
        }
        return res;
    }

    private void test(int[] arr1, int[] arr2, int[] expected) {
        assertArrayEquals(expected, relativeSortArray(arr1, arr2));
        assertArrayEquals(expected, relativeSortArray2(arr1, arr2));
        assertArrayEquals(expected, relativeSortArray3(arr1, arr2));
    }

    @Test public void test() {
        test(new int[] {2, 3, 1, 3, 2, 4, 6, 7, 9, 2, 19}, new int[] {2, 1, 4, 3, 9, 6},
             new int[] {2, 2, 2, 1, 4, 3, 3, 9, 6, 7, 19});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
