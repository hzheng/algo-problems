import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/3264486/dashboard#s=p1
// Qualification Round 2017: Problem B - Tidy Numbers
//
// Some integers, when written in base 10 with no leading zeroes, have their digits
// sorted in non-decreasing order, are called tidy. From 1 to N. What was the last
// tidy number?
// Input
// The first line of the input gives the number of test cases, T. T lines follow.
// Each line describes a test case with a single integer N, the last number counted by Tatiana.
// Output
// For each test case, output one line containing Case #x: y, where x is the test
// case number and y is the last tidy number counted by Tatiana.
// Limits
// 1 ≤ T ≤ 100.
// Small dataset
// 1 ≤ N ≤ 1000.
// Large dataset
// 1 ≤ N ≤ 10 ^ 18.
public class TidyNumbers {
    // Greedy
    // time complexity: O(log(N))
    public static long tidy(long n) {
        char[] chars = String.valueOf(n).toCharArray();
        int len = chars.length;
        int i = 1;
        for (; i < len; i++) {
            if (chars[i] < chars[i - 1]) break;
        }
        if (i == len) return n;

        for (--i; i >= 0; i--) {
            char c = --chars[i];
            if (i == 0 || c >= chars[i - 1]) break;
        }
        Arrays.fill(chars, i + 1, len, '9');
        return Long.valueOf(String.valueOf(chars));
    }

    // Greedy
    // time complexity: O(log(N))
    public static long tidy2(long n) {
        char[] chars = String.valueOf(n).toCharArray();
        int len = chars.length;
        int i = 1;
        int j = 0;
        for (int diff;; i++) {
            if (i == len) return n;

            if ((diff = chars[i] - chars[i - 1]) > 0) {
                j = i;
            } else if (diff < 0) break;
        }
        chars[j]--;
        Arrays.fill(chars, j + 1, len, '9');
        return Long.valueOf(String.valueOf(chars));
    }

    // time complexity: O(log(N) ^ 2)
    public static long tidy3(long n) {
        String s = String.valueOf(n);
        long max = 0;
        for (int prefix = 0, len = s.length(); prefix < len; prefix++) {
            char[] chars = s.toCharArray();
            middle : for (char digit = '0'; digit <= '9'; digit++) {
                chars[prefix] = digit;
                for (int i = 0; i < prefix; i++) {
                    if (chars[i] > chars[i + 1]) continue middle;
                }
                Arrays.fill(chars, prefix + 1, len, '9');
                try {
                    long num = Long.valueOf(String.valueOf(chars));
                    if (num <= n && num > max) {
                        max = num;
                    }
                } catch (NumberFormatException e) {}
            }
        }
        return max;
    }

    // Combinatorics + Recursion
    public static long tidy4(long n) {
        // For a fixed length L, the number of tidy numbers is the number of ways
        // to put 8 balls in L+1 bins.
        long[] res = new long[1];
        enumerate(n, "0", 9, String.valueOf(n).length(), res);
        return res[0];
    }

    private static void enumerate(long n, String s, int increase, int digitsLeft, long[] res) {
        if (digitsLeft > 0) {
            char last = s.charAt(s.length() - 1);
            for (char c = last; c <= last + increase; c++) {
                enumerate(n, s + c, last + increase - c, digitsLeft - 1, res);
            }
        } else {
            try {
                long num = Long.valueOf(s);
                if (num <= n && num > res[0]) {
                    res[0] = num;
                }
            } catch (NumberFormatException e) {}
        }
    }

    // Binary Search
    // time complexity: O(log(N) ^ 2)
    public static long tidy5(long n) {
        long low = 0;
        for (long high = n; low + 1 < high; ) {
            long mid = (low + high) >>> 1;
            long tidy = minTidy(mid);
            if (tidy == n) return n;

            if (tidy > n) {
                high = mid - 1;
            } else {
                low = mid;
            }
        }
        return minTidy(low + 1) <= n ? low + 1 : low;
    }

    private static long minTidy(long n) {
        char[] chars = String.valueOf(n).toCharArray();
        int len = chars.length;
        int i = 1;
        for (; i < len; i++) {
            if (chars[i] < chars[i - 1]) break;
        }
        if (i == len) return n;

        for (int j = i; j < len; j++) {
            chars[j] = chars[i - 1];
        }
        try {
            return Long.valueOf(String.valueOf(chars));
        } catch (NumberFormatException e) {
            return Long.MAX_VALUE;
        }
    }

    // Recursion
    public static long tidy6(long n) {
        return tidy(n, 0, 1);
    }

    private static long tidy(long n, long cur, int lastDigit) {
        if (cur > n) return 0;

        long res = cur;
        for (; lastDigit <= 9; lastDigit++) {
            res = Math.max(res, tidy(n, cur * 10 + lastDigit, lastDigit));
        }
        return res;
    }

    void test(long n, long expected) {
        assertEquals(expected, tidy(n));
        assertEquals(expected, tidy2(n));
        assertEquals(expected, tidy3(n));
        assertEquals(expected, tidy4(n));
        assertEquals(expected, tidy5(n));
        assertEquals(expected, tidy6(n));
    }

    @Test
    public void test() {
        test(7, 7);
        test(99, 99);
        test(998, 899);
        test(1243, 1239);
        test(1332, 1299);
        test(132, 129);
        test(128, 128);
        test(1000, 999);
        test(1939, 1899);
        test(2221, 1999);
        test(111111111111111111L, 111111111111111111L);
        test(111111111111111110L, 99999999999999999L);
        test(1000000000000000000L, 999999999999999999L);
        // test(9223372036854775807L, 8999999999999999999L); // > 10 ^ 18
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
        out.println(tidy2(in.nextLong()));
    }
}
