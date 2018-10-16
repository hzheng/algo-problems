import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC921: https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/
//
// Given a string S of '(' and ')' parentheses, we add the minimum number of 
// parentheses ( '(' or ')', and in any positions ) so that the resulting 
// parentheses string is valid.
public class MinAddToMakeParenValid {
    // beats 99.92%(5 ms for 116 tests)
    public int minAddToMakeValid(String S) {
        int res = 0;
        int extraLeftParens = 0;
        for (char c : S.toCharArray()) {
            if (c == '(') {
                extraLeftParens++;
            } else if (extraLeftParens > 0) {
                extraLeftParens--;
            } else {
                res++;
            }
        }
        return res + extraLeftParens;
    }

    // beats 71.06%(7 ms for 116 tests)
    public int minAddToMakeValid2(String S) {
        int res = 0;
        int bal = 0;
        for (char c : S.toCharArray()) {
            bal += (c == '(') ? 1 : -1;
            if (bal == -1) {
                res++;
                bal++;
            }
        }
        return res + bal;
    }

    void test(String S, int expected) {
        assertEquals(expected, minAddToMakeValid(S));
        assertEquals(expected, minAddToMakeValid2(S));
    }

    @Test
    public void test() {
        test("())", 1);
        test("(((", 3);
        test("()", 0);
        test("()))((", 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
