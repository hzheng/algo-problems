import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC978: https://leetcode.com/problems/longest-turbulent-subarray/
//
// A subarray A[i], A[i+1], ..., A[j] of A is turbulent if and only if:
// For i<=k<j, A[k]>A[k+1] when k is odd, and A[k] < A[k+1] when k is even;
// OR, for i<=k<j, A[k]>A[k+1] when k is even, and A[k] < A[k+1] when k is odd.
// Return the length of a maximum size turbulent subarray of A.
public class MaxTurbulenceSize {
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(95.36%), 34.7 MB(100.99%) for 85 tests
    public int maxTurbulenceSize(int[] A) {
        int res = 1;
        for (int cur = 1, i = 1, sign = 0; i < A.length; i++) {
            if (A[i] == A[i - 1]) {
                cur = 1;
                sign = 0;
            } else if (sign == 0) {
                sign = (A[i] > A[i - 1]) ? 1 : -1;
                cur++;
            } else if ((A[i] - A[i - 1]) * sign < 0) {
                sign *= -1;
                res = Math.max(res, ++cur);
            } else {
                res = Math.max(res, cur = 2);
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 11 ms(52.98%), 34.5 MB(100.99%) for 85 tests
    public int maxTurbulenceSize2(int[] A) {
        int res = 1;
        for (int i = 1, anchor = 0, n = A.length; i < n; i++) {
            int c = Integer.compare(A[i - 1], A[i]);
            if (c == 0) {
                anchor = i;
            } else if (i == n - 1 || c * Integer.compare(A[i], A[i + 1]) >= 0) {
                res = Math.max(res, i - anchor + 1);
                anchor = i;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 11 ms(52.98%), 34.5 MB(100.99%) for 85 tests
    public int maxTurbulenceSize3(int[] A) {
        int res = 0;
        for (int i = 0, cnt = 0; i + 1 < A.length; i++, cnt *= -1) {
            if (A[i] > A[i + 1]) {
                cnt = (cnt > 0) ? (cnt + 1) : 1;
            } else if (A[i] < A[i + 1]) {
                cnt = (cnt < 0) ? (cnt - 1) : -1;
            } else {
                cnt = 0;
            }
            res = Math.max(res, Math.abs(cnt));
        }
        return res + 1;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, maxTurbulenceSize(A));
        assertEquals(expected, maxTurbulenceSize2(A));
        assertEquals(expected, maxTurbulenceSize3(A));
    }

    @Test
    public void test() {
        test(new int[] {9, 4, 2, 10, 7, 8, 8, 1, 9}, 5);
        test(new int[] {4, 8, 12, 16}, 2);
        test(new int[] {100}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
