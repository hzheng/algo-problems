import java.util.*;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

// LC409: https://leetcode.com/problems/longest-palindrome/
//
// Given a string which consists of lowercase or uppercase letters, find the
// length of the longest palindromes that can be built with those letters.
// Note:
// Assume the length of given string will not exceed 1,010.
public class LongestPalindrome {
    // beats N/A(13 ms for 95 tests)
    public int longestPalindrome(String s) {
        int[] counts = new int[52];
        for (char c : s.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                counts[c - 'A']++;
            } else {
                counts[c - 'a' + 26]++;
            }
        }
        int res = 0;
        boolean hasOdd = false;
        for (int c : counts) {
            if ((c & 1) == 0) {
                res += c;
            } else {
                if (!hasOdd) {
                    hasOdd = true;
                    res++;
                }
                res += c & ~1;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 37.3 MB(86.13%) for 95 tests
    public int longestPalindrome2(String s) {
        boolean[] set = new boolean[128];
        int len = 0;
        for (char c : s.toCharArray()) {
            set[c] = !set[c];
            if (!set[c]) {
                len += 2;
            }
        }
        return (len < s.length()) ? len + 1 : len;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(65.05%), 39 MB(26.27%) for 95 tests
    public int longestPalindrome3(String s) {
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) {
            if (!set.add(c)) {
                set.remove(c);
            }
        }
        return s.length() - Math.max(set.size() - 1, 0);
    }

    void test(String s, int expected) {
        assertEquals(expected, longestPalindrome(s));
        assertEquals(expected, longestPalindrome2(s));
        assertEquals(expected, longestPalindrome3(s));
    }

    @Test public void test1() {
        test("aaaAaaaa", 7);
        test("abccccdd", 7);
        test("ccc", 3);
        test("Acc", 3);
        test("abc", 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
