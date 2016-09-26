import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC051: https://leetcode.com/problems/n-queens/
// Cracking the Coding Interview(5ed) Problem 9.9
//
// The n-queens puzzle
public class NQueens {
    // beats 85.11%(5 ms)
    public static final char QUEEN = 'Q';
    public static final char SPACE = '.';

    // Backtracking
    public List<List<String> > solveNQueens(int n) {
        if (n == 0) return Collections.emptyList();

        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = SPACE;
            }
        }
        List<List<String> > solutions = new ArrayList<>();
        Marker marker = new Marker(board);
        fill(board, 0, marker, solutions);
        return solutions;
    }

    static class Marker {
        char[][] board;
        int n;
        boolean[] cols;
        boolean[] sums;
        boolean[] diffs;
        int count;

        Marker(char[][] board) {
            this.board = board;
            n = board.length;
            cols = new boolean[n];
            sums = new boolean[2 * n - 1];
            diffs = new boolean[2 * n - 1];
        }

        boolean safe(int row, int col) {
            return !cols[col]
                   && !sums[row + col] && !diffs[row - col + n - 1];
        }

        void mark(int row, int col) {
            board[row][col] = QUEEN;
            cols[col] = true;
            sums[col + row] = true;
            diffs[row - col + n - 1] = true;
        }

        void unmark(int row, int col) {
            board[row][col] = SPACE;
            cols[col] = false;
            sums[col + row] = false;
            diffs[row - col + n - 1] = false;
        }
    }

    private void add(List<List<String> > solutions, char[][] board) {
        List<String> solution = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            solution.add(new String(board[i]));
        }
        solutions.add(solution);
    }

    private void fill(char[][] board, int row, Marker marker,
                      List<List<String> > solutions) {
        int n = board.length;
        if (row == n) {
            add(solutions, board);
            return;
        }

        for (int col = 0; col < n; col++) {
            if (marker.safe(row, col)) {
                marker.mark(row, col);
                fill(board, row + 1, marker, solutions);
                marker.unmark(row, col);
            }
        }
    }

    // Solution of Choice
    // Backtracking
    // adapted from EightQueens.java
    // beats 85.11%(5 ms)
    public List<List<String> > solveNQueens2(int n) {
        List<List<String> > solutions = new ArrayList<>();
        placeQueens(solutions, new int[n], 0);
        return solutions;
    }

    private void placeQueens(List<List<String> > solutions,
                             int[] occupiedCols, int row) {
        int n = occupiedCols.length;
        if (row == n) {
            List<String> solution = new ArrayList<>();
            char[] board = new char[n];
            for (int i = 0; i < n; i++) {
                int occupiedCol = occupiedCols[i];
                for (int j = 0; j < n; j++) {
                    board[j] = (j == occupiedCol) ? QUEEN : SPACE;
                }
                solution.add(new String(board));
            }
            solutions.add(solution);
            return;
        }

        // check all columns on the row-th row
        for (int col = 0; col < n; col++) {
            if (isSafe(row, col, occupiedCols)) {
                occupiedCols[row] = col;
                placeQueens(solutions, occupiedCols, row + 1);
            }
        }
    }

    private boolean isSafe(int row1, int col1, int[] occupiedCols) {
        for (int row2 = 0; row2 < row1; row2++) {
            int col2 = occupiedCols[row2];
            if (col1 == col2) return false;
            if ((row1 - row2) == Math.abs(col2 - col1)) return false;
        }
        return true;
    }

    //=======================================================
    // LC052: https://leetcode.com/problems/n-queens-ii/
    // N-Queens II
    //
    // Backtracking
    // we can improve efficiency by removing board but that
    // need change class Marker's code
    // beats 34.55%(6 ms)
    public int totalNQueens(int n) {
        if (n == 0) return 0;

        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = SPACE;
            }
        }
        int[] solutions = new int[1];
        Marker marker = new Marker(board);
        fill(board, 0, marker, solutions);
        return solutions[0];
    }

    private void fill(char[][] board, int row, Marker marker,
                      int[] solutions) {
        int n = board.length;
        if (row == n) {
            solutions[0]++;
            return;
        }

        for (int col = 0; col < n; col++) {
            if (marker.safe(row, col)) {
                marker.mark(row, col);
                fill(board, row + 1, marker, solutions);
                marker.unmark(row, col);
            }
        }
    }

    // Backtracking
    // beats 51.14%(4 ms)
    public int totalNQueens2(int n) {
        int[] solutions = new int[1];
        placeQueens(solutions, new int[n], 0);
        return solutions[0];
    }

    private void placeQueens(int[] solutions, int[] occupiedCols, int row) {
        int n = occupiedCols.length;
        if (row == n) {
            solutions[0]++;
            return;
        }

        for (int col = 0; col < n; col++) {
            if (isSafe(row, col, occupiedCols)) {
                occupiedCols[row] = col;
                placeQueens(solutions, occupiedCols, row + 1);
            }
        }
    }

    // Solution of Choice
    // Backtracking
    // https://discuss.leetcode.com/topic/29626/easiest-java-solution-1ms-98-22
    // beats 87.95%(2 ms)
    public int totalNQueens3(int n) {
        int[] solutions = new int[1];
        placeQueens(n, solutions, 0, new boolean[n],
                    new boolean[n * 2], new boolean[n * 2]);
        return solutions[0];
    }

    private void placeQueens(int n, int[] solutions, int row, boolean[] cols,
                             boolean[] diagonal1, boolean[] diagonal2) {
        if (row == n) {
            solutions[0]++;
            return;
        }

        for (int col = 0; col < n; col++) {
            int i1 = col - row + n;
            int i2 = col + row;
            if (cols[col] || diagonal1[i1] || diagonal2[i2]) continue;

            cols[col] = diagonal1[i1] = diagonal2[i2] = true;
            placeQueens(n, solutions, row + 1, cols, diagonal1, diagonal2);
            cols[col] = diagonal1[i1] = diagonal2[i2] = false;
        }
    }

    private void test(Function<Integer, List<List<String>>> placeQueens, int n) {
        List<List<String> > results = placeQueens.apply(n);
        for (List<String> result : results) {
            for (int i = 0; i < result.size(); i++) {
                System.out.format("(%d, %s) ", i, result.get(i));
            }
            System.out.println();
        }
        System.out.println("total solutions: " + results.size());
    }

    void test(Function<Integer, List<List<String>>> solve, String name,
              int expected, int n) {
        long t1 = System.nanoTime();
        List<List<String> > res = solve.apply(n);
        System.out.format("%d-queen %s: %.3f ms\n", n, name,
                          (System.nanoTime() - t1) * 1e-6);
        assertEquals(expected, res.size());
    }

    void test(int expected, int n) {
        NQueens q = new NQueens();
        test(q::solveNQueens, "solveNQueens", expected, n);
        test(q::solveNQueens2, "solveNQueens2", expected, n);
        assertEquals(expected, totalNQueens(n));
        assertEquals(expected, totalNQueens2(n));
        assertEquals(expected, totalNQueens3(n));
    }

    @Test
    public void test1() {
        test(2, 4);
        test(4, 6);
        test(92, 8);
        test(724, 10);
        test(14200, 12);
        // test(365596, 14);
    }

    // @Test
    public void test2() {
        NQueens q = new NQueens();
        test(q::solveNQueens, 8);
        System.out.println("=====================");
        test(q::solveNQueens2, 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NQueens");
    }
}
