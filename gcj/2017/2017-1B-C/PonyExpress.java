import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/8294486/dashboard#s=p2
// Round 1B 2017: Problem C - Pony Express
//
// The Pony Express serves N cities. In each city, there is one horse; each horse travels
// at a certain constant speed and has a maximum total distance it can travel. Rider starts
// off on the starting city's horse. Every time the rider reaches a city, they may continue
// to use their current horse or switch to that city's horse; switching is instantaneous.
// Horses never get a chance to rest, so whenever part of a horse's maximum total distance is
// "used up", it is used up forever! When the rider reaches the destination city, the mail is
// delivered. You have brought a fast computer from the future. A single computer is not enough
// for you to set up an e-mail service and make the Pony Express obsolete, but you can use it
// to make optimal routing plans for the Pony Express. Given all data about routes between cities
// and the horses in each city, and a list of pairs of starting and ending cities, can you quickly
// calculate the minimum time necessary for each delivery?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow. Each test case
// is described as follows: One line with two integers: N, the number of cities with horses, and Q,
// the number of pairs of stops we are interested in. Cities are numbered from 1 to N.
// N lines, each containing two integers Ei, the maximum total distance, the horse in the i-th city
// can go and Si, the constant speed, at which the horse travels. N lines, each containing N integers.
// The j-th integer on the i-th of these lines, Dij, is -1 if there is no direct route from the i-th
// to the j-th city, and the length of that route otherwise. Q lines containing two integers Uk and Vk,
// the starting and destination point, respectively, of the k-th pair of cities we want to investigate.
// Output
// For each test case, output one line containing Case #x: y1 y2 ... yQ, where x is the test case number
// and yk is the minimum time, in hours, to deliver a letter from city Uk to city Vk. Each yk will
// be considered correct if it is within an absolute or relative error of 10 ^ -6 of the correct answer.
// Limits
// 1 ≤ T ≤ 100.
// 2 ≤ N ≤ 100.
// 1 ≤ Ei ≤ 109, for all i.
// 1 ≤ Si ≤ 1000, for all i.
// -1 ≤ Dij ≤ 109, for all i, j.
// Dii = -1, for all i. (There are no direct routes from a city to itself.)
// Dij ≠ 0, for all i, j.
// Uk ≠ Vk, for all k.
// It is guaranteed that the delivery from Uk to Vk can be accomplished with the given horses.
// Ul ≠ Um and/or Vl ≠ Vm, for all different l, m. (No ordered pair of cities to investigate is repeated
// within a test case.)
// Small dataset
// Dij = -1, for all i, j where i + 1 ≠ j. (The cities are in a single line; each route goes from one
// city to the next city in line.)
// Q = 1.
// U1 = 1.
// V1 = N. (The only delivery to calculate is between the first and last cities in the line).
// Large dataset
// 1 ≤ Q ≤ 100.
// 1 ≤ Uk ≤ N, for all k.
// 1 ≤ Vk ≤ N, for all k.
public class PonyExpress {
    // Floyd–Warshall algorithm
    // time complexity: O(N ^ 3)
    public static double[] minTime(int[][] horses, int[][] dist, int[][] points) {
        int N = horses.length;
        for (int i = 0; i < N; ++i) {
            for (int j = 0; j < N; j++) {
                if (dist[i][j] == -1) {
                    dist[i][j] = (i == j) ? 0 : Integer.MAX_VALUE / 2;
                }
            }
        }
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
        double[][] time = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                time[i][j] = (dist[i][j] > horses[i][0])
                             ? Double.MAX_VALUE : dist[i][j] / ((double)horses[i][1]);
            }
        }
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    time[i][j] = Math.min(time[i][j], time[i][k] + time[k][j]);
                }
            }
        }
        double[] res = new double[points.length];
        int i = 0;
        for (int[] point : points) {
            res[i++] = time[point[0] - 1][point[1] - 1];
        }
        return res;
    }

    void test(int[][] horses, int[][] routes, int[][] points, double[] expected) {
        assertArrayEquals(expected, minTime(horses, routes, points), 1E-6);
    }

    @Test
    public void test() {
        test(new int[][] {{2, 3}, {2, 4}, {4, 4}}, new int[][] {{-1, 1, -1}, {-1, -1, 1}, {-1, -1, -1}},
             new int[][] {{1, 3}}, new double[] {0.583333333});
        test(new int[][] {{13, 10}, {1, 1000}, {10, 8}, {5, 5}},
             new int[][] {{-1, 1, -1, -1}, {-1, -1, 1, -1}, {-1, -1, -1, 10}, {-1, -1, -1, -1}},
             new int[][] {{1, 4}}, new double[] {1.2});
        test(new int[][] {{30, 60}, {10, 1000}, {12, 5}, {20, 1}},
             new int[][] {{-1, 10, -1, 31}, {10, -1, 10, -1}, {-1, -1, -1, 10}, {15, 6, -1, -1}},
             new int[][] {{2, 4}, {3, 1}, {3, 2}}, new double[] {0.51, 8.01, 8.0});
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
        int Q = in.nextInt();
        int[][] horses = new int[N][2];
        int[][] dist = new int[N][N];
        int[][] points = new int[Q][2];
        for (int i = 0; i < N; i++) {
            horses[i][0] = in.nextInt();
            horses[i][1] = in.nextInt();
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dist[i][j] = in.nextInt();
            }
        }
        for (int[] point : points) {
            point[0] = in.nextInt();
            point[1] = in.nextInt();
        }
        for (double t : minTime(horses, dist, points)) {
            out.printf("%.6f ", t);
        }
        out.println();
    }
}
