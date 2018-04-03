import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// LC796: https://leetcode.com/problems/rotate-string/
//
// We are given two strings, A and B. A shift on A consists of taking string A
// and moving the leftmost character to the rightmost position. For example, if
// A = 'abcde', then it will be 'bcdea' after one shift on A. Return True if
// and only if A can become B after some number of shifts on A.
public class RotateString {
    // Brute Force
    // beats %(4 ms for 44 tests)
    // time complexity: O(N ^ 2), space complexity: O(1)
    public boolean rotateString(String A, String B) {
        int n = A.length();
        if (B.length() != n) return false;
        if (n == 0) return true;

        char[] a = A.toCharArray();
        char[] b = B.toCharArray();
        outer : for (int shift = 0; shift < n; shift++) {
            for (int i = 0; i < n; i++) {
                if (b[i] != a[(i + shift) % n]) continue outer;
            }
            return true;
        }
        return false;
    }

    // beats %(4 ms for 44 tests)
    // time complexity: O(N ^ 2), space complexity: O(1)
    public boolean rotateString2(String A, String B) {
        return A.length() == B.length() && (A + A).contains(B);
    }

    void test(String A, String B, boolean expected) {
        assertEquals(expected, rotateString(A, B));
        assertEquals(expected, rotateString2(A, B));
    }

    @Test
    public void test() {
        test("", "", true);
        test("abcde", "cdeab", true);
        test("abcdef", "defabc", true);
        test("abcde", "abced", false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
