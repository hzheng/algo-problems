import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/surrounded-regions/
//
// Given a 2D board containing 'X' and 'O', capture all regions surrounded by 'X'.
// A region is captured by flipping all 'O's into 'X's in that surrounded region.
public class SurroundedRegions {
    // beats 8.41%
    private static final char O = 'O';
    private static final char X = 'X';
    private static final char o = 'o';
    private static final char workChar = ' ';

    public void solve(char[][] board) {
        int nRow = board.length;
        if (nRow == 0) return;

        int nCol = board[0].length;
        // mark O's on edges
        for (int i = 0; i < nRow; i++) {
            if (board[i][0] == O) {
                board[i][0] = o;
            }
            if (board[i][nCol - 1] == O) {
                board[i][nCol - 1] = o;
            }
        }
        for (int j = 1; j < nCol - 1; j++) {
            if (board[0][j] == O) {
                board[0][j] = o;
            }
            if (board[nRow - 1][j] == O) {
                board[nRow - 1][j] = o;
            }
        }

        // work on middle cells
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (board[i][j] == O) {
                    mark(board, nRow, nCol, new int[] {i, j});
                }
            }
        }

        // set all o's
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (board[i][j] != X) {
                    board[i][j] = O;
                }
            }
        }
    }

    private void mark(char[][] board, int nRow, int nCol, int[] startPos) {
        Queue<int[]> region = new LinkedList<>();
        Queue<int[]> pending = new LinkedList<>();
        region.offer(startPos);
        board[startPos[0]][startPos[1]] = workChar;
        boolean surrounded = true;
        while (!region.isEmpty()) {
            int[] pos = region.poll();
            pending.offer(pos);
            int x = pos[0];
            int y = pos[1];
            surrounded &= check(board, x + 1, y, region);
            surrounded &= check(board, x - 1, y, region);
            surrounded &= check(board, x, y + 1, region);
            surrounded &= check(board, x, y - 1, region);
        }
        char fillChar = surrounded ? X : o;
        while (!pending.isEmpty()) {
            int[] pos = pending.poll();
            board[pos[0]][pos[1]] = fillChar;
        }
    }

    private boolean check(char[][] board, int row, int col, Queue<int[]> region) {
        switch (board[row][col]) {
        case workChar: case X:
            return true;
        case O:
            board[row][col] = workChar;
            region.offer(new int[] {row, col});
            return true;
        case o:
            return false;
        default:
            throw new AssertionError("unknown char");
        }
    }

    // beats 50.65%
    // only consider borders
    public void solve2(char[][] board) {
        int nRow = board.length;
        if (nRow == 0) return;

        int nCol = board[0].length;
        fillEdges(board, nRow, nCol, O, o);
        replace(board, nRow, nCol, O, X);
        fillEdges(board, nRow, nCol, o, O);
    }

    private void fillEdges(char[][] board, int nRow, int nCol,
                           char from, char to) {
        for (int i = 0; i < nRow; i++) {
            if (board[i][0] == from) {
                fill(board, nRow, nCol, i, 0, from, to);
            }
            if (board[i][nCol - 1] == from) {
                fill(board, nRow, nCol, i, nCol - 1, from, to);
            }
        }
        for (int j = 1; j < nCol - 1; j++) {
            if (board[0][j] == from) {
                fill(board, nRow, nCol, 0, j, from, to);
            }
            if (board[nRow - 1][j] == from) {
                fill(board, nRow, nCol, nRow - 1, j, from, to);
            }
        }
    }

    private void fill(char[][] board, int nRow, int nCol, int row, int col,
                      char from, char to) {
        Queue<int[]> region = new LinkedList<>();
        region.offer(new int[] {row, col});
        board[row][col] = to;
        while (!region.isEmpty()) {
            int[] pos = region.poll();
            int x = pos[0];
            int y = pos[1];
            if (x + 1 < nRow && board[x + 1][y] == from) {
                board[x + 1][y] = to;
                region.offer(new int[] {x + 1, y});
            }
            if (x > 0 && board[x - 1][y] == from) {
                board[x - 1][y] = to;
                region.offer(new int[] {x - 1, y});
            }
            if (y + 1 < nCol && board[x][y + 1] == from) {
                board[x][y + 1] = to;
                region.offer(new int[] {x, y + 1});
            }
            if (y > 0 && board[x][y - 1] == from) {
                board[x][y - 1] = to;
                region.offer(new int[] {x, y - 1});
            }
        }
    }

    private void replace(char[][] board, int nRow, int nCol, char from, char to) {
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (board[i][j] == from) {
                    board[i][j] = to;
                }
            }
        }
    }

    // TODO: use iterative DFS by use of stack

    private char[][] convert(String[] s) {
        char[][] board = new char[s.length][];
        for (int i = 0; i < s.length; i++) {
            board[i] = s[i].toCharArray();
        }
        return board;
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<char[][]> solve, String[] s, String[] expected) {
        char[][] board = convert(s);
        char[][] res = convert(expected);
        solve.apply(board);
        assertArrayEquals(res, board);
    }

    void test(String[] s, String[] expected) {
        SurroundedRegions regions = new SurroundedRegions();
        test(regions::solve, s, expected);
        test(regions::solve2, s, expected);
    }

    @Test
    public void test1() {
        test(new String[] {"XXXX", "XOOX", "XXOX", "XOXX"},
             new String[] {"XXXX", "XXXX", "XXXX", "XOXX"});
        test(new String[] {"XXXX", "XOOX", "XXOX", "XOXX", "XOXX"},
             new String[] {"XXXX", "XXXX", "XXXX", "XOXX", "XOXX"});
        test(new String[] {
            "XOOXXXOXOO", "XOXXXXXXXX", "XXXXOXXXXX", "XOXXXOXXXO",
            "OXXXOXOXOX", "XXOXXOOXXX", "OXXOOXOXXO", "OXXXXXOXXX",
            "XOOXXOXXOO", "XXXOOXOXXO"
        },
             new String[] {
            "XOOXXXOXOO", "XOXXXXXXXX", "XXXXXXXXXX", "XXXXXXXXXO",
            "OXXXXXXXXX", "XXXXXXXXXX", "OXXXXXXXXO", "OXXXXXXXXX",
            "XXXXXXXXOO", "XXXOOXOXXO"
        });
        test(new String[] {
            "XOOXXXOXOO", "XOXXXXXXXX", "XXXXOXXXXX", "XOXXXOXXXO",
            "OXXXOXOXOX", "XXOXXOOXXX", "OXXOOXOXXO", "OOXXXXOXXX",
            "XOOXXOXXOO", "XXXOOXOXXO"
        },
             new String[] {
            "XOOXXXOXOO", "XOXXXXXXXX", "XXXXXXXXXX", "XXXXXXXXXO",
            "OXXXXXXXXX", "XXXXXXXXXX", "OXXXXXXXXO", "OOXXXXXXXX",
            "XOOXXXXXOO", "XXXOOXOXXO"
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SurroundedRegions");
    }
}
