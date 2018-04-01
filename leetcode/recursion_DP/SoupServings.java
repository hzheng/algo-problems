import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC808: https://leetcode.com/problems/soup-servings/
//
// There are two types of soup: type A and type B. Initially we have N ml of
// each type of soup. There are four kinds of operations:
// Serve 100 ml of soup A and 0 ml of soup B
// Serve 75 ml of soup A and 25 ml of soup B
// Serve 50 ml of soup A and 50 ml of soup B
// Serve 25 ml of soup A and 75 ml of soup B
// When we serve some soup, we give it to someone and we no longer have it.
// Each turn, we will choose from the 4 operations with equal probability 0.25.
// If the remaining volume of soup is not enough to complete the operation, we
// will serve as much as we can.  We stop once we no longer have some quantity
// of both types of soup.
// Return the probability that soup A will be empty first, plus half the
// probability that A and B become empty at the same time.
// Notes:
// 0 <= N <= 10^9.
// Answers within 10^-6 of the true value will be accepted as correct.
public class SoupServings {
    private static final int MAX = 500;

    // Recursion + Dynamic Programming(Top-Down)
    // beats %(22 ms for 41 tests)
    public double soupServings(int N) {
        int n = (N + 24) / 25;
        return (n > MAX) ? 1 : p(n, n, new HashMap<>());
    }

    private double p(int a, int b, Map<Integer, Double> memo) {
        if (a <= 0 && b <= 0) return 0.5;
        if (a <= 0) return 1;
        if (b <= 0) return 0;

        int key = (a << 16) | b;
        Double v = memo.get(key);
        if (v != null) return v;

        double res = 0;
        for (int i = 1; i <= 4; i++) {
            res += p(a - i, b - 4 + i, memo);
        }
        memo.put(key, res /= 4);
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // beats %(5 ms for 41 tests)
    public double soupServings_2(int N) {
        int n = (N + 24) / 25;
        return (n > MAX) ? 1 : p(n, n, new double[n + 1][n + 1]);
    }

    private double p(int a, int b, double[][] memo) {
        if (a <= 0 && b <= 0) return 0.5;
        if (a <= 0) return 1;
        if (b <= 0) return 0;

        if (memo[a][b] > 0) return memo[a][b];

        double res = 0;
        for (int i = 1; i <= 4; i++) {
            res += p(a - i, b - 4 + i, memo);
        }
        return memo[a][b] = res / 4;
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // beats %(11 ms for 41 tests)
    public double soupServings2(int N) {
        int n = (N + 24) / 25;
        if (n > MAX) return 1;

        double[][] dp = new double[n + 1][n + 1];
        for (int i = 0; i <= n; ++i) {
            for (int j = 0; j <= n; ++j) {
                if (i == 0 && j == 0) {
                    dp[i][j] = 0.5;
                } else if (i == 0) {
                    dp[i][j] = 1;
                } else if (j > 0) {
                    for (int k = 1; k <= 4; k++) {
                        dp[i][j] +=
                            dp[Math.max(0, i - k)][Math.max(0, j - 4 + k)];
                    }
                    dp[i][j] /= 4;
                }
            }
        }
        return dp[n][n];
    }

    void test(int N, double expected) {
        double delta = 1e-5;
        assertEquals(expected, soupServings(N), delta);
        assertEquals(expected, soupServings_2(N), delta);
        assertEquals(expected, soupServings2(N), delta);
    }

    @Test
    public void test() {
        test(50, .625);
        test(3275, 0.99987);
        test(660295675, 1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
