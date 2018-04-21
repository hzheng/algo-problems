import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/8224486/dashboard#s=p1
// Round 1B 2015: Problem B - Noisy Neighbors
//
// You own a building that is an R x C grid of apartments; each apartment is a
// unit square cell with four walls. You want to rent out N of these apartments
// to tenants, with exactly 1 tenant per apartment, and leave the others empty.
// Whenever any 2 occupied apartments share a wall (and not just a corner), this
// will add one point of unhappiness to the building. If you place N tenants
// optimally, what is the minimum unhappiness value for your building?
// Input
// The first line of the input gives the number of test cases, T. T lines
// follow; each contains three space-separated integers: R, C, and N.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number (starting from 1) and y is the minimum possible unhappiness.
// Limits
// 1 ≤ T ≤ 1000.
// 0 ≤ N ≤ R*C.
// Small dataset
// 1 ≤ R*C ≤ 16.
// Large dataset
// 1 ≤ R*C ≤ 10000.
public class NoisyNeighbors {
    public static int minNoise(int R, int C, int N) {
        if (R > C) return minNoise(C, R, N); // make sure R <= C

        if (N <= (R * C + 1) / 2) return 0;

        int max = C * (R - 1) + R * (C - 1);
        int toRemove = R * C - N;
        if (R == 1) return max - toRemove * 2;

        boolean oddRowAndCol = (R % 2 + C % 2) == 2;
        int res = getScore(max, new int[] {toRemove}, (R * C + 1) / 2,
                           ((R - 2) * (C - 2) + 1) / 2, oddRowAndCol ? 4 : 2);
        if (oddRowAndCol) {
            res = Math.min(
                // X.X
                // .X.
                // X.X
                res,
                // .X.
                // X.X
                // .X.
                getScore(max, new int[] {toRemove}, R * C / 2,
                         ((R - 2) * (C - 2)) / 2, 0));
        }
        return res;
    }

    private static int getScore(int maxScore, int[] toRemove, int all,
                                int inners, int corners) {
        return maxScore - removeTenants(toRemove, inners, 4)
               - removeTenants(toRemove, all - corners - inners, 3) // sides
               - removeTenants(toRemove, corners, 2);
    }

    private static int removeTenants(int[] toRemove, int maxRemove,
                                     int removeCost) {
        int removed = Math.min(toRemove[0], maxRemove);
        toRemove[0] -= removed;
        return removed * removeCost;
    }

    void test(int R, int C, int N, int expected) {
        assertEquals(expected, minNoise(R, C, N));
    }

    @Test
    public void test() {
        test(2, 3, 6, 7);
        test(4, 1, 2, 0);
        test(3, 3, 8, 8);
        test(5, 2, 0, 0);

        test(3, 3, 6, 3);
        test(3, 4, 12, 17);
        test(4, 3, 12, 17);
        test(4, 4, 15, 20);
        test(4, 4, 15, 20);
        test(1, 4, 3, 1);
        test(2, 3, 5, 4);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
            out.format("Usage: java %s input_file [output_file]%n%n",
                       clazz);
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
        int R = in.nextInt();
        int C = in.nextInt();
        int N = in.nextInt();
        out.println(minNoise(R, C, N));
    }
}
