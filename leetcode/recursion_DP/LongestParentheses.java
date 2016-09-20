import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC032: https://leetcode.com/problems/longest-valid-parentheses/
//
// Given a string containing just the characters '(' and ')', find the length
// of the longest valid (well-formed) parentheses substring.
public class LongestParentheses {
    // Stack
    // beats 14.98%(15 ms)
    public int longestValidParentheses(String s) {
        Stack<Integer> leftParens = new Stack<>();
        Stack<int[]> pairs = new Stack<>();
        int maxLen = 0;
        for (int cur = 0; cur < s.length(); cur++) {
            switch (s.charAt(cur)) {
            case '(':
                leftParens.push(cur);
                break;
            case ')':
                if (leftParens.isEmpty()) break;

                int matchedLeftParen = leftParens.pop();
                while (!pairs.isEmpty()) {
                    int[] pair = pairs.peek();
                    if (pair[1] + 1 < matchedLeftParen) break;

                    if (pair[1] + 1 == matchedLeftParen) {  // merge
                        matchedLeftParen = pair[0];
                        pairs.pop();
                        break;
                    }

                    matchedLeftParen = pair[0] - 1;
                    pairs.pop();
                }
                pairs.push(new int[] {matchedLeftParen, cur});
                maxLen = Math.max(maxLen, cur - matchedLeftParen + 1);
                break;
            }
        }

        return maxLen;
    }

    // Solution of Choice
    // Stack
    // http://www.geeksforgeeks.org/length-of-the-longest-valid-substring/
    // beats 48.06%(10 ms)
    public int longestValidParentheses2(String s) {
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        int maxLen = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.empty()) {
                    stack.push(i); // current index as base for next valid pairs
                } else {
                    maxLen = Math.max(maxLen, i - stack.peek());
                }
            }
        }
        return maxLen;
    }

    public int longestValidParenthesesBug(String s) {
        int extraLeftParen = 0;
        int maxLen = 0;
        int start = 0;
        for (int cur = 0; cur < s.length(); cur++) {
            switch (s.charAt(cur)) {
            case '(':
                extraLeftParen++;
                break;
            case ')':
                if (extraLeftParen > 0) {
                    extraLeftParen--;
                } else {
                    if (start < cur) {
                        maxLen = Math.max(maxLen, cur - start);
                    }
                    start = cur + 1;
                }
                break;
            }
        }
        return Math.max(maxLen, s.length() - start - extraLeftParen);
    }

    // Solution of Choice(2)
    // Dynamic Programming
    // beats 96.13%(3 ms)
    public int longestValidParentheses3(String s) {
        int len = s.length();
        int[] dp = new int[len];
        int maxLen = 0;
        int extraLeftParens = 0;
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == '(') {
                extraLeftParens++;
            } else if (extraLeftParens > 0) {
                extraLeftParens--;
                dp[i] = dp[i - 1] + 2;
                if (i > dp[i]) { // merge previous matches
                    dp[i] += dp[i - dp[i]];
                }
                maxLen = Math.max(maxLen, dp[i]);
            }
        }
        return maxLen;
    }

    // Dynamic Programming
    // beats 85.52%(4 ms)
    public int longestValidParentheses4(String s) {
        int len = s.length();
        int[] dp = new int[len + 1];
        int maxLen = 0;
        for (int i = 1; i < len; i++) {
            if (s.charAt(i) == ')') {
                int left = i - dp[i] - 1;
                if (left >= 0 && s.charAt(left) == '(') {
                    dp[i + 1] = dp[i] + 2 + dp[left];
                    maxLen = Math.max(maxLen, dp[i + 1]);
                }
            }
        }
        return maxLen;
    }

    void test(String s, int expected) {
        assertEquals(expected, longestValidParentheses(s));
        assertEquals(expected, longestValidParentheses2(s));
        assertEquals(expected, longestValidParentheses3(s));
        assertEquals(expected, longestValidParentheses4(s));
    }

    @Test
    public void test1() {
        test("((())(()", 4);
        test("(())()((()", 6);
        test("()(()", 2);
        test("()()()()", 8);
        test("(", 0);
        test("(((", 0);
        test("()", 2);
        test("(()", 2);
        test("(()())", 6);
        test(")()())", 4);
        test(")))((()())", 6);
        test(")))((()()))", 8);
        test(")))((()())))(())()", 8);
        test(")))((()())))(())()(())", 10);
        test(")))((()())))(())()(()))))))(((((())))))))", 12);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestParentheses");
    }
}
