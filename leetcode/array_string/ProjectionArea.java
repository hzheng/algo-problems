import org.junit.Test;
import static org.junit.Assert.*;

// LC883: https://leetcode.com/problems/projection-area-of-3d-shapes/
//
// On a N * N grid, we place some 1 * 1 * 1 cubes that are axis-aligned with the
// x, y, and z axes. Each value v = grid[i][j] represents a tower of v cubes
// placed on top of grid cell (i, j). Now we view the projection of these cubes 
// onto the xy, yz, and zx planes. we are viewing the "shadow" when looking at
// the cubes from the top, the front, and the side.
// Return the total area of all three projections.
public class ProjectionArea {
    // beats %(6 ms for 90 tests)
    public int projectionArea(int[][] grid) {
        int n = grid.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            int maxRow = 0;
            int maxCol = 0;
            for (int j = 0; j < n; j++) {
                maxRow = Math.max(maxRow, grid[i][j]);
                maxCol = Math.max(maxCol, grid[j][i]);
                if (grid[i][j] > 0) {
                    res++;
                }
            }
            res += maxRow + maxCol;
        }
        return res;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, projectionArea(grid));
    }

    @Test
    public void test() {
        test(new int[][] { { 2 } }, 5);
        test(new int[][] { { 1, 2 }, { 3, 4 } }, 17);
        test(new int[][] { { 2, 3 }, { 2, 4 } }, 17);
        test(new int[][] { { 1, 0 }, { 0, 2 } }, 8);
        test(new int[][] { { 1, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 } }, 14);
        test(new int[][] { { 2, 2, 2 }, { 2, 1, 2 }, { 2, 2, 2 } }, 21);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
