import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC070: https://leetcode.com/problems/climbing-stairs/
//
// Climbing a stair case takes n steps to reach to the top. Each time you can
// either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
public class ClimbStairs {
    // Time Limit Exceeded
    public int climbStairs(int n) {
        if (n < 3) return n;

        return climbStairs(n - 1) + climbStairs(n - 2);
    }

    // Dynamic Programming(top-down)
    // beats 13.04%(0 ms)
    public int climbStairs2(int n) {
        return climbStairs2(n, new int[n + 1]);
    }

    private int climbStairs2(int n, int[] cache) {
        if (n < 3) return n;

        if (cache[n] > 0) return cache[n];

        int res = climbStairs2(n - 1, cache) + climbStairs2(n - 2, cache);
        cache[n] = res;
        return res;
    }

    // Dynamic Programming(bottom-up)
    // Time complexity: O(N)
    // beats 13.04%(0 ms)
    public int climbStairs3(int n) {
        if (n < 3) return n;

        int last1 = 1;
        int last2 = 1;
        int cur = 0;
        for (int i = 2; i <= n; i++) {
            cur = last1 + last2;
            last2 = last1;
            last1 = cur;
        }
        return cur;
    }

    // Solution of Choice
    // Dynamic Programming(bottom-up)
    // Time complexity: O(N)
    // beats 13.04%(0 ms)
    public int climbStairs4(int n) {
        int a = 1;
        for (int i = n, b = 1; i > 0; i--) {
            b += a;
            a = b - a;
        }
        return a;
    }

    // Solution of Choice
    // matrix method
    // Time complexity: O(Log(N)))
    // beats 13.04%(0 ms)
    public int climbStairs5(int n) {
        if (n < 3) return n;

        /*
         * [ 1 1 ]     [ F(n+1) F(n)   ]
         * [ 1 0 ]   = [ F(n)   F(n-1) ]
         */
        int[][] base = {{1, 1}, {1, 0}};
        int[][] result = {{1, 0}, {0, 1}}; // identity

        int i = n + 1;
        while (true) {
            if (i % 2 == 1) {
                multiply(result, base);
            }
            i /= 2;
            if (i == 0) break;

            multiply(base, base);
        }
        return result[1][0];
    }

    private void multiply(int[][] m, int [][] n) {
        int a = m[0][0] * n[0][0] +  m[0][1] * n[1][0];
        int b = m[0][0] * n[0][1] +  m[0][1] * n[1][1];
        int c = m[1][0] * n[0][0] +  m[1][1] * n[0][1];
        int d = m[1][0] * n[0][1] +  m[1][1] * n[1][1];
        m[0][0] = a;
        m[0][1] = b;
        m[1][0] = c;
        m[1][1] = d;
    }

    void test(Function<Integer, Integer> climb, String name, int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)climb.apply(n));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int n, int expected) {
        ClimbStairs c = new ClimbStairs();
        test(c::climbStairs, "climbStairs", n, expected);
        test(c::climbStairs2, "climbStairs2", n, expected);
        test(c::climbStairs3, "climbStairs3", n, expected);
        test(c::climbStairs4, "climbStairs4", n, expected);
        test(c::climbStairs5, "climbStairs5", n, expected);
    }

    @Test
    public void test1() {
        test(8, 34);
        test(38, 63245986);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ClimbStairs");
    }
}
