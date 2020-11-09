import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC749: https://leetcode.com/problems/contain-virus/
//
// A virus is spreading, and your task is to quarantine the infected area by installing walls. The
// world is modeled as a 2-D array of cells, where 0 represents uninfected cells, and 1 represents
// cells contaminated with the virus. A wall (and only one wall) can be installed between any two
// 4-directionally adjacent cells, on the shared boundary. Every night, the virus spreads to all
// neighboring cells in all four directions unless blocked by a wall. Resources are limited.
// Each day, you can install walls around only one region -- the affected area (continuous block of
// infected cells) that threatens the most uninfected cells the following night. Can you save the
// day? If so, what is the number of walls required? If not, and the world becomes fully infected,
// return the number of walls used.
// Note:
// The number of rows and columns of grid will each be in the range [1, 50].
// Each grid[i][j] will be either 0 or 1.
// Throughout the described process, there is always a contiguous viral region that will infect
// strictly more uncontaminated squares in the next round.
public class ContainVirus {
    // DFS + Recursion + Heap
    // time complexity: O(M*N*log(M*N)), space complexity: O(M*N)
    // 7 ms(94.12%), 39.1 MB(10.46%) for 32 tests
    public int containVirus(int[][] grid) {
        for (int m = grid.length, n = grid[0].length, res = 0; ; ) {
            PriorityQueue<Region> regions = new PriorityQueue<>();
            boolean[][] visited = new boolean[m][n];
            for (int x = 0; x < m; x++) {
                for (int y = 0; y < n; y++) {
                    if (grid[x][y] == 1 && !visited[x][y]) {
                        Region region = new Region();
                        dfs(grid, x, y, visited, region);
                        if (!region.frontier.isEmpty()) {
                            regions.offer(region);
                        }
                    }
                }
            }
            Region quarantinedRegion = regions.poll();
            if (quarantinedRegion == null) { return res; }

            res += quarantinedRegion.neededWalls;
            for (int nei : quarantinedRegion.infected) {
                grid[nei / n][nei % n] = -1;
            }
            for (Region region : regions) {
                for (int nei : region.frontier) {
                    grid[nei / n][nei % n] = 1;
                }
            }
        }
    }

    private void dfs(int[][] grid, int x, int y, boolean[][] visited, Region region) {
        int m = grid.length;
        int n = grid[0].length;
        if (x < 0 || x >= m || y < 0 || y >= n || grid[x][y] < 0) { return; }

        if (grid[x][y] == 0) {
            region.neededWalls++;
            region.frontier.add(x * n + y);
        } else if (!visited[x][y]) { // grid[x][y] == 1
            visited[x][y] = true;
            region.infected.add(x * n + y);
            dfs(grid, x + 1, y, visited, region);
            dfs(grid, x - 1, y, visited, region);
            dfs(grid, x, y + 1, visited, region);
            dfs(grid, x, y - 1, visited, region);
        }
    }

    private static class Region implements Comparable<Region> {
        Set<Integer> infected = new HashSet<>();
        Set<Integer> frontier = new HashSet<>();
        int neededWalls;

        @Override public int compareTo(Region other) {
            return other.frontier.size() - frontier.size();
        }
    }

    private void test(int[][] grid, int expected) {
        assertEquals(expected, containVirus(grid));
    }

    @Test public void test() {
        test(new int[][] {{0, 1, 0, 0, 0, 0, 0, 1}, {0, 1, 0, 0, 0, 0, 0, 1},
                          {0, 0, 0, 0, 0, 0, 0, 1}, {0, 0, 0, 0, 0, 0, 0, 0}}, 10);
        test(new int[][] {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}}, 4);
        test(new int[][] {{1, 1, 1, 0, 0, 0, 0, 0, 0}, {1, 0, 1, 0, 1, 1, 1, 1, 1},
                          {1, 1, 1, 0, 0, 0, 0, 0, 0}}, 13);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
