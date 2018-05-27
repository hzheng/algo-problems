import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC813: https://leetcode.com/problems/largest-sum-of-averages/
//
// We partition a row of numbers A into at most K adjacent (non-empty) groups,
// then our score is the sum of the average of each group. What is the largest
// score we can achieve?
// Note:
// 1 <= A.length <= 100.
// 1 <= A[i] <= 10000.
// 1 <= K <= A.length.
public class LargestSumOfAverages {
    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2 * K), space complexity: O(N * K)
    // beats %(18 ms for 51 tests)
    public double largestSumOfAverages(int[] A, int K) {
        int n = A.length;
        double[][] dp = new double[n + 1][K + 1];
        for (double[] a : dp) {
            Arrays.fill(a, -2);
        }
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < K; k++) {
                if (dp[i][k] < -1) continue;

                double sum = 0;
                for (int j = i; j < n; j++) {
                    sum += A[j];
                    dp[j + 1][k + 1] = Math.max(dp[j + 1][k + 1],
                                                dp[i][k] + sum / (j - i + 1));
                }
            }
        }
        return dp[n][K];
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 2 * K), space complexity: O(N)
    // beats %(17 ms for 51 tests)
    public double largestSumOfAverages2(int[] A, int K) {
        int n = A.length;
        double[] P = new double[n + 1];
        for (int i = 0; i < n; i++) {
            P[i + 1] = P[i] + A[i];
        }
        double[] dp = new double[n]; // partition A[i:] into at most K parts
        for (int i = 0; i < n; i++) {
            dp[i] = (P[n] - P[i]) / (n - i);
        }
        for (int k = K; k > 1; k--) {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    dp[i] = Math.max(dp[i], (P[j] - P[i]) / (j - i) + dp[j]);
                }
            }
        }
        return dp[0];
    }

    // Dynamic Programming(Top-Down) + DFS/Recursion
    // time complexity: O(N ^ 2 * K), space complexity: O(N * K)
    // beats %(12 ms for 51 tests)
    public double largestSumOfAverages3(int[] A, int K) {
        int n = A.length;
        double[][] dp = new double[n + 1][K + 1]; // partition A[0:i]
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += A[i];
            dp[i + 1][1] = sum / (i + 1);
        }
        return dfs(n, K, A, dp);
    }

    public double dfs(int n, int k, int[] A, double[][] dp) {
        if (n < k) return 0;
        if (dp[n][k] > 0) return dp[n][k];

        double sum = 0;
        for (int i = n - 1; i > 0; i--) {
            sum += A[i];
            dp[n][k] = Math.max(dp[n][k], dfs(i, k - 1, A, dp) + sum / (n - i));
        }
        return dp[n][k];
    }

    void test(int[] A, int K, double expected) {
        assertEquals(expected, largestSumOfAverages(A, K), 1E-6);
        assertEquals(expected, largestSumOfAverages2(A, K), 1E-6);
        assertEquals(expected, largestSumOfAverages3(A, K), 1E-6);
    }

    @Test
    public void test() {
        test(new int[] { 9, 1, 2, 3, 9 }, 3, 20);
        test(new int[] { 5870, 2722, 6249, 2196, 8717 }, 4, 21531.5);
        test(new int[] { 7514, 4339, 1236, 5273, 9525, 9930, 5151 }, 7, 42968);
        test(new int[] { 5756, 4962, 5892, 6037, 9587, 4150 }, 4,
             25310.666666667);
        test(new int[] { 4663, 3020, 7789, 1627, 9668, 1356, 4207, 1133, 8765,
                         4649, 205, 6455, 8864, 3554, 3916, 5925,
                         3995, 4540, 3487, 5444, 8259, 8802, 6777, 7306, 989,
                         4958, 2921, 8155, 4922, 2469, 6923, 776, 9777,
                         1796, 708, 786, 3158, 7369, 8715, 2136, 2510, 3739,
                         6411, 7996, 6211, 8282, 4805, 236, 1489, 7698 }, 27,
             167436.0833333);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
