import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC628: https://leetcode.com/problems/maximum-product-of-three-numbers/
//
// Given an integer array, find three numbers whose product is maximum and
// output the maximum product.
// Note: The length of the given array will be in range [3,10^4] and all elements
// are in the range [-1000, 1000].
// Multiplication of any three numbers in the input won't exceed the range of
// 32-bit signed integer.
public class Max3Product {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 47.06%(31 ms for 83 tests)
    public int maximumProduct(int[] nums) {
        int n = nums.length;
        Arrays.sort(nums);
        List<Integer> cands = new ArrayList<>();
        cands.add(nums[0]);
        cands.add(nums[1]);
        cands.add(nums[2]);
        if (n > 3) {
            cands.add(nums[n - 1]);
            if (n > 4) {
                cands.add(nums[n - 2]);
                if (n > 5) {
                    cands.add(nums[n - 3]);
                }
            }
        }
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < cands.size(); i++) {
            for (int j = i + 1; j < cands.size(); j++) {
                for (int k = j + 1; k < cands.size(); k++) {
                    max = Math.max(max,
                                   cands.get(i) * cands.get(j) * cands.get(k));
                }
            }
        }
        return max;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats 60.14%(26 ms for 83 tests)
    public int maximumProduct2(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        return Math.max(nums[0] * nums[1] * nums[n - 1],
                        nums[n - 1] * nums[n - 2] * nums[n - 3]);
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 97.54%(11 ms for 83 tests)
    public int maximumProduct3(int[] nums) {
        int min1 = Integer.MAX_VALUE;
        int min2 = Integer.MAX_VALUE;
        int max1 = Integer.MIN_VALUE;
        int max2 = Integer.MIN_VALUE;
        int max3 = Integer.MIN_VALUE;
        for (int num : nums) {
            if (num < min1) {
                min2 = min1;
                min1 = num;
            } else if (num < min2) {
                min2 = num;
            }
            if (num > max1) {
                max3 = max2;
                max2 = max1;
                max1 = num;
            } else if (num > max2) {
                max3 = max2;
                max2 = num;
            } else if (num > max3) {
                max3 = num;
            }
        }
        return Math.max(min1 * min2 * max1, max1 * max2 * max3);
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, maximumProduct(nums));
        assertEquals(expected, maximumProduct2(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3}, 6);
        test(new int[] {1, 2, 3, 4}, 24);
        test(new int[] {-3, 2, -3, 4}, 36);
        test(new int[] {-3, -9, 0, -3, -4}, 0);
        test(new int[] {-3, -5, -2, -1, -4}, -6);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
