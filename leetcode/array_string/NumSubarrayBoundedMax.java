import org.junit.Test;
import static org.junit.Assert.*;

// LC795: https://leetcode.com/problems/number-of-subarrays-with-bounded-maximum/
//
// We are given an array A of positive integers, and two positive integers L and
// R (L <= R). Return the number of (contiguous, non-empty) subarrays such that
// the value of the maximum array element in that subarray is between L and R.
public class NumSubarrayBoundedMax {
    // 1D-Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats %(6 ms for 38 tests)
    public int numSubarrayBoundedMax(int[] A, int L, int R) {
        int n = A.length;
        int[] dp = new int[n + 1];
        for (int i = 0, start = -1; i < n; i++) {
            int a = A[i];
            if (a > R) {
                start = i;
            } else if (a < L) {
                dp[i + 1] = dp[i];
            } else {
                dp[i + 1] = i - start;
            }
        }
        int res = 0;
        for (int i = 1; i <= n; i++) {
            res += dp[i];
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(4 ms for 38 tests)
    public int numSubarrayBoundedMax2(int[] A, int L, int R) {
        int res = 0;
        for (int i = 0, start = -1, count = 0; i < A.length; i++) {
            if (A[i] > R) { // reset
                start = i;
                count = 0;
            } else if (A[i] >= L) {
                count = i - start;
            }
            res += count;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(4 ms for 38 tests)
    public int numSubarrayBoundedMax2_2(int[] A, int L, int R) {
        int res = 0;
        for (int i = 0, left = -1, right = -1; i < A.length; i++) {
            if (A[i] > R) {
                left = i;
            } 
            if (A[i] >= L) {
                right = i;
            }
            res += right - left;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(5 ms for 38 tests)
    public int numSubarrayBoundedMax3(int[] A, int L, int R) {
        return lowSubarray(A, R) - lowSubarray(A, L - 1);
    }

    private int lowSubarray(int[] A, int max) {
        int res = 0;
        int count = 0;
        for (int a : A) {
            if (a <= max) {
                count++;
            } else {
                count = 0;
            }
            res += count;
        }
        return res;
    }

    void test(int[] A, int L, int R, int expected) {
        assertEquals(expected, numSubarrayBoundedMax(A, L, R));
        assertEquals(expected, numSubarrayBoundedMax2(A, L, R));
        assertEquals(expected, numSubarrayBoundedMax2_2(A, L, R));
        assertEquals(expected, numSubarrayBoundedMax3(A, L, R));
    }

    @Test
    public void test() {
        test(new int[] {2, 1, 4, 3}, 2, 3, 3);
        test(new int[] {2, 9, 2, 5, 6}, 2, 8, 7);
        test(new int[] {73, 55, 36, 5, 55, 14, 9, 7, 72, 52}, 32, 69, 22);
        test(new int[] {876, 880, 482, 260, 132, 421, 732, 703, 795, 420, 871,
                        445, 400, 291, 358, 589, 617, 202, 755, 810, 227, 813,
                        549, 791, 418, 528, 835, 401, 526, 584, 873, 662, 13,
                        314, 988, 101, 299, 816, 833, 224, 160, 852, 179, 769,
                        646, 558, 661, 808, 651, 982, 878, 918, 406, 551, 467,
                        87, 139, 387, 16, 531, 307, 389, 939, 551, 613, 36, 528,
                        460, 404, 314, 66, 111, 458, 531, 944, 461, 951, 419,
                        82, 896, 467, 353, 704, 905, 705, 760, 61, 422, 395,
                        298, 127, 516, 153, 299, 801, 341, 668, 598, 98, 241},
             658, 719, 19);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
