import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC674: https://leetcode.com/problems/longest-continuous-increasing-subsequence/
//
// Given an unsorted array of integers, find the length of longest continuous
// increasing subsequence (subarray).
public class FindLengthOfLCIS {
    // beats 61.41%(6 ms for 36 tests)
    public int findLengthOfLCIS(int[] nums) {
        int maxLen = (nums.length == 0) ? 0 : 1;
        for (int i = 1, count = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                maxLen = Math.max(maxLen, ++count);
            } else {
                count = 1;
            }
        }
        return maxLen;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, findLengthOfLCIS(nums));
    }

    @Test
    public void test() {
        test(new int[] {}, 0);
        test(new int[] {1, 3, 5, 4, 7}, 3);
        test(new int[] {2, 2, 2, 2}, 1);
        test(new int[] {1, 2, 2, 1, 2, 3, 4, 4, 5, 6, 7, 8}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
