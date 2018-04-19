import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4314486/dashboard#s=p2
// Round 1C 2016: Problem C - Fashion Police
//
// You have brought along J different jackets, P different pairs of pants and S
// different shirts. J ≤ P ≤ S. Every day, you will pick one jacket, one pair of
// pants, and one shirt. You wash all of your garments every night so all of
// your garments are available to use each day. If the Fashion Police officers
// find out that you have worn the exact same outfit twice, you will immediately
// be taken to the Fashion Jail. You will also be taken to Fashion Jail if they
// find out that you have worn the same two-garment combination more than K
// times in total. You will wear one outfit per day. Can you figure out the
// largest possible number of days you can avoid being taken to Fashion Jail and
// produce a list of outfits to use each day?
// Input
// The first line of the input gives the number of test cases, T. T test cases
// follow; each consists of one line with four integers J, P, S, and K.
// Output
// For each test case, output one line containing Case #x: y, where x is the
// test case number (starting from 1) and y is an integer: the maximum number of
// days you will be able to avoid being taken to Fashion Jail. Then output y
// more lines, each of which consists of three integers: the numbers of the
// jacket, pants, and shirt for one day's outfit.
// If multiple answers are possible, you may output any of them.
// Limits
// 1 ≤ T ≤ 100.
// 1 ≤ J ≤ P ≤ S.
// 1 ≤ K ≤ 10.
// Small dataset
// S ≤ 3.
// Large dataset
// S ≤ 10.
public class FashionPolice {
    public static List<int[]> wear(int J, int P, int S, int K) {
        List<int[]> res = new ArrayList<>();
        // maximal possiblities should be J * P * min(S, K)
        if (S <= K) {
            for (int j = 1; j <= J; j++) {
                for (int p = 1; p <= P; p++) {
                    for (int s = 1; s <= S; s++) {
                        res.add(new int[] {j, p, s});
                    }
                }
            }
        } else {
            // e.g. J = 2, P = 3, S = 4, K = 2:
            // layer 1(i.e. J = 1): (*'s row means P, column mean S)
            // **..
            // .**.
            // ..**
            // layer 2(i.e. J = 2): (*'s row means P, column mean S)
            // .**.
            // ..**
            // *..*
            // Also, we can prove that given j and p, choosing (j + p + k) % S
            // will satisfy all condition by pure algebra.
            for (int j = 1; j <= J; j++) {
                for (int p = 1; p <= P; p++) {
                    for (int k = 0; k < K; k++) {
                        res.add(new int[] {j, p, (j + p + k - 2) % S + 1});
                        // or:
                        // res.add(new int[] {j, p, (j + p + k) % S + 1});
                    }
                }
            }
        }
        return res;
    }

    public static List<int[]> wear2(int J, int P, int S, int K) {
        List<int[]> res = new ArrayList<>();
        K = Math.min(S, K);
        for (int j = 0; j < J; j++) {
            for (int p = P * K - 1, s = j; p >= 0; p--, s++) {
                res.add(new int[] {j + 1, p / K + 1, s % S + 1});
            }
        }
        return res;
    }

    void test(int J, int P, int S, int K, int[][] expected) {
        int i = 0;
        for (int[] outfit : wear2(J, P, S, K)) {
            assertArrayEquals(expected[i++], outfit);
        }
    }

    @Test
    public void test() {
        test(1, 1, 1, 10, new int[][] {{1, 1, 1}});
        test (1, 1, 3, 2, new int[][] {{1, 1, 1}, {1, 1, 2}});
        test (1, 2, 3, 1, new int[][] {{1, 1, 1}, {1, 2, 2}});
        // test (1, 2, 3, 2, new int[][] {{1, 1, 2}, {1, 2, 3}, {1, 2, 1}, {1, 1, 1}});
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
            // System.out.println("case "+i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int J = in.nextInt();
        int P = in.nextInt();
        int S = in.nextInt();
        int K = in.nextInt();
        List<int[]> outfits = wear2(J, P, S, K);
        out.println(outfits.size());
        for (int[] outfit : outfits) {
            out.println(outfit[0] + " " + outfit[1] + " " + outfit[2]);
        }
    }
}
