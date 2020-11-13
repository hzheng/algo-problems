import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC801: https://leetcode.com/problems/minimum-swaps-to-make-sequences-increasing/
//
// We have two integer sequences A and B of the same non-zero length. We are allowed to swap
// elements A[i] and B[i].  Note that both elements are in the same index position in their
// respective sequences. At the end of some number of swaps, A and B are both strictly increasing.
// Given A and B, return the minimum number of swaps to make both sequences strictly increasing.
// It is guaranteed that the given input always makes it possible.
//
// Note:
// A, B are arrays with the same length, and that length will be in the range [1, 1000].
// A[i], B[i] are integer values in the range [0, 2000].
public class MinSwap {
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 38.7 MB(97.45%) for 102 tests
    public int minSwap(int[] A, int[] B) {
        int keep = 0;
        int swap = 1;
        for (int i = 1; i < A.length; i++) {
            if (Math.min(A[i], B[i]) > Math.max(A[i - 1], B[i - 1])) {
                keep = Math.min(keep, swap);
                swap = keep + 1;
            } else if (A[i] > A[i - 1] && B[i] > B[i - 1]) {
                swap++;
            } else {
                int tmp = keep;
                keep = swap;
                swap = tmp + 1;
            }
        }
        return Math.min(keep, swap);
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 39.2 MB(60.99%) for 102 tests
    public int minSwap2(int[] A, int[] B) {
        int keep = 0;
        int swap = 1;
        for (int i = 1; i < A.length; i++) {
            int nextKeep = Integer.MAX_VALUE;
            int nextSwap = Integer.MAX_VALUE;
            if (A[i - 1] < A[i] && B[i - 1] < B[i]) {
                nextKeep = Math.min(nextKeep, keep);
                nextSwap = Math.min(nextSwap, swap + 1);
            }
            if (A[i - 1] < B[i] && B[i - 1] < A[i]) {
                nextKeep = Math.min(nextKeep, swap);
                nextSwap = Math.min(nextSwap, keep + 1);
            }
            keep = nextKeep;
            swap = nextSwap;
        }
        return Math.min(keep, swap);
    }

    private void test(int[] A, int[] B, int expected) {
        assertEquals(expected, minSwap(A, B));
        assertEquals(expected, minSwap2(A, B));
    }

    @Test public void test() {
        test(new int[] {7, 8, 9, 8, 9}, new int[] {1, 2, 4, 18, 21}, 2);
        test(new int[] {1, 3, 5, 4}, new int[] {1, 2, 3, 7}, 1);
        test(new int[] {3, 3, 8, 9, 10}, new int[] {1, 7, 4, 6, 8}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
