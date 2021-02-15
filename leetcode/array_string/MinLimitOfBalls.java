import org.junit.Test;

import static org.junit.Assert.*;

// LC1760: https://leetcode.com/problems/minimum-limit-of-balls-in-a-bag/
//
// You are given an integer array nums where the ith bag contains nums[i] balls. You are also given
// an integer maxOperations. You can perform the following operation at most maxOperations times:
// Take any bag of balls and divide it into two new bags with a positive number of balls.
// For example, a bag of 5 balls can become two new bags of 1 and 4 balls, or two new bags of 2 and
// 3 balls. Your penalty is the maximum number of balls in a bag. You want to minimize your penalty
// after the operations. Return the minimum possible penalty after performing the operations.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= maxOperations, nums[i] <= 10^9
public class MinLimitOfBalls {
    // Binary Search
    // time complexity: O(N*log(MAX-MIN)), space complexity: O(1)
    // 26 ms(100.00%), 51.9 MB(50.00%) for 56 tests
    public int minimumSize(int[] nums, int maxOperations) {
        int low = 1;
        for (int high = 1000_000_000; low < high; ) {
            int mid = (low + high) >>> 1;
            if (ok(nums, maxOperations, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean ok(int[] nums, int times, int max) {
        int total = 0;
        for (int a : nums) {
            total += (a - 1) / max;
        }
        return total <= times;
    }

    private void test(int[] nums, int maxOperations, int expected) {
        assertEquals(expected, minimumSize(nums, maxOperations));
    }

    @Test public void test() {
        test(new int[] {9}, 2, 3);
        test(new int[] {2, 4, 8, 2}, 4, 2);
        test(new int[] {7, 17}, 2, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
