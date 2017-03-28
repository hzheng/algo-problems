import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/
// Qualification Round 2016: Problem D - Fractiles
//
// Long ago, the Fractal civilization created artwork consisting of linear rows of tiles.
// They had two types of tile that they could use: gold (G) and lead (L).
// Each piece of Fractal artwork is based on two parameters: an original sequence
// of K tiles, and a complexity C. For a given original sequence, the artwork with
// complexity 1 is just that original sequence, and the artwork with complexity X+1
// consists of the artwork with complexity X, transformed as follows:
// replace each L tile in the complexity X artwork with another copy of the original sequence
// replace each G tile in the complexity X artwork with K G tiles.
// You have just discovered a piece of Fractal artwork, but the tiles are too dirty
// for you to tell what they are made of. Because you are an expert archaeologist
// familiar with the local Fractal culture, you know the values of K and C for the artwork,
// but you do not know the original sequence. Since gold is exciting, you would like
// to know whether there is at least one G tile in the artwork. Your budget allows
// you to hire S graduate students, each of whom can clean one tile of your choice
// (out of the K ^ C tiles in the artwork) to see whether the tile is G or L.
// Is it possible for you to choose a set of no more than S specific tiles to clean,
// such that no matter what the original pattern was, you will be able to know for
// sure whether at least one G tile is present in the artwork? If so, which tiles
// should you clean?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with three integers: K, C, and S.
// Output
// For each test case, output one line containing Case #x: y, where x is the test
// case number (starting from 1) and y is either IMPOSSIBLE if no set of tiles will
// answer your question, or a list of between 1 and S positive integers, which are
// the positions of the tiles that will answer your question. The tile positions
// are numbered from 1 for the leftmost tile to K ^ C for the rightmost tile.
// Limits
// 1 ≤ T ≤ 100. 1 ≤ K ≤ 100. 1 ≤ C ≤ 100. K ^ C ≤ 10 ^ 18.
// Small dataset
// S = K.
// Large dataset
// 1 ≤ S ≤ K.
public class Fractiles {
    public static List<Long> pickTitles(int k, int complexity, int s) {
        List<Long> res = new ArrayList<>();
        for (int i = 0, cur = 1; i < s; i++) {
            long num = 1;
            for (int c = complexity - 1; c >= 0; c--, cur++) {
                num += pow(k, c) * (Math.min(cur, k) - 1);
            }
            res.add(num);
            if (cur > k) return res;
        }
        return null;
    }

    private static long pow(int k, int c) {
        long res = 1;
        for (int i = 1; i <= c; i++) {
            res *= k;
        }
        return res;
    }

    public static List<Long> pickTitles2(int k, int complexity, int s) {
        if (complexity * s < k) return null;

        List<Long> res = new ArrayList<>();
        for (int i = 0; i < k; i += complexity) {
            long num = 0;
            for (int j = i; j < i + complexity; j++) {
                num = num * k + Math.min(j, k - 1);
            }
            res.add(num + 1);
        }
        return res;
    }

    void test(int k, int c, int s, Long[] expected) {
        List<Long> res = pickTitles2(k, c, s);
        if (expected == null) {
            assertNull(res);
        } else {
            assertEquals(Arrays.asList(expected), res);
        }
    }

    @Test
    public void test() {
        test(2, 1, 1, null);
        test(1, 1, 1, new Long[] {1L});
        test(2, 3, 2, new Long[] {4L});
        test(2, 1, 2, new Long[] {1L, 2L});
        test(3, 2, 3, new Long[] {2L, 9L});
    }

    private static PrintStream out = System.out;

    public static void main(String[] args) {
        if (System.getProperty("gcj.submit") == null) {
            org.junit.runner.JUnitCore.main("Fractiles");
            return;
        }

        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in.nextInt(), in.nextInt(), in.nextInt());
        }
    }

    private static void printResult(int k, int c,  int s) {
        List<Long> res = pickTitles2(k, c, s);
        if (res == null) {
            out.println("IMPOSSIBLE");
            return;
        }
        res.forEach(i -> out.print(i + " "));
        out.println();
    }
}
