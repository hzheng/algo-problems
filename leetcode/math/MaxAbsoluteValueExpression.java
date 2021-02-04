import org.junit.Test;

import static org.junit.Assert.*;

// LC1131: https://leetcode.com/problems/maximum-of-absolute-value-expression/
//
// Given two arrays of integers with equal lengths, return the maximum value of:
// |arr1[i] - arr1[j]| + |arr2[i] - arr2[j]| + |i - j|
// where the maximum is taken over all 0 <= i, j < arr1.length.
//
// Constraints:
// 2 <= arr1.length == arr2.length <= 40000
// -10^6 <= arr1[i], arr2[i] <= 10^6
public class MaxAbsoluteValueExpression {
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(88.99%), 46 MB(92.07%) for 21 tests
    public int maxAbsValExpr(int[] arr1, int[] arr2) {
        int n = arr1.length;
        int res = 0;
        int[] sign = {-1, 1};
        for (int s1 : sign) {
            for (int s2 : sign) {
                int min = arr1[0] * s1 + arr2[0] * s2;
                for (int i = 1; i < n; i++) {
                    int cur = arr1[i] * s1 + arr2[i] * s2 + i;
                    res = Math.max(res, cur - min);
                    min = Math.min(min, cur);
                }
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 11 ms(17.18%), 52.7 MB(7.49%) for 21 tests
    public int maxAbsValExpr2(int[] arr1, int[] arr2) {
        int res = 0;
        final int MIN = Integer.MIN_VALUE / 2;
        int max1 = MIN;
        int max2 = MIN;
        int max3 = MIN;
        int max4 = MIN;
        for (int i = 0, n = arr1.length; i < n; i++) {
            max1 = Math.max(max1, -arr1[i] - arr2[i] - i);
            max2 = Math.max(max2, -arr1[i] + arr2[i] - i);
            max3 = Math.max(max3, arr1[i] - arr2[i] - i);
            max4 = Math.max(max4, arr1[i] + arr2[i] - i);
            res = Math.max(res, max1 + arr1[i] + arr2[i] + i);
            res = Math.max(res, max2 + arr1[i] - arr2[i] + i);
            res = Math.max(res, max3 - arr1[i] + arr2[i] + i);
            res = Math.max(res, max4 - arr1[i] - arr2[i] + i);
        }
        return res;
    }

    private void test(int[] arr1, int[] arr2, int expected) {
        assertEquals(expected, maxAbsValExpr(arr1, arr2));
        assertEquals(expected, maxAbsValExpr2(arr1, arr2));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4}, new int[] {-1, 4, 5, 6}, 13);
        test(new int[] {1, -2, -5, 0, 10}, new int[] {0, -2, -1, -7, -4}, 20);
        test(new int[] {1, -2, -5, 8, 12, 7, 0, 10}, new int[] {21, -2, 6, 0, -2, -1, -7, -4}, 41);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
