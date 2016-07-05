import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/maximal-rectangle/
//
// Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle
// containing all ones and return its area.
public class MaxRect {
    // beats 41.87%
    // time complexity: O(M * N)
    public int maximalRectangle(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int[] oneCounts = new int[n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m && matrix[i][j] == '1'; i++) {
                oneCounts[j]++;
            }
        }
        int max = maxArea(oneCounts);
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
                max = Math.max(max, maxArea(oneCounts));
            }
        }
        return max;
    }

    private int maxArea(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int max = 0;
        for (int i = 0; i <= heights.length; ) {
            if (stack.empty()
                || i < heights.length && heights[stack.peek()] < heights[i]) {
                stack.push(i++);
            } else {
                int last = stack.pop();
                int width = stack.empty() ? i : i - stack.peek() - 1;
                max = Math.max(max, width * heights[last]);
            }
        }
        return max;
    }

    // http://www.sigmainfy.com/blog/leetcode-maximal-rectangle.html
    // time complexity: O(M * M * N)
    // beats 41.87%
    public int maximalRectangle2(char[][] matrix) {
        int m = matrix.length;
        if (m == 0) return 0;

        int n = matrix[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            dp[i][0] = matrix[i][0] - '0';
            for (int j = 1; j < n; j++) {
                dp[i][j] = (matrix[i][j] == '0') ? 0 : 1 + dp[i][j - 1];
            }
        }

        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int oneCounts = dp[i][j];
                if (oneCounts == 0) continue;

                for (int k = i; k < m; k++) {
                    oneCounts = Math.min(oneCounts, dp[k][j]);
                    max = Math.max(max, (k - i + 1) * oneCounts);
                }
            }
        }
        return max;
    }

    // http://www.geeksforgeeks.org/maximum-size-sub-matrix-with-all-1s-in-a-binary-matrix/
    // only for sub-squrare matrix

    // https://www.youtube.com/watch?v=g8bSdXCG-lA
    // also histogram DP

    void test(int expected, String ... matrixStr) {
        char[][] matrix =  new char[matrixStr.length][];
        for (int i = 0; i < matrixStr.length; i++) {
            matrix[i] = matrixStr[i].toCharArray();
        }
        assertEquals(expected, maximalRectangle(matrix));
        assertEquals(expected, maximalRectangle2(matrix));
    }

    @Test
    public void test1() {
        test(6, "00110", "10110", "10111", "11011");
        test(9, "00110", "11110", "11101", "11111");
        test(9, "00110", "11111", "11101", "11111");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxRect");
    }
}
