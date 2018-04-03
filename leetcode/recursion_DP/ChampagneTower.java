import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC799: https://leetcode.com/problems/champagne-tower/
//
// We stack glasses in a pyramid, where the first row has 1 glass, the second
// row has 2 glasses, and so on until the 100th row.  Each glass holds one cup
// (250ml) of champagne. Then, some champagne is poured in the first glass at
// the top.  When the top most glass is full, any excess liquid poured will fall
// equally to the glass immediately to the left and right of it.  When those
// glasses become full, any excess champagne will fall equally to the left and
// right of those glasses, and so on.  (A glass at the bottom row has it's
// excess champagne fall on the floor.)
// For example, after one cup of champagne is poured, the top most glass is full.
// After two cups of champagne are poured, the two glasses on the second row are
// half full.  After three cups of champagne are poured, those two cups become
// full - there are 3 full glasses total now.  After four cups of champagne are
// poured, the third row has the middle glass half full, and the two outside
// glasses are a quarter full.
// Now after pouring some non-negative integer cups of champagne, return how
// full the j-th glass in the i-th row is (both i and j are 0 indexed.)
// Note:
// poured will be in the range of [0, 10 ^ 9].
// query_glass and query_row will be in the range of [0, 99].
public class ChampagneTower {
    // 2-D Dynamic Programming
    // beats 91.02%(27 ms for 309 tests)
    public double champagneTower(int poured, int query_row, int query_glass) {
        double[][] dp = new double[query_row + 2][query_row + 2];
        dp[0][0] = poured;
        for (int row = 0; row <= query_row; row++) {
            for (int col = 0; col <= row; col++) {
                if (dp[row][col] >= 1) {
                    dp[row + 1][col] += (dp[row][col] - 1) / 2;
                    dp[row + 1][col + 1] += (dp[row][col] - 1) / 2;
                }
            }
        }
        return Math.min(1, dp[query_row][query_glass]);
    }

    // 1-D Dynamic Programming
    // beats 95.93%(26 ms for 309 tests)
    public double champagneTower2(int poured, int query_row, int query_glass) {
        double[] dp = new double[query_row + 2];
        dp[0] = poured;
        for (int row = 1; row <= query_row; row++) {
            for (int col = row; col >= 0; col--) {
                dp[col] = Math.max(0.0, (dp[col] - 1) / 2);
                dp[col + 1] += dp[col];
            }
        }
        return Math.min(1, dp[query_glass]);
    }

    void test(int poured, int row, int glass, double expected) {
        assertEquals(expected, champagneTower(poured, row, glass), 1e-6);
        assertEquals(expected, champagneTower2(poured, row, glass), 1e-6);
    }

    @Test
    public void test() {
        test(1, 0, 0, 1.0);
        test(2, 0, 0, 1.0);
        test(1, 1, 1, 0.0);
        test(2, 1, 1, 0.5);
        test(1000, 33, 10, 0.679137);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
