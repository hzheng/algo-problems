import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1822: https://leetcode.com/problems/sign-of-the-product-of-an-array/
//
// There is a function signFunc(x) that returns:
// 1 if x is positive.
// -1 if x is negative.
// 0 if x is equal to 0.
// You are given an integer array nums. Let product be the product of all values in the array nums.
// Return signFunc(product).
public class ArraySign {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.7 MB(50.00%) for 74 tests
    public int arraySign(int[] nums) {
        int res = 1;
        for (int a : nums) {
            if (a == 0) { return 0; }

            res *= (a > 0) ? 1 : -1;
        }
        return res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, arraySign(nums));
    }

    @Test public void test() {
        test(new int[] {-1, -2, -3, -4, 3, 2, 1}, 1);
        test(new int[] {1, 5, 0, 2, -3}, 0);
        test(new int[] {-1, 1, -1, 1, -1}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
