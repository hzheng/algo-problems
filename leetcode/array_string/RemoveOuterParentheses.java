import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1021: https://leetcode.com/problems/remove-outermost-parentheses/
//
// A valid parentheses string is either empty (""), "(" + A + ")", or A + B, where A and B are valid
// parentheses strings, and + represents string concatenation.  For example, "", "()", "(())()", and
// "(()(()))" are all valid parentheses strings. A valid parentheses string S is primitive if it is
// nonempty, and there does not exist a way to split it into S = A+B, with A and B nonempty valid
// parentheses strings. Given a valid parentheses string S, consider its primitive decomposition:
// S = P_1 + P_2 + ... + P_k, where P_i are primitive valid parentheses strings.
// Return S after removing the outermost parentheses of every primitive string in the primitive
// decomposition of S.
//
// Note:
// S.length <= 10000
// S[i] is "(" or ")"
// S is a valid parentheses string
public class RemoveOuterParentheses {
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(98.30%), 38.8 MB(94.36%) for 59 tests
    public String removeOuterParentheses(String S) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, n = S.length(), start = 1, level = 0; i < n; i++) {
            if (S.charAt(i) == '(') {
                level++;
            } else if (--level == 0) {
                sb.append(S, start, i);
                start = i + 2;
            }
        }
        return sb.toString();
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.0%), 38.7 MB(98.01%) for 59 tests
    public String removeOuterParentheses2(String S) {
        StringBuilder sb = new StringBuilder();
        int level = 0;
        for (char c : S.toCharArray()) {
            if ((c == '(' && level++ > 0) || (c == ')' && --level > 0)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void test(String S, String expected) {
        assertEquals(expected, removeOuterParentheses(S));
        assertEquals(expected, removeOuterParentheses2(S));
    }

    @Test public void test() {
        test("(()())(())", "()()()");
        test("(()())(())(()(()))", "()()()()(())");
        test("()()", "");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
