import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 11.6:
 * Given a matrix in which each row and each column is sorted,
 * write a method to find an element.
 */
public class MatrixSearch {
    static class Coordinate implements Cloneable {
        int row;
        int column;

        public Coordinate(int r, int c) {
            row = r;
            column = c;
        }

        public boolean inbounds(int[][] matrix) {
            return row >= 0 &&
                   column >= 0 &&
                   row < matrix.length &&
                   column < matrix[0].length;
        }

        public boolean isBefore(Coordinate p) {
            return row <= p.row && column <= p.column;
        }

        public Object clone() {
            return new Coordinate(row, column);
        }

        public void setToAverage(Coordinate min, Coordinate max) {
            row = (min.row + max.row) / 2;
            column = (min.column + max.column) / 2;
        }

        public static Coordinate middle(Coordinate min, Coordinate max) {
            return new Coordinate((min.row + max.row) / 2,
                                  (min.column + max.column) / 2);
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

    public static Coordinate search(int[][] matrix, int x) {
        return search(matrix, x, new Coordinate(0, 0),
                      new Coordinate(matrix.length - 1, matrix[0].length - 1));
    }

    private static Coordinate search(int[][] matrix, int x,
                                     Coordinate start, Coordinate end) {
        if (!start.inbounds(matrix) || !end.inbounds(matrix)) return null;
        int startVal = matrix[start.row][start.column];
        if (startVal == x) return start;

        int endVal = matrix[end.row][end.column];
        if (endVal == x) return end;

        if ((startVal > x) || (endVal < x) || !start.isBefore(end)) return null;

        Coordinate minCorner = start;
        Coordinate maxCorner = end;
        // while (minCorner.isBefore(maxCorner)) {
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

    // From the book
    public static Coordinate findElement(int[][] matrix, int x) {
        Coordinate origin = new Coordinate(0, 0);
        Coordinate dest = new Coordinate(matrix.length - 1, matrix[0].length - 1);
        return findElement(matrix, origin, dest, x);
    }

    private static Coordinate findElement(int[][] matrix, Coordinate origin, Coordinate dest, int x) {
        if (!origin.inbounds(matrix) || !dest.inbounds(matrix)) {
            return null;
        }
        if (matrix[origin.row][origin.column] == x) {
            return origin;
        } else if (!origin.isBefore(dest)) {
            return null;
        }

        /* Set start to start of diagonal and end to the end of the diagonal. Since
         * the grid may not be square, the end of the diagonal may not equal dest.
         */
        Coordinate start = (Coordinate)origin.clone();
        int diagDist = Math.min(dest.row - origin.row, dest.column - origin.column);
        Coordinate end = new Coordinate(start.row + diagDist, start.column + diagDist);
        Coordinate p = new Coordinate(0, 0);

        /* binary search on the diagonal, looking for the first element greater than x */
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

        /* Split the grid into quadrants. Search the bottom left and the top right. */
        return partitionAndSearch(matrix, origin, dest, start, x);
    }

    private static Coordinate partitionAndSearch(int[][] matrix,
                                                 Coordinate origin,
                                                 Coordinate dest,
                                                 Coordinate pivot, int elem) {
        Coordinate lowerLeftOrigin = new Coordinate(pivot.row, origin.column);
        Coordinate lowerLeftDest = new Coordinate(dest.row, pivot.column - 1);
        Coordinate upperRightOrigin = new Coordinate(origin.row, pivot.column);
        Coordinate upperRightDest = new Coordinate(pivot.row - 1, dest.column);

        Coordinate lowerLeft = findElement(matrix, lowerLeftOrigin, lowerLeftDest, elem);
        if (lowerLeft == null) {
            return findElement(matrix, upperRightOrigin, upperRightDest, elem);
        }
        return lowerLeft;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private Coordinate test(Function<int[][], Integer, Coordinate> f, String name,
    int[][] matrix, int x) {
        long t1 = System.nanoTime();
        Coordinate p = f.apply(matrix, x);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return p;
    }

    private void test(int[][] matrix, int[] tgts) {
        for (int i : tgts) {
            Coordinate p1 = test(MatrixSearch::search, "search", matrix, i);
            Coordinate p2 = test(MatrixSearch::findElement, "findElement", matrix, i);
            System.out.println("found " + i + " at " + p1 + " and " + p2);
            assertEquals(p1, p2);
        }
    }

    @Test
    public void test1() {
        test(new int[][] {{15, 30,  50,  70,  73},
                          {35, 40, 100, 102, 120},
                          {36, 42, 105, 110, 125},
                          {46, 51, 106, 111, 130},
                          {48, 55, 109, 140, 150}},
             new int[] {106, 30, 55, 73, 109, 48, 10, 151});
        test(new int[][] {{15, 30,  50,  70,  73, 78},
                          {35, 40, 100, 102, 120, 125},
                          {36, 42, 105, 110, 125, 128},
                          {46, 51, 106, 111, 130, 140},
                          {48, 55, 109, 140, 150, 160}},
             new int[] {106, 30, 55, 73, 109, 48, 160, 51, 125});
        test(new int[][] {{15, 30,  50,  70,  73},
                          {36, 42, 105, 110, 125},
                          {46, 51, 106, 111, 130},
                          {48, 55, 109, 140, 150}},
             new int[] {106, 30, 55, 73, 109, 48, 10, 151});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MatrixSearch");
    }
}
