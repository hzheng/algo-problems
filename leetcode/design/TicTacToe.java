import java.lang.reflect.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC348: https://leetcode.com/problems/design-tic-tac-toe
//
// Design a Tic-tac-toe game that is played between two players on a n x n grid.
// You may assume the following rules:
// A move is guaranteed to be valid and is placed on an empty block.
// Once a winning condition is reached, no more moves is allowed.
// A player who succeeds in placing n of their marks in a horizontal, vertical,
// or diagonal row wins the game.
// Could you do better than O(n ^ 2) per move() operation?
public class TicTacToe {
    interface ITicTacToe {
        /** Player {player} makes a move at ({row}, {col}).
           @param row The row of the board.
           @param col The column of the board.
           @param player The player, can be either 1 or 2.
           @return The current winning condition, can be either:
             0: No one wins.
             1: Player 1 wins.
             2: Player 2 wins. */
        public int move(int row, int col, int player);
    }

    // beats 79.91%(112 ms for 32 tests)
    static class TicTacToe1 implements ITicTacToe {
        private int[] rows;
        private int[] cols;

        public TicTacToe1(int n) {
            rows = new int[n + 1]; // last for diagonal
            cols = new int[n + 1]; // last for anti-diagonal
        }

        public int move(int row, int col, int player) {
            int res = fill(rows, row, player);
            if (res != 0) return res;

            res = fill(cols, col, player);
            if (res != 0) return res;

            int n = rows.length - 1;
            if (row == col) {
                res = fill(rows, n, player);
                if (res != 0) return res;
            }
            return (row + col == n - 1) ? fill(cols, n, player) : 0;
        }

        private int fill(int[] rows, int index, int player) {
            int n = rows.length - 1;
            if (rows[index] == 0) { // initial
                rows[index] = (player == 1) ? 1 : -1;
            } else if (rows[index] > n) { // never win
            } else if ((rows[index] > 0) ^ (player == 1)) { // never win
                rows[index] = n + 1;
            } else if (player == 1) {
                if (++rows[index] == n) return 1;
            } else if (--rows[index] == -n) return 2;
            return 0;
        }
    }

    // beats 66.10%(118 ms for 32 tests)
    static class TicTacToe2 implements ITicTacToe {
        private int[] rows;
        private int[] cols;

        public TicTacToe2(int n) {
            rows = new int[n + 1]; // last for diagonal
            cols = new int[n + 1]; // last for anti-diagonal
        }

        public int move(int row, int col, int player) {
            int n = rows.length - 1;
            if (player == 1) {
                if (++rows[row] == n || ++cols[col] == n) return 1;
                if (row == col && ++rows[n] == n) return 1;
                if (row + col == n - 1 && ++cols[n] == n) return 1;
            } else {
                if (--rows[row] == -n || --cols[col] == -n) return 2;
                if (row == col && --rows[n] == -n) return 2;
                if (row + col == n - 1 && --cols[n] == -n) return 2;
            }
            return 0;
        }
    }

    // beats 79.91%(112 ms for 32 tests)
    static class TicTacToe3 implements ITicTacToe {
        private int[] rows;
        private int[] cols;

        public TicTacToe3(int n) {
            rows = new int[n + 1]; // last for diagonal
            cols = new int[n + 1]; // last for anti-diagonal
        }

        public int move(int row, int col, int player) {
            int n = rows.length - 1;
            int increase = (player == 1) ? 1 : -1;
            return (Math.abs(rows[row] += increase) == n
                    || Math.abs(cols[col] += increase) == n
                    || (row == col && Math.abs(rows[n] += increase) == n)
                    || (row + col == n - 1 && Math.abs(cols[n] += increase) == n))
                   ? player : 0;
        }
    }

    private void test1(ITicTacToe toe) {
        assertEquals(0, toe.move(0, 0, 1));
        assertEquals(0, toe.move(0, 2, 2));
        assertEquals(0, toe.move(2, 2, 1));
        assertEquals(0, toe.move(1, 1, 2));
        assertEquals(0, toe.move(2, 0, 1));
        assertEquals(0, toe.move(1, 0, 2));
        assertEquals(1, toe.move(2, 1, 1));
    }

    private void test2(ITicTacToe toe) {
        assertEquals(0, toe.move(0, 2, 1));
        assertEquals(0, toe.move(1, 2, 2));
        assertEquals(0, toe.move(2, 0, 1));
        assertEquals(0, toe.move(0, 0, 2));
        assertEquals(1, toe.move(1, 1, 1));
    }

    private void test1(String className, int n) {
        try {
            Class<?> clazz = Class.forName("TicTacToe$" + className);
            Constructor<?> ctor = clazz.getConstructor(int.class);
            test1((ITicTacToe)ctor.newInstance(new Object[] {n}));
            test2((ITicTacToe)ctor.newInstance(new Object[] {n}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test1(int n) {
        test1("TicTacToe1", n);
        test1("TicTacToe2", n);
        test1("TicTacToe3", n);
    }

    @Test
    public void test() {
        test1(3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TicTacToe");
    }
}
