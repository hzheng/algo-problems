import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

// LC1249: https://leetcode.com/problems/minimum-remove-to-make-valid-parentheses/
//
// Given a string s of '(' , ')' and lowercase English characters.
// Your task is to remove the minimum number of parentheses ( '(' or ')', in any positions ) so that
// the resulting parentheses string is valid and return any valid string.
// Formally, a parentheses string is valid if and only if:
// It is the empty string, contains only lowercase characters, or
// It can be written as AB (A concatenated with B), where A and B are valid strings, or
// It can be written as (A), where A is a valid string.
//
// Constraints:
// 1 <= s.length <= 10^5
// s[i] is one of  '(' , ')' and lowercase English letters.
public class MinRemoveToMakeValidParentheses {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 12 ms(91.04%), 39.9 MB(51.34%) for 60 tests
    public String minRemoveToMakeValid(String s) {
        char[] cs = s.toCharArray();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == '(') {
                stack.push(i);
            } else if (cs[i] == ')') {
                if (stack.isEmpty()) {
                    cs[i] = 0;
                } else {
                    stack.pop();
                }
            }
        }
        while (!stack.isEmpty()) {
            cs[stack.pop()] = 0;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            if (c != 0) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 13 ms(87.87%), 39.5 MB(85.22%) for 60 tests
    public String minRemoveToMakeValid2(String s) {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0, n = s.length(); i < n; i++) {
            char c = s.charAt(i);
            if (c == '(') {
                stack.push(i + 1);
            } else if (c == ')') {
                if (!stack.isEmpty() && stack.peek() > 0) {
                    stack.pop();
                } else {
                    stack.push(-i - 1);
                }
            }
        }
        StringBuilder sb = new StringBuilder(s);
        while (!stack.isEmpty()) {
            sb.deleteCharAt(Math.abs(stack.pop()) - 1);
        }
        return sb.toString();
    }

    // time complexity: O(N), space complexity: O(N)
    // 22 ms(34.08%), 46.9 MB(18.14%) for 60 tests
    public String minRemoveToMakeValid3(String s) {
        int closeLeft = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            closeLeft += (s.charAt(i) == ')') ? 1 : 0;
        }
        StringBuilder sb = new StringBuilder();
        int balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                if (balance == closeLeft) { continue; } // skip extra '('

                balance++;
            } else if (c == ')') {
                closeLeft--;
                if (balance == 0) { continue; } // skip extra ')'

                balance--;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private void test(String s, String... expected) {
        assertThat(minRemoveToMakeValid(s), in(expected));
        assertThat(minRemoveToMakeValid2(s), in(expected));
        assertThat(minRemoveToMakeValid3(s), in(expected));
    }

    @Test public void test() {
        test("lee(t(c)o)de)", "lee(t(c)o)de");
        test("a)b(c)d", "ab(c)d");
        test("))((", "");
        test("(a(b(c)d)", "a(b(c)d)", "(a(bc)d)");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
