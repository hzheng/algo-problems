import org.junit.Test;
import static org.junit.Assert.*;

// LC805: https://leetcode.com/problems/split-array-with-same-average/
//
// In a given integer array A, we must move every element of A to either list B
// or list C. (B and C initially start empty.)
// Return true if and only if after such a move, it is possible that the average
// value of B is equal to the average value of C, and B and C are both non-empty.
// Note:
// The length of A will be in the range [1, 30].
// A[i] will be in the range of [0, 10000].
public class SplitArraySameAverage {
    // DFS + Recursion
    // beats %(7 ms for 82 tests)
    public boolean splitArraySameAverage(int[] A) {
        int n = A.length;
        int sum = 0;
        for (int a : A) {
            sum += a;
        }
        // double mean = ((double)sum) / n;
        for (int m = n / 2; m > 0; m--) {
            // double expected = mean * m;
            // int tgt = (int)expected;
            // if ((expected - tgt < 1e-6) && dfs(A, 0, 0, m, 0, tgt)) return true;
            if (sum * m % n == 0) {
                if (dfs(A, n - 1, m, sum * m / n)) return true;
            }
        }
        return false;
    }

    private boolean dfs(int[] A, int index, int left, int target) {
        if (target < 0 || (index + 1 < left)) return false;

        if (index < 0 || left == 0) return (target == 0) && (left == 0);

        return dfs(A, index - 1, left - 1, target - A[index])
               || dfs(A, index - 1, left, target);
    }

    // DFS + Recursion
    // beats %(9 ms for 82 tests)
    public boolean splitArraySameAverage_2(int[] A) {
        int n = A.length;
        int sum = 0;
        for (int a : A) {
            sum += a;
        }
        for (int m = n / 2; m > 0; m--) {
            if (sum * m % n == 0 && split(A, 0, m, sum * m / n)) return true;
        }
        return false;
    }

    private boolean split(int[] A, int index, int left, int tgt) {
        if (left == 0) return tgt == 0;

        for (int i = index; i + left <= A.length; i++) {
            if (A[i] <= tgt && split(A, i + 1, left - 1, tgt - A[i])) return true;
        }
        return false;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 2 * SUM), space complexity: O(N * SUM)
    // beats %(88 ms for 82 tests)
    public boolean splitArraySameAverage2(int[] A) {
        int n = A.length;
        int total = 0;
        for (int i = 0; i < n; i++) {
            total += A[i];
        }
        boolean[][] dp = new boolean[n + 1][total + 1];
        dp[0][0] = true;
        for (int i = 0, j = 0, a = 0; i < n; i++, j += a) {
            a = A[i];
            for (int count = i; count >= 0; count--) {
                // for (int count = 0; count <= i; count++) { // WRONG!
                for (int sum = 0; sum <= j; sum++) {
                    dp[count + 1][sum + a] |= dp[count][sum];
                }
            }
        }
        for (int i = 1; i < n; i++) {
            if (total * i % n == 0 && dp[i][total * i / n]) return true;
        }
        return false;
    }

    void test(int[] A, boolean expected) {
        assertEquals(expected, splitArraySameAverage(A));
        assertEquals(expected, splitArraySameAverage_2(A));
        assertEquals(expected, splitArraySameAverage2(A));
        // assertEquals(expected, splitArraySameAverage3(A));
    }

    @Test
    public void test() {
        test(new int[] {17, 3, 7, 12, 1}, false);
        test(new int[] {17, 3, 17, 12, 1}, true);
        test(new int[] {4, 0, 6, 0, 0}, true);
        test(new int[] {4, 0, 2}, true);
        test(new int[] {18, 10, 5, 3}, false);
        test(new int[] {1, 2, 3, 4, 5, 6, 8, 7}, true);
        test(new int[] {1, 3, 5}, true);
        test(new int[] {1, 2}, false);
        test(new int[] {3, 1, 2}, true);
        test(new int[] {1, 4, 6, 8, 7}, false);
        test(new int[] {3, 6, 9, 1, 11}, true);
        test(new int[] {17, 5, 5, 1, 14, 10, 13, 1, 6}, true);
        test(new int[] {2, 0, 5, 6, 16, 12, 15, 12, 4}, true);
        test(new int[] {10, 17, 18, 8, 4, 0, 5, 9, 1}, true);
        test(new int[] {10, 29, 13, 53, 33, 48, 76, 70, 5, 5}, true);
        test(new int[] {73, 37, 34, 95, 4, 97, 22, 53, 55, 52, 46, 44, 71, 24,
                        26, 35, 96, 3}, true);
        test(new int[] {53, 6, 3, 34, 91, 82, 47, 9, 70, 1}, true);
        test(new int[] {86, 62, 43, 47, 34, 4, 76, 58, 14, 34, 70, 98, 97, 5},
             true);
        test(new int[] {1567, 2106, 4090, 293, 9642, 7740, 9159, 136, 3724,
                        1373, 9397, 3829, 1622, 1888, 7250, 8252, 12}, true);
        test(new int[] {3863, 703, 1799, 327, 3682, 4330, 3388, 6187, 5330,
                        6572, 938, 6842, 678, 9837, 8256, 6886, 2204, 5262,
                        6643, 829, 745, 8755, 3549, 6627, 1633, 4290, 7},
             false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
