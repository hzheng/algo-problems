import org.junit.Test;

import static org.junit.Assert.*;

// LC1929: https://leetcode.com/problems/concatenation-of-array/
//
// Given an integer array nums of length n, you want to create an array ans of length 2n where
// ans[i] == nums[i] and ans[i + n] == nums[i] for 0 <= i < n (0-indexed).
// Specifically, ans is the concatenation of two nums arrays.
//
// Return the array ans.
//
// Constraints:
// n == nums.length
// 1 <= n <= 1000
// 1 <= nums[i] <= 1000
public class ConcatenationOfArray {
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(%), 47.6 MB(%) for 91 tests
    public int[] getConcatenation(int[] nums) {
        int n = nums.length;
        int[] res = new int[n * 2];
        for (int i = 0; i < n; i++) {
            res[i] = res[i + n] = nums[i];
        }
        return res;
    }

    private void test(int[] nums, int[] expected) {
        assertArrayEquals(expected, getConcatenation(nums));
    }

    @Test public void test1() {
        test(new int[] {1, 2, 1}, new int[] {1, 2, 1, 1, 2, 1});
        test(new int[] {1, 3, 2, 1}, new int[] {1, 3, 2, 1, 1, 3, 2, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
