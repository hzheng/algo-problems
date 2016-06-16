import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/single-number/
//
// Given an array of integers, every element appears twice except for one.
// Find that single one.
// Your algorithm should have a linear runtime complexity. Could you implement
// it without using extra memory?
public class SingleNumber {
    // beats 32.35%
    public int singleNumber(int[] nums) {
        int xor = nums[0];
        for (int i = 1; i < nums.length; i++) {
            xor ^= nums[i];
        }
        return xor;
    }

    // beats 32.35%
    public int singleNumber2(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        return xor;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, singleNumber(nums));
        assertEquals(expected, singleNumber2(nums));
    }

    @Test
    public void test1() {
        test(3, 1, 2, 3, 2, 1);
        test(7, 6, 1, 2, 3, 4, 5, 2, 1, 3, 5, 7, 4, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SingleNumber");
    }
}
