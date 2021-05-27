import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1872: https://leetcode.com/problems/stone-game-viii/
//
// Alice and Bob take turns playing a game, with Alice starting first.
// There are n stones arranged in a row. On each player's turn, while the number of stones is more
// than one, they will do the following:
// Choose an integer x > 1, and remove the leftmost x stones from the row.
// Add the sum of the removed stones' values to the player's score.
// Place a new stone, whose value is equal to that sum, on the left side of the row.
// The game stops when only one stone is left in the row.
// The score difference between Alice and Bob is (Alice's score - Bob's score). Alice's goal is to
// maximize the score difference, and Bob's goal is the minimize the score difference.
// Given an integer array stones of length n where stones[i] represents the value of the ith stone
// from the left, return the score difference between Alice and Bob if they both play optimally.
//
// Constraints:
// n == stones.length
// 2 <= n <= 10^5
// -10^4 <= stones[i] <= 10^4
public class StoneGameVIII {
    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(75.54%), 53.2 MB(43.34%) for 77 tests
    public int stoneGameVIII(int[] stones) {
        int n = stones.length;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + stones[i];
        }
        int[] dp = new int[n];
        dp[n - 1] = sum[n];
        for (int i = n - 1; i > 1; i--) { // dp[i] = max(sum[j] - dp[j]) for all j > i
            dp[i - 1] = Math.max(dp[i], sum[i] - dp[i]);
        }
        return dp[1];
    }

    // Solution of Choice
    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(1)
    // 6 ms(35.29%), 49.8 MB(57.58%) for 77 tests
    public int stoneGameVIII2(int[] stones) {
        int sum = Arrays.stream(stones).sum();
        int res = sum; // take all
        for (int i = stones.length - 1; i > 1; i--) {
            sum -= stones[i];
            res = Math.max(res, sum - res);
        }
        return res;
    }

    private void test(int[] stones, int expected) {
        assertEquals(expected, stoneGameVIII(stones));
        assertEquals(expected, stoneGameVIII2(stones));
    }

    @Test public void test1() {
        test(new int[] {-1, 2, -3, 4, -5}, 5);
        test(new int[] {7, -6, 5, 10, 5, -2, -6}, 13);
        test(new int[] {-10, -12}, -22);
        test(new int[] {25, -35, -37, 4, 34, 43, 16, -33, 0, -17, -31, -42, -42, 38, 12, -5, -43,
                        -10, -37, 12}, 38);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
