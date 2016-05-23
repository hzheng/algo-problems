import java.util.*;

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

    void test(int expected, int n) {
        List<List<String> > res = solveNQueens(n);
        assertEquals(expected, res.size());
    }

    @Test
    public void test1() {
        test(2, 4);
        test(92, 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NQueens");
    }
}
