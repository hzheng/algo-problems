import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1848: https://leetcode.com/problems/minimum-distance-to-the-target-element/
//
// Given an integer array nums (0-indexed) and two integers target and start, find an index i such
// that nums[i] == target and abs(i - start) is minimized. Note that abs(x) is the absolute value of
// x.
// Return abs(i - start).
// It is guaranteed that target exists in nums.
//
// Constraints:
// 1 <= nums.length <= 1000
// 1 <= nums[i] <= 10^4
// 0 <= start < nums.length
// target is in nums.
public class GetMinDistance {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.6 MB(55.56%) for 72 tests
    public int getMinDistance(int[] nums, int target, int start) {
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == target) {
                res = Math.min(res, Math.abs(i - start));
            }
        }
        return res;
    }

    private void test(int[] nums, int target, int start, int expected) {
        assertEquals(expected, getMinDistance(nums, target, start));
    }

    @Test public void test1() {
        test(new int[] {1, 2, 3, 4, 5}, 5, 3, 1);
        test(new int[] {1}, 1, 0, 0);
        test(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 1, 0, 0);
        test(new int[] {1, 3, 1, 1, 3, 2, 1, 1, 2, 1}, 3, 0, 1);

    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
