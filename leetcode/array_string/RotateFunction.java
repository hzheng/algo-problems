import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC396: https://leetcode.com/problems/rotate-function/
//
// Given an array of integers A and let n to be its length.
// Assume Bk to be an array obtained by rotating the array A k positions
// clock-wise, we define a "rotation function" F on A as follow:
// F(k) = 0 * Bk[0] + 1 * Bk[1] + ... + (n-1) * Bk[n-1].
// Calculate the maximum value of F(0), F(1), ..., F(n-1).
public class RotateFunction {
    // time complexity: O(N ^ 2), space complexity: O(1)
    // Time Limit Exceeded
    public int maxRotateFunction(int[] A) {
        int n = A.length;
        if (n == 0) return 0;

        int maxSum = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += j * A[(j + i) % n];
            }
            maxSum = Math.max(sum, maxSum);
        }
        return maxSum;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 42.67%(5 ms)
    public int maxRotateFunction2(int[] A) {
        int n = A.length;
        int coefficientSum = 0;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += i * A[i];
            coefficientSum += A[i];
        }
        int maxSum = sum;
        for (int i = n - 1; i > 0; i--) {
            sum += coefficientSum - n * A[i];
            maxSum = Math.max(sum, maxSum);
        }
        return maxSum;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, maxRotateFunction(A));
        assertEquals(expected, maxRotateFunction2(A));
    }

    @Test
    public void test1() {
        test(new int[]{-2147483648, -2147483648}, -2147483648);
        test(new int[]{4, 3, 2, 6}, 26);
        test(new int[]{}, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RotateFunction");
    }
}
