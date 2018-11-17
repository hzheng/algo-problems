import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC204: https://leetcode.com/problems/count-primes/
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
    // beats 8.72%(240 ms for 20 tests)
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
    // beats 9.93%(191 ms for 20 tests)
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

    // Set + sieve method(very slow due to set operations)
    // time complexity: ?, space complexity: O(N)
    // Time Limit Exceeded
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
    // beats 97.09%(12 ms for 20 tests)
    public int countPrimes5(int n) {
        boolean[] isComposite = new boolean[n];
        int count = n - 1;
        for (int i = 2; i * i < n; i++) {
            if (isComposite[i]) continue;

            for (int j = i * i; j < n; j += i) {
                if (!isComposite[j]) {
                    isComposite[j] = true;
                    count--;
                }
            }
        }
        return count > 0 ? --count : 0;
    }

    // Solution of Choice
    // sieve method
    // time complexity: O(N * log(log(N))), space complexity: O(N)
    // beats 99.52%(8 ms for 20 tests)
    public int countPrimes6(int n) {
        if (n < 3) return 0;

        boolean[] isComposite = new boolean[n];
        int count = n / 2;
        for (int i = 3; i * i < n; i += 2) {
            if (isComposite[i]) continue;

            for (int j = i * i; j < n; j += i * 2) {
                if (!isComposite[j]) {
                    isComposite[j] = true;
                    count--;
                }
            }
        }
        return count;
    }

    // sieve method
    // time complexity: O(N), space complexity: O(N)
    // beats 73.81%(16 ms for 20 tests)
    public int countPrimes7(int n) {
        boolean[] isComposite = new boolean[n];
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isComposite[i]) continue;

            count++;
            for (int j = i * 2; j < n; j += i) {
                isComposite[j] = true;
            }
        }
        return count;
    }

    // sieve method
    // time complexity: O(N), space complexity: O(N)
    // beats 59.53%(18 ms for 20 tests)
    public int countPrimes8(int n) {
        boolean[] isComposite = new boolean[n];
        int count = 0;
        for (int i = 2, max = (int)Math.sqrt(n); i < n; i++) {
            if (isComposite[i]) continue;

            count++;
            if (i > max) continue;

            for (int j = i * i; j < n; j += i) {
                isComposite[j] = true;
            }
        }
        return count;
    }

    // sieve method
    // time complexity: O(N), space complexity: O(N)
    // beats 38.09%(25 ms for 20 tests)
    // TODO: improvement
    public int countPrimes9(int n) {
        boolean[] isComposite = new boolean[n];
        int[] primes = new int[n]; // can be smaller
        for (int i = 2, primeCount = 0; i < n; i++) {
            if (!isComposite[i]) {
                primes[primeCount++] = i;
            }
            for (int j = 0; j < primeCount; j++) {
                int multiple = primes[j] * i;
                if (multiple >= n) break;

                // all composites are deleted by its smallest prime factor,
                // so they are only marked once, hence O(N)
                isComposite[multiple] = true;
                if (i % primes[j] == 0) break;
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

    void test(Function<Integer, Integer> countPrimes, String name, int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)countPrimes.apply(n));
        if (n > 1000) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
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
        test(p::countPrimes7, "countPrimes7", n, expected);
        test(p::countPrimes8, "countPrimes8", n, expected);
        test(p::countPrimes9, "countPrimes9", n, expected);
    }

    @Test
    public void test1() {
        test(0, 0);
        test(1, 0);
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
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
