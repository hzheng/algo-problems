import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1254: https://leetcode.com/problems/number-of-closed-islands/
//
// Given a 2D grid consists of 0s (land) and 1s (water).  An island is a maximal 4-directionally
// connected group of 0s and a closed island is an island totally (all left, top, right, bottom)
// surrounded by 1s. Return the number of closed islands.
// Constraints:
// 1 <= grid.length, grid[0].length <= 100
// 0 <= grid[i][j] <=1
public class ClosedIsland {
    private static final int LAND = 0;
    private static final int WATER = 1;
    private static final int MARK = 2;
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // BFS + Queue
    // time complexity: O(N * M), space complexity: O(N * M)
    // 3 ms(39.85%), 43.3 MB(100%) for 47 tests
    public int closedIsland(int[][] grid) {
        int nRow = grid.length;
        int nCol = grid[0].length;
        int res = 0;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] == LAND) {
                    res += markIsland(grid, nRow, nCol, i, j);
                }
            }
        }
        // restore grid if required
        return res;
    }

    private int markIsland(int[][] grid, int nRow, int nCol, int startX, int startY) {
        Queue<int[]> island = new LinkedList<>();
        island.offer(new int[]{startX, startY});
        grid[startX][startY] = MARK;
        int valid = 1;
        while (!island.isEmpty()) {
            int[] pos = island.poll();
            for (int[] dir : MOVES) {
                int x = pos[0] + dir[0];
                int y = pos[1] + dir[1];
                if (x < 0 || y < 0 || x >= nRow || y >= nCol) {
                    valid = 0;
                } else if (grid[x][y] == LAND) {
                    island.offer(new int[]{x, y});
                    grid[x][y] = MARK;
                }
            }
        }
        return valid;
    }

    // DFS + Recursion
    // time complexity: O(N * M), space complexity: O(N * M)
    // 2 ms(88.52%), 43.1 MB(100%) for 47 tests
    public int closedIsland2(int[][] grid) {
        int nRow = grid.length;
        int nCol = grid[0].length;
        int res = 0;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] == LAND) {
                    res += dfs(grid, nRow, nCol, i, j);
                }
            }
        }
        // restore grid if required
        return res;
    }

    private int dfs(int[][] grid, int nRow, int nCol, int startX, int startY) {
        grid[startX][startY] = MARK;
        int valid = 1;
        for (int[] dir : MOVES) {
            int x = startX + dir[0];
            int y = startY + dir[1];
            if (x < 0 || y < 0 || x >= nRow || y >= nCol) {
                valid = 0;
            } else if (grid[x][y] == LAND) {
                valid &= dfs(grid, nRow, nCol, x, y);
            }
        }
        return valid;
    }

    // Solution of Choice
    // DFS + Recursion
    // time complexity: O(N * M), space complexity: O(N * M)
    // 1 ms(100.00%), 43.1 MB(100%) for 47 tests
    public int closedIsland3(int[][] grid) {
        int nRow = grid.length;
        int nCol = grid[0].length;
        int res = 0;
        for (int x = 0; x < nRow; x++) {
            for (int y = 0; y < nCol; y++) {
                if (grid[x][y] == LAND) {
                    res += dfs(grid, x, y) ? 1 : 0;
                }
            }
        }
        return res;
    }

    private boolean dfs(int[][] grid, int x, int y) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) { return false; }

        if (grid[x][y] != LAND) { return true; }

        grid[x][y] = MARK;
        boolean res = true;
        for (int[] dir : MOVES) {
            res &= dfs(grid, x + dir[0], y + dir[1]);
        }
        // restore grid if required
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N * M), space complexity: O(N * M)
    // 2 ms(88.52%), 43.1 MB(100%) for 47 tests
    public int closedIsland4(int[][] grid) {
        int nRow = grid.length;
        int nCol = grid[0].length;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (i * j * (i - nRow + 1) * (j - nCol + 1) == 0) {
                    fill(grid, i, j);
                }
            }
        }
        int res = 0;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                res += fill(grid, i, j);
            }
        }
        // restore grid if required
        return res;
    }

    private int fill(int[][] grid, int x, int y) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[x].length || grid[x][y] != LAND) {
            return 0;
        }
        grid[x][y] = MARK;
        fill(grid, x + 1, y);
        fill(grid, x, y + 1);
        fill(grid, x - 1, y);
        fill(grid, x, y - 1);
        return 1;
    }

    // Union Find
    // time complexity: O(N * M * log(K)), space complexity: O(N * M)
    // 2 ms(88.52%), 43.1 MB(100%) for 47 tests
    public int closedIsland5(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] id = new int[m * n];
        Arrays.fill(id, -1);
        int islands = 0;
        for (int x = 0; x < m; x++) {
            if (grid[x][0] != WATER) {
                islands -= count(grid, id, x, 0);
            }
            if (grid[x][n - 1] != WATER) {
                islands -= count(grid, id, x, n - 1);
            }
        }
        for (int y = 1; y < n - 1; y++) {
            if (grid[0][y] != WATER) {
                islands -= count(grid, id, 0, y);
            }
            if (grid[m - 1][y] != WATER) {
                islands -= count(grid, id, m - 1, y);
            }
        }
        for (int x = 1; x < m - 1; x++) {
            for (int y = 1; y < n - 1; y++) {
                if (grid[x][y] == WATER) { continue; }

                islands += count(grid, id, x, y);
            }
        }
        return islands;
    }

    private int count(int[][] grid, int[] id, int x, int y) {
        int m = grid.length;
        int n = grid[0].length;
        int islands = 1;
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == LAND) {
                islands -= union(id, x * n + y, nx * n + ny) ? 1 : 0;
            }
        }
        return islands;
    }

    private int parent(int[] id, int x) {
        int p = x;
        for (; id[p] >= 0; p = id[p]) {}
        return p;
    }

    private boolean union(int[] id, int x, int y) {
        int px = parent(id, x);
        int py = parent(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    private void reset(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == MARK) {
                    grid[i][j] = LAND;
                }
            }
        }
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, closedIsland(grid));
        reset(grid);
        assertEquals(expected, closedIsland2(grid));
        reset(grid);
        assertEquals(expected, closedIsland3(grid));
        reset(grid);
        assertEquals(expected, closedIsland4(grid));
        reset(grid);
    }

    @Test
    public void test() {
        test(new int[][]{{1, 1, 1, 1, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 1, 0},
                         {1, 0, 1, 0, 1, 1, 1, 0}, {1, 0, 0, 0, 0, 1, 0, 1},
                         {1, 1, 1, 1, 1, 1, 1, 0}}, 2);
        test(new int[][]{{1, 1, 1, 1, 1, 1, 1},
                         {1, 0, 0, 0, 0, 0, 1},
                         {1, 0, 1, 1, 1, 0, 1},
                         {1, 0, 1, 0, 1, 0, 1},
                         {1, 0, 1, 1, 1, 0, 1},
                         {1, 0, 0, 0, 0, 0, 1},
                         {1, 1, 1, 1, 1, 1, 1}}, 2);
        test(new int[][]{{1, 1, 0, 1, 1, 1, 1, 1, 1, 1}, {0, 0, 1, 0, 0, 1, 0, 1, 1, 1},
                         {1, 0, 1, 0, 0, 0, 1, 0, 1, 0}, {1, 1, 1, 1, 1, 0, 0, 1, 0, 0},
                         {1, 0, 1, 0, 1, 1, 1, 1, 1, 0}, {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
                         {1, 0, 1, 0, 0, 0, 0, 1, 1, 0}, {1, 1, 0, 0, 1, 1, 0, 0, 0, 0},
                         {0, 0, 0, 1, 1, 0, 1, 1, 1, 0}, {1, 1, 0, 1, 0, 1, 0, 0, 1, 0}}, 4);
        test(new int[][]{{1, 0, 1, 1, 1, 1, 0, 0, 1, 0}, {1, 0, 1, 1, 0, 0, 0, 1, 1, 1},
                         {0, 1, 1, 0, 0, 0, 1, 0, 0, 0}, {1, 0, 1, 1, 0, 1, 0, 0, 1, 0},
                         {0, 1, 1, 1, 0, 1, 0, 1, 0, 0}, {1, 0, 0, 1, 0, 0, 1, 0, 0, 0},
                         {1, 0, 1, 1, 1, 0, 0, 1, 1, 0}, {1, 1, 0, 1, 1, 0, 1, 0, 1, 1},
                         {0, 0, 1, 1, 1, 0, 1, 0, 1, 1}, {1, 0, 0, 1, 1, 1, 1, 0, 1, 1}}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
