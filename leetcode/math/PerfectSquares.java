import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/perfect-squares/
//
// Given a positive integer n, find the least number of perfect square numbers
//  which sum to n.
public class PerfectSquares {
    // StackOverFlowError when n is big
    public int numSquares(int n) {
        return numSquares(n, new HashMap<>());
    }

    private int numSquares(int n, Map<Integer, Integer> cache) {
        if (cache.containsKey(n)) {
            return cache.get(n);
        }

        int min = n;
        for (int i = 1;; i++) {
            int square = i * i;
            if (square == n) {
                min = 1;
                break;
            }
            if (square > n) break;

            min = Math.min(min, 1 + numSquares(n - square, cache));
        }
        cache.put(n, min);
        return min;
    }

    // time complexity: O(N ^ (1.5)), space complexity: O(N)
    // beats 60.85%(68 ms)
    public int numSquares2(int n) {
        int[] table = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            int min = n;
            for (int j = 1;; j++) {
                int square = j * j;
                if (square > i) break;

                min = Math.min(min, 1 + table[i - square]);
            }
            table[i] = min;
        }
        return table[n];
    }

    // time complexity: O(N ^ (1.5)), space complexity: O(N)
    // beats 71.36%(66 ms)
    public int numSquares3(int n) {
        int[] table = new int[n + 1];
        int[] squares = new int[n > 10 ? n / 2 : n + 1];
        for (int i = 1;; i++) {
            int square = i * i;
            if (square == n) return 1;

            squares[i] = square;
            if (square > n) break;
        }

        for (int i = 1; i <= n; i++) {
            int min = n;
            for (int j = 1;; j++) {
                int square = squares[j];
                if (square > i) break;

                min = Math.min(min, 1 + table[i - square]);
            }
            table[i] = min;
        }
        return table[n];
    }

    void test(Function<Integer, Integer> numSquares, String name,
              int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)numSquares.apply(n));
        if (n > 100) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int n, int expected) {
        PerfectSquares p = new PerfectSquares();
        if (n < 1000) {
            test(p::numSquares, "numSquares", n, expected);
        }
        test(p::numSquares2, "numSquares2", n, expected);
        test(p::numSquares3, "numSquares3", n, expected);
    }

    @Test
    public void test1() {
        test(2, 2);
        test(5, 2);
        test(11, 3);
        test(12, 3);
        test(9, 1);
        test(10, 2);
        test(13, 2);
        test(50, 2);
        test(126, 3);
        test(999, 4);
        test(9975, 4);
        test(999975, 4);
        test(999976, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PerfectSquares");
    }
}
