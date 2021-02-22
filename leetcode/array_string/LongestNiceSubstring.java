import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1763: https://leetcode.com/problems/longest-nice-substring/
//
// A string s is nice if, for every letter of the alphabet that s contains, it appears both in
// uppercase and lowercase. For example, "abABB" is nice because 'A' and 'a' appear, and 'B' and 'b'
// appear. However, "abA" is not because 'b' appears, but 'B' does not. Given a string s, return the
// longest substring of s that is nice. If there are multiple, return the substring of the earliest
// occurrence. If there are none, return an empty string.
//
// Constraints:
// 1 <= s.length <= 100
// s consists of uppercase and lowercase English letters.
public class LongestNiceSubstring {
    // Brute Force
    // time complexity: O(N^2), space complexity: O(N)
    // 13 ms(66.67%), 39.6 MB(33.33%) for 73 tests
    public String longestNiceSubstring(String s) {
        char[] cs = s.toCharArray();
        int maxStart = 0;
        int maxLen = 0;
        for (int i = 0, n = cs.length; i < n; i++) {
            for (int len = n - i; len >= maxLen; len--) {
                if (allNice(cs, i, i + len) < 0 && (len > maxLen || i < maxStart)) {
                    maxLen = len;
                    maxStart = i;
                    break;
                }
            }
        }
        return String.valueOf(cs, maxStart, maxLen);
    }

    // Queue
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 39 MB(33.33%) for 73 tests
    public String longestNiceSubstring2(String s) {
        char[] cs = s.toCharArray();
        Queue<int[]> queue = new LinkedList<>();
        int maxStart = 0;
        int maxLen = 0;
        for (queue.offer(new int[] {0, cs.length}); !queue.isEmpty(); ) {
            int[] range = queue.poll();
            int start = range[0];
            int end = range[1];
            if (end - start < maxLen) { continue; }

            int breakPos = allNice(cs, start, end);
            if (breakPos >= 0) {
                queue.offer(new int[] {start, breakPos});
                queue.offer(new int[] {breakPos + 1, end});
            } else if (end - start > maxLen || start < maxStart) {
                maxStart = start;
                maxLen = end - start;
            }
        }
        return String.valueOf(cs, maxStart, maxLen);
    }

    private int allNice(char[] s, int start, int end) {
        int[] nice = new int[26];
        for (int i = start; i < end; i++) {
            char c = s[i];
            if (c >= 'a') {
                nice[c - 'a'] |= 1;
            } else {
                nice[c - 'A'] |= 2;
            }
        }
        for (int i = start; i < end; i++) {
            if (nice[s[i] - (s[i] >= 'a' ? 'a' : 'A')] != 3) { return i; }
        }
        return -1;
    }

    // Recursion + Divide & Conquer
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100%), 39 MB(33.33%) for 73 tests
    public String longestNiceSubstring3(String s) {
        char[] cs = s.toCharArray();
        int[] res = longestNiceSubstring(cs, 0, cs.length);
        return String.valueOf(cs, res[0], res[1]);
    }

    public int[] longestNiceSubstring(char[] s, int start, int end) {
        if (start >= end) { return new int[2]; }

        int breakPos = allNice(s, start, end);
        if (breakPos < 0) { return new int[] {start, end - start}; }

        int[] left = longestNiceSubstring(s, start, breakPos);
        int[] right = longestNiceSubstring(s, breakPos + 1, end);
        return (left[1] >= right[1]) ? left : right;
    }

    private void test(String s, String expected) {
        assertEquals(expected, longestNiceSubstring(s));
        assertEquals(expected, longestNiceSubstring2(s));
        assertEquals(expected, longestNiceSubstring3(s));
    }

    @Test public void test() {
        test("YazaAay", "aAa");
        test("Bb", "Bb");
        test("c", "");
        test("dDzeE", "dD");
        test("VaiOlVBrVyoGqygbrELjHNyRAVmHDhtSsvLMCIeFStnoyTygcrMduvyYfJQ", "Ss");
        test("qlERNCNVvWLOrrkAaDcXnlaDQxNEneRXQMKnrNN", "NEne");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
