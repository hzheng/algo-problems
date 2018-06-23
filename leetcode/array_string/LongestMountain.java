import org.junit.Test;
import static org.junit.Assert.*;

// LC845: https://leetcode.com/problems/longest-mountain-in-array/
//
// Let's call any (contiguous) subarray B (of A) a mountain if the following
// properties hold:
// B.length >= 3
// There exists some 0 < i < B.length - 1 such that B[0] < B[1] < ... B[i-1]
// < B[i] > B[i+1] > ... > B[B.length - 1]
// Given an array A of integers, return the length of the longest mountain.
public class LongestMountain {
    // beats %(12 ms for 72 tests)
    public int longestMountain(int[] A) {
        int res = 0;
        boolean up = false;
        boolean down = false;
        for (int i = 1, start = 0; i < A.length; i++) {
            if (A[i] == A[i - 1]) {
                up = false;
                down = false;
                start = i;
            } else if (A[i] > A[i - 1]) {
                if (down) {
                    start = i - 1;
                }
                up = true;
                down = false;
            } else {
                if (up) {
                    res = Math.max(res, i - start + 1);
                } else {
                    start = i;
                }
                down = true;
            }
        }
        return res;
    }

    // beats %(11 ms for 72 tests)
    public int longestMountain2(int[] A) {
        int res = 0;
        boolean inMountain = false;
        for (int i = 1, start = -1; i < A.length; i++) {
            if (A[i] < A[i - 1]) {
                if (start >= 0) {
                    res = Math.max(res, i - start + 1);
                    inMountain = true;
                }
            } else if (A[i] > A[i - 1]) {
                if (start < 0) {
                    start = i - 1;
                } else if (inMountain) {
                    start = i - 1;
                    inMountain = false;
                }
            } else {
                start = -1;
                inMountain = false;
            }
        }
        return res;
    }

    // beats %(12 ms for 72 tests)
    public int longestMountain3(int[] A) {
        int res = 0;
        for (int start = 0, n = A.length, top, end = 0; start < n; ) {
            for (top = start; top + 1 < n && A[top] < A[top + 1]; top++) {}
            if (top > start) {
                for (end = top; end + 1 < n && A[end] > A[end + 1]; end++) {}
                if (end > top) {
                    res = Math.max(res, end - start + 1);
                }
            }
            start = Math.max(end, start + 1);
        }
        return res;
    }

