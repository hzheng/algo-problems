import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC868: https://leetcode.com/problems/transpose-matrix/
//
// Given a matrix A, return the transpose of A.
public class TransposeMatrix {
    // beats %(2 ms for 36 tests)
    public int[][] transpose(int[][] A) {
        int n = A.length;
        int m = A[0].length;
        int[][] res = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                res[i][j] = A[j][i];
            }
        }
        return res;
    }

    void test(int[][] A, int[][] expected) {
        assertArrayEquals(expected, transpose(A));
    }

    @Test
    public void test() {
        test(new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } },
             new int[][] { { 1, 4, 7 }, { 2, 5, 8 }, { 3, 6, 9 } });
        test(new int[][] { { 1, 2, 3 }, { 4, 5, 6 } }, 
             new int[][] { { 1, 4 }, { 2, 5 }, { 3, 6 } });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
