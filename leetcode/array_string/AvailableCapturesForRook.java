import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC999: https://leetcode.com/problems/available-captures-for-rook/
//
// On an 8 x 8 chessboard, there is one white rook.  There also may be empty squares, white bishops,
// and black pawns.  These are given as characters 'R', '.', 'B', and 'p' respectively. Uppercase
// characters represent white pieces, and lowercase characters represent black pieces.
// The rook moves as in the rules of Chess: it chooses one of four cardinal directions (north, east,
// west, and south), then moves in that direction until it chooses to stop, reaches the edge of the
// board, or captures an opposite colored pawn by moving to the same square it occupies. Also, rooks
// cannot move into the same square as other friendly bishops.
// Return the number of pawns the rook can capture in one move.
//
// Note:
// board.length == board[i].length == 8
// board[i][j] is either 'R', '.', 'B', or 'p'
// There is exactly one cell with board[i][j] == 'R'
public class AvailableCapturesForRook {
    // time complexity: O(N^2), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(28.91%) for 22 tests
    public int numRookCaptures(char[][] board) {
        int n = board.length;
        int rx = 0;
        int ry = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'R') {
                    rx = i;
                    ry = j;
                    break;
                }
            }
        }
        int[] res = new int[1];
        for (int y = ry + 1; y < n && check(res, board[rx][y]); y++) {}
        for (int y = ry - 1; y >= 0 && check(res, board[rx][y]); y--) {}
        for (int x = rx + 1; x < n && check(res, board[x][ry]); x++) {}
        for (int x = rx - 1; x >= 0 && check(res, board[x][ry]); x--) {}
        return res[0];
    }

    private boolean check(int[] res, char c) {
        if (c == '.') { return true; }

        res[0] += (c == 'p') ? 1 : 0;
        return false;
    }

    // time complexity: O(N^2), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(28.91%) for 22 tests
    public int numRookCaptures2(char[][] board) {
        int n = board.length;
        int rx = 0;
        int ry = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'R') {
                    rx = i;
                    ry = j;
                    break;
                }
            }
        }
        int res = 0;
        for (int[] d : new int[][] {{1, 0}, {0, 1}, {-1, 0}, {0, -1}}) {
            for (int x = rx + d[0], y = ry + d[1];
                 x >= 0 && x < n && y >= 0 && y < n; x += d[0], y += d[1]) {
                if (board[x][y] == 'p') {
                    res++;
                }
                if (board[x][y] != '.') { break; }
            }
        }
        return res;
    }

    // time complexity: O(N^2), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(28.91%) for 22 tests
    public int numRookCaptures3(char[][] board) {
        int n = board.length;
        for (int i = 0; ; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] != 'R') { continue; }

                return capture(board, i, j, 0, 1) + capture(board, i, j, 0, -1)
                       + capture(board, i, j, 1, 0) + capture(board, i, j, -1, 0);
            }
        }
    }

    private int capture(char[][] board, int x, int y, int dx, int dy) {
        for (int n = board.length;
             x >= 0 && x < n && y >= 0 && y < n && board[x][y] != 'B'; x += dx, y += dy) {
            if (board[x][y] == 'p') { return 1; }
        }
        return 0;
    }

    private void test(char[][] board, int expected) {
        assertEquals(expected, numRookCaptures(board));
        assertEquals(expected, numRookCaptures2(board));
        assertEquals(expected, numRookCaptures3(board));
    }

    @Test public void test() {
        test(new char[][] {{'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', '.', '.', 'p', '.', '.', '.', '.'},
                           {'.', '.', '.', 'R', '.', '.', '.', 'p'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', '.', '.', 'p', '.', '.', '.', '.'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'}}, 3);
        test(new char[][] {{'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', 'p', 'p', 'p', 'p', 'p', '.', '.'},
                           {'.', 'p', 'p', 'B', 'p', 'p', '.', '.'},
                           {'.', 'p', 'B', 'R', 'B', 'p', '.', '.'},
                           {'.', 'p', 'p', 'B', 'p', 'p', '.', '.'},
                           {'.', 'p', 'p', 'p', 'p', 'p', '.', '.'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'}}, 0);
        test(new char[][] {{'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', '.', '.', 'p', '.', '.', '.', '.'},
                           {'.', '.', '.', 'p', '.', '.', '.', '.'},
                           {'p', 'p', '.', 'R', '.', 'p', 'B', '.'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'},
                           {'.', '.', '.', 'B', '.', '.', '.', '.'},
                           {'.', '.', '.', 'p', '.', '.', '.', '.'},
                           {'.', '.', '.', '.', '.', '.', '.', '.'}}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
