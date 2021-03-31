import org.junit.Test;

import static org.junit.Assert.*;

// LC1652: https://leetcode.com/problems/defuse-the-bomb/
//
// You have a bomb to defuse, and your time is running out! Your informer will provide you with a
// circular array code of length of n and a key k.
// To decrypt the code, you must replace every number. All the numbers are replaced simultaneously.
// If k > 0, replace the ith number with the sum of the next k numbers.
// If k < 0, replace the ith number with the sum of the previous k numbers.
// If k == 0, replace the ith number with 0.
// As code is circular, the next element of code[n-1] is code[0], and the previous element of
// code[0] is code[n-1].
// Given the circular array code and an integer key k, return the decrypted code to defuse the bomb!
//
// Constraints:
// n == code.length
// 1 <= n <= 100
// 1 <= code[i] <= 100
// -(n - 1) <= k <= n - 1
public class DefuseBomb {
    // time complexity: O(N^2), space complexity: O(N)
    // 0 ms(100.00%), 38.9 MB(68.20%) for 74 tests
    public int[] decrypt(int[] code, int k) {
        int n = code.length;
        int[] res = new int[n];
        if (k == 0) { return res; }

        int start = (k > 0) ? 1 : n + k;
        int end = (k > 0) ? k : n - 1;
        for (int i = start; i <= end; i++) {
            res[0] += code[i % n];
        }
        for (int i = 1; i < n; i++) {
            res[i] = res[i - 1] - code[(start++) % n] + code[++end % n];
        }
        return res;
    }

    private void test(int[] code, int k, int[] expected) {
        assertArrayEquals(expected, decrypt(code, k));
    }

    @Test public void test() {
        test(new int[] {5, 7, 1, 4}, 3, new int[] {12, 10, 16, 13});
        test(new int[] {1, 2, 3, 4}, 0, new int[] {0, 0, 0, 0});
        test(new int[] {2, 4, 9, 3}, -2, new int[] {12, 5, 6, 13});
        test(new int[] {10, 5, 7, 7, 3, 2, 10, 3, 6, 9, 1, 6}, -4,
             new int[] {22, 26, 22, 28, 29, 22, 19, 22, 18, 21, 28, 19});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
