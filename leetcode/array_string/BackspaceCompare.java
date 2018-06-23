import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC844: https://leetcode.com/problems/backspace-string-compare/
//
// Given two strings S and T, return if they are equal when both are typed into
// empty text editors. # means a backspace character.
public class BackspaceCompare {
    // beats %(9 ms for 104 tests)
    public boolean backspaceCompare(String S, String T) {
        return type(S).equals(type(T));
    }

    private String type(String S) {
        StringBuilder sb = new StringBuilder();
        for (char c : S.toCharArray()) {
            if (c != '#') {
                sb.append(c);
            } else if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }

    // Stack
    // beats %(15 ms for 104 tests)
    public boolean backspaceCompare2(String S, String T) {
        return type2(S).equals(type2(T));
    }

    private String type2(String S) {
        Stack<Character> res = new Stack<>();
        for (char c : S.toCharArray()) {
            if (c != '#') {
                res.push(c);
            } else if (!res.empty()) {
                res.pop();
            }
        }
        return String.valueOf(res);
    }

    // beats %(9 ms for 104 tests)
    public boolean backspaceCompare3(String S, String T) {
        return type3(S).equals(type3(T));
    }

    private String type3(String S) {
        StringBuilder sb = new StringBuilder();
        for (int i = S.length() - 1, count = 0; i >= 0; i--) {
            char c = S.charAt(i);
            if (c == '#') {
                count++;
            } else {
                if (count > 0) {
                    count--;
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    // beats %(9 ms for 104 tests)
    // space complexity: O(1)
    public boolean backspaceCompare4(String S, String T) {
        for (int i = S.length() - 1, j = T.length() - 1;; i--, j--) {
            for (int b = 0; i >= 0 && (b > 0 || S.charAt(i) == '#'); i--) {
                b += (S.charAt(i) == '#') ? 1 : -1;
            }
            for (int b = 0; j >= 0 && (b > 0 || T.charAt(j) == '#'); j--) {
                b += (T.charAt(j) == '#') ? 1 : -1;
            }
            if (i < 0 || j < 0 || S.charAt(i) != T.charAt(j)) {
                return i == -1 && j == -1;
            }
        }
    }

    void test(String S, String T, boolean expected) {
        assertEquals(expected, backspaceCompare(S, T));
        assertEquals(expected, backspaceCompare2(S, T));
        assertEquals(expected, backspaceCompare3(S, T));
        assertEquals(expected, backspaceCompare4(S, T));
    }

    @Test
    public void test() {
        test("ab#c", "ad#c", true);
        test("abb##c", "ad#c", true);
        test("ab##c", "ad#c", false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
