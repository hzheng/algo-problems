import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC788: https://leetcode.com/problems/rotated-digits/
//
// X is a good number if after rotating each digit individually by 180 degrees,
// we get a valid number that is different from X. A number is valid if each
// digit remains a digit after rotation. 0, 1, and 8 rotate to themselves; 2 and
// 5 rotate to each other; so do 6 and 9, and the rest are invalid.
// Now given a positive number N, how many numbers X from 1 to N are good?
public class RotatedDigits {
    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats 27.90%(26 ms for 50 tests)
    public int rotatedDigits(int N) {
        int res = 0;
        for (int i = 1; i <= N; i++) {
            if (good("" + i)) {
                res++;
            }
        }
        return res;
    }

    private boolean good(String s) {
        boolean hasDiff = false;
        for (char c : s.toCharArray()) {
        switch (c) {
            case '3': case '4': case '7': return false;
            case '2': case '5': case '6': case '9':
                hasDiff = true;
                break;
            }
        }
        return hasDiff;
    }

    // time complexity: O(N * log(N)), space complexity: O(1)
    // beats 93.49%(9 ms for 50 tests)
    public int rotatedDigits2(int N) {
        int res = 0;
        for (int i = 1; i <= N; i++) {
            if (good(i)) {
                res++;
            }
        }
        return res;
    }

    private boolean good(int n) {
        boolean hasDiff = false;
        for (int i = n; i > 0; i /= 10) {
        switch (i % 10) {
            case 3: case 4: case 7: return false;
            case 2: case 5: case 6: case 9:
                hasDiff = true;
                break;
            }
        }
        return hasDiff;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 51.34%(12 ms for 50 tests)
    public int rotatedDigits3(int N) {
        int[] dp = new int[N + 10];
        int res = 0;
        for (int i = 0; i <= N; i++) {
            if (i < 10) {
                if (i == 0 || i == 1 || i == 8) {
                    dp[i] = 1;
                } else if (i == 2 || i == 5 || i == 6 || i == 9) {
                    dp[i] = 2;
                    res++;
                }
            } else {
                dp[i] = Math.min(dp[i / 10] * dp[i % 10], 2);
                res += Math.max(dp[i] - 1, 0);
            }
        }
        return res;
    }

    int[] count7 = new int[] {1, 2, 3, 3, 3, 4, 5, 5, 6, 7}; // 0,1,2,5,6,8,9
    int[] count3 = new int[] {1, 2, 2, 2, 2, 2, 2, 2, 3, 3}; // 0,1,8
    boolean[] digit7 = new boolean[] {true, true, true, false, false, true, 
                                      true, false, true, true};
    boolean[] digit3 = new boolean[] {true, true, false, false, false, false, 
                                      false, false, true, false};

    // time complexity: O(log(N)), space complexity: O(log(N))
    // beats 99.53%(5 ms for 50 tests)
    public int rotatedDigits4(int N) {
        int res = 0;
        char[] cs = Integer.toString(N).toCharArray();
        int len = cs.length;
        int base7 = (int) Math.pow(7, len - 1);
        int base3 = (int) Math.pow(3, len - 1);
        boolean excluded = true;
        for (int i = 0; i < len; i++, base7 /= 7, base3 /= 3) {
            int digit = cs[i] - '0';
            if (digit == 0 && i != len - 1) continue;

            int j = digit - ((i == len - 1) ? 0 : 1);
            res += count7[j] * base7 // 0,1,8 and 2,5,6,9
                   - (excluded ? count3[j] * base3 : 0); // 0,1,8
            if (!digit7[digit]) break;

            excluded &= digit3[digit];
        }
        return res;
    }

    void test(int N, int expected) {
        assertEquals(expected, rotatedDigits(N));
        assertEquals(expected, rotatedDigits2(N));
        assertEquals(expected, rotatedDigits3(N));
        assertEquals(expected, rotatedDigits4(N));
    }

    @Test
    public void test() {
        test(10, 4);
        test(20, 9);
        test(100, 40);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
