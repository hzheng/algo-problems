import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC778: https://leetcode.com/problems/swim-in-rising-water/
//
// On an N x N grid, each square grid[i][j] represents the elevation at that
// point(i,j). Rain starts to fall. At time t, the depth of the water everywhere
// is t. You can swim from a square to another 4-directionally adjacent square
// if and only if the elevation of both squares individually are at most t. You
// can swim infinite distance in zero time. Of course, you must stay within the
// boundaries of the grid during your swim.
// You start at the top left square (0, 0). What is the least time until you can
// reach the bottom right square (N-1, N-1)?
// Note:
// 2 <= N <= 50.
// grid[i][j] is a permutation of [0, ..., N*N - 1].
public class SwimInWater {
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    // Priority Queue
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N * N)
    // beats %(32 ms for 41 tests)
    public int swimInWater(int[][] grid) {
        int n = grid.length;
        boolean[][] visited = new boolean[n][n];
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return grid[a[0]][a[1]] - grid[b[0]][b[1]];
            }
        });
        int res = grid[0][0];
        for (pq.offer(new int[] {0, 0}); !pq.isEmpty(); ) {
            int[] top = pq.poll();
            int x = top[0];
            int y = top[1];
            res = Math.max(res, grid[x][y]);
            if (x == n - 1 && y == n - 1) break;

            visited[x][y] = true;
            for (int[] dir : DIRS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx >= 0 && nx < n && ny >= 0 && ny < n
                    && !visited[nx][ny]) {
                    pq.offer(new int[] {nx, ny});
                }
            }
        }
        return res;
    }

    // Priority Queue
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N * N)
    // beats %(100 ms for 41 tests)
    public int swimInWater2(int[][] grid) {
        int n = grid.length;
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>(
            (a, b) -> grid[a / n][a % n] - grid[b / n][b % n]);
        int res = 0;
        for (pq.offer(0); !pq.isEmpty(); ) {
            int top = pq.poll();
            int x = top / n;
            int y = top % n;
            res = Math.max(res, grid[x][y]);
            if (x == n - 1 && y == n - 1) break;

            for (int[] dir : DIRS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                int t = nx * n + ny;
                if (nx >= 0 && nx < n && ny >= 0 && ny < n && visited.add(t)) {
                    pq.offer(t);
                }
            }
        }
        return res;
    }

    // Binary Search + DFS + Stack
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N * N)
    // beats %(51 ms for 41 tests)
    public int swimInWater3(int[][] grid) {
        int low = grid[0][0];
        int n = grid.length;
        for (int high = n * n; low < high; ) {
            int mid = (low + high) >>> 1;
            if (!canSwim(mid, grid)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private boolean canSwim(int T, int[][] grid) {
        int n = grid.length;
        Set<Integer> visited = new HashSet<>();
        visited.add(0);
        Stack<Integer> stack = new Stack<>();
        for (stack.push(0); !stack.empty(); ) {
            int top = stack.pop();
            int x = top / n;
            int y = top % n;
            if (x == n - 1 && y == n - 1) return true;

            for (int[] dir : DIRS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                int t = nx * n + ny;
                if (nx >= 0 && nx < n && ny >= 0 && ny < n
                    && grid[nx][ny] <= T && visited.add(t)) {
                    stack.push(t);
                }
            }
        }
        return false;
    }

    // DFS + Recursion
    // time complexity: O(N ^ 4), space complexity: O(N * N)
    // beats %(120 ms for 41 tests)
    public int swimInWater4(int[][] grid) {
        int n = grid.length;
        int[][] max = new int[n][n];
        for (int[] line : max) {
            Arrays.fill(line, Integer.MAX_VALUE);
        }
        dfs(grid, max, 0, 0, grid[0][0]);
        return max[n - 1][n - 1];
    }

    private void dfs(int[][] grid, int[][] max, int x, int y, int cur) {
        int n = grid.length;
        if (x < 0 || x >= n || y < 0 || y >= n) return;

        int nMax = Math.max(cur, grid[x][y]);
        if (nMax < max[x][y]) {
            max[x][y] = nMax;
            for (int[] dir : DIRS) {
                dfs(grid, max, x + dir[0], y + dir[1], max[x][y]);
            }
        }
    }

    // Union Find with Rank
    // time complexity: O(N ^ 4), space complexity: O(N * N)
    // beats %(106 ms for 41 tests)
    public int swimInWater5(int[][] grid) {
        int n = grid.length;
        int[] id = new int[n * n];
        int[] size = new int[n * n];
        for (int i = n * n - 1; i >= 0; i--) {
            id[i] = i;
            size[i] = 1;
        }
        for (int limit = 0;; limit++) {
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    if (grid[x][y] != limit) continue;

                    for (int[] dir : DIRS) {
                        int nx = x + dir[0];
                        int ny = y + dir[1];
                        if (nx >= 0 && nx < n && ny >= 0 && ny < n
                            && grid[nx][ny] <= limit) {
                            union(id, size, x * n + y, nx * n + ny);
                        }
                    }
                }
            }
            if (root(id, 0) == root(id, n * n - 1)) return limit;
        }
    }

    private int root(int id[], int i) {
        int q = i;
        for (; id[q] != q; q = id[q] = id[id[q]]) {}
        return q;
    }

    private void union(int id[], int[] size, int x, int y) {
        int setX = root(id, x);
        int setY = root(id, y);
        if (setX == setY) return;

        if (size[x] < size[y]) {
           id[setX] = setY;
           size[setY] += size[setX];
        } else {
           id[setY] = setX;
           size[setX] += size[setY];
        }
    }

    // Union Find with Rank
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N * N)
    // beats %(13 ms for 41 tests)
    public int swimInWater6(int[][] grid) {
        int n = grid.length;
        int[] id = new int[n * n];
        int[] size = new int[n * n];
        for (int i = n * n - 1; i >= 0; i--) {
            id[i] = i;
            size[i] = 1;
        }
        int[][] map = new int[n * n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[grid[i][j]] = new int[] {i, j};
            }
        }
        for (int limit = 0;; limit++) {
            int x = map[limit][0];
            int y = map[limit][1];
            for (int[] dir : DIRS) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx >= 0 && nx < n && ny >= 0 && ny < n
                    && grid[nx][ny] <= limit) {
                    union(id, size, x * n + y, nx * n + ny);
                }
            }
            if (root(id, 0) == root(id, n * n - 1)) return limit;
        }
    }

    // Blood Fill
    // time complexity: O(N ^ 2), space complexity: O(N * N)
    // beats %(11 ms for 41 tests)
    public int swimInWater7(int[][] grid) {
        int n = grid.length;
        int[][] map = new int[n * n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                map[grid[i][j]] = new int[] {i, j};
            }
        }
        boolean[] reach = new boolean[map.length];
        reach[grid[0][0]] = true;
        for (int h = 0; h < map.length; h++) {
            if (reach[h] && fill(grid, map, reach, h, h)) return h;
        }
        return 0;
    }

    private boolean fill(int[][] grid, int[][] map, boolean[] reach,
                         int val, int limit) {
        int n = grid.length;
        int x = map[val][0];
        int y = map[val][1];
        if (x == n - 1 && y == n - 1) return true;

        for (int[] dir : DIRS) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (nx < 0 || ny < 0 || nx >= n || ny >= n || reach[grid[nx][ny]]) continue;

            reach[grid[nx][ny]] = true;
            if (grid[nx][ny] <= limit
                && fill(grid, map, reach, grid[nx][ny], limit)) return true;
        }
        return false;
    }

    void test(int[][] grid, int expected) {
        assertEquals(expected, swimInWater(grid));
        assertEquals(expected, swimInWater2(grid));
        assertEquals(expected, swimInWater3(grid));
        assertEquals(expected, swimInWater4(grid));
        assertEquals(expected, swimInWater5(grid));
        assertEquals(expected, swimInWater6(grid));
        assertEquals(expected, swimInWater7(grid));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 2}, {1, 3}}, 3);
        test(new int[][] {{0, 1, 2, 3, 4}, {24, 23, 22, 21, 5},
                          {12, 13, 14, 15, 16}, {11, 17, 18, 19, 20},
                          {10, 9, 8, 7, 6}}, 16);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
