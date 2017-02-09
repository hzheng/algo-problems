import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC391: https://leetcode.com/problems/perfect-rectangle/
//
// Given N axis-aligned rectangles where N > 0, determine if they all together
// form an exact cover of a rectangular region.
// Each rectangle is represented as a bottom-left point and a top-right point.
public class PerfectRectangle {
    // Heap + Sort
    // beats 99.00%(55 ms for 46 tests)
    public boolean isRectangleCover(int[][] rectangles) {
        int n = rectangles.length;
        if (n < 2) return true;

        Arrays.sort(rectangles, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] != b[0]) ? a[0] - b[0] : a[1] - b[1];
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
        for (int i = 0;; ) {
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
            return (x != other.x) ? x - other.x : y1 - other.y1;
        }
    }

    private static final Comparator<int[]> CMP = new Comparator<int[]>() {
        public int compare(int[] a, int[] b) {
            return (a[0] != b[0]) ? a[0] - b[0] : a[1] - b[1];
        }
    };

    // Heap + Sort
    // beats 99.17%(51 ms for 46 tests)
    public boolean isRectangleCover2(int[][] rectangles) {
        int n = rectangles.length;
        if (n < 2) return true;

        Arrays.sort(rectangles, CMP);
        PriorityQueue<int[]> segements = new PriorityQueue<>(CMP);
        int[] firstRect = rectangles[0];
        int maxY = firstRect[1];
        for (int i = 0; i < n && rectangles[i][0] == firstRect[0]; i++) {
            if (rectangles[i][1] != maxY) return false;

            maxY = rectangles[i][3];
        }
        // first virtual segement
        segements.offer(new int[] {firstRect[0], firstRect[1], maxY});
        for (int i = 0;; ) {
            int[] cur = segements.poll();
            // merge all ajacent aligned edges
            for (int[] next; !segements.isEmpty(); cur[2] = next[2]) {
                next = segements.peek();
                if (next[0] != cur[0] || next[1] > cur[2]) break;

                // if (next[1] < cur[2]) return false; // never happen
                segements.poll();
            }
            if (i == n) return segements.isEmpty() && cur[2] == maxY;

            int filledY = cur[1];
            for (int[] rect; i < n; i++, filledY = rect[3]) {
                rect = rectangles[i];
                if (rect[0] != cur[0] || rect[1] > filledY) break;

                if (rect[1] < filledY) return false;
                // maintaining left edges
                segements.offer(new int[] {rect[2], rect[1], rect[3]});
            }
            if (filledY != cur[2]) return false;
        }
    }

    // Hash Table
    // https://discuss.leetcode.com/topic/56052/really-easy-understanding-solution-o-n-java/
    // beats 79.97%(105 ms for 46 tests)
    public boolean isRectangleCover3(int[][] rectangles) {
        if (rectangles.length == 0 || rectangles[0].length == 0) return false;

        int x1 = Integer.MAX_VALUE;
        int x2 = Integer.MIN_VALUE;
        int y1 = Integer.MAX_VALUE;
        int y2 = Integer.MIN_VALUE;
        Set<String> set = new HashSet<>();
        int area = 0;
        for (int[] rect : rectangles) {
            x1 = Math.min(rect[0], x1);
            y1 = Math.min(rect[1], y1);
            x2 = Math.max(rect[2], x2);
            y2 = Math.max(rect[3], y2);
            area += (rect[2] - rect[0]) * (rect[3] - rect[1]);
            String s1 = rect[0] + "," + rect[1];
            String s2 = rect[0] + "," + rect[3];
            String s3 = rect[2] + "," + rect[3];
            String s4 = rect[2] + "," + rect[1];
            if (!set.add(s1)) { set.remove(s1); }
            if (!set.add(s2)) { set.remove(s2); }
            if (!set.add(s3)) { set.remove(s3); }
            if (!set.add(s4)) { set.remove(s4); }
        }
        return area == (x2 - x1) * (y2 - y1) && set.size() == 4
               && set.contains(x1 + "," + y1) && set.contains(x1 + "," + y2)
               && set.contains(x2 + "," + y1) && set.contains(x2 + "," + y2);
    }

    // Hash Table
    // https://discuss.leetcode.com/topic/55923/o-n-solution-by-counting-corners-with-detailed-explaination/
    // beats 77.63%(107 ms for 46 tests)
    public boolean isRectangleCover4(int[][] rectangles) {
        Map<Point, Integer> map = new HashMap<>();
        int left = Integer.MAX_VALUE;
        int down = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        int up = Integer.MIN_VALUE;
        for (int[] rect : rectangles) {
            left = Math.min(left, rect[0]);
            down = Math.min(down, rect[1]);
            right = Math.max(right, rect[2]);
            up = Math.max(up, rect[3]);
            if (!insertCorner(map, rect[0], rect[1], 1)) return false;
            if (!insertCorner(map, rect[2], rect[1], 2)) return false;
            if (!insertCorner(map, rect[2], rect[3], 4)) return false;
            if (!insertCorner(map, rect[0], rect[3], 8)) return false;
        }
        for (Map.Entry<Point, Integer> entry : map.entrySet()) {
            int x = entry.getKey().x;
            int y = entry.getKey().y;
            int v = entry.getValue();
            if ((x == left || x == right) && (y == up || y == down)) {
                if (v != 1 && v != 2 && v != 4 && v != 8) return false;
            } else if (v != 3 && v != 6 && v != 9 && v != 12 && v != 15) return false;
        }
        return true;
    }

    private boolean insertCorner(Map<Point, Integer> cornerCount,
                                 int x, int y, int pos) {
        // long key = (((long)x) << 32) | (y & 0xffffffffL); // Long is slow
        Point key = new Point(x, y);
        int count = cornerCount.getOrDefault(key, 0);
        if ((count & pos) != 0) return false;
        cornerCount.put(key, count | pos);
        return true;
    }

    static class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Point)) return false;

            Point pt = (Point)o;
            return x == pt.x && y == pt.y;
        }

        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    void test(int[][] rectangles, boolean expected) {
        assertEquals(expected, isRectangleCover(rectangles));
        assertEquals(expected, isRectangleCover2(rectangles));
        assertEquals(expected, isRectangleCover3(rectangles));
        assertEquals(expected, isRectangleCover4(rectangles));
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
