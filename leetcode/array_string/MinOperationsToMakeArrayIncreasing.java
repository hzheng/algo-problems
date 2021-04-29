import org.junit.Test;

import static org.junit.Assert.*;

// LC1827: https://leetcode.com/problems/minimum-operations-to-make-the-array-increasing/
//
// You are given an integer array nums (0-indexed). In one operation, you can choose an element of
// the array and increment it by 1.
// For example, if nums = [1,2,3], you can choose to increment nums[1] to make nums = [1,3,3].
// Return the minimum number of operations needed to make nums strictly increasing.
// An array nums is strictly increasing if nums[i] < nums[i+1] for all 0 <= i < nums.length - 1.
// An array of length 1 is trivially strictly increasing.
//
// Constraints:
// 1 <= nums.length <= 5000
// 1 <= nums[i] <= 10^4
public class MinOperationsToMakeArrayIncreasing {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100%), 39.4 MB(65.88%) for 94 tests
    public int minOperations(int[] nums) {
        int res = 0;
        int prev = 0;
        for (int num : nums) {
            if (num <= prev) {
                res += ++prev - num;
            } else {
                prev = num;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 2 ms(88.77%), 39.6 MB(58.68%) for 94 tests
    public int minOperations2(int[] nums) {
        int res = 0;
        int prev = 0;
        for (int num : nums) {
            res += Math.max(0, prev - num + 1);
            prev = Math.max(num, prev + 1);
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, minOperations(nums));
    }

    @Test public void test1() {
        test(new int[] {1, 1, 1}, 3);
        test(new int[] {1, 5, 2, 4, 1}, 14);
        test(new int[] {8}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
