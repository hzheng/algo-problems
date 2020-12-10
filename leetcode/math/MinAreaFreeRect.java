import java.util.*;
import java.awt.Point;

import org.junit.Test;

import static org.junit.Assert.*;

// LC963: https://leetcode.com/problems/minimum-area-rectangle-ii/
//
// Given a set of points in the xy-plane, determine the minimum area of any rectangle formed from
// these points, with sides not necessarily parallel to the x and y axes.
// If there isn't any rectangle, return 0.
//
// Note:
// 1 <= points.length <= 50
// 0 <= points[i][0] <= 40000
// 0 <= points[i][1] <= 40000
// All points are distinct.
// Answers within 10^-5 of the actual value will be accepted as correct.
public class MinAreaFreeRect {
    // Hash Table
    // time complexity: O(N^2), space complexity: O(N)
    // 12 ms(91.52%), 39.5 MB(61.16%) for 109 tests
    public double minAreaFreeRect(int[][] points) {
        int n = points.length;
        Map<Long, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long d = dist(points, i, j);
                map.computeIfAbsent(d, x -> new ArrayList<>()).add(i * n + j);
            }
        }
        double res = Double.MAX_VALUE;
        for (long d : map.keySet()) {
            List<Integer> edges = map.get(d);
            for (int i = edges.size() - 1; i > 0; i--) {
                int e1 = edges.get(i);
                int p11 = e1 / n;
                int p12 = e1 % n;
                for (int j = i - 1; j >= 0; j--) {
                    int e2 = edges.get(j);
                    int p21 = e2 / n;
                    int p22 = e2 % n;
                    if (p11 == p21 || p11 == p22 || p12 == p21 || p12 == p22) { continue; }

                    long d2 = dist(points, p11, p21);
                    if (d2 != dist(points, p12, p22)) { continue; }

                    if (isVertical(points, p11, p12, p21, p22) || isVertical(points, p11, p12, p22,
                                                                             p21)) {
                        long d3 = dist(points, p11, p22);
                        res = Math.min(res, d * Math.min(d2, d3));
                    }
                }
            }
        }
        return (res < Double.MAX_VALUE) ? Math.sqrt(res) : 0;
    }

    private boolean isVertical(int[][] points, int i, int j, int k, int l) {
        int[] p = points[i];
        int[] q = points[j];
        int[] r = points[k];
        int[] s = points[l];
        return (q[1] - p[1]) * (r[1] - p[1]) == (p[0] - q[0]) * (r[0] - p[0])
               && (q[1] - p[1]) * (s[1] - q[1]) == (p[0] - q[0]) * (s[0] - q[0]);
    }

    private long dist(int[][] points, int i, int j) {
        int[] p = points[i];
        int[] q = points[j];
        return ((long)(p[0] - q[0])) * (p[0] - q[0]) + ((long)(p[1] - q[1]) * (p[1] - q[1]));
    }

    // Set
    // time complexity: O(N^3), space complexity: O(N)
    // 47 ms(54.02%), 39.3 MB(65.18%) for 109 tests
    public double minAreaFreeRect2(int[][] points) {
        int n = points.length;
        Point[] A = new Point[n];
        Set<Point> pointSet = new HashSet<>();
        for (int i = 0; i < n; ++i) {
            A[i] = new Point(points[i][0], points[i][1]);
            pointSet.add(A[i]);
        }
        double res = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            Point p1 = A[i];
            for (int j = 0; j < n; j++) {
                if (j == i) { continue; }

                Point p2 = A[j];
                for (int k = j + 1; k < n; k++) {
                    if (k == i) { continue; }

                    Point p3 = A[k];
                    Point p4 = new Point(p2.x + p3.x - p1.x, p2.y + p3.y - p1.y);
                    if (!pointSet.contains(p4)) { continue; }

                    if ((p2.x - p1.x) * (p3.x - p1.x) + (p2.y - p1.y) * (p3.y - p1.y) == 0) {
                        res = Math.min(res, p1.distance(p2) * p1.distance(p3));
                    }
                }
            }
        }
        return res < Double.MAX_VALUE ? res : 0;
    }

    // Hash Table
    // time complexity: O(N^2*log(N)), space complexity: O(N)
    // 15 ms(85.71%), 39.3 MB(65.18%) for 109 tests
    public double minAreaFreeRect3(int[][] points) {
        int n = points.length;
        Point[] A = new Point[n];
        for (int i = 0; i < n; i++) {
            A[i] = new Point(points[i][0], points[i][1]);
        }
        Map<Integer, Map<Point, List<Point>>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Point center = new Point(A[i].x + A[j].x, A[i].y + A[j].y);
                int r = (A[i].x - A[j].x) * (A[i].x - A[j].x);
                r += (A[i].y - A[j].y) * (A[i].y - A[j].y);
                map.computeIfAbsent(r, x -> new HashMap<>())
                   .computeIfAbsent(center, x -> new ArrayList<>()).add(A[i]);
            }
        }
        double res = Double.MAX_VALUE;
        for (Map<Point, List<Point>> centerMap : map.values()) {
            for (Point center : centerMap.keySet()) {
                List<Point> candidates = centerMap.get(center);
                for (int i = 0, size = candidates.size(); i < size; i++) {
                    Point p = candidates.get(i);
                    for (int j = i + 1; j < size; j++) {
                        Point q = candidates.get(j);
                        Point r = new Point(center);
                        r.translate(-q.x, -q.y);
                        res = Math.min(res, p.distance(q) * p.distance(r));
                    }
                }
            }
        }
        return res < Double.MAX_VALUE ? res : 0;
    }

    // Hash Table
    // time complexity: O(N^2*log(N)), space complexity: O(N)
    // 28 ms(65.63%), 40.1 MB(38.84%) for 109 tests
    public double minAreaFreeRect4(int[][] points) {
        double res = Double.MAX_VALUE;
        int n = points.length;
        Map<String, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dist = dist(points, i, j);
                long center = points[i][0] + points[j][0];
                center = (center << Integer.SIZE) | (points[i][1] + points[j][1]); // double center
                String key = "" + dist + "," + center;
                map.computeIfAbsent(key, k -> new ArrayList<>()).add(i * n + j);
            }
        }
        for (List<Integer> candidates: map.values()) {
            for (int i = 0; i < candidates.size(); i++) {
                int p1 = candidates.get(i) / n;
                int p2 = candidates.get(i) % n;
                for (int j = i + 1; j < candidates.size(); j++) {
                    int p3 = candidates.get(j) / n;
                    res = Math.min(res, dist(points, p1, p3) * dist(points, p2, p3));
                }
            }
        }
        return res == Double.MAX_VALUE ? 0.0 : Math.sqrt(res);
    }

    private void test(int[][] points, double expected) {
        assertEquals(expected, minAreaFreeRect(points), 1e-5);
        assertEquals(expected, minAreaFreeRect2(points), 1e-5);
        assertEquals(expected, minAreaFreeRect3(points), 1e-5);
        assertEquals(expected, minAreaFreeRect4(points), 1e-5);
    }

    @Test public void test() {
        test(new int[][] {{0, 1}}, 0);
        test(new int[][] {{0, 1}, {1, 1}, {1, 0}, {2, 0}}, 0);
        test(new int[][] {{1, 2}, {2, 1}, {1, 0}, {0, 1}}, 2.00000);
        test(new int[][] {{0, 1}, {2, 1}, {1, 1}, {1, 0}, {2, 0}}, 1.00000);
        test(new int[][] {{0, 3}, {1, 2}, {3, 1}, {1, 3}, {2, 1}}, 0);
        test(new int[][] {{3, 1}, {1, 1}, {0, 1}, {2, 1}, {3, 3}, {3, 2}, {0, 2}, {2, 3}}, 2.00000);
        test(new int[][] {{0, 1}, {1, 0}, {3, 2}, {2, 3}, {0, 3}, {1, 1}, {3, 3}, {0, 2}}, 3.00000);
        test(new int[][] {{24420, 16685}, {20235, 25520}, {14540, 20845}, {20525, 14500},
                          {16876, 24557}, {24085, 23720}, {25427, 18964}, {21036, 14573},
                          {24420, 23315}, {22805, 24760}, {21547, 25304}, {16139, 23952},
                          {21360, 14645}, {24715, 17120}, {19765, 25520}, {19388, 25491},
                          {22340, 25005}, {25520, 19765}, {25365, 21320}, {23124, 15443},
                          {20845, 14540}, {24301, 16532}, {16685, 24420}, {25100, 17875},
                          {22125, 25100}, {15699, 23468}, {14592, 21131}, {25460, 19155},
                          {17837, 25084}, {23468, 24301}, {25460, 20845}, {18453, 25304},
                          {21131, 14592}, {22805, 15240}, {19475, 25500}, {15443, 23124},
                          {25355, 21360}, {15285, 22880}, {20000, 25525}, {24085, 16280},
                          {22163, 25084}, {22880, 15285}, {14916, 22163}, {16280, 24085},
                          {24875, 17400}, {22600, 24875}, {14573, 21036}, {25427, 21036},
                          {17120, 24715}, {25500, 19475}}, 2141490.00000);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
