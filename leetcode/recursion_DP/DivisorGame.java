import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1025: https://leetcode.com/problems/divisor-game/
//
// Alice and Bob take turns playing a game, with Alice starting first.
// Initially, there is a number N on the chalkboard.  On each player's turn, that player makes a
// move consisting of:
// Choosing any x with 0 < x < N and N % x == 0.
// Replacing the number N on the chalkboard with N - x.
// Also, if a player cannot make a move, they lose the game.
// Return True if and only if Alice wins the game, assuming both players play optimally.
//
// Note:
// 1 <= N <= 1000
public class DivisorGame {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N^1.5), space complexity: O(N)
    // 0 ms(100.00%), 35.6 MB(84.43%) for 40 tests
    public boolean divisorGame(int N) {
        return divisorGame(N, new Boolean[N + 1]);
    }

    private boolean divisorGame(int N, Boolean[] dp) {
        if (N <= 1) { return N == 0; }
        if (dp[N] != null) { return dp[N]; }

        dp[N] = false;
        for (int x = 1; x * x <= N; x++) {
            if (N % x != 0) { continue; }

            if (!divisorGame(N - x, dp) || !divisorGame(N - N / x, dp)) {
                dp[N] = true;
                break;
            }
        }
        return dp[N];
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^1.5), space complexity: O(N)
    // 1 ms(28.46%), 35.0 MB(26.86%) for 40 tests
    public boolean divisorGame2(int N) {
        boolean[] dp = new boolean[N + 1];
        dp[0] = true;
        for (int n = 2; n <= N; n++) {
            boolean res = false;
            for (int x = 1; x * x <= n; x++) {
                if (n % x != 0) { continue; }

                if (!dp[n - x] || !dp[n - n / x]) {
                    res = true;
                    break;
                }
            }
            dp[n] = res;
        }
        return dp[N];
    }

    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.5 MB(84.43%) for 40 tests
    public boolean divisorGame3(int N) {
        return N % 2 == 0;
    }

    private void test(int N, boolean expected) {
        assertEquals(expected, divisorGame(N));
        assertEquals(expected, divisorGame2(N));
        assertEquals(expected, divisorGame3(N));
    }

    @Test public void test() {
        test(1, false);
        test(2, true);
        test(3, false);
        test(4, true);
        test(5, false);
        test(6, true);
        test(7, false);
        test(8, true);
        test(9, false);
        test(10, true);
        test(11, false);
        test(12, true);
        test(99, false);
        test(100, true);
        test(1000, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
