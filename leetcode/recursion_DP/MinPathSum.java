import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC064: https://leetcode.com/problems/minimum-path-sum/
//
// Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right which minimizes the sum of all numbers along its path.
// Note: You can only move either down or right at any point in time.
public class MinPathSum {
    // Dynamic Programming(2D array)
    // beats %(4 ms)
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        if (n == 0) return 0;
        // if (m == 1) {
        //     return Arrays.stream(grid[0]).reduce(0, Integer::sum);
        // }

        // if (n == 1) {
        //     return Arrays.stream(grid).map(a -> a[0]).reduce(0, Integer::sum);
        // }
        int[][] min = new int[m][n];
        min[0][0] = grid[0][0];
        for (int i = 1; i < m; i++) {
            min[i][0] = grid[i][0] + min[i - 1][0];
        }
        for (int i = 1; i < n; i++) {
            min[0][i] = grid[0][i] + min[0][i - 1];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                min[i][j] = grid[i][j] + Math.min(min[i][j - 1], min[i - 1][j]);
            }
        }
        return min[m - 1][n - 1];
    }

    // Breadth first search
    // beats 0.48%(29 ms)
    public int minPathSum2(int[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        if (n == 0) return 0;

        int[][] mins = new int[m][n];
        mins[0][0] = grid[0][0] + 1; // make sure all values are positive
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[] {0, 0});
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int x = pos[0];
            int y = pos[1];
            if (x + 1 < m) {
                if (mins[x + 1][y] == 0) {
                    mins[x + 1][y] = mins[x][y] + grid[x + 1][y];
                    queue.add(new int[] {x + 1, y});
                } else {
                    mins[x + 1][y] = Math.min(mins[x + 1][y],
                                              mins[x][y] + grid[x + 1][y]);
                }
            }
            if (y + 1 < n) {
                if (mins[x][y + 1] == 0) {
                    mins[x][y + 1] = mins[x][y] + grid[x][y + 1];
                    queue.add(new int[] {x, y + 1});
                } else {
                    mins[x][y + 1] = Math.min(mins[x][y + 1],
                                              mins[x][y] + grid[x][y + 1]);
                }
            }
        }
        return mins[m - 1][n - 1] - 1;
    }

    static class Vertex {
        int x;
        int y;
        int min;

        Vertex(int x, int y, int min) {
            this.x = x;
            this.y = y;
            this.min = min;
        }

        Vertex(int x, int y) {
            this(x, y, Integer.MAX_VALUE);
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Vertex)) return false;
            Vertex that = (Vertex)other;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Dijkstra algorithm
    // beats 0.08%(151 ms)
    public int minPathSum3(int[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        if (n == 0) return 0;

        PriorityQueue<Vertex> queue = new PriorityQueue<>((a, b) -> a.min - b.min);
        Map<Vertex, Vertex> visited = new HashMap<>();
        queue.add(new Vertex(0, 0, grid[0][0]));
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            int x = v.x;
            int y = v.y;
            if (x + 1 == m && y + 1 == n) return v.min;

            visited.put(v, v);
            if (x + 1 < m) {
                visit(visited, queue, grid, v, x + 1, y);
            }
            if (y + 1 < n) {
                visit(visited, queue, grid, v, x, y + 1);
            }
        }
        return Integer.MAX_VALUE;
    }

    private void visit(Map<Vertex, Vertex>visited, PriorityQueue<Vertex> queue,
                       int[][] grid, Vertex v, int x, int y) {
        Vertex next = new Vertex(x, y);
        if (visited.containsKey(next)) {
            next = visited.get(next); // get object by reference instead of value
            next.min = Math.min(next.min, v.min + grid[x][y]);
        } else {
            next.min = v.min + grid[x][y];
            visited.put(next, next);
            queue.add(next);
        }
    }

    // Solution of Choice
    // Dynamic Programming(1D array, aka rolling array)
    // beats 92.89%(3 ms)
    public int minPathSum4(int[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        if (n == 0) return 0;

        int[] min = new int[n];
        for (int i = 1; i < n; i++) {
            min[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < m; i++) {
            min[0] += grid[i][0];
            for (int j = 1; j < n; j++) {
                min[j] = grid[i][j] + Math.min(min[j - 1], min[j]);
            }
        }
        return min[n - 1];
    }

    void test(Function<int[][], Integer> min, String name,
              int expected, int[][] grid) {
        assertEquals(expected, (int)min.apply(grid));
    }

    void test(int expected, int[][] grid) {
        MinPathSum m = new MinPathSum();
        test(m::minPathSum, "minPathSum", expected, grid);
        test(m::minPathSum2, "minPathSum2", expected, grid);
        test(m::minPathSum3, "minPathSum3", expected, grid);
        test(m::minPathSum4, "minPathSum4", expected, grid);
    }

    @Test
    public void test1() {
        test(11, new int[][] {{2, 0, 6, 4}, {4, 6, 0, 1}, {0, 5, 5, 2}});
        test(10, new int[][] {{1}, {2}, {3}, {4}});
        test(10, new int[][] {{1, 2, 3, 4}});
        test(7, new int[][] {{1, 2}, {3, 4}});
        test(15, new int[][] {
            {2, 0, 6, 4, 5, 3}, {0, 2, 4, 9, 4, 7}, {4, 6, 2, 0, 1, 3},
            {0, 3, 5, 5, 2, 1}, {2, 3, 4, 1, 0, 2}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinPathSum");
    }
}
