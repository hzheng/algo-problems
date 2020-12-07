import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC959: https://leetcode.com/problems/regions-cut-by-slashes/
//
// In a N x N grid composed of 1 x 1 squares, each 1 x 1 square consists of a /, \, or blank space.
// These characters divide the square into contiguous regions.
// Return the number of regions.
//
// Note:
// 1 <= grid.length == grid[0].length <= 30
// grid[i][j] is either '/', '\', or ' '.
public class RegionsBySlashes {
    // Union Find
    // time complexity: O(N^2*a(N)), space complexity: O(N*N)
    // 3 ms(92.10%), 38.2 MB(63.01%) for 137 tests
    public int regionsBySlashes(String[] grid) {
        int n = grid.length;
        int[] id = new int[n * n * 4]; // divide each cell by two diagonal lines
        Arrays.fill(id, -1); // assign 4 divided zones by 0, 1, 2, 3 counterclockwise
        for (int i = 0; i < n; i++) {
            char[] row = grid[i].toCharArray();
            for (int j = 0; j < n; j++) {
                int base = 4 * (i * n + j);
                if (row[j] != '/') { // connect back-diagonal zone
                    union(id, base, base + 1);
                    union(id, base + 2, base + 3);
                }
                if (row[j] != '\\') { // connect diagonal zone
                    union(id, base, base + 3);
                    union(id, base + 1, base + 2);
                }
                if (j > 0) {
                    union(id, base, base - 2); // connect to west
                }
                if (i > 0) {
                    union(id, base + 3, base - 4 * n + 1); // connect to north
                }
            }
        }
        return count(id);
    }

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

    private int count(int[] id) {
        int res = 0;
        for (int i : id) {
            res += (i < 0) ? 1 : 0;
        }
        return res;
    }

    // DFS + Recursion
    // time complexity: O(N^2), space complexity: O(N*N)
    // 6 ms(70.07%), 38.4 MB(54.65%) for 137 tests
    public int regionsBySlashes2(String[] grid) {
        int n = grid.length;
        int[][] graph = new int[n * 3][n * 3]; // divide each cell by 3*3-grid
        for (int i = 0; i < n; i++) {
            char[] row = grid[i].toCharArray();
            for (int j = 0; j < n; j++) {
                if (row[j] == '/') {
                    graph[i * 3][j * 3 + 2] =
                    graph[i * 3 + 1][j * 3 + 1] = graph[i * 3 + 2][j * 3] = 1;
                }
                if (row[j] == '\\') {
                    graph[i * 3][j * 3] =
                    graph[i * 3 + 1][j * 3 + 1] = graph[i * 3 + 2][j * 3 + 2] = 1;
                }
            }
        }
        int res = 0;
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] == 0) {
                    dfs(graph, i, j);
                    res++;
                }
            }
        }
        return res;
    }

    private void dfs(int[][] graph, int i, int j) {
        if (i >= 0 && j >= 0 && i < graph.length && j < graph.length && graph[i][j] == 0) {
            graph[i][j] = 1;
            dfs(graph, i - 1, j);
            dfs(graph, i + 1, j);
            dfs(graph, i, j - 1);
            dfs(graph, i, j + 1);
        }
    }

    // Union Find
    // time complexity: O(N^2*a(N)), space complexity: O(N*N)
    // 6 ms(56.73%), 38.9 MB(16.15%) for 137 tests
    public int regionsBySlashes3(String[] grid) {
        int n = grid.length;
        int[] id = new int[n * n * 2]; // divide each cell by two: a&b, a/b or b\a
        Arrays.fill(id, -1);
        for (int i = 0; i < n; i++) {
            char[] row = grid[i].toCharArray();
            for (int j = 0; j < n; j++) {
                int a = (i * n + j) * 2;
                int b = a + 1;
                if (row[j] == ' ') {
                    union(id, a, b);
                }
                if (j < n - 1) {
                    int x = row[j] == '/' ? b : a;
                    int y = (grid[i].charAt(j + 1) == '/') ? b + 1 : b + 2;
                    union(id, x, y);
                }
                if (i < n - 1) {
                    union(id, b, a + 2 * n);
                }
            }
        }
        return count(id);
    }

    // Union Find + Math (Euler's Formula)
    // time complexity: O(N^2*a(N)), space complexity: O(N*N)
    // 4 ms(87.69%), 38.8 MB(20.77%) for 137 tests
    public int regionsBySlashes4(String[] grid) {
        int n = grid.length;
        int[] id = new int[(n + 1) * (n + 1)];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                id[i * (n + 1) + j] = (i == 0 || i == n || j == 0 || j == n) ? 0 : i * (n + 1) + j;
            }
        }
        int res = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index1 = -1;
                int index2 = -1;
                switch (grid[i].charAt(j)) {
                case ' ':
                    continue;
                case '\\':
                    index1 = i * (n + 1) + j;
                    index2 = (i + 1) * (n + 1) + j + 1;
                    break;
                case '/':
                    index1 = i * (n + 1) + j + 1;
                    index2 = (i + 1) * (n + 1) + j;
                }
                res += connect(id, index1, index2) ? 0 : 1;
            }
        }
        return res; // F(=E-V+2) - 1 + #connectedComponents - 1
    }

    private boolean connect(int[] id, int x, int y) {
        int px = find(id, x);
        int py = find(id, y);
        if (px == py) { return false; }

        id[px] = py;
        return true;
    }

    private int find(int[] id, int x) {
        int p = x;
        for (; p != id[p]; p = id[p] = id[id[p]]) {}
        return p;
    }

    private void test(String[] grid, int expected) {
        assertEquals(expected, regionsBySlashes(grid));
        assertEquals(expected, regionsBySlashes2(grid));
        assertEquals(expected, regionsBySlashes3(grid));
        assertEquals(expected, regionsBySlashes4(grid));
    }

    @Test public void test() {
        test(new String[] {"//", "/ "}, 3);
        test(new String[] {" /", "/ "}, 2);
        test(new String[] {" /", "  "}, 1);
        test(new String[] {"\\/", "/\\"}, 4);
        test(new String[] {"/\\", "\\/"}, 5);
        test(new String[] {"/\\", "\\/"}, 5);
        test(new String[] {"/\\/", "\\/\\", "//\\"}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
