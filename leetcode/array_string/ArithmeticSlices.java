import org.junit.Test;
import static org.junit.Assert.*;

// LC413: https://leetcode.com/problems/arithmetic-slices/
//
// A zero-indexed array A consisting of N numbers is given. A slice of that
// array is any pair of integers (P, Q) such that 0 <= P < Q < N.
// A slice (P, Q) of array A is called arithmetic if the sequence:
// A[P], A[p + 1], ..., A[Q - 1], A[Q] is arithmetic. In particular, this means
// that P + 1 < Q.
// The function should return the number of arithmetic slices in the array A.
public class ArithmeticSlices {
    // beats N/A(3 ms for 15 tests)
    public int numberOfArithmeticSlices(int[] A) {
        int n = A.length;
        if (n < 3) return 0;

        int total = 0;
        int count = 1;
        int lastDiff = A[n - 1] - A[n - 2];
        for (int i = n - 2; i > 0; i--) {
            int diff = A[i] - A[i - 1];
            if (diff == lastDiff) {
                count++;
            } else {
                if (count > 1) {
                    total += calculate(count);
                    count = 1;
                }
                lastDiff = diff;
            }
        }
        return total += calculate(count);
    }

    private int calculate(int count) {
        return count * (count - 1) / 2;
    }

    // beats N/A(2 ms for 15 tests)
    public int numberOfArithmeticSlices2(int[] A) {
        int sum = 0;
        for (int i = A.length - 1, count = 0; i > 1; i--) {
            if (A[i] + A[i - 2] == A[i - 1] * 2) {
                sum += (++count);
            } else {
                count = 0;
            }
        }
        return sum;
    }

    // Dynamic Programming
    // beats N/A(2 ms for 15 tests)
    public int numberOfArithmeticSlices3(int[] A) {
        int[] dp = new int[A.length];
        int sum = 0;
        for (int i = A.length - 1; i > 1; i--) {
            if (A[i] + A[i - 2] == A[i - 1] * 2) {
                dp[i - 1] = dp[i] + 1;
                sum += dp[i - 1];
            }
        }
        return sum;
    }

    void test(int expected, int ... A) {
        assertEquals(expected, numberOfArithmeticSlices(A));
        assertEquals(expected, numberOfArithmeticSlices2(A));
        assertEquals(expected, numberOfArithmeticSlices3(A));
    }

    @Test
    public void test1() {
        test(0, 1);
        test(0, 1, 2);
        test(1, 1, 2, 3);
        test(3, 1, 2, 3, 4);
        test(6, 1, 2, 3, 4, 5);
        test(15, 1, 2, 3, 4, 5, 6, 7);
        test(19, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13, 16, 19);
        test(20, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13, 16, 19, 21, 11, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArithmeticSlices");
    }
}
