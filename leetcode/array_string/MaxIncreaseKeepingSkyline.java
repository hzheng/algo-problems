import org.junit.Test;
import static org.junit.Assert.*;

// LC807: https://leetcode.com/problems/max-increase-to-keep-city-skyline
//
// In a 2 dimensional array grid, each value grid[i][j] represents the height of
// a building located there. We are allowed to increase the height of any number
// of buildings, by any amount (the amounts can be different for different
// buildings). Height 0 is considered to be a building as well.
// At the end, the "skyline" when viewed from all four directions of the grid,
// i.e. top, bottom, left, and right, must be the same as the skyline of the
// original grid. A city's skyline is the outer contour of the rectangles formed
// by all the buildings when viewed from a distance.
// What is the maximum total sum that the height of the buildings can be increased?
public class MaxIncreaseKeepingSkyline {
    // beats %(13 ms for 133 tests)
    public int maxIncreaseKeepingSkyline(int[][] grid) {
        int n = grid.length;
        int[] maxRow = new int[n];
        int[] maxCol = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                maxRow[i] = Math.max(maxRow[i], grid[i][j]);
                maxCol[j] = Math.max(maxCol[j], grid[i][j]);
            }
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                res += Math.min(maxRow[j], maxCol[i]) - grid[i][j];
            }
        }
        return res;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, maxIncreaseKeepingSkyline(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{3, 0, 8, 4}, {2, 4, 5, 7}, {9, 2, 6, 3},
                          {0, 3, 1, 0}}, 35);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
