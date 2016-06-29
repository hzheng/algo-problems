import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/count-primes/
//
// Count the number of prime numbers less than a non-negative number, n.
public class PrimeCount {
    // time complexity: O(N ^ 1.5), space complexity: O(1)
    // Time Limit Exceeded
    public int countPrimes(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    private boolean isPrime(int n) {
        for (int i = 2; i * i <= n; i++) {
            if ((n % i) == 0) return false;
        }
        return true;
    }

    // time complexity: O((N ^ 1.5) / log(N)), space complexity: O(N / log(N))
    // beats 2.96%(310 ms)
    public int countPrimes2(int n) {
        List<Integer> primes = new LinkedList<>();
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i, primes)) {
                count++;
            }
        }
        return count;
    }

    private boolean isPrime(int n, List<Integer> primes) {
        for (int prime : primes) {
            if (prime * prime > n) break;

            if ((n % prime) == 0) return false;
        }
        primes.add(n);
        return true;
    }

    // time complexity: O((N ^ 1.5) / log(N)), space complexity: O(N / log(N))
    // beats 2.96%(304 ms)
    public int countPrimes3(int n) {
        if (n < 3) return 0;

        List<Integer> primes = new LinkedList<>();
        primes.add(2);
        int count = 1;
        for (int i = 3; i < n; i += 2) {
            if (isPrime(i, primes)) {
                count++;
            }
        }
        return count;
    }

    // sieve method(very slow due to set operations)
    // time complexity: ?, space complexity: O(N)
    public int countPrimes4(int n) {
        if (n < 3) return 0;

        SortedSet<Integer> set = new TreeSet<>();
        for (int i = 3; i < n; i += 2) {
            set.add(i);
        }
        int count = 1;
        while (!set.isEmpty()) {
            int first = set.first();
            count++;
            for (int i = first; i < n; i += first) {
                set.remove(i);
            }
        }
        return count;
    }

    // sieve method
    // time complexity: O(N * log(log(N))), space complexity: O(N)
    // beats 30.22%(37 ms)
    public int countPrimes5(int n) {
        boolean[] isComposite = new boolean[n];
        for (int i = 2; i < n; i++) {
            if (!isComposite[i]) {
                for (int j = i * 2; j < n; j += i) {
                    isComposite[j] = true;
                }
            }
        }
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (!isComposite[i]) {
                count++;
            }
        }
        return count;
    }

    // sieve method(from leetcode hint)
    // time complexity: O(N * log(log(N))), space complexity: O(N)
    // beats 62.33%(28 ms)
    public int countPrimes6(int n) {
        boolean[] isPrime = new boolean[n];
        for (int i = 2; i < n; i++) {
            isPrime[i] = true;
        }
        for (int i = 2; i * i < n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false;
                }
            }
        }
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                count++;
            }
        }
        return count;
    }

    void test(Function<Integer, Integer> countPrimes, String name,
              int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)countPrimes.apply(n));
        if (n > 1000) {
            System.out.format("%s: %.3f ms\n", name,
                              (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int n, int expected) {
        PrimeCount p = new PrimeCount();
        test(p::countPrimes2, "countPrimes2", n, expected);
        test(p::countPrimes3, "countPrimes3", n, expected);
        if (n < 1000) {
            test(p::countPrimes, "countPrimes", n, expected);
            test(p::countPrimes4, "countPrimes4", n, expected);
        }
        test(p::countPrimes5, "countPrimes5", n, expected);
        test(p::countPrimes6, "countPrimes6", n, expected);
    }

    @Test
    public void test1() {
        test(2, 0);
        test(3, 1);
        test(4, 2);
        test(6, 3);
        test(11, 4);
        test(12, 5);
        test(18, 7);
        test(25, 9);
        test(29, 9);
        test(30, 10);
        test(50, 15);
        test(10000, 1229);
        test(1500000, 114155);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PrimeCount");
    }
}
