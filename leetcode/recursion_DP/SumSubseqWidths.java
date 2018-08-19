import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC891: https://leetcode.com/problems/sum-of-subsequence-widths/
//
// Given an array A, consider all non-empty subsequences. For any sequence S,
// let the width of S be the difference between the maximum and minimum element
// of S. Return the sum of the widths of all subsequences of A.
// As the answer may be very large, return the answer modulo 10^9 + 7.
public class SumSubseqWidths {
    // Dynamic Programming + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats %(33 ms for 64 tests)
    public int sumSubseqWidths(int[] A) {
        int mod = 1000_000_000 + 7;
        Arrays.sort(A);
        int n = A.length;
        long[] power2 = new long[n + 1];
        power2[0] = 1;
        for (int i = 1; i <= n; i++) {
            power2[i] = power2[i - 1] * 2 % mod;
        }
        long res = 0;
        int i = 0;
        for (int a : A) {
            // 2^i smaller and 2^(n-i-1) bigger
            res = (res + (power2[i] - power2[n - i - 1]) * a) % mod;
            i++;
        }
        return (int) ((res + mod) % mod);
    }

    // Dynamic Programming + Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats %(30 ms for 64 tests)
    public int sumSubseqWidths2(int[] A) {
        int mod = 1000_000_000 + 7;
        Arrays.sort(A);
        int n = A.length;
        long power2 = 1;
        long res = 0;
        for (int i = 0; i < n; i++, power2 = (power2 << 1) % mod) {
            res = (res + power2 * (A[i] - A[n - 1 - i])) % mod;
        }
        return (int) ((res + mod) % mod);
    }

    void test(int[] A, int expected) {
        assertEquals(expected, sumSubseqWidths(A));
        assertEquals(expected, sumSubseqWidths2(A));
    }

    @Test
    public void test() {
        test(new int[] { 2, 1, 3 }, 6);
        test(new int[] { 5, 2, 1, 3 }, 30);
        test(new int[] { 5, 69, 89, 92, 31, 16, 25, 45, 63, 40, 16, 5 },
             305045);
        test(new int[] { 5, 69, 89, 92, 31, 16, 25, 45, 63, 40, 16, 56, 24, 40,
                         75, 82, 40, 12, 50, 62, 92, 44, 67, 38,
                         92, 22, 91, 24 }, 842042558);
        test(new int[] { 5, 69, 89, 92, 31, 16, 25, 45, 63, 40, 16, 56, 24, 40,
                         75, 82, 40, 12, 50, 62, 92, 44, 67, 38,
                         92, 22, 91, 24, 26, 21, 100, 42, 23, 56, 64, 43, 95,
                         76, 84, 79, 89, 4, 16, 94, 16, 77, 92, 9, 30, 13 },
             857876214);
        test(new int[] { 96, 87, 191, 197, 40, 101, 108, 35, 169, 50, 168, 182,
                         95, 80, 144, 43, 18, 60, 174, 13, 77,
                         173, 38, 46, 80, 117, 13, 19, 11, 6, 13, 118, 39, 80,
                         171, 36, 86, 156, 165, 190, 53, 49, 160, 192, 57,
                         42, 97, 35, 124, 200, 84, 70, 145, 180, 54, 141, 159,
                         42, 66, 66, 25, 95, 24, 136, 140, 159, 71, 131, 5,
                         140, 115, 76, 151, 137, 63, 47, 69, 164, 60, 172, 153,
                         183, 6, 70, 40, 168, 133, 45, 116, 188, 20, 52,
                         70, 156, 44, 27, 124, 59, 42, 172 }, 136988321);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
