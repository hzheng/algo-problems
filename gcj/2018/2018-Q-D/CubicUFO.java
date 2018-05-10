import java.util.*;

import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/00000000000000cb/dashboard/00000000000079cc
// Qualification Round 2018: Problem D - Cubic UFO
//
// Toronto is a plane in three-dimensional space that is parallel to the xz 
// plane at y = -3 km. The alien ship is a solid cube with side length 1 km, 
// centered at (0 km, 0 km, 0 km), with its eight corners at (+/- 0.5 km, 
// +/- 0.5 km, +/- 0.5 km). The ship is casting a shadow onto the plane; the sun
// is a point infinitely far above the Toronto plane along the y-axis. The 
// shadow must cover an area of the plane that is acceptably close to A km^2.
// The ship cannot change size and the center but is able to rotate arbitrarily
// in place. Find a way to rotate the ship s.t the shadow's area is close to A.
// Express rotation by 3 centers of any 3 non-pairwise-opposing faces.
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow; each consists of one line with a rational A, the desired area of the
// shadow, in km^2, with exactly six digits after the decimal point.
// It is guaranteed that there is always a way to rotate the ship in the desired
// manner for the values of A allowed in this problem.
// Output
// For each test case, first output one line containing Case #x:, where x is the 
// test case number. Then, output three more lines with three rational values 
// each: the x, y, and z coordinates of one of your three provided face-centers.
// Your answer will be considered correct iff all of the following are true:
// The distance from each point to the origin must be between 0.5 - 10^-6 and
// 0.5 + 10^-6, inclusive.
// The angles between segments connecting the origin to each point must be
// between π/2 - 10-6 and π/2 + 10-6, inclusive.
// The area of the shadow, computed by projecting all 8 vertices onto the y = -3
// plane and finding the area of the convex hull of those projected points, must
// be between A - 10^-6 and A + 10^-6, inclusive. We will compute the vertices
// as +/- p1 +/- p2 +/- p3 (that is, for each pi we add either pi or -pi to the
// total using vector addition), where p1, p2, and p3 are the face-centers.
// Limits
// 1 ≤ T ≤ 100.
// Time limit: 30 seconds per test set.
// Memory limit: 1GB.
// Test set 1 (Visible)
// 1.000000 ≤ A ≤ 1.414213
// Test set 2 (Hidden)
// 1.000000 ≤ A ≤ 1.732050
public class CubicUFO {
    private static final double EPILSON = 1E-12;
    private static double SQRT2 = Math.sqrt(2);
    private static final double[][] CENTERS =
    { { 0.5, 0, 0 }, { 0, 0.5, 0 }, { 0, 0, 0.5 } };

    private static class Cube {
        double[][] v = new double[8][];

        Cube() {
            v[0] = new double[] { -0.5, -0.5, -0.5 };
            v[1] = new double[] { +0.5, -0.5, -0.5 };
            v[2] = new double[] { +0.5, -0.5, +0.5 };
            v[3] = new double[] { -0.5, -0.5, +0.5 };
            v[4] = new double[] { -0.5, +0.5, -0.5 };
            v[5] = new double[] { +0.5, +0.5, -0.5 };
            v[6] = new double[] { +0.5, +0.5, +0.5 };
            v[7] = new double[] { -0.5, +0.5, +0.5 };
        }

        public double areaY() {
            // https://en.wikipedia.org/wiki/Shoelace_formula
            // 1/2(x1y2 + x2y3 + x3y4 + x4y1 - x2y1 - x3y2 - x4y3 - x1y4) * 2
            // x1: v[2], x2: v[6], x3: v[7], x4: v[4]
            double s = Math.abs(v[2][0] * v[6][2] + v[6][0] * v[7][2]
                                + v[7][0] * v[4][2] + v[4][0] * v[2][2]
                                - v[6][0] * v[2][2] - v[7][0] * v[6][2]
                                - v[4][0] * v[7][2] - v[2][0] * v[4][2]);
            // cube shadow theorem: https://www.youtube.com/watch?v=rAHcZGjKVvg
            double high = Double.MIN_VALUE;
            double low = Double.MAX_VALUE;
            for (double[] p : v) {
                high = Math.max(high, p[1]);
                low = Math.min(low, p[1]);
            }
            return high - low; // should be the SAME as s;
        }

