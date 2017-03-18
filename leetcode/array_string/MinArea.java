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

    void test(String[] s, int x, int y, int expected) {
        char[][] image = Stream.of(s).map(a -> a.toCharArray()).toArray(char[][]::new);
        assertEquals(expected, minArea(image, x, y));
        assertEquals(expected, minArea2(image, x, y));
        assertEquals(expected, minArea3(image, x, y));
    }

    @Test
    public void test() {
        test(new String[] {"0010", "0110", "0100"}, 0, 2, 6);
        test(new String[] {"01"}, 0, 1, 1);
        test(new String[] {"1111111101", "1000000101", "1011110101", "1010010101",
                           "1010110101", "1010000101", "1011111101", "1000000001", "1111111111"}, 4, 4, 90);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinArea");
    }
}
