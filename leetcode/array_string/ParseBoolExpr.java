import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1106: https://leetcode.com/problems/parsing-a-boolean-expression/
//
// Return the result of evaluating a given boolean expression, represented as a string.
// An expression can either be:
// "t", evaluating to True;
// "f", evaluating to False;
// "!(expr)", evaluating to the logical NOT of the inner expression expr;
// "&(expr1,expr2,...)", evaluating to the logical AND of 2 or more inner expressions;
// "|(expr1,expr2,...)", evaluating to the logical OR of 2 or more inner expressions
//
// Constraints:
// 1 <= expression.length <= 20000
// expression[i] consists of characters in {'(', ')', '&', '|', '!', 't', 'f', ','}.
// expression is a valid expression representing a boolean, as given in the description.
public class ParseBoolExpr {
    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 39.2 MB(50.75%) for 30 tests
    public boolean parseBoolExpr(String expression) {
        return eval(expression.toCharArray(), 0, expression.length());
    }

    private boolean eval(char[] expr, int start, int end) {
        if (start >= end) { return true; }

        char first = expr[start];
        if (first == 't') { return true; }
        if (first == 'f') { return false; }

        start += 2;
        end--;
        if (first == '!') { return !eval(expr, start, end); }

        boolean isAnd = (first == '&');
        for (int i = start, level = 0, prev = i; i <= end; i++) {
            switch ((i == end) ? ',' : expr[i]) {
            case '(':
                level++;
                break;
            case ')':
                level--;
                break;
            case ',':
                if (level == 0) {
                    boolean v = eval(expr, prev, i);
                    if (v ^ isAnd) { return v; }

                    prev = i + 1;
                }
            }
        }
        return isAnd;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(58.29%), 38.5 MB(99.50%) for 30 tests
    public boolean parseBoolExpr2(String expression) {
        Stack<Character> stack = new Stack<>();
        for (char c : expression.toCharArray()) {
            if (c == ',' || c == '(') { continue; }
            if (c != ')') {
                stack.push(c);
                continue;
            }
            // process ')'
            outer:
            for (boolean hasTrue = false, hasFalse = false; ; ) {
                switch (stack.pop()) {
                case 't':
                    hasTrue = true;
                    break;
                case 'f':
                    hasFalse = true;
                    break;
                case '!':
                    stack.push(hasFalse ? 't' : 'f');
                    break outer;
                case '&':
                    stack.push(hasFalse ? 'f' : 't');
                    break outer;
                case '|':
                    stack.push(hasTrue ? 't' : 'f');
                    break outer;
                }
            }
        }
        return stack.peek() == 't';
    }

    private void test(String expression, boolean expected) {
        assertEquals(expected, parseBoolExpr(expression));
        assertEquals(expected, parseBoolExpr2(expression));
    }

    @Test public void test() {
        test("!(f)", true);
        test("|(f,t)", true);
        test("&(t,f)", false);
        test("|(&(t,f,t),!(t))", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