        public void rotateX(double angle) {
            for (double[] vertex : v) {
                rotateX(vertex, angle);
            }
        }

        public void rotateZ(double angle) {
            for (double[] vertex : v) {
                rotateZ(vertex, angle);
            }
        }

        public void rotateYX(double angle) {
            for (double[] vertex : v) {
                rotateY(vertex, Math.PI / 4);
                rotateX(vertex, angle);
            }
            // or: 
            // for (int i = 0; i < v.length; i++) {
                // v[i] = Solution.rotateYX(v[i], angle);
            // }
        }

        static void rotateX(double[] p, double angle) {
            double y = Math.cos(angle) * p[1] + Math.sin(angle) * p[2];
            double z = -Math.sin(angle) * p[1] + Math.cos(angle) * p[2];
            p[1] = y;
            p[2] = z;
        }

        static void rotateY(double[] p, double angle) {
            double z = Math.cos(angle) * p[2] + Math.sin(angle) * p[0];
            double x = -Math.sin(angle) * p[2] + Math.cos(angle) * p[0];
            p[0] = x;
            p[2] = z;
        }

        static void rotateZ(double[] p, double angle) {
            double x = Math.cos(angle) * p[0] + Math.sin(angle) * p[1];
            double y = -Math.sin(angle) * p[0] + Math.cos(angle) * p[1];
            p[0] = x;
            p[1] = y;
        }
    }

    private static double[] midpoint(double[] p1, double[] p2) {
        return new double[] { (p1[0] + p2[0]) / 2, (p1[1] + p2[1]) / 2,
                              (p1[2] + p2[2]) / 2};
    }

    private static double[] rotateYX(double[] p, double angle) {
        // apply rotations(http://mathworld.wolfram.com/RotationMatrix.html)
        double[][] Rx = { { 1, 0, 0 }, { 0, Math.cos(angle), Math.sin(angle) },
                          { 0, -Math.sin(angle), Math.cos(angle) } };
        double[][] Ry = {{SQRT2 / 2, 0, -SQRT2 / 2}, {0, 1, 0},
                         {SQRT2 / 2, 0, SQRT2 / 2}};
        return rotate(Rx, rotate(Ry, p));
    }

    // Binary Search
    public static double[][] solve(double area) {
        Cube cube = null;
        // find rotation with max area
        double maxAngle = 0;
        for (double low = 0, high = Math.PI / 2; high - low > EPILSON; ) {
            maxAngle = (low + high) / 2;
            cube = new Cube();
            cube.rotateYX(maxAngle);
            double a = cube.areaY();
            cube = new Cube();
            cube.rotateYX(maxAngle + EPILSON);
            if (cube.areaY() > a) {
                low = maxAngle;
            } else {
                high = maxAngle;
            }
        }
        // maxAngle should be the SAME as Math.atan(SQRT2)
        double mid = 0;
        for (double low = 0, high = maxAngle;; ) {
            mid = (low + high) / 2;
            cube = new Cube();
            cube.rotateYX(mid);
            double a = cube.areaY();
            if (Math.abs(a - area) < EPILSON) break;

            if (a > area) {
                high = mid;
            } else {
                low = mid;
            }
        }
        double[][] res = new double[3][];
        int i = 0;
        for (double[] center : CENTERS) {
            res[i++] = rotateYX(center, mid);
            // or: 
            // center = center.clone();
            // Cube.rotateY(center, Math.PI / 4);
            // Cube.rotateX(center, mid);
            // res[i++] = center;
        }
        // or :
        // res[0] = midpoint(cube.v[1], cube.v[6]);
        // res[1] = midpoint(cube.v[6], cube.v[4]);
        // res[2] = midpoint(cube.v[2], cube.v[7]);
        return res;
    }

    // Pure Math
    public static double[][] solve2(double a) {
        // https://math.stackexchange.com/questions/2725780/orthogonal-projection-area-of-a-3-d-cube
        double v = Math.sqrt(6 - 2 * a * a);
        return new double[][] {
                   {(3 + a + v) / 12, (a * 2 - v) / 12, (3 - a - v) / 12},
                   {(-2 * a + v) / 12, (a + v) / 6, (a * 2 - v) / 12},
                   {(3 - a - v) / 12, (-a * 2 + v) / 12, (3 + a + v) / 12}
        };
    }

