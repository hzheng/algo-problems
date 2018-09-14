import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC010: https://leetcode.com/problems/regular-expression-matching/
//
// Implement regular expression matching with support for '.' and '*'.
public class Regex {
    // beats 50.80%(45 ms)
    public boolean isMatch(String s, String p) {
        if (s == null || p == null) return false;

        if (p.isEmpty()) return s.isEmpty();

        Matcher matcher = new Matcher(p);
        return matcher.match(s);
    }

    static class Token {
        char ch;
        boolean many;
        boolean any;
        Token(char c, boolean many) {
            ch = c;
            this.many = many;
            any = (c == '.');
        }

        int[] match(String s, int start) {
            if (many && any) return new int[] {start, s.length() + 1};

            if (start >= s.length()) {
                return many ? new int[] {start, start} : null;
            }

            char c = s.charAt(start);
            if (ch != c) {
                if (many) return new int[] {start, start};
                if (any) return new int[] {start + 1, start + 1};

                return null;
            }
            // ch == c
            if (!many) return new int[] {start + 1, start + 1};
            int end = start + 1;
            for (; end < s.length(); end++) {
                if (s.charAt(end) != c) break;
            }
            return new int[] {start, end};
        }
    }

    static class Matcher {
        Token[] tokens;
        Matcher(String p) {
            List<Token> tokenList = new ArrayList<>();
            int len = p.length();
            for (int i = 0; i < len; i++) {
                char c = p.charAt(i);
                if (c != '*') {  // * is either looked ahead or just ignored
                    tokenList.add(
                        new Token(c, (i + 1 < len) && p.charAt(i + 1) == '*'));
                }
            }
            // if keep tokens as list instead of array, "beat ratio" will
            // drop to 43.24%
            tokens = tokenList.toArray(new Token[0]);
        }

        public boolean match(String s) {
            return match(s, 0, 0);
        }

        boolean match(String s, int offset, int tokenStart) {
            int len = s.length();
            if (tokenStart == tokens.length) return offset == len;

            int[] matchRange = tokens[tokenStart].match(s, offset);
            if (matchRange == null) return false;

            ++tokenStart;
            for (int i = matchRange[1]; i >= matchRange[0]; i--) {
                if (i <= len) {
                    if (match(s, i, tokenStart)) return true;
                } else if (tokenStart >= tokens.length) {
                    return true;
                }
            }
            return false;
        }
    }

    // http://articles.leetcode.com/regular-expression-matching/
    // beats 2.28%(333 ms)
    public boolean isMatch2(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        char c = p.charAt(0);
        if (p.length() == 1 || p.charAt(1) != '*') {
            if (s.length() == 0 || c != '.' && c != s.charAt(0)) return false;
            return isMatch2(s.substring(1), p.substring(1));
        }

        // p.charAt(1) == '*'
        while (s.length() > 0 && (c == '.' || c == s.charAt(0))) {
            if (isMatch2(s, p.substring(2))) return true;

            s = s.substring(1);
        }
        return isMatch2(s, p.substring(2));
    }

    // Recursion
    // beats 7.52%(130 ms for 447 tests)
    public boolean isMatch3(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        if (p.length() == 1 || p.charAt(1) != '*') {
            return matchFirst(s, p) && isMatch3(s.substring(1), p.substring(1));
        }

        if (!matchFirst(s, p)) return isMatch3(s, p.substring(2));
        // fork: 1. consume the character and reuse the same pattern
        //       2. keep the character, and skip '*' pattern
        // Here is also an opportunity to use DP
        return isMatch3(s.substring(1), p) || isMatch3(s, p.substring(2));
    }

    private boolean matchFirst(String s, String p) {
        return !s.isEmpty() && (p.charAt(0) == '.' || p.charAt(0) == s.charAt(0));
    }

    // Recursion + Dynamic Programming(Top-Down)
    // beats 58.51%(20 ms for 447 tests)
    public boolean isMatch3_2(String s, String p) {
        return isMatch(s, p, new HashMap<>());
    }

