import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/3274486/dashboard#s=p0
// Round 1C 2017: Problem A - Ample Syrup
//
// The chef currently has N pancakes available, where N ≥ K. Each pancake is a cylinder,
// and different pancakes may have different radii and heights. You must choose K out of
// the N available pancakes and arrange those K pancakes in a stack on a plate as follows.
// First, take the pancake that has the largest radius, and lay it on the plate on one of
// its circular faces. Then, take the remaining pancake with the next largest radius and
// lay it on top of that pancake, and so on, until all K pancakes are in the stack and
// the centers of the circular faces are aligned in a line perpendicular to the plate.
// It is best to maximize the total amount of exposed pancake surface area in the stack.
// Any part of a pancake that is not touching part of another pancake or the plate is
// considered to be exposed. If you choose the K pancakes optimally, what is the largest
// total exposed pancake surface area you can achieve?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each begins with one line with two integers N and K: the total number of available
// pancakes, and the size of the stack that the diner has ordered. Then, N more lines
// follow. Each contains two integers Ri and Hi: the radius and height of the i-th pancake.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// and y is the maximum possible total exposed pancake surface area, in millimeters squared.
// y will be considered correct if it is within an absolute or relative error of 10 ^ -6 of
// the correct answer.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ K ≤ N.
// 1 ≤ Ri ≤ 106, for all i.
// 1 ≤ Hi ≤ 106, for all i.
// Small dataset
// 1 ≤ N ≤ 10.
// Large dataset
// 1 ≤ N ≤ 1000.
public class AmpleSyrup {
    // time complexity: O(N ^ 2 * log(N))
    public static double maxSyrup(int N, int K, int[][] cakes) {
        Arrays.sort(cakes, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] != b[0]) ? a[0] - b[0] : a[1] - b[1];
            }
        });
        long max = 0;
        for (int i = N - 1; i >= K - 1; i--) {
            max = Math.max(max, area(N, K, cakes, i));
        }
        return max * Math.PI;
    }

    private static long area(int N, int K, int[][] cakes, int index) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                long diff = (((long)b[0]) * b[1] - ((long)a[0]) * a[1]);
                return (diff > 0) ? 1 : (diff < 0 ? -1 : 0);
            }
        });
        for (int i = index - 1; i >= 0; i--) {
            pq.offer(cakes[i]);
        }
        int[] base = cakes[index];
        long res = ((long)base[0]) * base[0] + 2L * base[0] * base[1];
        for (int i = K - 1; i > 0; i--) {
            int[] cur = pq.poll();
            res += 2L * cur[0] * cur[1];
        }
        return res;
    }

    // time complexity: O(N * log(N))
    public static double maxSyrup2(int N, int K, int[][] cakes) {
        Arrays.sort(cakes, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                long diff = (((long)b[0]) * b[1] - ((long)a[0]) * a[1]);
                return (diff > 0) ? 1 : (diff < 0 ? -1 : b[0] - a[0]);
            }
        });
        long sideArea = 0;
        long maxR = 0;
        for (int i = 0; i < K; i++) {
            sideArea += 2L * cakes[i][0] * cakes[i][1];
            maxR = Math.max(maxR, cakes[i][0]);
        }
        long maxArea = sideArea + maxR * maxR;
        sideArea -= 2L * cakes[K - 1][0] * cakes[K - 1][1];
        for (int i = K; i < N; i++) {
            long r = cakes[i][0];
            if (r > maxR) { // actually, this condition can be omitted
                maxArea = Math.max(maxArea, sideArea + r * r + 2 * r * cakes[i][1]);
            }
        }
        return maxArea * Math.PI;
    }

    void test(int N, int K, int[][] cakes, double expected) {
        assertEquals(expected, maxSyrup(N, K, cakes), 1E-06);
        assertEquals(expected, maxSyrup2(N, K, cakes), 1E-06);
    }

    @Test
    public void test() {
        test(2, 2, new int[][] {{100, 20}, {200, 10}}, 150796.447372310);
        test(4, 2, new int[][] {{9, 3}, {7, 1}, {10, 1}, {8, 4}}, 625.176938064);
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
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
        int[][] cakes = new int[N][2];
        for (int i = 0; i < N; i++) {
            cakes[i] = new int[] {in.nextInt(), in.nextInt()};
        }
        out.printf("%.6f%n", maxSyrup2(N, K, cakes));
    }
}
