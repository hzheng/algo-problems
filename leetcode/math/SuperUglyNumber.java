// https://leetcode.com/problems/super-ugly-number/

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

//
// Write a program to find the nth super ugly number.
// Super ugly numbers are positive numbers whose all prime factors are in the
// given prime list primes of size k.
// Note:
// (1) 1 is a super ugly number for any given primes.
// (2) The given numbers in primes are in ascending order.
// (3) 0 < k <= 100, 0 < n <= 10 ^ 6, 0 < primes[i] < 1000.
public class SuperUglyNumber {
    //  Time Limit Exceeded
    public int nthSuperUglyNumber(int n, int[] primes) {
        SortedSet<Long> next = new TreeSet<>();
        next.add(1L);
        for (int i = 1;; i++) {
            long res = next.first();
            if (i >= n) return (int)res;

            next.remove(res);
            for (int prime : primes) {
                next.add(res * prime);
            }
        }
    }

    static class UglyNumber {
        int val;
        int index;
        UglyNumber(int val, int index) {
            this.val = val;
            this.index = index;
        }
    }

    // beats 4.93%(136 ms)
    public int nthSuperUglyNumber2(int n, int[] primes) {
        int[] ugly = new int[n];
        ugly[0] = 1;
        int m = primes.length;
        int[] indices = new int[m];
        PriorityQueue<UglyNumber> next = new PriorityQueue<>(
            (a, b) -> a.val - b.val);
        for (int i = 0; i < m; i++) {
            next.add(new UglyNumber(primes[i], i));
        }

        for (int i = 1; i < n; i++) {
            int val = next.peek().val;
            ugly[i] = val;
            while (next.peek().val == val) {
                UglyNumber nextUgly = next.poll();
                int j = nextUgly.index;
                nextUgly.val = ugly[++indices[j]] * primes[j];
                next.offer(nextUgly);
            }
        }
        return ugly[n - 1];
    }

    void test(int[] primes, int ... expected) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], nthSuperUglyNumber(i + 1, primes));
            assertEquals(expected[i], nthSuperUglyNumber2(i + 1, primes));
        }
    }

    @Test
    public void test1() {
        test(new int[] {2, 7, 13, 19},
             1, 2, 4, 7, 8, 13, 14, 16, 19, 26, 28, 32);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, int[], Integer> nthUgly, String name,
              int expected, int n, int ... primes) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)nthUgly.apply(n, primes));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int expected, int n, int ... primes) {
        SuperUglyNumber s = new SuperUglyNumber();
        test(s::nthSuperUglyNumber, "nthSuperUglyNumber", expected, n, primes);
        test(s::nthSuperUglyNumber2, "nthSuperUglyNumber2", expected, n, primes);
    }

    @Test
    public void test2() {
        test(1092889481, 100000, 7, 19, 29, 37, 41, 47, 53, 59, 61, 79, 83, 89,
             101, 103, 109, 127, 131, 137, 139, 157, 167, 179, 181, 199, 211,
             229, 233, 239, 241, 251);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SuperUglyNumber");
    }
}
