import org.junit.Test;

import static org.junit.Assert.*;

// LC1869: https://leetcode.com/problems/longer-contiguous-segments-of-ones-than-zeros/
//
// Given a binary string s, return true if the longest contiguous segment of 1s is strictly longer
// than the longest contiguous segment of 0s in s. Return false otherwise.
// For example, in s = "110100010" the longest contiguous segment of 1s has length 2, and the
// longest contiguous segment of 0s has length 3.
// Note that if there are no 0s, then the longest contiguous segment of 0s is considered to have
// length 0. The same applies if there are no 1s.
//
// Constraints:
// 1 <= s.length <= 100
// s[i] is either '0' or '1'.
public class CheckZeroOnes {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(87.80%), 37.3 MB(76.30%) for 137 tests
    public boolean checkZeroOnes(String s) {
        int[] max = new int[2];
        int[] cur = new int[2];
        for (int i = 0, n = s.length(); i < n; i++) {
            int index = s.charAt(i) - '0';
            if (i > 0 && s.charAt(i - 1) != s.charAt(i)) {
                cur[index] = 0;
            }
            max[index] = Math.max(max[index], ++cur[index]);
        }
        return max[1] > max[0];
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 36.9 MB(91.68%) for 137 tests
    public boolean checkZeroOnes2(String s) {
        int max0 = 0;
        int max1 = 0;
        for (int i = 0, count0 = 0, count1 = 0, n = s.length(); i < n; i++) {
            if (s.charAt(i) == '0') {
                count1 = 0;
                max0 = Math.max(max0, ++count0);
            } else {
                count0 = 0;
                max1 = Math.max(max1, ++count1);
            }
        }
        return max1 > max0;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(87.80%), 37.3 MB(76.30%) for 137 tests
    public boolean checkZeroOnes3(String s) {
        int[] count = new int[2];
        for (int i = 0, j = 0, n = s.length(); i < n; j = i) {
            for (int index = s.charAt(j) - '0'; i < n; i++) {
                if (s.charAt(i) - '0' != index) { break; }

                count[index] = Math.max(count[index], i - j + 1);
            }
        }
        return count[1] > count[0];
    }

    private void test(String s, boolean expected) {
        assertEquals(expected, checkZeroOnes(s));
        assertEquals(expected, checkZeroOnes2(s));
        assertEquals(expected, checkZeroOnes3(s));
    }

    @Test public void test1() {
        test("1101", true);
        test("111000", false);
        test("110100010", false);
        test("110100010111100", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
