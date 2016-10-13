import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC174: https://leetcode.com/problems/dungeon-game/
//
// The demons had captured the princess (P) and imprisoned her in the
// bottom-right corner of a dungeon. The dungeon consists of M x N rooms laid
// out in a 2D grid. Our valiant knight (K) was initially positioned in the
// top-left room and must fight his way through the dungeon to rescue the
// princess. The knight has an initial health point represented by a positive
// integer. If at any point his health point drops to 0 or below, he dies
// immediately. Some of the rooms are guarded by demons, so the knight loses
// health (negative integers) upon entering these rooms; other rooms are either
// empty (0's) or contain magic orbs that increase the knight's health
// (positive integers). In order to reach the princess as quickly as possible,
// the knight decides to move only rightward or downward in each step. Determine
// the knight's minimum initial health so that he is able to rescue the princess.
public class DungeonGame {
    // 2-D Dynamic Programming
    // beats 51.65%(4 ms)
    public int calculateMinimumHP(int[][] dungeon) {
        int row = dungeon.length;
        if (row == 0) return 0;

        int col = dungeon[0].length;
        int[][] minHP = new int[row][col];
        minHP[row - 1][col - 1] = min(dungeon[row - 1][col - 1], 1);
        for (int j = col - 2; j >= 0; j--) {
            minHP[row - 1][j] = min(dungeon[row - 1][j], minHP[row - 1][j + 1]);
        }
        for (int i = row - 2; i >= 0; i--) {
            minHP[i][col - 1] = min(dungeon[i][col - 1], minHP[i + 1][col - 1]);
            for (int j = col - 2; j >= 0; j--) {
                minHP[i][j] = min(dungeon[i][j],
                                  Math.min(minHP[i + 1][j], minHP[i][j + 1]));
            }
        }
        return minHP[0][0];
    }

    private int min(int dungeon, int min) {
        return Math.max(1, min - dungeon);
    }

    // Solution of Choice
    // 1-D Dynamic Programming
    // beats 62.59%(3 ms for 44 tests)
    public int calculateMinimumHP2(int[][] dungeon) {
        int row = dungeon.length;
        if (row == 0) return 0;

        int col = dungeon[0].length;
        int[] minHP = new int[col + 1];
        minHP[col] = 1;
        for (int j = col - 1; j >= 0; j--) {
            minHP[j] = min(dungeon[row - 1][j], minHP[j + 1]);
        }
        minHP[col] = Integer.MAX_VALUE;
        for (int i = row - 2; i >= 0; i--) {
            for (int j = col - 1; j >= 0; j--) {
                minHP[j] = min(dungeon[i][j], Math.min(minHP[j], minHP[j + 1]));
            }
        }
        return minHP[0];
    }

    // 1-D Dynamic Programming
    // beats 62.59%(3 ms for 44 tests)
    public int calculateMinimumHP3(int[][] dungeon) {
        int row = dungeon.length;
        if (row == 0) return 0;

        int col = dungeon[0].length;
        int[] dp = new int[col + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[col] = 1;
        for (int i = row - 1; i >= 0; i--) {
            for (int j = col - 1; j >= 0; j--) {
                int last = (i != row - 1 && j == col - 1) ? Integer.MAX_VALUE : dp[j + 1];
                dp[j] = Math.max(1, Math.min(dp[j], last) - dungeon[i][j]);
            }
        }
        return dp[0];
    }

    // Binary Search
    // http://www.jiuzhang.com/solutions/dungeon-game/
    // beats 3.33%(11 ms)
    public int calculateMinimumHP4(int[][] dungeon) {
        if (dungeon.length == 0) return 0;

        int start = 1, end = Integer.MAX_VALUE - 1;
        while (start < end) {
            int mid = (end - start) / 2 + start;
            if (canSurvive(mid, dungeon)) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return canSurvive(start, dungeon) ? start : end;
    }

    private boolean canSurvive(int health, int[][] dungeon) {
        int row = dungeon.length;
        int col = dungeon[0].length;
        int[][] hp = new int[row][col];
        hp[0][0] = dungeon[0][0] + health;
        if (hp[0][0] <= 0) return false;

        for (int i = 1; i < row; i++) {
            hp[i][0] = hp[i - 1][0] == Integer.MIN_VALUE
                       ? Integer.MIN_VALUE : hp[i - 1][0] + dungeon[i][0];
            if (hp[i][0] <= 0) {
                hp[i][0] = Integer.MIN_VALUE;
            }
        }
        for (int i = 1; i < col; i++) {
            hp[0][i] = hp[0][i - 1] == Integer.MIN_VALUE
                       ? Integer.MIN_VALUE : hp[0][i - 1] + dungeon[0][i];
            if (hp[0][i] <= 0) {
                hp[0][i] = Integer.MIN_VALUE;
            }
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                hp[i][j] = Math.max(hp[i - 1][j], hp[i][j - 1]);
                if (hp[i][j] == Integer.MIN_VALUE) continue;

                hp[i][j] += dungeon[i][j];
                if (hp[i][j] <= 0) {
                    hp[i][j] = Integer.MIN_VALUE;
                }
            }
        }
        return hp[row - 1][col - 1] > 0;
    }

    void test(Function<int[][], Integer> calculate, int expected, int[][] dungeon) {
        assertEquals(expected, (int)calculate.apply(dungeon));
    }

    void test(int expected, int[][] dungeon) {
        DungeonGame d = new DungeonGame();
        test(d::calculateMinimumHP, expected, dungeon);
        test(d::calculateMinimumHP2, expected, dungeon);
        test(d::calculateMinimumHP3, expected, dungeon);
        test(d::calculateMinimumHP4, expected, dungeon);
    }

    @Test
    public void test1() {
        test(7, new int[][] {{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DungeonGame");
    }
}
