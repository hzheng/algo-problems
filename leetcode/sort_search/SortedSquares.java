import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC977: https://leetcode.com/problems/squares-of-a-sorted-array/
//
// Given an array of integers A sorted in non-decreasing order, return an array 
// of the squares of each number, also in sorted non-decreasing order.
public class SortedSquares {
    // Two Pointers
    // 7 ms(95.58%), 31.6 MB(100%) for 132 tests
    // time complexity: O(N), space complexity: O(N)
    public int[] sortedSquares(int[] A) {
        int n = A.length;
        int[] res = new int[n];
        int delimiter = 0;
        for (; delimiter < n; delimiter++) {
            if (A[delimiter] >= 0) break;
        }
        for (int i = delimiter, j = delimiter - 1, index = 0; i < n || j >= 0; index++) {
            int next = 0;
            if (i >= n) {
                next = A[j--];
            } else if (j < 0) {
                next = A[i++];
            } else if (A[i] * A[i] <= A[j] * A[j]) {
                next = A[i++];
            } else {
                next = A[j--];
            }
            res[index] = next * next;
        }
        return res;
    }

    // Solution of Choice
    // Two Pointers
    // 6 ms(100.00%), 32.8 MB(100%) for 132 tests
    // time complexity: O(N), space complexity: O(N)
    public int[] sortedSquares2(int[] A) {
        int n = A.length;
        int[] res = new int[n];
        for (int i = 0, j = n - 1, index = n - 1; index >= 0; index--) {
            if (A[i] + A[j] < 0) {
                res[index] = A[i] * A[i++];
            } else {
                res[index] = A[j] * A[j--];
            }
        }
        return res;
    }

    // Sort
    // 9 ms(57.41%), 32.3 MB(100%) for 132 tests
    // time complexity: O(N * log(N)), space complexity: O(N)
    public int[] sortedSquares3(int[] A) {
        int n = A.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = A[i] * A[i];
        }
        Arrays.sort(res);
        return res;
    }

    void test(int[] A, int[] expected) {
        assertArrayEquals(expected, sortedSquares(A));
        assertArrayEquals(expected, sortedSquares2(A));
        assertArrayEquals(expected, sortedSquares3(A));
    }

    @Test
    public void test() {
        test(new int[] {-4, -1, 0, 3, 10}, new int[] {0, 1, 9, 16, 100});
        test(new int[] {-7, -3, 2, 3, 11}, new int[] {4, 9, 9, 49, 121});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
