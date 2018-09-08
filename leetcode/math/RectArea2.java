import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC850: https://leetcode.com/problems/rectangle-area-ii/
//
// We are given a list of (axis-aligned) rectangles.  Each rectangle are the
// coordinates of the bottom-left corner, and top-right corner.
// Find the total area covered by all rectangles in the plane.  Since the answer
// may be too large, return it modulo 10^9 + 7.
public class RectArea2 {
    static final int MOD = 1_000_000_007;

    // Math + Bit Manipulation
    // time complexity: O(N * (2 ^ N)), space complexity: O(N)
    // Time Limit Exceeded
    public int rectangleArea(int[][] rectangles) {
        int n = rectangles.length;
        long res = 0;
        int max = 1_000_000_000;
        for (int subset = 1; subset < (1 << n); subset++) {
            int[] rec = new int[] { 0, 0, max, max };
            int sign = -1;
            for (int bit = 0; bit < n; bit++) {
                if (((subset >> bit) & 1) != 0) {
                    rec = intersect(rec, rectangles[bit]);
                    sign *= -1;
                }
            }
            res += sign * area(rec);
        }
        return (int) Math.floorMod(res, MOD);
    }

    public long area(int[] rec) {
        return Math.max(0L, rec[2] - rec[0]) * Math.max(0L, rec[3] - rec[1]);
    }

    private int[] intersect(int[] rec1, int[] rec2) {
        return new int[] { Math.max(rec1[0], rec2[0]), Math.max(rec1[1], rec2[1]), Math.min(rec1[2], rec2[2]),
                Math.min(rec1[3], rec2[3]) };
    }

