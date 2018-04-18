import java.util.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4314486/dashboard#s=p1
// Round 1C 2016: Problem B - Slides!
//
// There are buildings in a hilly area which are numbered from 1 to B. The CEO 
// wants to build a set of slides between buildings that she can use to travel 
// from her office in building 1 to her favorite cafe in building B. Slides can 
// start in any building and end in any other building, and can go in either 
// direction. The exception is that no slides are allowed to originate in
// building B. The design must ensure that the CEO has exactly M different ways 
// to travel from building 1 to building B using the new slides. A way is a 
// sequence of buildings that starts with building 1, ends with building B, and
// has the property that for each pair of consecutive buildings x and y in the 
// sequence, a slide exists from x to y. Can you come up with any set of one or 
// more slides that satisfies the requirements?
// Input
// The first line of the input gives the number of test cases, T. T lines 
// follow; each consists of 1 line with 2 integers B and M, as described above.
// Output
// For each test case, output one line containing Case #x: y, where x is the 
// test case number (starting from 1) and y is the word POSSIBLE or IMPOSSIBLE,
// depending on whether the CEO's requirements can be fulfilled or not. If it
// is possible, output an additional B lines containing B characters each, 
// representing a matrix describing a valid way to build slides according to the
// requirements. The j-th character of the i-th of these lines (with both i and 
// j counting starting from 1) should be 1 if a slide should be built going from
// building i to building j, and 0 otherwise. The i-th character of the i-th 
// line should always be 0, and every character of the last line should be 0.
// If multiple solutions are possible, you may output any of them.
// Limits
// 1 ≤ T ≤ 100.
// Small dataset
// 2 ≤ B ≤ 6.
// 1 ≤ M ≤ 20.
// Large dataset
// 2 ≤ B ≤ 50.
// 1 ≤ M ≤ 10^18.
public class Slides {
    public static int[][] buildSlides(int B, long M) {
        int[][] res = new int[B][B];
        res[0][B - 1] = 1;
        if (M == 1) return res;

        String binary = Long.toBinaryString(M - 1);
        if (binary.length() > B - 2) return null;

        for (int i = B - 2 - binary.length(); i > 0; i--) {
            binary = "0" + binary;
        }
        for (int i = 0; i < B - 1; i++) {
            for (int j = 0; j < B - 1; j++) {
                res[i][j] = (i < j) ? 1 : 0;
            }
            res[i][B - 1] = (i > 0) ? binary.charAt(B - 2 - i) - '0' : 1;
        }
        return res;
    }

    public static int[][] buildSlides2(int B, long M) {
        long max = 1L << (B - 2); // don't forget L!
        if (M > max) return null;

        int[][] res = new int[B][B];
        for (int i = 1; i < B; i++) {
            for (int j = i + 1; j < B; j++) {
                res[i][j] = 1;
            }
        }
        String binary = Long.toBinaryString((M == max) ? M - 1 : M);
        binary = String.format("%" + (B - 1) + "s", binary).replace(' ', '0');
        // for (int i = B - 2 - binary.length(); i > 0; i--) {
        //     binary = "0" + binary;
        // }
        for (int j = 0; j < B - 1; j++) {
            res[0][j] = binary.charAt(j) - '0';
        }
        res[0][B - 1] = (M == max) ? 1 : 0;
        return res;
    }

    void test(int B, long M, String... expected) {
        int[][] res = buildSlides2(B, M);
        if (expected == null) {
            assertNull(res);
            return;
        }
        Arrays.stream(expected).map(a -> a.toCharArray());
        int[][] expectedArray = new int[expected.length][expected.length];
        int i = 0;
        for (String s : expected) {
            int j = 0;
            for (char c : s.toCharArray()) {
                expectedArray[i][j++] = c - '0';
            }
            i++;
        }
        assertArrayEquals(expectedArray, res);
    }

    @Test
    public void test() {
        test(2, 1, "01", "00");
        test(5, 7, "01110", "00111", "00011", "00001", "00000");
        test(5, 8, "01111", "00111", "00011", "00001", "00000");
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
        int B = in.nextInt();
        long M = in.nextLong();
        int[][] res = buildSlides2(B, M);
        if (res == null) {
            out.println("IMPOSSIBLE");
        } else {
            out.println("POSSIBLE");
            for (int[] slide : res) {
                for (int built : slide) {
                    out.print(built);
                }
                out.println();
            }
        }
    }
}
