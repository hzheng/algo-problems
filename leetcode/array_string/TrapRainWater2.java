import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC407: https://leetcode.com/problems/trapping-rain-water-ii/
//
// Given an m x n matrix of positive integers representing the height of each
// unit cell in a 2D elevation map, compute the volume of water it is able to
// trap after raining.
// Note:
// Both m and n are less than 110. The height of each unit cell is greater than
// 0 and is less than 20,000.
public class TrapRainWater2 {
    // BFS + Heap
    // beats 93.93%(28 ms for 38 tests)
    public int trapRainWater(int[][] heightMap) {
        int m = heightMap.length;
        if (m < 3) return 0;

        int n = heightMap[0].length;
        if (n < 3) return 0;

        boolean[][] visited = new boolean[m][n];
        PriorityQueue<Cell> pq = new PriorityQueue<>();
        for (int i = 0; i < m; i++) {
            pq.offer(new Cell(i, 0, heightMap[i][0]));
            visited[i][0] = true;
            pq.offer(new Cell(i, n - 1, heightMap[i][n - 1]));
            visited[i][n - 1] = true;
        }
        for (int j = 1; j < n - 1; j++) {
            pq.offer(new Cell(0, j, heightMap[0][j]));
            visited[0][j] = true;
            pq.offer(new Cell(m - 1, j, heightMap[m - 1][j]));
            visited[m - 1][j] = true;
        }
        final int[][] dirs = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int water = 0;
        while (!pq.isEmpty()) {
            Cell cell = pq.poll();
            for (int[] dir : dirs) {
                int x = cell.x + dir[0];
                int y = cell.y + dir[1];
                if (x < 0 || x >= m || y < 0 || y >= n || visited[x][y]) continue;

                visited[x][y] = true;
                water += Math.max(0, cell.h - heightMap[x][y]);
                pq.offer(new Cell(x, y, Math.max(heightMap[x][y], cell.h)));
            }
        }
        return water;
    }

    private static class Cell implements Comparable<Cell> {
        int x;
        int y;
        int h;
        Cell(int x, int y, int h) {
            this.x = x;
            this.y = y;
            this.h = h;
        }

        public int compareTo(Cell other) {
            return h - other.h;
        }
    }

    // https://discuss.leetcode.com/topic/60693/why-reinvent-the-wheel-an-easy-understood-commented-solution-based-on-trapping-rain-1
    // beats 25.94%(112 ms for 38 tests)
    public int trapRainWater2(int[][] heightMap) {
        int m = heightMap.length;
        if (m < 3) return 0;

        int n = heightMap[0].length;
        final int maxH = 20000;
        int water = 0;
        int[][] rainMap = new int[m][n];
        for (int i = 1; i < m - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                water += (rainMap[i][j] = maxH);
            }
        }
        final int[][] dirs = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (boolean spillWater = true; spillWater; ) {
            spillWater = false;
            for (int i = 1; i < m - 1; i++) {
                for (int j = 1; j < n - 1; j++) {
                    if (rainMap[i][j] == 0) continue;

                    for (int[] dir : dirs) {
                        int x = i + dir[0];
                        int y = j + dir[1];
                        int h = rainMap[x][y] + heightMap[x][y];
                        int curH = rainMap[i][j] + heightMap[i][j];
                        if (curH <= h) continue;

                        int spilledWater = curH - Math.max(h, heightMap[i][j]);
                        rainMap[i][j] -= spilledWater;
                        water -= spilledWater;
                        spillWater = true;
                    }
                }
            }
        }
        return water;
    }

    void test(int[][] heightMap, int expected) {
        assertEquals(expected, trapRainWater(heightMap));
        assertEquals(expected, trapRainWater2(heightMap));
    }

    @Test
    public void test1() {
        test(new int[][] {{13, 4, 13, 12}, {13, 8, 10, 12}, {12, 13, 12, 12}}, 0);
        test(new int[][] {{1, 4, 3, 1, 3, 2}, {3, 2, 1, 3, 2, 4}, {2, 3, 3, 2, 3, 1}}, 4);
        test(new int[][] {{12, 13, 1, 12}, {13, 4, 13, 12}, {13, 8, 10, 12},
                          {12, 13, 12, 12}, {13, 13, 13, 13}}, 14);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TrapRainWater2");
    }
}
