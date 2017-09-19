import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/32005/dashboard#s=p2
// APAC Semifinal 2008: Problem C - Millionaire
//
// You have been invited to the popular TV show "Would you like to be a millionaire?".
// Before the game starts, the host spins a wheel of fortune to determine P, the probability
// of winning each bet. You start out with some money: X dollars. There are M rounds of
// betting. In each round, you can bet any part of your current money. The amount is not
// limited to whole dollars or whole cents. If you win the bet, your total amount of money
// increases by the amount you bet. Otherwise, your amount of money decreases by that amount.
// After all the rounds of betting are done, you get to keep your winnings (this time the
// amount is rounded down to whole dollars) only if you have accumulated $1000000 or more.
// Otherwise you get nothing. Determine your probability of winning at least $1000000 if
// you play optimally.e. you play so that you maximize your chances of becoming a millionaire).
// Input
// The first line of input gives the number of cases, N.
// Each of the following N lines has the format "M P X".
// Output
// For each test case, output one line containing "Case #X: Y", where:
// X is the test case number, beginning at 1.
// Y is the probability of becoming a millionaire, between 0 and 1.
// Answers with a relative or absolute error of at most 10 ^ -6 will be considered correct.
// Limits
// 1 ≤ N ≤ 100
// 0 ≤ P ≤ 1.0, there will be at most 6 digits after the decimal point.
// 1 ≤ X ≤ 1000000
// Small dataset
// 1 ≤ M ≤ 5
// Large dataset
// 1 ≤ M ≤ 15
public class Millionaire {
    // Dynamic Programming(Bottom-Up)
    // time complexity: O(M * (4 ^ M)), space complexity: O(2 ^ M)
    public static double win(int M, double P, long X) {
        int n = 1 << M; // probability zones
        double[][] dp = new double[2][n + 1];
        dp[0][n] = 1;
        for (int round = 0, index = 0; round < M; round++, index ^= 1) {
            for (int money = 0; money <= n; money++) { // discretized money
                double p = 0;
                for (int bet = 0, maxBet = Math.min(money, n - money); bet <= maxBet; bet++) {
                    p = Math.max(p, P * dp[index][money + bet] + (1 - P) * dp[index][money - bet]);
                }
                dp[index ^ 1][money] = p;
            }
            System.arraycopy(dp[index ^ 1], 0, dp[index], 0, n);
        }
        return dp[0][(int)(X * n / 1000000)];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(M * (4 ^ M)), space complexity: O(M * (2 ^ M))
    public static double win2(int M, double P, long X) {
        int n = 1 << M; // probability zones
        double[][] dp = new double[M + 1][n];
        for (double[] a : dp) {
            Arrays.fill(a, -1);
        }
        return bestBet(M, M, P, (int)(X * n / 1000000), dp);
    }

    private static double bestBet(int M, int round, double P, int money,
                                  double[][] dp) {
        if (money >= (1 << M)) return 1;

        int betUnit = 1 << (M - round);
        if (money % betUnit != 0 || round == 0) return 0;
        if (dp[round][money] >= 0) return dp[round][money];

        double res = 0;
        for (int bet = 0; bet <= money; bet += betUnit) {
            double p1 = bestBet(M, round - 1, P, money - bet, dp);
            double p2 = bestBet(M, round - 1, P, money + bet, dp);
            res = Math.max(res, p1 * (1 - P) + p2 * P);
        }
        return dp[round][money] = res;
    }

    void test(int M, double P, int X, double expected) {
        assertEquals(expected, win(M, P, X), 1E-6);
        assertEquals(expected, win2(M, P, X), 1E-6);
    }

    @Test
    public void test() {
        test(1, 0.5, 500000, 0.5);
        test(3, 0.75, 600000, 0.843750);
        test(8, 0.365308, 528072, 0.373721);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }
        try {
            in = new Scanner(new File(args[0]));
            if (args.length > 1) {
                out = new PrintStream(args[1]);
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        out.printf("%.6f%n", win2(in.nextInt(), in.nextDouble(), in.nextInt()));
    }
}
