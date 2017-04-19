import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/4304486/dashboard#s=p1
// Round 1A 2016: Problem B - Rank and File
//
// When Sergeant Argus's army assembles for drilling, they stand in the shape of an
// N by N square grid, with exactly one soldier in each cell.
// Argus requires that: Within every row of the grid, the soldiers' heights must be
// in strictly increasing order, from left to right. Within every column of the grid,
// the soldiers' heights must be in strictly increasing order, from top to bottom.
// Argus has asked you to make a report consisting of 2*N lists of the soldiers' heights:
// one representing each row (in left-to-right order) and column (in top-to-bottom order).
// You wrote each list on a separate piece of paper. However, you dropped all of the
// pieces of paper, and the wind blew one away before you could recover it!
// Can you figure out what the missing list is?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with an integer N, followed by 2*N-1 lines of N integers
// each, representing the lists you have, as described in the statement. It is guaranteed
// that these lists represent all but one of the rows and columns from a valid grid.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// and y is a list of N integers in strictly increasing order, representing the missing list.
// Limits: 1 ≤ T ≤ 50; 1 ≤ all heights ≤ 2500.
// The integers on each line will be in strictly increasing order.
// It is guaranteed that a unique valid answer exists.
// Small dataset
// 2 ≤ N ≤ 10.
// Large dataset
// 2 ≤ N ≤ 50.
public class RankAndFile {
    public static List<Integer> findMissing(int[][] lists) {
        Map<Integer, Integer> freqs = new HashMap<>();
        for (int[] list : lists) {
            for (int h : list) {
                freqs.put(h, freqs.getOrDefault(h, 0) + 1);
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int h : freqs.keySet()) {
            if (freqs.get(h) % 2 == 1) { // all missing must be odd since they are different
                res.add(h);
            }
        }
        Collections.sort(res);
        return res;
    }

    public static List<Integer> findMissing2(int[][] lists) {
        int[] flags = new int[2500 + 1];
        for (int[] list : lists) {
            for (int h : list) {
                flags[h] ^= 1;
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < flags.length; i++) {
            if (flags[i] == 1) {
                res.add(i);
            }
        }
        return res;
    }

    void test(int[][] lists, Integer[] expected) {
        assertEquals(Arrays.asList(expected), findMissing(lists));
        assertEquals(Arrays.asList(expected), findMissing2(lists));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2, 3}, {2, 3, 5}, {3, 5, 6}, {2, 3, 4}, {1, 2, 3}},
             new Integer[] {3, 4, 6});
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
        int n = in.nextInt();
        int[][] lists = new int[2 * n - 1][n];
        for (int i = 0; i < 2 * n - 1; i++) {
            lists[i] = new int[n];
            for (int j = 0; j < n; j++) {
                lists[i][j] = in.nextInt();
            }
        }
        for (int i : findMissing2(lists)) {
            out.print(i + " ");
        }
        out.println();
    }
}
