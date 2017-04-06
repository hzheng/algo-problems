import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/11254486/dashboard#s=p1
// Round 1B 2016: Problem B - Close Match
//
// Can you fill in all of the missing digits in a way that minimizes the absolute
// difference between the scores? If there is more than one way to attain the minimum
// absolute difference, choose the way that minimizes the first score. If there is
// more than one way to attain the minimum absolute difference while also minimizing the
// first score, choose the way that minimizes the second score.
// Input
// The first line of the input gives the number of test cases, T. T cases follow. Each
// case consists of one line with two non-empty strings C and J of the same length,
// composed only of decimal digits and question marks, representing the score as you see
// it for the Coders and the Jammers, respectively. There will be at least one question
// mark in each test case.
// Output
// For each test case, output one line containing Case #x: c j, where x is the test case
// number, c is C with the question marks replaced by digits, and j is J with the
// question marks replaced by digits, such that the absolute difference between the
// integers represented by c and j is minimized. If there are multiple solutions with
// the same absolute difference, use the one in which c is minimized; if there are
// multiple solutions with the same absolute difference and the same value of c,
// use the one in which j is minimized.
// Limits
// 1 ≤ T ≤ 200.
// C and J have the same length.
// Small dataset
// 1 ≤ the length of C and J ≤ 3.
// Large dataset
// 1 ≤ the length of C and J ≤ 18.
public class CloseMatch {
    // Greedy + Recursion + DFS + Backtracking
    // time complexity: O(N ^ 2), space complexity: O(N)
    public static String closeMatch(String C, String J) {
        char[] cs1 = C.toCharArray();
        char[] cs2 = J.toCharArray();
        long[] res = {Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE};
        match(cs1, cs2, 0, 0, res);
        int n = cs1.length;
        return String.format("%0" + n + "d %0" + n + "d", res[1], res[2]);
    }

    private static void match(char[] cs1, char[] cs2, int start, int diff, long[] res) {
        if (start == cs1.length) {
            long n1 = Long.valueOf(String.valueOf(cs1));
            long n2 = Long.valueOf(String.valueOf(cs2));
            if (Math.abs(n1 - n2) < res[0] || Math.abs(n1 - n2) == res[0]
                && (n1 < res[1] || n1 == res[1] && n2 < res[2])) {
                res[0] = Math.abs(n1 - n2);
                res[1] = n1;
                res[2] = n2;
            }
            return;
        }

        char c1 = cs1[start];
        char c2 = cs2[start];
        if (c1 != '?' && c2 != '?') {
            match(cs1, cs2, start + 1, (diff == 0) ? c1 - c2 : diff, res);
            return;
        }
        if (diff == 0) {
            if (c1 != '?') {
                for (int c = c1 - 1; c <= c1 + 1; c++) {
                    if (Character.isDigit(c)) {
                        cs2[start] = (char)c;
                        match(cs1, cs2, start + 1, c1 - c, res);
                    }
                }
            } else if (c2 != '?') {
                for (int c = c2 - 1; c <= c2 + 1; c++) {
                    if (Character.isDigit(c)) {
                        cs1[start] = (char)c;
                        match(cs1, cs2, start + 1, c - c2, res);
                    }
                }
            } else {
                cs1[start] = '0';
                cs2[start] = '1';
                match(cs1, cs2, start + 1, -1, res);

                cs1[start] = '1';
                cs2[start] = '0';
                match(cs1, cs2, start + 1, 1, res);

                cs1[start] = cs2[start] = '0';
            }
        } else {
            if (c1 == '?') {
                cs1[start] = diff > 0 ? '0' : '9';
            }
            if (c2 == '?') {
                cs2[start] = diff > 0 ? '9' : '0';
            }
        }
        match(cs1, cs2, start + 1, diff, res);
        cs1[start] = c1;
        cs2[start] = c2;
    }

    // Greedy
    // time complexity: O(N ^ 2), space complexity: O(N)
    public static String closeMatch2(String C, String J) {
        char[] cs1 = C.toCharArray();
        char[] cs2 = J.toCharArray();
        int n = cs1.length;
        long[] res = {Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE};
        for (int i = 0; i < n; i++) {
            char c1 = cs1[i];
            char c2 = cs2[i];
            if (c1 != '?' && c2 != '?') {
                if (c1 == c2) continue;

                fill(cs1, cs2, c1 > c2, i);
                break;
            }
            if (c1 != '?') {
                if (c1 != '9') {
                    tryMin(cs1, cs2, i, c1, (char)(c1 + 1), res);
                }
                if (c1 != '0') {
                    tryMin(cs1, cs2, i, c1, (char)(c1 - 1), res);
                }
                cs2[i] = c1;
            } else if (c2 != '?') {
                if (c2 != '9') {
                    tryMin(cs1, cs2, i, (char)(c2 + 1), c2, res);
                }
                if (c2 != '0') {
                    tryMin(cs1, cs2, i, (char)(c2 - 1), c2, res);
                }
                cs1[i] = c2;
            } else {
                tryMin(cs1, cs2, i, '0', '1', res);
                tryMin(cs1, cs2, i, '1', '0', res);
                cs1[i] = cs2[i] = '0';
            }
        }
        compareMin(cs1, cs2, res);
        return String.format("%0" + n + "d %0" + n + "d", res[1], res[2]);
    }

    private static void tryMin(char[] cs1, char[] cs2, int i, char c1, char c2, long[] res) {
        char[] tmp1 = cs1.clone();
        char[] tmp2 = cs2.clone();
        tmp1[i] = c1;
        tmp2[i] = c2;
        fill(tmp1, tmp2, c1 > c2, i);
        compareMin(tmp1, tmp2, res);
    }

    private static void compareMin(char[] cs1, char[] cs2, long[] res) {
        long n1 = Long.valueOf(String.valueOf(cs1));
        long n2 = Long.valueOf(String.valueOf(cs2));
        if (Math.abs(n1 - n2) < res[0] || Math.abs(n1 - n2) == res[0]
            && (n1 < res[1] || n1 == res[1] && n2 < res[2])) {
            res[0] = Math.abs(n1 - n2);
            res[1] = n1;
            res[2] = n2;
        }
    }

    private static void fill(char[] cs1, char[] cs2, boolean max, int start) {
        for (int i = start; i < cs1.length; i++) {
            if (cs1[i] == '?') {
                cs1[i] = max ? '0' : '9';
            }
            if (cs2[i] == '?') {
                cs2[i] = max ? '9' : '0';
            }
        }
    }

    // TODO: O(N) solution

    void test(String s1, String s2, String expected) {
        assertEquals(expected, closeMatch(s1, s2));
        assertEquals(expected, closeMatch2(s1, s2));
    }

    @Test
    public void test() {
        test("?", "?", "0 0");
        test("?5", "?0", "05 00");
        test("?7", "?0", "07 10");
        test("99", "?0", "99 90");
        test("1?", "2?", "19 20");
        test("?92", "?2?", "092 120");
        test("?0?", "?5?", "009 050");
        test("??9", "6?1", "599 601");
        test("?2?", "??3", "023 023");
        test("8?7", "??0", "807 810");
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
        out.println(closeMatch2(in.next(), in.next()));
    }
}
