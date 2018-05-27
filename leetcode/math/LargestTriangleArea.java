import org.junit.Test;
import static org.junit.Assert.*;

// LC812: https://leetcode.com/problems/largest-triangle-area/
//
// You have a list of points in the plane. Return the area of the largest
// triangle that can be formed by any 3 of the points.
public class LargestTriangleArea {
    // beats %(11 ms for 57 tests)
    public double largestTriangleArea(int[][] points) {
        int n = points.length;
        double res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    res = Math.max(res, area(points[i], points[j], points[k]));
                }
            }
        }
        return res;
    }

    private double area(int[] A, int[] B, int[] C) {
        return 0.5 * Math.abs(
            A[0] * B[1] + B[0] * C[1] + C[0] * A[1]
            - A[1] * B[0] - B[1] * C[0] - C[1] * A[0]);
    }

    private double area2(int[] A, int[] B, int[] C) { // 16 ms
        double a = distance(B, C);
        double b = distance(A, C);
        double c = distance(A, B);
        double p = (a + b + c) / 2;
        return Math.sqrt(Math.abs(p * (p - a) * (p - b) * (p - c)));
    }

    private double distance(int[] A, int[] B) {
        return Math.sqrt((A[0] - B[0]) * (A[0] - B[0])
                          + (A[1] - B[1]) * (A[1] - B[1]));
    }

    // beats %(20 ms for 57 tests)
    public double largestTriangleArea2(int[][] points) {
        double res = 0;
        for (int[] A : points) {
            for (int[] B : points) {
                for (int[] C : points) {
                    res = Math.max(res, area(A, B, C));
                }
            }
        }
        return res;
    }

    void test(int[][] points, double expected) {
        assertEquals(expected, largestTriangleArea(points), 1E-6);
        assertEquals(expected, largestTriangleArea2(points), 1E-6);
    }

    @Test
    public void test() {
        test(new int[][] { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 0, 2 }, { 2, 0 } },
             2);
        test(new int[][] { { -35, 19 }, { 40, 19 }, { 27, -20 }, { 35, -3 },
                           { 44, 20 }, { 22, -21 }, { 35, 33 },
                           { -19, 42 }, { 11, 47 }, { 11, 37 } }, 1799);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
