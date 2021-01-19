import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1210: https://leetcode.com/problems/minimum-moves-to-reach-target-with-rotations/
//
// In an n*n grid, there is a snake that spans 2 cells and starts moving from the top left corner at
// (0, 0) and (0, 1). The grid has empty cells represented by zeros and blocked cells represented by
// ones. The snake wants to reach the lower right corner at (n-1, n-2) and (n-1, n-1).
// In one move the snake can:
// Move one cell to the right if there are no blocked cells there. This move keeps the
// horizontal/vertical position of the snake as it is.
// Move down one cell if there are no blocked cells there. This move keeps the horizontal/vertical
// position of the snake as it is.
// Rotate clockwise if it's in a horizontal position and the two cells under it are both empty. In
// that case the snake moves from (r, c) and (r, c+1) to (r, c) and (r+1, c)
// Rotate counterclockwise if it's in a vertical position and the two cells to its right are both
// empty. In that case the snake moves from (r, c) and (r+1, c) to (r, c) and (r, c+1).
// Return the minimum number of moves to reach the target.
// If there is no way to reach the target, return -1.
//
// Constraints:
// 2 <= n <= 100
// 0 <= grid[i][j] <= 1
// It is guaranteed that the snake starts at empty cells.
public class SnakeMove {
    private static final int[][] MOVES = {{0, 1, 0}, {1, 0, 0}, {0, 0, 1}, {0, 0, -1}};

    // BFS + Queue + Set
    // time complexity: O(N^2), space complexity: O(N^2)
    // 13 ms(95.16%), 40.1 MB(82.26%) for 42 tests
    public int minimumMoves(int[][] grid) {
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[3]);
        int m = grid.length;
        int n = grid[0].length;
        boolean[][][] visited = new boolean[m][n][2];
        for (int step = 0; !queue.isEmpty(); step++) {
            for (int k = queue.size(); k > 0; k--) {
                int[] cur = queue.poll();
                int x = cur[0];
                int y = cur[1];
                int z = cur[2];
                if (x == n - 1 && y == n - 2 && z == 0) { return step; }
                if (visited[x][y][z]) { continue; }

                visited[x][y][z] = true;
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    int nz = z + move[2];
                    if (nz < 0 || nz > 1) { continue; }
                    if (z == 0 && (nz == 1 || move[0] == 1)) {
                        if (x + 1 >= m || grid[x + 1][y] == 1) { continue; }
                        if (y + 1 >= n || grid[x + 1][y + 1] == 1) { continue; }
                    } else if (z == 0) {
                        if (y + 2 >= n || grid[x][y + 2] == 1) { continue; }
                    } else if (nz == 0 || move[0] == 0) {
                        if (y + 1 >= n || grid[x][y + 1] == 1) { continue; }
                        if (x + 1 >= m || grid[x + 1][y + 1] == 1) { continue; }
                    } else if (x + 2 >= m || grid[x + 2][y] == 1) { continue; }
                    queue.offer(new int[] {nx, ny, nz});
                }
            }
        }
        return -1;
    }

    private void test(int[][] grid, int expected) {
        assertEquals(expected, minimumMoves(grid));
    }

    @Test public void test() {
        test(new int[][] {{0, 0, 0, 0, 0, 1}, {1, 1, 0, 0, 1, 0}, {0, 0, 0, 0, 1, 1},
                          {0, 0, 1, 0, 1, 0}, {0, 1, 1, 0, 0, 0}, {0, 1, 1, 0, 0, 0}}, 11);
        test(new int[][] {{0, 0, 1, 1, 1, 1}, {0, 0, 0, 0, 1, 1}, {1, 1, 0, 0, 0, 1},
                          {1, 1, 1, 0, 0, 1}, {1, 1, 1, 0, 0, 1}, {1, 1, 1, 0, 0, 0}}, 9);
        test(new int[][] {{0, 0, 1, 1, 1, 1}, {0, 0, 0, 0, 1, 1}, {1, 1, 0, 0, 0, 1},
                          {1, 1, 1, 0, 0, 1}, {1, 1, 1, 0, 0, 1}, {1, 1, 1, 1, 0, 0}}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
