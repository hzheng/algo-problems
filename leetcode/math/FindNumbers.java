import org.junit.Test;

import static org.junit.Assert.*;

// LC1295: https://leetcode.com/problems/find-numbers-with-even-number-of-digits/
//
// Given an array nums of integers, return how many of them contain an even number of digits.
//
// Constraints:
// 1 <= nums.length <= 500
// 1 <= nums[i] <= 10^5
public class FindNumbers {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.7 MB(58.96%) for 104 tests
    public int findNumbers(int[] nums) {
        int res = 0;
        for (int num : nums) {
            //res += (int)(Math.log(num) / Math.log(10) + 1E-8) % 2;
            res += (int)(Math.log10(num)) % 2;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(94.57%), 38.4 MB(82.96%) for 104 tests
    public int findNumbers2(int[] nums) {
        int res = 0;
        for (int num : nums) {
            int digits = 0;
            for (; num > 0; num /= 10, digits++) {}
            res += 1 - digits % 2;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(94.57%), 38.5 MB(82.96%) for 104 tests
    public int findNumbers3(int[] nums) {
        int res = 0;
        for (int num : nums) {
            res += 1 - Integer.toString(num).length() % 2;
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, findNumbers(nums));
        assertEquals(expected, findNumbers2(nums));
        assertEquals(expected, findNumbers3(nums));
    }

    @Test public void test() {
        test(new int[] {12, 345, 2, 6, 7896}, 2);
        test(new int[] {555, 901, 482, 1771}, 1);
        test(new int[] {555, 901, 482, 1771, 1234, 45678, 123456}, 3);
        test(new int[] {1, 555, 901, 482, 1771, 1234, 45678, 123456}, 3);
        test(new int[] {1000, 100000, 999, 200}, 2);
        test(new int[] {1000}, 1);
        test(new int[] {100000}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
