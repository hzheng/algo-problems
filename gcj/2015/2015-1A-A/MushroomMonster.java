import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://codejam.withgoogle.com/codejam/contest/4224486/dashboard#s=p0
// Round 1A 2015: Problem A - Mushroom Monster
//
// Kaylin is eating a plate of mushrooms, and Bartholomew is putting more pieces
// on her plate. We'll look at how many pieces of mushroom are on her plate at
// 10-second intervals. Bartholomew could put any non-negative integer number of
// mushroom pieces down at any time, and the only way they can leave the plate
// is by being eaten. Figure out the minimum number of mushrooms that Kaylin
// could have eaten using two different methods of computation:
// Assume Kaylin could eat any number of mushroom pieces at any time.
// Assume that, starting with the first time we look at the plate, Kaylin eats
// mushrooms at a constant rate whenever there are mushrooms on her plate.
// For example, if the input is 10 5 15 5:
// With the first method, Kaylin must have eaten at least 15 mushroom pieces:
// first she eats 5, then 10 more are put on her plate, then she eats another 10.
// There's no way she could have eaten fewer pieces.
// With the second method, Kaylin must have eaten at least 25 mushroom pieces.
// We can determine that she must eat mushrooms at a rate of at least 1 piece
// per second. She starts with 10 pieces on her plate. In the first 10 seconds,
// she eats 10 pieces, and 5 more are put on her plate. In the next 5 seconds,
// she eats 5 pieces, then her plate stays empty for 5 seconds, and then
// Bartholomew puts 15 more pieces on her plate. Then she eats 10 pieces in the
// last 10 seconds.
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each will consist of one line containing a single integer N, followed
// by a line containing N space-separated integers mi; the number of mushrooms
// on Kaylin's plate at the start, and at 10-second intervals.
// Output
// For each test case, output one line containing "Case #x: y z", where x is the
// test case number (starting from 1), y is the minimum number of mushrooms
// Kaylin could have eaten using the first method of computation, and z is the
// minimum number of mushrooms Kaylin could have eaten using the second method
// of computation.
// Limits
// 1 ≤ T ≤ 100.
// Small dataset
// 2 ≤ N ≤ 10.
// 0 ≤ mi ≤ 100.
// Large dataset
// 2 ≤ N ≤ 1000.
// 0 ≤ mi ≤ 10000.
public class MushroomMonster {
    public static int[] eatMushrooms(int[] M) {
        return new int[] {eat1(M), eat2(M)};
    }

    public static int eat1(int[] M) {
        int res = 0;
        for (int i = 1; i < M.length; i++) {
            res += Math.max(M[i - 1] - M[i], 0);
        }
        return res;
    }

    public static int eat2(int[] M) {
        int speed = 0;
        int n = M.length;
        for (int i = 1; i < n; i++) {
            speed = Math.max(speed, M[i - 1] - M[i]);
        }
        int res = 0;
        for (int i = 0; i < n - 1; i++) {
            res += Math.min(M[i], speed);
        }
        return res;
    }

    void test(int[] M, int[] expected) {
        assertArrayEquals(expected, eatMushrooms(M));
    }

    @Test
    public void test() {
        test(new int[] {10, 5, 15, 5}, new int[] {15, 25});
        test(new int[] {100, 100}, new int[] {0, 0});
        test(new int[] {81, 81, 81, 81, 81, 81, 81, 0}, new int[] {81, 567});
        test(new int[] {23, 90, 40, 0, 100, 9}, new int[] {181, 244});
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        PrintStream out = System.out;
        if (args.length == 0) {
            String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
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
        int N = in.nextInt();
        int[] M = new int[N];
        for (int i = 0; i < N; i++) {
            M[i] = in.nextInt();
        }
        out.println(eat1(M) + " " + eat2(M));
    }
}
