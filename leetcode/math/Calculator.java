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

    void test(String s, int expected) {
        assertEquals(expected, calculate(s));
    }

    @Test
    public void test1() {
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
