import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1653: https://leetcode.com/problems/minimum-deletions-to-make-string-balanced/
//
// You are given a string s consisting only of characters 'a' and 'b'.
// You can delete any number of characters in s to make s balanced. s is balanced if there is no
// pair of indices (i,j) such that i < j and s[i] = 'b' and s[j]= 'a'.
// Return the minimum number of deletions needed to make s balanced.
//
// Constraints:
// 1 <= s.length <= 10^5
// s[i] is 'a' or 'b'.
public class MinimumDeletions {
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 43 ms(30.82%), 39.7 MB(56.00%) for 157 tests
    public int minimumDeletions(String s) {
        int n = s.length();
        int[] aCounts = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            aCounts[i] = aCounts[i + 1] + (s.charAt(i) == 'a' ? 1 : 0);
        }
        for (int i = 0, bCount = 0, res = n; ; i++) {
            res = Math.min(res, bCount + aCounts[i]);
            if (i == n) { return res; }

            bCount += (s.charAt(i) == 'b' ? 1 : 0);
        }
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 24 ms(75.76%), 39.8 MB(43.76%) for 157 tests
    public int minimumDeletions2(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        for (int i = 0, bCount = 0; i < n; i++) {
            if (s.charAt(i) == 'a') {
                dp[i + 1] = Math.min(dp[i] + 1, bCount);
            } else {
                dp[i + 1] = dp[i];
                bCount++;
            }
        }
        return dp[n];
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 71 ms(8.23%), 40 MB(31.76%) for 157 tests
    public int minimumDeletions3(String s) {
        Stack<Character> stack = new Stack<>();
        int res = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (stack.empty() || stack.peek() >= c) {
                stack.push(c);
            } else {
                stack.pop();
                res++;
            }
        }
        return res;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // 17 ms(99.53%), 39.5 MB(68.47%) for 157 tests
    public int minimumDeletions4(String s) {
        int res = 0;
        int bCount = 0;
        for (char c : s.toCharArray()) {
            if (c == 'b') {
                bCount++;
            } else if (bCount > 0) {
                bCount--;
                res++;
            }
        }
        return res;
    }

    private void test(String s, int expected) {
        assertEquals(expected, minimumDeletions(s));
        assertEquals(expected, minimumDeletions2(s));
        assertEquals(expected, minimumDeletions3(s));
        assertEquals(expected, minimumDeletions4(s));
    }

    @Test public void test() {
        test("bb", 0);
        test("aa", 0);
        test("a", 0);
        test("b", 0);
        test("aababbab", 2);
        test("bbaaaaabb", 2);
        test("aababbaabbbaaaababb", 7);
        test("aabbbbabaabbaabbbaaaababb", 10);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
