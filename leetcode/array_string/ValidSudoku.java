import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC036: https://leetcode.com/problems/valid-sudoku/
//
// Determine if a Sudoku is valid(not necessarily solvable)
public class ValidSudoku {
    // beats 68.29%(4 ms)
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

    // beats 56.40%(5 ms)
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

    // Hashtable
    // beats 39.90%(31 ms) (new percentage)
    public boolean isValidSudoku3(char[][] board) {
        for(int i = 0; i < 9; i++) {
            Set<Character> rows = new HashSet<>();
            Set<Character> cols = new HashSet<>();
            Set<Character> cube = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.' && !rows.add(board[i][j])) return false;
                if (board[j][i] !='.' && !cols.add(board[j][i])) return false;
                int rowIndex = 3 * (i / 3);
                int colIndex = 3 * (i % 3);
                if (board[rowIndex + j / 3][colIndex + j % 3] != '.'
                    && !cube.add(board[rowIndex + j / 3][colIndex + j % 3])) {
                    return false;
                }
            }
        }
        return true;
    }

    // Solution of Choice
    // Hashtable
    // beats 39.90%(31 ms) (new percentage)
    public boolean isValidSudoku4(char[][] board) {
        boolean[][] used1 = new boolean[9][9];
        boolean[][] used2 = new boolean[9][9];
        boolean[][] used3 = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') continue;

                int num = board[i][j] - '1';
                int k = i / 3 * 3 + j / 3;
                if (used1[i][num] || used2[j][num] || used3[k][num]) return false;

                used1[i][num] = used2[j][num] = used3[k][num] = true;
            }
        }
        return true;
    }

    void test(boolean expected, String[] boardStr) {
        int size = boardStr.length;
        char[][] board = new char[size][];
        for (int i = 0; i < size; i++) {
            board[i] = boardStr[i].toCharArray();
        }
        assertEquals(expected, isValidSudoku(board));
        assertEquals(expected, isValidSudoku2(board));
        assertEquals(expected, isValidSudoku3(board));
        assertEquals(expected, isValidSudoku4(board));
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
