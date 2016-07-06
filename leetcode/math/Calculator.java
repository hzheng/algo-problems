import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/basic-calculator/
//
// Implement a basic calculator to evaluate a simple expression string.
// The expression string may contain open ( and closing parentheses ), the
// plus + or minus sign -, non-negative integers and empty spaces
public class Calculator {
    // beats 94.27%(8 ms)
    public int calculate(String s) {
        Stack<Integer> stack = new Stack<>();
        int sign = 1;
        int res = 0;
        int num = 0;
        for (char c : s.toCharArray()) {
            switch (c) {
            case ' ': continue;
            case '+': case '-':
                res += sign * num;
                num = 0;
                sign = (c == '+') ? 1 : -1;
                break;
            case '(':
                stack.push(res);
                res = 0;
                stack.push(sign);
                sign = 1;
                break;
            case ')':
                res += sign * num;
                res *= stack.pop();
                res += stack.pop();
                num = 0;
                break;
            default: // assume digits
                num = num * 10 + c - '0';
            }
        }
        return res + sign * num;
    }

    // beats 60.30%(24 ms)
    public int calculate2(String s) {
        Stack<Boolean> signs = new Stack<>();
        signs.push(true);
        int res = 0;
        boolean sign = true;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            if (c == ' ') continue;

            if (c == '+' || c == '-') {
                sign = c == '+';
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

    // beats 43.79%(35 ms)
    public int calculate3(String s) {
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
            } else if (c >= '0'){
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
