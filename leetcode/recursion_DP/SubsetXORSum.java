import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1863: https://leetcode.com/problems/sum-of-all-subset-xor-totals/
//
/// The XOR total of an array is defined as the bitwise XOR of all its elements, or 0 if the array
// is empty.
// For example, the XOR total of the array [2,5,6] is 2 XOR 5 XOR 6 = 1.
// Given an array nums, return the sum of all XOR totals for every subset of nums.
// Note: Subsets with the same elements should be counted multiple times.
// An array a is a subset of an array b if a can be obtained from b by deleting some (possibly zero)
// elements of b.
//
// Constraints:
// 1 <= nums.length <= 12
// 1 <= nums[i] <= 20
public class SubsetXORSum {
    // Bit Manipulation
    // time complexity: O(N*2^N), space complexity: O(1)
    // 4 ms(%), 36.2 MB(%) for 47 tests
    public int subsetXORSum(int[] nums) {
        int n = nums.length;
        int res = 0;
        for (int mask = (1 << n) - 1; mask > 0; mask--) {
            int xor = 0;
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) != 0) {
                    xor ^= nums[i];
                }
            }
            res += xor;
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N*2^N), space complexity: O(N)
    // 0 ms(%), 36.2 MB(%) for 47 tests
    public int subsetXORSum2(int[] nums) {
        return dfs(nums, 0, 0);
    }

    private int dfs(int[] nums, int cur, int xor) {
        if (cur == nums.length) { return xor; }

        return dfs(nums, cur + 1, xor ^ nums[cur]) + dfs(nums, cur + 1, xor);
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(%), 36.2 MB(%) for 47 tests
    public int subsetXORSum3(int[] nums) {
        int res = 0;
        for (int a : nums) {
            res |= a;
        }
        return res * (1 << nums.length - 1);
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, subsetXORSum(nums));
        assertEquals(expected, subsetXORSum2(nums));
        assertEquals(expected, subsetXORSum3(nums));
    }

    @Test public void test1() {
        test(new int[] {1, 3}, 6);
        test(new int[] {5, 1, 6}, 28);
        test(new int[] {3, 4, 5, 6, 7, 8}, 480);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
