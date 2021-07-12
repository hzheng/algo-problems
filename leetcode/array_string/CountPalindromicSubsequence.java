import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1930: https://leetcode.com/problems/unique-length-3-palindromic-subsequences/
//
// Given a string s, return the number of unique palindromes of length three that are a subsequence
// of s.
//
// Note that even if there are multiple ways to obtain the same subsequence, it is still only
// counted once.
// A palindrome is a string that reads the same forwards and backwards.
// A subsequence of a string is a new string generated from the original string with some characters
// (can be none) deleted without changing the relative order of the remaining characters.
//
// Constraints:
// 3 <= s.length <= 10^5
// s consists of only lowercase English letters.
public class CountPalindromicSubsequence {
    // Set
    // time complexity: O(N), space complexity: O(N)
    // 354 ms(50.00%), 51.4 MB(50.00%) for 70 tests
    public int countPalindromicSubsequence(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int[] first = new int[26];
        int[] last = new int[26];
        for (int i = 0; i < n; i++) {
            last[cs[i] - 'a'] = i;
        }
        for (int i = n - 1; i >= 0; i--) {
            first[cs[i] - 'a'] = i;
        }
        int res = 0;
        for (int i = 0; i < 26; i++) {
            Set<Character> set = new HashSet<>();
            for (int j = first[i] + 1; j < last[i]; j++) {
                set.add(cs[j]);
            }
            res += set.size();
        }
        return res;
    }

    // Stream
    // time complexity: O(N), space complexity: O(1)
    // 420 ms(32.82%), 51.3 MB(47.86%) for 70 tests
    public int countPalindromicSubsequence2(String s) {
        int[] first = new int[26];
        int[] last = new int[26];
        Arrays.fill(first, Integer.MAX_VALUE);
        for (int i = 0, n = s.length(); i < n; i++) {
            int index = s.charAt(i) - 'a';
            first[index] = Math.min(first[index], i);
            last[index] = i;
        }
        int res = 0;
        for (int i = 0; i < 26; i++) {
            if (first[i] < last[i]) {
                res += s.substring(first[i] + 1, last[i]).chars().distinct().count();
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 299 ms(45.15%), 48.2 MB(64.23%) for 70 tests
    public int countPalindromicSubsequence3(String s) {
        int res = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            int start = s.indexOf(c);
            if (start == -1) { continue; }

            int last = s.lastIndexOf(c);

            Set<Character> set = new HashSet<>();
            for (int i = start + 1; i < last; i++) {
                set.add(s.charAt(i));
            }
            res += set.size();
        }
        return res;
    }

    private void test(String s, int expected) {
        assertEquals(expected, countPalindromicSubsequence(s));
        assertEquals(expected, countPalindromicSubsequence2(s));
        assertEquals(expected, countPalindromicSubsequence3(s));
    }

    @Test public void test1() {
        test("aabca", 3);
        test("adc", 0);
        test("bbcbaba", 4);
        test("bbcbabahjdsklfasduiewurioweucjvkasjdfdskljasdiofueoweowidfjuowiu", 198);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
