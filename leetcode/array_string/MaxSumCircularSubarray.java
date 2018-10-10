import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC918: https://leetcode.com/problems/maximum-sum-circular-subarray/
//
// Given a circular array C of integers represented by A, find the maximum
// possible sum of a non-empty subarray of C. Here, a circular array means the
// end of the array connects to the beginning of the array. Also, a subarray may
// only include each element of the fixed buffer A at most once.
public class MaxSumCircularSubarray {
    // Kadane's Algorithm
    // time complexity: O(N), space complexity: O(N)
    // beats 98.10%(10 ms for 108 tests)
    public int maxSubarraySumCircular(int[] A) {
        int n = A.length;
        int total = 0;
        for (int a : A) {
            total += a;
        }
        int max = maxSubarraySum(A);
        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            B[i] = -A[i];
        }
        int cand = total + maxSubarraySum(B);
        return cand == 0 ? max : Math.max(max, cand);
    }

    private int maxSubarraySum0(int[] A) {
        int n = A.length;
        int maxSum = A[0];
        for (int i = 0, sum = 0; i < n; i++) {
            sum += A[i];
            maxSum = Math.max(maxSum, sum);
            if (sum < 0) {
                sum = 0;
            }
        }
        return maxSum;
    }

    // standard Kadane's Algorithm
    private int maxSubarraySum(int[] A) {
        int max = Integer.MIN_VALUE;
        int cur = Integer.MIN_VALUE;
        for (int a : A) {
            cur = a + Math.max(cur, 0);
            max = Math.max(max, cur);
        }
        return max;
    }

    // Kadane's Algorithm
    // time complexity: O(N), space complexity: O(N)
    // beats 35.73%(20 ms for 108 tests)
    public int maxSubarraySumCircular2(int[] A) {
        int n = A.length;
        int res = maxSubarraySum(A); // the answer for 1-interval subarrays
        int[] maxRight = new int[n];
        maxRight[n - 1] = A[n - 1];
        for (int i = n - 2, rightSum = A[n - 1]; i >= 0; i--) {
            rightSum += A[i];
            maxRight[i] = Math.max(maxRight[i + 1], rightSum);
        }
        for (int i = 0, leftSum = 0; i < n - 2; i++) {
            leftSum += A[i];
            res = Math.max(res, leftSum + maxRight[i + 2]);
        }
        return res;
    }

    // Kadane's Algorithm (Sign Variant)
    // time complexity: O(N), space complexity: O(1)
    // beats 50.41%(17 ms for 108 tests)
    public int maxSubarraySumCircular3(int[] A) {
        int sum = 0;
        for (int a : A) {
            sum += a;
        }
        int res1 = kadane(A, 0, A.length - 1, 1);
        int res2 = sum + kadane(A, 1, A.length - 1, -1);
        int res3 = sum + kadane(A, 0, A.length - 2, -1);
        return Math.max(res1, Math.max(res2, res3));
    }

    private int kadane(int[] A, int start, int end, int sign) {
        int res = Integer.MIN_VALUE;
        int cur = Integer.MIN_VALUE;
        for (int k = start; k <= end; k++) {
            cur = sign * A[k] + Math.max(cur, 0);
            res = Math.max(res, cur);
        }
        return res;
    }

    // Kadane's Algorithm (Min Variant)
    // time complexity: O(N), space complexity: O(1)
    // beats 56.11%(16 ms for 108 tests)
    public int maxSubarraySumCircular4(int[] A) {
        int sum = 0;
        for (int a : A) {
            sum += a;
        }
        int res1 = kadane(A, 0, A.length - 1, 1);
        int res2 = sum - minKadane(A, 1, A.length - 1);
        int res3 = sum - minKadane(A, 0, A.length - 2);
        return Math.max(res1, Math.max(res2, res3));
    }

    private int minKadane(int[] A, int start, int end) {
        int res = Integer.MAX_VALUE;
        int cur = Integer.MAX_VALUE;
        for (int k = start; k <= end; k++) {
            cur = A[k] + Math.min(cur, 0);
            res = Math.min(res, cur);
        }
        return res;
    }

    // Kadane's Algorithm
    // time complexity: O(N), space complexity: O(1)
    // beats 88.04%(12 ms for 108 tests)
    public int maxSubarraySumCircular5(int[] A) {
        int sum = 0;
        int maxSum = Integer.MIN_VALUE;
        int curMax = 0;
        int minSum = Integer.MAX_VALUE;
        int curMin = 0;
        for (int a : A) {
            curMax = a + Math.max(curMax, 0);
            maxSum = Math.max(maxSum, curMax);
            curMin = a + Math.min(curMin, 0);
            minSum = Math.min(minSum, curMin);
            sum += a;
        }
        return maxSum > 0 ? Math.max(maxSum, sum - minSum) : maxSum;
    }

    // Prefix Sums + Monoqueue
    // time complexity: O(N), space complexity: O(N)
    // beats 19.70%(40 ms for 108 tests)
    public int maxSubarraySumCircular6(int[] A) {
        int n = A.length;
        int[] sum = new int[2 * n + 1];
        for (int i = 0; i < 2 * n; i++) {
            sum[i + 1] = sum[i] + A[i % n];
        }
        // want largest P[j] - P[i] with 1 <= j-i <= n
        // For each j, want smallest P[i] with i >= j-n
        int res = A[0];
        Deque<Integer> deque = new ArrayDeque<>();
        deque.offer(0);
        for (int j = 1; j <= 2 * n; j++) {
            if (deque.peekFirst() < j - n) {
                deque.pollFirst();
            }
            res = Math.max(res, sum[j] - sum[deque.peekFirst()]);
            while (!deque.isEmpty() && sum[j] <= sum[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(j);
        }
        return res;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, maxSubarraySumCircular(A));
        assertEquals(expected, maxSubarraySumCircular2(A));
        assertEquals(expected, maxSubarraySumCircular3(A));
        assertEquals(expected, maxSubarraySumCircular4(A));
        assertEquals(expected, maxSubarraySumCircular5(A));
        assertEquals(expected, maxSubarraySumCircular6(A));
    }

    @Test
    public void test() {
        test(new int[] {1, -2, 3, -2}, 3);
        test(new int[] {5, -3, 5}, 10);
        test(new int[] {3, -1, 2, -1}, 4);
        test(new int[] {3, -2, 2, -3}, 3);
        test(new int[] {-2, -3, -1}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
