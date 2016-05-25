import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Validate if a given string is numeric.
public class ValidNumber {
    // beats 52.09%
    public boolean isNumber(String s) {
        int len = s.length();
        int start = 0;
        while (start < len && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        int end = len - 1;
        while (end > start && Character.isWhitespace(s.charAt(end))) {
            end--;
        }

        boolean needDigit = true;
        boolean allowDot = true;
        boolean allowSign = true;
        boolean hadExp = false;
        for (int i = start; i <= end; i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                needDigit = false;
                allowSign = false;
            } else if (c == '+' || c == '-') {
                if (!allowSign) return false;

                allowSign = false;
            } else if (c == 'e') {
                if (hadExp || needDigit) return false;

                hadExp = true;
                allowDot = false;
                allowSign = true;
                needDigit = true;
            } else if (c == '.') {
                if (!allowDot) return false;

                allowDot = false;
                allowSign = false;
            } else {
                return false;
            }
        }
        return !needDigit;
    }

    void test(String s, boolean expected) {
        assertEquals(expected, isNumber(s));
    }

    @Test
    public void test1() {
        test("12e-1", true);
        test("123", true);
        test("123a", false);
        test("++123", false);
        test("123.", true);
        test("0123", true);
        test("00123", true);
        test(" 123", true);
        test(" ", false);
        test("+ 12", false);
        test("6+1", false);
        test(".+1", false);
        test("+12.", true);
        test("+12.1 ", true);
        test("+12.1. ", false);
        test("+12e1. ", false);
        test("+12e1 ", true);
        test("12e", false);
        test("12e1", true);
        test("12e13e4", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidNumber");
    }
}
