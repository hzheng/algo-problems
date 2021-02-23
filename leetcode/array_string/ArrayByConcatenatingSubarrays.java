import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1764: https://leetcode.com/problems/form-array-by-concatenating-subarrays-of-another-array/
//
// You are given a 2D integer array groups of length n. You are also given an integer array nums.
// You are asked if you can choose n disjoint subarrays from the array nums such that the ith
// subarray is equal to groups[i] (0-indexed), and if i > 0, the (i-1)th subarray appears before the
// ith subarray in nums (i.e. the subarrays must be in the same order as groups).
// Return true if you can do this task, and false otherwise.
// Note that the subarrays are disjoint if and only if there is no index k such that nums[k] belongs
// to more than one subarray. A subarray is a contiguous sequence of elements within an array.
//
// Constraints:
// groups.length == n
// 1 <= n <= 10^3
// 1 <= groups[i].length, sum(groups[i].length) <= 10^3
// 1 <= nums.length <= 10^3
//-10^7 <= groups[i][j], nums[k] <= 10^7
public class ArrayByConcatenatingSubarrays {
    // time complexity: O(M*N), space complexity: O(1)
    // 0 ms(100%), 39.6 MB(50.00%) for 92 tests
    public boolean canChoose(int[][] groups, int[] nums) {
        int cur = 0;
        int n = nums.length;
        for (int[] g : groups) {
            for (int i = 0, start = cur; i < g.length; i++) {
                if (cur == n) { return false; }

                if (g[i] == nums[cur]) {
                    cur++;
                } else {
                    cur = ++start;
                    i = -1;
                }
            }
        }
        return true;
    }

    // time complexity: O(M*N), space complexity: O(1)
    // 1 ms(100%), 42.7 MB(50.00%) for 92 tests
    public boolean canChoose2(int[][] groups, int[] nums) {
        for (int i = 0, start = 0, m = nums.length, n = groups.length; i < n; start++) {
            int[] g = groups[i];
            if (start + g.length > m) { return false; }

            if (search(g, nums, start)) {
                start += g.length - 1;
                i++;
            }
        }
        return true;
    }

    private boolean search(int[] group, int[] nums, int start) {
        for (int i = 0; i < group.length; i++) {
            if (group[i] != nums[i + start]) { return false; }
        }
        return true;
    }

    // TODO: KMP

    private void test(int[][] groups, int[] nums, boolean expected) {
        assertEquals(expected, canChoose(groups, nums));
        assertEquals(expected, canChoose2(groups, nums));
    }

    @Test public void test() {
        test(new int[][] {{1, -1, -1}, {3, -2, 0}}, new int[] {1, -1, 0, 1, -1, -1, 3, -2, 0},
             true);
        test(new int[][] {{10, -2}, {1, 2, 3, 4}}, new int[] {1, 2, 3, 4, 10, -2}, false);
        test(new int[][] {{1, 2, 3}, {3, 4}}, new int[] {7, 7, 1, 2, 3, 4, 7, 7}, false);
        test(new int[][] {{1, 2}}, new int[] {1, 3, 2}, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
