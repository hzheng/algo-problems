import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC391: https://leetcode.com/problems/perfect-rectangle/
//
// Given N axis-aligned rectangles where N > 0, determine if they all together
// form an exact cover of a rectangular region.
// Each rectangle is represented as a bottom-left point and a top-right point.
public class PerfectRectangle {
    // beats N/A(50 ms)
    public boolean isRectangleCover(int[][] rectangles) {
        int n = rectangles.length;
        if (n < 2) return true;

        Arrays.sort(rectangles, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] == b[0]) ? a[1] - b[1] : a[0] - b[0];
            }
        });

        PriorityQueue<Segement> segements = new PriorityQueue<>();
        int[] firstRect = rectangles[0];
        int maxY = firstRect[1];
        for (int i = 0; i < n && rectangles[i][0] == firstRect[0]; i++) {
            if (rectangles[i][1] != maxY) return false;

            maxY = rectangles[i][3];
        }
        // first virtual segement
        segements.offer(new Segement(firstRect[0], firstRect[1], maxY));
        for (int i = 0; ; ) {
            Segement cur = segements.poll();
            // merge all ajacent segements
            while (!segements.isEmpty()) {
                Segement next = segements.peek();
                if (next.x != cur.x || next.y1 > cur.y2) break;

                if (next.y1 < cur.y2) return false;

                segements.poll();
                cur.y2 = next.y2;
            }
            if (i == n) return segements.isEmpty() && cur.y2 == maxY;

            int filledY = cur.y1;
            for (; i < n; i++) {
                int[] rect = rectangles[i];
                if (rect[0] != cur.x || rect[1] > filledY) break;
                // if (rect[1] < filledY) return false;

                filledY = rect[3];
                segements.offer(new Segement(rect[2], rect[1], rect[3]));
            }
            if (filledY != cur.y2) return false;
        }
    }

    private static class Segement implements Comparable<Segement> {
        int x;
        int y1;
        int y2;

        public Segement(int x, int y1, int y2) {
            this.x = x;
            this.y1 = y1;
            this.y2 = y2;
        }

        public int compareTo(Segement other) {
            return (x == other.x) ? y1 - other.y1 : x - other.x;
        }
    }

    void test(int[][] rectangles, boolean expected) {
        assertEquals(expected, isRectangleCover(rectangles));
    }

    @Test
    public void test1() {
        test(new int[][] {{1, 2, 4, 4}, {1, 0, 4, 1}, {0, 2, 1, 3}, {0, 1, 3, 2},
                          {3, 1, 4, 2}, {0, 3, 1, 4}, {0, 0, 1, 1}}, true);
        test(new int[][] {{1, 1, 3, 3}, {3, 1, 4, 2}, {3, 2, 4, 4},
                          {1, 3, 2, 4}, {2, 3, 3, 4}}, true);
        test(new int[][] {{1, 1, 2, 3}, {1, 3, 2, 4}, {3, 1, 4, 2},
                          {3, 2, 4, 4}}, false);
        test(new int[][] {{1, 1, 3, 3}, {3, 1, 4, 2}, {1, 3, 2, 4},
                          {3, 2, 4, 4}}, false);
        test(new int[][] {{1, 1, 3, 3}, {3, 1, 4, 2}, {1, 3, 2, 4},
                          {2, 2, 4, 4}}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PerfectRectangle");
    }
}
