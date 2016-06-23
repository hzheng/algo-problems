import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/max-points-on-a-line/
//
// Given n points on a 2D plane, find the maximum number of points that lie on
// the same straight line.
public class MaxLinedPoints {
    static class Point {
        int x;
        int y;
        Point() {
            x = 0; y = 0;
        }
        Point(int a, int b) {
            x = a; y = b;
        }
    }

    // time complexity: O(N ^ 3), space complexity: O(1)
    // beats 98.87%
    public int maxPoints(Point[] points) {
        int n = points.length;
        if (n < 3) return n;

        int max = 0;
        for (int i = 0; i < n - 2; i++) {
            max = Math.max(max, maxPoints(points[i], points, i + 1, n));
        }
        return max;
    }

    private int maxPoints(Point a, Point[] points, int start, int end) {
        if (start + 1 == end) return 2;

        int max = 0;
        for (int j = start; j < end - 1; j++) {
            Point b = points[j];
            if (a.x == b.x && a.y == b.y) {
                max = Math.max(max, maxPoints(a, points, j + 1, end) + 1);
                continue;
            }

            int count = 2;
            for (int k = j + 1; k < end; k++) {
                if (collinear(a, b, points[k])) {
                    count++;
                }
            }
            max = Math.max(max, count);
        }
        return max;
    }

    private boolean collinear(Point a, Point b, Point c) {
        return (a.x - c.x) * (a.y - b.y) == (a.y - c.y) * (a.x - b.x);
    }

    // http://www.programcreek.com/2014/04/leetcode-max-points-on-a-line-java/
    // time complexity: O(N ^ 2), space complexity: O(N)
    // 88.40%
    public int maxPoints2(Point[] points) {
        int n = points.length;
        if (n < 3) return n;

        Map<Double, Integer> countMap = new HashMap<>();
        int max = 0;
        for (int i = 0; i < n; i++) {
            int duplicate = 1;
            int vertical = 0;
            Point a = points[i];
            for (int j = i + 1; j < n; j++) {
                Point b = points[j];
                if (a.x == b.x) {
                    if (a.y == b.y) {
                        duplicate++;
                    } else {
                        vertical++;
                    }
                } else {
                    // 0.0 and -0.0 are different keys in Map
                    double slope = (b.y == a.y) ? 0.0 : 1.0 * (b.y - a.y) / (b.x - a.x);
                    if (countMap.get(slope) != null) {
                        countMap.put(slope, countMap.get(slope) + 1);
                    } else {
                        countMap.put(slope, 1);
                    }
                }
            }

            for (int count : countMap.values()) {
                max = Math.max(max, count + duplicate);
            }
            max = Math.max(vertical + duplicate, max);
            countMap.clear();
        }
        return max;
    }

    void test(Function<Point[], Integer> maxPoints, int expected, int[][] pts) {
        Point[] points = new Point[pts.length];
        for (int i = 0; i < pts.length; i++) {
            points[i] = new Point(pts[i][0], pts[i][1]);
        }
        assertEquals(expected, (int)maxPoints.apply(points));
    }

    void test(int expected, int[][] pts) {
        MaxLinedPoints m = new MaxLinedPoints();
        test(m::maxPoints, expected, pts);
        test(m::maxPoints2, expected, pts);
    }

    @Test
    public void test1() {
        test(3, new int[][] {{2, 3}, {3, 3}, {-5, 3}});
        test(3, new int[][] {{1, 1}, {1, 1}, {1, 1}});
        test(4, new int[][] {{1, 1}, {1, 1}, {2, 2}, {2, 2}});
        test(3, new int[][] {{0, 0}, {1, 1}, {2, 2}, {1, 2}, {3, 4}, {5, 6}});
        test(4, new int[][] {{0, 0}, {1, 1}, {2, 2}, {3, 3}, {1, 2}, {3, 4}, {5, 6}});
        test(3, new int[][] {{0, 0}, {1, 1}, {0, 0}});
        test(12, new int[][] {{0, 9}, {138, 429}, {115, 359}, {115, 359},
                              {-30, -102}, {230, 709}, {-150, -686}, {-135, -613},
                              {-60, -248}, {-161, -481}, {207, 639}, {23, 79},
                              {-230, -691}, {-115, -341}, {92, 289}, {60, 336},
                              {-105, -467}, {135, 701}, {-90, -394},
                              {-184, -551}, {150, 774}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxLinedPoints");
    }
}
