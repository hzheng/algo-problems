import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.Function;

// https://leetcode.com/problems/maximum-product-subarray/
//
// Find the contiguous subarray within an array which has the largest product.
public class MaxProductSubarray {
    // beats 92.75%(2 ms)
    public int maxProduct(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];

        int max = 0;
        for (int i = 0; i < n; ) {
            while (i < n && nums[i] == 0) {
                i++;
            }
            if (i == n) break;

            int start = i;
            while (++i < n && nums[i] != 0);
            max = Math.max(max, maxProduct(nums, start, i));
        }
        return max;
    }

    // nonzeros' product
    private int maxProduct(int[] nums, int start, int end) {
        if (start + 1 == end) return nums[start];

        int firstNeg = -1;
        for (int i = start; i < end; i++) {
            if (nums[i] < 0) {
                firstNeg = i;
                break;
            }
        }
        if (firstNeg < 0) return product(nums, start, end);

        int lastNeg = -1;
        for (int i = end - 1; i >= start; i--) {
            if (nums[i] < 0) {
                lastNeg = i;
                break;
            }
        }
        if (firstNeg == lastNeg) {
            return Math.max(product(nums, start, firstNeg),
                            product(nums, firstNeg + 1, end));
        }

        boolean isPostive = true;
        for (int i = firstNeg + 1; i < lastNeg; i++) {
            if (nums[i] < 0) {
                isPostive = !isPostive;
            }
        }
        if (isPostive) return product(nums, start, end);

        // start - lastNeg or: firstNeg - end
        return Math.min(product(nums, start, firstNeg + 1),
                        product(nums, lastNeg, end))
               * product(nums, firstNeg + 1, lastNeg);
    }

    private int product(int[] nums, int start, int end) {
        if (start == end) return Integer.MIN_VALUE;

        int product = 1;
        for (int i = start; i < end; i++) {
            product *= nums[i];
        }
        return product;
    }

    // beats 44.91%(4 ms)
    public int maxProduct2(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;

        int localMax = nums[0];
        int localMin = localMax;
        int max = localMax;
        for (int i = 1; i < n; i++) {
            int num = nums[i];
            int oldLocalMax = localMax;
            localMax = Math.max(Math.max(num * localMax, num), num * localMin);
            localMin = Math.min(Math.min(num * oldLocalMax, num), num * localMin);
            max = Math.max(max, localMax);
        }
        return max;
    }

    void test(Function<int[], Integer> maxProduct, int expected, int ... nums) {
        assertEquals(expected, (int)maxProduct.apply(nums));
    }

    void test(int expected, int ... nums) {
        MaxProductSubarray m = new MaxProductSubarray();
        test(m::maxProduct, expected, nums);
        test(m::maxProduct2, expected, nums);
    }

    @Test
    public void test1() {
        test(3, -2, 3);
        test(2, 0, 2);
        test(6, 2, 3, -2, 4);
        test(-4, -4);
        test(0, -4, 0, -2);
        test(12, -3, -4);
        test(12, -3, -4, 0, -2, -5);
        test(4320, -2, -3, 8, 9, 10, 0, 3, 8, 9, -2, 5, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxProductSubarray");
    }
}
