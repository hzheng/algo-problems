import org.junit.Test;
import static org.junit.Assert.*;

// LC790: https://leetcode.com/problems/domino-and-tromino-tiling/
//
// We have two types of tiles: a 2x1 domino shape, and an "L" tromino shape.
// These shapes may be rotated. Given N, how many ways are there to tile a
// 2 x N board? Return your answer modulo 10^9 + 7.
public class NumTilings {
    static final int MOD = 1000_000_000 + 7;

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // beats 99.62%(4 ms for 39 tests)
    public int numTilings(int N) {
        // dp[n] = dp[n-1]+dp[n-2]+2*(dp[n-3]+...+dp[0])
        // = dp[n-1]+dp[n-2]+dp[n-3]+dp[n-3]+2*(dp[n-4]+...+dp[0])
        // = dp[n-1]+dp[n-3]+(dp[n-2]+dp[n-3]+2*(dp[n-4]+...+dp[0]))
        // = dp[n-1]+dp[n-3]+dp[n-1] = 2*dp[n-1]+dp[n-3]
        long[] dp = new long[N + 3];
        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 5;
        for (int i = 4; i <= N; i++) {
            dp[i] += dp[i - 1] * 2 + dp[i - 3];
            dp[i] %= MOD;
        }
        return (int) dp[N];
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(1)
    // beats 99.62%(4 ms for 39 tests)
    public int numTilings2(int N) {
        int p1 = 1;
        int p2 = 0;
        int p3 = -1;
        for (int i = 1; i <= N; i++) {
            int cur = (int) ((p1 * 2L + p3) % MOD);
            p3 = p2;
            p2 = p1;
            p1 = cur;
        }
        return p1;
    }

    // Dynamic Programming(Top-Down) + Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 44.77%(6 ms for 39 tests)
    public int numTilings3(int N) {
        // D: domino shape, T: tromino shape
        // a) remaining is rectangle:
        // 1. fill with | : D(i-1)
        // 2. fill with = : D(i-2)
        // 3. fill with L: T(i-1)
        // 4. fill with flipped L: T(i-1)
        // b) remaining is not rectangle:
        // 1. fill with L x|yy : D(i-2)
        //                xx|y
        // 2. fill with - x|yy : T(i-1)
        //                xx|
        return (int) dTile(N, new long[N + 1], new long[N + 1]);
    }

    private long dTile(int n, long[] D, long[] T) {
        if (n <= 2) return n;

        if (D[n] == 0) {
            D[n] = (dTile(n - 1, D, T) + dTile(n - 2, D, T)
                    + 2 * tTile(n - 1, D, T)) % MOD;
        }
        return D[n];
    }

    private long tTile(int n, long[] D, long[] T) {
        if (n <= 2) return n - 1;

        if (T[n] == 0) {
            T[n] = (dTile(n - 2, D, T) + tTile(n - 1, D, T)) % MOD;
        }
        return T[n];
    }

    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // beats 62.50%(5 ms for 39 tests)
    public int numTilings4(int N) {
        long[] D = new long[N + 2];
        long[] T = new long[N + 2];
        D[1] = 1;
        D[2] = 2;
        T[2] = 1;
        for (int i = 3; i <= N; i++) {
            D[i] = (D[i - 1] + D[i - 2] + 2 * T[i - 1]) % MOD;
            T[i] = (D[i - 2] + T[i - 1]) % MOD;
        }
        return (int) D[N];
    }

    // TODO: use matrix to reduce time complexity to log(N)
    // https://leetcode.com/problems/domino-and-tromino-tiling/discuss/116622/O(logN)-time-O(1)-space-linear-algebraic-algorithm-(in-python)

    void test(int N, int expected) {
        assertEquals(expected, numTilings(N));
        assertEquals(expected, numTilings2(N));
        assertEquals(expected, numTilings3(N));
        assertEquals(expected, numTilings4(N));
    }

    @Test
    public void test() {
        test(1, 1);
        test(2, 2);
        test(3, 5);
        test(4, 11);
        test(5, 24);
        test(6, 53);
        test(50, 451995198);
        test(500, 603582422);
        test(900, 422258822);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
