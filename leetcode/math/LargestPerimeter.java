import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC976: https://leetcode.com/problems/largest-perimeter-triangle/
//
// Given an array A of positive lengths, return the largest perimeter of a triangle with non-zero
// area, formed from 3 of these lengths.
// If it is impossible to form any triangle of non-zero area, return 0.
//
// Note:
// 3 <= A.length <= 10000
// 1 <= A[i] <= 10^6
public class LargestPerimeter {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 7 ms(60.65%), 39 MB(99.35%) for 84 tests
    public int largestPerimeter(int[] A) {
        Arrays.sort(A);
        int n = A.length;
        int a = A[n - 1];
        int b = A[n - 2];
        int c = A[n - 3];
        for (int i = n - 4; ; i--) {
            if (b + c > a) { return a + b + c; }
            if (i < 0) { return 0; }

            a = b;
            b = c;
            c = A[i];
        }
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 6 ms(99.84%), 39.9 MB(42.26%) for 84 tests
    public int largestPerimeter2(int[] A) {
        Arrays.sort(A);
        for (int i = A.length - 3; i >= 0; i--) {
            if (A[i] + A[i + 1] > A[i + 2]) {
                return A[i] + A[i + 1] + A[i + 2];
            }
        }
        return 0;
    }

    private void test(int[] A, int expected) {
        assertEquals(expected, largestPerimeter(A));
        assertEquals(expected, largestPerimeter2(A));
    }

    @Test public void test() {
        test(new int[] {1, 2, 2, 4, 18, 8}, 5);
        test(new int[] {2, 1, 2}, 5);
        test(new int[] {1, 2, 1}, 0);
        test(new int[] {3, 2, 3, 4}, 10);
        test(new int[] {3, 6, 2, 3}, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
