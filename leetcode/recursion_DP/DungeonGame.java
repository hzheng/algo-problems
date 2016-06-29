import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/dungeon-game/
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

    void test(Function<int[][], Integer> calculate, int expected, int[][] dungeon) {
        assertEquals(expected, (int)calculate.apply(dungeon));
    }

    void test(int expected, int[][] dungeon) {
        DungeonGame d = new DungeonGame();
        test(d::calculateMinimumHP, expected, dungeon);
    }

    @Test
    public void test1() {
        test(7, new int[][] {{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DungeonGame");
    }
}
