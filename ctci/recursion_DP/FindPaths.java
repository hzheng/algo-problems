import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.awt.Point;

/**
 * Cracking the Coding Interview(5ed) Problem 9.2:
 * A robot sits on the upper left corner of an X by Y grid. It can only move in
 * two directions: right and down. How many possible paths are there for the
 * robot to go from (0, 0) to (X, Y) ?
 * Imagine certain spots are "off limits" such that the robot cannot step on.
 * Find a path for the robot from the top left to the bottom right.
 */
public class FindPaths {
    // in fact, we can get answer directly from combination:  (x+y)!/(x!y!)
    public static int countPaths(int x, int y) {
        if (x < 0 || y < 0) return 0;
        if (x == 0 || y == 0) return 1;

        return countPaths(x, y - 1) + countPaths(x - 1, y);
    }

    private static int MAZE_W = 20;
    private static int MAZE_H = 20;
    private static int[][] maze = new int[MAZE_W][MAZE_H];

    private static boolean isFree(int x, int y) {
        return (maze[x][y] != 0);
    }

    public static boolean getPath(int x, int y, List<Point> path) {
        if (x == 0 && y == 0) {
            return true;
        }

        boolean found = false;
        if (x > 0 && isFree(x - 1, y)) {
            found = getPath(x - 1, y, path);
        }
        if (!found && y > 0 && isFree(x, y - 1)) {
            found = getPath(x, y - 1, path);
        }
        if (found) {
            path.add(new Point(x, y));
        }
        return found;
    }

    public static boolean getPathDP(int x, int y, List<Point> path) {
        return getPathDP(x, y, path, new HashMap<Point, Boolean>());
    }

    private static boolean getPathDP(int x, int y, List<Point> path,
                                     Map<Point, Boolean> cache) {
        Point p = new Point(x, y);
        if (cache.containsKey(p)) {
            return cache.get(p);
        }

        if (x == 0 && y == 0) {
            return true; // found a path
        }

        boolean found = false;
        if (x > 0 && isFree(x - 1, y)) {
            found = getPathDP(x - 1, y, path, cache);
        }
        if (!found && y > 0 && isFree(x, y - 1)) {
            found = getPathDP(x, y - 1, path, cache);
        }
        if (found) {
            path.add(p);
        }
        cache.put(p, found);
        return found;
    }

    private static int[][] randomMatrix(int m, int n, int min, int max) {
        int[][] matrix = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (int)(Math.random() * (max - min + 1) + min);
            }
        }
        return matrix;
    }

    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.format("%2d", matrix[i][j]);
            }
            System.out.println();
        }
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    static void getPath(int x, int y,
                        Function<Integer, Integer, List<Point>, Boolean> find) {
        List<Point> path = new ArrayList<Point>();
        if (find.apply(x, y, path)) {
            // NOTE: row -> y col -> x
            path.forEach(p -> System.out.print(
                             "(" + p.y + "," + p.x + ")->"));
            System.out.println("|");
        } else {
            System.out.println("No path found.");
        }
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("FindPaths");
        int row = 8;
        int col = 8;
        for (int i = 0; i < 1; i++) {
            maze = randomMatrix(row, col, 0, 4);
            // maze = new int[][] {{8, 0, 0, 2}, {7, 4, 6, 9},
                            //  {0, 0, 5, 0}, {4, 0, 1, 3}};
            printMatrix(maze);
            getPath(row - 1, col - 1, FindPaths::getPath);
            getPath(row - 1, col - 1, FindPaths::getPathDP);
        }
    }
}
