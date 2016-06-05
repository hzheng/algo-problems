import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// A robot is located at the top-left corner of a m x n grid.
// It can only move either down or right at any point. The robot
// is trying to reach the bottom-right corner of the grid.
// How many possible unique paths are there?
public class UniquePath {
    // Time Limit Exceeded
    public int uniquePaths(int m, int n) {
        int[] count = new int[1];
        visit(count, m, n, 1, 1);
        return count[0];
    }

    private void visit(int[] count, int m, int n, int x, int y) {
        if (x == m && y == n) {
            count[0]++;
            return;
        }

        if (x < m) {
            visit(count, m, n, x + 1, y);
        }
        if (y < n) {
            visit(count, m, n, x, y + 1);
        }
    }

    // beats 2.59%
    public int uniquePaths2(int m, int n) {
        if (m == 1 || n == 1) return 1;

        // if (m == 2) return n;

        // if (n == 2) return m;

        int count = 0;
        int n1 = n / 2;
        int n2 = n - n1;
        for (int i = 1; i <= m; i++) {
            count += uniquePaths2(i, n1) * uniquePaths2(m + 1 - i, n2);
        }
        return count;
    }

    // beats 5.98%
    public int uniquePaths3(int m, int n) {
        int[][] cache = new int[m][n];
        return uniquePaths3(m, n, cache);
    }

    private int uniquePaths3(int m, int n, int[][] cache) {
        if (m == 1 || n == 1) return 1;

        int computed = cache[m - 1][n - 1];
        if (computed > 0) return computed;

        int count = 0;
        int n1 = n / 2;
        int n2 = n - n1;
        for (int i = 1; i <= m; i++) {
            count += uniquePaths3(i, n1, cache)
                     * uniquePaths3(m + 1 - i, n2, cache);
        }
        cache[m - 1][n - 1] = count;
        if (n <= cache.length && m <= cache[0].length) {
            cache[n - 1][m - 1] = count;
        }
        return count;
    }

    // beats 5.98%
    public int uniquePaths4(int m, int n) {
        if (m == 1 || n == 1) return 1;

        int[][] count = new int[m][n];
        for (int i = 0; i < m; i++) {
            count[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            count[0][j] = 1;
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                count[i][j] = count[i - 1][j] + count[i][j - 1];
            }
        }
        return count[m - 1][n - 1];
    }

    // beats 83.92%
    public int uniquePaths5(int m, int n) {
        int[] count = new int[n];
        for (int i = 0; i < n; i++) {
            count[i] = 1;
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                count[j] += count[j - 1];
            }
        }
        return count[n - 1];
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, Integer, Integer> path, String name,
              int expected, int m, int n) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)path.apply(m, n));
        System.out.format("%s %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int expected, int m, int n) {
        UniquePath p = new UniquePath();
        test(p::uniquePaths, "uniquePaths", expected, m, n);
        test(p::uniquePaths2, "uniquePaths2", expected, m, n);
        test(p::uniquePaths3, "uniquePaths3", expected, m, n);
        test(p::uniquePaths4, "uniquePaths4", expected, m, n);
        test(p::uniquePaths5, "uniquePaths5", expected, m, n);
    }

    @Test
    public void test1() {
        test(1, 1, 2);
        test(2, 2, 2);
        test(3, 3, 2);
        test(6, 3, 3);
        test(21, 6, 3);
        test(21, 3, 6);
        test(193536720, 23, 12);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UniquePath");
    }
}
