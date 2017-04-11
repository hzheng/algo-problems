import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/3264486/dashboard#s=p0
// Qualification Round 2017: Problem A - Oversized Pancake Flipper
//
// A new kind of pancake has a happy side and a blank side. The pancakes are cooked in
// a single row. An oversized pancake flipper can flip exactly K consecutive pancakes.
// Given the current state of the pancakes, calculate the minimum number of uses of the
// oversized pancake flipper needed to leave all pancakes happy side up
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with a string S and an integer K. S represents the row of
// pancakes: each of its characters is either + or -.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case
// number and y is either IMPOSSIBLE if there is no way to get all the pancakes happy
// side up, or an integer representing the the minimum number of times you will need
// to use the oversized pancake flipper to do it.
// Limits
// 1 ≤ T ≤ 100.
// Every character in S is either + or -.
// 2 ≤ K ≤ length of S.
// Small dataset
// 2 ≤ length of S ≤ 10.
// Large dataset
// 2 ≤ length of S ≤ 1000.
public class PancakeFlipper {
    // time complexity: O(N ^ 2), space complexity: O(N)
    public static int flip(String S, int K) {
        List<Integer> row = new ArrayList<>(); // alternative -'s # and +'s #
        char[] chars = {'+', '-'};
        for (int i = 0, n = S.length(), prev = 0, cur; prev < n; i ^= 1, prev = cur) {
            cur = S.indexOf(chars[i], prev);
            if (cur < 0) {
                cur = n;
            }
            row.add(cur - prev);
        }
        int res = 0;
        for (int i = 0, len = row.size(); i < len; i++) {
            int cur = row.get(i);
            res += cur / K;
            if (++i >= len) return (cur % K) == 0 ? res : -1;

            if ((cur %= K) == 0) continue;

            int j = i;
            for (;; j++) {
                if (j == len) return -1;
                if ((cur += row.get(j)) >= K) break;
            }
            for (int k = i - 1; k < j - 1; k++) {
                row.set(k, row.get(k + 1));
            }
            row.set(j - 1, row.get(j) - (cur - K));
            row.set(j, cur - K);
            i -= 2;
            res++;
        }
        return res;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    public static int flip2(String S, int K) {
        int res = 0;
        StringBuilder sb = new StringBuilder(S);
        for (int i = 0, n = S.length(); i < n; i++) {
            if (sb.charAt(i) == '+') continue;

            int happyPos = sb.indexOf("+", i + 1);
            if (happyPos < 0) {
                happyPos = n;
            }
            int blanks = happyPos - i;
            res += blanks / K;
            if ((blanks %= K) > 0) {
                for (int j = happyPos; j < happyPos + K - blanks; j++) {
                    if (j == n) return -1;
                    sb.setCharAt(j, sb.charAt(j) == '+' ? '-' : '+');
                }
                res++;
            }
            i = happyPos - 1;
        }
        return res;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    public static int flip3(String S, int K) {
        int res = 0;
        char[] cs = S.toCharArray();
        for (int i = 0, n = cs.length; i < n; i++) {
            if (cs[i] == '+') continue;

            for (int j = i + 1; j < i + K; cs[j++] ^= ('-' ^ '+')) {
                if (j == n) return -1;
            }
            res++;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    public static int flip4(String S, int K) {
        int n = S.length();
        int[] flips = new int[n];
        int res = 0;
        for (int i = 0; i < n; i++) {
            if ((flips[i] % 2 == 0) ^ (S.charAt(i) == '-')) continue;

            res++;
            for (int j = i + 1; j < i + K; flips[j++]++) {
                if (j >= n) return -1;
            }
        }
        return res;
    }

    void test(String s, int k, int expected) {
        assertEquals(expected, flip(s, k));
        assertEquals(expected, flip2(s, k));
        assertEquals(expected, flip3(s, k));
        assertEquals(expected, flip4(s, k));
    }

    @Test
    public void test() {
        test("+--+++", 3, -1);
        test("+-++", 3, -1);
        test("-+---+-", 3, 5);
        test("-++-", 3, 2);
        test("---+-++-", 3, 3);
        test("+-+-+-+-+-", 2, -1);
        test("-----", 3, -1);
        test("------", 3, 2);
        test("--+++-", 3, 3);
        test("--++++-", 3, -1);
        test("--+++++-", 3, -1);
        test("--++++++-", 3, 5);
        test("--++-+---", 4, 3);
        test("+++++", 4, 0);
        test("-+-+-", 4, -1);
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
        int res = flip4(in.next(), in.nextInt());
        out.println(res >= 0 ? String.valueOf(res) : "IMPOSSIBLE");
    }
}
