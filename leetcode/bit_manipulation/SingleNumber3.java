import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// https://leetcode.com/problems/single-number-iii/
//
// Given an array of numbers nums, in which exactly two elements appear only
// once and all the other elements appear exactly twice. Find the two elements
// that appear only once.
public class SingleNumber3 {
    // beats 34.82%
    public int[] singleNumber(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        // int diffMask = xor - (xor & (xor - 1));
        int diffMask = xor & -xor;
        int a = 0;
        int b = 0;
        for (int num : nums) {
            if ((num & diffMask) == 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        return new int[] {a, b};
    }

    void test(Function<int[], int[]> single, int[] expected, int ... nums) {
        int[] res = single.apply(nums);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(int[] expected, int ... nums) {
        SingleNumber3 s = new SingleNumber3();
        test(s::singleNumber, expected, nums);
    }

    @Test
    public void test1() {
        test(new int[] {3, 5}, 1, 2, 1, 3, 2, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SingleNumber3");
    }
}
