import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC375: https://leetcode.com/problems/guess-number-higher-or-lower-ii/
//
// I pick a number from 1 to n. You have to guess which number I picked.
// Every time you guess wrong, I'll tell you whether the number I picked is
// higher or lower. However, when you guess a particular number x, and you guess
// wrong, you pay $x. You win the game when you guess the number I picked.
// Given a particular n ≥ 1, find out how much money you need to have to guarantee a win.
public class GuessNumber2 {
    // Recursion + Dynamic Programming(Top-Down) + Minmax
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 0.17%(820 ms for 13 tests)
    public int getMoneyAmount(int n) {
        return getMoneyAmount(1, n, new HashMap<>());
    }

    private int getMoneyAmount(int start, int end, Map<Long, Integer> memo) {
        if (start >= end) return 0;
        if (start + 1 == end) return start;

        long key = ((long)start << 32) | end;
        if (memo.containsKey(key)) return memo.get(key);

        int min = Integer.MAX_VALUE;
        for (int i = start; i <= end; i++) {
            min = Math.min(min, i + Math.max(getMoneyAmount(start, i - 1, memo),
                                             getMoneyAmount(i + 1, end, memo)));
        }
        memo.put(key, min);
        return min;
    }

    // Recursion + Dynamic Programming(Top-Down) + Minmax
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 72.00%(14 ms for 13 tests)
    public int getMoneyAmount2(int n) {
        return getMoneyAmount2(1, n, new int[n + 1][n + 1]);
    }

    private int getMoneyAmount2(int start, int end, int[][] memo) {
        if (start >= end) return 0;

        if (memo[start][end] > 0) return memo[start][end];

        int min = Integer.MAX_VALUE;
        for (int i = end - 1; i >= start; i--) {
            min = Math.min(min, i + Math.max(getMoneyAmount2(start, i - 1, memo),
                                             getMoneyAmount2(i + 1, end, memo)));
        }
        return memo[start][end] = min;
    }

    // Dynamic Programming(Bottom-Up) + Minmax
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 86.72%(13 ms for 13 tests)
    public int getMoneyAmount3(int n) {
        int[][] dp = new int[n + 1][n + 1];
        for (int step = 1; step < n; step++) {
            for (int i = 1; i + step <= n; i++) {
                int min = Integer.MAX_VALUE;
                for (int j = i; j < i + step; j++) {
                    min = Math.min(min, j + Math.max(
                                       dp[i][j - 1], dp[j + 1][i + step]));
                }
                dp[i][i + step] = min;
            }
        }
        return dp[1][n];
    }

    // Dynamic Programming(Bottom-Up) + Minmax
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 86.72%(13 ms for 13 tests)
    public int getMoneyAmount4(int n) {
        int[][] dp = new int[n + 1][n + 1];
        for (int step = 1; step < n; step++) {
            for (int i = 1; i + step <= n; i++) {
                int min = Integer.MAX_VALUE;
                int j = i;
                for (; j < i + step; j++) {
                    if (dp[i][j - 1] > dp[j + 1][i + step]) break;

                    min = Math.min(min, dp[j + 1][i + step] + j);
                }
                dp[i][i + step] = Math.min(min, dp[i][j - 1] + j);
            }
        }
        return dp[1][n];
    }

    // Solution of Choice
    // Dynamic Programming(Bottom-Up) + Minmax
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // https://leetcode.com/articles/guess-number-higher-or-lower-ii/#approach-4-better-approach-using-dp-accepted
    // beats 86.72%(13 ms for 13 tests)
    public int getMoneyAmount4_2(int n) {
        int[][] dp = new int[n + 1][n + 1];
        for (int len = 2; len <= n; len++) {
            for (int start = 1; start <= n - len + 1; start++) {
                int min = Integer.MAX_VALUE;
                for (int i = start + (len - 1) / 2; i < start + len - 1; i++) {
                    min = Math.min(min, i + Math.max(
                                       dp[start][i - 1], dp[i + 1][start + len - 1]));
                }
                dp[start][start + len - 1] = min;
            }
        }
        return dp[1][n];
    }

    // Dynamic Programming(Bottom-Up) + Minmax
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 86.72%(13 ms for 13 tests)
    public int getMoneyAmount5(int n) {
        int[][] dp = new int[n + 1][n + 1];
        for (int j = 2; j <= n; j++) {
            dp[j - 1][j] = j - 1;
            for (int i = j - 2; i > 0; i--) {
                int min = Integer.MAX_VALUE;
                for (int k = i + 1; k < j; k++) {
                    min = Math.min(min, k + Math.max(
                                       dp[i][k - 1], dp[k + 1][j]));
                }
                dp[i][j] = min;
            }
        }
        return dp[1][n];
    }

    // Solution of Choice
    // Dynamic Programming + Deque
    // http://artofproblemsolving.com/community/c296841h1273742
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 37.96%(17 ms for 13 tests)
    public int getMoneyAmount6(int n) {
        int[][] dp = new int[n + 1][n + 1];
        for (int j = 2; j <= n; j++) {
            Deque<int[]> mins = new LinkedList<>();
            for (int i = j - 1, k = i; i > 0; i--) {
                while (dp[i][k - 1] > dp[k + 1][j]) {
                    if (!mins.isEmpty() && mins.peekFirst()[1] == k) {
                        mins.pollFirst();
                    }
                    k--; // k is the max split point s.t left is less than right
                }
                int min = i + dp[i + 1][j];
                while (!mins.isEmpty() && min < mins.peekLast()[0]) {
                    mins.pollLast();
                }
                mins.offerLast(new int[] {min, i});
                dp[i][j] = Math.min(dp[i][k] + k + 1, mins.peekFirst()[0]);
            }
        }
        return dp[1][n];
    }

    void test(Function<Integer, Integer> getMoneyAmount, String name,
              int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)getMoneyAmount.apply(n));
        if (n >= 100) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int n, int expected) {
        GuessNumber2 g = new GuessNumber2();
        if (n <= 100) {
            test(g::getMoneyAmount, "getMoneyAmount", n, expected);
        }
        test(g::getMoneyAmount2, "getMoneyAmount2", n, expected);
        test(g::getMoneyAmount3, "getMoneyAmount3", n, expected);
        test(g::getMoneyAmount4, "getMoneyAmount4", n, expected);
        test(g::getMoneyAmount4_2, "getMoneyAmount4_2", n, expected);
        test(g::getMoneyAmount5, "getMoneyAmount5", n, expected);
        test(g::getMoneyAmount6, "getMoneyAmount6", n, expected);
    }

    @Test
    public void test1() {
        // test(0, 0);
        test(1, 0);
        test(2, 1);
        test(3, 2);
        test(4, 4);
        test(5, 6);
        test(6, 8);
        test(7, 10);
        test(8, 12);
        test(9, 14);
        test(10, 16);
        test(20, 49);
        test(50, 172);
        test(100, 400);
        test(200, 952);
        test(500, 2933);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GuessNumber2");
    }
}
