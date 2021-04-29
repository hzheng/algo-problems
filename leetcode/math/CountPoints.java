import org.junit.Test;

import static org.junit.Assert.*;

// LC1828: https://leetcode.com/problems/queries-on-number-of-points-inside-a-circle/
//
// You are given an array points where points[i] = [xi, yi] is the coordinates of the ith point on
// a 2D plane. Multiple points can have the same coordinates.
// You are also given an array queries where queries[j] = [xj, yj, rj] describes a circle centered
// at (xj, yj) with a radius of rj.
// For each query queries[j], compute the number of points inside the jth circle. Points on the
// border of the circle are considered inside.
// Return an array answer, where answer[j] is the answer to the jth query.
//
// Constraints:
// 1 <= points.length <= 500
// points[i].length == 2
// 0 <= xi, yi <= 500
// 1 <= queries.length <= 500
// queries[j].length == 3
// 0 <= xj, yj <= 500
// 1 <= rj <= 500
// All coordinates are integers.
public class CountPoints {
    // time complexity: O(N*Q), space complexity: O(Q)
    // 15 ms(98.09%), 39.6 MB(66.99%) for 94 tests
    public int[] countPoints(int[][] points, int[][] queries) {
        int n = queries.length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int[] q = queries[i];
            int d = q[2] * q[2];
            int count = 0;
            for (int[] p : points) {
                if ((p[0] - q[0]) * (p[0] - q[0]) + (p[1] - q[1]) * (p[1] - q[1]) <= d) {
                    count++;
                }
            }
            res[i] = count;
        }
        return res;
    }

    private void test(int[][] points, int[][] queries, int[] expected) {
        assertArrayEquals(expected, countPoints(points, queries));
    }

    @Test public void test1() {
        test(new int[][] {{1, 3}, {3, 3}, {5, 3}, {2, 2}},
             new int[][] {{2, 3, 1}, {4, 3, 1}, {1, 1, 2}}, new int[] {3, 2, 2});
        test(new int[][] {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}},
             new int[][] {{1, 2, 2}, {2, 2, 2}, {4, 3, 2}, {4, 3, 3}}, new int[] {2, 3, 2, 4});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
