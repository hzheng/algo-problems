import java.util.Objects;

import org.junit.Test;
import static org.junit.Assert.*;

// LC240: https://leetcode.com/problems/search-a-2d-matrix-ii/
//
// Write an efficient algorithm that searches for a value in an m x n matrix.
// This matrix has the following properties:
// Integers in each row are sorted in ascending from left to right.
// Integers in each column are sorted in ascending from top to bottom.
public class MatrixSearch2 {
    // Recursion + Divide & Conquer
    // time complexity: O(N ^ 1.58) (T(n) = 3T(n/2) + c)
    // beats 4.19%(36 ms)
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        int n = matrix[0].length;
        return searchMatrix(matrix, 0, n - 1, 0, m - 1, target);
    }

    private boolean searchMatrix(int[][] matrix, int x1, int x2,
                                 int y1, int y2, int target) {
        if (x1 > x2 || y1 > y2) return false;

        int midX = (x1 + x2) >>> 1;
        int midY = (y1 + y2) >>> 1;
        if (target == matrix[midY][midX]) return true;

        if (target < matrix[midY][midX]) {
            return searchMatrix(matrix, x1, midX - 1, y1, midY - 1, target)
                   || searchMatrix(matrix, x1, midX - 1, midY, y2, target)
                   || searchMatrix(matrix, midX, x2, y1, midY - 1, target);
        } else {
            return searchMatrix(matrix, midX + 1, x2, y1, midY, target)
                   || searchMatrix(matrix, x1, midX, midY + 1, y2, target)
                   || searchMatrix(matrix, midX + 1, x2, midY + 1, y2, target);
        }
    }

    // Solution of Choice
    // Recursion + Divide & Conquer + Binary Search
    // Cracking the Coding Interview(5ed) Problem 11.6:
    // time complexity: O(N)  (T(n) = 2T(n/2) + c lg n)
    // beats 8.52%(28 ms)
    public boolean searchMatrix2(int[][] matrix, int target) {
        Coordinate origin = new Coordinate(0, 0);
        Coordinate dest = new Coordinate(matrix.length - 1, matrix[0].length - 1);
        return findElement(matrix, origin, dest, target) != null;
    }

    private Coordinate findElement(int[][] matrix, Coordinate origin, Coordinate dest, int x) {
        if (!origin.inbounds(matrix) || !dest.inbounds(matrix)) return null;
        if (matrix[origin.row][origin.column] == x) return origin;
        if (!origin.isBefore(dest)) return null;

        // Set start to start of diagonal and end to the end of the diagonal
        // the grid may not be square, the end of the diagonal may not equal dest
        Coordinate start = (Coordinate)origin.clone();
        int diagDist = Math.min(dest.row - origin.row, dest.column - origin.column);
        Coordinate end = new Coordinate(start.row + diagDist, start.column + diagDist);
        Coordinate p = new Coordinate(0, 0);
        // binary search on the diagonal, looking for the first greater element
        while (start.isBefore(end)) {
            p.setToAverage(start, end);
            if (x > matrix[p.row][p.column]) {
                start.row = p.row + 1;
                start.column = p.column + 1;
            } else {
                end.row = p.row - 1;
                end.column = p.column - 1;
            }
        }
        // Split the grid into quadrants. Search the bottom left and the top right
        return partitionAndSearch(matrix, origin, dest, start, x);
    }

    private Coordinate partitionAndSearch(int[][] matrix,
                                          Coordinate origin, Coordinate dest,
                                          Coordinate pivot, int x) {
        Coordinate lowerLeftOrigin = new Coordinate(pivot.row, origin.column);
        Coordinate lowerLeftDest = new Coordinate(dest.row, pivot.column - 1);
        Coordinate lowerLeft = findElement(matrix, lowerLeftOrigin, lowerLeftDest, x);
        if (lowerLeft != null) return lowerLeft;

        Coordinate upperRightOrigin = new Coordinate(origin.row, pivot.column);
        Coordinate upperRightDest = new Coordinate(pivot.row - 1, dest.column);
        return findElement(matrix, upperRightOrigin, upperRightDest, x);
    }

    static class Coordinate implements Cloneable {
        int row;
        int column;

        public Coordinate(int r, int c) {
            row = r;
            column = c;
        }

        public boolean inbounds(int[][] matrix) {
            return row >= 0 && column >= 0 &&
                   row < matrix.length && column < matrix[0].length;
        }

        public boolean isBefore(Coordinate p) {
            return row <= p.row && column <= p.column;
        }

        public Object clone() {
            return new Coordinate(row, column);
        }

        public void setToAverage(Coordinate min, Coordinate max) {
            row = (min.row + max.row) >>> 1;
            column = (min.column + max.column) >>> 1;
        }

        public static Coordinate middle(Coordinate min, Coordinate max) {
            return new Coordinate((min.row + max.row) >>> 1,
                                  (min.column + max.column) >>> 1);
        }

        @Override
        public String toString() {
            return "(" + row + "," + column + ")";
        }

        @Override
        public boolean equals(Object that) {
            if (!(that instanceof Coordinate)) return false;
            Coordinate other = (Coordinate)that;
            return (row == other.row) && (column == other.column);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    // time complexity: O(M + N)
    // beats 21.86%(12 ms for 129 tests)
    public boolean searchMatrix3(int[][] matrix, int target) {
        int m = matrix.length;
        if (m == 0) return false;

        int n = matrix[0].length;
        for (int i = m - 1, j = 0; i >= 0 && j < n; ) {
            if (target == matrix[i][j]) return true;

            if (target < matrix[i][j]) {
                i--;
            } else {
                j++;
            }
        }
        return false;
    }

    // Recursion + Divide & Conquer + Binary Search
    // beats 4.07%(39 ms for 127 tests)
    public boolean searchMatrix4(int[][] matrix, int target) {
        return search(matrix, target, new Coordinate(0, 0),
                      new Coordinate(matrix.length - 1, matrix[0].length - 1)) != null;
    }

    private Coordinate search(int[][] matrix, int x,
                              Coordinate start, Coordinate end) {
        if (!start.inbounds(matrix) || !end.inbounds(matrix)) return null;
        if (matrix[start.row][start.column] == x) return start;
        if (matrix[end.row][end.column] == x) return end;
        if (!start.isBefore(end)) return null;

        Coordinate minCorner = start;
        Coordinate maxCorner = end;
        while ((maxCorner.row - minCorner.row > 1)
               || (maxCorner.column - minCorner.column > 1)) {
            Coordinate mid = Coordinate.middle(minCorner, maxCorner);
            int midVal = matrix[mid.row][mid.column];
            if (x == midVal) return mid;
            if (x < midVal) {
                maxCorner = mid;
            } else {
                minCorner = mid;
            }
        }
        Coordinate lowerLeftStart = new Coordinate(maxCorner.row, start.column);
        Coordinate lowerLeftEnd = new Coordinate(end.row, maxCorner.column - 1);
        Coordinate pos = search(matrix, x, lowerLeftStart, lowerLeftEnd);
        if (pos != null) return pos;

        Coordinate upperRightStart = new Coordinate(start.row, minCorner.column + 1);
        Coordinate upperRightEnd = new Coordinate(minCorner.row, end.column);
        return search(matrix, x, upperRightStart, upperRightEnd);
    }

    // iterate row and binary search column
    // time complexity: O(M * log(N))

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(Function<int[][], Integer, Boolean> f, String name,
                      int[][] matrix, int x, boolean expected) {
        // long t1 = System.nanoTime();
        assertEquals(expected, f.apply(matrix, x));
        // System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    private void test(int[][] matrix, int[] tgts, int[] expected) {
        MatrixSearch2 m = new MatrixSearch2();
        for (int i = 0; i < tgts.length; i++) {
            test(m::searchMatrix, "searchMatrix", matrix, tgts[i], expected[i] > 0);
            test(m::searchMatrix2, "searchMatrix2", matrix, tgts[i], expected[i] > 0);
            test(m::searchMatrix3, "searchMatrix3", matrix, tgts[i], expected[i] > 0);
            test(m::searchMatrix4, "searchMatrix4", matrix, tgts[i], expected[i] > 0);
        }
    }

    @Test
    public void test1() {
        test(new int[][] {{1}}, new int[] {0}, new int[] {0});
        test(new int[][] {{5}, {6}}, new int[] {6}, new int[] {1});
        test(new int[][] {{1,   2,  3},
                          {4,   8,  9},
                          {7,  10, 12}},
             new int[] {1, 5, 10, 11, 12},
             new int[] {1, 0,  1,  0,  1});
        test(new int[][] {{1,   2,  3, 5},
                          {4,   8,  9, 10},
                          {7,  10, 12, 15}},
             new int[] {1, 5, 10, 11, 12, 15, 16},
             new int[] {1, 1,  1,  0,  1, 1, 0});
        test(new int[][] {{1,   4,  7,  11, 15},
                          {2,   5,  8,  12, 19},
                          {3,   6,  9,  16, 22},
                          {10, 13, 14,  17, 24},
                          {18, 21, 23,  26, 30}},
             new int[] {5, 20, 9, 74, 19, 21, 23, 24, 26, 51},
             new int[] {1, 0,  1,  0,  1,  1,  1,  1, 1, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
