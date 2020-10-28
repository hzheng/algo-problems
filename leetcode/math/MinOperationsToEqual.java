import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1551: https://leetcode.com/problems/minimum-operations-to-make-array-equal/
//
// You have an array arr of length n where arr[i] = (2 * i) + 1 for all valid values of i.
// In one operation, you can select two indices x and y where 0 <= x, y < n and subtract 1 from
// arr[x] and add 1 to arr[y] (i.e. perform arr[x] -=1 and arr[y] += 1). The goal is to make all the
// elements of the array equal. It is guaranteed that all the elements of the array can be made
// equal using some operations.
// Given an integer n, the length of the array. Return the minimum number of operations needed to
// make all the elements of arr equal.
// Constraints:
// 1 <= n <= 10^4
public class MinOperationsToEqual {
    // time complexity: O(N)), space complexity: O(1)
    // 2 ms(36.39%), 35.7 MB(9.17%) for 301 tests
    public int minOperations(int n) {
        int res = 0;
        for (int i = 0; i < n / 2; i++) {
            res += n - (2 * i + 1);
        }
        return res;
    }

    // time complexity: O(1)), space complexity: O(1)
    // 0 ms(100%), 35.5 MB(8.85%) for 301 tests
    public int minOperations2(int n) {
        return n * n / 4;
    }

    private void test(int n, int expected) {
        assertEquals(expected, minOperations(n));
        assertEquals(expected, minOperations2(n));
    }

    @Test public void test() {
        test(3, 2);
        test(6, 9);
        test(60, 900);
        test(9999, 24995000);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
