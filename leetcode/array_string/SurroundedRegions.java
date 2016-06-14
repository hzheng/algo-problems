import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/surrounded-regions/
//
// Given a 2D board containing 'X' and 'O', capture all regions surrounded by 'X'.
// A region is captured by flipping all 'O's into 'X's in that surrounded region.
public class SurroundedRegions {
    // beats 6.55%
    private static final char O = 'O';
    private static final char X = 'X';
    private static final char o = 'o';
    private static final char workChar = ' ';

    public void solve(char[][] board) {
        int nRow = board.length;
        if (nRow == 0) return;

        int nCol = board[0].length;
        // mark O's on edges
        setEdges(board, nRow, nCol, O, o);

        // work on middle cells
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (board[i][j] == O) {
                    mark(board, nRow, nCol, new int[]{i, j});
                }
            }
        }

        // set all o's
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (board[i][j] == o) {
                    board[i][j] = O;
                }
            }
        }
    }

    private void setEdges(char[][] board, int nRow, int nCol, char from, char to) {
        for (int i = 0; i < nRow; i++) {
            if (board[i][0] == from) {
                board[i][0] = to;
            }
            if (board[i][nCol - 1] == from) {
                board[i][nCol - 1] = to;
            }
        }
        for (int j = 1; j < nCol - 1; j++) {
            if (board[0][j] == from) {
                board[0][j] = to;
            }
            if (board[nRow - 1][j] == from) {
                board[nRow - 1][j] = to;
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
            region.offer(new int[]{row, col});
            return true;
        case o:
            return false;
        default:
            throw new AssertionError("unknown char");
        }
    }

    private char[][] convert(String[] s) {
        char[][] board = new char[s.length][];
        for (int i = 0; i < s.length; i++) {
            board[i] = s[i].toCharArray();
        }
        return board;
    }

    void test(String[] s, String[] expected) {
        char[][] board = convert(s);
        char[][] res = convert(expected);
        solve(board);
        assertArrayEquals(res, board);
    }

    @Test
    public void test1() {
        test(new String[] {"XXXX", "XOOX", "XXOX", "XOXX"},
             new String[] {"XXXX", "XXXX", "XXXX", "XOXX"});
        test(new String[] {"XXXX", "XOOX", "XXOX", "XOXX", "XOXX"},
             new String[] {"XXXX", "XXXX", "XXXX", "XOXX", "XOXX"});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SurroundedRegions");
    }
}
