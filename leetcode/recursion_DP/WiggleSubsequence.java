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
    // Dynamic Programming + Greedy
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

    void test(int[] nums, int expected) {
        assertEquals(expected, wiggleMaxLength(nums));
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
