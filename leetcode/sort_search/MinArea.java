import java.util.*;
import java.util.stream.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC302: https://leetcode.com/problems/smallest-rectangle-enclosing-black-pixels
//
// An image is represented by a binary matrix with 0 as a white pixel and 1 as a black pixel.
// The black pixels are connected, i.e., there is only one black region. Pixels are
// connected horizontally and vertically. Given the location of one of the black pixels,
// return the area of the smallest (axis-aligned) rectangle that encloses all black pixels.
public class MinArea {
    private static final int[][] MOVES = new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // BFS + Queue
    // time complexity: O(B), space complexity: O(M * N)
    // beats 7.45%(24 ms for 111 tests)
    public int minArea(char[][] image, int x, int y) {
        int m = image.length;
        int n = image[0].length;
        int maxX = x;
        int minX = x;
        int maxY = y;
        int minY = y;
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[m][n];
        for (queue.offer(new int[] {x, y}); !queue.isEmpty(); ) {
            int[] black = queue.poll();
            int r = black[0];
            int c = black[1];
            for (int[] move : MOVES) {
                int nx = r + move[0];
                int ny = c + move[1];
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && image[nx][ny] == '1' && !visited[nx][ny]) {
                    queue.offer(new int[] {nx, ny});
                    visited[nx][ny] = true;
                    maxX = Math.max(maxX, nx);
                    minX = Math.min(minX, nx);
                    maxY = Math.max(maxY, ny);
                    minY = Math.min(minY, ny);
                }
            }
        }
        return (maxX - minX + 1) * (maxY - minY + 1);
    }

    // Binary Search
    // time complexity: O(M * log(N) + N * log(M)), space complexity: O(1)
    // beats 63.21%(1 ms for 111 tests)
    public int minArea2(char[][] image, int x, int y) {
        int leftMost = search(image, 0, y, false, true);
        int rightMost = search(image, y + 1, image[0].length, false, false);
        int topMost = search(image, 0, x, true, true);
        int bottomMost = search(image, x + 1, image.length, true, false);
        return (rightMost - leftMost) * (bottomMost - topMost);
    }

    private int search(char[][] image, int min, int max, boolean isRow, boolean isMin) {
        int low = min;
        for (int high = max; low < high; ) {
            int mid = (low + high) >>> 1;
            if (whiteLine(image, mid, isRow) == isMin) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private boolean whiteLine(char[][] image, int index, boolean isRow) {
        if (isRow) {
            for (int i = image[0].length - 1; i >= 0; i--) {
                if (image[index][i] == '1') return false;
            }
        } else {
            for (int i = image.length - 1; i >= 0; i--) {
                if (image[i][index] == '1') return false;
            }
        }
        return true;
    }

    // Binary Search
    // time complexity: O(M * log(N) + N * log(M)), space complexity: O(1)
    // beats 63.21%(1 ms for 111 tests)
    public int minArea3(char[][] image, int x, int y) {
        int m = image.length;
        int n = image[0].length;
        int top = search(image, 0, x, 0, n, true, true);
        int bottom = search(image, x + 1, m, 0, n, true, false);
        int left = search(image, 0, y, top, bottom, false, true);
        int right = search(image, y + 1, n, top, bottom, false, false);
        return (right - left) * (bottom - top);
    }

    private int search(char[][] image, int low, int high, int min, int max, boolean isRow, boolean isMin) {
        while (low < high) {
            int mid = (low + high) >>> 1;
            int i = min;
            for (; i < max &&  (isRow ? image[mid][i] : image[i][mid]) == '0'; i++) {}
            if ((i < max) == isMin) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    // DFS + Recursion
    // time complexity: O(B), space complexity: O(M * N)
    // beats 18.28%(8 ms for 111 tests)
    public int minArea4(char[][] image, int x, int y) {
        int[] boundaries = new int[] {x, 0, y, 0};
        dfs(image, x, y, boundaries, new boolean[image.length][image[0].length]);
        return (boundaries[1] - boundaries[0] + 1) * (boundaries[3] - boundaries[2] + 1);
    }

    private void dfs(char[][] image, int x, int y, int[] boundaries, boolean[][] visited) {
        if (x < 0 || y < 0 || x >= image.length || y >= image[0].length
            || image[x][y] == '0' || visited[x][y]) return;

        visited[x][y] = true;
        boundaries[0] = Math.min(boundaries[0], x);
        boundaries[1] = Math.max(boundaries[1], x);
        boundaries[2] = Math.min(boundaries[2], y);
        boundaries[3] = Math.max(boundaries[3], y);
        dfs(image, x + 1, y, boundaries, visited);
        dfs(image, x - 1, y, boundaries, visited);
        dfs(image, x, y - 1, boundaries, visited);
        dfs(image, x, y + 1, boundaries, visited);
    }

    // Linear Search
    // time complexity: O(M * N), space complexity: O(1)
    // beats 18.28%(8 ms for 111 tests)
    public int minArea5(char[][] image, int x, int y) {
        int top = x;
        int bottom = x;
        int left = y;
        int right = y;
        for (int i = 0; i < image.length; ++i) {
            for (int j = 0; j < image[0].length; j++) {
                if (image[i][j] == '1') {
                    top = Math.min(top, i);
                    bottom = Math.max(bottom, i + 1);
                    left = Math.min(left, j);
                    right = Math.max(right, j + 1);
                }
            }
        }
        return (right - left) * (bottom - top);
    }

    void test(String[] s, int x, int y, int expected) {
        char[][] image = Stream.of(s).map(a -> a.toCharArray()).toArray(char[][]::new);
        assertEquals(expected, minArea(image, x, y));
        assertEquals(expected, minArea2(image, x, y));
        assertEquals(expected, minArea3(image, x, y));
        assertEquals(expected, minArea4(image, x, y));
        assertEquals(expected, minArea5(image, x, y));
    }

    @Test
    public void test() {
        test(new String[] {"01"}, 0, 1, 1);
        test(new String[] {"0010", "0110", "0100"}, 0, 2, 6);
        test(new String[] {"1111111101", "1000000101", "1011110101", "1010010101",
                           "1010110101", "1010000101", "1011111101", "1000000001", "1111111111"}, 4, 4, 90);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinArea");
    }
}
