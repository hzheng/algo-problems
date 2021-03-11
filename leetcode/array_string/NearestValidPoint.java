import org.junit.Test;

import static org.junit.Assert.*;

// LC1779: https://leetcode.com/problems/find-nearest-point-that-has-the-same-x-or-y-coordinate/
//
// You are given two integers, x and y, which represent your current location on a Cartesian grid:
// (x, y). You are also given an array points where each points[i] = [ai, bi] represents that a
// point exists at (ai, bi). A point is valid if it shares the same x-coordinate or the same
// y-coordinate as your location.
// Return the index (0-indexed) of the valid point with the smallest Manhattan distance from your
// current location. If there are multiple, return the valid point with the smallest index. If there
// are no valid points, return -1.
// The Manhattan distance between two points (x1, y1) and (x2, y2) is abs(x1 - x2) + abs(y1 - y2).
//
// Constraints:
// 1 <= points.length <= 10^4
// points[i].length == 2
// 1 <= x, y, ai, bi <= 10^4
public class NearestValidPoint {
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(50.00%), 46.6 MB(50.00%) for 101 tests
    public int nearestValidPoint(int x, int y, int[][] points) {
        int min = Integer.MAX_VALUE;
        int res = -1;
        for (int i = 0; i < points.length; i++) {
            int[] p = points[i];
            if (p[0] != x && p[1] != y) { continue; }

            int d = Math.abs(p[0] - x) + Math.abs(p[1] - y);
            if (d < min) {
                min = d;
                res = i;
            }
        }
        return res;
    }

    private void test(int x, int y, int[][] points, int expected) {
        assertEquals(expected, nearestValidPoint(x, y, points));
    }

    @Test public void test() {
        test(3, 4, new int[][] {{1, 2}, {3, 1}, {2, 4}, {2, 3}, {4, 4}}, 2);
        test(3, 4, new int[][] {{3, 4}}, 0);
        test(3, 4, new int[][] {{2, 3}}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
