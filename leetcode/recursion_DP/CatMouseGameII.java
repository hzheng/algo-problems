import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1728: https://leetcode.com/problems/cat-and-mouse-ii/
//
// A game is played by a cat and a mouse named Cat and Mouse. The environment is represented by a
// grid of size rows x cols, where each element is a wall, floor, player (Cat, Mouse), or food.
// Players are represented by the characters 'C'(Cat),'M'(Mouse).
// Floors are represented by the character '.' and can be walked on.
// Walls are represented by the character '#' and cannot be walked on.
// Food is represented by the character 'F' and can be walked on.
// There is only one of each character 'C', 'M', and 'F' in grid.
// Mouse and Cat play according to the following rules:
// Mouse moves first, then they take turns to move.
// During each turn, Cat and Mouse can jump in one of the four directions (left, right, up, down).
// They cannot jump over the wall nor outside of the grid.
// catJump, mouseJump are the maximum lengths Cat and Mouse can jump at a time, respectively. Cat
// and Mouse can jump less than the maximum length.
// Staying in the same position is allowed.
// Mouse can jump over Cat.
// The game can end in 4 ways:
// If Cat occupies the same position as Mouse, Cat wins.
// If Cat reaches the food first, Cat wins.
// If Mouse reaches the food first, Mouse wins.
// If Mouse cannot get to the food within 1000 turns, Cat wins.
// Given a rows x cols matrix grid and two integers catJump and mouseJump, return true if Mouse can
// win the game if both Cat and Mouse play optimally, otherwise return false.
//
// Constraints:
// rows == grid.length
// cols = grid[i].length
// 1 <= rows, cols <= 8
// grid[i][j] consist only of characters 'C', 'M', 'F', '.', and '#'.
// There is only one of each character 'C', 'M', and 'F' in grid.
// 1 <= catJump, mouseJump <= 8
public class CatMouseGameII {
    private static final int[][] MOVES = new int[][] {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // Recursion + Dynamic Programming(Top-Down) + Backtracking
    // time complexity: O(M^3 * N^3), space complexity: O(M^2 * N^2)
    // 84 ms(90.4%), 43.3 MB(93.15%) for 175 tests
    public boolean canMouseWin(String[] grid, int catJump, int mouseJump) {
        int m = grid.length;
        int n = grid[0].length();
        char[][] g = new char[m][n];
        int[] cat = new int[2];
        int[] mouse = new int[2];
        for (int i = 0; i < m; i++) {
            g[i] = grid[i].toCharArray();
            int index = grid[i].indexOf('C');
            if (index >= 0) {
                cat = new int[] {i, index};
            }
            index = grid[i].indexOf('M');
            if (index >= 0) {
                mouse = new int[] {i, index};
            }
        }
        final int maxTurn = Math.max((m + 1) * n, (n + 1) * m) - 4;
        return play(g, new int[][] {mouse, cat}, 0, new int[] {mouseJump, catJump},
                    new int[maxTurn + 1][m][n][m][n]);
    }

    private boolean play(char[][] grid, int[][] player, int turn, int[] jump, int[][][][][] dp) {
        int index = turn % 2;
        if (turn >= dp.length) { return index == 1; }

        int win = dp[turn][player[0][0]][player[0][1]][player[1][0]][player[1][1]];
        if (win > 0) { return win == index + 1; }

        int x = player[index][0];
        int y = player[index][1];
        if ((grid[x][y] == 'F') || (index == 1 && x == player[0][0] && y == player[0][1])) {
            return true;
        }
        if (index == 0 && x == player[1][0] && y == player[1][1]) { return false; }

        boolean res = false;
        outer:
        for (int[] move : MOVES) {
            for (int i = 1 - index; i <= jump[index]; i++) { // cat can wait until mouse starves
                int nx = x + move[0] * i;
                int ny = y + move[1] * i;
                if (nx < 0 || nx >= grid.length || ny < 0 || ny >= grid[0].length
                    || grid[nx][ny] == '#') { break; }

                player[index][0] = nx;
                player[index][1] = ny;
                res = !play(grid, player, turn + 1, jump, dp);
                player[index][0] = x;
                player[index][1] = y;
                if (res) { break outer; }
            }
        }
        dp[turn][player[0][0]][player[0][1]][player[1][0]][player[1][1]] =
                1 + (res ? index : 1 - index);
        return res;
    }

    // TODO: BFS
    // TODO: Work backward to determine which other states can be labeled as winning or losing

    private void test(String[] grid, int catJump, int mouseJump, boolean expected) {
        assertEquals(expected, canMouseWin(grid, catJump, mouseJump));
    }

    @Test public void test() {
        test(new String[] {"####F", "#C...", "M...."}, 1, 2, true);
        test(new String[] {"M.C...F"}, 1, 4, true);
        test(new String[] {"M.C...F"}, 1, 3, false);
        test(new String[] {"C...#", "...#F", "....#", "M...."}, 2, 5, false);
        test(new String[] {".M...", "..#..", "#..#.", "C#.#.", "...#F"}, 3, 1, true);
        test(new String[] {"####.##", ".#C#F#.", "######.", "##M.###"}, 3, 6, false);
        test(new String[] {"CM......", "#######.", "........", ".#######", "........", "#######.",
                           "........", "F#######"}, 1, 1, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
