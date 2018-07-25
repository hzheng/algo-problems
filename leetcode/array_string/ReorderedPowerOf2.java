import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC869: https://leetcode.com/problems/reordered-power-of-2/
//
// Starting with a positive integer N, we reorder the digits in any order 
// (including the original order) such that the leading digit is not zero.
// Return true if and only if we can do this in a way such that the resulting 
// number is a power of 2.
// Note: 1 <= N <= 10^9
public class ReorderedPowerOf2 {
    // Sort
    // beats 79.94%(7 ms for 135 tests)
    public boolean reorderedPowerOf2(int N) {
        char[] nChars = ("" + N).toCharArray();
        Arrays.sort(nChars);
        for (int i = 0; i < 31; i++) {
            char[] num = String.valueOf(1 << i).toCharArray();
            Arrays.sort(num);
            if (Arrays.equals(num, nChars)) return true;
        }
        return false;
    }

    // Sort
    // beats 79.94%(7 ms for 135 tests)
    public boolean reorderedPowerOf2_2(int N) {
        char[] nChars = ("" + N).toCharArray();
        Arrays.sort(nChars);
        for (int i = 0; i < 31; i++) {
            char[] num = String.valueOf(1 << i).toCharArray();
            if (num.length > nChars.length) return false;
            if (num.length == nChars.length) {
                Arrays.sort(num);
                if (Arrays.equals(num, nChars)) return true;
            }
        }
        return false;
    }

    // Recursion + Backtracking
    // time complexity: O((logN)!∗logN), space complexity: O(log(N))
    // beats 26.67%(91 ms for 135 tests)
    public boolean reorderedPowerOf2_3(int N) {
        String S = Integer.toString(N);
        int[] A = new int[S.length()];
        for (int i = 0; i < S.length(); i++) {
            A[i] = S.charAt(i) - '0';
        }
        return permutations(A, 0);
    }

    private boolean isPowerOfTwo(int[] A) {
        if (A[0] == 0) return false;

        int N = 0;
        for (int x : A) {
            N = 10 * N + x;
        }
        for (; N > 0 && ((N & 1) == 0); N >>= 1) {}
        return N == 1;
    }

    private boolean permutations(int[] A, int start) {
        if (start == A.length) return isPowerOfTwo(A);

        for (int i = start; i < A.length; i++) {
            swap(A, start, i);
            if (permutations(A, start + 1)) return true;

            swap(A, start, i);
        }
        return false;
    }

    private void swap(int[] A, int i, int j) {
        int tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }

    // Counting
    // time complexity: O((logN)^2), space complexity: O(log(N))
    // beats 99.53%(6 ms for 135 tests)
    public boolean reorderedPowerOf2_4(int N) {
        int[] A = count(N);
        for (int i = 0; i < 31; i++) {
            if (Arrays.equals(A, count(1 << i))) return true;
        }
        return false;
    }

    private int[] count(int N) {
        int[] res = new int[10];
        for (; N > 0; N /= 10) {
            res[N % 10]++;
        }
        return res;
    }

    // Counting
    // time complexity: O((logN)^2), space complexity: O(log(N))
    // beats 79.94%(7 ms for 135 tests)
    public boolean reorderedPowerOf2_5(int N) {
        long cnt = count2(N);
        for (int i = 0; i < 32; i++) {
            if (count2(1 << i) == cnt) return true;
        }
        return false;
    }

    private long count2(int N) {
        long res = 0;
        for (; N > 0; N /= 10) {
            res += (int)Math.pow(10, N % 10);
        }
        return res;
    }

    void test(int N, boolean expected) {
        assertEquals(expected, reorderedPowerOf2(N));
        assertEquals(expected, reorderedPowerOf2_2(N));
        assertEquals(expected, reorderedPowerOf2_3(N));
        assertEquals(expected, reorderedPowerOf2_4(N));
        assertEquals(expected, reorderedPowerOf2_5(N));
    }

    @Test
    public void test() {
        test(1, true);
        test(10, false);
        test(16, true);
        test(24, false);
        test(46, true);
        test(4021, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
