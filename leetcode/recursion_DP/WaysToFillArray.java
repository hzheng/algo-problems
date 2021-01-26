import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1735: https://leetcode.com/problems/count-ways-to-make-array-with-product/
//
// You are given a 2D integer array, queries. For each queries[i], where queries[i] = [ni, ki], find
// the number of different ways you can place positive integers into an array of size ni such that
// the product of the integers is ki. As the number of ways may be too large, the answer to the ith
// query is the number of ways modulo 10^9 + 7. Return an integer array answer where
// answer.length == queries.length, and answer[i] is the answer to the ith query.
//
// Constraints:
// 1 <= queries.length <= 10^4
// 1 <= ni, ki <= 10^4
public class WaysToFillArray {
    private static final int MOD = 1_000_000_007;

    private static final Map<Long, Long> combinations = new HashMap<>();

    // Math + Dynamic Programming
    // 656 ms(32.14%), 69.4 MB(33.33%) for 66 tests
    public int[] waysToFillArray(int[][] queries) {
        int[] res = new int[queries.length];
        int i = 0;
        for (int[] q : queries) {
            res[i++] = waysToFillArray(q[0], q[1]);
        }
        return res;
    }

    private long combinate(int n, int m, Map<Long, Long> combinations) {
        if (m == 0 || n <= m) { return 1; }

        long key = (((long)n) << Integer.SIZE) | m;
        long res = combinations.getOrDefault(key, 0L);
        if (res > 0) { return res; }

        res = (combinate(n - 1, m - 1, combinations) + combinate(n - 1, m, combinations)) % MOD;
        combinations.put(key, res);
        return res;
    }

    private int waysToFillArray(int n, int k) {
        long res = 1;
        for (int f : factorList(k)) {
            res = (res * combinate(f + n - 1, n - 1, combinations)) % MOD;
        }
        return (int)res;
    }

    private List<Integer> factorList(int n) {
        List<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            int pow = 0;
            for (; n % i == 0; pow++, n /= i) {}
            if (pow > 0) {
                factors.add(pow);
            }
        }
        return factors;
    }

    private static final int[][] C = new int[10013][14];

    private static final int[] PRIMES =
            {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83,
             89, 97};

    // Math + Dynamic Programming
    // 22 ms(100.00%), 43.8 MB(88.09%) for 66 tests
    public int[] waysToFillArray2(int[][] queries) {
        if (C[0][0] == 0) {
            for (int n = 0; n < C.length; n++) {
                C[n][0] = 1;
                for (int r = Math.min(n, C[0].length - 1); r > 0; r--) {
                    C[n][r] = (C[n - 1][r - 1] + C[n - 1][r]) % MOD;
                }
            }
        }
        int[] res = new int[queries.length];
        int i = 0;
        for (int[] q : queries) {
            res[i++] = waysToFillArray2(q[0], q[1]);
        }
        return res;
    }

    private int waysToFillArray2(int n, int k) {
        int res = 1;
        for (int p : PRIMES) {
            int pow = 0;
            for (; k % p == 0; pow++, k /= p) {}
            res = (int)(((long)res) * C[n - 1 + pow][pow] % MOD);
        }
        return (k == 1) ? res : (int)((long)res * n % MOD);
    }

    private void test(int[][] queries, int[] expected) {
        assertArrayEquals(expected, waysToFillArray(queries));
        assertArrayEquals(expected, waysToFillArray2(queries));
    }

    @Test public void test() {
        test(new int[][] {{2, 6}, {5, 1}, {73, 660}}, new int[] {4, 1, 50734910});
        test(new int[][] {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}}, new int[] {1, 2, 3, 10, 5});
        test(new int[][] {{373, 196}, {101, 229}, {466, 109}, {308, 83}, {296, 432}},
             new int[] {865201973, 101, 466, 308, 411805778});
        test(new int[][] {{474, 282}, {38, 139}, {235, 321}, {383, 325}, {224, 70}},
             new int[] {106496424, 38, 55225, 28164288, 11239424});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
