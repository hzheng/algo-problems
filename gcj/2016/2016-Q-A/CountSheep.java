import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/6254486/dashboard#s=p0
// Qualification Round 2016: Problem A - Counting Sheep
//
// Bleatrix Trotter has devised a strategy that helps her fall asleep faster.
// First, she picks a number N. Then she starts naming N, 2 × N, 3 × N, and so on.
// Whenever she names a number, she thinks about all of the digits in that number.
// She keeps track of which digits she has seen at least once so far as part of
// any number she has named. Once she has seen each of the ten digits at least once,
// she will fall asleep.
// Bleatrix must start with N and must always name (i + 1) × N directly after i × N.
// For example, suppose that Bleatrix picks N = 1692. She would count as follows:
// N = 1692. Now she has seen the digits 1, 2, 6, and 9.
// 2N = 3384. Now she has seen the digits 1, 2, 3, 4, 6, 8, and 9.
// 3N = 5076. Now she has seen all ten digits, and falls asleep.
// What is the last number that she will name before falling asleep? If she will
// count forever, print INSOMNIA instead.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with a single integer N, the number Bleatrix has chosen.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// (starting from 1) and y is the last number that Bleatrix will name before falling asleep,
// according to the rules described in the statement.
// Limits
// 1 ≤ T ≤ 100.
// Small dataset: 0 ≤ N ≤ 200.
// Large dataset 0 ≤ N ≤ 10 ^ 6.
public class CountSheep {
    public static int count(int n) {
        if (n == 0) return -1;

        // digit 0: The tenth number is guaranteed to end in 0.
        // digits 1-9: Consider the smallest power of 10 greater than N; call it P.
        // The leftmost digit will take on every possible value from 1-9 as
        // the number increases up to (or past) 9P. No digit can be skipped, because
        // that would require the step between successive numbers to be larger than P.
        boolean[] flags = new boolean[10];
        int counted = 0;
        for (int i = n;; i += n) {
            for (int j = i; j > 0; j /= 10) {
                int digit = j % 10;
                if (!flags[digit]) {
                    flags[digit] = true;
                    if (++counted == 10) return i;
                }
            }
        }
    }

    void test(int n, int expected) {
        assertEquals(expected, count(n));
    }

    @Test
    public void test() {
        test(0, -1);
        test(1, 10);
        test(2, 90);
        test(11, 110);
        test(1692, 5076);
        test(300000, 2700000);
        test(999999, 9999990);
    }

    private static Scanner in = new Scanner(System.in);
    private static PrintStream out = System.out;

    public static void main(String[] args) {
        if (System.getProperty("gcj.submit") == null) {
            org.junit.runner.JUnitCore.main("CountSheep");
            return;
        }

        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in.nextInt());
        }
    }

    private static void printResult(int n) {
        int res = count(n);
        out.println(res >= 0 ? String.valueOf(res) : "INSOMNIA");
    }
}
