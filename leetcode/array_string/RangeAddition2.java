import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC598: https://leetcode.com/problems/range-addition-ii/
//
// Given an m * n matrix M initialized with all 0's and several update operations.
// Operations are represented by a 2D array, and each operation is represented by
// an array with two positive integers a and b, which means M[i][j] should be added
// by one for all 0 <= i < a and 0 <= j < b. You need to count and return the number
// of maximum integers in the matrix after performing all the operations.
public class RangeAddition2 {
    // beats 75.14%(6 ms for 69 tests)
    public int maxCount(int m, int n, int[][] ops) {
        int minCol = m;
        int minRow = n;
        for (int[] op : ops) {
            minCol = Math.min(minCol, op[0]);
            minRow = Math.min(minRow, op[1]);
        }
        return minCol * minRow;
    }

    void test(int m, int n, int[][] ops, int expected) {
        assertEquals(expected, maxCount(m, n, ops));
    }

    @Test
    public void test() {
        test(3, 3, new int[][] {{2, 2}, {3, 3}}, 4);
        test(3, 3, new int[][] {}, 9);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
