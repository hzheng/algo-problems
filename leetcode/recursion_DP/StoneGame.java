import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC877: https://leetcode.com/problems/stone-game/
//
// There are an even number of piles arranged in a row, and each pile has a
// positive integer number of stones piles[i]. The objective of the game is to
// end with the most stones. The total number of stones is odd. Alex and Lee
// take turns, with Alex starting first. Each turn, a player takes the entire
// pile of stones from either the beginning or the end of the row. This
// continues until there are no more piles left, at which point the person with
// the most stones wins. Assuming Alex and Lee play optimally, return True if
// and only if Alex wins the game.
public class StoneGame {
    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 100%(11 ms for 46 tests)
    public boolean stoneGame(int[] piles) {
        int n = piles.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        for (int len = 1; len < n; len++) {
            for (int i = 0, j = i + len; j < n; i++, j++) {
                dp[i][j] = Math.max(piles[i] - dp[i + 1][j],
                                    piles[j] - dp[i][j - 1]);
            }
        }
        return dp[0][n - 1] > 0;
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 100%(6 ms for 46 tests)
    public boolean stoneGame2(int[] piles) {
        int n = piles.length;
        int[] dp = Arrays.copyOf(piles, n);
        for (int len = 1; len < n; len++) {
            for (int i = 0, j = i + len; j < n; i++, j++) {
                dp[i] = Math.max(piles[i] - dp[i + 1], piles[j] - dp[i]);
            }
        }
        return dp[0] > 0;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 0%(50 ms for 46 tests)
    public boolean stoneGame3(int[] piles) {
        int n = piles.length;
        return win(piles, 0, n - 1, 0, new int[n][n][2]) > 0;
    }

    private int win(int[] piles, int start, int end, int player,
                    int[][][] memo) {
        if (start == end) return piles[start] * (1 - player * 2);

        if (memo[start][end][player] > 0) return memo[start][end][player];

        int r1 = piles[start] + win(piles, start + 1, end, 1 - player, memo);
        int r2 = piles[end] + win(piles, start, end - 1, 1 - player, memo);
        return memo[start][end][player] = Math.max(r1, r2);
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 100%(13 ms for 46 tests)
    public boolean stoneGame3_2(int[] piles) {
        int n = piles.length;
        return win(piles, 0, n - 1, new int[n][n]) > 0;
    }

    private int win(int[] piles, int start, int end, int[][] memo) {
        if (start == end) return piles[start];

        if (memo[start][end] > 0) return memo[start][end];

        int r1 = piles[start] - win(piles, start + 1, end, memo);
        int r2 = piles[end] - win(piles, start, end - 1, memo);
        return memo[start][end] = Math.max(r1, r2);
    }

    // Logic
    // time complexity: O(1), space complexity: O(1)
    // beats 100%(3 ms for 46 tests)
    public boolean stoneGame4(int[] piles) {
        // always choose piles of odd indices or even ones whoseever sum is bigger
        return true;
    }

    void test(int[] piles, boolean expected) {
        assertEquals(expected, stoneGame(piles));
        assertEquals(expected, stoneGame2(piles));
        assertEquals(expected, stoneGame3(piles));
        assertEquals(expected, stoneGame3_2(piles));
        assertEquals(expected, stoneGame4(piles));
    }

    @Test
    public void test() {
        test(new int[] { 5, 3, 4, 5 }, true);
        test(new int[] { 91, 66, 56, 43, 16, 41, 91, 12, 11, 29, 57, 34, 47, 57,
                         47, 98, 77, 42, 23, 13, 55, 4, 76, 37,
                         87, 16, 57, 49, 15, 69, 54, 78, 94, 78, 81, 59, 48, 4,
                         23, 66, 40, 85, 35, 92, 11, 81, 50, 78, 24, 61,
                         66, 76, 33, 23, 1, 88, 99, 55, 17, 71, 72, 69, 60, 36,
                         87, 51, 36, 20, 37, 17, 30, 19, 20, 60, 35, 60,
                         80, 6, 35, 99, 37, 93, 77, 32, 22, 34, 55, 46, 18, 38,
                         86, 73, 75, 100, 80, 11, 46, 13, 40, 57 }, true);
        test(new int[] { 7, 7, 12, 16, 41, 48, 41, 48, 11, 9, 34, 2, 44, 30, 27,
                         12, 11, 39, 31, 8, 23, 11, 47, 25, 15,
                         23, 4, 17, 11, 50, 16, 50, 38, 34, 48, 27, 16, 24, 22,
                         48, 50, 10, 26, 27, 9, 43, 13, 42, 46, 24 },
             true);
        test(new int[] { 59, 48, 36, 70, 59, 93, 60, 98, 15, 32, 31, 13, 27, 14,
                         8, 17, 4, 76, 24, 47, 39, 81, 26, 6,
                         70, 73, 8, 36, 71, 19, 66, 61, 86, 63, 97, 32, 15, 36,
                         68, 69, 32, 53, 83, 35, 100, 41, 44, 8, 28, 76,
                         39, 90, 37, 35, 11, 99, 48, 49, 64, 74, 6, 54, 12, 99,
                         34, 47, 78, 36, 51, 26, 43, 83, 10, 68, 32, 48,
                         72, 54, 64, 64, 44, 62, 77, 60, 100, 84, 15, 24, 95, 6,
                         6, 8, 24, 21, 84, 61, 75, 26, 63, 54 }, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
