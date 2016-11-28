import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC344: https://leetcode.com/problems/reverse-string/
//
// Write a function that takes a string as input and returns the string reversed.
public class ReverseString {
    // beats 20.70%(5 ms)
    public String reverseString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            sb.append(s.charAt(i));
        }
        return sb.toString();
    }

    // Solution of Choice
    // beats 90.17%(2 ms)
    public String reverseString2(String s) {
        char[] chars = s.toCharArray();
        for (int i = 0, j = chars.length - 1; i < j; i++, j--) {
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars);
    }

    void test(String s, String expected) {
        assertEquals(expected, reverseString(s));
        assertEquals(expected, reverseString2(s));
    }

    @Test
    public void test1() {
        test("", "");
        test("abc", "cba");
        test("aa", "aa");
        test("hello", "olleh");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseString");
    }
}
