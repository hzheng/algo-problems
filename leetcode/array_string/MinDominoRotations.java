import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1007: https://leetcode.com/problems/minimum-domino-rotations-for-equal-row/
//
// In a row of dominoes, A[i] and B[i] represent the top and bottom halves of the ith domino. (A
// domino is a tile with two numbers from 1 to 6 - one on each half of the tile.)
// We may rotate the ith domino, so that A[i] and B[i] swap values.
// Return the minimum number of rotations so that all the values in A are the same, or all the
// values in B are the same. If it cannot be done, return -1.
//
// Constraints:
// 2 <= A.length == B.length <= 2 * 10^4
// 1 <= A[i], B[i] <= 6
public class MinDominoRotations {
    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 6 ms(30.45%), 47.1 MB(21.63%) for 81 tests
    public int minDominoRotations(int[] A, int[] B) {
        int[] count = new int[7];
        for (int a : A) {
            count[a]++;
        }
        for (int b : B) {
            count[b]++;
        }
        int cand = 0;
        int n = A.length;
        for (int i = 1; i < count.length; i++) {
            if (count[i] >= n) {
                cand = i;
                break;
            }
        }
        return check(A, B, cand);
    }

    private int check(int[] A, int[] B, int cand) {
        int res1 = 0;
        int res2 = 0;
        for (int i = A.length - 1; i >= 0; i--) {
            if (A[i] != cand) {
                if (B[i] != cand) { return -1; }

                res2++;
            }
            if (B[i] != cand) {
                res1++;
            }
        }
        return Math.min(res1, res2);
    }

    // time complexity: O(N), space complexity: O(1)
    // 3 ms(97.14%), 46.8 MB(39.40%) for 81 tests
    public int minDominoRotations2(int[] A, int[] B) {
        int res = check(A, B, A[0]);
        return (res >= 0) ? res : check(A, B, B[0]);
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // 5 ms(48.70%), 47.3 MB(11.57%) for 81 tests
    public int minDominoRotations3(int[] A, int[] B) {
        int[] countA = new int[7];
        int[] countB = new int[7];
        int[] same = new int[7];
        int n = A.length;
        for (int i = 0; i < n; i++) {
            countA[A[i]]++;
            countB[B[i]]++;
            if (A[i] == B[i]) {
                same[A[i]]++;
            }
        }
        for (int i = 1; i < countA.length; i++) {
            if (countA[i] + countB[i] - same[i] >= n) {
                return Math.min(countA[i], countB[i]) - same[i];
            }
        }
        return -1;
    }

    // Set
    // time complexity: O(N), space complexity: O(1)
    // 76 ms(5.10%), 46.8 MB(49.49%) for 81 tests
    public int minDominoRotations4(int[] A, int[] B) {
        Set<Integer> cand = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        int[] countA = new int[7];
        int[] countB = new int[7];
        for (int i = A.length - 1; i >= 0; i--) {
            cand.retainAll(new HashSet<>(Arrays.asList(A[i], B[i])));
            countA[A[i]]++;
            countB[B[i]]++;
        }
        for (int i : cand) {
            return A.length - Math.max(countA[i], countB[i]);
        }
        return -1;
    }

    // time complexity: O(N), space complexity: O(1)
    // 6 ms(30.45%), 46.5 MB(71.21%) for 81 tests
    public int minDominoRotations5(int[] A, int[] B) {
        int cand1 = A[0];
        int cand2 = B[0];
        int topSwap1 = 0;
        int bottomSwap1 = 0;
        int topSwap2 = 0;
        int bottomSwap2 = 0;
        int n = A.length;
        for (int i = 0; i < n; i++) {
            if (A[i] != cand1 && B[i] != cand1) {
                cand1 = 0;
            }
            if (A[i] != cand2 && B[i] != cand2) {
                cand2 = 0;
            }
            if (cand1 + cand2 == 0) { return -1; }

            topSwap1 += (A[i] == cand1) ? 1 : 0;
            bottomSwap1 += (B[i] == cand1) ? 1 : 0;
            topSwap2 += (A[i] == cand2) ? 1 : 0;
            bottomSwap2 += (B[i] == cand2) ? 1 : 0;
        }
        return n - Math.max(Math.max(topSwap1, bottomSwap1), Math.max(topSwap2, bottomSwap2));
    }

    private void test(int[] A, int[] B, int expected) {
        assertEquals(expected, minDominoRotations(A, B));
        assertEquals(expected, minDominoRotations2(A, B));
        assertEquals(expected, minDominoRotations3(A, B));
        assertEquals(expected, minDominoRotations4(A, B));
        assertEquals(expected, minDominoRotations5(A, B));
    }

    @Test public void test() {
        test(new int[] {2, 1, 2, 4, 2, 2}, new int[] {5, 2, 6, 2, 3, 2}, 2);
        test(new int[] {3, 5, 1, 2, 3}, new int[] {3, 6, 3, 3, 4}, -1);
        test(new int[] {1, 2, 1, 1, 1, 2, 2, 2}, new int[] {2, 1, 2, 2, 2, 2, 2, 2}, 1);
        test(new int[] {1, 2, 1, 3}, new int[] {2, 1, 2, 1}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
