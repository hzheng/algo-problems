import java.util.*;
import java.awt.Point;

import org.junit.Test;
import static org.junit.Assert.*;

// LC835: https://leetcode.com/problems/image-overlap/
//
// Two images A and B are given, represented as binary, square matrices of the
// same size.  (A binary matrix has only 0s and 1s as values.) We translate one
// image however we choose (sliding it left, right, up, or down any number of
// units), and place it on top of the other image.  After, the overlap of this
// translation is the number of positions that have a 1 in both images.
// What is the largest possible overlap?
public class ImageOverlap {
    // Bit Manipulation
    // beats %(13 ms for 50 tests)
    // time complexity: O(N ^ 4), space complexity: O(N)
    public int largestOverlap(int[][] A, int[][] B) {
        int n = A.length;
        int[] a = toInts(A);
        int[] b = toInts(B);
        int res = 0;
        for (int x = 1 - n; x < n; x++) {
            for (int y = 1 - n; y < n; y++) {
                res = Math.max(res, overlap(a, b, x, y));
            }
        }
        return res;
    }

    private int overlap(int[] A, int[] B, int x, int y) {
        int res = 0;
        int[] C = new int[A.length];
        for (int i = Math.max(0, y); i < A.length + Math.min(0, y); i++) {
            C[i] = x > 0 ? (A[i - y] >> x) : (A[i - y] << -x);
        }
        for (int i = 0; i < A.length; i++) {
            res += Integer.bitCount(C[i] & B[i]);
        }
        return res;
    }

    private int[] toInts(int[][] A) {
        int[] a = new int[A.length];
        for (int i = 0; i < A.length; i++) {
            for (int j = A.length - 1, power = 1; j >= 0; j--, power <<= 1) {
                a[i] += A[i][j] * power;
            }
        }
        return a;
    }

    // beats %(23 ms for 50 tests)
    // time complexity: O(N ^ 4), space complexity: O(N ^ 2)
    public int largestOverlap2(int[][] A, int[][] B) {
        int n = A.length;
        int[][] count = new int[2 * n + 1][2 * n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    for (int l = 0; l < n; l++) {
                        count[k - i + n][l - j + n] += A[i][j] & B[k][l];
                    }
                }
            }
        }
        int res = 0;
        for (int[] row : count) {
            for (int c : row) {
                res = Math.max(res, c);
            }
        }
        return res;
    }

    // HashSet
    // beats %(418 ms for 50 tests)
    // time complexity: O(N ^ 6), space complexity: O(N ^ 2)
    public int largestOverlap3(int[][] A, int[][] B) {
        int n = A.length;
        Set<Point> A2 = new HashSet<>();
        Set<Point> B2 = new HashSet<>();
        for (int i = 0; i < n * n; i++) {
            if (A[i / n][i % n] == 1) {
                A2.add(new Point(i / n, i % n));
            }
            if (B[i / n][i % n] == 1) {
                B2.add(new Point(i / n, i % n));
            }
        }
        int res = 0;
        Set<Point> seen = new HashSet<>();
        for (Point a : A2)
            for (Point b : B2) {
                Point delta = new Point(b.x - a.x, b.y - a.y);
                if (!seen.add(delta)) continue;

                int count = 0;
                for (Point p : A2) {
                    if (B2.contains(new Point(p.x + delta.x, p.y + delta.y))) {
                        count++;
                    }
                }
                res = Math.max(res, count);
            }

        return res;
    }

    // Hash Table
    // beats %(107 ms for 50 tests)
    // time complexity: O(N ^ 4), space complexity: O(N ^ 2)
    public int largestOverlap4(int[][] A, int[][] B) {
        int n = A.length;
        List<Integer> a = new ArrayList<>();
        for (int i = n * n - 1; i >= 0; i--) {
            if (A[i / n][i % n] == 1) {
                a.add(i / n * 100 + i % n);
            }
        }
        List<Integer> b = new ArrayList<>();
        for (int i = n * n - 1; i >= 0; i--) {
            if (B[i / n][i % n] == 1) {
                b.add(i / n * 100 + i % n);
            }
        }
        Map<Integer, Integer> count = new HashMap<>();
        for (int i : a) {
            for (int j : b) {
                count.put(i - j, count.getOrDefault(i - j, 0) + 1);
            }
        }
        int res = 0;
        for (int c : count.values()) {
            res = Math.max(res, c);
        }
        return res;
    }

    void test(int[][] A, int[][] B, int expected) {
        assertEquals(expected, largestOverlap(A, B));
        assertEquals(expected, largestOverlap2(A, B));
        assertEquals(expected, largestOverlap3(A, B));
        assertEquals(expected, largestOverlap4(A, B));
    }

    @Test
    public void test() {
        test(new int[][] { { 0, 1 }, { 1, 1 } }, 
             new int[][] { { 1, 1 }, { 1, 0 } }, 2);
        test(new int[][] { { 1, 1, 0 }, { 0, 1, 0 }, { 0, 1, 0 } },
             new int[][] { { 0, 0, 0 }, { 0, 1, 1 }, { 0, 0, 1 } }, 3);
        test(new int[][] { { 0, 1, 0, 0, 0 }, { 1, 0, 1, 0, 0 }, 
                           { 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0 },
                           { 1, 0, 0, 1, 1 } },
             new int[][] { { 1, 0, 1, 1, 1 }, { 1, 1, 1, 1, 0 },
                           { 1, 1, 1, 1, 1 }, { 0, 1, 1, 1, 0 },
                           { 0, 0, 1, 1, 1 } }, 5);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
