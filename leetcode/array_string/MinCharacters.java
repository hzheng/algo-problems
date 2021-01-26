import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1737: https://leetcode.com/problems/change-minimum-characters-to-satisfy-one-of-three-conditions/
//
// You are given two strings a and b that consist of lowercase letters. In one operation, you can
// change any character in a or b to any lowercase letter. Your goal is to satisfy one of the
// following three conditions:
// Every letter in a is strictly less than every letter in b in the alphabet.
// Every letter in b is strictly less than every letter in a in the alphabet.
// Both a and b consist of only one distinct letter.
// Return the minimum number of operations needed to achieve your goal.
//
// Constraints:
// 1 <= a.length, b.length <= 10^5
// a and b consist only of lowercase letters.
public class MinCharacters {
    // time complexity: O(L1+L2), space complexity: O(L1+L2)
    // 80 ms(29.70%), 39.8 MB(67.55%) for 67 tests
    public int minCharacters(String a, String b) {
        char[] s1 = a.toCharArray();
        char[] s2 = b.toCharArray();
        int n = s1.length + s2.length;
        int res = n;
        for (char c = 'a'; c < 'z'; c++) {
            int change = change(s1, c, 1) + change(s2, c, 0);
            res = Math.min(res, Math.min(change, n - change));
        }
        return Math.min(res, n - mostFreq(s1) - mostFreq(s2));
    }

    private int change(char[] s, char threshold, int big) {
        int res = 0;
        for (char c : s) {
            res += (c > threshold) ? (1 - big) : big;
        }
        return res;
    }

    private int mostFreq(char[] s) {
        int[] count = new int[26];
        for (char c : s) {
            count[c - 'a']++;
        }
        int res = 0;
        for (int a : count) {
            res = Math.max(res, a);
        }
        return res;
    }

    // time complexity: O(L1+L2), space complexity: O(L1+L2)
    // 10 ms(78.37%), 52.1 MB(10.72%) for 67 tests
    public int minCharacters2(String a, String b) {
        int m = a.length();
        int n = b.length();
        int res = m + n;
        int[] bigger1 = new int[26]; // store count bigger than the index of a
        int[] bigger2 = new int[26]; // store count bigger than the index of b
        for (int i = 0; i < m; i++) {
            bigger1[a.charAt(i) - 'a']++;
        }
        for (int i = 0; i < n; i++) {
            bigger2[b.charAt(i) - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            res = Math.min(res, m + n - bigger1[i] - bigger2[i]);
            if (i > 0) {
                bigger1[i] += bigger1[i - 1];
                bigger2[i] += bigger2[i - 1];
            }
            if (i < 25) {
                res = Math.min(res, m - bigger1[i] + bigger2[i]);
                res = Math.min(res, n - bigger2[i] + bigger1[i]);
            }
        }
        return res;
    }

    private void test(String a, String b, int expected) {
        assertEquals(expected, minCharacters(a, b));
        assertEquals(expected, minCharacters2(a, b));
    }

    @Test public void test() {
        test("aba", "caa", 2);
        test("dabadd", "cda", 3);
        test("dababcdeabaddwaabccbbdd", "cdacdadfdepadfadf", 14);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
