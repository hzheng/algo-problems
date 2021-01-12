import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1720: https://leetcode.com/problems/decode-xored-array/
//
// There is a hidden integer array arr that consists of n non-negative integers. It was encoded
// into another integer array encoded of length n - 1, such that encoded[i] = arr[i] XOR arr[i + 1].
// For example, if arr = [1,0,2,1], then encoded = [1,2,3].
// You are given the encoded array. You are also given an integer first, that is the first element
// of arr, i.e. arr[0]. Return the original array arr. It can be proved that the answer exists and
// is unique.
public class DecodeXOR {
    // Bit Manipulation
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 40.1 MB(76.82%) for 76 tests
    public int[] decode(int[] encoded, int first) {
        int n = encoded.length;
        int[] res = new int[n + 1];
        res[0] = first;
        for (int i = 0; i < n; i++) {
            res[i + 1] = res[i] ^ encoded[i];
        }
        return res;
    }

    private void test(int[] encoded, int first, int[] expected) {
        assertArrayEquals(expected, decode(encoded, first));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3}, 1, new int[] {1, 0, 2, 1});
        test(new int[] {6, 2, 7, 3}, 4, new int[] {4, 2, 0, 7, 4});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
