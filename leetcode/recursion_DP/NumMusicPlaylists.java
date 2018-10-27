import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC920: https://leetcode.com/problems/number-of-music-playlists/
//
// Your music player contains N different songs and she wants to listen to L 
// (not necessarily different) songs during your trip.  You create a playlist so
// that: 
// Every song is played at least once
// A song can only be played again only if K other songs have been played
// Return the number of possible playlists.  
// As the answer can be very large, return it modulo 10^9 + 7.
// Note:
// 0 <= K < N <= L <= 100
public class NumMusicPlaylists {
    static final int MOD = 1000_000_000 + 7;

    // Dynamic Programming
    // time complexity: O(N * L), space complexity: O(N * L)
    // beats 64.42%(11 ms for 83 tests)
    public int numMusicPlaylists(int N, int L, int K) {
        long[][] dp = new long[L + 1][N + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= L; i++) {
            for (int j = 1; j <= N; j++) {
                dp[i][j] += dp[i - 1][j - 1] * (N - j + 1);
                dp[i][j] += dp[i - 1][j] * Math.max(j - K, 0);
                dp[i][j] %= MOD;
            }
        }
        return (int)dp[L][N];
    }

    // Dynamic Programming
    // time complexity: O((N - K) * (L - K)), space complexity: O(N * L)
    // beats 98.77%(6 ms for 83 tests)
    public int numMusicPlaylists2(int N, int L, int K) {
        long[][] dp = new long[N + 1][L + 1];
        long f = factorial(K + 1);
        for (int i = K + 1; i <= N; f = f * (++i) % MOD) {
            for (int j = i; j <= L; j++) {
                if ((i == j) || (i == K + 1)) {
                    dp[i][j] = f; // factorial(i);
                } else {
                    dp[i][j] = (dp[i - 1][j - 1] * i + dp[i][j - 1] * (i - K)) % MOD;
                }
            }
        }
        return (int)dp[N][L];
    }

    private long factorial(int n) {
        long res = 1;
        for (int i = n; i > 1; i--) {
            res = res * i % MOD;
        }
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down)
    // time complexity: O((N - K) * (L - K)), space complexity: O(N * L)
    // beats 96.73%(7 ms for 83 tests)
    public int numMusicPlaylists3(int N, int L, int K) {
        return (int)numMusicPlaylists(N, L, K, new long[N + 1][L + 1]);
    }

    private long numMusicPlaylists(int N, int L, int K, long[][] dp) {
        if (N == L || N == K + 1) return factorial(N) % MOD;

        if (dp[N][L] > 0) return dp[N][L];

        long res = numMusicPlaylists(N - 1, L - 1, K, dp) * N;
        res += numMusicPlaylists(N, L - 1, K, dp) * (N - K);
        return dp[N][L] = res % MOD;
    }

    void test(int N, int L, int K, int expected) {
        assertEquals(expected, numMusicPlaylists(N, L, K));
        assertEquals(expected, numMusicPlaylists2(N, L, K));
        assertEquals(expected, numMusicPlaylists3(N, L, K));
    }

    @Test
    public void test() {
        test(3, 3, 1, 6);
        test(2, 3, 0, 6);
        test(2, 3, 1, 2);
        test(6, 8, 3, 18000);
        test(9, 18, 5, 4479967);
        test(29, 98, 15, 921785630);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
