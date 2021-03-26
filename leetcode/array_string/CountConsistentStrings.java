import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1684: https://leetcode.com/problems/count-the-number-of-consistent-strings/
//
// You are given a string allowed consisting of distinct characters and an array of strings words. A
// string is consistent if all characters in the string appear in the string allowed.
// Return the number of consistent strings in the array words.
//
// Constraints:
// 1 <= words.length <= 10^4
// 1 <= allowed.length <= 26
// 1 <= words[i].length <= 10
// The characters in allowed are distinct.
// words[i] and allowed contain only lowercase English letters.
public class CountConsistentStrings {
    // time complexity: O(N*W), space complexity: O(N)
    // 5 ms(98.35%), 39.8 MB(59.58%) for 74 tests
    public int countConsistentStrings(String allowed, String[] words) {
        boolean[] allowList = new boolean[26];
        for (char c : allowed.toCharArray()) {
            allowList[c - 'a'] = true;
        }
        int res = 0;
        outer:
        for (String word : words) {
            for (char c : word.toCharArray()) {
                if (!allowList[c - 'a']) { continue outer; }
            }
            res++;
        }
        return res;
    }

    // time complexity: O(N*W), space complexity: O(N)
    // 5 ms(98.35%), 39.6 MB(68.34%) for 74 tests
    public int countConsistentStrings2(String allowed, String[] words) {
        int allowList = 0;
        for (int i = 0; i < allowed.length(); ++i) {
            int shift = allowed.charAt(i) - 'a';
            allowList |= 1 << shift;
        }
        int res = 0;
        outer:
        for (String word : words) {
            for (char c : word.toCharArray()) {
                if ((allowList & (1 << (c - 'a'))) == 0) { continue outer; }
            }
            res++;
        }
        return res;
    }

    private void test(String allowed, String[] words, int expected) {
        assertEquals(expected, countConsistentStrings(allowed, words));
        assertEquals(expected, countConsistentStrings2(allowed, words));
    }

    @Test public void test() {
        test("ab", new String[] {"ad", "bd", "aaab", "baa", "badab"}, 2);
        test("abc", new String[] {"a", "b", "c", "ab", "ac", "bc", "abc"}, 7);
        test("cad", new String[] {"cc", "acd", "b", "ba", "bac", "bad", "ac", "d"}, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
