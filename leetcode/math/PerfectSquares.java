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

    // Lagrange's four-square theorem
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

    // BFS
    // Memory Limit Exceeded
    public int numSquares4(int n) {
        int[] squares = new int[n > 10 ? n / 2 : n + 1];
        for (int i = 1;; i++) {
            int square = i * i;
            if (square == n) return 1;

            squares[i] = square;
            if (square > n) break;
        }

        Queue<Integer> queue = new LinkedList<>();
        add(queue, n, squares);

        for (int level = 2;; level++) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int cur = queue.poll();
                if (add(queue, cur, squares)) {
                    return level;
                }
            }
        }
    }

    private boolean add(Queue<Integer> queue, int n, int[] squares) {
        for (int i = 1;; i++) {
            int left = n - squares[i];
            if (left == 0) return true;
            if (left < 0) return false;

            queue.add(left);
        }
    }

    // BFS with cache
    // beats 13.38%(146 ms)
    public int numSquares5(int n) {
        int[] squares = new int[n > 10 ? n / 2 : n + 1];
        for (int i = 1;; i++) {
            int square = i * i;
            if (square == n) return 1;

            squares[i] = square;
            if (square > n) break;
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> added = new HashSet<>();
        add(queue, n, squares, added);

        for (int level = 2;; level++) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int cur = queue.poll();
                if (add(queue, cur, squares, added)) {
                    return level;
                }
            }
        }
    }

    private boolean add(Queue<Integer> queue, int n, int[] squares,
                        Set<Integer> added) {
        for (int i = 1;; i++) {
            int left = n - squares[i];
            if (left == 0) return true;
            if (left < 0) return false;

            if (!added.contains(left)) {
                queue.add(left);
                added.add(left);
            }
        }
    }

    // DP
    // beats 60.85%(68 ms)
    public int numSquares6(int n) {
        int[] table = new int[n + 1];
        Arrays.fill(table, n);
        for (int i = 0; i * i <= n; i++) {
            table[i * i] = 1;
        }
        for (int i = 1; i <= n; i++) {
            for (int j = 1;; j++) {
                int composite = i + j * j;
                if (composite > n) break;

                table[composite] = Math.min(table[i] + 1, table[composite]);
            }
        }
        return table[n];
    }

    // Lagrange's four-square theorem & Legendre's three-square theorem
    // time complexity: O(N ^ (0.5)), space complexity: O(1)
    // beats 97.12%(2 ms)
    public int numSquares7(int n) {
        while (n % 4 == 0) {
            n /= 4;
        }

        if (n % 8 == 7) return 4;

        for (int i = 0; i * i <= n; i++) {
            int j = (int)Math.sqrt(n - i * i);
            if (i * i + j * j == n) {
                return (i > 0 ? 1 : 0) + (j > 0 ? 1 : 0);
            }
        }
        return 3;
    }

    // beats 97.12%(2 ms)
    public int numSquares8(int n) {
        if (isSquare(n)) return 1;

        while ((n & 3) == 0) {
            n >>= 2;
        }
        if ((n & 7) == 7) return 4;

        for(int i = 1; i * i <= n; i++) {
            if (isSquare(n - i * i)) return 2;
        }
        return 3;
    }

    boolean isSquare(int n) {
        int sqrt = (int)(Math.sqrt(n));
        return sqrt * sqrt == n;
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
        if (n < 10000) {
            test(p::numSquares4, "numSquares4", n, expected);
        }
        test(p::numSquares5, "numSquares5", n, expected);
        test(p::numSquares6, "numSquares6", n, expected);
        test(p::numSquares7, "numSquares7", n, expected);
        test(p::numSquares8, "numSquares8", n, expected);
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
        test(999976, 3);
        // test(999975, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PerfectSquares");
    }
}
