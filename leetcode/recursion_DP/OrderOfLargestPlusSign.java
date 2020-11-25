import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC764: https://leetcode.com/problems/largest-plus-sign/
//
// In a 2D grid from (0, 0) to (N-1, N-1), every cell contains a 1, except those cells in the given
// list mines which are 0. What is the largest axis-aligned plus sign of 1s contained in the grid?
// Return the order of the plus sign. If there is none, return 0.
// An "axis-aligned plus sign of 1s of order k" has some center grid[x][y] = 1 along with 4 arms of
// length k-1 going up, down, left, and right, and made of 1s. Note that there could be 0s or 1s
// beyond the arms of the plus sign, only the relevant area of the plus sign is checked for 1s.
//
// Note:
// N will be an integer in the range [1, 500].
// mines will have length at most 5000.
// mines[i] will be length 2 and consist of integers in the range [0, N-1].
public class OrderOfLargestPlusSign {
    // SortedSet
    // time complexity: O(N^2*log(M)), space complexity: O(M)
    // 349 ms(12.28%), 45 MB(62.82%) for 58 tests
    public int orderOfLargestPlusSign(int N, int[][] mines) {
        Map<Integer, NavigableSet<Integer>> zeroRows = new HashMap<>();
        Map<Integer, NavigableSet<Integer>> zeroCols = new HashMap<>();
        for (int[] mine : mines) {
            int x = mine[0];
            int y = mine[1];
            zeroRows.computeIfAbsent(x, a -> new TreeSet<>()).add(y);
            zeroCols.computeIfAbsent(y, a -> new TreeSet<>()).add(x);
        }
        int res = 0;
        final NavigableSet<Integer> emptySet = Collections.emptyNavigableSet();
        for (int x = 0; x < N; x++) {
            NavigableSet<Integer> xZeros = zeroRows.getOrDefault(x, emptySet);
            for (int y = 0; y < N; y++) {
                if (xZeros.contains(y)) { continue; }

                Integer left = xZeros.lower(y);
                int order = y - (left == null ? -1 : left);

                Integer right = xZeros.higher(y);
                order = Math.min(order, (right == null ? N : right) - y);

                NavigableSet<Integer> yZeros = zeroCols.getOrDefault(y, emptySet);
                Integer top = yZeros.lower(x);
                order = Math.min(order, x - (top == null ? -1 : top));

                Integer bottom = yZeros.higher(x);
                order = Math.min(order, (bottom == null ? N : bottom) - x);

                res = Math.max(res, order);
            }
        }
        return res;
    }

