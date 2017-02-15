import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC505: https://leetcode.com/problems/the-maze-ii/
//
// There is a ball in a maze with empty spaces and walls. The ball can go through
// empty spaces by rolling up, down, left or right, but it won't stop rolling
// until hitting a wall. When the ball stops, it could choose the next direction.
// Given the ball's start position, the destination and the maze, find the
// shortest distance for the ball to stop at the destination.
// The maze is represented by a binary 2D array. 1 means the wall and 0 means
// the empty space. You may assume that the borders of the maze are all walls.
public class Maze2 {
    private static final int[][] dirShifts = {{1, 0}, {0, -1}, {0, 1}, {-1, 0}};
    private static final int[] dirMasks = {6, 9, 9, 6}; // next possible directions

    // Recursion + DFS
    // beats 31.94%(109 ms for 78 tests)
    public int shortestDistance(int[][] maze, int[] start, int[] destination) {
        int m = maze.length;
        int n = maze[0].length;
        if (isValid(maze, m, n, destination)) return -1; // speed or not?

        int[][] dist = new int[m][n];
        dist[start[0]][start[1]] = 1;
        dfs(start[0], start[1], destination, maze, m, n, 15, dist);
        return dist[destination[0]][destination[1]] - 1;
    }

    private void dfs(int startX, int startY, int[] dest, int[][] maze,
                     int m, int n, int dirs, int[][] dist) {
        for (int i = 0; i < 4; i++) {
            if ((dirs & (1 << i)) == 0) continue; // avoid same directions(for performance)

            int dx = dirShifts[i][0];
            int dy = dirShifts[i][1];
            int x = startX;
            int y = startY;
            int step = dist[x][y] - 1;
            for (; x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0;
                 x += dx, y += dy, step++) {}
            x -= dx;
            y -= dy;
            if (dist[x][y] <= 0 || dist[x][y] > step) {
                dist[x][y] = step;
                if (x != dest[0] || y != dest[1]) {
                    dfs(x, y, dest, maze, m, n, dirMasks[i], dist);
                }
            }
        }
    }

    // BFS + Queue
    // beats 82.94%(25 ms for 78 tests)
    public int shortestDistance2(int[][] maze, int[] start, int[] destination) {
        int m = maze.length;
        int n = maze[0].length;
        if (isValid(maze, m, n, destination)) return -1; // speed or not?

        int[][] dist = new int[m][n];
        dist[start[0]][start[1]] = 1;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(start);
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int[] shift : dirShifts) {
                int x = cur[0];
                int y = cur[1];
                int step = dist[x][y] - 1;
                for (; x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0;
                     x += shift[0], y += shift[1], step++) {}
                x -= shift[0];
                y -= shift[1];
                if (dist[x][y] <= 0 || dist[x][y] > step) {
                    dist[x][y] = step;
                    queue.offer(new int[] {x, y});
                }
            }
        }
        return dist[destination[0]][destination[1]] - 1;
    }

    // BFS + Heap
    // beats 65.52%(33 ms for 78 tests)
    public int shortestDistance3(int[][] maze, int[] start, int[] destination) {
        int m = maze.length;
        int n = maze[0].length;
        if (isValid(maze, m, n, destination)) return -1; // speed or not?

        int[][] dist = new int[m][n];
        dist[start[0]][start[1]] = 1;
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return dist[a[0]][a[1]] - dist[b[0]][a[1]];
            }});
        pq.offer(start);
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            for (int[] shift : dirShifts) {
                int x = cur[0];
                int y = cur[1];
                int step = dist[x][y] - 1;
                for (; x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0;
                     x += shift[0], y += shift[1], step++) {}
                x -= shift[0];
                y -= shift[1];
                if (dist[x][y] <= 0 || dist[x][y] > step) {
                    dist[x][y] = step;
                    pq.offer(new int[] {x, y});
                }
            }
        }
        return dist[destination[0]][destination[1]] - 1;
    }

    private boolean isValid(int[][] maze, int m, int n, int[] dest) {
        int res = 0;
        int i = 0;
        for (int[] shift : dirShifts) {
            int x = dest[0];
            int y = dest[1];
            x += shift[0];
            y += shift[1];
            if (x < 0 || x >= m || y < 0 || y >=n || maze[x][y] == 1) {
                res += (1 << i);
            }
            i++;
        }
        return res == 15 || res == 0 || res == 6 || res == 9;
    }

    void test(int[][] maze, int[] start, int[] dest, int expected) {
        assertEquals(expected, shortestDistance(maze, start, dest));
        assertEquals(expected, shortestDistance2(maze, start, dest));
        assertEquals(expected, shortestDistance3(maze, start, dest));
    }

    @Test
    public void test() {
        test(new int[][] {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0},
        }, new int[] {0, 4}, new int[] {4, 4}, 12);
        test(new int[][] {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0},
        }, new int[] {0, 4}, new int[] {3, 2}, -1);
        // test(new int[][] {
        //     {0, 0, 1, 0, 0},
        //     {0, 0, 0, 0, 0},
        //     {0, 0, 0, 1, 0},
        //     {1, 1, 0, 1, 1},
        //     {0, 0, 0, 0, 0},
        // }, new int[] {0, 4}, new int[] {1, 2}, true);
        test(new int[][] {
            {0, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1},
            {0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 1, 0, 0, 0, 1},
            {0, 0, 0, 0, 1, 0, 0}
        }, new int[] {0, 0}, new int[] {8, 6}, 26);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Maze2");
    }
}
