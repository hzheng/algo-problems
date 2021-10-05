import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1900: https://leetcode.com/problems/the-earliest-and-latest-rounds-where-players-compete/
//
// There is a tournament where n players are participating. The players are standing in a single row
// and are numbered from 1 to n based on their initial standing position (player 1 is the first
// player in the row, player 2 is the second player in the row, etc.).
// The tournament consists of multiple rounds (starting from round number 1). In each round, the ith
// player from the front of the row competes against the ith player from the end of the row, and the
// winner advances to the next round. When the number of players is odd for the current round, the
// player in the middle automatically advances to the next round.
//
// For example, if the row consists of players 1, 2, 4, 6, 7
//P layer 1 competes against player 7.
//P layer 2 competes against player 6.
//P layer 4 automatically advances to the next round.
//A fter each round is over, the winners are lined back up in the row based on the original ordering
// assigned to them initially (ascending order).
// The players numbered firstPlayer and secondPlayer are the best in the tournament. They can win
// against any other player before they compete against each other. If any two other players compete
// against each other, either of them might win, and thus you may choose the outcome of this round.
// Given the integers n, firstPlayer, and secondPlayer, return an integer array containing two
// values, the earliest possible round number and the latest possible round number in which these
// two players will compete against each other, respectively.
//
// Constraints:
// 2 <= n <= 28
// 1 <= firstPlayer < secondPlayer <= n
public class EarliestAndLatest {
    // Recursion + DFS + Bit Manipulation
    // time complexity: O(2^N), space complexity: O(log(N))
    // 513 ms(43.14%), 36.8 MB(80.39%) for 137 tests
    public int[] earliestAndLatest(int n, int firstPlayer, int secondPlayer) {
        int[] res = new int[] {n, 0};
        dfs(n, (1 << n) - 1, 1, 0, n - 1, firstPlayer - 1, secondPlayer - 1, res);
        return res;
    }

    private void dfs(int n, int winMask, int round, int i, int j, int first, int second,
                     int[] res) {
        if (i >= j) { // finish one round
            dfs(n, winMask, round + 1, 0, n - 1, first, second, res);
        } else if ((winMask & (1 << i)) == 0) { // skip
            dfs(n, winMask, round, i + 1, j, first, second, res);
        } else if ((winMask & (1 << j)) == 0) { // skip
            dfs(n, winMask, round, i, j - 1, first, second, res);
        } else if (i == first && j == second) {
            res[0] = Math.min(res[0], round);
            res[1] = Math.max(res[1], round);
        } else {
            if (i != first && i != second) { // i loses
                dfs(n, winMask ^ (1 << i), round, i + 1, j - 1, first, second, res);
            }
            if (j != first && j != second) { // j loses
                dfs(n, winMask ^ (1 << j), round, i + 1, j - 1, first, second, res);
            }
        }
    }

    // Recursion + DFS
    // time complexity: O(N^3), space complexity: O(log(N))
    // 0 ms(100.00%), 36.7 MB(86.27%) for 137 tests
    public int[] earliestAndLatest2(int n, int firstPlayer, int secondPlayer) {
        int[] res = new int[] {n, 0};
        dfs(firstPlayer, n - secondPlayer + 1, n, 1, res);
        return res;
    }

    private void dfs(int left, int right, int n, int round, int[] res) {
        if (left == right) {
            res[0] = Math.min(res[0], round);
            res[1] = Math.max(res[1], round);
            return;
        }
        if (left > right) {
            int tmp = left;
            left = right;
            right = tmp;
        }
        for (int i = 1; i <= left; i++) { // left winners
            for (int j = left - i + 1; i + j <= Math.min(right, (n + 1) / 2); j++) { // right winners
                if ((left - i + right - j) <= n / 2) { // losers outside of [left, right] <= half
                    dfs(i, j, (n + 1) / 2, round + 1, res);
                }
            }
        }
    }

    // TODO: Dynamic Programming

    // TODO: Greedy

    private void test(int n, int first, int second, int[] expected) {
        assertArrayEquals(expected, earliestAndLatest(n, first, second));
        assertArrayEquals(expected, earliestAndLatest2(n, first, second));
    }

    @Test public void test1() {
        test(11, 2, 4, new int[] {3, 4});
        test(5, 1, 5, new int[] {1, 1});
        test(21, 10, 19, new int[] {2, 5});
        test(28, 10, 19, new int[] {1, 1});
        test(28, 2, 19, new int[] {2, 5});
        test(28, 1, 19, new int[] {2, 5});
        test(28, 1, 9, new int[] {3, 5});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
