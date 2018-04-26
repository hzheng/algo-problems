import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4244486/dashboard#s=p2
// Round 1C 2015: Problem C - Less Money, More Problems
//
// The nation you live in has used D different positive integer denominations of
// coin for all transactions. The queen decreed that no more than C coins of any
// one denomination may be used in any one purchase. You are in charge of the
// mint, and you can issue new denominations of coin. You want to make it
// possible for any item of positive value at most V to be purchased under the
// queen's rules. Moreover, you want to introduce as few new denominations as
// possible, and your final combined set of pre-existing and new denominations
// may not have any repeats. What is the smallest number of new denominations?
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow. Each consists of one line with three space-separated values C, D, and
// V, followed by another line with D distinct space-separated values
// representing the preexisting denominations, in ascending order.
// Output
// For each test case, output one line containing "Case #x: y", where x is the
// test case number (starting from 1) and y is the minimum number of new
// denominations required, as described above.
// Limits
// 1 ≤ T ≤ 100.
// Each existing denomination ≤ V.
// Small dataset
// C = 1.
// 1 ≤ D ≤ 5.
// 1 ≤ V ≤ 30.
// Large dataset
// 1 ≤ C ≤ 100.
// 1 ≤ D ≤ 100.
// 1 ≤ V ≤ 10 ^ 9.
public class LessMoneyMoreProblems {
    // time complexity: O(D+log(V)), space complexity: O(1)
    public static int minNew(int coins, int value, int[] denominations) {
        int res = 0;
        long reach = 0;
        for (int i = 0, n = denominations.length; reach < value; ) {
            if (i < n && reach + 1 >= denominations[i]) {
                reach += (long)denominations[i++] * coins;
            } else {
                res++;
                reach += (reach + 1) * coins;
            }
        }
        return res;
    }

    // Queue
    // time complexity: O(D+log(V)), space complexity: O(D)
    public static int minNew2(int coins, int value, int[] denominations) {
        Queue<Integer> queue = new LinkedList<>();
        for (int d : denominations) {
            queue.offer(d);
        }
        int res = 0;
        for (long reach = 0, next = 0; reach < value; reach += next * coins) {
            next = reach + 1;
            if (!queue.isEmpty() && queue.peek() <= next) {
                next = queue.poll();
            } else {
                res++;
            }
        }
        return res;
    }

    void test(int coins, int value, int[] denominations, int expected) {
        assertEquals(expected, minNew(coins, value, denominations));
        assertEquals(expected, minNew2(coins, value, denominations));
    }

    @Test
    public void test() {
        test(1, 3, new int[] {1, 2}, 0);
        test(1, 100, new int[] {1, 5, 10, 25, 50, 100}, 3);
        test(1, 6, new int[] {1, 2, 5}, 1);
        test(2, 3, new int[] {1}, 1);
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
        int C = in.nextInt();
        int D = in.nextInt();
        int V = in.nextInt();
        int[] denominations = new int[D];
        for (int i = 0; i < D; i++) {
            denominations[i] = in.nextInt();
        }
        out.println(minNew(C, V, denominations));
    }
}
