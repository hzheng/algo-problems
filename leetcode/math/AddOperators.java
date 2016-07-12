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
            String expression = codeToStr(num, addCode, 2);
            if (expression != null && evaluate(expression) == target) {
                res.add(expression);
            }
        }
        return res;
    }

    private String codeToStr(String num, int code, int bits) {
        StringBuilder sb = new StringBuilder();
        sb.append(num.charAt(0));
        int len = num.length();
        int mask = (1 << bits) - 1;
        for (int i = 1; i < len; i++) {
            switch (code & mask) {
            case 0:
                if (num.charAt(i - 1) == '0') {
                    if (i == 1 || !Character.isDigit(sb.charAt(sb.length() - 2))) {
                        return null;
                    }
                }
                break;
            case 1:
                sb.append("*");
                break;
            case 2:
                sb.append("+");
                break;
            case 3:
                sb.append("-");
                break;
            }
            sb.append(num.charAt(i));
            code >>= bits;
        }
        return sb.toString();
    }

    private long evaluate(String s) {
        boolean multiply = false;
        int sign = 1;
        long lastOperand = 0;
        long res = 0;
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

    // beats 0.83%(915 ms)
    public List<String> addOperators2(String num, int target) {
        int len = num.length();
        if (len == 0) return Collections.emptyList();

        List<String> res = new ArrayList<>();
        if (Long.parseLong(num) == target) {
            if (num.charAt(0) != '0' || num.length() == 1) {
                res.add(num);
            }
        }
        for (int i = 1; i <= len; i++) {
            for (int multCode = (1 << (i - 1)) - 1; multCode >= 0; multCode--) {
                String leftExpr = multiplyStr(num.substring(0, i), multCode);
                if (leftExpr == null) continue;

                int partialVal = 1;
                try {
                    for (String factor : leftExpr.split("\\*")) {
                        partialVal *= Integer.parseInt(factor);
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
                if (i == len) {
                    if (multCode != 0 && partialVal == target) {
                        res.add(leftExpr);
                    }
                    continue;
                }
                for (String rightExpr : addOperators2(num.substring(i), target - partialVal)) {
                    res.add(leftExpr + "+" + rightExpr);
                }
                for (String rightExpr : addOperators2(num.substring(i), partialVal - target)) {
                    rightExpr = rightExpr.replace('+', ' ')
                                         .replace('-', '+').replace(' ', '-');
                    res.add(leftExpr + "-" + rightExpr);
                }
            }
        }
        return res;
    }

    private String multiplyStr(String num, int code) {
        StringBuilder sb = new StringBuilder();
        sb.append(num.charAt(0));
        int len = num.length();
        for (int i = 1; i < len; i++) {
            switch (code & 1) {
            case 0:
                if (num.charAt(i - 1) == '0') {
                    for (int j = sb.length() - 2;; j--) {
                        if (j < 0) return null;

                        char c = sb.charAt(j);
                        if (c < '0' || c > '9') return null;

                        if (c != '0') break;
                    }
                }
                break;
            case 1:
                sb.append("*");
                break;
            }
            sb.append(num.charAt(i));
            code >>= 1;
        }
        return sb.toString();
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, Integer, List<String> > addOperators,
              String name, String num, int x, String ... expected) {
        Arrays.sort(expected);
        long t1 = System.nanoTime();
        List<String> res = addOperators.apply(num, x);
        if (num.length() > 9) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
        Collections.sort(res);
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    void test(String num, int x, String ... expected) {
        AddOperators a = new AddOperators();
        test(a::addOperators, "addOperators", num, x, expected);
        test(a::addOperators2, "addOperators2", num, x, expected);
    }

    @Test
    public void test1() {
        test("00", 0, "0+0", "0-0", "0*0");
        test("8999999", -999991, "8-999999");
        test("123", 6, "1+2+3", "1*2*3");
        test("232", 8, "2*3+2", "2+3*2");
        test("105", 5, "1*0+5", "10-5");
        test("3456237490", 9191);
        test("2147483648", -2147483648);
        test("999999999", 81, "9+9+9+9+9+9+9+9+9", "999-9*99-9-9-9",
             "999-9-9*99-9-9", "999-9-9-9*99-9", "999-9-9-9-9*99",
             "999-9-9-9-99*9", "999-9-9-99*9-9","999-9-99*9-9-9","999-99*9-9-9-9");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AddOperators");
    }
}
