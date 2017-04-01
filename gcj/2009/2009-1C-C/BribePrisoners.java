import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/189252/dashboard#s=p2
// Round 1C 2009: Problem C - Bribe the Prisoners
//
// In a kingdom there are prison cells (numbered 1 to P) built to form a straight
// line segment. Cells number i and i+1 are adjacent, and prisoners in adjacent cells
// are called "neighbours." A wall with a window separates adjacent cells, and neighbours
// can communicate through that window. All prisoners live in peace until a prisoner is
// released. When that happens, the released prisoner's neighbours find out, and each
// communicates this to his other neighbour. That prisoner passes it on to his other
// neighbour, and so on until they reach a prisoner with no other neighbour. A prisoner
// who discovers that another prisoner has been released will angrily break everything
// in his cell, unless he is bribed with a gold coin. So, after releasing a prisoner
// in cell A, all prisoners housed on either side of cell A - until cell 1, cell P or
// an empty cell - need to be bribed. Assume that each prison cell is initially
// occupied by exactly one prisoner, and that only one prisoner can be released per day.
// Given the list of Q prisoners to be released in Q days, find the minimum total
// number of gold coins needed as bribes if the prisoners may be released in any order.
// Note that each bribe only has an effect for one day.
// Input
// The first line of input gives the number of cases, N. N test cases follow. Each
// case consists of 2 lines. The first line is formatted as: P Q
// where P is the number of prison cells and Q is the number of prisoners to be released.
// This will be followed by a line with Q distinct cell numbers (of the prisoners to
// be released), space separated, sorted in ascending order.
// Output
// For each test case, output one line in the format
// Case #X: C
// where X is the case number, starting from 1, and C is the minimum number of gold coins needed as bribes.
// Limits: 1 ≤ N ≤ 100; Q ≤ P; Each cell number is between 1 and P, inclusive.
// Small dataset
// 1 ≤ P ≤ 100; 1 ≤ Q ≤ 5
// Large dataset
// 1 ≤ P ≤ 10000; 1 ≤ Q ≤ 100
public class BribePrisoners {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(Q ^ 3), space complexity: O(Q ^ 2)
    public static int bribePrisoners(int p, int[] releases) {
        int q = releases.length - 1;
        releases[q] = p + 1;
        int[] sums = new int[q + 1];
        for (int i = 0; i < q; i++) {
            sums[i + 1] = sums[i] + releases[i + 1] - releases[i] - 1;
        }
        return minCost(sums, 0, q - 1, new int[q][q]);
    }

    private static int minCost(int[] sums, int start, int end, int[][] dp) {
        if (start >= end) return 0;
        if (dp[start][end] > 0) return dp[start][end];

        int min = Integer.MAX_VALUE;
        for (int i = start; i < end; i++) {
            min = Math.min(min, minCost(sums, start, i, dp) + minCost(sums, i + 1, end, dp));
        }
        return dp[start][end] = min + sums[end + 1] - sums[start] + end - start - 1;
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(Q ^ 3), space complexity: O(Q ^ 2)
    public static int bribePrisoners2(int p, int[] releases) {
        int q = releases.length - 1;
        releases[q] = p + 1;
        int[][] dp = new int[q][q + 1]; // min cost to release prisoners in (i, j)
        for (int dist = 2; dist <= q; dist++) {
            for (int i = 0; i + dist <= q; i++) {
                int min = Integer.MAX_VALUE;
                int j = i + dist;
                for (int k = i + 1; k < j; k++) {
                    min = Math.min(min, dp[i][k] + dp[k][j]);
                }
                dp[i][j] = min + releases[j] - releases[i] - 2;
            }
        }
        return dp[0][q];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(Q ^ 3), space complexity: O(Q ^ 2)
    public static int bribePrisoners3(int p, int[] releases) {
        return minCost(releases, 1, p, new HashMap<>());
    }

    private static int minCost(int[] releases, int start, int end, Map<Integer, Integer> dp) {
        if (start >= end) return 0;

        int key = start * 10000 + end; // p <= 10000
        Integer cached = dp.get(key);
        if (cached != null) return cached;

        int min = 0;
        for (int i = 1; i < releases.length - 1; i++) {
            int prisoner = releases[i];
            if (prisoner < start) continue;
            if (prisoner > end) break;

            int cost = (end - start) + minCost(releases, start, prisoner - 1, dp)
                       + minCost(releases, prisoner + 1, end, dp);
            if (min == 0 || min > cost) {
                min = cost;
            }
        }
        dp.put(key, min);
        return min;
    }

    void test(int p, int[] releases, int expected) {
        assertEquals(expected, bribePrisoners(p, releases));
        assertEquals(expected, bribePrisoners2(p, releases));
        assertEquals(expected, bribePrisoners3(p, releases));
    }

    @Test
    public void test() {
        test(8, new int[] {0, 3, 0}, 7);
        test(20, new int[] {0, 3, 6, 14, 0}, 35);
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
        int p = in.nextInt();
        int q = in.nextInt();
        int[] releases = new int[q + 2]; // padding two ends
        for (int j = 0; j < q; j++) {
            releases[j + 1] = in.nextInt();
        }
        out.println(bribePrisoners2(p, releases));
    }
}
