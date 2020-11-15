import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC803: https://leetcode.com/problems/bricks-falling-when-hit/
//
// We have a grid of 1s and 0s; the 1s in a cell represent bricks.  A brick will not drop if and
// only if it is directly connected to the top of the grid, or at least one of its (4-way) adjacent
// bricks will not drop. We will do some erasures sequentially. Each time we want to do the erasure
// at the location (i, j), the brick (if it exists) on that location will disappear, and then some
// other bricks may drop because of that erasure.
// Return an array representing the number of bricks that will drop after each erasure in sequence.
//
// Note:
// The number of rows and columns in the grid will be in the range [1, 200].
// The number of erasures will not exceed the area of the grid.
// It is guaranteed that each erasure will be different from any other erasure, and located inside
// the grid.
// An erasure may refer to a location with no brick - if it does, no bricks drop.
public class HitBricks {
    private static final int[][] MOVES = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // Union Find
    // time complexity: O(M*N*Q), space complexity: O(M*N)
    // 18 ms(14.18%), 55.4 MB(9.93%) for 40 tests
    public int[] hitBricks(int[][] grid, int[][] hits) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] grid2 = new int[m][n];
        for (int i = 0; i < m; i++) {
            grid2[i] = grid[i].clone();
        }
        for (int[] hit : hits) {
            grid2[hit[0]][hit[1]] = 0;
        }
        DisjointSet ds = new DisjointSet(m * n + 1);
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (grid2[x][y] == 1) {
                    connect(grid2, ds, x, y);
                }
            }
        }
        int[] res = new int[hits.length];
        for (int i = hits.length - 1, prevCount = ds.topCount(); i >= 0; i--) {
            int[] hit = hits[i];
            int x = hit[0];
            int y = hit[1];
            if (grid[x][y] == 1) {
                grid2[x][y] = 1;
                connect(grid2, ds, x, y);
                int count = ds.topCount();
                res[i] = Math.max(0, count - prevCount - 1);
                prevCount = count;
            }
        }
        return res;
    }

    private void connect(int[][] grid, DisjointSet ds, int x, int y) {
        int m = grid.length;
        int n = grid[0].length;
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx < 0) {
                ds.unionTop(x * n + y);
            } else if (nx < m && ny < n && ny >= 0 && grid[nx][ny] == 1) {
                ds.union(x * n + y, nx * n + ny);
            }
        }
    }

    private static class DisjointSet {
        private final int[] id;

        public DisjointSet(int n) {
            id = new int[n];
            Arrays.fill(id, -1);
        }

        public int root(int x) {
            for (; id[x] >= 0; x = id[x]) {}
            return x;
        }

        public boolean union(int x, int y) {
            int px = root(x);
            int py = root(y);
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

        public boolean unionTop(int x) {
            return union(x, id.length - 1);
        }

        public int topCount() {
            return -id[root(id.length - 1)] - 1;
        }
    }

    // Union Find
    // time complexity: O(M*N*Q), space complexity: O(M*N)
    // 15 ms(19.15%), 53.6 MB(11.35%) for 40 tests
    public int[] hitBricks2(int[][] grid, int[][] hits) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] grid2 = new int[m][n];
        for (int i = 0; i < m; i++) {
            grid2[i] = grid[i].clone();
        }
        for (int[] hit : hits) {
            grid2[hit[0]][hit[1]] = 0;
        }
        DisjointSet ds = new DisjointSet(m * n + 1);
        for (int x = 0; x < m; ++x) {
            for (int y = 0; y < n; ++y) {
                if (grid2[x][y] == 1) {
                    int i = x * n + y;
                    if (x == 0) {
                        ds.union(i, m * n);
                    }
                    if (x > 0 && grid2[x - 1][y] == 1) {
                        ds.union(i, (x - 1) * n + y);
                    }
                    if (y > 0 && grid2[x][y - 1] == 1) {
                        ds.union(i, x * n + y - 1);
                    }
                }
            }
        }
        int[] res = new int[hits.length];
        for (int i = hits.length - 1, preCount = ds.topCount(); i >= 0; i--) {
            int x = hits[i][0];
            int y = hits[i][1];
            if (grid[x][y] == 1) {
                grid2[x][y] = 1;
                int z = x * n + y;
                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid2[nx][ny] == 1) {
                        ds.union(z, nx * n + ny);
                    }
                }
                if (x == 0) {
                    ds.union(z, m * n);
                }
                int count = ds.topCount();
                res[i] = Math.max(0, count - preCount - 1);
                preCount = count;
            }
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(M*N*Q), space complexity: O(M*N)
    // 10 ms(24.11%), 54.2 MB(11.35%) for 40 tests
    public int[] hitBricks3(int[][] grid, int[][] hits) {
        for (int[] hit : hits) {
            int x = hit[0];
            int y = hit[1];
            if (grid[x][y] == 1) {
                grid[x][y] = -1;
            }
        }
        for (int i = 0; i < grid[0].length; i++) {
            dfs(grid, 0, i);
        }
        int[] res = new int[hits.length];
        for (int k = hits.length - 1; k >= 0; k--) {
            int x = hits[k][0];
            int y = hits[k][1];
            if (grid[x][y] != 0) {
                grid[x][y] = 1;
                if (connectedTop(grid, x, y)) {
                    res[k] = dfs(grid, x, y) - 1;
                }
            }
        }
        return res;
    }

    private int dfs(int[][] grid, int x, int y) {
        if (grid[x][y] != 1) { return 0; }

        int m = grid.length;
        int n = grid[0].length;
        int count = 1;
        grid[x][y] = 2;
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == 1) {
                count += dfs(grid, nx, ny);
            }
        }
        return count;
    }

    private boolean connectedTop(int[][] grid, int x, int y) {
        if (x == 0) { return true; }

        int m = grid.length;
        int n = grid[0].length;
        for (int[] move : MOVES) {
            int nx = x + move[0];
            int ny = y + move[1];
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == 2) { return true; }
        }
        return false;
    }

    private void test(int[][] grid, int[][] hits, int[] expected) {
        assertArrayEquals(expected, hitBricks(grid, hits));
        assertArrayEquals(expected, hitBricks2(grid, hits));
        assertArrayEquals(expected, hitBricks3(grid, hits));
    }

    @Test public void test() {
        test(new int[][] {{1, 0, 1}, {1, 1, 1}}, new int[][] {{0, 0}, {0, 2}, {1, 1}},
             new int[] {0, 3, 0});
        test(new int[][] {{1, 0, 0, 0}, {1, 1, 1, 0}}, new int[][] {{1, 0}}, new int[] {2});
        test(new int[][] {{1, 0, 0, 0}, {1, 1, 0, 0}}, new int[][] {{1, 1}, {1, 0}},
             new int[] {0, 0});
        test(new int[][] {{1}, {1}, {1}, {1}, {1}},
             new int[][] {{3, 0}, {4, 0}, {1, 0}, {2, 0}, {0, 0}}, new int[] {1, 0, 1, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
