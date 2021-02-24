import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1190: https://leetcode.com/problems/reverse-substrings-between-each-pair-of-parentheses/
//
// You are given a string s that consists of lower case English letters and brackets.
// Reverse the strings in each pair of matching parentheses, starting from the innermost one.
// Your result should not contain any brackets.
//
// Constraints:
// 0 <= s.length <= 2000
// s only contains lower case English characters and parentheses.
// It's guaranteed that all parentheses are balanced.
public class ReverseParentheses {
    // Stack
    // time complexity: O(N^2), space complexity: O(N)
    // 1 ms(96.03%), 37.7 MB(60.25%) for 38 tests
    public String reverseParentheses(String s) {
        Stack<StringBuilder> stack = new Stack<>();
        stack.push(new StringBuilder());
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(new StringBuilder());
            } else if (c == ')') {
                StringBuilder reversed = stack.pop().reverse();
                stack.peek().append(reversed);
            } else {
                stack.peek().append(c);
            }
        }
        return stack.peek().toString();
    }

    // Solution of Choice
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(96.03%), 37.7 MB(60.25%) for 38 tests
    public String reverseParentheses2(String s) {
        int n = s.length();
        Stack<Integer> stack = new Stack<>();
        int[] parentheses = new int[n];
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else if (s.charAt(i) == ')') {
                int j = stack.pop();
                parentheses[i] = j;
                parentheses[j] = i;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, dir = 1; i < n; i += dir) {
            char c = s.charAt(i);
            if (c == '(' || c == ')') {
                i = parentheses[i];
                dir = -dir;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    // Recursion
    // time complexity: O(N^2), space complexity: O(N)
    // 7 ms(17.50%), 39.2 MB(24.18%) for 38 tests
    public String reverseParentheses3(String s) {
        for (int i = 0, open = 0, n = s.length(); i < n; i++) {
            if (s.charAt(i) == ')') {
                String block = s.substring(open + 1, i);
                return reverseParentheses3(
                        s.substring(0, open) + new StringBuilder(block).reverse() + s
                                .substring(i + 1));
            }
            if (s.charAt(i) == '(') {
                open = i;
            }
        }
        return s;
    }

    private void test(String s, String expected) {
        assertEquals(expected, reverseParentheses(s));
        assertEquals(expected, reverseParentheses2(s));
        assertEquals(expected, reverseParentheses3(s));
    }

    @Test public void test() {
        test("(abcd)", "dcba");
        test("(u(love)i)", "iloveu");
        test("(ed(et(oc))el)", "leetcode");
        test("a(bcdefghijkl(mno)p)q", "apmnolkjihgfedcbq");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
