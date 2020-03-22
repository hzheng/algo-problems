import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1390: https://leetcode.com/problems/four-divisors/
//
// Given an integer array nums, return the sum of divisors of the integers in that array that have exactly four divisors.
// If there is no such integer in the array, return 0.
// Constraints:
// 1 <= nums.length <= 10^4
// 1 <= nums[i] <= 10^5
public class FourDivisors {
    // time complexity: O(N^1/2), space complexity: O(1)
    // 12 ms(21.41%), 41.6 MB(100%) for 18 tests
    public int sumFourDivisors(int[] nums) {
        int res = 0;
        for (int num : nums) {
            res += sumOfDivisors(num);
        }
        return res;
    }

    private int sumOfDivisors(int num) {
        int factor = 0;
        for (int i = 2; ; i++) {
            int square = i * i;
            if (square == num) return 0;
            if (square > num) break;

            if (num % i == 0) {
                if (factor == 0) {
                    factor = i;
                } else return 0;
            }
        }
        return (factor == 0) ? 0 : (factor + num / factor + 1 + num);
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, sumFourDivisors(nums));
    }

    @Test
    public void test() {
        test(new int[]{21, 4, 7}, 32);
        test(new int[]{16, 81, 625, 2401, 83521}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
