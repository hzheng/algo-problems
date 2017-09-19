import org.junit.Test;
import static org.junit.Assert.*;

// LC289: https://leetcode.com/problems/game-of-life/
//
// Given a board with m by n cells, each cell has an initial state live (1) or
// dead (0). Each cell interacts with its eight neighbors (horizontal, vertical,
// diagonal) using the following four rules (taken from the above Wikipedia article):
//
// Any live cell with fewer than two live neighbors dies, as if caused by
// under-population.
// Any live cell with two or three live neighbors lives on to the next generation.
// Any live cell with more than three live neighbors dies, as if by over-population.
// Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
// Write a function to compute the next state (after one update) of the board
// given its current state.
//
// Follow up:
// Could you solve it in-place? Remember that the board needs to be updated at
// the same time: You cannot update some cells first and then use their updated
// values to update other cells.
// In this question, we represent the board using a 2D array. In principle,
// the board is infinite, which would cause problems when the active area
// encroaches the border of the array. How would you address these problems?
public class GameOfLife {
    // beats 12.39%(1 ms)
    public void gameOfLife(int[][] board) {
        int m = board.length;
        if (m == 0) return;

        int n = board[0].length;
        int mask = 2;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                switch (liveNeighbors(board, i, j, m, n)) {
                case 2:
                    board[i][j] |= (board[i][j] << 1);
                    break;
                case 3:
                    board[i][j] |= mask;
                    break;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] >>= 1;
            }
        }
    }

    private int liveNeighbors(int[][] board, int row, int col, int m, int n) {
        int count = 0;
        if (row > 0) {
            if (col > 0 && (board[row - 1][col - 1] & 1) > 0) {
                count++;
            }
            if ((board[row - 1][col] & 1) > 0) {
                count++;
            }
            if (col + 1 < n && (board[row - 1][col + 1] & 1) > 0) {
                count++;
            }
        }
        if (row + 1 < m) {
            if (col > 0 && (board[row + 1][col - 1] & 1) > 0) {
                count++;
            }
            if ((board[row + 1][col] & 1) > 0) {
                count++;
            }
            if (col + 1 < n && (board[row + 1][col + 1] & 1) > 0) {
                count++;
            }
        }
        if (col > 0  && (board[row][col - 1] & 1) > 0) {
            count++;
        }
        if (col + 1 < n  && (board[row][col + 1] & 1) > 0) {
            count++;
        }
        return count;
    }

    // beats 11.82%(1 ms for 22 tests)
    public void gameOfLife2(int[][] board) {
        int m = board.length;
        if (m == 0) return;

        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int lives = liveNeighbors2(board, m, n, i, j);
                if (board[i][j] == 1 && lives == 2 || lives == 3) {
                    board[i][j] |= 2;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] >>= 1;
            }
        }
    }

    private int liveNeighbors2(int[][] board, int m, int n, int i, int j) {
        int lives = 0;
        for (int x = Math.max(i - 1, 0); x <= Math.min(i + 1, m - 1); x++) {
            for (int y = Math.max(j - 1, 0); y <= Math.min(j + 1, n - 1); y++) {
                lives += board[x][y] & 1;
            }
        }
        return lives -= board[i][j] & 1;
    }

    // beats 12.39%(1 ms)
    public void gameOfLife3(int[][] board) {
        int m = board.length;
        if (m == 0) return;

        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int count = 0;
                for (int r = Math.max(i - 1, 0); r < Math.min(i + 2, m); r++) {
                    for (int c = Math.max(j - 1, 0); c < Math.min(j + 2, n); c++) {
                        count += board[r][c] & 1;
                    }
                }
                if (count == 3 || count - board[i][j] == 3) {
                    board[i][j] |= 2;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] >>= 1;
            }
        }
    }

    // Solution of Choice
    // beats 11.82%(1 ms for 22 tests)
    public void gameOfLife4(int[][] board) {
        int m = board.length;
        if (m == 0) return;

        int n = board[0].length;
        int[][] d = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int count = 0;
                for (int[] diff : d) {
                    int x = i + diff[0];
                    int y = j + diff[1];
                    if (x >= 0 && x < m && y >= 0 && y < n) {
                        count += (board[x][y] & 1);
                    }
                }
                if (count == 3 || count == 2 && board[i][j] == 1) {
                    board[i][j] |= 2;
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] >>= 1;
            }
        }
    }

    void print(int[][] board) {
        int m = board.length;
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j] + ",");
            }
            System.out.println();
        }
        System.out.println("=========");
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<int[][]> game, String name, int[][] board, int[][] expected) {
        game.apply(board);
        assertArrayEquals(expected, board);
    }

    void test(int[][] board, int[][] expected) {
        GameOfLife g = new GameOfLife();
        test(g::gameOfLife, "gameOfLife", board, expected);
        test(g::gameOfLife2, "gameOfLife2", board, expected);
        test(g::gameOfLife3, "gameOfLife3", board, expected);
        test(g::gameOfLife4, "gameOfLife4", board, expected);
    }

    @Test
    public void test1() {
        test(new int[][] {
            {0, 1, 0, 1}, {1, 0, 1, 0}, {0, 1, 0, 1}, {1, 0, 1, 0}
        },
             new int[][] {
            {0, 1, 1, 0}, {1, 0, 0, 1}, {1, 0, 0, 1}, {0, 1, 1, 0}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GameOfLife");
    }
}
