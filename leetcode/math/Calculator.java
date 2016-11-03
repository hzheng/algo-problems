import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC224: https://leetcode.com/problems/basic-calculator/
//
// Implement a basic calculator to evaluate a simple expression string.
// The expression string may contain open ( and closing parentheses ), the
// plus + or minus sign -, non-negative integers and empty spaces
public class Calculator {
    // Solution of Choice
    // Stack
    // beats 90.37%(13 ms for 37 tests)
    public int calculate(String s) {
        Stack<Integer> stack = new Stack<>();
        int sign = 1;
        int res = 0;
        int num = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(res);
                res = 0;
                stack.push(sign);
                sign = 1;
            } else if (c == ')') {
                res += sign * num;
                res *= stack.pop();
                res += stack.pop();
                num = 0;
            } else if (c == '+' || c == '-') {
                res += sign * num;
                num = 0;
                sign = (c == '+') ? 1 : -1;
            } else if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            }
        }
        return res + sign * num;
    }

    // Stack
    // beats 59.79%(23 ms for 37 tests)
    public static int calculate2(String s) {
        int res = 0;
        int sign = 1;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0, len = s.length(); i < len; i++) {
            if (Character.isDigit(s.charAt(i))) {
                int sum = 0;
                while (i < len && Character.isDigit(s.charAt(i))) {
                    sum = sum * 10 + (s.charAt(i++) - '0');
                }
                res += sum * sign;
                i--;
            } else if (s.charAt(i) == '+') {
                sign = 1;
            } else if (s.charAt(i) == '-') {
                sign = -1;
            } else if (s.charAt(i) == '(') {
                stack.push(res);
                stack.push(sign);
                res = 0;
                sign = 1;
            } else if (s.charAt(i) == ')') {
                res *= stack.pop();
                res += stack.pop();
            }
        }
        return res;
    }

    // Stack
    // beats 60.30%(24 ms)
    public int calculate3(String s) {
        Stack<Boolean> signs = new Stack<>();
        signs.push(true);
        int res = 0;
        boolean sign = true;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == ' ') continue;

            if (c == '+' || c == '-') {
                sign = (c == '+');
            } else if (c == '(') {
                signs.push(!(signs.peek() ^ sign));
                sign = true;
            } else if (c == ')') {
                signs.pop();
            } else {
                int num = c - '0';
                while (++i < len && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + (s.charAt(i) - '0');
                }
                i--;
                res += ((signs.peek() ^ sign) ? -1 : 1) * num;
            }
        }
        return res;
    }

    // Stack
    // beats 43.79%(35 ms)
    public int calculate4(String s) {
        Stack<Integer> signs = new Stack<>();
        signs.push(1);
        signs.push(1);
        int res = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == ' ') continue;

            if (c == ')') {
                signs.pop();
            } else if (c >= '0') {
                int num = c - '0';
                while (++i < len && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + (s.charAt(i) - '0');
                }
                i--;
                res += signs.pop() * num;
            } else {
                signs.push(signs.peek() * (c == '-' ? -1 : 1));
            }
        }
        return res;
    }

    void test(String s, int expected) {
        assertEquals(expected, calculate(s));
        assertEquals(expected, calculate2(s));
        assertEquals(expected, calculate3(s));
        assertEquals(expected, calculate4(s));
    }

    @Test
    public void test1() {
        test("(7)-(0)+(4)", 11);
        test(" 1-(5) ", -4);
        test(" 2-(1 + 1) ", 0);
        test("3 ", 3);
        test("1 + 1", 2);
        test(" 2-1 + 2 ", 3);
        test(" 3-2+10 - 11 + 22 -10 ", 12);
        test("(1+(4+5+2)-3)+(6+8)", 23);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Calculator");
    }
}
