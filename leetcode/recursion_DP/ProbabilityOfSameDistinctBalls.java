import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1467: https://leetcode.com/problems/probability-of-a-two-boxes-having-the-same-number-of-distinct-balls/
//
// Given 2n balls of k distinct colors. You will be given an integer array balls of size k where
// balls[i] is the number of balls of color i.
// All the balls will be shuffled uniformly at random, then we will distribute the first n balls to
// the first box and the remaining n balls to the other box. Please note that the two boxes are
// considered different. For example, if we have two balls of colors a and b, and two boxes [] and
// (), then the distribution [a] (b) is considered different than the distribution [b] (a).
// We want to calculate the probability that the two boxes have the same number of distinct balls.
// Constraints:
//
// 1 <= balls.length <= 8
// 1 <= balls[i] <= 6
// sum(balls) is even.
// Answers within 10^-5 of the actual value will be accepted as correct.
public class ProbabilityOfSameDistinctBalls {
    // Recursion + Dynamic Programming(Top-Down)
    // 43 ms(77.11%), 36.6 MB(100%) for 21 tests
    public double getProbability(int[] balls) {
        final int N = 50;
        double[] cTable = new double[N];
        cTable[0] = 1;
        for (int i = 1; i < N; i++) {
            cTable[i] = cTable[i - 1] * i;
        }
        int sum = Arrays.stream(balls).sum();
        double valid = dfs(balls, 0, 0, 0, 0, 0, cTable);
        double total = combine(sum, sum / 2, cTable);
        return valid / total;
    }

    private double dfs(int[] balls, int cur, int distinct1, int distinct2, int total1, int total2,
                       double[] cTable) {
        if (cur == balls.length) {
            return (total1 == total2 && distinct1 == distinct2) ? 1 : 0;
        }

        double res =
                dfs(balls, cur + 1, distinct1 + 1, distinct2, total1 + balls[cur], total2, cTable);
        res += dfs(balls, cur + 1, distinct1, distinct2 + 1, total1, total2 + balls[cur], cTable);
        for (int i = 1; i < balls[cur]; i++) {
            res += dfs(balls, cur + 1, distinct1 + 1, distinct2 + 1, total1 + i,
                       total2 + balls[cur] - i, cTable) * combine(balls[cur], i, cTable);
        }
        return res;
    }

    private double combine(int a, int b, double[] cTable) {
        return cTable[a] / cTable[b] / cTable[a - b];
    }

    private static double[][] C;

    private static void createTable(int N) {
        C = new double[N][N];
        for (int i = 0; i < N; i++) {
            C[i][i] = C[i][0] = 1;
            for (int j = 1; j < i; j++) {
                C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
            }
        }
    }

    // Recursion + Dynamic Programming(Top-Down)
    // 24 ms(88.45%), 36.6 MB(100%) for 21 tests
    public double getProbability2(int[] balls) {
        createTable(10);
        int sum = Arrays.stream(balls).sum();
        double[] res = new double[2];
        dfs(balls, sum / 2, 0, 0, 0, 0, 1, res);
        return res[0] / res[1];
    }

    private void dfs(int[] balls, int target, int cur, int total1, int distinct1, int distinct2,
                     double p, double[] res) {
        if (cur == balls.length) {
            if (total1 == target) {
                res[0] += (distinct1 == distinct2 ? 1 : 0) * p;
                res[1] += p;
            }
            return;
        }
        for (int i = 0; i <= balls[cur] && total1 + i <= target; i++) {
            int d1 = distinct1 + (i > 0 ? 1 : 0);
            int d2 = distinct2 + (balls[cur] > i ? 1 : 0);
            dfs(balls, target, cur + 1, total1 + i, d1, d2, p * C[balls[cur]][i], res);
        }
    }

    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N*M*SUM*MAX(ball)), space complexity: O(N*SUM)
    // 4 ms(97.48%), 38.9 MB(100%) for 21 tests
    public double getProbability3(int[] balls) {
        createTable(60);
        int m = balls.length;
        int sum = Arrays.stream(balls).sum();
        double[][] dp = new double[2 * m + 1][sum / 2 + 1];
        dp[m][0] = 1; // dp[i][j] - i: box distinct difference shifted by m; j: total balls
        int ballsSoFar = 0;
        for (int ball : balls) {
            ballsSoFar += ball;
            double[][] ndp = new double[2 * m + 1][sum / 2 + 1];
            for (int i = 0; i <= ball; i++) { // select i of ball to box 1
                for (int distinctDiff = 0; distinctDiff <= 2 * m; distinctDiff++) {
                    for (int total1 = 0; total1 <= sum / 2; total1++) {
                        int nTotal1 = total1 + i;
                        int nTotal2 = ballsSoFar - nTotal1;
                        if (dp[distinctDiff][total1] > 0 && nTotal1 <= sum / 2 && nTotal2 <= sum / 2) {
                            int nDiff = (i == 0) ? (distinctDiff - 1) : ((i == ball) ? distinctDiff + 1 : distinctDiff);
                            ndp[nDiff][nTotal1] += dp[distinctDiff][total1] * C[ball][i];
                        }
                    }
                }
            }
            dp = ndp;
        }
        return dp[m][sum / 2] / C[sum][sum / 2];
    }

    // Math combinatorics
    // https://en.wikipedia.org/wiki/Multinomial_theorem
    public double getProbability4(int[] balls) {
        return 0.0; // TODO
    }

    private void test(int[] balls, double expected) {
        assertEquals(expected, getProbability(balls), 1e-5);
        assertEquals(expected, getProbability2(balls), 1e-5);
        assertEquals(expected, getProbability3(balls), 1e-5);
    }

    @Test public void test() {
        test(new int[] {1, 1}, 1);
        test(new int[] {2, 1, 1}, 0.66667);
        test(new int[] {1, 2, 1, 2}, 0.6);
        test(new int[] {3, 2, 1}, 0.3);
        test(new int[] {4, 3, 4, 5, 3, 3}, 0.58599);
        test(new int[] {6, 6, 6, 6, 6, 6}, 0.90327);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
