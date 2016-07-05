import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/maximal-square/
//
// Given a 2D binary matrix filled with 0's and 1's, find the largest square
// containing all 1's and return its area.
public class MaxSquare {
    // beats 83.29%(10 ms)
    public int maximalSquare(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int[] oneCounts = new int[n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m && matrix[i][j] == '1'; i++) {
                oneCounts[j]++;
            }
        }
        int max = maxSide(oneCounts);
        for (int i = 1; i < m; i++) {
            boolean hasZero = false; // skip all 1's when possible
            for (int j = 0; j < n; j++) {
                if (oneCounts[j] > 0) {
                    oneCounts[j]--;
                } else {
                    hasZero = true;
                    for (int k = i; k < m && matrix[k][j] == '1'; k++) {
                        oneCounts[j]++;
                    }
                }
            }
            if (hasZero) {
                max = Math.max(max, maxSide(oneCounts));
            }
        }
        return max * max;
    }

    private int maxSide(int[] heights) {
        int max = 0;
        int n = heights.length;
        int front = 0;
        int[] heights2 = heights.clone();
        for (int i = 1; i < n; i++) {
            if (i - front >= heights2[front]) {
                max = Math.max(max, heights2[front]);
                if (++front == n) return max;

                i--;
                continue;
            }

            int cur = heights[i];
            if (heights2[i - 1] > cur) {
                max = Math.max(max, i - front);
                for (int j = i - 1; j >= front; j--) {
                    if (heights2[j] > cur) {
                        heights2[j] = cur;
                    } else break;
                }
            }
        }
        return Math.max(max, Math.min(n - front, heights2[front]));
    }

    public void testMaxSide(int expected, int... nums) {
        assertEquals(expected, maxSide(nums));
    }

    @Test
    public void testMaxSide() {
        testMaxSide(2, 4, 0, 3, 4, 0);
        testMaxSide(1, 4, 0, 3, 0, 0);
        testMaxSide(4, 0, 2, 3, 6, 4, 5, 4);
        testMaxSide(3, 0, 2, 3, 6, 4, 5);
        testMaxSide(2, 2, 3, 6, 1, 5);
        testMaxSide(2, 2, 3, 1, 1, 5);
        testMaxSide(3, 8, 5, 6, 1, 5, 2);
    }

    void test(int expected, String ... matrixStr) {
        char[][] matrix =  new char[matrixStr.length][];
        for (int i = 0; i < matrixStr.length; i++) {
            matrix[i] = matrixStr[i].toCharArray();
        }
        assertEquals(expected, maximalSquare(matrix));
    }

    @Test
    public void test1() {
        test(4, "10100", "10111", "11111", "10010");
        test(4, "10110", "10111", "11111", "10010");
        test(9, "10111", "10111", "11111", "10010");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSquare");
    }
}
