import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC439: https://leetcode.com/problems/ternary-expression-parser
//
// Given a string representing arbitrarily nested ternary expressions, calculate
// the result of the expression. You can always assume that the given expression
// is valid and only consists of digits 0-9, ?, :, T and F (T and F represent
// True and False respectively).
// Note:
// The length of the given string is ≤ 10000.
// Each number will contain only one digit.
// The conditional expressions group right-to-left (as usual in most languages).
// The condition will always be either T or F. That is, the condition will never
// be a digit.
// The result of the expression will always evaluate to either a digit 0-9, T or F.
public class ParseTernary {
    public String parseTernary(String expression) {
        int len = expression.length();
        if (len <= 1) return expression;

        int lastBranch = expression.lastIndexOf('?');
        String next = expression.substring(0, lastBranch - 1);
        if (expression.charAt(lastBranch - 1) == 'T') {
            return parseTernary(next + expression.charAt(lastBranch + 1)
                                + expression.substring(lastBranch + 4));
        }
        return parseTernary(next + expression.substring(lastBranch + 3));
    }

    public String parseTernary2(String expression) {
        return parse(new StringBuilder(expression)).toString();
    }

    private StringBuilder parse(StringBuilder expression) {
        int branchPos = expression.lastIndexOf("?");
        if (branchPos < 0) return expression;

        int colonPos1 = expression.indexOf(":", branchPos + 1);
        int colonPos2 = expression.indexOf(":", colonPos1 + 1);
        if (expression.charAt(branchPos - 1) == 'T') {
            expression.delete(colonPos1, colonPos2 < 0 ? expression.length() : colonPos2);
            expression.delete(branchPos - 1, branchPos + 1);
        } else {
            expression.delete(branchPos - 1, colonPos1 + 1);
        }
        return parse(expression);
    }

    void test(String expression, String expected) {
        assertEquals(expected, parseTernary(expression));
        assertEquals(expected, parseTernary2(expression));
    }

    @Test
    public void test() {
        test("F?F?T?1:2:3:4", "4");
        test("T?T:F?T?1:2:F?3:4", "T");
        test("F?F?3:8:T?F:T?0:F?8:T", "F");
        test("T?2:3", "2");
        test("F?2:3", "3");
        test("F?1:T?4:5", "4");
        test("T?T?F:5:3", "F");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ParseTernary");
    }
}
