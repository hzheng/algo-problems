import org.junit.Test;
import static org.junit.Assert.*;

// LC600: https://leetcode.com/problems/non-negative-integers-without-consecutive-ones/
//
// Given a positive integer n, find the number of non-negative integers less than or
// equal to n, whose binary representations do NOT contain consecutive ones.
public class IntegersWithoutConsecutiveOnes {
    // Dynamic Programming
    // beats 86.78%(28 ms for 527 tests)
    public int findIntegers(int num) {
        int bits = 32 - Integer.numberOfLeadingZeros(num);
        for (int i = bits - 2, prev = bits - 1; i >= 0; i--) {
            if ((num & (1 << i)) == 0) continue;

            if (prev == i + 1) {
                num &= Integer.MAX_VALUE << i + 1; // Or: num >>= i + 1; num <<= i + 1;
                num |= (0x55555555 >> (31 - i));
                break;
            }
            prev = i;
        }
        int[] dp = new int[bits + 2];
        dp[0] = 1;
        dp[1] = 2;
        for (int i = 2; i < bits; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        // Or:
        // int[][] dp = new int[bits][2];
        // dp[0][0] = dp[0][1] = 1;
        // for(int i = 1; i < bits; i++) {
        //     dp[i][0] = dp[i - 1][0] + dp[i - 1][1];
        //     dp[i][1] = dp[i - 1][0];
        // }
        int res = 1;
        for (int i = bits - 1; i >= 0; i--) {
            if ((num & (1 << i)) != 0) {
                res += dp[i];
            }
        }
        return res;
    }

    // Dynamic Programming
    // beats 86.78%(28 ms for 527 tests)
    public int findIntegers2(int num) {
        int[] dp = new int[31];
        dp[0] = 1;
        dp[1] = 2;
        for (int i = 2; i < dp.length; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        int res = 0;
        for (int i = 30, prev = 0; i >= 0; i--) {
            if ((num & (1 << i)) == 0) {
                prev = 0;
            } else {
                res += dp[i];
                if (prev == 1) return res;

                prev = 1;
            }
        }
        return res + 1;
    }

    // Dynamic Programming
    // beats 50.66%(34 ms for 527 tests)
    public int findIntegers3(int num) {
        StringBuilder sb =
            new StringBuilder(Integer.toBinaryString(num)).reverse();
        int bits = sb.length();
        int[] dp = new int[bits + 1];
        dp[0] = 1;
        dp[1] = 2;
        for (int i = 2; i <= bits; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        int res = dp[bits];
        for (int i = bits - 2; i >= 0; i--) {
            if (sb.charAt(i) == '1' && sb.charAt(i + 1) == '1') break;

            if (sb.charAt(i) == '0' && sb.charAt(i + 1) == '0') {
                res -= (i == 0) ? 1 : dp[i - 1];
            }
        }
        return res;
    }

    void test(int num, int expected) {
        assertEquals(expected, findIntegers(num));
        assertEquals(expected, findIntegers2(num));
        assertEquals(expected, findIntegers3(num));
    }

    @Test
    public void test() {
        test(1, 2);
        test(2, 3);
        test(3, 3);
        test(4, 4);
        test(5, 5);
        test(6, 5);
        test(7, 5);
        test(8, 6);
        test(9, 7);
        test(10, 8);
        test(11, 8);
        test(12, 8);
        test(13, 8);
        test(14, 8);
        test(15, 8);
        test(16, 9);
        test(20, 12);
        test(32, 14);
        test(38, 18);
        test(100, 34);
        test(1000, 144);
        test(2000, 233);
        test(6000, 610);
        test(10000, 843);
        test(1000000000, 2178309);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
