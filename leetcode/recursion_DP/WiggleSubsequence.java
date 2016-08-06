import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/wiggle-subsequence/
//
// A sequence of numbers is called a wiggle sequence if the differences between
// successive numbers strictly alternate between positive and negative. The
// first difference (if one exists) may be either positive or negative. A
// sequence with fewer than two elements is trivially a wiggle sequence.
// Given a sequence of integers, return the length of the longest subsequence
// that is a wiggle sequence.
public class WiggleSubsequence {
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 20.93%(1 ms)
    public int wiggleMaxLength(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        int[] seq = new int[n];
        return Math.max(wiggleMaxLength(nums, n, seq, 1),
                        wiggleMaxLength(nums, n, seq, 0));
    }

    private int wiggleMaxLength(int[] nums, int n, int[] seq, int bit) {
        seq[0] = nums[0];
        int len = 1;
        for (int i = 1; i < n; i++) {
            int num = nums[i];
            if ((len & 1) == bit && num > seq[len - 1]
                || (len & 1) == (1 - bit) && num < seq[len - 1]) {
                seq[len++] = num;
            } else {
                seq[len - 1] = num;
            }
        }
        return len;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats 56.36%(0 ms)
    public int wiggleMaxLength2(int[] nums) {
        return Math.max(wiggleMaxLength2(nums, 1), wiggleMaxLength2(nums, 0));
    }

    private int wiggleMaxLength2(int[] nums, int bit) {
        int last = (bit == 0) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int len = 0;
        for (int num : nums) {
            if ((len & 1) == bit && num > last
                || (len & 1) == (1 - bit) && num < last) {
                len++;
            }
            last = num;
        }
        return len;
    }

    // Dynamic Programming
    // https://leetcode.com/articles/wiggle-subsequence/
    // time complexity: O(N), space complexity: O(1)
    // beats 56.36%(0 ms)
    public int wiggleMaxLength3(int[] nums) {
        if (nums.length < 2) return nums.length;

        int down = 1;
        int up = 1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                up = down + 1;
            } else if (nums[i] < nums[i - 1]) {
                down = up + 1;
            }
        }
        return Math.max(down, up);
    }

    // Greedy
    // https://leetcode.com/articles/wiggle-subsequence/
    // time complexity: O(N), space complexity: O(1)
    // beats 56.36%(0 ms)
    public int wiggleMaxLength4(int[] nums) {
        if (nums.length < 2) return nums.length;

        int prevDiff = nums[1] - nums[0];
        int count = (prevDiff != 0) ? 2 : 1;
        for (int i = 2; i < nums.length; i++) {
            int diff = nums[i] - nums[i - 1];
            if ((diff > 0 && prevDiff <= 0) || (diff < 0 && prevDiff >= 0)) {
                count++;
                prevDiff = diff;
            }
        }
        return count;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, wiggleMaxLength(nums));
        assertEquals(expected, wiggleMaxLength2(nums));
        assertEquals(expected, wiggleMaxLength3(nums));
        assertEquals(expected, wiggleMaxLength4(nums));
    }

    @Test
    public void test1() {
        test(new int[] {3, 3, 3, 2, 5}, 3);
        test(new int[] {1, 7, 4, 9, 2, 5}, 6);
        test(new int[] {1, 17, 5, 10, 13, 15, 10, 5, 16, 8}, 7);
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, 2);
        test(new int[] {126, 37, 130, 225, 239, 77, 235, 333, 30, 69, 294, 128,
                        163, 17, 224, 229, 128, 59, 205, 265, 328, 259, 337, 93,
                        354, 316, 309, 67, 36, 88, 133, 359, 8, 335, 247, 209,
                        279, 94, 41, 62}, 25);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WiggleSubsequence");
    }
}
