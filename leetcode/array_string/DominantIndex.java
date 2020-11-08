import org.junit.Test;

import static org.junit.Assert.*;

// LC747: https://leetcode.com/problems/largest-number-at-least-twice-of-others/
//
// In a given integer array nums, there is always exactly one largest element. Find whether the
// largest element in the array is at least twice as much as every other number in the array.
// If it is, return the index of the largest element, otherwise return -1.
// Note:
// nums will have a length in the range [1, 50].
// Every nums[i] will be an integer in the range [0, 99].
public class DominantIndex {
    // 2-Pass
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.7 MB(5.93%) for 250 tests
    public int dominantIndex(int[] nums) {
        int max = Integer.MIN_VALUE;
        int maxIndex = -1;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] > max) {
                max = nums[i];
                maxIndex = i;
            }
        }
        for (int num : nums) {
            if (num != max && num * 2 > max) { return -1; }
        }
        return maxIndex;
    }

    // 1-Pass
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.7 MB(5.93%) for 250 tests
    public int dominantIndex2(int[] nums) {
        int maxIndex = -1;
        int max = Integer.MIN_VALUE;
        int secondMax = max;
        for (int i = nums.length - 1; i >= 0; i--) {
            if (nums[i] > max) {
                secondMax = max;
                max = nums[i];
                maxIndex = i;
            } else if (nums[i] > secondMax)  {
                secondMax = nums[i];
            }
        }
        return (max >= 2 * secondMax) ? maxIndex : -1;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, dominantIndex(nums));
        assertEquals(expected, dominantIndex2(nums));
    }

    @Test public void test() {
        test(new int[] {3, 6, 1, 0}, 1);
        test(new int[] {1, 2, 3, 4}, -1);
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 15}, -1);
        test(new int[] {0, 0, 3, 2}, -1);
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 16}, 8);
        test(new int[] {1, 2, 3, 8, 12, 21, 33, 98, 13, 20, 4}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
