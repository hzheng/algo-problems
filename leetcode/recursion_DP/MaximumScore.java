import org.junit.Test;

import static org.junit.Assert.*;

// LC1770: https://leetcode.com/problems/maximum-score-from-performing-multiplication-operations/
//
// You are given two integer arrays nums and multipliers of size n and m respectively, where n >= m.
// The arrays are 1-indexed. You begin with a score of 0. You want to perform exactly m operations.
// On the ith operation (1-indexed), you will:
// Choose one integer x from either the start or the end of the array nums.
// Add multipliers[i] * x to your score.
// Remove x from the array nums.
// Return the maximum score after performing m operations.
//
// Constraints:
// n == nums.length
// m == multipliers.length
// 1 <= m <= 10^3
// m <= n <= 10^5
// -1000 <= nums[i], multipliers[i] <= 1000
public class MaximumScore {
    // Recursion + 2-D Dynamic Programming(Top-Down)
    // time complexity: O(M^2), space complexity: O(M^2)
    // 150 ms(16.67%), 66.6 MB(33.33%) for 21 tests
    public int maximumScore(int[] nums, int[] multipliers) {
        int m = multipliers.length;
        return dfs(nums, multipliers, 0, 0, new Integer[m][m]);
    }

    private int dfs(int[] nums, int[] multipliers, int head, int tail, Integer[][] dp) {
        int i = head + tail;
        if (i >= multipliers.length) { return 0; }
        if (dp[head][tail] != null) { return dp[head][tail]; }

        int score1 = nums[head] * multipliers[i] + dfs(nums, multipliers, head + 1, tail, dp);
        int score2 = nums[nums.length - 1 - tail] * multipliers[i] + dfs(nums, multipliers, head,
                                                                         tail + 1, dp);
        return dp[head][tail] = Math.max(score1, score2);
    }

    // time complexity: O(M^2), space complexity: O(M^2)
    // 45 ms(100.00%), 49.7 MB(100.00%) for 21 tests
    public int maximumScore2(int[] nums, int[] multipliers) {
        int m = multipliers.length;
        int n = nums.length;
        int[][] dp = new int[m + 1][m + 1];
        int res = Integer.MIN_VALUE;
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= m - i; j++) {
                if (i + j == 0) { continue; }

                dp[i][j] = Math.max(i > 0 ? dp[i - 1][j] + nums[i - 1] * multipliers[i + j - 1] :
                                    Integer.MIN_VALUE,
                                    j > 0 ? dp[i][j - 1] + nums[n - j] * multipliers[i + j - 1] :
                                    Integer.MIN_VALUE);
            }
            res = Math.max(res, dp[i][m - i]);
        }
        return res;
    }

    // Solution of Choice
    // time complexity: O(M^2), space complexity: O(M^2)
    // 43 ms(100.00%), 49.7 MB(100.00%) for 21 tests
    public int maximumScore3(int[] nums, int[] multipliers) {
        int m = multipliers.length;
        int n = nums.length;
        int[][] dp = new int[m + 1][m + 1];
        for (int i = m - 1; i >= 0; i--) {
            for (int j = m - i - 1; j >= 0; j--) {
                dp[i][j] = Math.max(dp[i + 1][j] + nums[i] * multipliers[i + j],
                                    dp[i][j + 1] + nums[n - 1 - j] * multipliers[i + j]);
            }
        }
        return dp[0][0];
    }

    private void test(int[] nums, int[] multipliers, int expected) {
        assertEquals(expected, maximumScore(nums, multipliers));
        assertEquals(expected, maximumScore2(nums, multipliers));
        assertEquals(expected, maximumScore3(nums, multipliers));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3}, new int[] {3, 2, 1}, 14);
        test(new int[] {-5, -3, -3, -2, 7, 1}, new int[] {-10, -5, 3, 4, 6}, 102);
        test(new int[] {-854, -941, 10, 299, 995, -346, 294, -393, 351, -76, 210, 897, -651, 920,
                        624, 969, -629, 985, -695, 236, 637, -901, -817, 546, -69, 192, -377, 251,
                        542, -316, -879, -764, -560, 927, 629, 877, 42, 381, 367, -549, 602, 139,
                        -312, -281, 105, 690, -376, -705, -906, 85, -608, 639, 752, 770, -139, -601,
                        341, 61, 969, 276, 176, -715, -545, 471, -170, -126, 596, -737, 130},
             new int[] {83, 315, -442, -714, 461, 920, -737, -93, -818, -760, 558, -584, -358, -228,
                        -220}, 3040819);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
