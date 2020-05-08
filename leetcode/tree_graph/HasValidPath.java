import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1391: https://leetcode.com/problems/check-if-there-is-a-valid-path-in-a-grid/
//
// Given a m x n grid. Each cell of the grid represents a street. The street of grid[i][j] can be:
// 1 which means a street connecting the left cell and the right cell.
// 2 which means a street connecting the upper cell and the lower cell.
// 3 which means a street connecting the left cell and the lower cell.
// 4 which means a street connecting the right cell and the lower cell.
// 5 which means a street connecting the left cell and the upper cell.
// 6 which means a street connecting the right cell and the upper cell.
// You will initially start at the street of the upper-left cell (0,0). A valid path in the grid is
// a path which starts from the upper left cell (0,0) and ends at the bottom-right cell
// (m - 1, n - 1). The path should only follow the streets.
// Notice that you are not allowed to change any street.
// Return true if there is a valid path in the grid or false otherwise.
//
// Constraints:
// m == grid.length
// n == grid[i].length
// 1 <= m, n <= 300
// 1 <= grid[i][j] <= 6
public class HasValidPath {
    private final static int[][] MOVES = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // Queue + BFS + Set
    // time complexity: O(M*N), space complexity: O(M*N)
    // 36 ms(32.73%), 48.2 MB(100%) for 77 tests
    public boolean hasValidPath(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        queue.offer(new int[3]);
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0];
            int y = cur[1];
            int prevDir = cur[2];
            if (visited[x][y]) { continue; }
            visited[x][y] = true;

            int curDir = grid[x][y];
            List<int[]> directions = next(prevDir, curDir);
            if (directions.isEmpty()) { continue; }
            if (x == m - 1 && y == n - 1) { return true; }

