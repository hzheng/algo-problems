import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC910: https://leetcode.com/problems/smallest-range-ii/
//
// Given an array A of integers, for each integer A[i] we need to choose either
// x = -K or x = K, and add x to A[i]. After this process, we have some array B.
// Return the smallest possible difference between the maximum value of B and 
// the minimum value of B.
public class SmallestRangeII {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats %(13 ms for 68 tests)
    public int smallestRangeII(int[] A, int K) {
        Arrays.sort(A);
        int n = A.length;
        int res = A[n - 1] - A[0];
        for (int i = 1; i < n; i++) {
            int max = Math.max(A[i - 1] + K, A[n - 1] - K);
            int min = Math.min(A[0] + K, A[i] - K);
            res = Math.min(res, max - min);
        }
        return res;
    }

    void test(int[] A, int K, int expected) {
        assertEquals(expected, smallestRangeII(A, K));
    }

    @Test
    public void test() {
        test(new int[] {1}, 0, 0);
        test(new int[] {0, 10}, 2, 6);
        test(new int[] {1, 3, 6}, 3, 3);
        test(new int[] {10, 3, 1}, 4, 2);
        test(new int[] {Integer.MAX_VALUE}, 0, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
