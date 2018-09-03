import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC713: https://leetcode.com/problems/subarray-product-less-than-k/
//
// Count and print the number of (contiguous) subarrays where the product of all
// the elements in the subarray is less than k.
public class NumSubarrayProductLessThanK {
    // time complexity: O(N), space complexity: O(1)
    // beats 3.12%(32 ms for 84 tests)
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        int res = 0;
        int product = 1;
        int s1 = 0;
        int e1 = 0;
        int s2 = 0;
        for (int i = 0; i < nums.length; i++) {
            product *= nums[i];
            if (product < k) continue;

            res += total(s1, e1, s2, i);
            for (s1 = s2, e1 = i; s2 <= i; s2++) {
                product /= nums[s2];
                if (product < k) {
                    s2++;
                    break;
                }
            }
        }
        return res + total(s1, e1, s2, nums.length);
    }

    private int total(int s1, int e1, int s2, int e2) {
        return (int) ((e1 - s2) * (e2 - e1) + ((long) (e2 - e1)) *
                      (e2 - e1 + 1) / 2);
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 39.35%(18 ms for 84 tests)
    public int numSubarrayProductLessThanK2(int[] nums, int k) {
        if (k <= 1) return 0;

        for (int i = 0, j = 0, product = 1, res = 0; ; ) {
            if (product < k) {
                res += (j - i);
                if (j == nums.length) return res;

                product *= nums[j++];
            } else {
                product /= nums[i++];
            }
        }
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 17.85%(22 ms for 84 tests)
    public int numSubarrayProductLessThanK3(int[] nums, int k) {
        if (k <= 1) return 0;

        int res = 0;
        for (int i = 0, j = 0, prod = 1; j < nums.length; j++, res += j - i) {
            for (prod *= nums[j]; prod >= k; prod /= nums[i++]) {}
        }
        return res;
    }

    // Binary Search
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 0.00%(80 ms for 84 tests)
    public int numSubarrayProductLessThanK4(int[] nums, int k) {
        if (k == 0) return 0;

        double target = Math.log(k);
        int n = nums.length;
        double[] sum = new double[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + Math.log(nums[i]);
        }
        int res = 0;
        for (int i = 0; i <= n; i++) {
            int low = i + 1;
            for (int high = sum.length; low < high; ) {
                int mid = (low + high) >>> 1;
                if (sum[mid] - sum[i] < target - 1e-9) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            res += low - i - 1;
        }
        return res;
    }

    void test(int[] nums, int k, int expected) {
        assertEquals(expected, numSubarrayProductLessThanK(nums, k));
        assertEquals(expected, numSubarrayProductLessThanK2(nums, k));
        assertEquals(expected, numSubarrayProductLessThanK3(nums, k));
        assertEquals(expected, numSubarrayProductLessThanK4(nums, k));
    }

    @Test
    public void test() {
        test(new int[] { 10, 5, 2, 6 }, 1, 0);
        test(new int[] { 10, 5, 2, 6 }, 100, 8);
        test(new int[] { 2, 3, 12, 5, 6, 3, 5, 8, 7}, 80, 18);
        test(new int[] { 2, 3, 12, 5, 6, 3, 5, 8, 7, 90, 1, 2, 8}, 80, 24);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
