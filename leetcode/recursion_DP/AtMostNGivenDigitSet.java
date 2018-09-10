import org.junit.Test;
import static org.junit.Assert.*;

// LC902: https://leetcode.com/problems/numbers-at-most-n-given-digit-set/
//
// Given a sorted non-empty subset of {'1','2','3','4','5', '6','7','8','9'}.
// We write numbers using these digits, using each digit as many times as we
// want. Return the number of positive integers that can be written that are
// less than or equal to N.
public class AtMostNGivenDigitSet {
    // Recursion
    // time complexity: O(log(N)), space complexity: O(log(N))
    // beats %(6 ms for 83 tests)
    public int atMostNGivenDigitSet(String[] D, int N) {
        String S = String.valueOf(N);
        int res = atMostNGivenDigitSet(D, S);
        for (int i = S.length() - 1; i > 0; i--) {
            res += Math.pow(D.length, i);
        }
        return res;
    }

    private int atMostNGivenDigitSet(String[] D, String S) {
        int digits = S.length();
        if (digits == 0) return 1;

        char firstDigit = S.charAt(0);
        int res = 0;
        int n = D.length;
        for (int i = 0; i < n; i++) {
            if (D[i].charAt(0) > firstDigit) break;
            if (D[i].charAt(0) == firstDigit) {
                res += atMostNGivenDigitSet(D, S.substring(1));
                break;
            }
            res += Math.pow(n, digits - 1);
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(log(N)), space complexity: O(log(N))
    // beats %(5 ms for 83 tests)
    public int atMostNGivenDigitSet2(String[] D, int N) {
        String S = String.valueOf(N);
        int digits = S.length();
        int[] dp = new int[digits + 1];
        dp[digits] = 1;
        for (int i = digits - 1; i >= 0; i--) {
            char s = S.charAt(i);
            for (String d : D) {
                if (d.charAt(0) < s) {
                    dp[i] += Math.pow(D.length, digits - i - 1);
                } else if (d.charAt(0) == s) {
                    dp[i] += dp[i + 1];
                }
            }
        }
        for (int i = 1; i < digits; i++) {
            dp[0] += Math.pow(D.length, i);
        }
        return dp[0];
    }

    // Dynamic Programming
    // time complexity: O(log(N)), space complexity: O(1)
    // beats %(4 ms for 83 tests)
    public int atMostNGivenDigitSet3(String[] D, int N) {
        String S = String.valueOf(N);
        int digits = S.length();
        int prev = 1;
        for (int i = digits - 1; i >= 0; i--) {
            char s = S.charAt(i);
            int cur = 0;
            for (String d : D) {
                if (d.charAt(0) < s) {
                    cur += Math.pow(D.length, digits - i - 1);
                } else if (d.charAt(0) == s) {
                    cur += prev;
                }
            }
            prev = cur;
        }
        for (int i = 1; i < digits; i++) {
            prev += Math.pow(D.length, i);
        }
        return prev;
    }

    // Dynamic Programming + Bit Manipulation
    // time complexity: O(log(N)), space complexity: O(1)
    // beats %(6 ms for 83 tests)
    public int atMostNGivenDigitSet4(String[] D, int N) {
        int mask = 0;
        for (String d : D) {
            mask |= 1 << (d.charAt(0) - '0');
        }
        char[] s = Integer.toString(N + 1).toCharArray();
        int count = 0;
        boolean more = true;
        for (int i = 0; i < s.length; i++) {
            count *= D.length;
            for (int j = 1; more && (j < s[i] - '0'); j++) {
                if (((mask >> j) & 1) > 0) { // if ((mask << ~j) < 0) {
                    count++;
                }
            }
            if (i > 0) {
                count += D.length;
            }
            more &= ((mask << ~(s[i] - '0')) < 0);
        }
        return count;
    }

    void test(String[] D, int N, int expected) {
        assertEquals(expected, atMostNGivenDigitSet(D, N));
        assertEquals(expected, atMostNGivenDigitSet2(D, N));
        assertEquals(expected, atMostNGivenDigitSet3(D, N));
        assertEquals(expected, atMostNGivenDigitSet4(D, N));
    }

    @Test
    public void test() {
        test(new String[] { "1", "3", "5", "7" }, 100, 20);
        test(new String[] { "1", "4", "9" }, 1000000000, 29523);
        test(new String[] { "1", "2", "3", "6", "7", "8" }, 211, 79);
        test(new String[] { "1", "5", "7", "8" }, 10000, 340);
        test(new String[] { "1", "5", "7", "8" }, 10212, 340);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
