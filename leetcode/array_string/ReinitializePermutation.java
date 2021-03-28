import org.junit.Test;

import static org.junit.Assert.*;

// LC1806: https://leetcode.com/problems/minimum-number-of-operations-to-reinitialize-a-permutation/
//
// You are given an even integer n. You initially have a permutation perm of size n where
// perm[i] == i (0-indexed).
// In one operation, you will create a new array arr, and for each i:
// If i % 2 == 0, then arr[i] = perm[i / 2].
// If i % 2 == 1, then arr[i] = perm[n / 2 + (i - 1) / 2].
// You will then assign arr to perm.
// Return the minimum non-zero number of operations you need to perform on perm to return the
// permutation to its initial value.
//
// Constraints:
// 2 <= n <= 1000
// n is even.
public class ReinitializePermutation {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(%), 35.9 MB(%) for 66 tests
    public int reinitializePermutation(int n) {
        int res = 1;
        for (int i = 1; ; res++) {
            if (i % 2 == 0) {
                i /= 2;
            } else {
                i = n / 2 + (i - 1) / 2;
            }
            if (i == 1) { break; }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(%), 35.3 MB(%) for 66 tests
    public int reinitializePermutation2(int n) {
        int res = 0;
        for (int i = 1; res == 0 || i > 1; res++) { // reverse process
            i = i * 2 % (n - 1);
        }
        return res;
    }

    private void test(int n, int expected) {
        assertEquals(expected, reinitializePermutation(n));
        assertEquals(expected, reinitializePermutation2(n));
    }

    @Test public void test() {
        test(2, 1);
        test(4, 2);
        test(6, 4);
        test(10, 6);
        test(1000, 36);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
