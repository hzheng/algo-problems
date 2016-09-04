import org.junit.Test;
import static org.junit.Assert.*;

// LC009: https://leetcode.com/problems/palindrome-number/
//
// Determine whether an integer is a palindrome. Do this without extra space.
public class PalindromeNumber {
    // beats 23.73%
    public boolean isPalindrome(int x) {
        if (x < 0) return false;

        while (x > 9) {
            int firstDigit = x;
            int power = 1;
            int digits = 1;
            for (; firstDigit > 9; firstDigit /= 10, power *= 10) {
                digits++;
            }
            if (firstDigit != (x % 10)) return false;

            x -= firstDigit * power;
            x /= 10;
            digits -= 2;
            // check 0's
            for (int y = x; y > 0; y /= 10) {
                digits--;
            }
            for (; digits-- > 0; x /= 10) {
                if ((x % 10) != 0) return false;
            }
        }
        return true;
    }

    // beats 19.31%
    public boolean isPalindrome2(int x) {
        if (x < 0) return false;

        String s = Integer.toString(x);
        int len = s.length();
        for (int i = 0; i < len / 2; ++i) {
            if (s.charAt(i) != s.charAt(len - i - 1)) return false;
        }
        return true;
    }

    // beats 75.99%(11 ms)
    public boolean isPalindrome3(int x) {
        if (x < 0) return false;

        while (x > 9) {
            int lastDigit = x % 10;
            x /= 10;

            int firstDigit = x;
            int power = 1;
            for (; firstDigit > 9; firstDigit /= 10, power *= 10) {}
            if (firstDigit != lastDigit) return false;

            x -= firstDigit * power;
            if (x == 0) return true;

            int y = x;
            for (power /= 10; (y % 10) == 0; y /= 10, power /= 10) {}
            if (x < power || x >= power * 10) return false;
            x = y;
        }
        return true;
    }

    // Solution of Choice
    // https://discuss.leetcode.com/topic/8090/9-line-accepted-java-code-without-the-need-of-handling-overflow/19
    // beats 50.41%(12 ms)
    public boolean isPalindrome4(int x) {
        if (x < 0 || (x != 0 && x % 10 == 0)) return false;

        int reversed = 0;
        int y = x;
        for (; y > reversed; y /= 10) {
            reversed = reversed * 10 + y % 10;
        }
        return (y == reversed || y == reversed / 10);
    }

    void test(int x, boolean expected) {
        assertEquals(expected, isPalindrome(x));
        assertEquals(expected, isPalindrome2(x));
        assertEquals(expected, isPalindrome3(x));
        assertEquals(expected, isPalindrome4(x));
    }

    @Test
    public void test1() {
        test(0, true);
        test(-1, false);
        test(30, false);
        test(3, true);
        test(33, true);
        test(23, false);
        test(123, false);
        test(121, true);
        test(1000021, false);
        test(100020001, true);
        test(10002001, false);
        test(10020001, false);
        test(Integer.MAX_VALUE, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PalindromeNumber");
    }
}
