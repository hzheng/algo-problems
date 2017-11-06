import org.junit.Test;
import static org.junit.Assert.*;

// LC629: https://leetcode.com/problems/k-inverse-pairs-array/
//
// Given two integers n and k, find how many different arrays consist of numbers
// from 1 to n such that there are exactly k inverse pairs.
// We define an inverse pair as following: For ith and jth element in the array,
// if i < j and a[i] > a[j] then it's an inverse pair; Otherwise, it's not.
// Since the answer may be very large, the answer should be modulo 10 ^ 9 + 7.
// Note:
// The integer n is in the range [1, 1000] and k is in the range [0, 1000].
public class KInversePairs {
    private static final int MOD = 1000_000_000 + 7;

    // 2-D Dynamic Programming
    // time complexity: O(n * k), space complexity: O(n * k)
    // beats 50.66%(49 ms for 80 tests)
    public int kInversePairs(int n, int k) {
        int max = n * (n - 1) / 2;
        if (k == 0 || k == max) return 1;
        if (k > max) return 0;

        int[][] dp = new int[n + 1][k + 1];
        // a(n,k)=a(n-1,k)+a(n-1,k-1)+a(n-1,k-2)+...+a(n-1,k+1-n+1)+a(n-1,k-n+1)
        // a(n,k)=a(n,k-1)+a(n-1,k)-a(n-1,k-n)
        for (int i = 2; i <= n; i++) {
            dp[i][0] = 1;
            for (int j = 1; j <= Math.min(k, i * (i - 1) / 2); j++) {
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j];
                if (j >= i) {
                    dp[i][j] -= dp[i - 1][j - i];
                }
                dp[i][j] = (int)(((long)dp[i][j] + MOD) % MOD);
            }
        }
        return dp[n][k];
    }

    // 1-D Dynamic Programming
    // time complexity: O(n * k), space complexity: O(k)
    // beats 69.16%(42 ms for 80 tests)
    public int kInversePairs2(int n, int k) {
        int[] dp = new int[k + 1];
        for (int i = 1; i <= n; i++) {
            int[] buf = new int[k + 1];
            buf[0] = 1;
            for (int j = 1, max = Math.min(k, i * (i - 1) / 2); j <= max; j++) {
                buf[j] = buf[j - 1] + dp[j] - ((j < i) ? 0 : dp[j - i]);
                buf[j] = (int)(((long)buf[j] + MOD) % MOD);
            }
            dp = buf;
        }
        return dp[k];
    }

    // Recursion + Dynamic Programming(Top-Down)
    // https://leetcode.com/problems/k-inverse-pairs-array/solution/ #6
    // time complexity: O(n * k), space complexity: O(n * k)
    // beats 59.47%(45 ms for 80 tests)
    public int kInversePairs3(int n, int k) {
        if (k == 0) return 1;

        Integer[][] dp = new Integer[n + 1][k + 1];
        return (invPairs(n, k, dp) + MOD - invPairs(n, k - 1, dp)) % MOD;
    }

    private int invPairs(int n, int k, Integer[][] dp) {
        if (n == 0) return 0;
        if (k == 0) return 1;
        if (dp[n][k] != null) return dp[n][k];

        int v = (invPairs(n - 1, k, dp)
                 - ((n <= k) ? invPairs(n - 1, k - n, dp) : 0) + MOD) % MOD;
        return dp[n][k] = (invPairs(n, k - 1, dp) + v) % MOD;
    }

    void test(int n, int k, int expected) {
        assertEquals(expected, kInversePairs(n, k));
        assertEquals(expected, kInversePairs2(n, k));
        assertEquals(expected, kInversePairs3(n, k));
    }

    @Test
    public void test() {
        test(1, 0, 1);
        test(3, 2, 2);
        test(3, 3, 1);
        test(3, 0, 1);
        test(3, 1, 2);
        test(4, 1, 3);
        test(4, 2, 5);
        test(4, 3, 6);
        test(4, 4, 5);
        test(4, 5, 3);
        test(4, 6, 1);
        test(4, 7, 0);
        test(10, 7, 4489);
        test(100, 10, 961439433);
        test(1000, 1000, 663677020);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
