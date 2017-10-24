import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Point;

// LC587: https://leetcode.com/problems/erect-the-fence/
//
// There are some trees, where each tree is represented by (x,y) coordinate in a
// two-dimensional garden. Your job is to fence the entire garden using the minimum
// length of rope. The garden is well fenced only if all the trees are enclosed. Your
// task is to help find the coordinates of trees which are exactly located on the
// fence perimeter.
public class Fence {
    // Gift wrapping aka Jarvis march(https://en.wikipedia.org/wiki/Gift_wrapping_algorithm)
    // beats 45.18%(47 ms for 82 tests)
    // time complexity: O(N * M), space complexity: O(M) (M: output points)
    public List<Point> outerTrees(Point[] points) {
        int leftmost = points.length - 1;
        for (int i = leftmost - 1; i >= 0; i--) {
            if (points[i].x < points[leftmost].x) {
                leftmost = i;
            }
        }
        List<Point> hull = new ArrayList<>();
        for (int a = leftmost, b = -1, n = points.length; b != leftmost;
             a = b) {
            hull.add(points[a]);
            b = (a + 1) % n;
            List<Point> lines = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int dir = clockwise(points[a], points[i], points[b]);
                if (dir < 0) {
                    b = i;
                    lines.clear();
                } else if (dir == 0) {
                    lines.add(points[i]);
                }
            }
            hull.addAll(lines);
        }
        Collections.sort(hull, new Comparator<Point>() {
            public int compare(Point a, Point b) {
                return (a.x != b.x) ? (a.x - b.x) : (a.y - b.y);
            }
        });
        return new ArrayList<>(new HashSet<>(hull));
    }

    // Gift wrapping aka Jarvis march
    // beats 41.42%(50 ms for 82 tests)
    // time complexity: O(N * M), space complexity: O(M) (M: output points)
    public List<Point> outerTrees_2(Point[] points) {
        List<Point> hull = new ArrayList<>();
        if (points.length < 4) return Arrays.asList(points);

        int leftmost = points.length - 1;
        for (int i = leftmost - 1; i >= 0; i--) {
            if (points[i].x < points[leftmost].x) {
                leftmost = i;
            }
        }
        for (int a = leftmost, b = -1, n = points.length; b != leftmost;
             a = b) {
            b = (a + 1) % n;
            for (int i = 0; i < n; i++) {
                if (clockwise(points[a], points[i], points[b]) < 0) {
                    b = i;
                }
            }
            for (int i = 0; i < n; i++) {
                if (i != a && i != b
                    && clockwise(points[a], points[i], points[b]) == 0
                    && inBetween(points[a], points[i], points[b])) {
                    hull.add(points[i]);
                }
            }
            hull.add(points[b]);
        }
        return hull;
    }

    private boolean inBetween(Point a, Point b, Point c) {
        return (b.x >= a.x && b.x <= c.x || b.x <= a.x && b.x >= c.x)
               && (b.y >= a.y && b.y <= c.y || b.y <= a.y && b.y >= c.y);
    }

    private int clockwise(Point a, Point b, Point c) { // compare slope or vector's cross product
        return (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
    }

    // Graham Scan(https://en.wikipedia.org/wiki/Graham_scan)
    // beats 47.34%(45 ms for 82 tests)
    // time complexity: O(N * log(N)), space complexity: O(N)
    public List<Point> outerTrees2(Point[] points) {
        int n = points.length;
        if (n < 4) return Arrays.asList(points);

        Point start = bottommost(points);
        Arrays.sort(points, new Comparator<Point>() { // by polar angle
            public int compare(Point p, Point q) {
                double diff = clockwise(start, p, q);
                return (diff == 0) ? distance(start, p) - distance(start, q)
                : (diff > 0 ? 1 : -1);
            }
        });
        int i = n - 1;
        for (; i >= 0 && clockwise(start, points[n - 1], points[i]) == 0;
             i--) {}
        for (int low = i + 1, high = n - 1; low < high; low++, high--) { // reverse
            Point tmp = points[low];
            points[low] = points[high];
            points[high] = tmp;
        }
        ArrayDeque<Point> stack = new ArrayDeque<>();
        stack.push(points[0]);
        stack.push(points[1]);
        for (int j = 2; j < n; j++) {
            Point top = stack.pop();
            for (; clockwise(stack.peek(), top, points[j]) > 0;
                 top = stack.pop()) {}
            stack.push(top);
            stack.push(points[j]);
        }
        return new ArrayList<>(stack);
    }

    private int distance(Point p, Point q) {
        return (p.x - q.x) * (p.x - q.x) + (p.y - q.y) * (p.y - q.y);
    }

    private Point bottommost(Point[] points) {
        Point bottom = points[0];
        for (Point p : points) {
            if (p.y < bottom.y) {
                bottom = p;
            }
        }
        return bottom;
    }

    // Monotone chain aka Andrew's algorithm(https://en.wikibooks.org/wiki/Algorithm_Implementation/Geometry/Convex_hull/Monotone_chain)
    // beats 40.61%(51 ms for 82 tests)
    // time complexity: O(N * log(N)), space complexity: O(N)
    public List<Point> outerTrees3(Point[] points) {
        Arrays.sort(points, new Comparator<Point>() {
            public int compare(Point p, Point q) {
                return p.x == q.x ? p.y - q.y : p.x - q.x;
            }
        });
        Stack<Point> hull = new Stack<>();
        for (Point point : points) { // lower hull
            while (hull.size() >= 2 && clockwise(hull.get(hull.size() - 2),
                                                 hull.peek(), point) > 0) {
                hull.pop();
            }
            hull.push(point);
        }
        hull.pop();
        for (int i = points.length - 1; i >= 0; i--) { // upper hull
            while (hull.size() >= 2 && clockwise(hull.get(hull.size() - 2),
                                                 hull.peek(), points[i]) > 0) {
                hull.pop();
            }
            hull.push(points[i]);
        }
        return new ArrayList<>(new HashSet<>(hull));
    }

    void test(int[][] pts, int[][] expected) {
        Fence f = new Fence();
        test(pts, expected, f::outerTrees);
        test(pts, expected, f::outerTrees_2);
        test(pts, expected, f::outerTrees2);
        test(pts, expected, f::outerTrees3);
    }

    void test(int[][] pts, int[][] expected,
              Function<Point[], List<Point> > outerTrees) {
        List<Point> expectedList = Arrays.asList(toPoints(expected));
        Collections.sort(expectedList);
        List<Point> res = outerTrees.apply(toPoints(pts));
        Collections.sort(res);
        assertEquals(expectedList, res);
    }

    Point[] toPoints(int[][] pts) {
        int m = pts.length;
        Point[] points = new Point[m];
        for (int i = 0; i < m; i++) {
            points[i] = new Point(pts[i][0], pts[i][1]);
        }
        return points;
    }

    @Test
    public void test() {
        test(new int[][] {{1, 1}, {2, 2}, {2, 0}, {2, 4}, {3, 3}, {4, 2}},
             new int[][] {{1, 1}, {2, 0}, {4, 2}, {3, 3}, {2, 4}});
        test(new int[][] {{1, 2}, {2, 2}, {4, 2}},
             new int[][] {{1, 2}, {2, 2}, {4, 2}});
        test(new int[][] {{3, 0}, {4, 0}, {5, 0}, {6, 1}, {7, 2}, {7, 3},
                          {7, 4}, {6, 5}, {5, 5}, {4, 5}, {3, 5}, {2, 5},
                          {1, 4}, {1, 3}, {1, 2}, {2, 1}, {4, 2}, {0, 3}},
             new int[][] {{0, 3}, {1, 2}, {1, 4}, {2, 1}, {2, 5}, {3, 0},
                          {3, 5}, {4, 0}, {4, 5}, {5, 0}, {5, 5}, {6, 1},
                          {6, 5}, {7, 2}, {7, 3}, {7, 4}});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
