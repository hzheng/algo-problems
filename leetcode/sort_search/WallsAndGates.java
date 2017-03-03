import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC286: https://leetcode.com/problems/walls-and-gates/?tab=Description
//
// You are given a m x n 2D grid initialized with these three possible values.
// -1 - A wall or an obstacle.
// 0 - A gate.
// INF - Infinity means an empty room.
// Fill each empty room with the distance to its nearest gate. If it is impossible
// to reach a gate, it should be filled with INF.
public class WallsAndGates {
    private static final int INF = Integer.MAX_VALUE;

    private static final int[][] MOVES = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    // DFS + Recursion
    // beats 72.33%(11 ms for 62 tests)
    public void wallsAndGates(int[][] rooms) {
        int m = rooms.length;
        if (m == 0) return;

        int n = rooms[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) {
                    dfs(rooms, m, n, i, j, 1);
                }
            }
        }
    }

    private void dfs(int[][] rooms, int m, int n, int x, int y, int distance) {
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx < 0 || nx >= m || ny < 0 || ny >= n || rooms[nx][ny] <= distance) continue;

            rooms[nx][ny] = distance;
            dfs(rooms, m, n, nx, ny, distance + 1);
        }
    }

    // DFS + Recursion
    // beats 74.47%(10 ms for 62 tests)
    public void wallsAndGates_2(int[][] rooms) {
        int m = rooms.length;
        if (m == 0) return;

        int n = rooms[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) {
                    dfs_2(rooms, m, n, i, j);
                }
            }
        }
    }

    private void dfs_2(int[][] rooms, int m, int n, int x, int y) {
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx < 0 || nx >= m || ny < 0 || ny >= n || rooms[nx][ny] <= rooms[x][y]) continue;

            rooms[nx][ny] = rooms[x][y] + 1;
            dfs_2(rooms, m, n, nx, ny);
        }
    }

    // DFS + Recursion
    // beats 86.87%(7 ms for 62 tests)
    public void wallsAndGates2(int[][] rooms) {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] == 0) {
                    dfs2(rooms, i, j, 0);
                }
            }
        }
    }

    private void dfs2(int[][] rooms, int x, int y, int distance) {
        if (x < 0 || x >= rooms.length || y < 0 || y >= rooms[0].length || rooms[x][y] < distance) return;

        rooms[x][y] = distance;
        dfs2(rooms, x - 1, y, distance + 1);
        dfs2(rooms, x + 1, y, distance + 1);
        dfs2(rooms, x, y - 1, distance + 1);
        dfs2(rooms, x, y + 1, distance + 1);
    }

    // BFS + Queue
    // beats 28.80%(20 ms for 62 tests)
    public void wallsAndGates3(int[][] rooms) {
        int m = rooms.length;
        if (m == 0) return;

        int n = rooms[0].length;
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) {
                    queue.offer(new int[] {i, j});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];
            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || rooms[nx][ny] <= rooms[x][y]) continue;

                rooms[nx][ny] = rooms[x][y] + 1;
                queue.offer(new int[] {nx, ny});
            }
        }
    }

    // BFS + Queue
    // beats 28.80%(20 ms for 62 tests)
    public void wallsAndGates4(int[][] rooms) {
        int m = rooms.length;
        if (m == 0) return;

        int n = rooms[0].length;
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == 0) {
                    queue.offer(new int[] {i, j});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];
            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || rooms[nx][ny] != INF) continue;

                rooms[nx][ny] = rooms[x][y] + 1;
                queue.offer(new int[] {nx, ny});
            }
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<int[][]> wallsAndGates,
              int[][] rooms, int[][] expected) {
        rooms = Utils.clone(rooms);
        wallsAndGates.apply(rooms);
        assertArrayEquals(expected, rooms);
    }

    void test(int[][] rooms, int[][] expected) {
        WallsAndGates w = new WallsAndGates();
        test(w::wallsAndGates, rooms, expected);
        test(w::wallsAndGates_2, rooms, expected);
        test(w::wallsAndGates2, rooms, expected);
        test(w::wallsAndGates3, rooms, expected);
        test(w::wallsAndGates4, rooms, expected);
    }

    @Test
    public void test() {
        test(new int[][] {
            {INF, -1,    0, INF},
            {INF, INF, INF, -1},
            {INF, -1, INF, -1},
            {0,   -1, INF, INF}
        },
             new int[][] {{3, -1, 0, 1},
                          {2, 2, 1, -1},
                          {1, -1, 2, -1},
                          {0, -1, 3, 4}});
        test(new int[][] {
            {0, INF, -1, INF, INF, -1, -1, 0, 0, -1, INF, INF, 0, -1, INF, INF,
             INF, INF,0,  INF, 0, -1, -1, -1, -1, INF, -1, -1, INF, INF, -1, -1,
             0, 0, -1, 0, 0, 0, INF, 0, INF, -1, -1, 0, -1, 0, 0, 0, INF},
            {INF, 0, -1, INF, 0, -1, -1, -1, -1, 0, 0, INF, INF, -1, -1, INF,
             -1, -1, INF, INF,-1, 0, -1, INF, 0, INF, -1, INF, 0, INF, 0, INF,
             -1, INF, 0, INF, -1, INF, 0, INF, INF, 0, -1, INF, -1, -1, -1, 0, INF}
        },
             new int[][] {
            {0, 1, -1, 2, 1, -1, -1, 0, 0, -1, 1, 1, 0, -1, 4, 3, 2, 1, 0, 1, 0, -1, -1, -1, -1,
             2, -1, -1, 1, 2, -1, -1, 0, 0, -1, 0, 0, 0, 1, 0, 1, -1, -1, 0, -1, 0, 0, 0, 1},
            {1, 0, -1, 1, 0, -1, -1, -1, -1, 0, 0, 1, 1, -1, -1, 4, -1, -1, 1, 2, -1, 0, -1,
             1, 0, 1, -1, 1, 0, 1, 0, 1, -1, 1, 0, 1, -1, 1, 0, 1, 1, 0, -1, 1, -1, -1, -1, 0, 1}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WallsAndGates");
    }
}
