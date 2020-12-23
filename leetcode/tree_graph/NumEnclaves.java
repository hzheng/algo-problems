import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1020: https://leetcode.com/problems/number-of-enclaves/
//
// Given a 2D array A, each cell is 0 (representing sea) or 1 (representing land). A move consists
// of walking from one land square 4-directionally to another land square, or off the boundary of
// the grid. Return the number of land squares in the grid for which we cannot walk off the boundary
// of the grid in any number of moves.
//
// Note:
// 1 <= A.length <= 500
// 1 <= A[i].length <= 500
// 0 <= A[i][j] <= 1
// All rows have the same size.
public class NumEnclaves {
    // DFS + Recursion
    // time complexity: O(N*M), space complexity: O(N*M)
    // 2 ms(99.10%), 47.5 MB(29.80%) for 56 tests
    public int numEnclaves(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        for (int i = 0; i < m; i++) {
            if (A[i][0] == 1) {
                dfs(A, i, 0);
            }
            if (A[i][n - 1] == 1) {
                dfs(A, i, n - 1);
            }
        }
        for (int i = 0; i < n; i++) {
            if (A[0][i] == 1) {
                dfs(A, 0, i);
            }
            if (A[m - 1][i] == 1) {
                dfs(A, m - 1, i);
            }
        }
        int res = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (A[i][j] < 0) {
                    A[i][j] = 1;
                } else {
                    res += A[i][j];
                }
            }
        }
        return res;
    }

    private void dfs(int[][] A, int x, int y) {
        int m = A.length;
        int n = A[0].length;
        if (x < 0 || x >= m || y < 0 || y >= n || A[x][y] <= 0) { return; }

        A[x][y] = -1;
        dfs(A, x + 1, y);
        dfs(A, x - 1, y);
        dfs(A, x, y + 1);
        dfs(A, x, y - 1);
    }

    // Union Find + DFS + Recursion
    // time complexity: O(N*M), space complexity: O(1)
    // 33 ms(5.42%), 47.4 MB(36.79%) for 56 tests
    public int numEnclaves2(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        int[] id = new int[m * n + 1];
        int exterior = m * n; // connect to all edge ones
        Arrays.fill(id, -1);
        int totalOnes = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                totalOnes += A[i][j];
            }
            if (A[i][0] == 1) {
                explore(A, i, 0, id, new boolean[m][n]);
                union(id, exterior, i * n);
            }
            if (A[i][n - 1] == 1) {
                explore(A, i, n - 1, id, new boolean[m][n]);
                union(id, exterior, i * n + n - 1);
            }
        }
        for (int i = 0; i < n; i++) {
            if (A[0][i] == 1) {
                explore(A, 0, i, id, new boolean[m][n]);
                union(id, exterior, i);
            }
            if (A[m - 1][i] == 1) {
                explore(A, m - 1, i, id, new boolean[m][n]);
                union(id, exterior, (m - 1) * n + i);
            }
        }
        return totalOnes + id[root(id, exterior)] + 1;
    }

    private void explore(int[][] A, int x, int y, int[] id, boolean[][] visited) {
        int m = A.length;
        int n = A[0].length;
        visited[x][y] = true;
        int cur = x * n + y;
        for (int[] move : MOVES) {
            int dx = move[0];
            int dy = move[1];
            int nx = x + dx;
            int ny = y + dy;
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && !visited[nx][ny] && A[nx][ny] == 1) {
                union(id, cur, nx * n + ny);
                explore(A, nx, ny, id, visited);
            }
        }
    }

    // Union Find
    // time complexity: O(N*M), space complexity: O(1)
    // 13 ms(8.80%), 46.4 MB(98.65%) for 56 tests
    public int numEnclaves3(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        int[] id = new int[m * n + 1];
        int exterior = m * n; // connect to all edge ones
        Arrays.fill(id, -1);
        int totalOnes = 0;
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (A[x][y] == 0) { continue; }

                totalOnes++;
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && A[nx][ny] == 1) {
                        union(id, x * n + y, nx * n + ny);
                    }
                }
                if (x == 0 || y == 0 || x == m - 1 || y == n - 1) {
                    union(id, exterior, x * n + y);
                }
            }
        }
        return totalOnes + id[root(id, exterior)] + 1;
    }

    private static final int[][] MOVES = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    private boolean union(int[] id, int x, int y) {
        int px = root(id, x);
        int py = root(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    private int root(int[] id, int x) {
        int parent = x;
        for (; id[parent] >= 0; parent = id[parent]) {}
        return parent;
    }

    // BFS + Queue
    // time complexity: O(N*M), space complexity: O(N*M)
    // 7 ms(30.47%), 47.4 MB(36.79%) for 56 tests
    public int numEnclaves4(int[][] A) {
        int m = A.length;
        int n = A[0].length;
        boolean[][] visited = new boolean[m][n];
        Queue<Integer> queue = new LinkedList<>();
        int res = 0;
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (A[x][y] == 0) { continue; }

                res++;
                if (x == 0 || y == 0 || x == m - 1 || y == n - 1) {
                    queue.offer(x * n + y);
                }
            }
        }
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            int x = cur / n;
            int y = cur % n;
            if (visited[x][y]) { continue; }

            visited[x][y] = true;
            res--;
            for (int[] move : MOVES) {
                int dx = move[0];
                int dy = move[1];
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && A[nx][ny] == 1) {
                    queue.offer(nx * n + ny);
                }
            }
        }
        return res;
    }

    private void test(int[][] A, int expected) {
        assertEquals(expected, numEnclaves(A));
        assertEquals(expected, numEnclaves2(A));
        assertEquals(expected, numEnclaves3(A));
        assertEquals(expected, numEnclaves4(A));
    }

    @Test public void test() {
        test(new int[][] {{0, 0, 0, 0}, {1, 0, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}}, 3);
        test(new int[][] {{0, 1, 1, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}}, 0);
        test(new int[][] {{0, 1, 1, 0, 0, 0}, {0, 0, 1, 0, 1, 0}, {0, 0, 1, 0, 1, 0},
                          {0, 0, 0, 0, 0, 0}}, 2);
        test(new int[][] {{0, 0, 0, 1, 1, 1, 0, 1, 0, 0}, {1, 1, 0, 0, 0, 1, 0, 1, 1, 1},
                          {0, 0, 0, 1, 1, 1, 0, 1, 0, 0}, {0, 1, 1, 0, 0, 0, 1, 0, 1, 0},
                          {0, 1, 1, 1, 1, 1, 0, 0, 1, 0}, {0, 0, 1, 0, 1, 1, 1, 1, 0, 1},
                          {0, 1, 1, 0, 0, 0, 1, 1, 1, 1}, {0, 0, 1, 0, 0, 1, 0, 1, 0, 1},
                          {1, 0, 1, 0, 1, 1, 0, 0, 0, 0}, {0, 0, 0, 0, 1, 1, 0, 0, 0, 1}}, 3);
        test(new int[][] {{0, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1}, {1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0},
                          {0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0}, {1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 1},
                          {0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0}, {1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1},
                          {0, 1, 0, 1, 1, 0, 0, 0, 1, 0, 0}, {0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 0},
                          {1, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0}, {1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 1}}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