    private boolean isMatch(String s, String p, Map<String, Boolean> memo) {
        if (p.isEmpty()) return s.isEmpty();

        String key = s + "|" + p;
        if (memo.containsKey(key)) return memo.get(key);

        boolean res = false;
        if (p.length() == 1 || p.charAt(1) != '*') {
            res = matchFirst(s, p) && isMatch(s.substring(1), p.substring(1), memo);
        } else if (!matchFirst(s, p)) {
            res = isMatch(s, p.substring(2), memo);
        } else {
            res = isMatch(s.substring(1), p, memo) || isMatch(s, p.substring(2), memo);
        }
        memo.put(key, res);
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 97.99%(3 ms)
    public boolean isMatch4(String s, String p) {
        int m = s.length();
        int n = p.length();
        int[] chars = new int[n];
        int j = 0;
        for (int i = 0; i < n; i++, j++) {
            chars[j] = p.charAt(i);
            if (i + 1 < n && p.charAt(i + 1) == '*') {
                chars[j] = -chars[j];
                i++;
            }
        }
        n = j;

        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (j = 1; j <= n && chars[j - 1] < 0; j++) {
            dp[0][j] = true;
        }
        for (int i = 1; i <= m; i++) {
            for (j = 1; j <= n; j++) {
                char c = (char)Math.abs(chars[j - 1]);
                boolean same = (c == '.' || c == s.charAt(i - 1));
                dp[i][j] = (same && dp[i - 1][j - 1]) // 1-occurance
                           || (chars[j - 1] < 0 // wildcard
                               && (dp[i][j - 1] // 0-occurence
                                   || same && dp[i - 1][j])); // multi-occurrance
            }
        }
        return dp[m][n];
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 71.62%(6 ms)
    public boolean isMatch5(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 2; i <= n; i++) {
            dp[0][i] = (p.charAt(i - 1) == '*') && dp[0][i - 2];
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char c = p.charAt(j - 1);
                if (c == '*') {
                    dp[i][j] = dp[i][j - 2]; // 0-occurence
                    if (p.charAt(j - 2) == '.' || p.charAt(j - 2) == s.charAt(i - 1)) {
                        // dp[i][j] |= dp[i][j - 1] || dp[i - 1][j];
                        dp[i][j] |= dp[i - 1][j];
                    }
                } else if (c == '.' || c == s.charAt(i - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    // Solution of Choice
    // https://discuss.leetcode.com/topic/31974/java-4ms-dp-solution-with-o-n-2-time-and-o-n-space-beats-95
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 97.99%(3 ms)
    public boolean isMatch6(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int j = 2; j <= n; j++) {
            dp[j] = dp[j - 2] && (p.charAt(j - 1) == '*');
        }

        for (int i = 1; i <= m; i++) {
            boolean last = dp[0]; // dp[i - 1][j - 1]
            dp[0] = false;
            char sc = s.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                boolean cur = dp[j]; // dp[i-1][j]
                char pc = p.charAt(j - 1);
                if (pc != '*') {
                    dp[j] = last && (sc == pc || pc == '.');
                } else {
                    char pc2 = p.charAt(j - 2);
                    dp[j] = dp[j - 2] || (dp[j] && (sc == pc2 || pc2 == '.'));
                }
                last = cur;
            }
        }
        return dp[n];
    }

    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats 58.51%(20 ms for 447 tests)
    public boolean isMatch7(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[m][n] = true;
        for (int i = m; i >= 0; i--){
            for (int j = n - 1; j >= 0; j--) {
                boolean match = (i < m && (p.charAt(j) == s.charAt(i) 
                                 || p.charAt(j) == '.'));
                if (j + 1 < n && p.charAt(j + 1) == '*') {
                    dp[i][j] = dp[i][j + 2] || match && dp[i + 1][j];
                } else {
                    dp[i][j] = match && dp[i + 1][j + 1];
                }
            }
        }
        return dp[0][0];
    }

    // TODO: backtracking

    // TODO: state machine

    void test(String s, String p, boolean expected) {
        assertEquals(expected, isMatch(s, p));
        assertEquals(expected, isMatch2(s, p));
        assertEquals(expected, isMatch3(s, p));
        assertEquals(expected, isMatch3_2(s, p));
        assertEquals(expected, isMatch4(s, p));
        assertEquals(expected, isMatch5(s, p));
        assertEquals(expected, isMatch6(s, p));
        assertEquals(expected, isMatch7(s, p));
    }

    @Test
    public void test1() {
        test("", "c*c*", true);
        test("", "", true);
        test("aa","a", false);
        test("aaa","a", false);
        test("aa","aa", true);
        test("aaa","aa", false);
        test("aa", "a*", true);
        test("aa", ".*", true);
        test("ab", ".*", true);
        test("aab", "c*a*b", true);
        test("aab", "c*a*b.", false);
        test("aabd", "c*a*b.", true);
        test("babcd", "b.*", true);
        test("babcd", "a.*", false);
        test("abcd", ".*d", true);
        test("babcd", "b.*d", true);
        test("aa", "b*a", false);
        test("aaa", "ab*a", false);
        test("", "b*", true);
        test("a", "ab*", true);
        test("a", "ab*c*", true);
        test("abcd", "a.*d", true);
        test("aasdfasdfasdfasdfas", "aasdf.*asdf.*asdf.*asdf.*s", true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
