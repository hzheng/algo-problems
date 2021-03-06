
import org.junit.Test;
import static org.junit.Assert.*;

// LC227: https://leetcode.com/problems/basic-calculator-ii/
//
// Implement a basic calculator to evaluate a simple expression string.
// The expression string contains only non-negative integers, +, -, *, /
// operators and empty spaces. The integer division should truncate toward zero.
public class Calculator2 {
    // beats 76.08%(28 ms)
    public int calculate(String s) {
        int[] operands = new int[2];
        boolean addition = true;
        int operandCount = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int[] end = {len};
            switch (c) {
            case ' ': continue;
            case '*':
                operands[operandCount - 1] *= getNumber(s, ++i, end);
                break;
            case '/':
                operands[operandCount - 1] /= getNumber(s, ++i, end);
                break;
            case '+': case '-':
                if (operandCount == 2) {
                    add(operands, addition);
                    operandCount--;
                }
                addition = (c == '+');
                operands[operandCount++] = getNumber(s, ++i, end);
                break;

            default:     // number, first time
                operands[operandCount++] = getNumber(s, i, end);
            }
            i = end[0] - 1;
        }
        if (operandCount == 2) {
            add(operands, addition);
        }
        return operands[0];
    }

    private void add(int[] operands, boolean isAddition) {
        if (isAddition) {
            operands[0] += operands[1];
        } else {
            operands[0] -= operands[1];
        }
    }

    private int getNumber(String s, int start, int[] end) {
        int num = 0;
        for (int i = start; i < end[0]; i++) {
            char c = s.charAt(i);
            if (c == ' ') continue;

            if (c < '0' || c > '9') {
                end[0] = i;
                break;
            }

            num *= 10;
            num += c - '0';
        }
        return num;
    }

    // beats 96.03%(14 ms for 109 tests)
    public int calculate2(String s) {
        int res = 0;
        int sign = 1;
        char operator = '+';
        int operand = 0;
        for (int i = 0, len = s.length(); i < len; i++) {
            char c = s.charAt(i);
            switch (c) {
            case ' ': continue;
            case '*': case '/':
                operator = c;
                break;
            case '+': case '-':
                res += sign * operand;
                sign = (c == '+') ? 1 : -1;
                operator = c;
                break;
            default: // assume digits
                int num = c - '0';
                while (++i < len && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + (s.charAt(i) - '0');
                }
                i--;
                if (operator == '*') {
                    operand *= num;
                } else if (operator == '/') {
                    operand /= num;
                } else {
                    operand = num;
                }
            }
        }
        return res + sign * operand;
    }

    // Solution of Choice
    // beats 95.74%(15 ms for 109 tests)
    public int calculate3(String s) {
        int res = 0;
        int sign = 1;
        char operator = '+';
        int operand = 0;
        int num = 0;
        for (int i = 0, len = s.length(); i <= len; i++) {
            char c = (i == len) ? '+' : s.charAt(i);
            if (c == ' ') continue;
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
                continue;
            }
            if (operator == '*') {
                operand *= num;
            } else if (operator == '/') {
                operand /= num;
            } else {
                operand = num;
            }
            if (c == '+' || c == '-') {
                res += sign * operand;
                sign = (c == '+') ? 1 : -1;
            }
            operator = c;
            num = 0;
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
        test("3 ", 3);
        test("3+2*2", 7);
        test(" 3/2 ", 1);
        test(" 3+5 / 2 ", 5);
        test(" 3*2 + 8 - 9 + 5 / 2 ", 7);
        test(" 81 + 3*2 + 8 - 9 + 5 / 2 * 6 / 3 - 9 * 8 /5 - 1 ", 75);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Calculator2");
    }
}
