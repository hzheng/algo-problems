import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC908: https://leetcode.com/problems/smallest-range-i/
//
// Given an array A of integers, for each integer A[i] we may choose any x with
// -K <= x <= K, and add x to A[i]. After this process, we have some array B.
// Return the smallest possible difference between the maximum value of B and
// the minimum value of B.
public class SmallestRangeI {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats %(11 ms for 68 tests)
    public int smallestRangeI(int[] A, int K) {
        Arrays.sort(A);
        return Math.max(A[A.length - 1] - A[0] - 2 * K, 0);
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(10 ms for 68 tests)
    public int smallestRangeI2(int[] A, int K) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int a : A) {
            min = Math.min(min, a);
            max = Math.max(max, a);
        }
        return Math.max(max - min - 2 * K, 0);
    }

    void test(int[] A, int K, int expected) {
        assertEquals(expected, smallestRangeI(A, K));
        assertEquals(expected, smallestRangeI2(A, K));
    }

    @Test
    public void test() {
        test(new int[] { 1 }, 0, 0);
        test(new int[] { 0, 10 }, 2, 6);
        test(new int[] { 1, 3, 6 }, 3, 0);
        test(new int[] { 10, 3, 1 }, 4, 1);
        test(new int[] { Integer.MAX_VALUE }, 0, 0);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
