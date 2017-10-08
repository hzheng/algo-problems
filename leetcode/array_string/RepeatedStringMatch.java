import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// LC686: https://leetcode.com/problems/repeated-string-match/description/
//
// Given two strings A and B, find the minimum number of times A has to be
// repeated such that B is a substring of it. If no such solution, return -1
// Note: The length of A and B will be between 1 and 10000.
public class RepeatedStringMatch {
    // time complexity: O(N * (N + M)), space complexity: O(N + M)
    // beats 97.19%(7 ms for 36 tests)
    public int repeatedStringMatch(String A, String B) {
        final int CHAR_COUNT = 256;
        int[] freqA = new int[CHAR_COUNT];
        int[] freqB = new int[CHAR_COUNT];
        for (char c : A.toCharArray()) {
            freqA[c]++;
        }
        for (char c : B.toCharArray()) {
            freqB[c]++;
        }
        int repeated = 0;
        for (int i = 0; i < CHAR_COUNT; i++) {
            int c1 = freqA[i];
            int c2 = freqB[i];
            if (c2 == 0) continue;
            if (c1 == 0) return -1;

            repeated = Math.max(repeated, (int)Math.ceil(c2 / (double)c1));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < repeated; i++) {
            sb.append(A);
        }
        if (sb.toString().contains(B)) return repeated;
        // or: if (sb.indexOf(B) >= 0) return repeated;

        return (sb.append(A).indexOf(B) >= 0) ? repeated + 1 : -1;
    }

    // time complexity: O(N * (N + M)), space complexity: O(N + M)
    // beats 67.86%(179 ms for 36 tests)
    public int repeatedStringMatch2(String A, String B) {
        StringBuilder sb = new StringBuilder(A);
        int repeated = 1;
        for (; sb.length() < B.length(); repeated++) {
            sb.append(A);
        }
        if (sb.indexOf(B) >= 0) return repeated;

        return (sb.append(A).indexOf(B) >= 0) ? repeated + 1 : -1;
    }

    // Rabin-Karp algorithm(Rolling Hash)
    // https://leetcode.com/articles/repeated-string-match/ (Approach #2)
    // time complexity: O(N + M), space complexity: O(1)
    // beats 92.02%(17 ms for 36 tests)
    public int repeatedStringMatch3(String A, String B) {
        final int p = 113;
        final int MOD = 1_000_000_007;
        final int lenA = A.length();
        final int lenB = B.length();
        long hashB = 0;
        long power = 1;
        for (int i = 0; i < lenB; i++, hashB %= MOD) {
            hashB += power * B.codePointAt(i);
            power = (power * p) % MOD;
        }
        long hashA = 0;
        power = 1;
        for (int i = 0; i < lenB; i++, hashA %= MOD) {
            hashA += power * A.codePointAt(i % lenA);
            power = (power * p) % MOD;
        }
        int repeated = (lenB - 1) / lenA + 1;
        if (hashA == hashB && check(0, A, B)) return repeated;

        int pInv =
            BigInteger.valueOf(p).modInverse(BigInteger.valueOf(MOD)).intValue();
        power = (power * pInv) % MOD;
        for (int i = lenB; i < (repeated + 1) * lenA; i++) {
            hashA -= A.codePointAt((i - lenB) % lenA);
            hashA *= pInv;
            hashA += power * A.codePointAt(i % lenA);
            hashA %= MOD;
            if (hashA == hashB && check(i - lenB + 1, A, B)) {
                return i < repeated * lenA ? repeated : repeated + 1;
            }
        }
        return -1;
    }

    private boolean check(int index, String A, String B) {
        for (int i = 0; i < B.length(); i++) {
            if (A.charAt((i + index) % A.length()) != B.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    // time complexity: O(N * (N + M)), space complexity: O(1)
    // beats 11.40%(485 ms for 36 tests)
    public int repeatedStringMatch4(String A, String B) {
        for (int i = 0, lenA = A.length(), lenB = B.length(); i < lenA; i++) {
            int j = 0;
            for (; j < lenB && A.charAt((i + j) % lenA) == B.charAt(j); j++) {}
            if (j == lenB) return (i + j - 1) / lenA + 1;
        }
        return -1;
    }

    // TODO: KMP algorithm

    void test(String A, String B, int expected) {
        assertEquals(expected, repeatedStringMatch(A, B));
        assertEquals(expected, repeatedStringMatch2(A, B));
        assertEquals(expected, repeatedStringMatch3(A, B));
        assertEquals(expected, repeatedStringMatch4(A, B));
    }

    @Test
    public void test() {
        test("abcd", "cdabcdab", 3);
        test("abababaaba", "aabaaba", 2);
        test("abc", "cdabcdab", -1);
        test("abcd", "abcdb", -1);
        test("abc", "aabcbabcc", -1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
