import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC827: https://leetcode.com/problems/making-a-large-island/
//
// In a 2D grid of 0s and 1s, we change at most one 0 to a 1.
// After, what is the size of the largest island? (An island is a
// 4-directionally connected group of 1s).
public class MakeLargeIsland {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // BFS + Queue + Set
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats %(24 ms for 63 tests)
    public int largestIsland(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] id = new int[m * n];
        int[] islands = new int[m * n]; // island counts
        Arrays.fill(id, -1);
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                int index = x * n + y;
                if (grid[x][y] == 0 || id[index] >= 0) continue;

                id[index] = index;
                Queue<int[]> queue = new LinkedList<>();
                int count = 0;
                for (queue.offer(new int[] {x, y}); !queue.isEmpty(); count++) {
                    int[] cur = queue.poll();
                    for (int[] move : MOVES) {
                        int nx = cur[0] + move[0];
                        int ny = cur[1] + move[1];
                        int ni = nx * n + ny;
                        if (nx >= 0 && nx < m && ny >= 0 && ny < n
                            && id[ni] < 0 && grid[nx][ny] == 1) {
                            id[ni] = index;
                            queue.offer(new int[] {nx, ny});
                        }
                    }
                }
                islands[index] = count;
            }
        }
        int res = 0;
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (grid[x][y] == 1) continue;

                int count = 1;
                Set<Integer> neighborIslands = new HashSet<>();
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n
                        && grid[nx][ny] == 1) {
                        int index = id[nx * n + ny];
                        if (neighborIslands.add(index)) {
                            count += islands[index];
                        }
                    }
                }
                res = Math.max(res, count);
            }
        }
        return (res == 0) ? m * n : res;
    }

    // Union Find + Set
    // time complexity: O(M * N), space complexity: O(M * N)
    // beats %(21 ms for 63 tests)
    public int largestIsland2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] id = new int[m * n];
        int[] islands = new int[m * n]; // island counts
        Arrays.fill(id, -1);
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                int index = x * n + y;
                if (grid[x][y] == 0 || id[index] >= 0) continue;

                id[index] = index;
                islands[index] = 1;
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    int ni = nx * n + ny;
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n
                        && grid[nx][ny] == 1 && id[ni] >= 0) {
                        for (; ni != id[ni]; ni = id[ni] = id[id[ni]]) {}
                        if (index != ni) {
                            islands[ni] += islands[index];
                            id[index] = ni;
                            index = ni;
                        }
                    }
                }
            }
        }
        int res = 0;
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (grid[x][y] == 1) continue;

                int count = 1;
                Set<Integer> neighborIslands = new HashSet<>();
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n
                        && grid[nx][ny] == 1) {
                        int ni = nx * n + ny;
                        for (; ni != id[ni]; ni = id[ni] = id[id[ni]]) {}
                        if (neighborIslands.add(ni)) {
                            count += islands[ni];
                        }
                    }
                }
                res = Math.max(res, count);
            }
        }
        return (res == 0) ? m * n : res;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, largestIsland(grid));
        assertEquals(expected, largestIsland2(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 0}, {0, 0}}, 1);
        test(new int[][] {{0, 0}, {0, 1}}, 2);
        test(new int[][] {{1, 0}, {0, 1}}, 3);
        test(new int[][] {{1, 1}, {1, 0}}, 4);
        test(new int[][] {{1, 1}, {1, 1}}, 4);
        test(new int[][] {{0, 0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 1, 0, 0},
                          {0, 1, 0, 0, 1, 0, 0}, {1, 0, 1, 0, 1, 0, 0},
                          {0, 1, 0, 0, 1, 0, 0}, {0, 1, 0, 0, 1, 0, 0},
                          {0, 1, 1, 1, 1, 0, 0}}, 18);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
