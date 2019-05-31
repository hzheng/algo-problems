import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1053: https://leetcode.com/problems/previous-permutation-with-one-swap/
//
// Given an array A of positive integers, return the lexicographically largest permutation that is
// smaller than A, that can be made with one swap. If it cannot be done, then return the same array.
//
// Note:
// 1 <= A.length <= 10000
// 1 <= A[i] <= 10000
public class PreviousPermutationWithOneSwap {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 39.5 MB(100%) for 52 tests
    public int[] prevPermOpt1(int[] A) {
        int i = A.length - 1;
        for (; i > 0 && A[i] >= A[i - 1]; i--) {}
        if (--i < 0) {
            return A;
        }
        for (int j = A.length - 1; j > i; j--) {
            if (A[j] < A[i] && (A[j - 1] != A[j])) {
                int tmp = A[j];
                A[j] = A[i];
                A[i] = tmp;
                break;
            }
        }
        return A;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 39.5 MB(100%) for 52 tests
    public int[] prevPermOpt1_2(int[] A) {
        int left = A.length - 2;
        for (; left >= 0 && A[left] <= A[left + 1]; left--) {}
        if (left < 0) {
            return A;
        }
        int right = A.length - 1;
        for (; A[left] <= A[right]; right--) {}
        for (; A[right - 1] == A[right]; right--) {}
        int tmp = A[left];
        A[left] = A[right];
        A[right] = tmp;
        return A;
    }

    void test(int[] A, int[] expected) {
        assertArrayEquals(expected, prevPermOpt1(A.clone()));
        assertArrayEquals(expected, prevPermOpt1_2(A.clone()));
    }

    @Test
    public void test() {
        test(new int[]{3, 2, 1}, new int[]{3, 1, 2});
        test(new int[]{1, 1, 5}, new int[]{1, 1, 5});
        test(new int[]{3, 1, 1, 3}, new int[]{1, 3, 1, 3});
        test(new int[]{1, 9, 4, 6, 7}, new int[]{1, 7, 4, 6, 9});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
