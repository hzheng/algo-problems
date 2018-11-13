import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC939: https://leetcode.com/problems/minimum-area-rectangle/
//
// Given a set of points in the xy-plane, determine the minimum area of a 
// rectangle formed from these points, with sides parallel to the x and y axes.
// If there isn't any rectangle, return 0.
// Note:
// 1 <= points.length <= 500
// 0 <= points[i][0] <= 40000
// 0 <= points[i][1] <= 40000
// All points are distinct.
public class MinAreaRect {
    // Sort + SortedSet + SortedMap
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(490 ms for 137 tests)
    public int minAreaRect(int[][] points) {
        Arrays.sort(points, (a, b) -> (a[0] != b[0]) ? a[0] - b[0] : a[1] - b[1]);
        SortedMap<Integer, List<Integer>> xMap = new TreeMap<>();
        SortedMap<Integer, NavigableSet<Integer>> yMap = new TreeMap<>();
        for (int[] pt : points) {
            xMap.computeIfAbsent(pt[0], a -> new ArrayList<>()).add(pt[1]);
            yMap.computeIfAbsent(pt[1], a -> new TreeSet<>()).add(pt[0]);
        }
        int res = Integer.MAX_VALUE;
        for (int x : xMap.keySet()) {
            List<Integer> ys = xMap.get(x);
            for (int d = 1, sz = ys.size(); d < sz; d++) {
                for (int i = d; i < sz; i++) {
                    int y1 = ys.get(i - d);
                    int y2 = ys.get(i);
                    Iterator<Integer> itr1 = yMap.get(y1).tailSet(x, false).iterator();
                    Iterator<Integer> itr2 = yMap.get(y2).tailSet(x, false).iterator();
                    // find the first intersection of ltr1 and ltr2
                    if (!itr1.hasNext() || !itr2.hasNext()) continue;

                    for (int x1 = itr1.next(), x2 = itr2.next();;) {
                        if (x1 == x2) {
                            res = Math.min(res, (y2 - y1) * (x1 - x));
                            break;
                        } else if (x1 < x2) {
                            if (!itr1.hasNext()) break;
                            x1 = itr1.next();
                        } else {
                            if (!itr2.hasNext()) break;
                            x2 = itr2.next();
                        }
                    }
                }
            }
        }
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    // Set
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(263 ms for 137 tests)
    public int minAreaRect2(int[][] points) {
        int res = Integer.MAX_VALUE;
        int n = points.length;
        Set<Integer> pointSet = new HashSet<>();
        final int SHIFT = 16;
        for (int[] point : points) {
            pointSet.add(point[0] << SHIFT | point[1]);
        }
        for (int i = 0; i < n; i++) {
            int x1 = points[i][0];
            int y1 = points[i][1];
            for (int j = i + 1; j < n; j++) {
                int x2 = points[j][0];
                int y2 = points[j][1];
                if (x1 == x2 || y1 == y2) continue;

                if (pointSet.contains(x1 << SHIFT | y2) && pointSet.contains(x2 << SHIFT | y1)) {
                    res = Math.min(res, Math.abs((x1 - x2) * (y1 - y2)));
                }
            }
        }
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    // SortedMap + hashtable
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(326 ms for 137 tests)
    public int minAreaRect3(int[][] points) {
        SortedMap<Integer, List<Integer>> cols = new TreeMap<>();
        for (int[] point : points) {
            cols.computeIfAbsent(point[0], a -> new ArrayList<>()).add(point[1]);
        }
        int res = Integer.MAX_VALUE;
        final int SHIFT = 16;
        Map<Integer, Integer> prevX = new HashMap<>();
        for (int x : cols.keySet()) {
            List<Integer> row = cols.get(x);
            Collections.sort(row);
            for (int i = 0, len = row.size(); i < len; i++)
                for (int j = i + 1; j < len; j++) {
                    int y1 = row.get(i);
                    int y2 = row.get(j);
                    int code = y1 << SHIFT | y2;
                    if (prevX.containsKey(code)) {
                        res = Math.min(res, (x - prevX.get(code)) * (y2 - y1));
                    }
                    prevX.put(code, x);
                }
        }
        return res == Integer.MAX_VALUE ? 0 : res;
    }

    void test(int[][] points, int expected) {
        assertEquals(expected, minAreaRect(points));
        assertEquals(expected, minAreaRect2(points));
        assertEquals(expected, minAreaRect3(points));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 1}, {1, 3}, {3, 1}, {3, 3}, {2, 2}}, 4);
        test(new int[][] {{1, 1}, {1, 3}, {3, 1}, {3, 3}, {4, 1}, {4, 3}}, 2);
        test(new int[][] {{3, 2}, {3, 1}, {4, 4}, {1, 1}, {4, 3}, {0, 3}, {0, 2}, {4, 0}}, 0);
        test(new int[][] {{1, 2}, {3, 2}, {1, 3}, {3, 3}, {3, 0}, {1, 4}, {4, 2}, {4, 0}}, 2);
        test(new int[][] {{0, 1}, {3, 2}, {0, 0}, {4, 4}, {2, 0}, {1, 3}, {0, 4}, {4, 2}, {1, 0},
                          {2, 4}},
             8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
