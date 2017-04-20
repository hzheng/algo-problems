import java.util.*;
import java.math.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/5304486/dashboard#s=p0
// Round 1A 2017: Problem A - Alphabet Cake
//
// A cake has every child's initial in icing on exactly one cell of the cake. Each
// cell contains at most one initial, and since no two children share the same initial,
// no initial appears more than once on the cake. Each child wants a single rectangular
// piece of cake that has their initial and no other child's initial(s). Can you find
// a way to assign every blank cell of the cake to one child, such that this goal is
// accomplished? It is guaranteed that this is always possible.
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each begins with one line with two integers R and C. Then, there are R more lines of
// C characters each, representing the cake. Each character is either an uppercase English
// letter or ? (which means that that cell is blank).
// Output
// For each test case, output one line containing Case #x: and nothing else. Then output
// R more lines of C characters each. Your output grid must be identical to the input grid,
// but with every ? replaced with an uppercase English letter, representing that that cell
// appears in the slice for the child who has that initial.
// If there are multiple possible answers, you may output any of them.
// Limits
// 1 ≤ T ≤ 100.
// There is at least one letter in the input grid.
// No letter appears in more than one cell in the input grid.
// It is guaranteed that at least one answer exists for each test case.
// Small dataset
// 1 ≤ R ≤ 12.
// 1 ≤ C ≤ 12.
// R × C ≤ 12.
// Large dataset
// 1 ≤ R ≤ 25.
// 1 ≤ C ≤ 25.
public class AlphabetCake {
    // Priority Queue
    public static void cut(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        PriorityQueue<int[]> chunks = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] != b[0]) ? a[0] - b[0] : a[1] - b[1];
            }
        });
        int lastRow = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                char c = grid[i][j];
                if (c != '?') {
                    chunks.offer(new int[] {i, j});
                    lastRow = i;
                }
            }
        }
        while (!chunks.isEmpty()) {
            int[] cur = chunks.poll();
            int[] next = chunks.peek();
            for (int i = (cur[0] == lastRow) ? m - 1 : cur[0]; i >= 0; i--) {
                int col = (next != null && next[0] == cur[0]) ? next[1] : n;
                for (int j = 0; j < col; j++) {
                    if (grid[i][j] == '?') {
                        grid[i][j] = grid[cur[0]][cur[1]];
                    }
                }
            }
        }
    }

    // Divide & Conquer + Recursion
    public static void cut2(char[][] grid) {
        cut2(grid, 0, 0, grid.length, grid[0].length);
    }

    private static void cut2(char[][] grid, int x, int y, int rows, int cols) {
        int x1 = -1;
        int y1 = -1;
        int x2 = -1;
        int y2 = -1;
        outer: for (int i = x; i < x + rows; i++) {
            for (int j = y; j < y + cols; j++) {
                char c = grid[i][j];
                if (c != '?') {
                    if (x1 < 0) {
                        x1 = i;
                        y1 = j;
                    } else {
                        x2 = i;
                        y2 = j;
                        break outer;
                    }
                }
            }
        }
        if (x2 < 0) { // only one initial
            for (int i = x; i < x + rows; i++) {
                for (int j = y; j < y + cols; j++) {
                    if (grid[i][j] == '?') {
                        grid[i][j] = grid[x1][y1];
                    }
                }
            }
            return;
        }
        if (x1 != x2) { // cut horizontally and recurse
            cut2(grid, x, y, x1 - x + 1, cols);
            cut2(grid, x1 + 1, y, rows - x1 + x - 1, cols);
        } else { // cut vertically and recurse
            cut2(grid, x, y, rows, y1 - y + 1);
            cut2(grid, x, y1 + 1, rows, cols - y1 + y - 1);
        }
    }

    public static void cut3(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int lastRow = 0;
        int blanks = 0; // for optimization
        List<Integer> lastCols = null;
        List<Integer> cols = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != '?') {
                    cols.add(j);
                } else {
                    blanks++;
                }
            }
            if (cols.isEmpty()) continue;

            if (blanks > 0) {
                fill(grid, cols, lastRow, i);
                blanks = 0;
            }
            lastRow = i + 1;
            lastCols = cols;
            cols = new ArrayList<>();
        }
        if (blanks > 0) {
            fill(grid, lastCols, lastRow, m);
        }
    }

    private static void fill(char[][] grid, List<Integer> cols, int startRow, int endRow) {
        int row = (endRow == grid.length) ? startRow - 1 : endRow;
        endRow = (endRow == grid.length) ? endRow - 1 : endRow;
        for (int i = startRow; i <= endRow; i++) {
            int startCol = 0;
            for (int k = 0, n = cols.size(); k < n; k++) {
                int col = cols.get(k);
                for (int j = (k < n - 1) ? col : grid[0].length - 1; j >= startCol; j--) {
                    if (grid[i][j] == '?') {
                        grid[i][j] = grid[row][col];
                    }
                }
                startCol = col + 1;
            }
        }
    }

    // Greedy
    public static void cut4(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (grid[i][j] != '?') {
                    if (grid[i][j - 1] == '?') { // fill to the leftmost
                        for (int k = j - 1; k >= 0; k--) {
                            grid[i][k] = grid[i][j];
                        }
                    }
                } else if (grid[i][j - 1] != '?') {
                    grid[i][j] = grid[i][j - 1];
                }
            }
        }
        for (int i = 1; i < m; i++) {
            if (grid[i][0] == '?' && grid[i - 1][0] != '?') {
                for (int j = 0; j < n; j++) { // fill empty with the above
                    grid[i][j] = grid[i - 1][j];
                }
            }
        }
        for (int i = m - 2; i >= 0; i--) {
            if (grid[i][0] == '?') {
                for (int j = 0; j < n; j++) { // fill empty with the below
                    grid[i][j] = grid[i + 1][j];
                }
            }
        }
    }

    void test(String[] strs, String[] expected) {
        int m = strs.length;
        int n = strs[0].length();
        char[][] grid = new char[m][n];
        for (int i = 0; i < m; i++) {
            grid[i] = strs[i].toCharArray();
        }
        cut4(grid);
        for (int i = 0; i < m; i++) {
            assertEquals(expected[i], String.valueOf(grid[i]));
        }
    }

    @Test
    public void test() {
        test(new String[] {"AB", "?C"}, new String[] {"AB", "CC"});
        test(new String[] {"ECD", "AB?", "???", "???"}, new String[] {"ECD", "ABB", "ABB", "ABB"});
        // test(new String[] {"G??", "?C?", "??J"}, new String[] {"GGJ", "CCJ", "CCJ"});
        test(new String[] {"G??", "?C?", "??J"}, new String[] {"GGG", "CCC", "JJJ"});
        test(new String[] {"CODE", "????", "?JAM"}, new String[] {"CODE", "CODE", "JJAM"});
        test(new String[] {"CA", "AE"}, new String[] {"CA", "AE"});
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
        int R = in.nextInt();
        int C = in.nextInt();
        char[][] grid = new char[R][C];
        for (int i = 0; i < R; i++) {
            grid[i] = in.next().toCharArray();
        }
        cut4(grid);
        out.println();
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                out.print(grid[i][j]);
            }
            out.println();
        }
    }
}
