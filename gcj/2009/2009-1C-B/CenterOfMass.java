import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/189252/dashboard#s=p1
// Round 1C 2009: Problem B - Center of Mass
//
// You are studying a swarm of N fireflies. Each firefly is moving in a straight line
// at a constant speed. You are standing at position (0, 0, 0). Each firefly has the
// same mass, and you want to know how close the center of the swarm will get to the origin.
// You know the position and velocity of each firefly at t = 0, and are only interested in t ≥ 0.
// The fireflies have constant velocity, and may pass freely through all of space, including
// each other and you. Let M(t) be the location of the center of mass of the N fireflies
// at time t. Let d(t) be the distance between your position and M(t) at time t. Find the
// minimum value of d(t), dmin, and the earliest time when d(t) = dmin, tmin.
// Input
// The first line of input contains a single integer T, the number of test cases. Each test
// case starts with a line that contains an integer N, the number of fireflies, followed
// by N lines of the form x y z vx vy vz
// Each of these lines describes one firefly: (x, y, z) is its initial position at time t = 0,
// and (vx, vy, vz) is its velocity.
// Output
// For each test case, output
// Case #X: dmin tmin, where X is the test case number.
// Limits
// All the numbers in the input will be integers.
// 1 ≤ T ≤ 100
// The values of x, y, z, vx, vy and vz will be between -5000 and 5000, inclusive.
// Small dataset
// 3 ≤ N ≤ 10
// Large dataset
// 3 ≤ N ≤ 500
public class CenterOfMass {
    // Math(Dot Product)
    public static double[] center(int[][] pos, int[][] velocity) {
        int n = pos.length;
        double px = 0;
        double py = 0;
        double pz = 0;
        for (int[] p : pos) {
            px += p[0];
            py += p[1];
            pz += p[2];
        }
        px /= n;
        py /= n;
        pz /= n;
        double d2 = px * px + py * py + pz * pz;
        double d = Math.sqrt(d2);
        double vx = 0;
        double vy = 0;
        double vz = 0;
        for (int[] v : velocity) {
            vx += v[0];
            vy += v[1];
            vz += v[2];
        }
        vx /= n;
        vy /= n;
        vz /= n;
        double v = Math.sqrt(vx * vx + vy * vy + vz * vz);
        if (v < 1E-8) return new double[] {d, 0};

        double projection = -(px * vx + py * vy + pz * vz) / v;
        if (projection < 0) return new double[] {d, 0};

        return new double[] {Math.sqrt(Math.abs(d2 - projection * projection)), projection / v};
        // d2 - projection ^ 2 could be negative when it's actually 0 due to precision problem
    }

    // Ternary search
    public static double[] center2(int[][] pos, int[][] v) {
        int n = pos.length;
        double d0 = distance(pos, v, 0);
        double vx = 0;
        double vy = 0;
        double vz = 0;
        for (int[] w : v) {
            vx += w[0];
            vy += w[1];
            vz += w[2];
        }
        if (vx == 0 && vy == 0 && vz == 0) return new double[]{Math.sqrt(d0) / n, 0};

        double precision = 1E-8;
        double t = 2; // could be long, but cannot be int!
        for (; distance(pos, v, t) < d0; t = t * t) {}
        double minT = ternarySearch(pos, v, 0, t, precision);
        return new double[] {Math.sqrt(distance(pos, v, minT)) / n, minT};
    }

    private static double distance(int[][] pos, int[][] velocity, double t) {
        double px = 0;
        double py = 0;
        double pz = 0;
        for (int[] p : pos) {
            px += p[0];
            py += p[1];
            pz += p[2];
        }
        for (int[] v : velocity) {
            px += v[0] * t;
            py += v[1] * t;
            pz += v[2] * t;
        }
        return px * px + py * py + pz * pz;
    }

    private static double ternarySearch(int[][] pos, int[][] v, double left, double right, double precision) {
        while (true) {
            if (Math.abs(right - left) < precision) return (left + right) / 2;

            double leftThird = left + (right - left) / 3;
            double rightThird = right - (right - left) / 3;
            if (distance(pos, v, leftThird) < distance(pos, v, rightThird)) {
                right = rightThird;
            } else {
                left = leftThird;
            }
        }
    }

    // Math(Quadratic Function's min point)
    public static double[] center3(int[][] pos, int[][] velocity) {
        int n = pos.length;
        long px = 0;
        long py = 0;
        long pz = 0;
        for (int[] p : pos) {
            px += p[0];
            py += p[1];
            pz += p[2];
        }
        long vx = 0;
        long vy = 0;
        long vz = 0;
        for (int[] v : velocity) {
            vx += v[0];
            vy += v[1];
            vz += v[2];
        }
        long a = vx * vx + vy * vy + vz * vz;
        long b = 2 * (px * vx + py * vy + pz * vz);
        long c = px * px + py * py + pz * pz;
        double t = 0;
        if (a != 0) {
            t = -b / (2.0 * a);
        }
        if (t < 0) {
            t = 0;
        }
        return new double[]{Math.sqrt((a * t * t + b * t + c)) / n, t};
    }

    void test(int[][] pos, int[][] v, double ... expected) {
        assertArrayEquals(expected, center(pos, v), 10E-5);
        assertArrayEquals(expected, center2(pos, v), 10E-5);
        assertArrayEquals(expected, center3(pos, v), 10E-5);
    }

    @Test
    public void test() {
        test(new int[][] {{3, 0, -4}, {-3, -2, -1}, {-3, -1, 2}},
             new int[][] {{0, 0, 3}, {3, 0, 0}, {0, 3, 0}},
             0.0, 1.0);
        test(new int[][] {{-5, 0, 0}, {-7, 0, 0}, {-6, 3, 0}},
             new int[][] {{1, 0, 0}, {1, 0, 0}, {1, 0, 0}},
             1.0, 6.0);
        test(new int[][] {{1, 2, 3}, {3, 2, 1}, {1, 0, 0}, {0, 10, 0}},
             new int[][] {{1, 2, 3}, {3, 2, 1}, {0, 0, -1}, {0, -10, -1}},
             3.36340601, 1.00);
        test(new int[][] {{-2748, -625, -4002}, {3393, -957, 2919}, {2955, -2018, 2883}},
             new int[][] {{3984, -2863, -2490}, { -3536, 4543, -144}, {-448, -1680, 2634}},
             1800, 0);
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
        int n = in.nextInt();
        int[][] pos = new int[n][3];
        int[][] v = new int[n][3];
        for (int i = 0; i < n; i++) {
            pos[i][0] = in.nextInt();
            pos[i][1] = in.nextInt();
            pos[i][2] = in.nextInt();
            v[i][0] = in.nextInt();
            v[i][1] = in.nextInt();
            v[i][2] = in.nextInt();
        }
        double[] res = center3(pos, v);
        out.printf("%.5f %.5f%n", res[0], res[1]);
        // out.println(res[0] + " " + res[1]);
    }
}
