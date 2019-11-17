import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1255: https://leetcode.com/problems/maximum-score-words-formed-by-letters/
//
// Given a list of words, list of  single letters (might be repeating) and score of every character.
// Return the maximum score of any valid set of words formed by using the given letters (words[i]
// cannot be used two or more times).
// It is not necessary to use all characters in letters and each letter can only be used once. Score
// of letters 'a', 'b', 'c', ... ,'z' is given by score[0], score[1], ... , score[25] respectively.
// Constraints:
//
// 1 <= words.length <= 14
// 1 <= words[i].length <= 15
// 1 <= letters.length <= 100
// letters[i].length == 1
// score.length == 26
// 0 <= score[i] <= 10
// words[i], letters[i] contains only lower case English letters.
public class MaxScoreWords {
    // Recursion + DFS + Backtracking
    // 1 ms(99.08%), 36.3 MB(100%) for 52 tests
    public int maxScoreWords(String[] words, char[] letters, int[] score) {
        int[] avail = new int[26];
        for (char c : letters) {
            avail[c - 'a']++;
        }
        int[] res = new int[1];
        dfs(convert(words), avail, score, 0, 0, res);
        return res[0];
    }

    private int[][] convert(String[] words) {
        int n = words.length;
        int[][] res = new int[n][26];
        for (int i = 0; i < n; i++) {
            for (char c : words[i].toCharArray()) {
                res[i][c - 'a']++;
            }
        }
        return res;
    }

    private void dfs(int[][] words, int[] avail, int[] score, int index, int total, int[] res) {
        int n = words.length;
        if (index >= n) {
            res[0] = Math.max(res[0], total);
            return;
        }

        int[] cur = words[index];
        int[] copy = avail.clone();
        int v = consume(cur, avail, score);
        if (v >= 0) {
            dfs(words, avail, score, index + 1, total + v, res);
        }
        dfs(words, copy, score, index + 1, total, res);
    }

    private int consume(int[] word, int[] avail, int[] score) {
        int total = 0;
        for (int i = 0; i < 26; i++) {
            if ((avail[i] -= word[i]) < 0) { return -1; }
            total += word[i] * score[i];
        }
        return total;
    }

    // Recursion + DFS + Backtracking
    // 1 ms(99.08%), 35.8 MB(100%) for 52 tests
    public int maxScoreWords2(String[] words, char[] letters, int[] score) {
        int[] avail = new int[score.length];
        for (char c : letters) {
            avail[c - 'a']++;
        }
        return dfs(words, avail, score, 0);
    }

    private int dfs(String[] words, int[] avail, int[] score, int index) {
        int max = 0;
        for (int i = index; i < words.length; i++) {
            int total = 0;
            boolean isValid = true;
            for (char c : words[i].toCharArray()) {
                total += score[c - 'a'];
                if (--avail[c - 'a'] < 0) {
                    isValid = false;
                }
            }
            if (isValid) {
                total += dfs(words, avail, score, i + 1);
                max = Math.max(total, max);
            }
            for (char c : words[i].toCharArray()) {
                avail[c - 'a']++;
            }
        }
        return max;
    }

    // Bit Manipulation
    // 6 ms(32.34%), 36.5 MB(100%) for 52 tests
    public int maxScoreWords3(String[] words, char[] letters, int[] score) {
        final int N = 26;
        int[] freq = new int[N];
        for (char c : letters) {
            freq[c - 'a']++;
        }

        int res = 0;
        int n = words.length;
        outer:
        for (int mask = (1 << n) - 1; mask >= 0; mask--) {
            int[] count = new int[N];
            for (int i = 0; i < n; i++) {
                if ((mask << ~i) < 0) {
                    for (char c : words[i].toCharArray()) {
                        if (++count[c - 'a'] > freq[c - 'a']) { continue outer; }
                    }
                }
            }
            int totalScore = 0;
            for (int i = 0; i < N; i++) {
                totalScore += count[i] * score[i];
            }
            res = Math.max(res, totalScore);
        }
        return res;
    }

    void test(String[] words, char[] letters, int[] score, int expected) {
        assertEquals(expected, maxScoreWords(words, letters, score));
        assertEquals(expected, maxScoreWords2(words, letters, score));
        assertEquals(expected, maxScoreWords3(words, letters, score));
    }

    @Test
    public void test() {
        test(new String[]{"dog", "cat", "dad", "good"},
             new char[]{'a', 'a', 'c', 'd', 'd', 'd', 'g', 'o', 'o'},
             new int[]{1, 0, 9, 5, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0},
             23);
        test(new String[]{"xxxz", "ax", "bx", "cx"}, new char[]{'z', 'a', 'b', 'c', 'x', 'x', 'x'},
             new int[]{4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 0,
                       10}, 27);
        test(new String[]{"leetcode"}, new char[]{'l', 'e', 't', 'c', 'o', 'd'},
             new int[]{0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0,
                       0}, 0);
        test(new String[]{"aac", "ab", "cc", "aab"}, new char[]{'a', 'a', 'a', 'b', 'c', 'c'},
             new int[]{1, 5, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                       0}, 23);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