    // beats %(12 ms for 72 tests)
    public int longestMountain4(int[] A) {
        int res = 0;
        for (int i = 1, up = 0, down = 0; i < A.length; i++) {
            if (A[i - 1] == A[i] || down > 0 && A[i - 1] < A[i]) {
                up = down = 0;
            } 
            if (A[i - 1] < A[i]) {
                up++;
            } else if (A[i - 1] > A[i]) {
                down++;
                if (up > 0 && up + down + 1 > res) {
                    res = up + down + 1;
                }
            }
        }
        return res;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, longestMountain(A));
        assertEquals(expected, longestMountain2(A));
        assertEquals(expected, longestMountain3(A));
        assertEquals(expected, longestMountain4(A));
    }

    @Test
    public void test() {
        test(new int[] { 2, 3, 3, 2, 0, 2 }, 0);
        test(new int[] { 1, 2, 0, 2, 0, 2 }, 3);
        test(new int[] { 2, 1, 4, 7, 3, 2, 5 }, 5);
        test(new int[] { 2, 2, 2 }, 0);
        test(new int[] { 875, 884, 239, 731, 723, 685 }, 4);
        test(new int[] { 2, 1, 3, 1, 0, 1, 3, 1, 0, 1, 2, 0, 0, 0, 2, 1, 0, 3,
                         0, 3, 1, 0, 3, 2, 1, 1, 1, 0, 2, 0, 0, 1,
                         0, 2, 2, 0, 3, 1, 2, 2, 0, 3, 2, 2, 3, 3, 3, 1, 3, 0,
                         0, 2, 3, 0, 0, 1, 0, 1, 2, 3, 1, 0, 1, 0, 0, 3, 0,
                         3, 2, 0, 2, 1, 1, 2, 0, 0, 2, 3, 1, 1, 1, 3, 1, 0, 1,
                         3, 2, 0, 0, 3, 0, 2, 0, 3, 2, 2, 0, 0, 3, 0, 3, 1,
                         0, 0, 2, 2, 1, 2, 1, 1, 3, 0, 1, 1, 3, 1, 2, 0, 3, 1,
                         1, 0, 2, 2, 2, 0, 2, 2, 2, 0, 0, 0, 0, 0, 1, 0, 1,
                         2, 2, 1, 3, 3, 0, 0, 3, 0, 1, 0, 0, 2, 2, 2, 1, 0, 3,
                         0, 2, 3, 1, 0, 1, 0, 0, 2, 3, 2, 3, 0, 2, 0, 0, 1,
                         2, 3, 1, 3, 3, 3, 2, 2, 3, 2, 0, 0, 0, 2, 2, 1, 0, 3,
                         0, 1, 3, 1, 0, 2, 3, 3, 3, 0, 2, 3, 3, 3, 3, 3, 3,
                         1, 2, 3, 1, 2, 2, 2, 2, 3, 3, 2, 0, 0, 1, 2, 1, 3, 2,
                         1, 2, 2, 2, 0, 0, 3, 2, 2, 2, 3, 1, 0, 1, 3, 0, 2,
                         3, 2, 3, 1, 3, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 0, 0, 0,
                         1, 3, 2, 0, 3, 1, 3, 2, 2, 0, 0, 3, 3, 1, 0, 1, 3,
                         1, 2, 2, 1, 2, 1, 2, 1, 3, 0, 1, 1, 1, 0, 0, 0, 1, 2,
                         0, 2, 2, 1, 2, 2, 2, 2, 1, 2, 3, 3, 3, 3, 1, 2, 1,
                         1, 0, 0, 0, 0, 1, 1, 2, 3, 1, 0, 3, 0, 3, 0, 3, 3, 2,
                         2, 3, 0, 3, 2, 2, 3, 0, 3, 0, 3, 3, 0, 2, 0, 3, 0,
                         2, 0, 3, 2, 0, 2, 1, 0, 1, 1, 1, 1, 2, 2, 3, 0, 1, 1,
                         2, 0, 0, 1, 2, 3, 2, 2, 2, 3, 0, 1, 3, 3, 0, 3, 3,
                         3, 0, 1, 3, 0, 3, 0, 3, 2, 0, 2, 2, 0, 1, 0, 0, 3, 1,
                         1, 2, 0, 0, 3, 1, 3, 2, 0, 0, 0, 3, 1, 0, 1, 1, 1,
                         0, 0, 3, 2, 2, 3, 0, 3, 0, 3, 2, 1, 1, 2, 0, 3, 3, 0,
                         0, 2, 2, 3, 0, 1, 2, 0, 2, 0, 0, 0, 1, 3, 1, 2, 2,
                         1, 1, 3, 0, 2, 0, 3, 3, 3, 2, 3, 3, 2, 0, 2, 1, 2, 3,
                         2, 1, 0, 2, 2, 2, 2, 0, 3, 0, 2, 3, 3, 2, 2, 0, 3,
                         0, 2, 1, 1, 3, 2, 0, 3, 0, 1, 3, 1, 1, 2, 3, 1, 1, 1,
                         3, 3, 3, 0, 3, 2, 1, 2, 0, 0, 0, 0, 2, 0, 3, 0, 2,
                         0, 3, 0, 3, 0, 3, 0, 3, 1, 3, 3, 2, 1, 0, 2, 3, 1, 0,
                         3, 2, 0, 1, 1, 0, 2, 2, 1, 1, 3, 2, 2, 2, 2, 1, 1,
                         2, 0, 2, 2, 2, 0, 0, 2, 0, 1, 1, 0, 0, 1, 3, 1, 1, 3,
                         3, 1, 1, 2, 0, 1, 2, 2, 1, 3, 3, 2, 3, 1, 0, 1, 0,
                         2, 3, 1, 3, 2, 1, 3, 2, 1, 0, 0, 0, 3, 3, 1, 0, 0, 2,
                         0, 1, 2, 2, 2, 3, 2, 0, 3, 0, 0, 2, 3, 0, 1, 2, 0,
                         2, 3, 0, 3, 1, 0, 1, 0, 1, 1, 3, 2, 3, 0, 2, 3, 1, 2,
                         0, 0, 3, 2, 1, 0, 0, 1, 3, 0, 2, 1, 2, 3, 2, 2, 3,
                         3, 1, 3, 1, 0, 0, 1, 0, 2, 1, 3, 1, 0, 3, 0, 3, 2, 3,
                         2, 1, 1, 2, 1, 2, 3, 2, 1, 0, 1, 1, 1, 3, 2, 1, 1,
                         3, 1, 0, 3, 1, 2, 2, 3, 3, 3, 3, 3, 1, 3, 3, 2, 0, 3,
                         2, 3, 1, 1, 3, 0, 3, 0, 0, 2, 1, 3, 2, 2, 1, 0, 1,
                         3, 1, 3, 0, 3, 3, 3, 2, 2, 0, 2, 1, 0, 2, 3, 3, 2, 3,
                         0, 2, 1, 1, 2, 2, 3, 0, 3, 2, 3, 1, 1, 1, 1, 3, 3,
                         2, 1, 2, 3, 2, 1, 2, 0, 0, 0, 1, 0, 1, 2, 2, 2, 3, 0,
                         1, 0, 3, 0, 2, 3, 0, 3, 1, 1, 0, 3, 1, 2, 2, 0, 1,
                         0, 1, 2, 0, 2, 3, 0, 1, 1, 0, 1, 0, 2, 0, 2, 2, 3, 0,
                         0, 0, 2, 2, 3, 1, 2, 2, 3, 1, 3, 2, 3, 3, 0, 2, 2,
                         3, 2, 3, 0, 1, 1, 1, 0, 3, 3, 2, 2, 0, 2, 0, 2, 2, 2,
                         1, 2, 2, 0, 0, 1, 2, 0, 2, 0, 1, 0, 1, 1, 3, 0, 3,
                         2, 2, 2, 1, 1, 0, 1, 2, 3, 2, 1, 0, 1, 2, 1, 1, 3, 0,
                         1, 2, 2, 1, 3, 2, 2, 1, 3, 3, 0, 0, 3, 0, 3, 1, 0,
                         3, 2, 1, 3, 3, 0, 0, 3, 2, 0, 1, 0, 2, 0, 1, 2, 3, 0,
                         3, 2, 2, 0, 2, 0, 1, 0, 2, 1, 2, 0, 3, 2, 0, 3, 1,
                         0, 0, 0, 0, 1, 3, 0, 2, 0, 1, 2, 3, 3, 1, 2, 3, 1, 1,
                         3, 0, 3, 3, 3, 1, 0, 0, 1, 1, 1, 3, 3, 0, 3, 0, 1,
                         2, 0, 0, 1, 1, 0, 3, 2, 0, 0, 0, 1, 0, 1, 1, 0, 1, 2,
                         3, 1, 3, 3, 3 }, 7);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
