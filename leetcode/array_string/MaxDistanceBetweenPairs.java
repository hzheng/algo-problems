import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1855: https://leetcode.com/problems/maximum-distance-between-a-pair-of-values/
//
// You are given two non-increasing 0-indexed integer arrays nums1 and nums2.
// A pair of indices (i, j), where 0 <= i < nums1.length and 0 <= j < nums2.length, is valid if both
// i <= j and nums1[i] <= nums2[j]. The distance of the pair is j - i.
// Return the maximum distance of any valid pair (i, j). If there are no valid pairs, return 0.
// An array arr is non-increasing if arr[i-1] >= arr[i] for every 1 <= i < arr.length.
//
// Constraints:
// 1 <= nums1.length <= 10^5
// 1 <= nums2.length <= 10^5
// 1 <= nums1[i], nums2[j] <= 10^5
// Both nums1 and nums2 are non-increasing.
public class MaxDistanceBetweenPairs {
    // Binary Search
    // time complexity: O(N*log(M)), space complexity: O(1)
    // 9 ms(41.51%), 51.8 MB(88.15%) for 30 tests
    public int maxDistance(int[] nums1, int[] nums2) {
        int res = 0;
        for (int i = 0, n = nums1.length; i < n; i++) {
            int index = find(nums2, i, nums1[i]);
            res = Math.max(res, index - i);
        }
        return res;
    }

    private int find(int[] arr, int start, int key) {
        int low = start;
        for (int high = arr.length - 1; low < high; ) {
            int mid = (low + high + 1) >>> 1;
            if (arr[mid] >= key) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    // Two Pointers
    // time complexity: O(N+M), space complexity: O(1)
    // 2 ms(100.00%), 52 MB(74.08%) for 30 tests
    public int maxDistance2(int[] nums1, int[] nums2) {
        int res = 0;
        for (int i1 = 0, i2 = 0, n1 = nums1.length, n2 = nums2.length; i1 < n1 && i2 < n2; ) {
            if (nums1[i1] > nums2[i2]) {
                i1++;
            } else {
                res = Math.max(res, i2++ - i1);
            }
        }
        return res;
    }

    private void test(int[] nums1, int[] nums2, int expected) {
        assertEquals(expected, maxDistance(nums1, nums2));
        assertEquals(expected, maxDistance2(nums1, nums2));
    }

    @Test public void test1() {
        test(new int[] {55, 30, 5, 4, 2}, new int[] {100, 20, 10, 10, 5}, 2);
        test(new int[] {2, 2, 2}, new int[] {10, 10, 1}, 1);
        test(new int[] {30, 29, 19, 5}, new int[] {25, 25, 25, 25, 25}, 2);
        test(new int[] {5, 4}, new int[] {3, 2}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
