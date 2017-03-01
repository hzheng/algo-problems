import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC529: https://leetcode.com/problems/minesweeper/
//
// Let's play the minesweeper game. You are given a 2D char matrix representing
// the game board. 'M' represents an unrevealed mine, 'E' represents an unrevealed
// empty square, 'B' represents a revealed blank square that has no adjacent mines,
// digit ('1' to '8') represents how many mines are adjacent to this revealed square,
// and finally 'X' represents a revealed mine.
// Now given the next click position among all the unrevealed squares ('M' or 'E'),
//  return the board after revealing this position according to the following rules:
//
// If a mine ('M') is revealed, then the game is over - change it to 'X'.
// If an empty square ('E') with no adjacent mines is revealed, then change it to
// revealed blank ('B') and all of its adjacent unrevealed squares should be revealed recursively.
// If an empty square ('E') with at least one adjacent mine is revealed, then change
// it to a digit ('1' to '8') representing the number of adjacent mines.
// Return the board when no more squares will be revealed.
public class Minesweeper {
    // DFS + Recursion
    // beats 87.87%(6 ms for 54 tests)
    private static final int[][] MOVES = new int[][] {{0, 1}, {0, -1}, {1, 0}, {-1, 0},
                                                      {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public char[][] updateBoard(char[][] board, int[] click) {
        int m = board.length;
        int n = board[0].length;
        int x = click[0];
        int y = click[1];
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
        } else {
            update(board, m, n, x, y);
        }
        return board;
    }

    private void update(char[][] board, int m, int n, int x, int y) {
        if (x < 0 || x >= m || y < 0 || y >= n || board[x][y] != 'E') return;

        int mines = detectMines(board, m, n, x, y);
        if (mines > 0) {
            board[x][y] = (char)('0' + mines);
        } else {
            board[x][y] = 'B';
            for (int[] move : MOVES) {
                update(board, m, n, x + move[0], y + move[1]);
            }
        }
    }

    private int detectMines(char[][] board, int m, int n, int x, int y) {
        int count = 0;
        for (int[] move : MOVES) {
            if (x + move[0] >= 0 && x + move[0] < m
                && y + move[1] >= 0 && y + move[1] < n) {
                if (board[x + move[0]][y + move[1]] == 'M') {
                    count++;
                }
            }
        }
        return count;
    }

    // beats 44.59%(10 ms for 54 tests)
    public char[][] updateBoard2(char[][] board, int[] click) {
        int m = board.length;
        int n = board[0].length;
        int x = click[0];
        int y = click[1];
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
            return board;
        }

        board[x][y] = 'B';
        Queue<int[]> queue = new LinkedList<>();
        for (queue.offer(click); !queue.isEmpty(); ) {
            int[] pos = queue.poll();
            x = pos[0];
            y = pos[1];
            int mines = detectMines(board, m, n, x, y);
            if (mines > 0) {
                board[x][y] = (char)('0' + mines);
                continue;
            }
            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && board[nx][ny] == 'E') {
                    queue.offer(new int[] {nx, ny});
                    board[nx][ny] = 'B'; // before next queue.poll()!
                }
            }
        }
        return board;
    }

    void test(char[][] board, int[] click, char[][] expected) {
        assertArrayEquals(expected, updateBoard(Utils.clone(board), click));
        assertArrayEquals(expected, updateBoard2(Utils.clone(board), click));
    }

    @Test
    public void test() {
        test(new char[][] {{'E'}}, new int[] {0, 0}, new char[][] {{'B'}});
        test(new char[][] {
            {'E', 'E', 'E', 'E', 'E'},
            {'E', 'E', 'M', 'E', 'E'},
            {'E', 'E', 'E', 'E', 'E'},
            {'E', 'E', 'E', 'E', 'E'}
        },
             new int[] {3, 0},
             new char[][] {
            {'B', '1', 'E', '1', 'B'},
            {'B', '1', 'M', '1', 'B'},
            {'B', '1', '1', '1', 'B'},
            {'B', 'B', 'B', 'B', 'B'}
        });
        test(new char[][] {
            {'B', '1', 'E', '1', 'B'},
            {'B', '1', 'M', '1', 'B'},
            {'B', '1', '1', '1', 'B'},
            {'B', 'B', 'B', 'B', 'B'}
        },
             new int[] {1, 2},
             new char[][] {
            {'B', '1', 'E', '1', 'B'},
            {'B', '1', 'X', '1', 'B'},
            {'B', '1', '1', '1', 'B'},
            {'B', 'B', 'B', 'B', 'B'}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Minesweeper");
    }
}
