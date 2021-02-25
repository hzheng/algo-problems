import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1191: https://leetcode.com/problems/k-concatenation-maximum-sum/
//
// Given an integer array arr and an integer k, modify the array by repeating it k times.
// For example, if arr = [1, 2] and k = 3 then the modified array will be [1, 2, 1, 2, 1, 2].
// Return the maximum sub-array sum in the modified array. Note that the length of the sub-array can
// be 0 and its sum in that case is 0.
// As the answer can be very large, return the answer modulo 10^9 + 7.
//
// Constraints:
// 1 <= arr.length <= 10^5
// 1 <= k <= 10^5
// -10^4 <= arr[i] <= 10^4
public class KConcatenationMaxSum {
    private static final int MOD = 1000_000_007;

    // time complexity: O(N), space complexity: O(1)
    // 6 ms(44.06%), 55.1 MB(32.18%) for 42 tests
    public int kConcatenationMaxSum(int[] arr, int k) {
        int sum = maxSum(arr, Math.min(k, 2));
        if (k <= 2) { return sum; }

        int total = IntStream.of(arr).sum() % MOD;
        if (total <= 0) { return sum; }

        return (int)((sum + total * (k - 2L) % MOD) % MOD);
    }

    private int maxSum(int[] arr, int k) {
        long res = 0;
        long sum = 0;
        for (int i = 0, n = arr.length; i < n * k; i++) {
            sum = arr[i % n] + Math.max(sum, 0);
            res = Math.max(res, sum);
        }
        return (int)(res % MOD);
    }

    // time complexity: O(N), space complexity: O(1)
    // 4 ms(69.73%), 56.4 MB(19.54%) for 42 tests
    public int kConcatenationMaxSum2(int[] arr, int k) {
        long maxSum = 0;
        long runningSum = 0;
        long sum = 0;
        for (int i = 0, n = arr.length, len = n * Math.min(k, 2); i < len; i++) {
            int a = arr[i % n];
            sum += a;
            runningSum = a + Math.max(runningSum, 0);
            maxSum = Math.max(maxSum, runningSum);
        }
        return (int)((maxSum + Math.max(sum / 2, 0) * Math.max(k - 2, 0)) % MOD);
    }

    private void test(int[] arr, int k, int expected) {
        assertEquals(expected, kConcatenationMaxSum(arr, k));
        assertEquals(expected, kConcatenationMaxSum2(arr, k));
    }

    @Test public void test() {
        test(new int[] {1, 2}, 3, 9);
        test(new int[] {1, -2, 1}, 5, 2);
        test(new int[] {-1, -2}, 7, 0);
        test(new int[] {10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000},
             100000, 999999937);
        int[] arr = new int[100000];
        Arrays.fill(arr, 10000);
        test(arr, 2, 999999993);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
