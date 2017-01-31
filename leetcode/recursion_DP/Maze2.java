import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC499: https://leetcode.com/problems/the-maze-ii/
//
// There is a ball in a maze with empty spaces and walls. The ball can go through
// empty spaces by rolling up (u), down (d), left (l) or right (r), but it won't
// stop rolling until hitting a wall. When the ball stops, it could choose the
// next direction. There is also a hole in this maze. The ball will drop into the
// hole if it rolls on to the hole.
// Given the ball position, the hole position and the maze, your job is to find
// out how the ball could drop into the hole by moving shortest distance in the
// maze. The distance is defined by the number of empty spaces the ball go
// through from the start position (exclude) to the hole (include). Output the
// moving directions by using 'u', 'd', 'l' and 'r'. Since there may have several
// different shortest ways, you should output the lexicographically smallest way.
// If the ball cannot reach the hole, output "impossible".
// The maze is represented by a binary 2D array. 1 means the wall and 0 means
// the empty space. You may assume that the borders of the maze are all walls.
// The ball and hole coordinates are represented by row and column indexes.
public class Maze2 {
    // Recursion + DFS + Backtracking + Set + Bit Manipulation
    // beats 45.51%(35 ms for 64 tests)
    public String findShortestWay(int[][] maze, int[] ball, int[] hole) {
        Path min = new Path("impossible", Integer.MAX_VALUE);
        find(maze, hole, ball[0], ball[1], "", 0, min, 15, new HashMap<>());
        return min.path;
    }

    private static class Path implements Comparable<Path> {
        String path;
        int distance;

        Path(String path, int distance) {
            this.path = path;
            this.distance = distance;
        }

        public int compareTo(Path other) {
            return distance != other.distance ? distance - other.distance : path.compareTo(other.path);
        }
    }

    private static final char[] dirChars = {'d', 'l', 'r', 'u'}; // lexicographically ordered
    private static final int[][] dirShifts = {{1, 0}, {0, -1}, {0, 1}, {-1, 0}};
    private static final int[] dirMasks = {6, 9, 9, 6}; // next possible directions

    private void find(int[][] maze, int[] hole, int row, int col, String path,
                      int dist, Path min, int dirs, Map<Long, Path> visited) {
        if (dist >= min.distance) return;

        long key = ((long)row << 32) | col;
        Path last = visited.get(key);
        Path next = new Path(path, dist);
        if (last != null && last.compareTo(next) < 0) return;

        visited.put(key, next); // visited = new HashSet<>(visited); // as slow as 124 ms
        for (int i = 0; i < 4; i++) {
            if ((dirs & (1 << i)) == 0) continue; // avoid same directions(for performance)

            int dx = dirShifts[i][0];
            int dy = dirShifts[i][1];
            for (int d = dist, x = row + dx, y = col + dy;; x += dx, y += dy, d++) {
                if (x == hole[0] && y == hole[1]) {
                    if (d < min.distance) { // no equal sign here, path already sorted
                        min.distance = d;
                        min.path = path + dirChars[i];
                    }
                    return;
                }
                if (x < 0 || y < 0 || x >= maze.length || y >= maze[0].length || maze[x][y] == 1) {
                    if (d > dist) {
                        find(maze, hole, x - dx, y - dy, path + dirChars[i], d,
                             min, dirMasks[i], visited);
                    }
                    break;
                }
            }
        }
    }

    // BFS + Heap
    // beats 72.46%(18 ms for 64 tests)
    public String findShortestWay2(int[][] maze, int[] ball, int[] hole) {
        int m = maze.length;
        int n = maze[0].length;
        boolean[][][] visited = new boolean[4][m][n];
        PriorityQueue<Position> pq = new PriorityQueue<>();
        pq.offer(new Position(ball[0], ball[1], -1, 0, ""));
        while (!pq.isEmpty()) {
            Position pos = pq.poll();
            if (pos.x == hole[0] && pos.y == hole[1]) return pos.path;

            int x = pos.dir < 0 ? -1 : pos.x + dirShifts[pos.dir][0];
            int y = pos.dir < 0 ? -1 : pos.y + dirShifts[pos.dir][1];
            if (x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0) {
                if (!visited[pos.dir][x][y]) {
                    pq.offer(new Position(x, y, pos.dir, pos.distance + 1, pos.path));
                    visited[pos.dir][x][y] = true;
                }
                continue;
            }
            for (int i = 0; i < 4; i++) { // hit the wall
                x = pos.x + dirShifts[i][0];
                y = pos.y + dirShifts[i][1];
                if (x >= 0 && x < m && y >= 0 && y < n && maze[x][y] == 0) {
                    if (!visited[i][x][y]) {
                        pq.offer(new Position(x, y, i, pos.distance + 1, pos.path + dirChars[i]));
                        visited[i][x][y] = true;
                    }
                }
            }
        }
        return "impossible";
    }

