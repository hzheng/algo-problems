import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/product-of-array-except-self/
//
// Given an array of n integers where n > 1, nums, return an array output
// such that output[i] is equal to the product of all the elements of
// nums except nums[i].
// Solve it without division and in O(n).
public class ProductofArrayExceptSelf {
    // USED division
    // beats 47.45%(2 ms)
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        int zeroIndex = -1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                if (zeroIndex >= 0) return res;

                zeroIndex = i;
            }
        }
        if (zeroIndex >= 0) {
            res[zeroIndex] = 1;
            for (int i = 0; i < n; i++) {
                if (i != zeroIndex) {
                    res[zeroIndex] *= nums[i];
                }
            }
            return res;
        }

        res[0] = 1;
        for (int i = 1; i < n; i++) {
            res[0] *= nums[i];
        }
        for (int i = 1; i < n; i++) {
            res[i] = res[0] / nums[i] * nums[0];
        }
        return res;
    }

    // USED division
    // beats 47.45%(2 ms)
    public int[] productExceptSelf2(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        int zeroIndex = -1;
        int product = 1;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                if (zeroIndex >= 0) return res;

                zeroIndex = i;
            } else {
                product *= nums[i];
            }
        }
        if (zeroIndex >= 0) {
            res[zeroIndex] = product;
            return res;
        }

        for (int i = 0; i < n; i++) {
            res[i] = product / nums[i];
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 47.45%(2 ms)
    public int[] productExceptSelf3(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        res[0] = nums[0];
        for (int i = 1; i + 1 < n; i++) {
            res[i] = res[i - 1] * nums[i];
        }
        res[n - 1] = res[n - 2];
        int rightProduct = nums[n - 1];
        for (int i = n - 2; i > 0; i--) {
            res[i] = res[i - 1] * rightProduct;
            rightProduct *= nums[i];
        }
        res[0] = rightProduct;
        return res;
    }

    // beats 47.45%(2 ms)
    public int[] productExceptSelf4(int[] nums) {
        int n = nums.length;
        int[] res = new int[n];
        res[n - 1] = 1;
        for (int i = n - 2; i >= 0; i--) {
            res[i] = res[i + 1] * nums[i + 1];
        }

        int leftProduct = 1;
        for (int i = 0; i < n; i++) {
            res[i] *= leftProduct;
            leftProduct *= nums[i];
        }
        return res;
    }

    void test(int[] nums, int ... expected) {
        assertArrayEquals(expected, productExceptSelf(nums));
        assertArrayEquals(expected, productExceptSelf2(nums));
        assertArrayEquals(expected, productExceptSelf3(nums));
        assertArrayEquals(expected, productExceptSelf4(nums));
    }

    @Test
    public void test1() {
        test(new int[] {0, 0}, 0, 0);
        test(new int[] {1, 0}, 0, 1);
        test(new int[] {1, 2, 3, 4}, 24, 12, 8, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ProductofArrayExceptSelf");
    }
}
