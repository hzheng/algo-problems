import java.math.BigInteger;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1392: https://leetcode.com/problems/longest-happy-prefix/
//
// A string is called a happy prefix if is a non-empty prefix which is also a suffix
// (excluding itself).
// Given a string s. Return the longest happy prefix of s.
// Return an empty string if no such prefix exists.
public class LongestPrefix {
    // KMP
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(100.00%), 40.8 MB(100.00%) for 72 tests
    public String longestPrefix(String s) {
        char[] pattern = s.toCharArray();
        int len = pattern.length;
        int[] table = new int[len]; // failure table
        for (int cur = 1, prefix = 0; cur < len; cur++) {
            if (pattern[cur] == pattern[prefix]) { // matched and expanding
                table[cur] = ++prefix;
            } else if (prefix > 0) { // mismatched and try to expand the next best match
                prefix = table[prefix - 1];
                cur--;
            }
        }
        return s.substring(0, table[len - 1]);
    }

    // Incremental Hash
    // time complexity: O(N), space complexity: O(1)
    // 35 ms(29.31% if not check collision), 40.8 MB(100.00%) for 72 tests
    public String longestPrefix2(String s) {
        long MOD = (long)1e9 + 7;
        long lHash = 0;
        long rHash = 0;
        long power = 1;
        int k = 0;
        for (int i = 0, n = s.length(); i < n - 1; i++) {
            lHash = (lHash * 128 + s.charAt(i)) % MOD;
            rHash = (rHash + power * s.charAt(n - i - 1)) % MOD;
            if ((lHash == rHash) && (s.substring(0, i + 1).equals(s.substring(n - i - 1)))) {
                k = i + 1;
            }
            power = power * 128 % MOD;
        }
        return s.substring(0, k);
    }

    // Decremental Hash
    // time complexity: O(N), space complexity: O(1)
    // 39 ms(17.73%), 40.2 MB(100.00%) for 72 tests
    String longestPrefix3(String s) {
        long MOD = (long)1e9 + 7;
        long INV_MOD = BigInteger.valueOf(26).modInverse(BigInteger.valueOf(MOD)).longValue();
        long power = 1;
        char[] cs = s.toCharArray();
        int n = cs.length;
        long hash = 0;
        for (int i = 0; i < n; i++) {
            hash = (hash * 26 + s.charAt(i) - 'a') % MOD;
            power = power * 26 % MOD;
        }
        long lHash = hash;
        long rHash = hash;
        for (int i = 0, j = n - 1; j > 0; i++, j--) {
            power = power * INV_MOD % MOD;
            lHash = (lHash - (cs[j] - 'a')) * INV_MOD % MOD;
            rHash = (MOD + rHash - power * (cs[i] - 'a') % MOD) % MOD;
            if (lHash == rHash) {
                String prefix = s.substring(0, j);
                String postfix = s.substring(i + 1);
                if (prefix.equals(postfix)) { return prefix; }
            }
        }
        return "";
    }

    void test(String s, String expected) {
        assertEquals(expected, longestPrefix(s));
        assertEquals(expected, longestPrefix2(s));
        assertEquals(expected, longestPrefix3(s));
    }

    @Test public void test() {
        test("level", "l");
        test("ababab", "abab");
        test("leetcodeleet", "leet");
        test("a", "");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
