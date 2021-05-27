import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1591: https://leetcode.com/problems/strange-printer-ii/
//
// There is a strange printer with the following two special requirements:
// On each turn, the printer will print a solid rectangular pattern of a single color on the grid.
// This will cover up the existing colors in the rectangle.
// Once the printer has used a color for the above operation, the same color cannot be used again.
// You are given a m x n matrix targetGrid, where targetGrid[row][col] is the color in the position
// (row, col) of the grid.
// Return true if it is possible to print the matrix targetGrid, otherwise, return false.
//
// Constraints:
// m == targetGrid.length
// n == targetGrid[i].length
// 1 <= m, n <= 60
// 1 <= targetGrid[row][col] <= 60
public class StrangePrinterII {
    // Hash Table
    // time complexity: O(M^2*N^2), space complexity: O(C)
    // 7 ms(99.03%), 38.8 MB(95.15%) for 152 tests
    public boolean isPrintable(int[][] targetGrid) {
        Map<Integer, int[]> colorPos = new HashMap<>();
        int m = targetGrid.length;
        int n = targetGrid[0].length;
        for (int r = 0; r < m; r++) {
            int[] row = targetGrid[r];
            for (int c = 0; c < n; c++) {
                int[] rect = colorPos.computeIfAbsent(row[c], x -> new int[] {m, 0, n, 0});
                rect[0] = Math.min(rect[0], r);
                rect[1] = Math.max(rect[1], r);
                rect[2] = Math.min(rect[2], c);
                rect[3] = Math.max(rect[3], c);
            }
        }
        for (boolean hasRect = true; hasRect && !colorPos.isEmpty(); ) {
            var itr = colorPos.keySet().iterator();
            for (hasRect = false; itr.hasNext(); ) {
                int color = itr.next();
                if (uncolor(targetGrid, color, colorPos)) {
                    itr.remove();
                    hasRect = true;
                }
            }
        }
        return colorPos.isEmpty();
    }

    private boolean uncolor(int[][] targetGrid, int color, Map<Integer, int[]> colorPos) {
        int[] pos = colorPos.get(color);
        for (int r = pos[0]; r <= pos[1]; r++) {
            for (int c = pos[2]; c <= pos[3]; c++) {
                if (targetGrid[r][c] != 0 && targetGrid[r][c] != color) { return false; }
            }
        }
        for (int r = pos[0]; r <= pos[1]; r++) {
            for (int c = pos[2]; c <= pos[3]; c++) {
                if (targetGrid[r][c] == color) {
                    targetGrid[r][c] = 0;
                }
            }
        }
        return true;
    }

    // Topological Sort + Queue + Set
    // time complexity: O(M*N*C), space complexity: O(C)
    // 45 ms(36.89%), 39.7 MB(14.56%) for 152 tests
    public boolean isPrintable2(int[][] targetGrid) {
        final int COLORS = 60;
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[COLORS + 1];
        for (int i = 0; i <= COLORS; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 1; i <= COLORS; i++) {
            createDependency(targetGrid, i, graph, inDegree);
        }
        Queue<Integer> outermost = new LinkedList<>();
        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                outermost.offer(i);
            }
        }
        Set<Integer> visited = new HashSet<>();
        while (!outermost.isEmpty()) {
            int cur = outermost.poll();
            if (!visited.add(cur)) { continue; }

            for (int nei : graph.get(cur)) {
                if (--inDegree[nei] == 0) {
                    outermost.offer(nei);
                }
            }
        }
        return visited.size() == inDegree.length;
    }

    private void createDependency(int[][] grid, int color, List<List<Integer>> graph,
                                  int[] inDegree) {
        int top = Integer.MAX_VALUE;
        int bottom = Integer.MIN_VALUE;
        int left = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == color) {
                    top = Math.min(top, i);
                    bottom = Math.max(bottom, i);
                    left = Math.min(left, j);
                    right = Math.max(right, j);
                }
            }
        }
        if (top == Integer.MAX_VALUE) { return; }

        for (int i = top; i <= bottom; i++) {
            for (int j = left; j <= right; j++) {
                if (grid[i][j] != color) {
                    graph.get(grid[i][j]).add(color);
                    inDegree[color]++;
                }
            }
        }
    }

    // DFS + Recursion + Set
    // time complexity: O(M*N*C), space complexity: O(C)
    // 42 ms(42.72%), 39.5 MB(42.72%) for 152 tests
    public boolean isPrintable3(int[][] targetGrid) {
        int m = targetGrid.length;
        int n = targetGrid[0].length;
        final int COLORS = 60;
        Set<Integer>[] graph = new Set[COLORS + 1];
        for (int c = 1; c <= COLORS; c++) {
            graph[c] = new HashSet<>();
        }
        for (int c = 1; c <= COLORS; c++) {
            int top = Integer.MAX_VALUE;
            int bottom = Integer.MIN_VALUE;
            int left = Integer.MAX_VALUE;
            int right = Integer.MIN_VALUE;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (targetGrid[i][j] == c) {
                        top = Math.min(top, i);
                        bottom = Math.max(bottom, i);
                        left = Math.min(left, j);
                        right = Math.max(right, j);
                    }
                }
            }
            for (int i = top; i <= bottom; i++) {
                for (int j = left; j <= right; j++) {
                    if (targetGrid[i][j] != c) {
                        graph[c].add(targetGrid[i][j]);
                    }
                }
            }
        }
        int[] visited = new int[COLORS + 1];
        for (int c = 1; c <= COLORS; c++) {
            if (visited[c] == 0 && hasCircle(c, graph, visited)) { return false; }
        }
        return true;
    }

    private boolean hasCircle(int cur, Set<Integer>[] graph, int[] visited) {
        visited[cur] = 1;
        for (int nei : graph[cur]) {
            if (visited[nei] == 2) { continue; }
            if (visited[nei] == 1 || hasCircle(nei, graph, visited)) { return true; }
        }
        visited[cur] = 2;
        return false;
    }

    private void test(int[][] targetGrid, boolean expected) {
        assertEquals(expected, isPrintable2(targetGrid));
        assertEquals(expected, isPrintable3(targetGrid));
        assertEquals(expected, isPrintable(targetGrid));
    }

    @Test public void test1() {
        test(new int[][] {{1, 1, 4, 4, 1, 1, 1}, {1, 1, 4, 4, 1, 1, 1}, {1, 1, 4, 4, 1, 1, 1},
                          {5, 1, 1, 1, 1, 1, 1}, {5, 3, 3, 3, 3, 3, 1}, {5, 3, 3, 3, 3, 3, 1},
                          {1, 3, 3, 3, 3, 3, 1}, {1, 3, 3, 3, 3, 3, 1}, {6, 3, 3, 3, 3, 3, 1},
                          {1, 1, 2, 1, 1, 1, 1}}, true);
        test(new int[][] {{1, 1, 1, 1}, {1, 2, 2, 1}, {1, 2, 2, 1}, {1, 1, 1, 1}}, true);
        test(new int[][] {{1, 1, 1, 1}, {1, 1, 3, 3}, {1, 1, 3, 4}, {5, 5, 1, 4}}, true);
        test(new int[][] {{1, 1, 1}, {3, 1, 3}}, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
