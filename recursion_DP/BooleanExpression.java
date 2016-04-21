import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.11:
 * Given a boolean expression consisting of the symbols 0, 1, &, |, and ^, and
 * a desired boolean result, implement a function to count the number of ways
 * of parenthesizing the expression such that it evaluates to result.
 */
public class BooleanExpression {
    private static boolean[][] counterEval(char operator, boolean desired) {
        switch (operator) {
        case '&':
            return desired ? new boolean[][] {{true, true}}
                   : new boolean[][] {{true, false}, {false, true}, {false, false}};
        case '|':
            return desired ? new boolean[][] {{true, true}, {true, false}, {false, true}}
                   : new boolean[][] {{false, false}};
        case '^':
            return desired ? new boolean[][] {{true, false}, {false, true}}
                   : new boolean[][] {{true, true}, {false, false}};
        default:
            throw new IllegalArgumentException();
        }
    }

    private final static Pattern EXP_PAT = Pattern.compile("[0,1]([&|^][0,1])*");

    private static boolean validate(String expression) {
        return EXP_PAT.matcher(expression).matches();
    }

    public static long evaluate(String expression, boolean result) {
        if (!validate(expression)) {
            throw new IllegalArgumentException(expression + " is invalid.");
        }

        return evaluate(expression, result, new HashMap<String, Long>());
    }

    private static long evaluate(String expr, boolean result,
                                Map<String, Long> map) {

        int len = expr.length();
        if (len == 1) {
            if (expr.charAt(0) == '1') return result ? 1 : 0;
            return result ? 0 : 1;
        }

        String key = expr + (result ? "T" : "F");
        if (map.containsKey(key)) {
            // System.out.println("****" + key + "=" + map.get(key));
            return map.get(key);
        }

        long count = 0;
        for (int i = 1; i < len; i += 2) {
            String leftExpr = expr.substring(0, i);
            String rightExpr = expr.substring(i + 1);
            for (boolean[] boolPair : counterEval(expr.charAt(i), result)) {
                long leftCount = evaluate(leftExpr, boolPair[0], map);
                long rightCount = evaluate(rightExpr, boolPair[1], map);
                count += leftCount * rightCount;
            }
        }
        map.put(key, count);
        return count;
    }

    // From the book
    public static long countDP(String exp, boolean result) {
        if (!validate(exp)) {
            throw new IllegalArgumentException(exp + " is invalid.");
        }

        return countDP(exp, result, 0, exp.length() - 1,
                       new HashMap<String, Long>());
    }

    private static long countDP(String exp, boolean result, int start, int end,
                               Map<String, Long> cache) {
        // problem 1: may have BUG: start + "|" + end
        // problem 2: adding start and end make cache less useful
        String key = "" + result + start + end;
        if (cache.containsKey(key)) {
            // System.out.println("****" + key + "=" + cache.get(key));
            return cache.get(key);
        }
        if (start == end) {
            if (exp.charAt(start) == '1' && result == true) {
                return 1;
            } else if (exp.charAt(start) == '0' && result == false) {
                return 1;
            }
            return 0;
        }
        long count = 0;
        if (result) {
            for (int i = start + 1; i <= end; i += 2) {
                char op = exp.charAt(i);
                if (op == '&') {
                    count += countDP(exp, true, start, i - 1, cache) * countDP(exp, true, i + 1, end, cache);
                } else if (op == '|') {
                    count += countDP(exp, true, start, i - 1, cache) * countDP(exp, false, i + 1, end, cache);
                    count += countDP(exp, false, start, i - 1, cache) * countDP(exp, true, i + 1, end, cache);
                    count += countDP(exp, true, start, i - 1, cache) * countDP(exp, true, i + 1, end, cache);
                } else if (op == '^') {
                    count += countDP(exp, true, start, i - 1, cache) * countDP(exp, false, i + 1, end, cache);
                    count += countDP(exp, false, start, i - 1, cache) * countDP(exp, true, i + 1, end, cache);
                }
            }
        } else {
            for (int i = start + 1; i <= end; i += 2) {
                char op = exp.charAt(i);
                if (op == '&') {
                    count += countDP(exp, false, start, i - 1, cache) * countDP(exp, true, i + 1, end, cache);
                    count += countDP(exp, true, start, i - 1, cache) * countDP(exp, false, i + 1, end, cache);
                    count += countDP(exp, false, start, i - 1, cache) * countDP(exp, false, i + 1, end, cache);
                } else if (op == '|') {
                    count += countDP(exp, false, start, i - 1, cache) * countDP(exp, false, i + 1, end, cache);
                } else if (op == '^') {
                    count += countDP(exp, true, start, i - 1, cache) * countDP(exp, true, i + 1, end, cache);
                    count += countDP(exp, false, start, i - 1, cache) * countDP(exp, false, i + 1, end, cache);
                }
            }
        }
        cache.put(key, count);
        return count;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private long test(Function<String, Boolean, Long> count,
                     String name, String expression, boolean result) {
        long t1 = System.nanoTime();
        long n = count.apply(expression, result);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return n;
    }

    private void test(String expr, boolean result) {
        test(expr, result, -1);
    }

    private void test(String expr, boolean result, int expected) {
        System.out.println("\nexpression: " + expr + ", result: " + result);
        long n1 = test(BooleanExpression::evaluate, "evaluate", expr, result);
        long n2 = test(BooleanExpression::countDP, "countDP", expr, result);
        System.out.println("count=" + n1 + " and " + n2);
        assertEquals(n1, n2);
        if (expected >= 0) {
            assertEquals(expected, n1);
        }
    }

    @Test
    public void test1() {
        test("0", false, 1);
        test("1^0", false, 0);
        test("1^1", false, 1);
        test("1&0", true, 0);
        test("1&1", true, 1);
        test("1|0", true, 1);
        test("1^0|0|1", false, 2);
        test("0|1&1", true, 2);
        test("0^0|1&1", true, 5);
        test("0^0|1&1^1|0|1", false, 28);
        test("0^0|1&1^1|0|1", true, 104);
        test("1&0^0|1&1^1|0|1^0|1|1^0&1&1|0", true);
    }

    private String randExpr(int length) {
        char[] expr = new char[length + 2];
        int n = ThreadLocalRandom.current().nextInt(0, 2);
        expr[0] = (n == 0) ? '0' : '1';
        int i = 1;
        for ( ; i < length; i++) {
            switch (ThreadLocalRandom.current().nextInt(0, 3)) {
                case 0: expr[i] = '|'; break;
                case 1: expr[i] = '&'; break;
                case 2: expr[i] = '^'; break;
            }
            n = ThreadLocalRandom.current().nextInt(0, 2);
            expr[++i] = (n == 0) ? '0' : '1';
        }
        return new String(expr, 0, i);
    }

    @Test
    public void test2() {
        test(randExpr(50), true);
        test(randExpr(50), false);
        test(randExpr(100), true);
        test(randExpr(100), false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BooleanExpression");
    }
}
