import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC125: https://leetcode.com/problems/valid-palindrome/
//
// Given a string, determine if it is a palindrome, considering only
// alphanumeric characters and ignoring cases.
public class ValidPalindrome {
    // beats 34.29%(12 ms)
    public boolean isPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            char left = s.charAt(i);
            while (!Character.isLetterOrDigit(left)) {
                if (++i >= j) return true;

                left = s.charAt(i);
            }
            char right = s.charAt(j);
            while (!Character.isLetterOrDigit(right)) {
                if (--j <= i) return true;

                right = s.charAt(j);
            }
            if (Character.toUpperCase(left) != Character.toUpperCase(right)) {
                return false;
            }
        }
        return true;
    }

    // Solution of Choice
    // beats 49.35%(10 ms)
    public boolean isPalindrome2(String s) {
        for (int i = 0, j = s.length() - 1; i < j; ) {
            char left = Character.toUpperCase(s.charAt(i));
            char right = Character.toUpperCase(s.charAt(j));
            if (left == right) {
                i++;
                j--;
            } else if (!Character.isLetterOrDigit(left)) {
                i++;
            } else if (!Character.isLetterOrDigit(right)) {
                j--;
            } else return false;
        }
        return true;
    }

    // beats 5.66%(45 ms)
    public boolean isPalindrome3(String s) {
        String trimed = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        return new StringBuilder(trimed).reverse().toString().equals(trimed);
    }

    void test(String s, boolean expected) {
        assertEquals(expected, isPalindrome(s));
        assertEquals(expected, isPalindrome2(s));
        assertEquals(expected, isPalindrome3(s));
    }

    @Test
    public void test1() {
        test("", true);
        test("A man, a plan, a canal: Panama", true);
        test("race a car", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidPalindrome");
    }
}
