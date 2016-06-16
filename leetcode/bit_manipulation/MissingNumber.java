import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// https://leetcode.com/problems/missing-number/
//
// Given an array containing n distinct numbers taken from 0, 1, 2, ..., n,
// find the one that is missing from the array.
public class MissingNumber {
    // beats 42.37%
    public int missingNumber(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        // for (int i = nums.length; i > 0; i--) {
        for (int i = nums.length & 3; i > 0; i--) {
            xor ^= i;
        }
        return xor;
    }

    // beats 42.37%
    public int missingNumber2(int[] nums) {
       int xor = 0;
       for (int i = nums.length - 1; i >= 0; i--){
           xor ^= i ^ nums[i];
       }
       return xor ^ nums.length;
    }

    void test(Function<int[], Integer> missing, int expected, int ... nums) {
        assertEquals(expected, (int)missing.apply(nums));
    }

    void test(int expected, int ... nums) {
        MissingNumber m = new MissingNumber();
        test(m::missingNumber, expected, nums);
        test(m::missingNumber2, expected, nums);
    }

    @Test
    public void test1() {
        test(2, 0, 1, 3);
        test(4, 0, 1, 5, 2, 3);
        test(6, 0, 1, 5, 4, 7, 8, 10, 9, 13, 12, 2, 11, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MissingNumber");
    }
}
