import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.9:
 * Primt all ways of arranging 8 queens on an 8x8 chess board so that none of
 * them share the same row, column or diagonal.
 */
public class EightQueens {
    public static final int SIZE = 8;

    public static List<int[]> placeQueens() {
        List<int[]> result = new ArrayList<int[]>();
        placeQueens(result, new int[SIZE], 0);
        return result;
    }

    private static void placeQueens(List<int[]> results, int[] occupiedCols, int row) {
        if (row == SIZE) {
            results.add(occupiedCols.clone());
            return;
        }

        // check all columns on the row-th row
        for (int col = 0; col < SIZE; col++) {
            if (isSafe(row, col, occupiedCols)) {
                occupiedCols[row] = col;
                placeQueens(results, occupiedCols, row + 1);
            }
        }
    }

    private static boolean isSafe(int row1, int col1, int[] occupiedCols) {
        for (int row2 = 0; row2 < row1; row2++) {
            int col2 = occupiedCols[row2];
            if (col1 == col2) return false;
            if ((row1 - row2) == Math.abs(col2 - col1)) {
                return false;
            }
        }
        return true;
    }

    @FunctionalInterface
    interface Function<A> {
        public A apply();
    }

    private void test(Function<List<int[]> > placeQueens) {
        List<int[]> results = placeQueens.apply();
        for (int[] result : results) {
            for (int i = 0; i < result.length; i++) {
                System.out.format("(%d, %d) ", i, result[i]);
            }
            System.out.println();
        }
        System.out.println("total solutions: " + results.size());
    }

    @Test
    public void test1() {
        test(EightQueens::placeQueens);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EightQueens");
    }
}
