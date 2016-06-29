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

    // time complexity: O((N ^ 1.5) / log(N)), space complexity: O(1)
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
        test(p::countPrimes, "countPrimes", n, expected);
        test(p::countPrimes2, "countPrimes2", n, expected);
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
        test(1500000, 114155);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PrimeCount");
    }
}