    // Coordinate Compression + Hash Table + SortedSet
    // time complexity: O(N ^ 3)), space complexity: O(N ^ 2)
    // beats 43.00%(59 ms for 76 tests)
    public int rectangleArea2(int[][] rectangles) {
        Set<Integer> xSet = new TreeSet<>();
        Set<Integer> ySet = new TreeSet<>();
        for (int[] rec : rectangles) {
            xSet.add(rec[0]);
            xSet.add(rec[2]);
            ySet.add(rec[1]);
            ySet.add(rec[3]);
        }
        Integer[] xArr = xSet.toArray(new Integer[0]);
        Integer[] yArr = ySet.toArray(new Integer[0]);
        Map<Integer, Integer> xMap = new HashMap<>();
        Map<Integer, Integer> yMap = new HashMap<>();
        for (int i = 0; i < xArr.length; i++) {
            xMap.put(xArr[i], i);
        }
        for (int i = 0; i < yArr.length; i++) {
            yMap.put(yArr[i], i);
        }
        boolean[][] grid = new boolean[xArr.length][yArr.length];
        for (int[] rec : rectangles) {
            for (int x = xMap.get(rec[0]); x < xMap.get(rec[2]); x++) {
                for (int y = yMap.get(rec[1]); y < yMap.get(rec[3]); y++) {
                    grid[x][y] = true;
                }
            }
        }
        long area = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (!grid[i][j]) continue;

                area += (long)(xArr[i + 1] - xArr[i]) * (yArr[j + 1] - yArr[j]);
            }
        }
        return (int)(area % MOD);
    }

    // Line Sweep + Sort
    // time complexity: O(N ^ 2 * log(N))), space complexity: O(N)
    // beats 17.59%(70 ms for 76 tests)
    public int rectangleArea3(int[][] rectangles) {
        int[][] scans = new int[rectangles.length * 2][];
        int i = 0;
        for (int[] rec : rectangles) {
            scans[i++] = new int[]{0, rec[1], rec[0], rec[2]};
            scans[i++] = new int[]{1, rec[3], rec[0], rec[2]};
        }
        Arrays.sort(scans, (a, b) -> a[1] - b[1]);
        List<int[]> active = new ArrayList<>();
        int lastY = scans[0][1];
        long area = 0;
        for (int[] scan : scans) {
            int y = scan[1];
            int left = scan[2];
            int right = scan[3];
            long totalLen = 0;
            int cur = 0;
            for (int[] a : active) {
                cur = Math.max(cur, a[0]);
                totalLen += Math.max(a[1] - cur, 0);
                cur = Math.max(cur, a[1]);
            }
            area += totalLen * (y - lastY);
            if (scan[0] == 0) { // come
                active.add(new int[]{left, right});
                Collections.sort(active, (a, b) -> a[0] - b[0]);
            } else { // leave
                for (i = 0; i < active.size(); i++) {
                    if (active.get(i)[0] == left && active.get(i)[1] == right) {
                        active.remove(i);
                        break;
                    }
                }
            }
            lastY = y;
        }
        return (int)(area % MOD);
    }

    // Line Sweep + Segment Tree + Hash Table + Set + Sort
    // time complexity: O(N * log(N))), space complexity: O(N)
    // beats 17.59%(70 ms for 76 tests)
    public int rectangleArea4(int[][] rectangles) {
        int[][] scans = new int[rectangles.length * 2][];
        Set<Integer> xSet = new HashSet<>();
        int i = 0;
        for (int[] rec : rectangles) {
            scans[i++] = new int[]{1, rec[1], rec[0], rec[2]};
            scans[i++] = new int[]{-1, rec[3], rec[0], rec[2]};
            xSet.add(rec[0]);
            xSet.add(rec[2]);
        }
        Arrays.sort(scans, (a, b) -> a[1] - b[1]);
        Integer[] xArr = xSet.toArray(new Integer[0]);
        Arrays.sort(xArr);
        Map<Integer, Integer> xMap = new HashMap<>();
        for (i = 0; i < xArr.length; i++) {
            xMap.put(xArr[i], i);
        }
        Node active = new Node(0, xArr.length - 1, xArr);
        long area = 0;
        long totalLen = 0;
        int lastY = scans[0][1];
        for (int[] scan : scans) {
            int y = scan[1];
            int x1 = scan[2];
            int x2 = scan[3];
            area += totalLen * (y - lastY);
            totalLen = active.update(xMap.get(x1), xMap.get(x2), scan[0]);
            lastY = y;
        }
        return (int)(area % MOD);
    }

    private static class Node {
        int start, end;
        Integer[] xArr;
        Node left, right;
        int count;
        long total;

        public Node(int start, int end, Integer[] xArr) {
            this.start = start;
            this.end = end;
            this.xArr = xArr;
        }

        private int middle() {
            return (start + end) >>> 1;
        }

        private Node getLeft() {
            if (left == null) {
                left = new Node(start, middle(), xArr);
            }
            return left;
        }

        private Node getRight() {
            if (right == null) {
                right = new Node(middle(), end, xArr);
            }
            return right;
        }

        public long update(int start, int end, int val) {
            if (start >= end) return 0;

            if (this.start == start && this.end == end) {
                count += val;
            } else {
                getLeft().update(start, Math.min(middle(), end), val);
                getRight().update(Math.max(middle(), start), end, val);
            }
            if (count > 0) {
                return total = xArr[this.end] - xArr[this.start];
            } else {
                return total = getLeft().total + getRight().total;
            }
        }
    }

    // TODO: Binary Indexed Tree

    // Recursion
    // time complexity: O(N ^ 2)), space complexity: O(N)
    // beats 95.11%(8 ms for 76 tests)
    public int rectangleArea5(int[][] rectangles) {
        long res = 0;
        List<int[]> recList = new ArrayList<>();
        for (int[] rec : rectangles) {
            addRect(recList, rec, 0);
        }
        for (int[] rec : recList) {
            res = (res + ((long)(rec[2] - rec[0]) * (rec[3] - rec[1]))) % MOD;
        }
        return (int) res % MOD;
    }

    public void addRect(List<int[]> rects, int[] rect, int index) {
        if (index >= rects.size()) {
            rects.add(rect);
            return;
        }
        int[] r = rects.get(index++);
        if (rect[2] <= r[0] || rect[3] <= r[1] || rect[0] >= r[2] || rect[1] >= r[3]) {
            addRect(rects, rect, index);
            return;
        }
        if (rect[0] < r[0]) {
            addRect(rects, new int[]{rect[0], rect[1], r[0], rect[3]}, index);
        }
        if (rect[2] > r[2]) {
            addRect(rects, new int[]{r[2], rect[1], rect[2], rect[3]}, index);
        }
        if (rect[1] < r[1]) {
            addRect(rects, new int[]{Math.max(r[0], rect[0]), rect[1],
                                     Math.min(r[2], rect[2]), r[1]}, index);
        }
        if (rect[3] > r[3]) {
            addRect(rects, new int[]{Math.max(r[0], rect[0]), r[3],
                                     Math.min(r[2], rect[2]), rect[3]}, index);
        }
    }

    void test(int[][] rectangles, int expected) {
        if (rectangles.length < 12) {
            assertEquals(expected, rectangleArea(rectangles));
        }
        assertEquals(expected, rectangleArea2(rectangles));
        assertEquals(expected, rectangleArea3(rectangles));
        assertEquals(expected, rectangleArea4(rectangles));
        assertEquals(expected, rectangleArea5(rectangles));
    }

    @Test
    public void test1() {
        test(new int[][] { { 0, 0, 2, 2 }, { 1, 0, 2, 3 }, { 1, 0, 3, 1 } }, 6);
        test(new int[][] { { 224386961, 128668997, 546647847, 318900555 },
                { 852286866, 238086790, 992627088, 949888275 }, { 160239672, 137108804, 398130330, 944807066 },
                { 431047948, 462092719, 870611028, 856851714 }, { 736895365, 511285772, 906155231, 721626624 },
                { 289309389, 607009433, 558359552, 883664714 }, { 780746435, 397872372, 931219192, 863727103 },
                { 573523994, 124874359, 889018012, 471879750 }, { 619886375, 149607927, 727026507, 446976526 },
                { 51739879, 716225241, 115331335, 785850603 }, { 171077223, 267051983, 548436248, 349498903 },
                { 314437215, 169054168, 950814572, 481179241 }, { 64126215, 646689712, 595562376, 829164135 },
                { 926011655, 481539702, 982179297, 832455610 }, { 40370235, 231510218, 770233582, 851797196 },
                { 292546319, 45032676, 413358795, 783606009 }, { 424366277, 369838051, 453541063, 777456024 },
                { 211837048, 142665527, 217366958, 952362711 }, { 228416869, 402115549, 672143142, 644930626 },
                { 755018294, 194555696, 846854520, 939022548 }, { 192890972, 586071668, 992336688, 759060552 },
                { 127869582, 392855032, 338983665, 954245205 }, { 665603955, 208757599, 767586006, 276627875 },
                { 260384651, 10960359, 736299693, 761411808 }, { 46440611, 559601039, 911666265, 904518674 },
                { 54013763, 90331595, 332153447, 106222561 }, { 73093292, 378586103, 423488105, 826750366 },
                { 327100855, 516514806, 676134763, 653520887 }, { 930781786, 407609872, 960671631, 510621750 },
                { 35479655, 449171431, 931212840, 617916927 } }, 862275791);

    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
