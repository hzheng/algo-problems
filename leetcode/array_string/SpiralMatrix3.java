import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC889: https://leetcode.com/problems/spiral-matrix-iii/
//
// On a 2 dimensional grid with R rows and C columns, we start at (r0, c0)
// facing east. Here, the north-west corner of the grid is at the first row and
// column, and the south-east corner of the grid is at the last row and column.
// We walk in a clockwise spiral shape to visit every position in this grid.
// Whenever we would move outside the boundary of the grid, we continue our walk
// outside the grid (but may return to the grid boundary later.)
// Eventually, we reach all R * C spaces of the grid. Return a list of
// coordinates representing the positions of the grid in the order of visit.
public class SpiralMatrix3 {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    // beats %(9 ms for 73 tests)
    public int[][] spiralMatrixIII(int R, int C, int r0, int c0) {
        int n = R * C;
        int[][] res = new int[n][2];
        for (int x = r0, y = c0, i = 0, j = 1, dir = 0, scale = 2; ; j++) {
            if (x >= 0 && x < R && y >= 0 && y < C) {
                res[i] = new int[] {x, y};
                if (++i == n) return res;
            }
            x += MOVES[dir][0];
            y += MOVES[dir][1];
            if (j == scale / 2) {
                scale++;
                j = 0;
                dir = (dir + 1) % 4;
            }
        }
    }

    // beats %(8 ms for 73 tests)
    public int[][] spiralMatrixIII_2(int R, int C, int r0, int c0) {
        int n = R * C;
        int[][] res = new int[n][2];
        for (int i = 0, j = 1, x = 0, y = 1, scale = 2, r = r0, c = c0; ; j++) {
            if (r >= 0 && r < R && c >= 0 && c < C) {
                res[i] = new int[] {r, c};
                if (++i == n) return res;
            }
            r += x;
            c += y;
            if (j == scale / 2) {
                scale++;
                j = 0; 
                int tmp = x; // cross product: (x, y, 0) × (0, 0, 1) = (y, -x, 0)
                x = y;
                y = -tmp;
            }
        }
    }

    // beats %(10 ms for 73 tests)
    public int[][] spiralMatrixIII2(int R, int C, int r0, int c0) {
        int n = R * C;
        int[][] res = new int[n][2];
        int i = 0;
        res[i++] = new int[]{r0, c0};
        if (n == 1) return res;

        for (int j = 1, r = r0, c = c0; ; j += 2) {
            for (int dir = 0; dir < 4; dir++) {
                for (int k = j + (dir / 2); k > 0; k--) {
                    r += MOVES[dir][0];
                    c += MOVES[dir][1];
                    if (r >= 0 && r < R && c >= 0 && c < C) {
                        res[i++] = new int[]{r, c};
                        if (i == n) return res;
                    }
                }
            }
        }
    }

    // beats %(10 ms for 73 tests)
    public int[][] spiralMatrixIII3(int R, int C, int r0, int c0) {
        int n = R * C;
        int[][] res = new int[n][2];
        res[0] = new int[]{r0, c0};
        for (int i = 1, r = r0, c = c0, d = 0, step = 0; i < n; d = (++d) % 4) {
            step += (d + 1) & 1; // increase by 1 when moving east or west
            for (int j = step; j > 0; j--) {
                r += MOVES[d][0];
                c += MOVES[d][1];
                if (r >= 0 && r < R && c >= 0 && c < C) {
                    res[i] = new int[]{r, c};
                    if (++i == n) return res;
                }
            }
        }
        return res;
    }

    void test(int R, int C, int r0, int c0, int[][] expected) {
        assertArrayEquals(expected, spiralMatrixIII(R, C, r0, c0));
        assertArrayEquals(expected, spiralMatrixIII_2(R, C, r0, c0));
        assertArrayEquals(expected, spiralMatrixIII2(R, C, r0, c0));
        assertArrayEquals(expected, spiralMatrixIII3(R, C, r0, c0));
    }

    @Test
    public void test() {
        test(1, 4, 0, 0,
             new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 } });
        test(5, 6, 1, 4,
             new int[][] { { 1, 4 }, { 1, 5 }, { 2, 5 }, { 2, 4 }, { 2, 3 },
                           { 1, 3 }, { 0, 3 }, { 0, 4 }, { 0, 5 },
                           { 3, 5 }, { 3, 4 }, { 3, 3 }, { 3, 2 }, { 2, 2 },
                           { 1, 2 }, { 0, 2 }, { 4, 5 }, { 4, 4 },
                           { 4, 3 }, { 4, 2 }, { 4, 1 }, { 3, 1 }, { 2, 1 },
                           { 1, 1 }, { 0, 1 }, { 4, 0 }, { 3, 0 },
                           { 2, 0 }, { 1, 0 }, { 0, 0 } });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
