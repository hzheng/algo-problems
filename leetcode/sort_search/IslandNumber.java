import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

// LC200: https://leetcode.com/problems/number-of-islands/
//
// Given a 2d grid map of '1's (land) and '0's (water), count the number of
// islands. An island is surrounded by water and is formed by connecting
// adjacent lands horizontally or vertically. You may assume all four edges of
// the grid are all surrounded by water.
public class IslandNumber {
    private static final char LAND = '1';
    private static final char MARK = ' ';
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    // Solution of Choice
    // BFS + Queue
    // time complexity: O(N * M), space complexity: O(1)
    // beats 25.69%(8 ms for 47 tests)
    public int numIslands(char[][] grid) {
        int nRow = grid.length;
        if (nRow == 0) return 0;

        int nCol = grid[0].length;
        int islands = 0;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] == LAND) {
                    markIsland(grid, nRow, nCol, i, j);
                    islands++;
                }
            }
        }
        // restore grid if required
        // for (int i = 0; i < nRow; i++) {
        // for (int j = 0; j < nCol; j++) {
        // if (grid[i][j] == MARK) {
        // grid[i][j] = LAND;
        // }
        // }
        // }
        return islands;
    }

    private void markIsland(char[][] grid, int nRow, int nCol, int startX, int startY) {
        Queue<int[]> island = new LinkedList<>();
        island.offer(new int[] {startX, startY});
        grid[startX][startY] = MARK;
        while (!island.isEmpty()) {
            int[] pos = island.poll();
            for (int[] dir : MOVES) {
                int x = pos[0] + dir[0];
                int y = pos[1] + dir[1];
                if (x >= 0 && y >= 0 && x < nRow && y < nCol && grid[x][y] == LAND) {
                    island.offer(new int[] {x, y});
                    grid[x][y] = MARK;
                }
            }
        }
    }

    // DFS + Recursion
    // time complexity: O(N * M), space complexity: O(1)
    // beats 33.18%(7 ms for 47 tests)
    public int numIslands2(char[][] grid) {
        int nRow = grid.length;
        if (nRow == 0) return 0;

        int nCol = grid[0].length;
        int islands = 0;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] == LAND) {
                    markIsland2(grid, nRow, nCol, i, j);
                    islands++;
                }
            }
        }
        // restore grid if required
        return islands;
    }

    private void markIsland2(char[][] grid, int nRow, int nCol, int x, int y) {
        grid[x][y] = MARK;
        for (int[] dir : MOVES) {
            int i = x + dir[0];
            int j = y + dir[1];
            if (i >= 0 && j >= 0 && i < nRow && j < nCol && grid[i][j] == LAND) {
                markIsland2(grid, nRow, nCol, i, j);
            }
        }
    }

    // DFS + Recursion
    // time complexity: O(N * M), space complexity: O(1)
    // beats 95.19%(4 ms for 47 tests)
    public int numIslands2_2(char[][] grid) {
        int islands = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == '1') {
                    islands++;
                    dfs(grid, i, j);
                }
            }
        }
        return islands;
    }

    private void dfs(char[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[i].length || grid[i][j] == '0') return;

        grid[i][j] = '0';
        dfs(grid, i + 1, j);
        dfs(grid, i - 1, j);
        dfs(grid, i, j + 1);
        dfs(grid, i, j - 1);
    }

    // Union Find
    // time complexity: O(N * M * log(K)), space complexity: O(N * M)
    // beats 33.18%(7 ms for 47 tests)
    public int numIslands3(char[][] grid) {
        int nRow = grid.length;
        if (nRow == 0) return 0;

        int nCol = grid[0].length;
        int[] root = new int[nRow * nCol];
        int islands = 0;
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] == '1') {
                    root[i * nCol + j] = i * nCol + j;
                    islands++;
                }
            }
        }
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                if (grid[i][j] != '1') continue;

                for (int[] move : MOVES) {
                    int x = i + move[0];
                    int y = j + move[1];
                    if (x >= 0 && x < nRow && y >= 0 && y < nCol && grid[x][y] == '1') {
                        int root1 = getRoot(root, i * nCol + j);
                        int root2 = getRoot(root, x * nCol + y);
                        if (root1 != root2) {
                            root[root1] = root2;
                            islands--;
                        }
                    }
                }
            }
        }
        return islands;
    }

    private int getRoot(int[] id, int i) {
        for (; i != id[i]; i = id[i] = id[id[i]]) {}
        return i;
    }

    // Solution of Choice
    // Union Find
    // time complexity: O(N * M * log(K)), space complexity: O(N * M)
    // beats 33.18%(7 ms for 47 tests)
    public int numIslands4(char[][] grid) {
        int m = grid.length;
        if (m == 0) return 0;

        int n = grid[0].length;
        int[] id = new int[m * n];
        Arrays.fill(id, -1);
        int islands = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '0') continue;

                islands++;
                for (int[] move : MOVES) {
                    int x = i + move[0];
                    int y = j + move[1];
                    if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == '1') {
                        islands -= union(id, i * n + j, x * n + y) ? 1 : 0;
                    }
                }
            }
        }
        return islands;
    }

    private int parent(int[] id, int x) {
        int p = x;
        for (; id[p] >= 0; p = id[p]) {}
        return p;
    }

    private boolean union(int[] id, int x, int y) {
        int px = parent(id, x);
        int py = parent(id, y);
        if (px == py) return false;

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    void test(Function<char[][], Integer> count, int expected, String... s) {
        assertEquals(expected, (int)count.apply(convert(s)));
    }

    private char[][] convert(String[] s) {
        char[][] board = new char[s.length][];
        for (int i = 0; i < s.length; i++) {
            board[i] = s[i].toCharArray();
        }
        return board;
    }

    void test(int expected, String... s) {
        IslandNumber i = new IslandNumber();
        test(i::numIslands, expected, s);
        test(i::numIslands2, expected, s);
        test(i::numIslands2_2, expected, s);
        test(i::numIslands3, expected, s);
        test(i::numIslands4, expected, s);
    }

    @Test
    public void test1() {
        test(1, "111", "010", "111");
        test(1, "1111", "1101", "1100");
        test(1, "11110", "11010", "11000", "00000");
        test(3, "11000", "11000", "00100", "00011");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
