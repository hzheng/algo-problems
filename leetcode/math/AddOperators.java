import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/expression-add-operators/
//
// Given a string that contains only digits 0-9 and a target value, return all
// possibilities to add binary operators (not unary) +, -, or * between the
// digits so they evaluate to the target value.
public class AddOperators {
    // beats 3.49%(476 ms)
    public List<String> addOperators(String num, int target) {
        int len = num.length();
        if (len == 0) return Collections.emptyList();

        List<String> res = new ArrayList<>();
        for (int addCode = (1 << (len * 2 - 2)) - 1; addCode >= 0; addCode--) {
            String expression = codeToStr(num, addCode);
            if (expression != null && evaluate(expression) == target) {
                res.add(expression);
            }
        }
        return res;
    }

    private String codeToStr(String num, int code) {
        StringBuilder sb = new StringBuilder();
        sb.append(num.charAt(0));
        int len = num.length();
        for (int i = 1; i < len; i++) {
            switch (code & 3) {
            case 0:
                if (num.charAt(i - 1) == '0') {
                    if (i == 1 || !Character.isDigit(sb.charAt(sb.length() - 2))) {
                        return null;
                    }
                }
                break;
            case 1:
                sb.append("+");
                break;
            case 2:
                sb.append("-");
                break;
            case 3:
                sb.append("*");
                break;
            }
            sb.append(num.charAt(i));
            code >>= 2;
        }
        return sb.toString();
    }

    private long evaluate(String s) {
        boolean multiply = false;
        int sign = 1;
        long lastOperand = 0;
        int res = 0;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            switch (c) {
            case '*':
                multiply = true;
                break;
            case '+': case '-':
                res += sign * lastOperand;
                sign = (c == '+') ? 1 : -1;
                multiply = false;
                break;
            default: // assume digits
                long num = c - '0';
                while (++i < len && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + s.charAt(i) - '0';
                }
                i--;

                if (multiply) {
                    lastOperand *= num;
                } else {
                    lastOperand = num;
                }
            }
        }
        return res + sign * lastOperand;
    }

    void test(String num, int x, String ... expected) {
        Arrays.sort(expected);
        List<String> res = addOperators(num, x);
        Collections.sort(res);
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    @Test
    public void test1() {
        test("123", 6, "1+2+3", "1*2*3");
        test("232", 8, "2*3+2", "2+3*2");
        test("105", 5, "1*0+5", "10-5");
        test("00", 0, "0+0", "0-0", "0*0");
        test("3456237490", 9191);
        test("2147483648", -2147483648);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AddOperators");
    }
}
