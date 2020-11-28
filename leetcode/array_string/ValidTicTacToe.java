import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC794: https://leetcode.com/problems/valid-tic-tac-toe-state/
//
// A Tic-Tac-Toe board is given as a string array board. Return True if and only if it is possible
// to reach this board position during the course of a valid tic-tac-toe game.
// The board is a 3 x 3 array, and consists of characters " ", "X", and "O".  The " " character
// represents an empty square.
// Here are the rules of Tic-Tac-Toe:
// Players take turns placing characters into empty squares (" ").
// The first player always places "X" characters, while the second player places "O" characters.
// "X" and "O" characters are always placed into empty squares, never filled ones.
// The game ends when there are 3 of the same (non-empty) character filling any row/column/diagonal.
// The game also ends if all squares are non-empty.
// No more moves can be played if the game is over.
//
// Note:
// board is a length-3 array of strings, where each string board[i] has length 3.
// Each board[i][j] is a character in the set {" ", "X", "O"}.
public class ValidTicTacToe {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 38.4 MB(5.06%) for 108 tests
    public boolean validTicTacToe(String[] board) {
        final int n = 3;
        int[] win = new int[n]; // 0: X wins; 2: O wins
        int[] map = new int[128];
        map['X'] = 1;
        map['O'] = -1;
        int turn = 0;
        int diaSum = 0;
        int diaSum2 = 0;
        for (int i = 0; i < n; i++) {
            int rowSum = 0;
            int colSum = 0;
            for (int j = 0; j < n; j++) {
                rowSum += map[board[i].charAt(j)];
                colSum += map[board[j].charAt(i)];
                turn += map[board[i].charAt(j)];
            }
            win[rowSum / n + 1]++;
            win[colSum / n + 1]++;
            diaSum += map[board[i].charAt(i)];
            diaSum2 += map[board[2 - i].charAt(i)];
        }
        if (turn != 0 && turn != 1) { return false; }

        win[diaSum / n + 1]++;
        win[diaSum2 / n + 1]++;
        return (win[2] * turn > 0) || (win[2] == 0 && win[0] * turn == 0);
    }

    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.2 MB(62.15%) for 108 tests
    public boolean validTicTacToe2(String[] board) {
        int turns = 0;
        int[] rows = new int[3];
        int[] cols = new int[3];
        int diaSum = 0;
        int diaSum2 = 0;
        int[] map = new int[128];
        map['X'] = 1;
        map['O'] = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int increase = map[board[i].charAt(j)];
                turns += increase;
                rows[i] += increase;
                cols[j] += increase;
                diaSum += (i == j) ? increase : 0;
                diaSum2 += (i + j == 2) ? increase : 0;
            }
        }
        if (turns != 0 && turns != 1) { return false; }

        boolean xWin = rows[0] == 3 || rows[1] == 3 || rows[2] == 3 || cols[0] == 3 || cols[1] == 3
                       || cols[2] == 3 || diaSum == 3 || diaSum2 == 3;
        boolean oWin =
                rows[0] == -3 || rows[1] == -3 || rows[2] == -3 || cols[0] == -3 || cols[1] == -3
                || cols[2] == -3 || diaSum == -3 || diaSum2 == -3;
        return (!xWin || turns != 0) && (!oWin || turns != 1);
    }

    private void test(String[] board, boolean expected) {
        assertEquals(expected, validTicTacToe(board));
        assertEquals(expected, validTicTacToe2(board));
    }

    @Test public void test() {
        test(new String[] {"XXO", "XOX", "OXO"}, false);
        test(new String[] {"O  ", "   ", "   "}, false);
        test(new String[] {"XOX", " X ", "   "}, false);
        test(new String[] {"XXX", "   ", "OOO"}, false);
        test(new String[] {"XOX", "O O", "XOX"}, true);
        test(new String[] {"XXX", "OXO", "OOX"}, true);
        test(new String[] {"OXX", "XOX", "OXO"}, false);
        test(new String[] {"XXX", "XOO", "OO "}, false);
        test(new String[] {"XO ", "XO ", "XO "}, false);
        test(new String[] {"XXX", "XOO", "XOO"}, true);
        test(new String[] {" OX", " XO", "X  "}, true);
        test(new String[] {" XO", " OX", "O X"}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
