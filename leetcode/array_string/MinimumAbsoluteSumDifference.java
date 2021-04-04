import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1818: https://leetcode.com/problems/minimum-absolute-sum-difference/
//
// You are given two positive integer arrays nums1 and nums2, both of length n.
// The absolute sum difference of arrays nums1 and nums2 is defined as the sum of
// |nums1[i] - nums2[i]| for each 0 <= i < n (0-indexed).
// You can replace at most one element of nums1 with any other element in nums1 to minimize the
// absolute sum difference.
// Return the minimum absolute sum difference after replacing at most one element in the array
// nums1. Since the answer may be large, return it modulo 10^9 + 7.
//
// Constraints:
// n == nums1.length
// n == nums2.length
// 1 <= n <= 10^5
// 1 <= nums1[i], nums2[i] <= 10^5
public class MinimumAbsoluteSumDifference {
    private static final int MOD = 1_000_000_007;

    // SortedSet
    // time complexity: O(N* log(N)), space complexity: O(N)
    // 65 ms(%), 55.4 MB(%) for 43 tests
    public int minAbsoluteSumDiff(int[] nums1, int[] nums2) {
        TreeSet<Integer> set = new TreeSet<>();
        for (int num : nums1) {
            set.add(num);
        }
        long res = 0;
        int maxGain = 0;
        for (int i = 0, n = nums1.length; i < n; i++) {
            int diff = Math.abs(nums1[i] - nums2[i]);
            res += diff;
            if (diff == 0 || maxGain >= diff) { continue; }

            int newDiff = diff;
            Integer ceil = set.ceiling(nums2[i]);
            if (ceil != null) {
                newDiff = Math.min(newDiff, ceil - nums2[i]);
            }
            Integer floor = set.floor(nums2[i]);
            if (floor != null) {
                newDiff = Math.min(newDiff, nums2[i] - floor);
            }
            maxGain = Math.max(maxGain, diff - newDiff);
        }
        return (int)((res - maxGain) % MOD);
    }

    private void test(int[] nums1, int[] nums2, int expected) {
        assertEquals(expected, minAbsoluteSumDiff(nums1, nums2));
    }

    @Test public void test() {
        test(new int[] {1, 7, 5}, new int[] {2, 3, 5}, 3);
        test(new int[] {2, 4, 6, 8, 10}, new int[] {2, 4, 6, 8, 10}, 0);
        test(new int[] {1, 10, 4, 4, 2, 7}, new int[] {9, 3, 5, 1, 7, 4}, 20);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
