import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/189252/dashboard#s=p0
// Round 1C 2009: Problem A - All Your Base
//
// Aliens wrote a message in a cryptic language, and next to it they wrote a series of symbols.
// The symbols indicate a number: the number of seconds before war begins!
// We've decided that each symbol indicates one digit, but we aren't sure what each digit
// means or what base the aliens are using. We are sure of three things: the number is
// positive; the aliens will never start a number with a zero; and they aren't using unary.
// Your job is to determine the minimum possible number.
// Input
// The first line of input contains a single integer, T. T test cases follow. Each test
// case is a string on a line by itself. The line will contain only characters in the
// 'a' to 'z' and '0' to '9' ranges, representing the message the aliens left us.
// Output
// For each test case, output a line in the following format:
// Case #X: V
// Where X is the case number and V is the minimum number.
// Limits
// 1 ≤ T ≤ 100
// The answer will never exceed 10 ^ 18
// Small dataset
// 1 ≤ the length of each line < 10
// Large dataset
// 1 ≤ the length of each line < 61
public class AllYourBase {
    public static long minNum(String s) {
        Map<Character, Integer> map = new HashMap<>();
        char[] cs = s.toCharArray();
        map.put(cs[0], 1);
        int digit = 0;
        for (char c : cs) {
            if (!map.containsKey(c)) {
                map.put(c, digit);
                digit = (digit == 0) ? 2 : digit + 1;
            }
        }
        long res = 0;
        int base = Math.max(2, map.size());
        for (char c : cs) {
            res *= base;
            res += map.get(c);
        }
        return res;
    }

    void test(String s, long expected) {
        assertEquals(expected, minNum(s));
    }

    @Test
    public void test() {
        test("11001001", 201);
        test("cats", 75);
        test("zig", 11);
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
        out.println(minNum(in.next()));
    }
}
