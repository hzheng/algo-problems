import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC906: https://leetcode.com/problems/super-palindromes/
//
// Let's say a positive integer is a superpalindrome if it is a palindrome, and
// it is also the square of a palindrome. Given two positive integers L and R,
// return the number of superpalindromes in the inclusive range [L, R].
public class SuperPalindromes {
    // beats %(36 ms for 48 tests)
    public int superpalindromesInRange(String L, String R) {
        return countSuperpalindrome(Long.valueOf(R))
               - countSuperpalindrome(Long.valueOf(L) - 1);
    }

    private int countSuperpalindrome(Long limit) {
        int max = (int)(Math.floor(Math.sqrt(limit)));
        int count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 1;; j++) {
                int pal = composePalindrome(j, i);
                if (pal > max) break;

                if (isPalindrome((long) pal * pal)) {
                    count++;
                }
            }
        }
        return count;
    }

    private int composePalindrome(int num, int odd) {
        int power = 1;
        int res = 0;
        if (odd == 1) {
            res = num % 10;
            num /= 10;
            power *= 10;
        }
        for (int n = num; n > 0; n /= 10, power *= 10) {
            res *= 10;
            res += n % 10;
        }
        return res + num * power;
    }

    private boolean isPalindrome(long num) {
        long power = 1;
        for (long n = num; n >= 10; n /= 10, power *= 10) {}
        for (long n = num; n >= 10; n /= 10, power /= 100) {
            long first = n / power;
            if (first != n % 10) return false;

            n -= first * power;
        }
        return true;
    }

    // beats %(113 ms for 48 tests)
    public int superpalindromesInRange2(String L, String R) {
        long l = Long.valueOf(L);
        long r = Long.valueOf(R);
        int max = (int)Math.pow(10, 18 * 0.25);
        return count(l, r, max, 0) + count(l, r, max, 1);
    }

    private int count(long L, long R, int max, int odd) {
        int res = 0;
        for (int i = 1; i < max; i++) {
            StringBuilder sb = new StringBuilder(Integer.toString(i));
            for (int j = sb.length() - 1 - odd; j >= 0; j--) {
                sb.append(sb.charAt(j));
            }
            long v = Long.valueOf(sb.toString());
            if ((v *= v) > R) break;

            if (v >= L && isPalindrome2(v)) {
                res++;
            }
        }
        return res;
    }

    public boolean isPalindrome2(long x) {
        return x == reverse(x);
    }

    public long reverse(long x) {
        long res = 0;
        for (; x > 0; x /= 10) {
            res = 10 * res + x % 10;
        }
        return res;
    }

    void test(String L, String R, int expected) {
        assertEquals(expected, superpalindromesInRange(L, R));
        assertEquals(expected, superpalindromesInRange2(L, R));
    }

    @Test
    public void test() {
        test("1", "5", 2);
        test("1", "19028", 8);
        test("398904669", "13479046850", 6);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
