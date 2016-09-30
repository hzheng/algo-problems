import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC097: https://leetcode.com/problems/interleaving-string/
//
// Given s1, s2, s3, find whether s3 is formed by the interleaving of s1 and s2.
public class InterleaveStr {
    // Recursion
    // Time Limit Exceeded
    public boolean isInterleave(String s1, String s2, String s3) {
        int i1 = 0;
        int i2 = 0;
        int len1 = s1.length();
        int len2 = s2.length();
        int len3 = s3.length();
        for (int i3 = 0; i3 < len3; i3++) {
            char c1 = (i1 < len1) ? s1.charAt(i1) : '\0';
            char c2 = (i2 < len2) ? s2.charAt(i2) : '\0';
            char c3 = s3.charAt(i3);

            if (c3 != c1 && c3 != c2) return false;

            if (c3 == c1 && c3 == c2) {
                return isInterleave(s1.substring(1), s2, s3.substring(1))
                       || isInterleave(s1, s2.substring(1), s3.substring(1));
            }

            if (c3 == c1 && c3 != c2) {
                i1++;
            } else {
                i2++;
            }
        }
        return i1 == len1 && i2 == len2;
    }

    // Stack
    // Time Limit Exceeded
    public boolean isInterleave2(String s1, String s2, String s3) {
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int len1 = s1.length();
        int len2 = s2.length();
        int len3 = s3.length();
        Stack<Integer> marks = new Stack<>();

        for (;; i3++) {
            if (i3 == len3 && i1 == len1 && i2 == len2) return true;

            if (i3 < len3 && i1 < len1 && i2 < len2) {
                char c1 = s1.charAt(i1);
                char c2 = s2.charAt(i2);
                char c3 = s3.charAt(i3);
                if (i3 < len3 && c3 == c1 && c3 != c2) {
                    if (++i1 < len1) continue;

                    if (s3.substring(i3 + 1).equals(s2.substring(i2))) return true;
                } else if (i3 < len3 && c3 != c1 && c3 == c2) {
                    if (++i2 < len2) continue;

                    if (s3.substring(i3 + 1).equals(s1.substring(i1))) return true;
                } else if (i3 < len3 && c3 == c1 && c3 == c2) {
                    marks.push(i2 + 1);
                    marks.push(i1++);
                    continue;
                }
            }
            if (marks.isEmpty()) break;

            i1 = marks.pop();
            i2 = marks.pop();
            i3 = i1 + i2 - 1;
        }

        if (i1 == len1) {
            return s3.substring(i3).equals(s2.substring(i2));
        }
        if (i2 == len2) {
            return s3.substring(i3).equals(s1.substring(i1));
        }
        return false;
    }

