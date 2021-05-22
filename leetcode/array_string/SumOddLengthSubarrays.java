import org.junit.Test;

import static org.junit.Assert.*;

// LC1588: https://leetcode.com/problems/sum-of-all-odd-length-subarrays/
//
// Given an array of positive integers arr, calculate the sum of all possible odd-length subarrays.
// A subarray is a contiguous subsequence of the array.
// Return the sum of all odd-length subarrays of arr.
//
// Constraints:
// 1 <= arr.length <= 100
// 1 <= arr[i] <= 1000
public class SumOddLengthSubarrays {
    // time complexity: O(N^2), space complexity: O(N)
    // 1 ms(69.92%), 36.5 MB(60.16%) for 96 tests
    public int sumOddLengthSubarrays(int[] arr) {
        int n = arr.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + arr[i];
        }
        int res = 0;
        for (int len = 1; len <= n; len += 2) {
            for (int i = 0; i + len <= n; i++) {
                res += sum[i + len] - sum[i];
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.7 MB(45.01%) for 96 tests
    public int sumOddLengthSubarrays2(int[] arr) {
        int res = 0;
        for (int i = 0, n = arr.length; i < n; ++i) {
            res += ((i + 1) * (n - i) + 1) / 2 * arr[i];
        }
        return res;
    }

    private void test(int[] arr, int expected) {
        assertEquals(expected, sumOddLengthSubarrays(arr));
        assertEquals(expected, sumOddLengthSubarrays2(arr));
    }

    @Test public void test1() {
        test(new int[] {1, 4, 2, 5, 3}, 58);
        test(new int[] {1, 2}, 3);
        test(new int[] {10, 11, 12}, 66);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
