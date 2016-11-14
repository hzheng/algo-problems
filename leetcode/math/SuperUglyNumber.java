// https://leetcode.com/problems/super-ugly-number/

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

// LC313: https://leetcode.com/problems/super-ugly-number/
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

    static class UglyNumber implements Comparable<UglyNumber> {
        int val;
        int index;
        UglyNumber(int val, int index) {
            this.val = val;
            this.index = index;
        }

        public int compareTo(UglyNumber that) {
            return val - that.val;
        }
    }

    // Solution of Choice
    // Heap + Dynamic Programming
    // time complexity: O(N * log(K))
    // beats 32.70%(48 ms for 83 tests)
    public int nthSuperUglyNumber2(int n, int[] primes) {
        int[] ugly = new int[n];
        ugly[0] = 1;
        int k = primes.length;
        int[] indices = new int[k];
        PriorityQueue<UglyNumber> pq = new PriorityQueue<>();
        for (int i = 0; i < k; i++) {
            pq.add(new UglyNumber(primes[i], i));
        }
        for (int i = 1; i < n; i++) {
            int val = pq.peek().val;
            ugly[i] = val;
            do {
                UglyNumber next = pq.poll();
                next.val = ugly[++indices[next.index]] * primes[next.index];
                pq.offer(next);
            } while (pq.peek().val == val);
        }
        return ugly[n - 1];
    }

    // Dynamic Programming
    // time complexity: O(N * K)
    // beats 59.01%(31 ms)
    public int nthSuperUglyNumber3(int n, int[] primes) {
        int k = primes.length;
        int[] ugly = new int[n];
        int[] multIndex = new int[k];
        ugly[0] = 1;
        for (int i = 1; i < n; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < k; j++) {
                min = Math.min(min, primes[j] * ugly[multIndex[j]]);
            }
            ugly[i] = min;
            for (int j = 0; j < k; j++) {
                if (primes[j] * ugly[multIndex[j]] == min) {
                    multIndex[j]++;
                }
            }
        }
        return ugly[n - 1];
    }

    // Dynamic Programming
    // https://discuss.leetcode.com/topic/34841/java-three-methods-23ms-36-ms-58ms-with-heap-performance-explained
    // time complexity: O(N * K)
    // beats 95.18%(21 ms for 83 tests)
    public int nthSuperUglyNumber4(int n, int[] primes) {
        int[] ugly = new int[n];
        int k = primes.length;
        int[] multIndex = new int[k];
        int[] val = new int[k];
        Arrays.fill(val, 1);
        for (int i = 0, next = 1; i < n; i++) {
            ugly[i] = next;
            next = Integer.MAX_VALUE;
            for (int j = 0; j < k; j++) {
                //skip duplicate and avoid extra multiplication
                if (val[j] == ugly[i]) {
                    val[j] = ugly[multIndex[j]++] * primes[j];
                }
                next = Math.min(next, val[j]);
            }
        }
        return ugly[n - 1];
    }

    void test(int[] primes, int ... expected) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], nthSuperUglyNumber(i + 1, primes));
            assertEquals(expected[i], nthSuperUglyNumber2(i + 1, primes));
            assertEquals(expected[i], nthSuperUglyNumber3(i + 1, primes));
            assertEquals(expected[i], nthSuperUglyNumber4(i + 1, primes));
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
        test(s::nthSuperUglyNumber3, "nthSuperUglyNumber3", expected, n, primes);
        test(s::nthSuperUglyNumber4, "nthSuperUglyNumber4", expected, n, primes);
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