    // Dynamic Programming(2D, bottom-up)
    // beats 39.83%(9 ms)
    public boolean isInterleave3(String s1, String s2, String s3) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 + len2 != s3.length()) return false;

        boolean[][] dp = new boolean[len1 + 1][len2 + 1];
        dp[0][0] = true;
        for (int i2 = 0; i2 < len2; i2++) {
            dp[0][i2 + 1] = s3.charAt(i2) == s2.charAt(i2);
            if (!dp[0][i2 + 1]) break;
        }
        for (int i1 = 0; i1 < len1; i1++) {
            dp[i1 + 1][0] = dp[i1][0] && (s3.charAt(i1) == s1.charAt(i1));
            for (int i2 = 0; i2 < len2; i2++) {
                char c = s3.charAt(i1 + i2 + 1);
                if (c == s1.charAt(i1)) {
                    dp[i1 + 1][i2 + 1] = dp[i1][i2 + 1];
                }
                if (c == s2.charAt(i2)) {
                    dp[i1 + 1][i2 + 1] |= dp[i1 + 1][i2];
                }
            }
        }
        return dp[len1][len2];
    }

    // Solution of Choice
    // Dynamic Programming(1D, bottom-up)
    // beats 81.84%(3 ms)
    public boolean isInterleave4(String s1, String s2, String s3) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 + len2 != s3.length()) return false;

        boolean[] dp = new boolean[len2 + 1];
        dp[0] = true;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        char[] chars3 = s3.toCharArray();
        // for (int i2 = 0; i2 < len2; i2++) { // first row
        //     dp[i2 + 1] = dp[i2] && (s3.charAt(i2) == s2.charAt(i2));
        // }
        for (int i = 0; i < len2 && chars3[i] == chars2[i]; i++) {
            dp[i + 1] = true;
        }
        for (int i1 = 0; i1 < len1; i1++) {
            char c1 = chars1[i1];
            dp[0] &= (chars3[i1] == c1); // first column
            for (int i2 = 0; i2 < len2; i2++) {
                dp[i2 + 1] = (dp[i2] && chars3[i1 + i2 + 1] == chars2[i2])
                             || (dp[i2 + 1] && chars3[i1 + i2 + 1] == c1);
            }
        }
        return dp[len2];
    }

    // DFS or Dynamic Programming(top-down)
    // beats 93.95%(1 ms)
    public boolean isInterleave5(String s1, String s2, String s3) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 + len2 != s3.length()) return false;

        return dfs(s1.toCharArray(), s2.toCharArray(), s3.toCharArray(),
                   0, 0, new byte[len1 + 1][len2 + 1]);
    }

    private boolean dfs(char[] c1, char[] c2, char[] c3, int i, int j,
                        byte[][] memo) {
        if (memo[i][j] != 0) return memo[i][j] > 0;
        if (i + j == c3.length) return true;

        boolean res = i < c1.length && c1[i] == c3[i + j] && dfs(c1, c2, c3, i + 1, j, memo) ||
                      j < c2.length && c2[j] == c3[i + j] && dfs(c1, c2, c3, i, j + 1, memo);
        memo[i][j] = (byte)(res ? 1 : -1);
        return res;
    }

    // Solution of Choice
    // BFS + Queue
    // beats 30.63%(10 ms)
    public boolean isInterleave6(String s1, String s2, String s3) {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 + len2 != s3.length()) return false;

        Queue<Long> queue = new LinkedList<>();
        Set<Long> visited = new HashSet<>();
        queue.offer(0L);
        while (!queue.isEmpty()) {
            long pos = queue.poll();
            int x = (int)pos;
            int y = (int)(pos >> 32);
            if (x == len1 && y == len2) return true;

            if (visited.contains(pos)) continue;

            visited.add(pos);
            if (x < len1 && s1.charAt(x) == s3.charAt(x + y)) { // down
                queue.offer((long)(x + 1) + ((long)y << 32));
            }
            if (y < len2 && s2.charAt(y) == s3.charAt(x + y)) { // right
                queue.offer((long)(x) + ((long)(y + 1) << 32));
            }
        }
        return false;
    }

    void test(String s1, String s2, String s3, boolean expected) {
        assertEquals(expected, isInterleave(s1, s2, s3));
        assertEquals(expected, isInterleave2(s1, s2, s3));
        assertEquals(expected, isInterleave3(s1, s2, s3));
        assertEquals(expected, isInterleave4(s1, s2, s3));
        assertEquals(expected, isInterleave5(s1, s2, s3));
        assertEquals(expected, isInterleave6(s1, s2, s3));
    }

    @Test
    public void test1() {
        test("", "", "", true);
        test("", "b", "b", true);
        test("", "b", "a", false);
        test("b", "", "b", true);
        test("a", "b", "a", false);
        test("ab", "bc", "bbac", false);
        test("bc", "bb", "bbcb", true);
        test("bc", "bbca", "bbcbca", true);
        test("ab", "bca", "abbac", false);
        test("ab", "ca", "abac", false);
        test("ab", "cd", "abdc", false);
        test("db", "b", "cbb", false);
        test("aa", "aae", "aaeaa", true);
        test("aa", "aae", "aaeaa", true);
        test("aabc", "a", "aabca", true);
        test("aa", "aadaae", "aadaaeaa", true);
        test("aabc", "abad", "aabcabad", true);
        test("bcc", "bbca", "bbcbcac", true);
        test("aabcc", "dbbca", "aadbbcbcac", true);
        test("aabcc", "dbbca", "aadbbbcacc", true);
        test("aabcc", "dbbca", "aadbbbaccc", false);
        test("aacaac", "aacaaeaac", "aacaaeaaeaacaac", false);
        test("aabaac", "aadaaeaaf", "aadaaeaabaafaac", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("InterleaveStr");
    }
}
