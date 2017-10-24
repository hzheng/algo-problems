import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC546: https://leetcode.com/problems/remove-boxes/
//
// Given several boxes with different colors represented by different positive 
// numbers. You may experience several rounds to remove boxes until there is no 
// box left. Each time you can choose some continuous boxes with the same color 
// (composed of k boxes, k >= 1), remove them and get k*k points. Find the 
// maximum points.
public class RemoveBoxes {
    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 4), space complexity: O(N ^ 3)
    // beats 52.87%(50 ms for 60 tests)
    public int removeBoxes(int[] boxes) {
        int n = boxes.length;
        if (n == 0) return 0;

        List<Box> boxList = new ArrayList<>();
        Box lastBox = new Box(boxes[0], 1);
        boxList.add(lastBox);
        for (int i = 1; i < n; i++) {
            if (boxes[i] == boxes[i - 1]) {
                lastBox.count++;
            } else {
                lastBox = new Box(boxes[i], 1);
                boxList.add(lastBox);
            }
        }
        int m = boxList.size();
        return maxPoint(boxList, 0, m, 0, new int[m + 1][m + 1][n + 1]);
    }

    private int maxPoint(List<Box> boxList, int start, int end, int presum, int[][][] dp) {
        if (start >= end) return 0;
        if (dp[start][end][presum] != 0) return dp[start][end][presum];

        Box curBox = boxList.get(start);
        int sum = presum + curBox.count;
        int res = maxPoint(boxList, start + 1, end, 0, dp) + sum * sum;
        for (int i = start + 1; i < end; i++) {
            if (boxList.get(i).color == curBox.color) {
                res = Math.max(res, maxPoint(boxList, start + 1, i, 0, dp)
                               + maxPoint(boxList, i, end, sum, dp));
            }
        }
        return dp[start][end][presum] = res;
    }

    private static class Box {
        int color;
        int count;
        Box(int color, int count) {
            this.color = color;
            this.count = count;
        }
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 4), space complexity: O(N ^ 3)
    // beats 17.62(166 ms for 60 tests)
    public int removeBoxes2(int[] boxes) {
        int n = boxes.length;
        // int[][][] dp = new int[n][n][n];
        return maxPoint(boxes, 0, n - 1, 0, new int[n][n][n]);
    }

    private int maxPoint(int[] boxes, int start, int end, int count, int[][][] dp) {
        if (start > end) return 0;
        if (dp[start][end][count] != 0) return dp[start][end][count];

        int res = maxPoint(boxes, start + 1, end, 0, dp) + (count + 1) * (count + 1);
        for (int i = start + 1; i <= end; i++) {
            if (boxes[start] == boxes[i]) {
                res = Math.max(res, maxPoint(boxes, start + 1, i - 1, 0, dp)
                               + maxPoint(boxes, i, end, count + 1, dp));
            }
        }
        return dp[start][end][count] = res;
    }

    // Dynamic Programming(Botttom-Up)
    // time complexity: O(N ^ 4), space complexity: O(N ^ 3)
    // beats 0.41%(458 ms for 60 tests)
    public int removeBoxes3(int[] boxes) {
        int n = boxes.length;
        int[][][] dp = new int[n][n][n + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                for (int k = n - 1; k >= 0; k--) {
                    int max = (i == j ? 0 : dp[i + 1][j][0]) + (k + 1) * (k + 1);
                    for (int m = i + 1; m <= j; m++) {
                        if (boxes[i] != boxes[m]) continue;

                        max = Math.max(max, dp[i + 1][m - 1][0] + dp[m][j][k + 1]);
                    }
                    dp[i][j][k] = max;
                }
            }
        }
        return dp[0][n - 1][0];
    }

    // Dynamic Programming(Botttom-Up)
    // time complexity: O(N ^ 4), space complexity: O(N ^ 2)
    // beats 50.00%(75 ms for 60 tests)
    public int removeBoxes4(int[] boxes) {
        int n = boxes.length;
        int[][] scores = new int[n][n];
        int[][] accumulated = new int[n][n + 1];
        for(int i = n - 1; i >= 0; i--) {
            for (int[] a : accumulated) {
                Arrays.fill(a, -1);
            }
            accumulated[i][0] = 1;
            accumulated[i][1] = 0;
            scores[i][i] = 1;
            for (int j = i; j + 1 < n; j++) {
                for (int k = 0; k <= j - i + 1; k++) {
                    if (accumulated[j][k] < 0) continue;
                    if (boxes[j + 1] == boxes[i]) {
                        accumulated[j + 1][k + 1] = Math.max(accumulated[j + 1][k + 1], accumulated[j][k]);
                    }
                    for (int m = j + 1; m < n; m++) {
                        accumulated[m][k] = Math.max(accumulated[m][k],
                                                     accumulated[j][k] + scores[j + 1][m]);
                    }
                }
            }
            for (int j = i; j < n; j++) {
                for (int k = 0; k <= j - i + 1; k++) {
                    if (accumulated[j][k] >= 0) {
                        scores[i][j] = Math.max(scores[i][j], accumulated[j][k] + k * k);
                    }
                }
            }
        }
        return scores[0][n - 1];
    }

    void test(int[] boxes, int expected) {
        assertEquals(expected, removeBoxes(boxes));
        assertEquals(expected, removeBoxes2(boxes));
        assertEquals(expected, removeBoxes3(boxes));
        assertEquals(expected, removeBoxes4(boxes));
    }

    @Test
    public void test() {
        test(new int[] {1, 3, 2, 2, 2, 3, 4, 3, 1}, 23);
        test(new int[] {1, 1, 1}, 9);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
