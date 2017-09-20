import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC022: https://leetcode.com/problems/generate-parentheses/
// Cracking the Coding Interview(5ed) Problem 9.6:
//
// Print all valid combinations of n-pairs of parentheses.
public class Parentheses {
    // FIXME: wrong
    public static List<String> parenthesesWrong(int count) {
        List<String> list = new ArrayList<String>();
        if (count == 1) {
            list.add("()");
            return list;
        }

        for (String parens : parenthesesWrong(count - 1)) {
            list.add("(" + parens + ")");
            list.add(parens + "()");
            if (parens.charAt(1) == '('
                || parens.charAt(parens.length() - 2) == ')') {
                list.add("()" + parens);
            }
        }
        return list;
    }

    // Solution of Choice
    // Recursion/Backtracking
    // beats 54.04%(2 ms)
    public static List<String> parentheses2(int n) {
        List<String> res = new ArrayList<>();
        addParen2(res, n, n, new char[n * 2], 0);
        return res;
    }

    private static void addParen2(List<String> res, int leftRem, int rightRem,
                                  char[] buf, int index) {
        if (rightRem == 0) { // all out of left and right parentheses
            res.add(String.valueOf(buf));
            return;
        }

        if (leftRem > 0) { // try a left paren, if there are some available
            buf[index] = '(';
            addParen2(res, leftRem - 1, rightRem, buf, index + 1);
        }
        if (rightRem > leftRem) { // try a right paren, if there's a matching left
            buf[index] = ')';
            addParen2(res, leftRem, rightRem - 1, buf, index + 1);
        }
    }

    // rewrite <tt>parentheses2</tt> by bit operation
    public static List<String> parenthesesBit(int count) {
        // char[] str = new char[count * 2];
        List<String> list = new ArrayList<>();
        long[] bits = new long[1];
        if (count < 32)
            addParenBit(list, count, count, bits, 0, count * 2);
        else return null; //TODO
        return list;
    }

    private static void addParenBit(List<String> list, int leftRem, int rightRem,
                                    long[] bits, int index, int count) {
        if (leftRem == 0 && rightRem == 0) {
            list.add(int2str(bits[0], count));
        } else {
            if (leftRem > 0) {
                setBit(bits, true, index, count);
                addParenBit(list, leftRem - 1, rightRem, bits, index + 1, count);
            }
            if (rightRem > leftRem) {
                setBit(bits, false, index, count);
                addParenBit(list, leftRem, rightRem - 1, bits, index + 1, count);
            }
        }
    }

    private static void setBit(long[] n, boolean left, int index, int count) {
        if (left) {
            n[0] |= (1 << (count - index - 1));
        } else {
            n[0] &= ~(1 << (count - index - 1));
        }
    }

    private static String int2str(long n, int count) {
        char[] bitStr = new char[count];
        for (int i = 0; i < count; i++) {
            bitStr[i] = ((n & (1 << (count - i - 1))) > 0) ? '(' : ')';
        }
        return new String(bitStr);
    }

    // FIXME: wrong
    public static List<String> generateParenthesisWrong(int n) {
        if (n < 1) return Collections.emptyList();

        if (n == 1) return Arrays.asList("()");

        List<String> res = new ArrayList<>();
        for (String p : generateParenthesisWrong(n - 1)) {
            res.add("()" + p);
            res.add("(" + p + ")");
            if (p.charAt(1) != ')') {
                res.add(p + "()");
            }
        }
        return res;
    }

    // Recursion/Backtracking
    // beats 28.68%(3 ms)
    public static List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        generateParenthesis(n, 0, 0, new StringBuilder(), res);
        return res;
    }

    private static void generateParenthesis(int n, int leftParens, int rightParens,
                                     StringBuilder sb, List<String> res) {
        if (n == rightParens) {
            res.add(sb.toString());
            return;
        }

        int len = sb.length();
        if (leftParens < n) {
            generateParenthesis(n, leftParens + 1, rightParens, sb.append('('), res);
            sb.setLength(len);
        }
        if (leftParens > rightParens) {
            generateParenthesis(n, leftParens, rightParens + 1, sb.append(')'), res);
            // sb.setLength(len);
        }
    }

    void test(Function<Integer, List<String> > getParen, String name,
              int n, String ... expected) {
        Arrays.sort(expected);
        List<String> res = getParen.apply(n);
        Collections.sort(res);
        assertArrayEquals(expected, res.toArray());
    }

    void test(int n, String ... expected) {
        test(Parentheses::parentheses2, "parentheses2", n, expected);
        test(Parentheses::parenthesesBit, "parenthesesBit", n, expected);
        test(Parentheses::generateParenthesis, "generateParenthesis", n, expected);
    }

    @Test
    public void test1() {
        test(1, "()");
        test(2, "()()", "(())");
        test(3, "((()))", "(()())", "(())()", "()(())", "()()()");
    }

    private List<String> test(int n, Function<Integer, List<String> > getParen) {
        long t = System.nanoTime();
        List<String> parens = getParen.apply(n);
        System.out.format("%.03f ms\n", (System.nanoTime() - t) * 1e-6);
        // System.out.println("total=" + parens.size());
        if (n < 0) {
            Collections.sort(parens);
            for (String s : parens) {
                System.out.println(s);
            }
        }
        return parens;
    }

    private void test(int n) {
        System.out.println("test printParentheses2");
        List<String> parens2 = test(n, Parentheses::parentheses2);
        System.out.println("test printParentheseBit");
        List<String> parensBit = test(n, Parentheses::parenthesesBit);
        assertEquals(parens2, parensBit);
        // System.out.println("test generateParenthesis");
        // List<String> parens = test(n, Parentheses::generateParenthesis);
        // assertEquals(parens2, parens);
    }

    @Test
    public void test2() {
        for (int i = 1; i < 13; i++) { // out of memory when i > 15
            System.out.format("=====%s=====\n", i);
            test(i);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Parentheses");
    }
}
