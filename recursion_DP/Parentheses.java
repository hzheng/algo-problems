import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.6:
 * Print all valid combinations of n-pairs of parentheses.
 */
public class Parentheses {
    // TODO: represent left/right parenthese with 1 and 0 for efficiency.

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

    // from the book
    public static List<String> parentheses2(int count) {
        char[] str = new char[count * 2];
        List<String> list = new ArrayList<String>();
        addParen2(list, count, count, str, 0);
        return list;
    }

    private static void addParen2(List<String> list, int leftRem, int rightRem,
                                  char[] str, int count) {
        // if (leftRem < 0 || rightRem < leftRem) return; // invalid state

        if (leftRem == 0 && rightRem == 0) { /* all out of left and right parentheses */
            String s = String.copyValueOf(str);
            list.add(s);
        } else {
            if (leftRem > 0) { // try a left paren, if there are some available
                str[count] = '(';
                addParen2(list, leftRem - 1, rightRem, str, count + 1);
            }
            if (rightRem > leftRem) { // try a right paren, if there's a matching left
                str[count] = ')';
                addParen2(list, leftRem, rightRem - 1, str, count + 1);
            }
        }
    }

    // rewrite <tt>parentheses2</tt> by bit operation
    public static List<String> parenthesesBit(int count) {
        char[] str = new char[count * 2];
        List<String> list = new ArrayList<String>();
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

    private List<String> test(int n, Function<Integer, List<String> > getParen) {
        long t = System.nanoTime();
        List<String> parens = getParen.apply(n);
        System.out.format("%.03f ms\n", (System.nanoTime() - t) * 1e-6);
        System.out.println("total=" + parens.size());
        if (n < 8) {
            Collections.sort(parens);
            for (String s : parens) {
                System.out.println(s);
            }
        }
        return parens;
    }

    @Test
    public void test() {
        int n = 13; // out of memory when n > 15
        System.out.println("test printParentheses2");
        List<String> parens2 = test(n, Parentheses::parentheses2);
        System.out.println("test printParentheseBit");
        List<String> parensBit = test(n, Parentheses::parenthesesBit);
        assertEquals(parens2, parensBit);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Parentheses");
    }
}
