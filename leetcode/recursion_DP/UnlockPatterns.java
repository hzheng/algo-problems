import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC351: https://leetcode.com/problems/android-unlock-patterns
//
// Given an Android 3x3 key lock screen and two integers m and n, where 1 ≤ m ≤ n ≤ 9,
// count the total number of unlock patterns of the Android lock screen, which
// consist of minimum of m keys and maximum n keys.
// Rules for a valid pattern:
// Each pattern must connect at least m keys and at most n keys.
// All the keys must be distinct.
// If the line connecting two consecutive keys in the pattern passes through any
// other keys, the other keys must have previously selected in the pattern. No
// jumps through non selected key is allowed.
// The order of keys used matters.
public class UnlockPatterns {
    private static final int[][][] JUMPS = new int[][][] {
        {{3, 7, 9}, {2, 4, 5}}, {{8}, {5}}, {{1, 7, 9}, {2, 5, 6}},
        {{6}, {5}}, {{}}, {{4}, {5}}, {{1, 3, 9}, {4, 5, 8}}, {{2}, {5}},
        {{1, 3, 7}, {5, 6, 8}}
    };

    private static final int[][] OBSTACLES = new int[][] {
        {0, 0, 2, 0, 0, 0, 4, 0, 5},
        {0, 0, 0, 0, 0, 0, 0, 5, 0},
        {2, 0, 0, 0, 0, 0, 5, 0, 6},
        {0, 0, 0, 0, 0, 5, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 5, 0, 0, 0, 0, 0},
        {4, 0, 5, 0, 0, 0, 0, 0, 8},
        {0, 5, 0, 0, 0, 0, 0, 0, 0},
        {5, 0, 6, 0, 0, 0, 8, 0, 0}
    };

    // DFS + Recursion + Bit Manipulation(Set)
    // beats 20.62%(40 ms for 24 tests)
    public int numberOfPatterns(int m, int n) {
        int[] res = new int[1];
        dfs(m, n, 0, -1, 0, res);
        return res[0];
    }

    private void dfs(int m, int n, int steps, int prev, int chosen, int[] res) {
        if (steps > n) return;

        if (steps >= m) {
            res[0]++;
        }
        for (int i = 0; i < 9; i++) {
            int mask = 1 << i;
            if (prev < 0 || (chosen & mask) == 0 && isValid(prev, i, chosen)) {
                dfs(m, n, steps + 1, i, chosen | mask, res);
            }
        }
    }

    private boolean isValid(int prev, int cur, int chosen) {
        // int[][] jumps = JUMPS[prev - 1];
        // method 1:
        // int index = Arrays.binarySearch(jumps[0], cur);
        // return index < 0 || (chosen & (1 << jumps[1][index])) != 0;

        // method 2:
        // for (int i = jumps[0].length - 1; i >= 0; i--) {
        //     if (jumps[0][i] == cur) return (chosen & (1 << jumps[1][i])) != 0;
        // }

        // method 3:
        int obstacle = OBSTACLES[prev][cur];
        return obstacle == 0 || (chosen & (1 << obstacle - 1)) != 0;
    }

    // the following method also works without OBSTACLES array
    private boolean isValid2(int prev, int cur, int chosen) {
        if (prev == cur || prev == 4 || cur == 4) return true;
        int sum = prev + cur;
        return (sum % 4 != 2 || cur % 2 == 1) && sum != 8 || (chosen & (1 << sum / 2)) != 0;
    }

    // DFS + Recursion + Bit Manipulation(Set)
    // beats 59.03%(14 ms for 24 tests)
    public int numberOfPatterns2(int m, int n) {
        return dfs2(m, n, 1, 0, 1) * 4 + dfs2(m, n, 1, 1, 1 << 1) * 4 + dfs2(m, n, 1, 4, 1 << 4);
    }

    private int dfs2(int m, int n, int step, int prev, int chosen) {
        if (step > n) return 0;

        int res = 0;
        if (step >= m) {
            res++;
        }
        for (int i = 0; i < 9; i++) {
            int mask = 1 << i;
            if ((chosen & mask) == 0 && isValid(prev, i, chosen)) {
                res += dfs2(m, n, step + 1, i, chosen | mask);
            }
        }
        return res;
    }

    // DFS + Recursion + Bit Manipulation + Dynamic Programming(Top-Down)
    // beats 99.33%(7 ms for 24 tests)
    public int numberOfPatterns3(int m, int n) {
        return dfs3(m, n, 0, 0, 0, new int[n + 1][10][512]);
    }

    private int dfs3(int m, int n, int step, int prev, int chosen, int[][][] dp) {
        if (step > n) return 0;

        if (dp[step][prev][chosen] > 0) return dp[step][prev][chosen];

        int res = 0;
        if (step >= m) {
            res++;
        }
        for (int i = 0; i < 9; i++) {
            int mask = 1 << i;
            if (prev == 0 || (chosen & mask) == 0 && isValid(prev - 1, i, chosen)) {
                res += dfs3(m, n, step + 1, i + 1, chosen | mask, dp);
            }
        }
        return dp[step][prev][chosen] = res;
    }

    // Dynamic Programming(Bottom-Up) + Bit Manipulation
    // beats 31.27%(24 ms for 24 tests)
    public int numberOfPatterns4(int m, int n) {
        final int SIZE = 512;
        int[][][] dp = new int[n + 1][10][SIZE];
        for (int step = n - 1; step >= 0; step--) {
            for (int prev = 0; prev <= 9; prev++) { // or: for (int prev = 9; prev >= 0; prev--) {
                for (int chosen = 0; chosen < SIZE; chosen++) { // or: iterate all possible permutations
                    if (Integer.bitCount(chosen) != step + 1) continue;
                    for (int i = 0; i < 9; i++) {
                        int mask = 1 << i;
                        if (prev == 0 || (chosen & mask) != 0 && isValid(prev - 1, i, chosen)) {
                            dp[step][prev][chosen & ~mask]
                                += dp[step + 1][i + 1][chosen] + (step + 1 >= m ? 1 : 0);
                        }
                    }
                }
            }
        }
        return dp[0][0][0];
    }

    // DFS + Recursion + Bit Manipulation(Set)
    // beats 79.78%(12 ms for 24 tests)
    public int numberOfPatterns5(int m, int n) {
        int res = 0;
        for (int i = m; i <= n; i++) {
            res += dfs5(0, i - 1, 1) * 4;
            res += dfs5(1, i - 1, 1 << 1) * 4;
            res += dfs5(4, i - 1, 1 << 4);
        }
        return res;
    }

    private int dfs5(int cur, int remain, int chosen) {
        if (remain < 0) return 0;
        if (remain == 0) return 1;

        int res = 0;
        for (int i = 0; i < 9; i++) {
            int mask = 1 << i;
            if ((chosen & mask) == 0 && isValid(cur, i, chosen)) {
                res += dfs5(i, remain - 1, chosen | mask);
            }
        }
        return res;
    }

    void test(int m, int n, int expected) {
        assertEquals(expected, numberOfPatterns(m, n));
        assertEquals(expected, numberOfPatterns2(m, n));
        assertEquals(expected, numberOfPatterns3(m, n));
        assertEquals(expected, numberOfPatterns4(m, n));
        assertEquals(expected, numberOfPatterns5(m, n));
    }

    @Test
    public void test() {
        test(1, 1, 9);
        test(2, 2, 56);
        test(1, 2, 65);
        test(2, 4, 2000);
        test(3, 3, 320);
        test(3, 5, 9096);
        test(8, 9, 281408);
        test(4, 9, 389112);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UnlockPatterns");
    }
}
