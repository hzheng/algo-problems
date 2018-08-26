import org.junit.Test;
import static org.junit.Assert.*;

// LC892: https://leetcode.com/problems/surface-area-of-3d-shapes/
//
// On a N * N grid, we place some 1 * 1 * 1 cubes. Each value v = grid[i][j] 
// represents a tower of v cubes placed on top of grid cell (i, j).
// Return the total surface area of the resulting shapes.
public class SurfaceArea {
    // beats %(11 ms for 90 tests)
    public int surfaceArea(int[][] grid) {
        int res = 0;
        int n = grid.length;
        for (int i = 0; i < n; i++) {
            if (grid[i][0] > 0) {
                res += 2;
            }
            res += 2 * grid[i][0];
            res += 2 * grid[0][i];
            for (int j = 1; j < n; j++) {
                if (grid[i][j] > 0) {
                    res += 2;
                }

                res += 2 * grid[i][j];
                res -= 2 * Math.min(grid[i][j], grid[i][j - 1]);

                res += 2 * grid[j][i];
                res -= 2 * Math.min(grid[j][i], grid[j - 1][i]);
            }
        }
        return res;
    }

    // beats %(9 ms for 90 tests)
    public int surfaceArea2(int[][] grid) {
        int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        int n = grid.length;
        int res = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 0) continue;

                res += 2;
                for (int[] move : MOVES) {
                    int nr = r + move[0];
                    int nc = c + move[1];
                    int v = 0;
                    if (nr >= 0 && nr < n && nc >= 0 && nc < n) {
                        v = grid[nr][nc];
                    }
                    res += Math.max(grid[r][c] - v, 0);
                }
            }
        }
        return res;
    }

    // beats %(8 ms for 90 tests)
    public int surfaceArea3(int[][] grid) {
        int res = 0;
        int n = grid.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) continue;

                res += grid[i][j] * 4 + 2;
                if (i > 0) {
                    res -= Math.min(grid[i][j], grid[i - 1][j]) * 2;
                }
                if (j > 0) {
                    res -= Math.min(grid[i][j], grid[i][j - 1]) * 2;
                }
            }
        }
        return res;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, surfaceArea(grid));
        assertEquals(expected, surfaceArea2(grid));
        assertEquals(expected, surfaceArea3(grid));
    }

    @Test
    public void test() {
        test(new int[][] { { 2 } }, 10);
        test(new int[][] { { 1, 2 }, { 3, 4 } }, 34);
        test(new int[][] { { 2, 3 }, { 2, 4 } }, 34);
        test(new int[][] { { 1, 0 }, { 0, 2 } }, 16);
        test(new int[][] { { 1, 0 }, { 5, 4 } }, 36);
        test(new int[][] { { 1, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 } }, 32);
        test(new int[][] { { 2, 2, 2 }, { 2, 1, 2 }, { 2, 2, 2 } }, 46);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
