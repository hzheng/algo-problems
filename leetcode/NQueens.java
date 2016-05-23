import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// The n-queens puzzle
public class NQueens {
    // beats 85.11%
    public static final char QUEEN = 'Q';
    public static final char SPACE = '.';

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

    // adapted from EightQueens.java
    // beats 85.11%
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
            if ((row1 - row2) == Math.abs(col2 - col1)) {
                return false;
            }
        }
        return true;
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

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NQueens");
    }
}
