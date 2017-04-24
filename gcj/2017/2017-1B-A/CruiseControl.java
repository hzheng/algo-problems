import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/8294486/dashboard#s=p0
// Round 1B 2017: Problem A - Steed 2: Cruise Control
//
// Annie is riding her horse to the east along a road that runs west to east. She is
// currently at kilometer 0 of the road, and her destination is at kilometer D; kilometers
// along the road are numbered from west to east. There are N other horses traveling east on
// the same road; all of them are currently between Annie's horse and her destination. The
// i-th of these horses is initially at kilometer Ki and is traveling at its maximum speed of
// Si kilometers per hour. A horse H1 will not pass another horse H2 that started off ahead of H1.
// Horses (other than Annie's) travel at their maximum speeds, except that whenever a horse H1
// catches up to another slower horse H2, H1 reduces its speed to match the speed of H2.
// Annie's horse does not have a maximum speed and can travel at any speed that Annie chooses,
// as long as it does not pass another horse. To ensure a smooth ride for her and her horse,
// Annie wants to choose a single constant speed for her horse such that her horse will not pass any
// other horses. What is the maximum such speed that she can choose?
// Input
// The first line of the input gives the number of test cases, T; T test cases follow. Each test
// case begins with two integers D and N: the destination position of all of the horses and the
// number of other horses on the road. Then, N lines follow. The i-th of those lines has two integers
// Ki and Si: the initial position and maximum speed of the i-th of the other horses on the road.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number and y
// is the maximum constant speed that Annie can use without colliding with other horses. y will be
// considered correct if it is within an absolute or relative error of 10^-6 of the correct answer.
// Limits
// 1 ≤ T ≤ 100.
// 0 < Ki < D ≤ 109, for all i.
// Ki ≠ Kj, for all i ≠ j. (No two horses start in the same position.)
// 1 ≤ Si ≤ 10000.
// Small dataset
// 1 ≤ N ≤ 2.
// Large dataset
// 1 ≤ N ≤ 1000.
public class CruiseControl {
    public static double maxSpeed(int D, int N, int[][] KS) {
        double maxTime = 0;
        for (int[] ks : KS) {
            maxTime = Math.max(maxTime, ((double)D - ks[0]) / ks[1]);
        }
        return D / maxTime;
    }

    void test(int D, int N, int[][] KS, double expected) {
        assertEquals(expected, maxSpeed(D, N, KS), 1e-6);
    }

    @Test
    public void test() {
        test(2525, 1, new int[][] {{2400, 5}}, 101.0);
        test(300, 2, new int[][] {{120, 60}, {60, 90}}, 100.0);
        test(100, 2, new int[][] {{80, 100}, {70, 10}}, 33.333333);
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
        int D = in.nextInt();
        int N = in.nextInt();
        int[][] KS = new int[N][2];
        for (int i = 0; i < N; i++) {
            KS[i][0] = in.nextInt();
            KS[i][1] = in.nextInt();
        }
        out.printf("%.6f%n", maxSpeed(D, N, KS));
    }
}
