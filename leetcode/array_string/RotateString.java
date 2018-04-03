import java.math.BigInteger;
import java.util.Arrays;

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
    // beats 86.75%(4 ms for 44 tests)
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

    // beats 86.75%(4 ms for 44 tests)
    // time complexity: O(N ^ 2), space complexity: O(1)
    public boolean rotateString2(String A, String B) {
        return A.length() == B.length() && (A + A).contains(B);
    }

    // Rolling Hash
    // beats 4.73%(9 ms for 44 tests)
    // time complexity: O(N), space complexity: O(N)
    public boolean rotateString3(String A, String B) {
        if (A.equals(B)) return true;

        int MOD = 1_000_000_007;
        int P = 113;
        int P_INV = BigInteger.valueOf(P)
                    .modInverse(BigInteger.valueOf(MOD)).intValue();
        long hashB = 0;
        long power = 1;
        for (char c : B.toCharArray()) {
            hashB = (hashB + power * c) % MOD;
            power = power * P % MOD;
        }
        long hashA = 0;
        power = 1;
        char[] cs = A.toCharArray();
        for (char c : cs) {
            hashA = (hashA + power * c) % MOD;
            power = power * P % MOD;
        }
        for (int i = 0; i < cs.length; i++) {
            hashA = (hashA + power * cs[i] - cs[i]) % MOD;
            hashA = hashA * P_INV % MOD;
            if (hashA == hashB
                && (A.substring(i + 1) +
                    A.substring(0, i + 1)).equals(B)) return true;
        }
        return false;
    }

    // KMP
    // beats 40.21%(5 ms for 44 tests)
    // time complexity: O(N), space complexity: O(N)
    public boolean rotateString4(String A, String B) {
        int n = A.length();
        if (n != B.length()) return false;
        if (n == 0) return true;

        // shifts[i] is the largest prefix of B that ends here. i.e.
        // B[:shifts[i+1]] == B[i - shifts[i+1] : i] is the largest possible 
        // prefix of B ending before B[i].
        int[] shifts = new int[n + 1];
        Arrays.fill(shifts, 1);
        for (int right = 0, left = -1; right < n; right++, left++) {
            while (left >= 0 && (B.charAt(left) != B.charAt(right))) {
                left -= shifts[left];
            }
            shifts[right + 1] = right - left;
        }
        int matchLen = 0;
        for (char c : (A + A).toCharArray()) {
            while (matchLen >= 0 && B.charAt(matchLen) != c) {
                matchLen -= shifts[matchLen];
            }
            if (++matchLen == n) return true;
        }
        return false;
    }

    // KMP
    // beats 86.75%(4 ms for 44 tests)
    // time complexity: O(N), space complexity: O(N)
    public boolean rotateString5(String A, String B) {
        int n = A.length();
        if (n != B.length()) return false;
        if (n == 0) return true;

        char[] pattern = B.toCharArray();
        int[] table = new int[n]; // failure table
        for (int i = 1, j = 0; i < n - 1; i++) {
            if (pattern[i] == pattern[j]) { // can expand
                table[i] = ++j;
            } else if (j > 0) { // try to expand the next best(largest) match
                j = table[j - 1];
                i--;
            }
        }
        char[] text = (A + A).toCharArray();
        for (int i = 0, j = 0; i < 2 * n; i++) {
            if (text[i] == pattern[j]) { // can expand
                if (++j == n) return true;
            } else if (j > 0) { // try to expand the next best(largest) match
                j = table[j - 1];
                i--;
            }
        }
        return false;
    }

    void test(String A, String B, boolean expected) {
        assertEquals(expected, rotateString(A, B));
        assertEquals(expected, rotateString2(A, B));
        assertEquals(expected, rotateString3(A, B));
        assertEquals(expected, rotateString4(A, B));
        assertEquals(expected, rotateString5(A, B));
    }

    @Test
    public void test() {
        test("", "", true);
        test("abcde", "abced", false);
        test("abcde", "bceda", false);
        test("abcde", "cdeab", true);
        test("abcdda", "cddaab", true);
        test("baaba", "abaab", true);
        test("bababa", "ababab", true);
        test("babcaba", "ababcab", true);
        test("abcdef", "defabc", true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
