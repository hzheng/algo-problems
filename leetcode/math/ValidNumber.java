import java.util.*;
import java.util.regex.Pattern;

import org.junit.Test;
import static org.junit.Assert.*;

// LC065: https://leetcode.com/problems/valid-number/
//
// Validate if a given string is numeric.
public class ValidNumber {
    // beats 58.20%(4 ms)
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

    // Solution of Choice
    // finite automata
    // http://blog.csdn.net/kenden23/article/details/18696083
    // beats 90.78%(3 ms)
    private static final int[] INPUT_MAP = new int[256];
    static {
        INPUT_MAP[' '] = 1;
        INPUT_MAP['+'] = INPUT_MAP['-'] = 2;
        for (char c = '0'; c <= '9'; c++) { INPUT_MAP[c] = 3; }
        INPUT_MAP['.'] = 4;
        INPUT_MAP['e'] = 5;
    }

    private static final int ERROR = -1; // error state
    private static final int INIT = 0;  // initial state
    private static final int DIGIT_STATE = 1;   // digit state
    private static final int DOT_STATE = 2;   // dot state
    private static final int SIGN_STATE = 3;   // sign state
    private static final int NUM_DOT = 4;   // digit+dot state
    private static final int EXP_STATE = 5;   // exponential state
    private static final int EXP_SIGN = 6;   // exponential+sign state
    private static final int EXP_NUM = 7;   // exponential+digit state
    private static final int TERM = 8;   // terminate after space

    private static final int transitionTable[][] = {
        // SPACE SIGN       DIGIT        DOT        EXP
        {INIT,  SIGN_STATE, DIGIT_STATE, DOT_STATE, ERROR},       // INIT
        {TERM,  ERROR,      DIGIT_STATE, NUM_DOT,   EXP_STATE},   // DIGIT_STATE
        {ERROR, ERROR,      NUM_DOT,     ERROR,     ERROR},       // DOT_STATE
        {ERROR, ERROR,      DIGIT_STATE, DOT_STATE, ERROR},       // SIGN_STATE
        {TERM,  ERROR,      NUM_DOT,     ERROR,     EXP_STATE},   // NUM_DOT
        {ERROR, EXP_SIGN,   EXP_NUM,     ERROR,     ERROR},       // EXP_STATE
        {ERROR, ERROR,      EXP_NUM,     ERROR,     ERROR},       // EXP_SIGN
        {TERM,  ERROR,      EXP_NUM,     ERROR,     ERROR},       // EXP_NUM
        {TERM,  ERROR,      ERROR,       ERROR,     ERROR}        // TERM
    };

    public boolean isNumber2(String s) {
        int state = 0;
        for (char c : s.toCharArray()) {
            int index = INPUT_MAP[c] - 1;
            if (index < 0) return false;

            state = transitionTable[state][index];
            if (state == ERROR) return false;
        }
        return (state == DIGIT_STATE) || (state == NUM_DOT)
               || (state == EXP_NUM) || (state == TERM);
    }

    // Regex
    // beats 26.02%(12 ms)
    private static final Pattern VALID_PAT
        = Pattern.compile(" *[+-]?(\\d+(\\.\\d*)?|\\.\\d+)(e[+-]?\\d+)? *");

    public boolean isNumber3(String s) {
        return VALID_PAT.matcher(s).matches();
    }

    // kind of cheating
    // beats 19.47%(22 ms)
    public boolean isNumber4(String s) {
        try {
            s = s.trim();
            int n = s.length();
            if (n == 0) return false;

            char c = s.charAt(n - 1);
            if (c != '.' && !Character.isDigit(c)) return false;

            double i = Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    void test(String s, boolean expected) {
        assertEquals(expected, isNumber(s));
        assertEquals(expected, isNumber2(s));
        assertEquals(expected, isNumber3(s));
        assertEquals(expected, isNumber4(s));
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
        test("1.2e1", true);
        test("12e1", true);
        test("12e13e4", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidNumber");
    }
}