    // Dynamic Programming + Set
    // time complexity: O(N^2), space complexity: O(N^2)
    // 191 ms(19.86%), 47.9 MB(32.49%) for 58 tests
    public int orderOfLargestPlusSign2(int N, int[][] mines) {
        Set<Integer> mineSet = new HashSet<>();
        for (int[] mine : mines) {
            mineSet.add(mine[0] * N + mine[1]);
        }
        int[][] left = new int[N + 2][N + 2];
        int[][] right = new int[N + 2][N + 2];
        int[][] top = new int[N + 2][N + 2];
        int[][] bottom = new int[N + 2][N + 2];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (!mineSet.contains(i * N + j)) {
                    left[i + 1][j + 1] = left[i + 1][j] + 1;
                }
                if (!mineSet.contains(j * N + i)) {
                    top[j + 1][i + 1] = top[j][i + 1] + 1;
                }
            }
            for (int j = N - 1; j >= 0; j--) {
                if (!mineSet.contains(i * N + j)) {
                    right[i + 1][j + 1] = right[i + 1][j + 2] + 1;
                }
                if (!mineSet.contains(j * N + i)) {
                    bottom[j + 1][i + 1] = bottom[j + 2][i + 1] + 1;
                }
            }
        }
        int res = 0;
        for (int x = 1; x <= N; x++) {
            for (int y = 1; y <= N; y++) {
                int order1 = Math.min(left[x][y], right[x][y]);
                int order2 = Math.min(top[x][y], bottom[x][y]);
                res = Math.max(res, Math.min(order1, order2));
            }
        }
        return res;
    }

    // Dynamic Programming + Set
    // time complexity: O(N^2), space complexity: O(N^2)
    // 172 ms(30.33%), 46 MB(59.93%) for 58 tests
    public int orderOfLargestPlusSign3(int N, int[][] mines) {
        Set<Integer> mineSet = new HashSet<>();
        for (int[] mine : mines) {
            mineSet.add(mine[0] * N + mine[1]);
        }
        int[][] dp = new int[N][N];
        for (int x = 0; x < N; x++) {
            for (int y = 0, count = 0; y < N; y++) {
                count = mineSet.contains(x * N + y) ? 0 : count + 1;
                dp[x][y] = count;
            }
            for (int y = N - 1, count = 0; y >= 0; y--) {
                count = mineSet.contains(x * N + y) ? 0 : count + 1;
                dp[x][y] = Math.min(dp[x][y], count);
            }
        }

        int res = 0;
        for (int y = 0; y < N; y++) {
            for (int x = 0, count = 0; x < N; x++) {
                count = mineSet.contains(x * N + y) ? 0 : count + 1;
                dp[x][y] = Math.min(dp[x][y], count);
            }
            for (int x = N - 1, count = 0; x >= 0; x--) {
                count = mineSet.contains(x * N + y) ? 0 : count + 1;
                dp[x][y] = Math.min(dp[x][y], count);
                res = Math.max(res, dp[x][y]);
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N^2), space complexity: O(N^2)
    // 29 ms(94.22%), 41.6 MB(72.20%) for 58 tests
    public int orderOfLargestPlusSign4(int N, int[][] mines) {
        int[][] dp = new int[N][N];
        for (int[] a : dp) {
            Arrays.fill(a, 1);
        }
        for (int[] mine : mines) {
            dp[mine[0]][mine[1]] = 0;
        }
        for (int x = 0; x < N; x++) {
            for (int y = 0, count = 0; y < N; y++) {
                count = (dp[x][y] != 0) ? count + 1 : 0;
                dp[x][y] = count;
            }
            for (int y = N - 1, count = 0; y >= 0; y--) {
                count = (dp[x][y] != 0) ? count + 1 : 0;
                dp[x][y] = Math.min(dp[x][y], count);
            }
        }
        int res = 0;
        for (int y = 0; y < N; y++) {
            for (int x = 0, count = 0; x < N; x++) {
                count = (dp[x][y] != 0) ? count + 1 : 0;
                dp[x][y] = Math.min(dp[x][y], count);
            }
            for (int x = N - 1, count = 0; x >= 0; x--) {
                count = (dp[x][y] != 0) ? count + 1 : 0;
                dp[x][y] = Math.min(dp[x][y], count);
                res = Math.max(res, dp[x][y]);
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N^2), space complexity: O(N^2)
    // 30 ms(92.06%), 41.3 MB(89.89%) for 58 tests
    public int orderOfLargestPlusSign5(int N, int[][] mines) {
        int[][] dp = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dp[i], N);
        }
        for (int[] m : mines) {
            dp[m[0]][m[1]] = 0;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0, k = N - 1, l = 0, r = 0, u = 0, d = 0; j < N; j++, k--) {
                dp[i][j] = Math.min(dp[i][j], l = (dp[i][j] == 0 ? 0 : l + 1));
                dp[i][k] = Math.min(dp[i][k], r = (dp[i][k] == 0 ? 0 : r + 1));
                dp[j][i] = Math.min(dp[j][i], u = (dp[j][i] == 0 ? 0 : u + 1));
                dp[k][i] = Math.min(dp[k][i], d = (dp[k][i] == 0 ? 0 : d + 1));
            }
        }
        int res = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                res = Math.max(res, dp[i][j]);
            }
        }
        return res;
    }

    private void test(int N, int[][] mines, int expected) {
        assertEquals(expected, orderOfLargestPlusSign(N, mines));
        assertEquals(expected, orderOfLargestPlusSign2(N, mines));
        assertEquals(expected, orderOfLargestPlusSign3(N, mines));
        assertEquals(expected, orderOfLargestPlusSign4(N, mines));
        assertEquals(expected, orderOfLargestPlusSign5(N, mines));
    }

    @Test public void test() {
        test(5, new int[][] {{4, 2}}, 2);
        test(2, new int[][] {}, 1);
        test(1, new int[][] {{0, 0}}, 0);
        test(2, new int[][] {{0, 0}, {0, 1}, {1, 0}}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
