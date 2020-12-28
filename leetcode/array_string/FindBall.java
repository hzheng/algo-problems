import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1706: https://leetcode.com/problems/where-will-the-ball-fall/
//
// You have a 2-D grid of size m x n representing a box, and you have n balls. The box is open on
// the top and bottom sides. Each cell in the box has a diagonal board spanning two corners of the
// cell that can redirect a ball to the right or to the left.
// A board that redirects the ball to the right spans the top-left corner to the bottom-right corner
// and is represented in the grid as 1. A board that redirects the ball to the left spans the
// top-right corner to the bottom-left corner and is represented in the grid as -1. We drop one ball
// at the top of each column of the box. Each ball can get stuck in the box or fall out of the
// bottom. A ball gets stuck if it hits a "V" shaped pattern between two boards or if a board
// redirects the ball into either wall of the box. Return an array answer of size n where answer[i]
// is the column that the ball falls out of at the bottom after dropping the ball from the ith
// column at the top, or -1 if the ball gets stuck in the box.
//
// Constraints:
// m == grid.length
// n == grid[i].length
// 1 <= m, n <= 100
// grid[i][j] is 1 or -1.
public class FindBall {
    // time complexity: O(M*N), space complexity: O(N)
    // 2 ms(100.00%), 40.1 MB(100.00%) for 63 tests
    public int[] findBall(int[][] grid) {
        int n = grid[0].length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int column = i;
            for (int[] row : grid) {
                int board = row[column];
                column += board;
                if (column < 0 || column >= n || row[column] != board) {
                    column = -1;
                    break;
                }
            }
            res[i] = column;
        }
        return res;
    }

    // time complexity: O(M*N), space complexity: O(N)
    // 0 ms(100.00%), 40.2 MB(100.00%) for 63 tests
    public int[] findBall2(int[][] grid) {
        int n = grid[0].length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = dfs(grid, 0, i);
        }
        return res;
    }

    private int dfs(int[][] grid, int x, int y) {
        if (x == grid.length) { return y; }

        int board = grid[x][y];
        y += board;
        if (y < 0 || y >= grid[0].length || grid[x][y] != board) { return -1; }

        return dfs(grid, x + 1, y);
    }

    private void test(int[][] grid, int[] expected) {
        assertArrayEquals(expected, findBall(grid));
        assertArrayEquals(expected, findBall2(grid));
    }

    @Test public void test() {
        test(new int[][] {{1, 1, 1, -1, -1}, {1, 1, 1, -1, -1}, {-1, -1, -1, 1, 1},
                          {1, 1, 1, 1, -1}, {-1, -1, -1, -1, -1}}, new int[] {1, -1, -1, -1, -1});
        test(new int[][] {{-1}}, new int[] {-1});
        test(new int[][] {
                     {-1, 1, -1, -1, -1, -1, -1, -1, 1, -1, -1, -1, -1, 1, 1, -1, -1, -1, 1, 1, 1, -1,
                      -1, 1, 1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 1, -1, -1, -1, -1,
                      -1, -1, -1, 1, -1, -1, 1, -1, 1, -1, -1, 1, 1, -1, 1, -1, -1, -1, -1, 1, 1, 1, 1,
                      1, 1, -1, 1, 1, 1, -1, 1, 1, 1, -1, -1, -1, 1, -1, 1, -1, -1, 1, 1, -1, -1, 1, -1,
                      1, -1, 1, 1, 1, -1, -1, -1, -1}},
             new int[] {-1, -1, -1, 2, 3, 4, 5, 6, -1, -1, 9, 10, 11, 14, -1, -1, 15, 16, 19, 20,
                        -1, -1, 21, 24, -1, -1, 25, -1, -1, 28, 29, 30, 31, 32, 33, 34, 35, -1, -1,
                        -1, -1, 40, 41, 42, 43, 44, 45, -1, -1, 48, -1, -1, -1, -1, 53, 56, -1, -1,
                        -1, -1, 59, 60, 61, 64, 65, 66, 67, 68, -1, -1, 71, 72, -1, -1, 75, 76, -1,
                        -1, 77, 78, -1, -1, -1, -1, 83, 86, -1, -1, 87, -1, -1, -1, -1, 94, 95, -1,
                        -1, 96, 97, 98});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
