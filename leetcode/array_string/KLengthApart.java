import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

// LC1437: https://leetcode.com/problems/check-if-all-1s-are-at-least-length-k-places-away/
//
// Given an array nums of 0s and 1s and an integer k, return True if all 1's are at least k places
// away from each other, otherwise return False.
// Constraints
// 1 <= nums.length <= 10^5
// 0 <= k <= nums.length
// nums[i] is 0 or 1
public class KLengthApart {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100%), 49.8 MB(100%) for 62 tests
    public boolean kLengthApart(int[] nums, int k) {
        for (int i = 0, prev = -1; i < nums.length; i++) {
            if (nums[i] == 0) { continue; }

            if (prev >= 0 && i - prev <= k) { return false; }

            prev = i;
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100%), 50.5 MB(100%) for 62 tests
    public boolean kLengthApart2(int[] nums, int k) {
        for (int i = 0, prev = -k - 1; i < nums.length; i++) {
            if (nums[i] == 1) {
                if (i - prev <= k) { return false; }
                prev = i;
            }
        }
        return true;
    }

    @Test public void test() {
        test(new int[] {1, 0, 0, 0, 1, 0, 0, 1}, 2, true);
        test(new int[] {1, 0, 0, 1, 0, 1}, 2, false);
        test(new int[] {1, 1, 1, 1, 1}, 0, true);
        test(new int[] {0, 1, 0, 1}, 1, true);
    }

    private void test(int[] nums, int k, boolean expected) {
        assertEquals(expected, kLengthApart(nums, k));
        assertEquals(expected, kLengthApart2(nums, k));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
