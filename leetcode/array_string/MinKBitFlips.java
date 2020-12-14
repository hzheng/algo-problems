import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC995: https://leetcode.com/problems/minimum-number-of-k-consecutive-bit-flips/
//
// In an array A containing only 0s and 1s, a K-bit flip consists of choosing a (contiguous)
// subarray of length K and simultaneously changing every 0 in the subarray to 1, and every 1 in the
// subarray to 0. Return the minimum number of K-bit flips required so that there is no 0 in the
// array.  If it is not possible, return -1.
//
// Note:
// 1 <= A.length <= 30000
// 1 <= K <= A.length
public class MinKBitFlips {
    // Greedy + Brute Force
    // time complexity: O(N * K), space complexity: O(1)
    // 1577 ms(5.32%), 47 MB(79.29%) for 110 tests
    public int minKBitFlips(int[] A, int K) {
        int n = A.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (A[i] == 1) { continue; }
            if (i + K > n) { return -1; }

            res++;
            for (int j = i; j < i + K; j++) {
                A[j] ^= 1;
            }
        }
        return res;
    }

    // Greedy + Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(99.86%), 47.5 MB(41.48%) for 110 tests
    public int minKBitFlips2(int[] A, int K) {
        int n = A.length;
        int[] flipped = new int[n];
        int res = 0;
        for (int i = 0, needFlip = 0; i < n; i++) {
            if (i >= K) {
                needFlip ^= flipped[i - K]; // cancel 1 flip
            }
            if (needFlip == A[i]) {
                if (i + K > n) { return -1; }

                res++;
                flipped[i] = 1;
                needFlip ^= 1; // accumulate 1 flip
            }
        }
        return res;
    }

    // Greedy + Sliding Window + Deque
    // time complexity: O(N), space complexity: O(K)
    // 8 ms(37.16%), 47.3 MB(52.47%) for 110 tests
    public int minKBitFlips3(int[] A, int K) {
        int n = A.length;
        Deque<Integer> flipped = new LinkedList<>();
        int res = 0;
        for (int i = 0, needFlip = 0; i < n; i++) {
            if (i >= K) {
                needFlip ^= flipped.pollFirst(); // cancel 1 flip
            }
            if (needFlip == A[i]) {
                if (i + K > n) { return -1; }

                res++;
                needFlip ^= 1; // accumulate 1 flip
                flipped.offerLast(1);
            } else {
                flipped.offerLast(0);
            }
        }
        return res;
    }

    // Greedy + Sliding Window + Queue
    // time complexity: O(N), space complexity: O(K)
    // 16 ms(28.49%), 46.6 MB(96.54%) for 110 tests
    public int minKBitFlips4(int[] A, int K) {
        Queue<Integer> flipped = new LinkedList<>();
        int res = 0;
        for (int i = 0, n = A.length; i < n; i++) {
            if (A[i] == flipped.size() % 2) {
                res++;
                flipped.offer(i + K - 1);
            }
            if (!flipped.isEmpty() && flipped.peek() <= i) {
                flipped.poll();
            }
        }
        return flipped.isEmpty() ? res : -1;
    }

    private void test(int[] A, int K, int expected) {
        assertEquals(expected, minKBitFlips(A.clone(), K));
        assertEquals(expected, minKBitFlips2(A, K));
        assertEquals(expected, minKBitFlips3(A, K));
        assertEquals(expected, minKBitFlips4(A, K));
    }

    @Test public void test() {
        test(new int[] {0, 1, 0, 1, 1}, 2, 2);
        test(new int[] {1, 1, 0}, 2, -1);
        test(new int[] {0, 1}, 2, -1);
        test(new int[] {0, 1, 0}, 1, 2);
        test(new int[] {0, 1, 0, 0, 1, 0}, 4, 3);
        test(new int[] {0, 0, 0, 1, 0, 1, 1, 0}, 3, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
