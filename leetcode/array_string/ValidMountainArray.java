import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC941: https://leetcode.com/problems/valid-mountain-array/
//
// Given an array, return true if and only if it is a valid mountain array.
// Recall that A is a mountain array if and only if:
// A.length >= 3
// There exists some i with 0 < i < A.length - 1 such that:
// A[0] < A[1] < ... A[i-1] < A[i]
// A[i] > A[i+1] > ... > A[B.length - 1]
public class ValidMountainArray {
    // beats %(4 ms for 51 tests)
    public boolean validMountainArray(int[] A) {
        boolean topFound = false;
        for (int i = 1, n = A.length; i < n; i++) {
            if (A[i] == A[i - 1]) return false;

            if (A[i] > A[i - 1]) {
                if (topFound) return false;
            } else {
                if (i == 1) return false;

                topFound = true;
            }
        }
        return topFound;
    }

    // beats %(5 ms for 51 tests)
    public boolean validMountainArray2(int[] A) {
        int n = A.length;
        int i = 0;
        for (; i + 1 < n && A[i] < A[i + 1]; i++) {}

        if (i == 0 || i == n - 1) return false;

        for (; i + 1 < n && A[i] > A[i + 1]; i++) {}
        return i == n - 1;
    }

    // beats %(3 ms for 51 tests)
    public boolean validMountainArray3(int[] A) {
        int n = A.length;
        int i = 0;
        int j = n - 1;
        for (; i + 1 < n && A[i] < A[i + 1]; i++) {}
        for (; j > 0 && A[j - 1] > A[j]; j--) {}
        return i > 0 && i == j && j < n - 1;
    }

    void test(int[] A, boolean expected) {
        assertEquals(expected, validMountainArray(A));
        assertEquals(expected, validMountainArray2(A));
        assertEquals(expected, validMountainArray3(A));
    }

    @Test
    public void test() {
        test(new int[] {2, 1}, false);
        test(new int[] {3, 5, 5}, false);
        test(new int[] {0, 2, 3, 1}, true);
        test(new int[] {1, 2, 3}, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