    // Pure Math
    public static double[][] solve3(double area) {
        // small set case
        // if (area <= S2RT2 + EPILSON) return rotateZ(area);

        // https://math.stackexchange.com/questions/874848/area-of-projection-of-cube-in-mathbbz3-onto-a-hyperplane
        // area = cosA + cosB + cosC(normal vector's angle)
        // let A = C, solve equation 2x + (1-2x^2) ^ 1/2 = area, we have:
        double cosA = area / 3 - Math.sqrt(6 - 2 * area * area) / 6;
        double sinA = Math.sqrt(1 - cosA * cosA);
        double cosB = Math.sqrt(1 - cosA * cosA * 2);
        // https://stackoverflow.com/questions/1251828/calculate-rotations-to-look-at-a-3d-point
        double Z = Math.atan2(cosA, cosB); // rotate around Z-axis
        double sinZ = Math.sin(Z);
        double cosZ = Math.cos(Z);
        double sinX = cosA; // X = pi/2 - C  // rotate around X-axis
        double cosX = sinA; // X = pi/2 - C
        double[][] res = new double[3][];
        // apply rotations(http://mathworld.wolfram.com/RotationMatrix.html)
        double[][] Rx = { { 1, 0, 0 }, { 0, cosX, sinX }, { 0, -sinX, cosX } };
        // double[][] Ry = {{cosY, 0, -sinY}, {0, 1, 0}, {sinY, 0, cosY}};
        double[][] Rz = { { cosZ, sinZ, 0 }, { -sinZ, cosZ, 0 }, { 0, 0, 1 } };
        int i = 0;
        for (double[] center : CENTERS) {
            res[i++] = rotate(Rx, rotate(Rz, center));
        }
        return res;
    }

    // only good for visible test set (i.e. area <= SQRT2)
    private static double[][] rotateZ(double area) {
        // area = cosA + cosB + cosC(A, B, C are normal vector's angles)
        // rotate around z-axis by keeping C=pi/2, B=pi/2-A, we get:
        // cosA + sinA = area, so solve the equation:
        double sinA = (area + Math.sqrt(Math.max(2 - area * area, 0))) * 0.5;
        double cosA = area - sinA;
        // X, Y, Z are rotations(around X/Y/Z axis resp.)
        // normal vector rotates around Z axis from (pi/2, 0, pi/2) to
        // (A, pi/2 - A, pi/2), rotation is (0, 0, A - pi/2), hence:
        double sinZ = -cosA; // Z = A - pi/2
        double cosZ = sinA; // Z = A - pi/2
        double[][] res = new double[3][];
        int i = 0;
        double[][] R = { { cosZ, sinZ, 0 }, { -sinZ, cosZ, 0 }, { 0, 0, 1 } };
        for (double[] center : CENTERS) {
            res[i++] = rotate(R, center);
        }
        return res;
    }

    // 2-D geometry method for the small set solution
    private static double[][] rotateZ0(double area) {
        // calculate the projection after rotate angle V:
        // area = 1 + (2 + 2 - 4cosV) ^ 1/2 * cos(pi/4 + V/2)
        // area = SQRT2 * sin(pi/4 + V), we have:
        // cosV + sinV = area(same as rotateZ except V is rotation angle)
        double sinV = (area + Math.sqrt(Math.max(2 - area * area, 0))) * 0.5;
        double cosV = area - sinV;
        return new double[][] { new double[] { sinV * 0.5, cosV * 0.5, 0 },
                                new double[] { -cosV * 0.5, sinV * 0.5, 0 },
                                new double[] { 0.0, 0.0, 0.5 } };
    }

    private static double[] rotate(double[][] R, double[] P) {
        double[] res = new double[P.length];
        for (int i = 0; i < P.length; i++) {
            for (int j = 0; j < P.length; j++) {
                res[i] += R[i][j] * P[j];
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner in =
            new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            out.println();
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        double A = in.nextDouble();
    for (double[] point : solve(A)) {
            out.format("%.10f %.10f %.10f", point[0], point[1], point[2]);
            out.println();
        }
    }
}
