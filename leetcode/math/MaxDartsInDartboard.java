import org.junit.Test;

import static org.junit.Assert.*;

// LC1453: https://leetcode.com/problems/maximum-number-of-darts-inside-of-a-circular-dartboard/
//
// You have a very large square wall and a circular dartboard placed on the wall. You have been
// challenged to throw darts into the board blindfolded. Darts thrown at the wall are represented as
// an array of points on a 2D plane. Return the maximum number of points that are within or lie on
// any circular dartboard of radius r.
//
// Constraints:
// 1 <= points.length <= 100
// points[i].length == 2
// -10^4 <= points[i][0], points[i][1] <= 10^4
// 1 <= r <= 5000
public class MaxDartsInDartboard {
    // time complexity: O(N^3), space complexity: O(1)
    // 25 ms(60.00%), 38.6 MB(26.67%) for 65 tests
    public int numPoints(int[][] points, int r) {
        int n = points.length;
        int squareOfDiameter = r * r * 4;
        int res = 1;
        for (int i = 0; i < n - 1; i++) {
            int[] p = points[i];
            for (int j = i + 1; j < n; j++) {
                int[] q = points[j];
                int d = (p[0] - q[0]) * (p[0] - q[0]) + (p[1] - q[1]) * (p[1] - q[1]);
                if (d > squareOfDiameter) { continue; }

                double midPointXOfPQ = (p[0] + q[0]) * 0.5;
                double midPointYOfPQ = (p[1] + q[1]) * 0.5;
                for (int k = 1; k >= 0; k--) {
                    int sign = k * 2 - 1;
                    double[] rotatedPQ = new double[] {(q[1] - p[1]) * sign, (p[0] - q[0]) * sign};
                    changeLength(rotatedPQ, Math.sqrt(r * r - d * 0.25));
                    double cx = midPointXOfPQ + rotatedPQ[0];
                    double cy = midPointYOfPQ + rotatedPQ[1];
                    int count = 0;
                    for (int[] x : points) {
                        if ((x[0] - cx) * (x[0] - cx) + (x[1] - cy) * (x[1] - cy) <= r * r) {
                            count++;
                        }
                    }
                    res = Math.max(res, count);
                }
            }
        }
        return res;
    }

    private void changeLength(double[] v, double len) {
        double scale = len / Math.sqrt(v[0] * v[0] + v[1] * v[1]);
        v[0] *= scale;
        v[1] *= scale;
    }

    // TODO: O(N^2*log(N))
    //  https://www.quora.com/What-is-an-algorithm-for-enclosing-the-maximum-number-of-points-in-a-2-D-plane-with-a-fixed-radius-circle

    private void test(int[][] points, int r, int expected) {
        assertEquals(expected, numPoints(points, r));
    }

    @Test public void test() {
        test(new int[][] {{5, 5}, {-2, 5}, {4, 2}, {1, -1}, {1, 1}}, 5, 5);
        test(new int[][] {{-2, 0}, {2, 0}, {0, 2}, {0, -2}}, 2, 4);
        test(new int[][] {{-3, 0}, {3, 0}, {2, 6}, {5, 4}, {0, 9}, {7, 8}}, 5, 5);
        test(new int[][] {{-2, 0}, {2, 0}, {0, 2}, {0, -2}}, 1, 1);
        test(new int[][] {{1, 2}, {3, 5}, {1, -1}, {2, 3}, {4, 1}, {1, 3}}, 2, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
