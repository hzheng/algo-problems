import org.junit.Test;

import static org.junit.Assert.*;

// LC1745: https://leetcode.com/problems/palindrome-partitioning-iv/
//
// Given a string s, return true if it is possible to split the string s into three non-empty
// palindromic substrings. Otherwise, return false.
//
// Constraints:
// 3 <= s.length <= 2000
// s consists only of lowercase English letters.
public class PalindromePartitionIV {
    // DFS + Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 86 ms(74.88%), 58.5 MB(54.94%) for 81 tests
    public boolean checkPartitioning(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        Boolean[][] isPal = new Boolean[n][n];
        for (int i = 0; i < n - 1; i++) {
            if (!dfs(cs, 0, i, isPal)) { continue; }

            for (int j = i + 1; j < n - 1; j++) {
                if (dfs(cs, i + 1, j, isPal) && dfs(cs, j + 1, n - 1, isPal)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[] cs, int start, int end, Boolean[][] isPal) {
        Boolean v = isPal[start][end];
        if (v != null) { return v; }
        if (start >= end) { return true; }

        boolean res = (cs[start] == cs[end]) && dfs(cs, start + 1, end - 1, isPal);
        return isPal[start][end] = res;
    }

    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 86 ms(74.88%), 58.5 MB(54.94%) for 81 tests
    public boolean checkPartitioning2(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        boolean[][] isPal = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                isPal[i][j] = (cs[i] == cs[j]) && (i + 1 >= j || isPal[i + 1][j - 1]);
            }
        }
        for (int i = 1; i < n - 1; i++) {
            if (!isPal[0][i - 1]) { continue; }

            for (int j = i; j < n - 1; j++) {
                if (isPal[i][j] && isPal[j + 1][n - 1]) { return true; }
            }
        }
        return false;
    }

    private void test(String s, boolean expected) {
        assertEquals(expected, checkPartitioning(s));
        assertEquals(expected, checkPartitioning2(s));
    }

    @Test public void test() {
        test("abcbdd", true);
        test("bcbddxy", false);
        test("babccbayy", true);
        test("babccbayxy", true);
        test("bbabccbayxy", true);
        test("bbabccbaxyy", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
