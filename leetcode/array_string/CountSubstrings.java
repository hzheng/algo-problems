import org.junit.Test;
import static org.junit.Assert.*;

// LC647: https://leetcode.com/problems/palindromic-substrings/
//
// Given a string, your task is to count how many palindromic substrings in this
// string. The substrings with different start indexes or end indexes are
// counted as different substrings even they consist of same characters.
// Note: The input string length won't exceed 1000.
public class CountSubstrings {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 54.40%(18 ms for 130 tests)
    public int countSubstrings(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int res = 0;
        boolean[][] dp = new boolean[n + 1][n + 1];
        for (int i = 0; i < n; i++) {
            dp[0][i] = dp[1][i] = true;
        }
        for (int len = 2; len <= n; len++) {
            for (int start = 0; start + len - 1 < n; start++) {
                if (dp[len - 2][start + 1] &&
                    (cs[start] == cs[start + len - 1])) {
                    dp[len][start] = true;
                    res++;
                }
            }
        }
        return res + n;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 53.11%(19 ms for 130 tests)
    public int countSubstrings_2(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int res = 0;
        boolean[][] dp = new boolean[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (cs[i] == cs[j] && (j - i < 3 || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    res++;
                }
            }
        }
        return res;
    }

    // Expand Around Center
    // time complexity: O(N ^ 2), space complexity: O(1)
    // beats 57.09%(16 ms for 130 tests)
    public int countSubstrings2(String s) {
        char[] cs = s.toCharArray();
        int res = 0;
        for (int center = 0, n = cs.length; center <= 2 * n - 1; center++) {
            for (int left = center / 2, right = left + center % 2;
                 left >= 0 && right < n && cs[left] == cs[right];
                 left--, right++) {
                res++;
            }
        }
        return res;
    }

    // Manacher's Algorithm
    // time complexity: O(N), space complexity: O(N)
    // beats 64.33%(14 ms for 130 tests)
    public int countSubstrings3(String s) {
        char[] S = new char[2 * s.length() + 3];
        S[0] = '^';
        S[1] = '#';
        S[S.length - 1] = '$';
        int j = 2;
        for (char c : s.toCharArray()) {
            S[j++] = c;
            S[j++] = '#';
        }
        int[] R = new int[S.length]; // max radius
        // loop invariant: the palindrome with the largest right-most boundary 
        // with center < i, centered at 'center' with right-boundary 'right'
        for (int i = 1, center = 0, right = 0; i < R.length - 1; i++) {
            if (right > i) { // take advantage of reflection
                R[i] = Math.min(right - i, R[2 * center - i]);
            }
            for (; S[i + R[i] + 1] == S[i - R[i] - 1]; R[i]++) {} // expand
            if (i + R[i] > right) {
                center = i;
                right = i + R[i];
            }
        }
        int res = 0;
        for (int r : R) {
            res += (r + 1) / 2;
        }
        return res;
    }

    void test(String s, int expected) {
        assertEquals(expected, countSubstrings(s));
        assertEquals(expected, countSubstrings_2(s));
        assertEquals(expected, countSubstrings2(s));
        assertEquals(expected, countSubstrings3(s));
    }

    @Test
    public void test() {
        test("abc", 3);
        // test("aaa", 6);
        // test("aabacac", 11);
        // test("bcacaaaabacacb", 25);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().
        getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
