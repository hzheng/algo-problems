import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.Utils;

// LC1034: https://leetcode.com/problems/coloring-a-border/
//
// Given a 2-dimensional grid of integers, each value in the grid represents the color of the grid
// square at that location. Two squares belong to the same connected component if and only if they
// have the same color and are next to each other in any of the 4 directions.
// The border of a connected component is all the squares in the connected component that are either
// 4-directionally adjacent to a square not in the component, or on the boundary of the grid (the
// first or last row or column).
// Given a square at location (r0, c0) in the grid and a color, color the border of the connected
// component of that square with the given color, and return the final grid.
public class ColorBorder {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // BFS + Queue + Set
    // 2 ms(44.09%), 48.8 MB(100%) for 154 tests
    public int[][] colorBorder(int[][] grid, int r0, int c0, int color) {
        int r = grid.length;
        int c = grid[0].length;
        List<int[]> border = new LinkedList<>();
        Queue<int[]> queue = new LinkedList<>();
        int oldColor = grid[r0][c0];
        Set<Integer> visited = new HashSet<>();
        for (queue.offer(new int[]{r0, c0}); !queue.isEmpty(); ) {
            int[] cur = queue.poll();
            int x = cur[0];
            int y = cur[1];
            if (x == 0 || y == 0 || x == r - 1 || y == c - 1
                || grid[x - 1][y] != oldColor || grid[x + 1][y] != oldColor
                || grid[x][y - 1] != oldColor || grid[x][y + 1] != oldColor) {
                border.add(cur);
            }
            for (int[] dir : DIRS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx >= 0 && ny >= 0 && nx < r && ny < c
                    && grid[nx][ny] == oldColor && visited.add(nx * c + ny)) {
                    queue.offer(new int[]{nx, ny});
                }
            }
        }
        for (int[] b : border) {
            grid[b[0]][b[1]] = color;
        }
        return grid;
    }

    // BFS + Queue + Set
    // 2 ms(44.09%), 45.8 MB(100%) for 154 tests
    public int[][] colorBorder2(int[][] grid, int r0, int c0, int color) {
        int r = grid.length;
        int c = grid[0].length;
        Set<Integer> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        int oldColor = grid[r0][c0];
        visited.add(r0 * c + c0);
        for (queue.offer(new int[]{r0, c0}); !queue.isEmpty(); ) {
            int[] cur = queue.poll();
            int x = cur[0];
            int y = cur[1];
            if (x == 0 || x == r - 1 || y == 0 || y == c - 1) {
                grid[x][y] = color;
            }
            for (int[] dir : DIRS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx < 0 || nx >= r || ny < 0 || ny >= c || visited.contains(nx * c + ny)) {
                    continue;
                }
                if (grid[nx][ny] == oldColor) {
                    visited.add(nx * c + ny);
                    queue.offer(new int[]{nx, ny});
                } else {
                    grid[x][y] = color;
                }
            }
        }
        return grid;
    }

    // DFS + Recursion
    // 1 ms(99.41%), 46.8 MB(100%) for 154 tests
    public int[][] colorBorder3(int[][] grid, int r0, int c0, int color) {
        dfs(grid, r0, c0, grid[r0][c0]);
        for (int[] g : grid) {
            for (int i = 0; i < g.length; i++) {
                if (g[i] < 0) {
                    g[i] = color;
                }
            }
        }
        return grid;
    }

    private void dfs(int[][] grid, int x, int y, int oldColor) {
        grid[x][y] = -oldColor;
        int cnt = 0; // use to count grid[r][c]'s component neighbors (same color as itself).
        for (int[] dir : DIRS) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length
                && Math.abs(grid[nx][ny]) == oldColor) {
                cnt++;
                if (grid[nx][ny] == oldColor) {
                    dfs(grid, nx, ny, oldColor);
                }
            }
        }
        if (cnt == 4) { // recover the inner color
            grid[x][y] = oldColor;
        }
    }

    void test(int[][] grid, int r0, int c0, int color, int[][] expected) {
        assertArrayEquals(expected, colorBorder(Utils.clone(grid), r0, c0, color));
        assertArrayEquals(expected, colorBorder2(Utils.clone(grid), r0, c0, color));
        assertArrayEquals(expected, colorBorder3(Utils.clone(grid), r0, c0, color));
    }

    @Test
    public void test() {
        test(new int[][]{{1, 1}, {1, 2}}, 0, 0, 3, new int[][]{{3, 3}, {3, 2}});
        test(new int[][]{{1, 2, 2}, {2, 3, 2}}, 0, 1, 3, new int[][]{{1, 3, 3}, {2, 3, 3}});
        test(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, 1, 1, 2,
             new int[][]{{2, 2, 2}, {2, 1, 2}, {2, 2, 2}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
