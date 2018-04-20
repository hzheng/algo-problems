import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/8224486/dashboard#s=p0
// Round 1B 2015: Problem A - Counter Culture
//
// In the Counting Poetry Slam, a performer takes the microphone, chooses a 
// number N, and counts aloud from 1 to N. She starts by saying 1, and then
// repeatedly says the number that is 1 greater than the previous number,
// stopping after she has said N. It's your turn to perform, but you find this 
// process tedious, and you want to add a twist to speed it up: sometimes, 
// instead of adding 1 to the previous number, you might reverse the digits of 
// the number (removing any leading zeroes that this creates). You may reverse 
// as many times as you want (or not at all) within a performance.
// The first number you say must be 1; what is the fewest number of numbers you 
// will need to say in order to reach the number N? 
// Input
// The first line of the input gives the number of test cases, T. T lines 
// follow. Each has one integer N, the number you must reach.
// Output
// For each test case, output one line containing "Case #x: y", where x is the 
// test case number (starting from 1) and y is the minimum number of numbers you
// need to say.
// Limits
// 1 ≤ T ≤ 100.
// Small dataset
// 1 ≤ N ≤ 10 ^ 6.
// Large dataset
// 1 ≤ N ≤ 10 ^ 14.
public class CounterCulture {
    public static long count(long N) {
        if (N <= 10) return N;

        int res = 9; // count all 1-digit numbers
        if (N % 10 == 0) { // avoid 0-ending to make reversal possible
            N--;
            res++;
        }
        String s = String.valueOf(N);
        int digits = s.length();
        long power = 1;
        // for each 1 < n < digits, count 10...0 to 99...9
        for (int n = 2; n < digits; n++) {
            if (n % 2 == 0) {
                power *= 10;
                res += power * 2 - 1; // e.g. 1000-1099 and 9901-9999
            } else {
                res += power * 11 - 1; // e.g. 100-109 and 901-999
            }
        }
        // count from 10...0 to N
        StringBuilder reversed = new StringBuilder(s).reverse();
        int rightHalf = Integer.valueOf(reversed.substring((digits + 1) / 2));
        res += rightHalf + ((rightHalf == 1) ? 0 : 1); // swap when necessary
        return res + Integer.valueOf(s.substring(digits / 2));
    }

    void test(long N, long expected) {
        assertEquals(expected, count(N));
    }

    @Test
    public void test() {
        test(1, 1);
        test(10, 10);
        test(19, 19);
        test(23, 15);
        test(100, 29);
        test(200, 129);
        test(201, 32);
        test(1000, 138);
        test(990000, 3425);
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
        out.println(count(in.nextLong()));
    }
}