            for (int[] c : directions) {
                int[] next = new int[] {x + c[0], y + c[1], curDir};
                if (next[0] >= 0 && next[0] < m && next[1] >= 0 && next[1] < n) {
                    queue.offer(next);
                }
            }
        }
        return false;
    }

    private final static int[][][] STREETS =
            {{}, {{2, 2}, {3, 3}}, {{0, 0}, {1, 1}}, {{2, 0}, {1, 3}}, {{3, 0}, {1, 2}},
             {{2, 1}, {0, 3}}, {{3, 1}, {0, 2}}};

    private List<int[]> next(int prev, int cur) {
        List<int[]> directions = new ArrayList<>();
        for (int[] curStreet : STREETS[cur]) {
            if (prev == 0) {
                directions.add(MOVES[curStreet[1]]);
            } else {
                for (int[] prevStreet : STREETS[prev]) {
                    if (curStreet[0] == prevStreet[1]) {
                        directions.add(MOVES[curStreet[1]]);
                    }
                }
            }
        }
        return directions;
    }

    private static final int[][][] DIRS =
            {{}, {{0, -1}, {0, 1}}, {{-1, 0}, {1, 0}}, {{0, -1}, {1, 0}}, {{0, 1}, {1, 0}},
             {{0, -1}, {-1, 0}}, {{0, 1}, {-1, 0}}};

    // Queue + BFS + Set
    // time complexity: O(M*N), space complexity: O(M*N)
    // 14 ms(76.25%), 48.2 MB(100%) for 77 tests
    public boolean hasValidPath2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> queue = new LinkedList<>();
        visited[0][0] = true;
        for (queue.offer(new int[2]); !queue.isEmpty(); ) {
            int[] cur = queue.poll();
            int x = cur[0];
            int y = cur[1];
            if (x == m - 1 && y == n - 1) { return true; }

            for (int[] dir : DIRS[grid[x][y]]) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || visited[nx][ny]) { continue; }

                for (int[] revDir : DIRS[grid[nx][ny]]) {
                    if ((nx + revDir[0] == x) && (ny + revDir[1] == y)) { // compatible
                        visited[nx][ny] = true;
                        queue.offer(new int[] {nx, ny});
                    }
                }
            }
        }
        return false;
    }

    // DFS + Recursion + Set
    // time complexity: O(M*N), space complexity: O(M*N)
    // 15 ms(73.26%), 65.6 MB(100%) for 77 tests
    public boolean hasValidPath3(int[][] grid) {
        return dfs3(grid, 0, 0, 0, new boolean[grid.length][grid[0].length]);
    }

    private boolean dfs3(int[][] grid, int x, int y, int prev, boolean[][] visited) {
        int m = grid.length;
        int n = grid[0].length;
        if (x < 0 || x >= m || y < 0 || y >= n || visited[x][y]) { return false; }

        int cur = grid[x][y];
        boolean hasNext = (prev == 0);
        if (prev != 0) {
            for (int[] curStreet : STREETS[cur]) {
                for (int[] prevStreet : STREETS[prev]) {
                    if (curStreet[0] == prevStreet[1]) {
                        hasNext = true;
                        break;
                    }
                }
            }
        }
        if (!hasNext) { return false; }
        if (x == m - 1 && y == n - 1) { return true; }

        visited[x][y] = true;
        for (int[] curStreet : STREETS[cur]) {
            int[] move = MOVES[curStreet[1]];
            if (prev == 0) {
                if (dfs3(grid, x + move[0], y + move[1], cur, visited)) {return true;}
            } else {
                for (int[] prevStreet : STREETS[prev]) {
                    if (curStreet[0] == prevStreet[1]) {
                        if (dfs3(grid, x + move[0], y + move[1], cur, visited)) {return true;}
                    }
                }
            }
        }
        visited[x][y] = false;
        return false;
    }

    // DFS + Recursion + Set
    // time complexity: O(M*N), space complexity: O(M*N)
    // 14 ms(76.25%), 62 MB(100%) for 77 tests
    public boolean hasValidPath4(int[][] grid) {
        return dfs4(grid, 0, 0, grid[0][0], new boolean[grid.length][grid[0].length]);
    }

    private boolean dfs4(int[][] grid, int x, int y, int cur, boolean[][] visited) {
        int m = grid.length;
        int n = grid[0].length;
        if (x < 0 || x >= m || y < 0 || y >= n || cur != grid[x][y] || visited[x][y]) {
            return false;
        }
        if (x == m - 1 && y == n - 1) {
            return true;
        }

        visited[x][y] = true;
        int i = 0;
        for (int[] next : NEXT[cur - 1]) {
            if (next != null) {
                for (int street : next) {
                    int[] move = MOVES[i];
                    if (dfs4(grid, x + move[0], y + move[1], street, visited)) {return true;}
                }
            }
            i++;
        }
        visited[x][y] = false;
        return false;
    }

    final static int[][][] NEXT = new int[][][] {{null, null, {1, 3, 5}, {1, 4, 6}}, // street 1
                                                 {{2, 5, 6}, {2, 3, 4}, null, null}, // street 2
                                                 {{2, 5, 6}, null, null, {1, 4, 6}}, // street 3
                                                 {{2, 5, 6}, null, {1, 3, 5}, null}, // street 4
                                                 {null, {2, 3, 4}, null, {1, 4, 6}}, // street 5
                                                 {null, {2, 3, 4}, {1, 3, 5}, null}, // street 6
    };

    // DFS + Recursion + Set
    // time complexity: O(M*N), space complexity: O(M*N)
    // 13 ms(78.63%), 62 MB(100%) for 77 tests
    public boolean hasValidPath5(int[][] grid) {
        return dfs5(grid, 0, 0, new boolean[grid.length][grid[0].length]);
    }

    private boolean dfs5(int[][] grid, int x, int y, boolean[][] visited) {
        int m = grid.length;
        int n = grid[0].length;
        if (x == m - 1 && y == n - 1) { return true; }

        visited[x][y] = true;
        for (int[] nextDir : DIRS[grid[x][y]]) {
            int nx = x + nextDir[0];
            int ny = y + nextDir[1];
            if (nx < 0 || nx >= m || ny < 0 || ny >= n || visited[nx][ny]) { continue; }

            for (int[] revDir : DIRS[grid[nx][ny]]) {
                if ((nx + revDir[0] == x) && (ny + revDir[1] == y)) { // compatible
                    if (dfs5(grid, nx, ny, visited)) { return true; }
                }
            }
        }
        return false;
    }

    // TODO: Union Find

    @Test public void test() {
        test(new int[][] {{1, 2}}, false);
        test(new int[][] {{1, 1, 2}}, false);
        test(new int[][] {{1, 2, 1}, {1, 2, 1}}, false);
        test(new int[][] {{2, 4, 3}, {6, 5, 2}}, true);
        test(new int[][] {{1, 1, 1, 1, 1, 1, 3}}, true);
        test(new int[][] {{2}, {2}, {2}, {2}, {2}, {2}, {6}}, true);
        test(new int[][] {{3, 4, 3, 4}, {2, 2, 2, 2}, {6, 5, 6, 5}}, true);
        test(new int[][] {{3, 4, 3, 4, 3, 4, 3}, {2, 2, 2, 2, 2, 2, 2}, {2, 2, 2, 2, 2, 2, 2},
                          {2, 2, 2, 2, 2, 2, 2}, {2, 2, 2, 2, 2, 2, 2}, {6, 5, 6, 5, 6, 5, 6}},
             true);
    }

    private void test(int[][] grid, boolean expected) {
        assertEquals(expected, hasValidPath(grid));
        assertEquals(expected, hasValidPath2(grid));
        assertEquals(expected, hasValidPath3(grid));
        assertEquals(expected, hasValidPath4(grid));
        assertEquals(expected, hasValidPath5(grid));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
