import java.util.*;
import java.io.*;
import java.math.BigInteger;

import org.junit.Test;
import static org.junit.Assert.*;

// import common.Point;

// https://codejam.withgoogle.com/codejam/contest/4224486/dashboard#s=p2
// Round 1A 2015: Problem C - Logging
// A forest consists of N trees, each of which is inhabited by a squirrel.
// The boundary of the forest is the convex polygon of smallest area which
// contains every tree. Formally, every tree is a single point in 2-dimensional
// space with unique coordinates (Xi, Yi), and the boundary is the convex hull
// of those points. Some trees are on the boundary of the forest, which means
// they are on an edge or a corner of the polygon. The squirrels wonder how
// close their trees are to being on the boundary of the forest.
// One at a time, each squirrel climbs down from its tree, examines the forest,
// and determines the minimum number of trees that would need to be cut down for
// its own tree to be on the boundary. It then writes that number down on a log.
// Determine the list of numbers written on the log.
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow; each consists of a single line with an integer N, the number of
// trees, followed by N lines with two space-separated integers Xi and Yi, the
// coordinates of each tree. No two trees will have the same coordinates.
// Output
// For each test case, output one line containing "Case #x:", followed by N
// lines with one integer each, where line i contains the number of trees that
// the squirrel living in tree i would need to cut down.
// Limits
// -10^6 ≤ Xi, Yi ≤ 10^6.
// Small dataset
// 1 ≤ T ≤ 100.
// 1 ≤ N ≤ 15.
// Large dataset
// 1 ≤ T ≤ 14.
// 1 ≤ N ≤ 3000.

class Point implements Comparable<Point> {
    public int x;
    public int y;

    public Point(int a, int b) {
        x = a;
        y = b;
    }

    public int compareTo(Point other) {
        return x != other.x ? x - other.x : y - other.y;
    }

    public boolean equals(Object other) {
        return compareTo((Point)other) == 0;
    }

    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

public class Logging {
    private static final double EPSILON = 1e-13;

    // Sort + Two Pointers
    // time complexity: O(N ^ 2 * log(N))
    public static int[] log(int[][] trees) {
        int N = trees.length;
        int[] res = new int[N];
        for (int i = N - 1; i >= 0; i--) {
            res[i] = log(trees, i);
        }
        return res;
    }

    private static int log(int[][] trees, int index) {
        int N = trees.length;
        final int A = 2 * N - 2;
        double[] angles = new double[A];
        for (int i = 0, k = 0; i < N; i++) {
            if (i == index) continue;

            angles[k++] = Math.atan2(trees[i][1] - trees[index][1],
                                     trees[i][0] - trees[index][0]);
        }
        Arrays.sort(angles, 0, N - 1);
        for (int i = 0; i < N - 1; i++) {
            angles[i + N - 1] = angles[i] + 2 * Math.PI;
        }
        int res = N - 1;
        for (int i = 0, l = 0, r = 0; i < N - 1; i++) {
            for (; l < A && angles[l] < angles[i] + EPSILON; l++) {}
            for (; r < A && angles[r] < angles[i] + Math.PI - EPSILON; r++) {}
            res = Math.min(res, r - l);
        }
        return res;
    }

    void test(int[][] trees, int[] expected) {
        assertArrayEquals(expected, log(trees));
    }

    @Test
    public void test() {
        test (new int[][] {{0, 0}, {10, 0}, {10, 10}, {0, 10}, {5, 5}},
              new int[] {0, 0, 0, 0, 1});
        test (new int[][] {{0, 0}, {5, 0}, {10, 0}, {0, 5}, {5, 5}, {10, 5},
                           {0, 10}, {5, 10}, {10, 10}},
              new int[] {0, 0, 0, 0, 3, 0, 0, 0, 0});
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
            out.format("Case #%d:\n", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int[][] trees = new int[N][2];
        for (int i = 0; i < N; i++) {
            trees[i] = new int[] {in.nextInt(), in.nextInt()};
        }
        for (int i : log(trees)) {
            out.println(i);
        }
    }
}
