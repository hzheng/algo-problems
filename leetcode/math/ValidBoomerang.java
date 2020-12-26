import org.junit.Test;

import static org.junit.Assert.*;

// LC1037: https://leetcode.com/problems/valid-boomerang/
//
// A boomerang is a set of 3 points that are all distinct and not in a straight line.
// Given a list of three points in the plane, return whether these points are a boomerang.
//
// Note:
// points.length == 3
// points[i].length == 2
// 0 <= points[i][j] <= 100
public class ValidBoomerang {
    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 36.4 MB(70.29%) for 190 tests
    public boolean isBoomerang(int[][] points) {
        int x1 = points[1][0] - points[0][0];
        int y1 = points[1][1] - points[0][1];
        int x2 = points[2][0] - points[0][0];
        int y2 = points[2][1] - points[0][1];
        return x1 * y2 != x2 * y1; // vector cross product = 0 or two slopes equal
    }

    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(33.23%) for 190 tests
    public boolean isBoomerang2(int[][] points) {
        // area = | (Ax * (By − Cy) + Bx * (Cy − Ay) + Cx * (Ay − By)) / 2 |
        int[] p0 = points[0];
        int[] p1 = points[1];
        int[] p2 = points[2];
        return p0[0] * (p1[1] - p2[1]) + p1[0] * (p2[1] - p0[1]) + p2[0] * (p0[1] - p1[1]) != 0;
    }

    private void test(int[][] points, boolean expected) {
        assertEquals(expected, isBoomerang(points));
        assertEquals(expected, isBoomerang2(points));
    }

    @Test public void test() {
        test(new int[][] {{1, 1}, {2, 3}, {3, 2}}, true);
        test(new int[][] {{1, 1}, {2, 2}, {3, 3}}, false);
        test(new int[][] {{0, 1}, {0, 2}, {0, -1}}, false);
        test(new int[][] {{0, 1}, {0, 2}, {0, 1}}, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
