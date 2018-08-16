import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC650: https://leetcode.com/problems/2-keys-keyboard/
//
// Initially on a notepad only one character 'A' is present. You can 
// perform two operations on this notepad for each step:
// Copy All: You can copy all the characters present on the notepad.
// Paste: You can paste the characters which are copied last time.
// You have to get exactly n 'A' on the notepad by performing the minimum number
// of steps permitted. Output the minimum number of steps to get n 'A'.
// Note:
// The n will be in the range [1, 1000].
public class TwoKeysKeyboard {
    // Dynamic Programming
    // time complexity: O(n ^ 3/2), space complexity: O(n)
    // beats 22.29%(50 ms for 126 tests)
    public int minSteps(int n) {
        int[] dp = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            dp[i] = i;
        }
        for (int i = 4; i <= n; i++) {
            for (int j = 2; j < i; j++) {
                if (i % j == 0) {
                    dp[i] = Math.min(dp[i], dp[j] + i / j);
                }
            }
        }
        return dp[n];
    }

    // Dynamic Programming
    // time complexity: O(n ^ 3/2), space complexity: O(n)
    // beats 75.00%(7 ms for 126 tests)
    public int minSteps2(int n) {
        int[] dp = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            dp[i] = i;
        }
        for (int i = 2; i < n; i++) {
            for (int j = 1, k = i; k <= n; j++, k += i) {
                dp[k] = Math.min(dp[k], dp[i] + j);
            }
        }
        return dp[n];
    }

    // Math
    // time complexity: O(n ^ 1/2), space complexity: O(1)
    // beats 100.00%(5 ms for 126 tests)
    public int minSteps3(int n) {
        // p+q <= pq <=> (p-1)(q-1) >= 1, hence always break compound numbers
        int res = 0;
        for (int factor = 2, m = n; m > 1; factor++) {
            for (; m % factor == 0; m /= factor) {
                res += factor;
            }
        }
        return res;
    }

    void test(int n, int expected) {
        assertEquals(expected, minSteps(n));
        assertEquals(expected, minSteps2(n));
        assertEquals(expected, minSteps3(n));
    }

    @Test
    public void test() {
        test(3, 3);
        test(6, 5);
        test(10, 7);
        test(108, 13);
        test(1000, 21);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
