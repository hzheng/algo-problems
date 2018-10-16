import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC922: https://leetcode.com/problems/sort-array-by-parity-ii/
//
// Given an array A of non-negative integers, half of the integers in A are odd,
// and half of the integers are even. Sort the array so that whenever A[i] is 
// odd, i is odd; and whenever A[i] is even, i is even.
public class SortArrayByParityII {
    // time complexity: O(N), space complexity: O(N)
    // beats 70.13%(7 ms for 61 tests)
    public int[] sortArrayByParityII(int[] A) {
        int n = A.length;
        int[] res = new int[n];
        for (int i = 0, j = 0, k = 1; i < n; i++) {
            int cur = A[i];
            if (cur % 2 == 0) {
                res[j] = cur;
                j += 2;
            } else {
                res[k] = cur;
                k += 2;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 70.13%(7 ms for 61 tests)
    public int[] sortArrayByParityII2(int[] A) {
        for (int i = 0, j = 1; i < A.length; i += 2) {
            if (A[i] % 2 == 1) {
                for (; A[j] % 2 == 1; j += 2) {}

                int tmp = A[i];
                A[i] = A[j];
                A[j] = tmp;
            }
        }
        return A;
    }

    void test(int[] A, int[] expected) {
        assertArrayEquals(expected, sortArrayByParityII(A));
        assertArrayEquals(expected, sortArrayByParityII2(A));
    }

    @Test
    public void test() {
        test(new int[] {4, 2, 5, 7}, new int[] {4, 5, 2, 7});
        test(new int[] {7, 2, 1, 0}, new int[] {2, 7, 0, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