    private static class Position implements Comparable<Position> {
        int x;
        int y;
        int dir;
        int distance;
        String path;

        public Position(int x, int y, int dir, int distance, String path) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.distance = distance;
            this.path = path;
        }

        public int compareTo(Position o) {
            return distance != o.distance ? distance - o.distance : path.compareTo(o.path);
        }
    }

    // BFS + Queue
    // beats 72.46%(18 ms for 64 tests)
    public String findShortestWay3(int[][] maze, int[] ball, int[] hole) {
        int m = maze.length;
        int n = maze[0].length;
        Queue<Point> queue = new LinkedList<>();
        queue.offer(new Point("", 0, ball[0], ball[1]));
        Map<Point, Point> visited = new HashMap<>();
        int minDist = Integer.MAX_VALUE;
        String minPath = "impossible";
        while (!queue.isEmpty()) {
            Point pt = queue.poll();
            for (int i = 0; i < 4; i++) {
                int dx = dirShifts[i][0];
                int dy = dirShifts[i][1];
                String path = pt.path + dirChars[i];
                for (int d = pt.d, x = pt.x + dx, y = pt.y + dy; d <= minDist;
                     x += dx, y += dy, d++) {
                    if (x == hole[0] && y == hole[1]) {
                        if (d < minDist || path.compareTo(minPath) < 0) {
                            minDist = d;
                            minPath = path;
                        }
                        break;
                    }
                    if (x < 0 || y < 0 || x >= m || y >= n || maze[x][y] == 1) {
                        if (d > pt.d) {
                            Point next = new Point(path, d, x - dx, y - dy);
                            Point last = visited.get(next); // faster than Long key?
                            if (last == null || next.compareTo(last) < 0) {
                                visited.put(next, next);
                                queue.offer(next);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return minPath;
    }

    static class Point implements Comparable<Point> {
        String path;
        int d;
        int x;
        int y;

        Point(String path, int d, int x, int y) {
            this.path = path;
            this.d = d;
            this.x = x;
            this.y = y;
        }

        public int compareTo(Point other) {
            return d != other.d ? d - other.d : path.compareTo(other.path);
        }

        public boolean equals(Object other) {
            Point p = (Point)other;
            return x == p.x && y == p.y;
        }

        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    void test(int[][] maze, int[] ball, int[] hole, String expected) {
        assertEquals(expected, findShortestWay(maze, ball, hole));
        assertEquals(expected, findShortestWay2(maze, ball, hole));
        assertEquals(expected, findShortestWay3(maze, ball, hole));
    }

    @Test
    public void test() {
        test(new int[][] {
            {0,0,0},{0,0,0},{0,0,0}
        }, new int[] {0, 0}, new int[] {1, 1}, "impossible");
        test(new int[][] {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0}
        }, new int[] {0, 4}, new int[] {0, 1}, "ldlur");
        test(new int[][] {
            {0, 0, 0, 0, 0},
            {1, 1, 0, 0, 1},
            {0, 0, 0, 0, 0},
            {0, 1, 0, 0, 1},
            {0, 1, 0, 0, 0}
        }, new int[] {4, 3}, new int[] {0, 1}, "lul");
        test(new int[][] {
            {0, 0, 0, 0, 0},
            {1, 1, 0, 0, 1},
            {0, 0, 0, 0, 0},
            {0, 1, 0, 0, 1},
            {0, 1, 0, 0, 0}
        }, new int[] {4,3}, new int[] {3, 0}, "impossible");
        test(new int[][] {
            {0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
            {0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {0, 1, 0, 0, 1, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 1, 0}
        }, new int[] {2,4}, new int[] {7, 6}, "drdrdrdldl");
        test(new int[][] {
            {0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0},
            {0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1},
            {0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0},
            {1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0},
            {0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0},
            {0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0},
            {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0},
            {1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0},
            {0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0},
            {0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1},
            {0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0},
            {1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0},
            {0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0},
            {0,0,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1},
            {0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0},
            {1,0,0,0,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0},
            {0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1},
            {0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,0},
            {1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
            {0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0,0,0,0,0,0,0,1,0},
            {0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1},
            {0,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0},
            {1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0},
            {0,1,0,0,1,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,0,0},
            {0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1},
            {0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0,0},
            {1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,0,0},
            {0,1,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1,0},
            {0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,1,0,0,0,0,0,1,0,0,1,0,0,1}
        },
             new int[] {29, 18}, new int[] {14, 22}, "ururulululululurul");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Maze2");
    }
}
