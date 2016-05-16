import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string containing just the characters '(' and ')', find the length
// of the longest valid (well-formed) parentheses substring.
public class LongestParentheses {
    // beats 3.59%
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

    void test(String s, int expected) {
        assertEquals(expected, longestValidParentheses(s));
    }

    @Test
    public void test1() {
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
