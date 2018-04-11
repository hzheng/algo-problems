import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/3274486/dashboard#s=p2
// Round 1C 2017: Problem C - Core Training
//
// To make an AI as creative as possible, we have given it N different "cores",
// each of which has its own "personality". However, these cores may refuse to
// work; the i-th core has a success probability Pi of functioning properly. As
// long as at least K of the cores function properly, the AI will function
// properly. We plan to train one or more of the cores to become more reliable.
// We have a total of U "training units" that we can use to improve the cores.
// Spending X units on the i-th core will add X to its success probability. We
// can divide up the units among the cores however we like, and it is possible
// that one or more cores may not receive any units. Of course, a core's success
// probability cannot be increased above 1. If we assign the training units to
// maximize the probability that the AI will function properly, what is that
// probability?
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow; each consists of three lines. The first line contains two integers
// N and K: the total number of cores, and the minimum number of cores that must
// succeed for the AI to function properly. The second line contains one
// rational U: the number of training units. The third line contains N rational
// numbers Pi; the i-th of these gives the probability that the i-th core will
// function properly. All of these probabilities are specified to exactly four
// decimal places of precision.
// Output
// For each test case, output one line containing Case #x: y, where x is the
// test case number (starting from 1) and y is the probability that the AI will
// function properly if the training units are assigned optimally. y will be
// considered correct if it is within an absolute or relative error of 10-6 of
// the correct answer.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ N ≤ 50.
// For all i, 0.0000 ≤ Pi ≤ 1.0000.
// 0.0000 ≤ U ≤ N - the sum of all Pi.
// Small dataset 1:  K = N.
// Small dataset 2: 1 ≤ K ≤ N.
public class CoreTraining {
    private static final double EPISON = 1E-6;

    public static double maxProbability(double[] P, int K, double U) {
        Arrays.sort(P);
        double res = 0;
        for (int i = P.length - 1; i >= 0; i--) {
            res = Math.max(res, maxProbability(P.clone(), K, U, i));
        }
        return res;
    }

    private static double maxProbability(double[] P, int K, double U,
                                         int start) {
        int N = P.length;
        int end = start;
        double mean = P[start];
        for (int count = 1; end < N && U > EPISON; end++, count++) {
            double target = (end == N - 1) ? 1 : P[end + 1];
            double spend = Math.min(U, (target - P[end]) * count);
            mean += spend / count;
            U -= spend;
        }
        if (start > 0 && (P[start - 1] += U) > 1) return 0; // ignore

        for (int i = start; i < end; i++) {
            P[i] = mean;
        }
        return calcProbability(P, K);
    }

    // Only good for small dataset 1
    public static double maxProbability1(double[] P, int K, double U) {
        int N = P.length;
        double totalP = U;
        for (double p : P) {
            totalP += p;
        }
        Arrays.sort(P);
        double res = 1;
        int end = N - 1;
        for (; end >= 0; totalP -= P[end--]) {
            if (P[end] * (end + 1) <= totalP) break;
        }
        double mean = totalP / (end + 1);
        for (int i = 0; i <= end; i++) {
            res *= mean;
        }
        for (int i = N - 1; i > end; i--) {
            res *= P[i];
        }
        return res;
    }

    // passed C-small-practice-1.in but failed on test 3!
    public static double maxProbability0(double[] P, int K, double U) {
        int N = P.length;
        double totalP = U;
        for (double p : P) {
            totalP += p;
        }
        if (totalP + EPISON >= N) return 1.0;

        Arrays.sort(P);
        double mean = totalP / N;
        double res = 1;
        int end = N - 1;
        for (; end >= 0; end--) {
            if (P[end] <= mean) break;

            totalP -= P[end];
        }
        mean = totalP / (end + 1);
        for (int i = 0; i <= end; i++) {
            res *= mean;
        }
        for (int i = N - 1; i > end; i--) {
            res *= P[i];
        }
        return res;
    }

    public static double maxProbability2(double[] P, int K, double U) {
        Arrays.sort(P);
        double res = 0;
        int N = P.length;
        outer : for (int start = 0; start < N; start++) {
            double[] p = P.clone();
            double curSum = 0;
            for (int i = start, count = 1; ; count++, i++) {
                curSum += p[i];
                double target = (i == N - 1) ? 1 : p[i + 1];
                if ((i < N - 1) && (count * target - curSum < U)) continue;

                double mean = Math.min((curSum + U) / count, 1);
                for (int j = start; j <= i; j++) {
                    p[j] = mean;
                }
                double more = curSum + U - mean * count;
                if (start > 0 && (p[start - 1] += more) > 1) continue outer;
                break;
            }
            res = Math.max(res, calcProbability(p, K));
        }
        return res;
    }

    private static double calcProbability(double[] P, int K) {
        // 1D Dynamic Programming
        double[] dp = new double[K + 1];
        dp[0] = 1; // at least i good
        for (double p : P) {
            for (int i = K - 1; i >= 0; i--) {
                dp[i + 1] += p * dp[i];
                dp[i] *= 1 - p;
            }
        }
        return dp[K];

        // 2D Dynamic Programming
        // int N = P.length;
        // double[][] dp = new double[N + 1][K + 1];
        // dp[0][0] = 1; // first i cores has exactly j good
        // for (int i = 0; i < N; i++) {
        //     for (int j = K - 1; j >= 0; j--) {
        //         dp[i + 1][j + 1] += dp[i][j] * P[i];
        //         dp[i + 1][j] = dp[i][j] * (1 - P[i]);
        //     }
        // }
        // double res = 1;
        // for (int i = 0; i < K; i++) {
        //     res -= dp[N][i];
        // }

        // or:
        // double res = 0;
        // for (int i = 0; i <= N; i++) {
        //     res2 += dp[i][K];
        // }
        // return res;
    }

    void test(double[] P, int K, double U, double expected) {
        assertEquals(expected, maxProbability(P.clone(), K, U), 10E-6);
        assertEquals(expected, maxProbability2(P.clone(), K, U), 10E-6);
    }

    @Test
    public void test() {
        test(new double[] {0.5, 0.7, 0.8, 0.6}, 4, 1.4, 1);
        test(new double[] {0, 0}, 2, 1, 0.25);
        test(new double[] {0.1, 0.5, 0.9}, 3, 0.1, 0.09); // test 3
        test(new double[] {0.9, 0.8}, 1, 0, 0.98);
        test(new double[] {0.4, 0.5}, 1, 0.1, 0.76);
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n", clazz);
            org.junit.runner.JUnitCore.main(clazz);
            return;
        }

        in = new Scanner(new File(args[0]));
        if (args.length > 1) {
            out = new PrintStream(args[1]);
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int K = in.nextInt();
        double U = in.nextDouble();
        double[] P = new double[N];
        for (int i = 0; i < N; i++) {
            P[i] = in.nextDouble();
        }
        out.printf("%.6f%n", maxProbability(P, K, U));
    }
}
