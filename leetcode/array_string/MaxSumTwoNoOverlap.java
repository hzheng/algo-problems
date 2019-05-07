import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1031: https://leetcode.com/problems/maximum-sum-of-two-non-overlapping-subarrays/
//
// Given an array A of non-negative integers, return the maximum sum of elements in two
// non-overlapping (contiguous) subarrays, which have lengths L and M.
// Note:
// L >= 1
// M >= 1
// L + M <= A.length <= 1000
// 0 <= A[i] <= 1000
public class MaxSumTwoNoOverlap {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(61.89%), 37.9 MB(100%) for 51 tests
    public int maxSumTwoNoOverlap(int[] A, int L, int M) {
        return Math.max(maxSum(A, L, M), maxSum(A, M, L));
    }

    private int maxSum(int[] A, int L, int M) {
        int n = A.length;
        int sum = 0;
        for (int i = n - M; i < n; i++) {
            sum += A[i];
        }
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{sum, n - M});
        for (int i = n - M - 1; i >= L; i--) {
            sum += A[i] - A[i + M];
            if (sum > stack.peek()[0]) {
                stack.push(new int[]{sum, i});
            }
        }
        sum = 0;
        for (int i = 0; i < L; i++) {
            sum += A[i];
        }
        int max = sum + stack.peek()[0];
        for (int i = L; i < n - M; i++) {
            while (!stack.isEmpty() && stack.peek()[1] <= i) {
                stack.pop();
            }
            if (stack.isEmpty()) {
                break;
            }
            sum += A[i] - A[i - L];
            max = Math.max(max, sum + stack.peek()[0]);
        }
        return max;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(99.09%), 37.7 MB(100%) for 51 tests
    public int maxSumTwoNoOverlap2(int[] A, int L, int M) {
        return Math.max(maxSum2(A, L, M), maxSum2(A, M, L));
    }

    private int maxSum2(int[] A, int L, int M) {
        int res = 0;
        int lSum = 0; // sum of the last L elements
        int mSum = 0; // sum of the last M elements
        int lMax = 0; // max sum of contiguous L elements before the last M elements
        for (int i = 0; i < A.length; i++) {
            mSum += A[i];
            if (i < M) {
                continue;
            }
            mSum -= A[i - M];
            lSum += A[i - M];
            if (i >= M + L) {
                lSum -= A[i - L - M];
            }
            lMax = Math.max(lMax, lSum);
            res = Math.max(res, lMax + mSum);
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(99.09%), 37.9 MB(100%) for 51 tests
    public int maxSumTwoNoOverlap3(int[] A, int L, int M) {
        int[] sum = new int[A.length + 1];
        for (int i = 0; i < A.length; i++) {
            sum[i + 1] = sum[i] + A[i];
        }
        return Math.max(maxSum3(sum, L, M), maxSum3(sum, M, L));
    }

    private int maxSum3(int[] sum, int L, int M) {
        int res = 0;
        for (int i = L + M, maxL = 0; i < sum.length; i++) {
            maxL = Math.max(maxL, sum[i - M] - sum[i - M - L]);
            res = Math.max(res, maxL + sum[i] - sum[i - M]);
        }
        return res;
    }

    void test(int[] A, int L, int M, int expected) {
        assertEquals(expected, maxSumTwoNoOverlap(A, L, M));
        assertEquals(expected, maxSumTwoNoOverlap2(A, L, M));
        assertEquals(expected, maxSumTwoNoOverlap3(A, L, M));
    }

    @Test
    public void test() {
        test(new int[]{0, 6, 5, 2, 2, 5, 1, 9, 4}, 1, 2, 20);
        test(new int[]{3, 8, 1, 3, 2, 1, 8, 9, 0}, 3, 2, 29);
        test(new int[]{2, 1, 5, 6, 0, 9, 5, 0, 3, 8}, 4, 3, 31);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
