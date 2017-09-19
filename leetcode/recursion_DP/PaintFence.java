import org.junit.Test;
import static org.junit.Assert.*;

// LC276: https://leetcode.com/problems/paint-fence/
//
// There is a fence with n posts, each post can be painted with one of the k
// colors. You have to paint all the posts such that no more than two adjacent
// fence posts have the same color.
// Return the total number of ways you can paint the fence.
public class PaintFence {
    // Dynamic Programming
    // time complexity: O(N * K ^ 2), space complexity: O(N * K)
    // Time Limit Exceeded
    public int numWays1(int n, int k) {
        if (k == 0 || n == 0) return 0;

        int[][] dp1 = new int[n][k];
        int[][] dp2 = new int[n][k];
        for (int i = 0; i < k; i++) {
            dp1[0][i] = 1;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < k; j++) {
                for (int m = 0; m < k; m++) {
                    if (m != j) {
                        dp1[i][j] += dp1[i - 1][m];
                        dp1[i][j] += dp2[i - 1][m];
                    }
                }
                dp2[i][j] = dp1[i - 1][j];
            }
        }
        int count = 0;
        for (int i = 0; i < k; i++) {
            count += dp1[n - 1][i];
            count += dp2[n - 1][i];
        }
        return count;
    }

    // Dynamic Programming
    // time complexity: O(N * K ^ 2), space complexity: O(K)
    // Time Limit Exceeded
    public int numWays1_2(int n, int k) {
        if (k == 0 || n == 0) return 0;

        int[] dp1 = new int[k];
        int[] dp2 = new int[k];
        for (int i = 0; i < k; i++) {
            dp1[i] = 1;
        }
        for (int i = 1; i < n; i++) {
            int[] total = new int[k];
            for (int j = 0; j < k; j++) {
                for (int m = 0; m < k; m++) {
                    if (m != j) {
                        total[j] += dp1[m];
                        total[j] += dp2[m];
                    }
                }
            }
            for (int j = 0; j < k; j++) {
                dp2[j] = dp1[j];
                dp1[j] = total[j];
            }
        }
        int count = 0;
        for (int i = 0; i < k; i++) {
            count += dp1[i];
            count += dp2[i];
        }
        return count;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats 13.18%(0 ms for 79 tests)
    public int numWays(int n, int k) {
        if (k == 0 || n == 0) return 0;

        int dp1 = 1;
        int dp2 = 0;
        for (int i = 1; i < n; i++) {
            int total = (dp1 + dp2) * (k - 1);
            dp2 = dp1;
            dp1 = total;
        }
        return (dp1 + dp2) * k;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats 13.18%(0 ms for 79 tests)
    public int numWays2(int n, int k) {
        if (k == 0 || n == 0) return 0;

        int diffColors = k; // first two: k * (k - 1)
        int sameColors = 0; // first two: k
        for (int i = 1; i < n; i++) {
            int total = (diffColors + sameColors) * (k - 1);
            sameColors = diffColors;
            diffColors = total;
        }
        return diffColors + sameColors;
    }

    // Math
    // https://en.wikipedia.org/wiki/Lucas_sequence#Distinct_roots
    // time complexity: O(1), space complexity: O(1)
    // beats 13.18%(0 ms for 79 tests)
    public int numWays3(int n, int k) {
        if (k == 0 || n == 0) return 0;
        if (k == 1) return n < 3 ? 1 : 0;

        // Lucas sequences Xn = P * Xn−1 − Q * Xn−2,
        // where P = k-1 and Q = -(k-1).
        int p = k - 1;
        double root = Math.sqrt(p) * Math.sqrt(p + 4); // sqrt separately to avoid overflow
        return (int)(Math.pow((p + root) / 2.0, n + 1) / root * k / p + 0.5);
    }

    void test(int n, int k, int expected) {
        if (n + k < 1000) {
            assertEquals(expected, numWays1(n, k));
            assertEquals(expected, numWays1_2(n, k));
        }
        assertEquals(expected, numWays(n, k));
        assertEquals(expected, numWays2(n, k));
        assertEquals(expected, numWays3(n, k));
    }

    @Test
    public void test() {
        test(0, 0, 0);
        test(1, 1, 1);
        test(0, 1, 0);
        test(1, 0, 0);
        test(1, 2, 2);
        test(3, 2, 6);
        test(4, 2, 10);
        test(5, 3, 180);
        test(2, 4634, 21473956);
        test(2, 16349, 267289801);
        test(2, 46340, 2147395600);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PaintFence");
    }
}
