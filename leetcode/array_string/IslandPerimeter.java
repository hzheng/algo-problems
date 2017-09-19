import org.junit.Test;
import static org.junit.Assert.*;

// LC463: https://leetcode.com/problems/island-perimeter/
//
// You are given a map in form of a two-dimensional integer grid where 1
// represents land and 0 represents water. Grid cells are connected
// horizontally/vertically (not diagonally). The grid is completely surrounded
// by water, and there is exactly one island (i.e., one or more connected land
// cells). The island doesn't have "lakes" (water inside that isn't connected to
// the water around the island). One cell is a square with side length 1. The
// grid is rectangular, width and height don't exceed 100. Determine the
// perimeter of the island.
public class IslandPerimeter {
    // beats N/A(131 ms for 5833 tests)
    public int islandPerimeter(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int perimeter = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) continue;

                if (i == 0 || grid[i - 1][j] == 0) {
                    perimeter++;
                }
                if (i == m - 1 || grid[i + 1][j] == 0) {
                    perimeter++;
                }
                if (j == 0 || grid[i][j - 1] == 0) {
                    perimeter++;
                }
                if (j == n - 1 || grid[i][j + 1] == 0) {
                    perimeter++;
                }
            }
        }
        return perimeter;
    }

    // Recursion + DFS
    // beats N/A(149 ms for 5833 tests)
    public int islandPerimeter2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] perimeter = new int[1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    calcPerimeter(grid, i, j, perimeter);
                    return perimeter[0];
                }
            }
        }
        return 0;
    }

    private void calcPerimeter(int[][] grid, int i, int j, int[] perimeter) {
        int m = grid.length;
        int n = grid[0].length;
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] != 1) return;

        if (i == 0 || grid[i - 1][j] == 0) {
            perimeter[0]++;
        }
        if (i == m - 1 || grid[i + 1][j] == 0) {
            perimeter[0]++;
        }
        if (j == 0 || grid[i][j - 1] == 0) {
            perimeter[0]++;
        }
        if (j == n - 1 || grid[i][j + 1] == 0) {
            perimeter[0]++;
        }
        grid[i][j] = -1; // mark it as visited
        calcPerimeter(grid, i, j + 1, perimeter);
        calcPerimeter(grid, i, j - 1, perimeter);
        calcPerimeter(grid, i + 1, j, perimeter);
        calcPerimeter(grid, i - 1, j, perimeter);
    }

    // beats N/A(144 ms for 5833 tests)
    public int islandPerimeter3(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int islands = 0;
        int neighbours = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    islands++;
                    if (i < m - 1 && grid[i + 1][j] == 1) {
                        neighbours++;
                    }
                    if (j < n - 1 && grid[i][j + 1] == 1) {
                        neighbours++;
                    }
                }
            }
        }
        return islands * 4 - neighbours * 2;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, islandPerimeter(grid));
        assertEquals(expected, islandPerimeter3(grid));
        assertEquals(expected, islandPerimeter2(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 1, 0, 0}, {1, 1, 1, 0}, {0, 1, 0, 0}, {1, 1, 0, 0}}, 16);
        test(new int[][] {{1}}, 4);
        test(new int[][] {{1, 1}, {1, 1}}, 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IslandPerimeter");
    }
}
