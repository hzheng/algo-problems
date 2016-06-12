import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/valid-sudoku/
// Determine if a Sudoku is valid(not necessarily solvable)
public class ValidSudoku {
    // beats 68.29%
    public boolean isValidSudoku(char[][] board) {
        final int MAX = board.length;
        char[] seq = new char[MAX];
        for (int i = 0; i < MAX; i++) {
            if (!isValid(board[i])) return false;

            for (int j = 0; j < MAX; j++) {
                seq[j] = board[j][i];
            }
            if (!isValid(seq)) return false;

            int groupRow = (i / 3) * 3;
            int groupCol = (i % 3) * 3;
            for (int j = 0, m = groupRow; m < groupRow + 3; m++) {
                for (int n = groupCol; n < groupCol + 3; n++) {
                    seq[j++] = board[m][n];
                }
            }
            if (!isValid(seq)) return false;
        }
        return true;
    }

    boolean isValid(char[] seq) {
        int bits = 0;
        for (char c : seq) {
            if (c != '.') {
                int mask = 1 << (c - '1');
                if ((bits & mask) > 0) return false;

                bits |= mask;
            }
        }
        return true;
    }

    // beats 56.40%
    public boolean isValidSudoku2(char[][] board) {
        final int MAX = board.length;
        for (int i = 0; i < MAX; i++) {
            int[] bits = {0};
            for (char c : board[i]) {
                if (!check(c, bits)) return false;
            }

            bits[0] = 0;
            for (int j = 0; j < MAX; j++) {
                if (!check(board[j][i], bits)) return false;
            }

            bits[0] = 0;
            int groupRow = (i / 3) * 3;
            int groupCol = (i % 3) * 3;
            for (int m = groupRow; m < groupRow + 3; m++) {
                for (int n = groupCol; n < groupCol + 3; n++) {
                    if (!check(board[m][n], bits)) return false;
                }
            }
        }
        return true;
    }

    boolean check(char c, int[] bits) {
        if (c == '.') return true;

        int mask = 1 << (c - '1');
        if ((bits[0] & mask) > 0) return false;

        bits[0] |= mask;
        return true;
    }

    // TODO: use hash table

    void test(boolean expected, String[] boardStr) {
        int size = boardStr.length;
        char[][] board = new char[size][];
        for (int i = 0; i < size; i++) {
            board[i] = boardStr[i].toCharArray();
        }
        assertEquals(expected, isValidSudoku(board));
        assertEquals(expected, isValidSudoku2(board));
    }

    @Test
    public void test1() {
        test(true, new String[] {
            "53..7....", "6..195...", ".98....6.",
            "8...6...3", "4..8.3..1", "7...2...6",
            ".6....28.", "...419..5", "....8..79"
        });
        test(false, new String[] {
            "53..7....", "6..195...", ".98....6.",
            "8...6...3", "4..8.3..1", "7...2...6",
            ".6....28.", "5..419..5", "....8..79"
        });
        test(false, new String[] {
            "53..7...1", "6..195...", ".98....6.",
            "8...6...3", "4..8.3..1", "7...2...6",
            ".6....28.", "...419..5", "....8..79"
        });
        test(false, new String[] {
            "....5..1.", ".4.3.....", ".....3..1",
            "8......2.", "..2.7....", ".15......",
            ".....2...", ".2.9.....", "..4......"
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidSudoku");
    }
}
