import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC469: https://leetcode.com/problems/convex-polygon/
//
// Given a list of points that form a polygon when joined sequentially, find if
// this polygon is convex.
// Note:
// There are at least 3 and at most 10,000 points.
// Coordinates are in the range -10,000 to 10,000.
// You may assume the polygon formed by given points is always a simple polygon
public class ConvexPolygon {
    // beats N/A(29 ms for 53 tests)
    public boolean isConvex(List<List<Integer> > points) {
        int sign = 0;
        int n = points.size();
        for (int i = 0; i < n; i++) {
            List<Integer> p1 = points.get(i);
            List<Integer> p2 = points.get((i + 1) % n);
            List<Integer> p3 = points.get((i + 2) % n);
            int dx1 = p2.get(0) - p1.get(0);
            int dy1 = p2.get(1) - p1.get(1);
            int dx2 = p3.get(0) - p2.get(0);
            int dy2 = p3.get(1) - p2.get(1);
            int crossProduct = dx1 * dy2 - dy1 * dx2;
            if (crossProduct == 0) continue;

            if (sign == 0) {
                sign = crossProduct > 0 ? 1 : -1; // avoid overflow
            } else if (sign * crossProduct < 0) return false;
        }
        return true;
    }

    void test(int[][] points, boolean expected) {
        List<List<Integer> > pts = new ArrayList<>();
        for (int[] p : points) {
            pts.add(Arrays.asList(p[0], p[1]));
        }
        assertEquals(expected, isConvex(pts));
    }

    @Test
    public void test() {
        test(new int[][] {{0,0},{1,0},{1,1},{-1,1},{-1,0}}, true);
        test(new int[][] {{0,0},{1,1},{0,2},{-1,2},{0,1}}, false);
        test(new int[][] {{0,0},{0,1},{1,1},{1,0}}, true);
        test(new int[][] {{0,0},{0,10},{5,5},{10,10},{10,0}}, false);
        test(new int[][] {{0, 1372}, {6, 11}, {447, 2}, {5173, 0}, {6939, 0},
                          {9331, 4}, {9846, 67}, {9904, 85}, {9995, 886},
                          {9999, 3485}, {9995, 8709}, {9981, 9186}, {9960, 9896},
                          {9784, 9995}, {5925, 9998}, {3889, 9999}, {3080, 9999},
                          {1827, 9995}, {195, 9984}, {68, 9962}, {52, 9905},
                          {22, 9646}, {16, 9495}, {7, 9124}, {1, 4093}}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConvexPolygon");
    }
}
