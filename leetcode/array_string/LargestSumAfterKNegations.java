import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1006: https://leetcode.com/problems/maximize-sum-of-array-after-k-negations/
//
// Given an array A of integers, we must modify the array in the following way: we choose an i and
// replace A[i] with -A[i], and we repeat this process K times in total. (We may choose the same
// index i multiple times.)
// Return the largest possible sum of the array after modifying it in this way.
//
// Note:
// 1 <= A.length <= 10000
// 1 <= K <= 10000
// -100 <= A[i] <= 100
public class LargestSumAfterKNegations {
    // Greedy + Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 1 ms(99.77%), 38.6 MB(21.63%) for 78 tests
    public int largestSumAfterKNegations(int[] A, int K) {
        Arrays.sort(A);
        int res = 0;
        for (int k = K, i = 0, n = A.length; i < n; i++) {
            if (k-- <= 0) {
                res += A[i];
            } else if (A[i] <= 0) {
                res -= A[i];
            } else {
                res += A[i];
                if (k % 2 == 0) {
                    if (i > 0 && -A[i - 1] < A[i]) {
                        res += 2 * A[i - 1];
                    } else {
                        res -= 2 * A[i];
                    }
                }
                k = 0;
            }
        }
        return res;
    }

    // Greedy + Heap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 4 ms(19.53%), 38.7 MB(13.49%) for 78 tests
    public int largestSumAfterKNegations2(int[] A, int K) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int a : A) {
            pq.offer(a);
        }
        for (int k = K; k > 0; k--) {
            pq.offer(-pq.poll());
        }
        int res = 0;
        for (int i = A.length; i > 0; i--) {
            res += pq.poll();
        }
        return res;
    }

    // Greedy + Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 1 ms(99.77%), 38.4 MB(67.60%) for 78 tests
    public int largestSumAfterKNegations3(int[] A, int K) {
        Arrays.sort(A);
        for (int i = 0, n = A.length; K > 0 && i < n && A[i] < 0; i++, K--) {
            A[i] = -A[i];
        }
        int res = 0;
        int min = Integer.MAX_VALUE;
        for (int a : A) {
            res += a;
            min = Math.min(min, a);
        }
        return res - (K % 2) * min * 2;
    }

    // Greedy + Bucket Sort
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(99.77%), 38.4 MB(67.60%) for 78 tests
    public int largestSumAfterKNegations4(int[] A, int K) {
        int MAX = 100;
        int[] count = new int[201];
        int res = 0;
        for (int i : A) {
            count[i + MAX]++;
        }
        for (int a = -MAX, k = K; a <= MAX && k > 0; a++) {
            if (count[a + MAX] <= 0) { continue; }

            int cnt = (a < 0) ? Math.min(k, count[a + MAX]) : k % 2;
            count[-a + MAX] += cnt;
            count[a + MAX] -= cnt;
            k = (a < 0) ? k - cnt : 0;
        }
        for (int a = -MAX; a <= MAX; a++) {
            res += a * count[a + MAX];
        }
        return res;
    }

    private void test(int[] A, int K, int expected) {
        assertEquals(expected, largestSumAfterKNegations(A.clone(), K));
        assertEquals(expected, largestSumAfterKNegations2(A.clone(), K));
        assertEquals(expected, largestSumAfterKNegations3(A.clone(), K));
        assertEquals(expected, largestSumAfterKNegations4(A.clone(), K));
    }

    @Test public void test() {
        test(new int[] {4, 2, 3}, 1, 5);
        test(new int[] {3, -1, 0, 2}, 3, 6);
        test(new int[] {2, -3, -1, 5, -4}, 2, 13);
        test(new int[] {5, 6, 9, -3, 3}, 2, 20);
        test(new int[] {-4, 4, -3, 3, -4, -1, 8, -7, -7}, 3, 25);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
