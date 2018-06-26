import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC856: https://leetcode.com/problems/score-of-parentheses/
//
// Given a balanced parentheses string S, compute the score of the string based
// on the following rule:
// () has score 1
// AB has score A + B, where A and B are balanced parentheses strings.
// (A) has score 2 * A, where A is a balanced parentheses string.
public class ScoreOfParentheses {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats %(9 ms for 85 tests)
    public int scoreOfParentheses(String S) {
        Stack<Integer> stack = new Stack<>();
        for (char c : S.toCharArray()) {
            if (c == '(') {
                stack.push(0);
                continue;
            }
            for (int a = 0;; ) {
                int top = stack.pop();
                if (top == 0) {
                    stack.push(a == 0 ? 1 : a * 2);
                    break;
                } else {
                    a += top;
                }
            }
        }
        int res = 0;
        for (int a : stack) {
            res += a;
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats %(9 ms for 85 tests)
    public int scoreOfParentheses2(String S) {
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        for (char c : S.toCharArray()) {
            if (c == '(') {
                stack.push(0);
            } else {
                int top = stack.pop();
                stack.push(stack.pop() + Math.max(2 * top, 1));
            }
        }
        return stack.peek();
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(8 ms for 85 tests)
    public int scoreOfParentheses3(String S) {
        int res = 0;
        for (int i = 0, bal = 0; i < S.length(); i++) {
            if (S.charAt(i) == '(') {
                bal++;
            } else {
                bal--;
                if (S.charAt(i - 1) == '(') {
                    res += 1 << bal;
                }
            }
        }
        return res;
    }

    // Divide & Conquer/Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats %(8 ms for 85 tests)
    public int scoreOfParentheses4(String S) {
        return score(S, 0, S.length());
    }

    public int score(String S, int start, int end) {
        int res = 0;
        for (int i = start, bal = 0; i < end; i++) {
            bal += (S.charAt(i) == '(') ? 1 : -1;
            if (bal == 0) {
                if (i - start == 1) {
                    res++;
                } else {
                    res += 2 * score(S, start + 1, i);
                }
                start = i + 1;
            }
        }
        return res;
    }

    void test(String S, int expected) {
        assertEquals(expected, scoreOfParentheses(S));
        assertEquals(expected, scoreOfParentheses2(S));
        assertEquals(expected, scoreOfParentheses3(S));
        assertEquals(expected, scoreOfParentheses4(S));
    }

    @Test
    public void test() {
        test("()", 1);
        test("(())", 2);
        test("()()", 2);
        test("(()(()))", 6);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
