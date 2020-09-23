import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1593: https://leetcode.com/problems/split-a-string-into-the-max-number-of-unique-substrings/
//
// Given a string s, return the maximum number of unique substrings that the given string can be
// split into. You can split string s into any list of non-empty substrings, where the concatenation
// of the substrings forms the original string. However, you must split the substrings such that all
// of them are unique.
// A substring is a contiguous sequence of characters within a string.
// Constraints:
// 1 <= s.length <= 16
// s contains only lower case English letters.
public class MaxUniqueSplit {
    // Bit Manipulation
    // time complexity: O(2^N), space complexity: O(N)
    // 116 ms(16.13%), 39.4 MB(96.00%) for 95 tests
    public int maxUniqueSplit(String s) {
        int res = 0;
        char[] cs = s.toCharArray();
        for (int i = (1 << (s.length() - 1)) - 1; i >= 0; i--) {
            res = Math.max(res, count(cs, i));
        }
        return res;
    }

    private int count(char[] s, int mask) {
        int n = s.length;
        Set<String> set = new HashSet<>();
        int start = 0;
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (i == n - 1 || ((mask & (1 << i))) == 0) {
                if (!set.add(String.valueOf(s, start, i - start + 1))) { return 0; }

                res++;
                start = i + 1;
            }
        }
        return res;
    }

    // Bit Manipulation
    // time complexity: O(2^N), space complexity: O(N)
    // 19 ms(97.81%), 39.5 MB(86.44%) for 95 tests
    public int maxUniqueSplit2(String s) {
        int res = -1;
        char[] cs = s.toCharArray();
        for (int i = (1 << (s.length() - 1)) - 1; i >= 0; i--) {
            res = count(cs, i, res);
        }
        return res;
    }

    private int count(char[] s, int mask, int max) {
        int n = s.length;
        Set<String> set = new HashSet<>();
        int start = 0;
        int res = 0;
        if (Integer.bitCount(mask) < max) { return max; } // optimize

        for (int i = 0; i < n; i++) {
            if (i == n - 1 || ((mask & (1 << i))) != 0) {
                if (!set.add(String.valueOf(s, start, i - start + 1))) { return max; }

                res++;
                start = i + 1;
            }
        }
        return Math.max(max, res);
    }

    // Recursive + DFS + Backtracking
    // time complexity: O(2^N), space complexity: O(N)
    // 33 ms(90.63%), 39.5 MB(89.63%) for 95 tests
    public int maxUniqueSplit3(String s) {
        return dfs(s, 0, new HashSet<>());
    }

    private int dfs(String s, int cur, Set<String> set) {
        int n = s.length();
        if (cur >= n) { return 0; }

        int res = -1;
        for (int i = cur + 1; i <= n; i++) {
            String segment = s.substring(cur, i);
            if (!set.add(segment)) { continue; }

            int next = dfs(s, i, set);
            if (next >= 0) {
                res = Math.max(res, next + 1);
            }
            set.remove(segment);
        }
        return res;
    }

    void test(String s, int expected) {
        assertEquals(expected, maxUniqueSplit(s));
        assertEquals(expected, maxUniqueSplit2(s));
        assertEquals(expected, maxUniqueSplit3(s));
    }

    @Test public void test() {
        test("wwwzfvedwfvhsww", 11);
        test("ababc", 4);
        test("ababccc", 5);
        test("aba", 2);
        test("aa", 1);
        test("acbcdbcadcbdea", 9);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
