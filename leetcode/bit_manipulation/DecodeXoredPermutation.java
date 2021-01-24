import org.junit.Test;

import static org.junit.Assert.*;

// LC1734: https://leetcode.com/problems/decode-xored-permutation/
//
// There is an integer array perm that is a permutation of the first n positive integers, where n is
// always odd. It was encoded into another integer array encoded of length n - 1, such that
// encoded[i] = perm[i] XOR perm[i + 1]. For example, if perm = [1,3,2], then encoded = [2,1].
// Given the encoded array, return the original array perm. It is guaranteed that the answer exists
// and is unique.
//
// Constraints:
// 3 <= n < 10^5
// n is odd.
// encoded.length == n - 1
public class DecodeXoredPermutation {
    // Bit Manipulation
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(100.00%), 60.8 MB(22.22%) for 63 tests
    public int[] decode(int[] encoded) {
        int n = encoded.length + 1;
        int first = n;
        for (int i = 1, firstXor = 0; i < n; i++) {
            first ^= i; // store xor of all numbers
            firstXor ^= encoded[i - 1]; // store a1^a2, a1^a3, ..., a1^an
            first ^= firstXor; // store a1 since even number of a1's cancel themselves
        }
        int[] res = new int[n];
        res[0] = first;
        for (int i = 1; i < n; i++) {
            res[i] = encoded[i - 1] ^ res[i - 1];
        }
        return res;
    }

    private void test(int[] encoded, int[] expected) {
        assertArrayEquals(expected, decode(encoded));
    }

    @Test public void test() {
        test(new int[] {3, 1}, new int[] {1, 2, 3});
        test(new int[] {6, 5, 4, 6}, new int[] {2, 4, 1, 5, 3});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
