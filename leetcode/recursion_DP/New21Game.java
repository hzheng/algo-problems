import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC837: https://leetcode.com/problems/new-21-game/
//
// Alice starts with 0 points, and draws numbers while she has less than K
// points. During each draw, she gains an integer number of points randomly from
// the range [1, W], where W is an integer. Each draw is independent and the
// outcomes have equal probabilities. Alice stops drawing numbers when she gets
// K or more points.  What is the probability that she has N or less points?
// Note:
// 0 <= K <= N <= 10000
// 1 <= W <= 10000
public class New21Game {
    // Dynamic Programming
    // time complexity: O(N + W), space complexity: O(N + W)
    // beats %(19 ms for 146 tests)
    public double new21Game(int N, int K, int W) {
        double[] dp = new double[N + W + 1]; // win chance for the point i
        for (int i = K; i <= N; i++) {
            dp[i] = 1.0;
        }
        // f(x)=​(∑f(x+i)) / W
        double S = Math.min(N - K + 1, W);
        for (int i = K - 1; i >= 0; i--) {
            dp[i] = S / W;
            S += dp[i] - dp[i + W];
        }
        return dp[0];
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats %(20 ms for 146 tests)
    public double new21Game2(int N, int K, int W) {
        if (K == 0 || N >= K + W) return 1;

        double res = 0;
        double[] dp = new double[N + 1]; // probability of getting point i
        double S = dp[0] = 1;
        for (int i = 1; i <= N; i++) {
            dp[i] = S / W;
            if (i < K) {
                S += dp[i];
            } else {
                res += dp[i];
            }
            if (i >= W) {
                S -= dp[i - W];
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats %(22 ms for 146 tests)
    public double new21Game3(int N, int K, int W) {
        double[] dp = new double[N + W + 1];
        dp[0] = 1;
        dp[1] = -1;
        double res = 0;
        for (int i = 0; i <= N; i++) {
            dp[i + 1] += dp[i];
            if (i < K) {
                dp[i + 1] += dp[i] / W;
                dp[i + W + 1] -= dp[i] / W;
            } else {
                res += dp[i];
            }
        }
        return res;
    }

    void test(int N, int K, int W, double expected) {
        assertEquals(expected, new21Game(N, K, W), 1e-5);
        assertEquals(expected, new21Game2(N, K, W), 1e-5);
        assertEquals(expected, new21Game3(N, K, W), 1e-5);
    }

    @Test
    public void test() {
        test(0, 0, 1, 1.0);
        test(10, 1, 10, 1.0);
        test(6, 1, 10, 0.6);
        test(21, 17, 10, 0.73278);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
