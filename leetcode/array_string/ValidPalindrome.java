import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string, determine if it is a palindrome, considering only
// alphanumeric characters and ignoring cases.
public class ValidPalindrome {
    // beats 81.03%
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

    void test(String s, boolean expected) {
        assertEquals(expected, isPalindrome(s));
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
