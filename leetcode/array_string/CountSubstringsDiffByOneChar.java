import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1638: https://leetcode.com/problems/count-substrings-that-differ-by-one-character/
//
// Given two strings s and t, find the number of ways you can choose a non-empty substring of s and
// replace a single character by a different character such that the resulting substring is a
// substring of t. In other words, find the number of substrings in s that differ from some
// substring in t by exactly one character.
// For example, the underlined substrings in "computer" and "computation" only differ by the
// 'e'/'a', so this is a valid way.
// Return the number of substrings that satisfy the condition above.
// A substring is a contiguous sequence of characters within a string.
//
// Constraints:
// 1 <= s.length, t.length <= 100
// s and t consist of lowercase English letters only.
public class CountSubstringsDiffByOneChar {
    // Hash Table
    // time complexity: O(S^3), space complexity: O(T^2)
    // 2125 ms(5.21%), 42 MB(5.67%) for 63 tests
    public int countSubstrings(String s, String t) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0, n = t.length(); i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                String substr = t.substring(i, j);
                map.put(substr, map.getOrDefault(substr, 0) + 1);
            }
        }
        int res = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                char[] cs = s.substring(i, j).toCharArray();
                for (int k = 0; k < cs.length; k++) {
                    char self = cs[k];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == self) { continue; }

                        cs[k] = c;
                        res += map.getOrDefault(String.valueOf(cs), 0);
                    }
                    cs[k] = self;
                }
            }
        }
        return res;
    }

    // Trie
    // time complexity: O(S^3), space complexity: O(T^2)
    // 211 ms(6.57%), 39.9 MB(14.51%) for 63 tests
    public int countSubstrings2(String s, String t) {
        Trie trie = new Trie();
        for (int i = 0, n = t.length(); i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                trie.insert(t, i, j);
            }
        }
        int res = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                char[] cs = s.substring(i, j).toCharArray();
                for (int k = 0; k < cs.length; k++) {
                    char original = cs[k];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == original) { continue; }

                        cs[k] = c;
                        res += trie.search(cs);
                    }
                    cs[k] = original;
                }
            }
        }
        return res;
    }

    private static class Trie {
        private static class TrieNode {
            int count;
            TrieNode[] children = new TrieNode[26];
        }

        private final TrieNode root = new TrieNode();

        public void insert(String s, int start, int end) {
            TrieNode cur = root;
            for (int i = start; i < end; i++) {
                int index = s.charAt(i) - 'a';
                if (cur.children[index] == null) {
                    cur.children[index] = new TrieNode();
                }
                cur = cur.children[index];
            }
            cur.count++;
        }

        public int search(char[] str) {
            TrieNode cur = root;
            for (char c : str) {
                int index = c - 'a';
                cur = cur.children[index];
                if (cur == null) { return 0; }
            }
            return cur.count;
        }
    }

    // time complexity: O(S*T*(S+T)), space complexity: O(1)
    // 3 ms(84.43%), 36.6 MB(97.96%) for 63 tests
    public int countSubstrings3(String s, String t) {
        int res = 0;
        for (int i = 0, m = s.length(), n = t.length(); i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int p = i, q = j, diff = 0; p < m && q < n; p++, q++) {
                    if (s.charAt(p) != t.charAt(q) && ++diff > 1) { break; }

                    res += diff;
                }
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(S*T), space complexity: O(S*T)
    // 2 ms(87.94%), 39 MB(34.21%) for 63 tests
    public int countSubstrings4(String s, String t) {
        int res = 0;
        char[] cs1 = s.toCharArray();
        char[] cs2 = t.toCharArray();
        int m = cs1.length;
        int n = cs2.length;
        int[][] left = new int[m + 1][n + 1];
        int[][] right = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (cs1[i - 1] == cs2[j - 1]) {
                    left[i][j] = left[i - 1][j - 1] + 1;
                }
            }
        }
        for (int i = m; i > 0; i--) {
            for (int j = n; j > 0; j--) {
                if (cs1[i - 1] == cs2[j - 1]) {
                    right[i - 1][j - 1] = right[i][j] + 1;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (cs1[i] != cs2[j]) {
                    res += (left[i][j] + 1) * (right[i + 1][j + 1] + 1);
                }
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(S*T), space complexity: O(S*T)
    // 3 ms(84.43%), 39.1 MB(34.21%) for 63 tests
    public int countSubstrings5(String s, String t) {
        int res = 0;
        char[] cs1 = s.toCharArray();
        char[] cs2 = t.toCharArray();
        int m = cs1.length;
        int n = cs2.length;
        int[][] same = new int[m + 1][n + 1];
        int[][] diff = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (cs1[i - 1] == cs2[j - 1]) {
                    same[i][j] = same[i - 1][j - 1] + 1;
                    diff[i][j] = diff[i - 1][j - 1];
                } else {
                    diff[i][j] = same[i - 1][j - 1] + 1;
                }
                res += diff[i][j];
            }
        }
        return res;
    }

    // Solution of Choice
    // time complexity: O(S*T), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(97.96%) for 63 tests
    public int countSubstrings6(String s, String t) {
        int res = 0;
        char[] cs1 = s.toCharArray();
        char[] cs2 = t.toCharArray();
        for (int i = 0; i < cs1.length; i++) {
            res += count(cs1, cs2, i, 0);
        }
        for (int j = 1; j < cs2.length; j++) {
            res += count(cs1, cs2, 0, j);
        }
        return res;
    }

    private int count(char[] s, char[] t, int i, int j) {
        int res = 0;
        for (int n = s.length, m = t.length, prevCnt = 0, curCnt = 0; i < n && j < m; i++, j++) {
            curCnt++;
            if (s[i] != t[j]) {
                prevCnt = curCnt;
                curCnt = 0;
            }
            res += prevCnt;
        }
        return res;
    }

    private void test(String s, String t, int expected) {
        assertEquals(expected, countSubstrings(s, t));
        assertEquals(expected, countSubstrings2(s, t));
        assertEquals(expected, countSubstrings3(s, t));
        assertEquals(expected, countSubstrings4(s, t));
        assertEquals(expected, countSubstrings5(s, t));
        assertEquals(expected, countSubstrings6(s, t));
    }

    @Test public void test() {
        test("aba", "baba", 6);
        test("ab", "bb", 3);
        test("a", "a", 0);
        test("abe", "bbc", 10);
        test("abcdeace", "bbcabceacbadabc", 130);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
