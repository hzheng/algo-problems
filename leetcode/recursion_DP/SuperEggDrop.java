import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC891: https://leetcode.com/problems/super-egg-drop/
//
// Given K eggs, you have access to a building with N floors. Each egg is
// identical in function, and if an egg breaks, you cannot drop it again. There
// exists a floor F with 0 <= F <= N such that any egg dropped at a floor higher
// than F will break, and any egg dropped at or below floor F will not break.
// Each move, you may take an egg and drop it from any floor X. Your goal is to
// know with certainty what the value of F is. What is the minimum number of
// moves that you need to know what F is?
// Note:
// 1 <= K <= 100
// 1 <= N <= 10000
public class SuperEggDrop {
    // 2D-Dynamic Programming
    // Time Limit Exceeded
    // time complexity: O(K * N ^ 2), space complexity: O(K * N)
    public int superEggDrop(int K, int N) {
        int dp[][] = new int[K + 1][N + 1]; // min moves given eggs and floors
        for (int i = 1; i <= K; i++) {
            dp[i][1] = 1;
        }
        for (int j = 1; j <= N; j++) {
            dp[1][j] = j;
        }
        for (int i = 2; i <= K; i++) {
            for (int j = 2; j <= N; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = 1; k <= j; k++) {
                    int moves = 1 + Math.max(dp[i - 1][k - 1], dp[i][j - k]);
                    dp[i][j] = Math.min(dp[i][j], moves);
                }
            }
        }
        return dp[K][N];
    }

    // Recursion + Dynamic Programming(Top-Down) + Binary Search + Hash Table
    // beats %(137 ms for 121 tests)
    // time complexity: O(K * N * log(N)), space complexity: O(K * N)
    public int superEggDrop2(int K, int N) {
        return drop(K, N, new HashMap<>());
    }
    
    private int drop(int K, int N, Map<Integer, Integer> dp) {
        if (N == 0) return 0;

        if (K == 1) return N;

        int key = N * 100 + K;
        if (dp.containsKey(key)) return dp.get(key);

        int low = 1;
        int high = N;
        while (low + 1 < high) {
            int x = (low + high) / 2;
            int t1 = drop(K - 1, x - 1, dp);
            int t2 = drop(K, N - x, dp);
            if (t1 < t2) { 
                low = x;
            } else if (t1 > t2) {
                high = x;
            } else {
                low = high = x;
            }
        }
        int res = 1 + Math.min(Math.max(drop(K - 1, low - 1, dp),
                                        drop(K, N - low, dp)),
                               Math.max(drop(K - 1, high - 1, dp),
                                        drop(K, N - high, dp)));
        dp.put(key, res);
        return res;
    }

    // 1D-Dynamic Programming
    // beats %(40 ms for 121 tests)
    // time complexity: O(K * N), space complexity: O(N)
    public int superEggDrop3(int K, int N) {
        int[] dp = new int[N + 1]; // min moves given floors
        for (int i = 0; i <= N; i++) {
            dp[i] = i;
        }
        for (int k = 2; k <= K; k++) {
            int[] dp2 = new int[N + 1];
            for (int n = 1, best = 1; n <= N; n++) {
                while (best < n && Math.max(dp[best - 1], dp2[n - best])
                                > Math.max(dp[best], dp2[n - best - 1])) {
                    best++;
                }
                // 'best' is increasing w.r.t n
                dp2[n] = 1 + Math.max(dp[best - 1], dp2[n - best]);
            }
            dp = dp2;
        }
        return dp[N];
    }

    // 2D-Dynamic Programming
    // beats %(20 ms for 121 tests)
    // time complexity: O(K * N), space complexity: O(K * N)
    public int superEggDrop4(int K, int N) {
        int dp[][] = new int[K + 1][N + 1]; // max floor given eggs and moves
        for (int i = 1; i <= K; i++) {
            dp[i][1] = 1;
        }
        for (int j = 1; j <= N; j++) {
            dp[1][j] = j;
        }
        for (int i = 2; i <= K; i++) {
            for (int j = 2; j <= N; j++) {
                dp[i][j] = dp[i - 1][j - 1] + dp[i][j - 1] + 1;
            }
        }
        for (int i = 0;; i++) {
            if (dp[K][i] >= N) return i;
        }
    }

    // 2D-Dynamic Programming
    // beats %(11 ms for 121 tests)
    // time complexity: O(K * log(N)), space complexity: O(K * N)
    public int superEggDrop5(int K, int N) {
        int dp[][] = new int[K + 1][N + 1]; // max floor given eggs and moves
        int moves = 0;
        for (; dp[K][moves] < N; moves++) {
            for (int k = 1; k <= K; k++) {
                dp[k][moves + 1] = dp[k - 1][moves] + dp[k][moves] + 1;
            }
        }
        return moves;
    }

    // 1D-Dynamic Programming
    // beats %(5 ms for 121 tests)
    // time complexity: O(K * log(N)), space complexity: O(N)
    public int superEggDrop6(int K, int N) {
        int dp[] = new int[K + 1]; // max floor given eggs and moves
        int moves = 0;
        for (; dp[K] < N; moves++) {
            for (int k = K; k > 0; k--) {
                dp[k] += dp[k - 1] + 1;
            }
        }
        return moves;
    }

    // Binary Search + Combinatorics
    // https://leetcode.com/problems/super-egg-drop/solution/
    // beats %(5 ms for 121 tests)
    // time complexity: O(K * log(N)), space complexity: O(1)
    public int superEggDrop7(int K, int N) {
        int low = 1;
        for (int high = N; low < high; ) {
            int mid = (low + high) / 2;
            if (check(mid, K, N) < N) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // f(t, k) = 1 + f(t-1, k-1) + f(t-1, k)
    // f(t,k−1)=1+f(t−1,k−2)+f(t−1,k−1)
    // let g(t, k) = f(t, k) - f(t, k-1)
    // we have: g(t,k)=g(t−1,k)+g(t−1,k−1)
    // hence g(t,k)= C(k+1,t) and f(t,k)=sum(C(t,x))
    private int check(int x, int K, int N) {
        int res = 0;
        for (int i = 1, r = 1; i <= K; i++) {
            // C(K, n) * (n-k)/(k+1)=C(k+1, n)
            r *= x - i + 1;
            r /= i;
            res += r;
            if (res >= N) break;
        }
        return res;
    }

    // Recursion
    // beats %(5 ms for 121 tests)
    public int superEggDrop8(int K, int N) {
        int times = 1;
        for (; drop(K, times) < N; times++) {}
        return times;
    }

    private int drop(int eggs, int times) {
        if (eggs == 1) return times;

        if (eggs >= times) return (1 << times) - 1;

        return drop(eggs, times - 1) + drop(eggs - 1, times - 1) + 1;
    }

    void test(int K, int N, int expected) {
        assertEquals(expected, superEggDrop(K, N));
        assertEquals(expected, superEggDrop2(K, N));
        assertEquals(expected, superEggDrop3(K, N));
        assertEquals(expected, superEggDrop4(K, N));
        assertEquals(expected, superEggDrop5(K, N));
        assertEquals(expected, superEggDrop6(K, N));
        assertEquals(expected, superEggDrop7(K, N));
        assertEquals(expected, superEggDrop8(K, N));
    }

    @Test
    public void test() {
        test(1, 1, 1);
        test(1, 2, 2);
        test(2, 6, 3);
        test(3, 14, 4);
        test(6, 100, 7);
        test(60, 1000, 10);
        test(60, 5000, 13);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
