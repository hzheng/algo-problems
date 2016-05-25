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

    // finite automata
    // http://blog.csdn.net/kenden23/article/details/18696083
    // beats 52.09%
    public boolean isNumber2(String s) {
        int i = 0;
        // input values
        final int SPACE = i++;
        final int SIGN = i++;
        final int DIGIT = i++;
        final int DOT = i++;
        final int EXP = i++;

        // state values
        i = -1;
        final int ERROR = i++; // error state
        final int INIT = i++;  // initial state
        final int DIGIT_STATE = i++;   // digit state
        final int DOT_STATE = i++;   // dot state
        final int SIGN_STATE = i++;   // sign state
        final int NUM_DOT = i++;   // digit+dot state
        final int EXP_STATE = i++;   // exponential state
        final int EXP_SIGN = i++;   // exponential+sign state
        final int EXP_NUM = i++;   // exponential+digit state
        final int TERM = i++;   // terminate after space

        int transitionTable[][] = {
            //SPACE SIGN        DIGIT        DOT        EXP
            {INIT,  SIGN_STATE, DIGIT_STATE, DOT_STATE, ERROR},     // INIT
            {TERM,  ERROR,      DIGIT_STATE, NUM_DOT,   EXP_STATE}, // DIGIT_STATE
            {ERROR, ERROR,      NUM_DOT,     ERROR,     ERROR},     // DOT_STATE
            {ERROR, ERROR,      DIGIT_STATE, DOT_STATE, ERROR},     // SIGN_STATE
            {TERM,  ERROR,      NUM_DOT,     ERROR,     EXP_STATE}, // NUM_DOT
            {ERROR, EXP_SIGN,   EXP_NUM,     ERROR,     ERROR},     // EXP_STATE
            {ERROR, ERROR,      EXP_NUM,     ERROR,     ERROR},     // EXP_SIGN
            {TERM,  ERROR,      EXP_NUM,     ERROR,     ERROR},     // EXP_NUM
            {TERM,  ERROR,      ERROR,       ERROR,     ERROR}      // TERM
        };
        int state = 0;
        for (char c : s.toCharArray()) {
            int input;
            switch (c) {
            case ' ':
                input = SPACE;
                break;
            case '+': case '-':
                input = SIGN;
                break;
            case '.':
                input = DOT;
                break;
            case 'e':
                input = EXP;
                break;
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                input = DIGIT;
                break;
            default:
                return false;
            }

            state = transitionTable[state][input];
            if (state == ERROR) return false;
        }
        return (state == DIGIT_STATE) || (state == NUM_DOT)
               || (state == EXP_NUM) || (state == TERM);
    }

    // TODO: regular expression
    // s = s.trim();
    // if (s.matches("[+-]?\\d*\\.?\\d+")) return true;
    // if (s.matches("[+-]?\\d+\\.?\\d*")) return true;

    void test(String s, boolean expected) {
        assertEquals(expected, isNumber(s));
        assertEquals(expected, isNumber2(s));
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
