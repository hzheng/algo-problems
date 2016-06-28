import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// https://leetcode.com/problems/number-of-islands/
//
// Given a 2d grid map of '1's (land) and '0's (water), count the number of
// islands. An island is surrounded by water and is formed by connecting
// adjacent lands horizontally or vertically. You may assume all four edges of
// the grid are all surrounded by water.
public class IslandNumber {
    private static final char LAND = '1';
    private static final char WATER = '0';
    private static final char MARK = ' ';

    // BFS
    // beats 21.91%(7 ms)
    // time complexity: O(N ^ 2), space complexity: O(1)
    public int numIslands(char[][] grid) {
        int nRow = grid.length;
        if (nRow == 0) return 0;

        int nCol = grid[0].length;
        int islands = 0;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] == LAND) {
                    markIsland(grid, nRow, nCol, i, j);
                    islands++;
                }
            }
        }

        // restore grid(if omitted, beat rate will rise to 25.82%(6 ms))
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] == MARK) {
                    grid[i][j] = LAND;
                }
            }
        }
        return islands;
    }

    private void markIsland(char[][] grid, int nRow, int nCol,
                            int startX, int startY) {
        Queue<int[]> island = new LinkedList<>();
        island.offer(new int[] {startX, startY});
        grid[startX][startY] = MARK;
        while (!island.isEmpty()) {
            int[] pos = island.poll();
            int x = pos[0];
            int y = pos[1];
            if (x > 0 && grid[x - 1][y] == LAND) {
                island.offer(new int[] {x - 1, y});
                grid[x - 1][y] = MARK;
            }
            if (x + 1 < nRow && grid[x + 1][y] == LAND) {
                island.offer(new int[] {x + 1, y});
                grid[x + 1][y] = MARK;
            }
            if (y > 0 && grid[x][y - 1] == LAND) {
                island.offer(new int[] {x, y - 1});
                grid[x][y - 1] = MARK;
            }
            if (y + 1 < nCol && grid[x][y + 1] == LAND) {
                island.offer(new int[] {x, y + 1});
                grid[x][y + 1] = MARK;
            }
        }
    }

    private char[][] convert(String[] s) {
        char[][] board = new char[s.length][];
        for (int i = 0; i < s.length; i++) {
            board[i] = s[i].toCharArray();
        }
        return board;
    }

    void test(Function<char[][], Integer> count, int expected, String ... s) {
        assertEquals(expected, (int)count.apply(convert(s)));
    }

    void test(int expected, String ... s) {
        IslandNumber i = new IslandNumber();
        test(i::numIslands, expected, s);
    }

    @Test
    public void test1() {
        test(1, "111", "010", "111");
        test(1, "11110", "11010", "11000", "00000");
        test(3, "11000", "11000", "00100", "00011");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IslandNumber");
    }
}
