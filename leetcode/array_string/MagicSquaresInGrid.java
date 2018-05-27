import org.junit.Test;
import static org.junit.Assert.*;

// LC840: https://leetcode.com/problems/magic-squares-in-grid/
//
// A 3 x 3 magic square is a 3 x 3 grid filled with distinct numbers from 1 to 9
// such that each row, column, and both diagonals all have the same sum.
// Given an grid of integers, how many 3 x 3 "magic square" subgrids are there?
// 1 <= grid.length <= 10
// 1 <= grid[0].length <= 10
// 0 <= grid[i][j] <= 15
public class MagicSquaresInGrid {
    private static final int SUM = 15;

    // beats %(8 ms for 87 tests)
    public int numMagicSquaresInside(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int res = 0;
        for (int i = 0; i < n - 2; i++) {
            outer : for (int j = 0; j < m - 2; j++) {
                int x = 1;
                for (int k = 0; k < 3; k++) {
                    for (int p = 0; p < 3; p++) {
                        x |= 1 << grid[i + k][j + p];
                    }
                }
                if (x != (1 << 10) - 1) continue;

                int sum = 0;
                for (int k = 0; k < 3; k++) {
                    sum += grid[i + k][j + k];
                }
                if (sum != SUM) continue;

                sum = 0;
                for (int k = 0; k < 3; k++) {
                    sum += grid[i + k][j - k + 2];
                }
                if (sum != SUM) continue;

                for (int k = 0; k < 3; k++) {
                    sum = 0;
                    for (int p = 0; p < 3; p++) {
                        sum += grid[i + k][j + p];
                    }
                    if (sum != SUM) continue outer;
                }
                for (int k = 0; k < 3; k++) {
                    sum = 0;
                    for (int p = 0; p < 3; p++) {
                        sum += grid[i + p][j + k];
                    }
                    if (sum != SUM) continue outer;
                }
                res++;
            }
        }
        return res;
    }

    // beats %(11 ms for 87 tests)
    public int numMagicSquaresInside2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int res = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = 0; j < m - 2; j++) {
                if (magic(grid[i][j], grid[i][j + 1], grid[i][j + 2],
                          grid[i + 1][j], grid[i + 1][j + 1],
                          grid[i + 1][j + 2], grid[i + 2][j],
                          grid[i + 2][j + 1], grid[i + 2][j + 2])) {
                    res++;
                }
            }
        }
        return res;
    }

    public boolean magic(int ... nums) {
        boolean[] numSet = new boolean[16];
        for (int num : nums) {
            numSet[num] = true;
        }
        for (int num = 1; num <= 9; num++) {
            if (!numSet[num]) return false;
        }
        return (nums[0] + nums[1] + nums[2] == SUM
                && nums[3] + nums[4] + nums[5] == SUM
                && nums[6] + nums[7] + nums[8] == SUM
                && nums[0] + nums[3] + nums[6] == SUM
                && nums[1] + nums[4] + nums[7] == SUM
                && nums[2] + nums[5] + nums[8] == SUM
                && nums[0] + nums[4] + nums[8] == SUM
                && nums[2] + nums[4] + nums[6] == SUM);
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, numMagicSquaresInside(grid));
        assertEquals(expected, numMagicSquaresInside2(grid));
    }

    @Test
    public void test() {
        test(new int[][] { { 10, 3, 5 }, { 1, 6, 11 }, { 7, 9, 2 } }, 0);
        test(new int[][] { { 4, 3, 8, 4 }, { 9, 5, 1, 9 }, { 2, 7, 6, 2 } }, 1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
