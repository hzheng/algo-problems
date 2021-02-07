import org.junit.Test;

import static org.junit.Assert.*;

// LC1752: https://leetcode.com/problems/check-if-array-is-sorted-and-rotated/
//
// Given an array nums, return true if the array was originally sorted in non-decreasing order, then
// rotated some number of positions (including zero). Otherwise, return false.
// There may be duplicates in the original array.
// Note: An array A rotated by x positions results in an array B of the same length such that
// A[i] == B[(i+x) % A.length], where % is the modulo operation.
//
// Constraints:
// 1 <= nums.length <= 100
// 1 <= nums[i] <= 100
public class CheckRotatedSorted {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.8 MB(20.00%) for 96 tests
    public boolean check(int[] nums) {
        boolean needRotate = false;
        int n = nums.length;
        for (int i = 1; i < n; i++) {
            if (nums[i] < nums[i - 1]) {
                if (needRotate) { return false; }
                needRotate = true;
            }
        }
        return !needRotate || nums[n - 1] <= nums[0];
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(20.00%) for 96 tests
    public boolean check2(int[] nums) {
        for (int i = 0, n = nums.length, rotated = 0; i < n; i++) {
            if (nums[i] > nums[(i + 1) % n] && ++rotated > 1) { return false; }
        }
        return true;
    }

    private void test(int[] nums, boolean expected) {
        assertEquals(expected, check(nums));
        assertEquals(expected, check2(nums));
    }

    @Test public void test() {
        test(new int[] {3, 4, 5, 1, 2}, true);
        test(new int[] {2, 1, 3, 4}, false);
        test(new int[] {1, 2, 3}, true);
        test(new int[] {1, 1, 1}, true);
        test(new int[] {2, 1}, true);

    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
