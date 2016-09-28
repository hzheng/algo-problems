import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC085: https://leetcode.com/problems/maximal-rectangle/
//
// Given a 2D binary matrix filled with 0's and 1's, find the largest rectangle
// containing all ones and return its area.
public class MaxRect {
    // Stack
    // beats 54.04%(30 ms)
    // time complexity: O(M * N), space complexity: O(N)
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

    // Solution of Choice
    // Stack
    // beats 26.54%(37 ms)
    public int maximalRectangle2(char[][] matrix) {
        if (matrix.length == 0) return 0;

        int max = 0;
        int n = matrix[0].length;
        int[] heights = new int[n + 1]; // append a dummy element
        for (char[] row : matrix) {
            Stack<Integer> stack = new Stack<>();
            for (int i = 0; i <= n; i++) {
                if (i < n) {
                    heights[i] = (row[i] == '1') ? heights[i] + 1 : 0;
                }
                while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                    int height = heights[stack.pop()];
                    int width = stack.empty() ? i : i - stack.peek() - 1;
                    max = Math.max(max, width * height);
                }
                stack.push(i);
            }
        }
        return max;
    }

    // Dynamic Programming
    // http://www.sigmainfy.com/blog/leetcode-maximal-rectangle.html
    // time complexity: O(M * M * N), space complexity: O(M * N)
    // beats 58.08%(29 ms)
    public int maximalRectangle3(char[][] matrix) {
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

    // Solution of Choice
    // Dynamic Programming
    // https://discuss.leetcode.com/topic/6650/share-my-dp-solution
    // time complexity: O(M * N), space complexity: O(N)
    // beats 92.12%(9 ms)
    public int maximalRectangle4(char[][] matrix) {
        if (matrix.length == 0) return 0;

        int n = matrix[0].length;
        int[] left = new int[n]; // max distance (inclusive) to leftmost 1
        int[] right = new int[n]; // max distance (inclusive) to rightmost 1
        int[] height = new int[n]; // max distance (inclusive) to topmost 1
        Arrays.fill(right, n);
        int max = 0;
        for (char[] row : matrix) {
            for (int i = n - 1, curRight = n; i >= 0; i--) {
                // calculate right
                if (row[i] == '1') {
                    right[i] = Math.min(right[i], curRight);
                } else {
                    right[i] = n;
                    curRight = i;
                }
            }
            for (int i = 0, curLeft = 0; i < n; i++) {
                // calculate left
                if (row[i] == '1') {
                    left[i] = Math.max(left[i], curLeft);
                } else {
                    left[i] = 0;
                    curLeft = i + 1;
                }
                // calculate height
                height[i] = (row[i] == '1') ? height[i] + 1 : 0;
                // calculate max
                max = Math.max(max, (right[i] - left[i]) * height[i]);
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
        assertEquals(expected, maximalRectangle3(matrix));
        assertEquals(expected, maximalRectangle4(matrix));
    }

    @Test
    public void test1() {
        test(6, "0001000", "0011100", "0111110");
        test(6, "10100", "10111", "11111", "10010");
        test(6, "00110", "10110", "10111", "11011");
        test(9, "00110", "11110", "11101", "11111");
        test(9, "00110", "11111", "11101", "11111");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxRect");
    }
}
