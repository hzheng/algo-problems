import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC317: https://leetcode.com/problems/shortest-distance-from-all-buildings
//
// You want to build a house on an empty land which reaches all buildings in the
// shortest amount of distance. You can only move up, down, left and right. You
// are given a 2D grid of values 0, 1 or 2, where:
// Each 0 marks an empty land which you can pass by freely.
// Each 1 marks a building which you cannot pass through.
// Each 2 marks an obstacle which you cannot pass through.
// Note:
// There will be at least one building. If it is not possible to build such house
// according to the above rules, return -1.
public class ShortestDistance {
    // BFS + Queue + Hash Table
    // time complexity: O(M * N + B * E)
    // beats 18.91%(116 ms for 72 tests)
    public int shortestDistance(int[][] grid) {
        int m = grid.length;
        if (m == 0) return -1;

        int n = grid[0].length;
        Map<Integer, int[]> distances = new HashMap<>();
        int buildings = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    bfs(grid, m, n, i, j, distances);
                    buildings++;
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int[] distance : distances.values()) {
            if (distance[1] == buildings) {
                min = Math.min(min, distance[0]);
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    private static final int[][] MOVES = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    private void bfs(int[][] grid, int m, int n, int row, int col, Map<Integer, int[]> distances) {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        queue.offer(new int[] {row, col});
        visited[row][col] = true;
        for (int dist = 1; !queue.isEmpty(); dist++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] point = queue.poll();
                for (int[] move : MOVES) {
                    int x = point[0] + move[0];
                    int y = point[1] + move[1];
                    if (x >= 0 && x < m && y >= 0 && y < n
                        && !visited[x][y] && grid[x][y] == 0) {
                        queue.offer(new int[] {x, y});
                        visited[x][y] = true;
                        int key = n * x + y;
                        int[] val = distances.get(key);
                        if (val == null) {
                            distances.put(key, val = new int[2]);
                        }
                        val[0] += dist;
                        val[1]++;
                    }
                }
            }
        }
    }

    // BFS + Queue + Hash Table(Array)
    // time complexity: O(M * N + B * E)
    // beats 64.42%(55 ms for 72 tests)
    public int shortestDistance_2(int[][] grid) {
        int m = grid.length;
        if (m == 0) return -1;

        int n = grid[0].length;
        int[][][] distances = new int[m][n][2];
        int buildings = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    bfs(grid, m, n, i, j, distances);
                    buildings++;
                }
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0 && distances[i][j][1] == buildings) {
                    min = Math.min(min, distances[i][j][0]);
                }
            }
        }
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    private void bfs(int[][] grid, int m, int n, int row, int col, int[][][] distances) {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        queue.offer(new int[] {row, col});
        visited[row][col] = true;
        for (int dist = 1; !queue.isEmpty(); dist++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] point = queue.poll();
                for (int[] move : MOVES) {
                    int x = point[0] + move[0];
                    int y = point[1] + move[1];
                    if (x >= 0 && x < m && y >= 0 && y < n
                        && !visited[x][y] && grid[x][y] == 0) {
                        queue.offer(new int[] {x, y});
                        visited[x][y] = true;
                        distances[x][y][0] += dist;
                        distances[x][y][1]++;
                    }
                }
            }
        }
    }

    // TODO: more efficient algorithm

    void test(int[][] grid, int expected) {
        assertEquals(expected, shortestDistance(grid));
        assertEquals(expected, shortestDistance_2(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{1}, {0}}, 1);
        test(new int[][] {{1, 1}, {0, 1}}, -1);
        test(new int[][] {{0, 2, 1}, {1, 0, 2}, {0, 1, 0}}, -1);
        test(new int[][] {{1, 0, 2, 0, 1}, {0, 0, 0, 0, 0}, {0, 0, 1, 0, 0}}, 7);
        test(new int[][] {{0, 2, 0, 2, 0, 0, 1, 2, 0, 2, 0, 0, 1, 0, 2, 0, 0, 2},
                          {0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 0, 2, 0, 2, 0, 2, 0}}, -1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ShortestDistance");
    }
}
