import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC490: https://leetcode.com/problems/the-maze/
//
// There is a ball in a maze with empty spaces and walls. The ball can go through
// empty spaces by rolling up, down, left or right, but it won't stop rolling
// until hitting a wall. When the ball stops, it could choose the next direction.
// Given the ball's start position, the destination and the maze, determine
// whether the ball could stop at the destination.
// The maze is represented by a binary 2D array. 1 means the wall and 0 means
// the empty space. You may assume that the borders of the maze are all walls.
public class Maze {
    private static final int[][] dirShifts = {{1, 0}, {0, -1}, {0, 1}, {-1, 0}};
    private static final int[] dirMasks = {6, 9, 9, 6}; // next possible directions

    // Recursion + DFS + Set
    // beats 13.52%(41 ms for 78 tests)
    public boolean hasPath(int[][] maze, int[] start, int[] destination) {
        int m = maze.length;
        if (m == 0) return false;

        return dfs(maze, start[0], start[1], destination, m, maze[0].length, new HashSet<>());
    }

    private boolean dfs(int[][] maze, int startX, int startY, int[] destination,
                        int m, int n, Set<Long> visited) {
        long key = ((long)startX << 32) + startY;
        if (visited.contains(key)) return false;

        visited.add(key);
        for (int[] shift : dirShifts) {
            int x = startX;
            int y = startY;
            for (; x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0;
                 x += shift[0], y += shift[1]) {}
            x -= shift[0];
            y -= shift[1];
            if (x == destination[0] && y == destination[1]
                || dfs(maze, x, y, destination, m, n, visited)) return true;
        }
        return false;
    }

    // Recursion + DFS + Set
    // beats 97.60%(10 ms for 78 tests)
    public boolean hasPath2(int[][] maze, int[] start, int[] destination) {
        int m = maze.length;
        if (m == 0) return false;

        int n = maze[0].length;
        return dfs2(maze, start[0], start[1], destination, m, n, new boolean[m][n]);
    }

    private boolean dfs2(int[][] maze, int startX, int startY, int[] destination,
                         int m, int n, boolean[][] visited) {
        if (visited[startX][startY]) return false;

        visited[startX][startY] = true;
        for (int[] shift : dirShifts) {
            int x = startX;
            int y = startY;
            for (; x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0;
                 x += shift[0], y += shift[1]) {}
            x -= shift[0];
            y -= shift[1];
            if (x == destination[0] && y == destination[1]
                || dfs2(maze, x, y, destination, m, n, visited)) return true;
        }
        return false;
    }

    // Recursion + DFS + Set
    // beats 94.08%(11 ms for 78 tests)
    public boolean hasPath3(int[][] maze, int[] start, int[] destination) {
        int m = maze.length;
        if (m == 0) return false;

        int n = maze[0].length;
        return dfs3(maze, start[0], start[1], destination, m, n, 15, new boolean[m][n]);
    }

    private boolean dfs3(int[][] maze, int startX, int startY, int[] destination,
                         int m, int n, int dirs, boolean[][] visited) {
        if (visited[startX][startY]) return false;

        visited[startX][startY] = true;
        for (int i = 0; i < 4; i++) {
            if ((dirs & (1 << i)) == 0) continue; // avoid same directions(for performance)

            int dx = dirShifts[i][0];
            int dy = dirShifts[i][1];
            int x = startX;
            int y = startY;
            for (; x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0;
                 x += dx, y += dy) {}
            x -= dx;
            y -= dy;
            if (x == destination[0] && y == destination[1]
                || dfs3(maze, x, y, destination, m, n, dirMasks[i], visited)) return true;
        }
        return false;
    }

    // BFS + Queue
    // beats 42.88%(19 ms for 78 tests)
    public boolean hasPath4(int[][] maze, int[] start, int[] destination) {
        int m = maze.length;
        if (m == 0) return false;

        int n = maze[0].length;
        boolean[][] visited = new boolean[m][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(start);
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] shift : dirShifts) {
                int x = cur[0];
                int y = cur[1];
                for (; x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0;
                     x += shift[0], y += shift[1]) {}
                x -= shift[0];
                y -= shift[1];
                if (x == destination[0] && y == destination[1]) return true;

                if (!visited[x][y]) {
                    visited[x][y] = true;
                    queue.offer(new int[] {x, y});
                }
            }
        }
        return false;
    }

    void test(int[][] maze, int[] start, int[] dest, boolean expected) {
        assertEquals(expected, hasPath(maze, start, dest));
        assertEquals(expected, hasPath2(maze, start, dest));
        assertEquals(expected, hasPath3(maze, start, dest));
        assertEquals(expected, hasPath4(maze, start, dest));
    }

    @Test
    public void test() {
        test(new int[][] {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0},
        }, new int[] {0, 4}, new int[] {4, 4}, true);
        test(new int[][] {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0},
        }, new int[] {0, 4}, new int[] {3, 2}, false);
        test(new int[][] {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0},
        }, new int[] {0, 4}, new int[] {1, 2}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Maze");
    }
}
