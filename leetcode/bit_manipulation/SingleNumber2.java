import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/single-number-ii/
//
// Given an array of integers, every element appears three times except for one.
// Find that single one.
// Your algorithm should have a linear runtime complexity. Could you implement
// it without using extra memory?
public class SingleNumber2 {
    // beats 50.74%(5 ms)
    public int singleNumber(int[] nums) {
        int res = 0;
        int mask = 1;
        for (int i = 0; i < 32; i++) {
            int sum = 0;
            for (int num : nums) {
                if ((num & mask) != 0) { // don't use > 0(could be negative)
                    sum++;
                }
            }
            if (sum % 3 != 0) {
                res |= mask;
            }
            mask <<= 1;
        }
        return res;
    }

    public int singleNumber2(int[] nums) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            int sum = 0;
            for (int j = 0; j < nums.length; j++) {
                if (((nums[j] >> i) & 1) == 1) {
                    sum++;
                }
            }
            sum %= 3;
            if (sum != 0) {
                res |= sum << i;
            }
        }
        return res;
    }

    // https://traceformula.blogspot.com/2015/08/single-number-ii-how-to-come-up-with.html
    // this works even the exceptional number occurs any times other than one
    // beats 87.51%(1 ms)
    public int singleNumber3(int[] nums) {
        int a = 0;
        int b = 0;
        for (int x : nums) {
            a = b & (a ^ x);
            b = a | (b ^ x);
        }
        return b;
    }

    // Solution of Choice
    // https://discuss.leetcode.com/topic/2031/challenge-me-thx
    public int singleNumber4(int[] nums) {
        int ones = 0; // holds XOR of all the elements which have appeared once
        int twos = 0; // holds XOR of all the elements which have appeared twice
        for (int x : nums) {
            ones = (ones ^ x) & ~twos;
            twos = (twos ^ x) & ~ones;
        }
        return ones;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, singleNumber(nums));
        assertEquals(expected, singleNumber2(nums));
        assertEquals(expected, singleNumber3(nums));
        assertEquals(expected, singleNumber4(nums));
    }

    @Test
    public void test1() {
        test(-1, 1, 1, 1, -1);
        test(-4, -2, -2, 1, 1, -3, 1, -3, -3, -4, -2);
        test(1, -1, -1, -1, 1);
        test(3, 1, 2, 3, 2, 2, 1, 1);
        test(7, 6, 1, 2, 5, 6, 3, 4, 5, 1, 2, 3, 4, 2, 1, 3, 5, 7, 4, 6);
        test(-28, -19, -46, -19, -46, -9, -9, -19, 17, 17, 17, -13, -13, -9,
             -13, -46, -28);
        // appears twice (expect 7 or -1?)
        // test(7, 6, 1, 2, 5, 6, 3, 4, 5, 1, 2, 3, 4, 2, 1, 3, 5, 7, 7, 4, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SingleNumber2");
    }
}
