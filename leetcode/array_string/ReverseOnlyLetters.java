import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC917: https://leetcode.com/problems/reverse-only-letters/
//
// Given a string S, return the "reversed" string where all characters that are
// not a letter stay in the same place, and all letters reverse their positions.
public class ReverseOnlyLetters {
    // beats 79.86%(6 ms for 116 tests)
    public String reverseOnlyLetters(String S) {
        char[] cs = S.toCharArray();
        for (int i = 0, j = cs.length - 1; ; i++, j--) {
            for (; i < j && !Character.isLetter(cs[i]); i++) {}
            for (; i < j && !Character.isLetter(cs[j]); j--) {}
            if (i >= j) return String.valueOf(cs);

            char tmp = cs[i];
            cs[i] = cs[j];
            cs[j] = tmp;
        }
    }

    // beats 42.67%(8 ms for 116 tests)
    public String reverseOnlyLetters2(String S) {
        StringBuilder res = new StringBuilder();
        for (int i = 0, j = S.length() - 1; i < S.length(); i++) {
            if (Character.isLetter(S.charAt(i))) {
                for (; !Character.isLetter(S.charAt(j)); j--) {}
                res.append(S.charAt(j--));
            } else {
                res.append(S.charAt(i));
            }
        }
        return res.toString();
    }

    // Stack
    // beats 3.85%(14 ms for 116 tests)
    public String reverseOnlyLetters3(String S) {
        Stack<Character> letters = new Stack<>();
        for (char c : S.toCharArray()) {
            if (Character.isLetter(c)) {
                letters.push(c);
            }
        }
        StringBuilder res = new StringBuilder();
        for (char c : S.toCharArray()) {
            res.append(Character.isLetter(c) ? (char) letters.pop() : c);
        }
        return res.toString();
    }

    void test(String S, String expected) {
        assertEquals(expected, reverseOnlyLetters(S));
        assertEquals(expected, reverseOnlyLetters2(S));
        assertEquals(expected, reverseOnlyLetters3(S));
    }

    @Test
    public void test() {
        test("ab-cd", "dc-ba");
        test("a-bC-dEf-ghIj", "j-Ih-gfE-dCba");
        test("Test1ng-Leet=code-Q!", "Qedo1ct-eeLg=ntse-T!");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
