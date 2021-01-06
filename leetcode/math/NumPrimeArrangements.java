import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

// LC1175: https://leetcode.com/problems/prime-arrangements/
//
// Return the number of permutations of 1 to n so that prime numbers are at prime indices(1-indexed)
// Since the answer may be large, return the answer modulo 10^9 + 7.
//
// Constraints:
// 1 <= n <= 100
public class NumPrimeArrangements {
    private final static int MOD = 1_000_000_007;

    // time complexity: O((N ^ 1.5) / log(N)), space complexity: O(N / log(N))
    // 1 ms(100.00%), 36.4 MB(70.29%) for 100 tests
    public int numPrimeArrangements(int n) {
        int primes = countPrime(n);
        return (int)(factorial(primes) * factorial(n - primes) % MOD);
    }

    private long factorial(int n) {
        long res = 1;
        for (int i = 2; i <= n; i++) {
            res = (res * i) % MOD;
        }
        return res;
    }

    private int countPrime(int n) {
        if (n < 2)
            return 0;

        List<Integer> primes = new LinkedList<>();
        primes.add(2);
        int count = 1;
        for (int i = 3; i <= n; i += 2) {
            if (isPrime(i, primes)) {
                count++;
            }
        }
        return count;
    }

    private boolean isPrime(int n, List<Integer> primes) {
        for (int prime : primes) {
            if (prime * prime > n)
                break;

            if ((n % prime) == 0)
                return false;
        }
        primes.add(n);
        return true;
    }

    // sieve method
    // time complexity: O(N * log(log(N))), space complexity: O(N)
    // 0 ms(100.00%), 35.7 MB(76.98%) for 100 tests
    public int numPrimeArrangements2(int n) {
        int primes = countPrime2(n);
        long[] memo = new long[n + 1];
        return (int)(factorial(primes, memo) * factorial(n - primes, memo) % MOD);
    }

    private int countPrime2(int n) {
        if (n < 2)
            return 0;

        boolean[] isComposite = new boolean[n + 1];
        int count = (n + 1) / 2;
        for (int i = 3; i * i <= n; i += 2) {
            if (isComposite[i]) { continue; }

            for (int j = i * i; j <= n; j += i * 2) {
                if (!isComposite[j]) {
                    isComposite[j] = true;
                    count--;
                }
            }
        }
        return count;
    }

    private long factorial(int n, long[] memo) {
        if (n < 2) { return 1; }

        if (memo[n] != 0) { return memo[n]; }

        return memo[n] = factorial(n - 1, memo) * n % MOD;
    }

    private void test(int n, int expected) {
        assertEquals(expected, numPrimeArrangements(n));
        assertEquals(expected, numPrimeArrangements2(n));
    }

    @Test public void test() {
        test(5, 12);
        test(100, 682289015);
        test(2, 1);
        test(11, 86400);
        test(21, 476898489);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
