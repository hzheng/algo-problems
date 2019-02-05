import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC982: https://leetcode.com/problems/triples-with-bitwise-and-equal-to-zero/
//
// Given an array of integers A, find the number of triples of indices (i, j, k)
// such that:
// 0 <= i < A.length
// 0 <= j < A.length
// 0 <= k < A.length
// A[i] & A[j] & A[k] == 0, where & represents the bitwise-AND operator.
// Note:
// 1 <= A.length <= 1000
// 0 <= A[i] < 2^16
public class CountTriplets {
    // Dynamic Programming
    // time complexity: O(3 * 2 ^ 16 * N), space complexity: O(2 ^ 16)
    // 608 ms(27.20%), 26.9 MB(100.00%) for 25 tests
    public int countTriplets(int[] A) {
        int n = 1 << 16;
        int[] dp = new int[n];
        dp[n - 1] = 1;
        for (int i = 0; i < 3; i++) {
            int[] ndp = new int[n];
            for (int j = 0; j < n; j++) {
                for (int a : A) {
                    ndp[j & a] += dp[j];
                }
            }
            dp = ndp;
        }
        return dp[0];
    }

    // Dynamic Programming
    // time complexity: O(3 * 2 ^ 16 * N), space complexity: O(3 * 2 ^ 16)
    // 679 ms(20.97%), 22.2 MB(100.00%) for 25 tests
    public int countTriplets2(int[] A) {
        int n = 1 << 16;
        int k = 3;
        int[][] dp = new int[k + 1][n];
        dp[0][n - 1] = 1;
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < n; j++) {
                for (int a : A) {
                    dp[i + 1][j & a] += dp[i][j];
                }
            }
        }
        return dp[k][0];
    }

    // Hash Table
    // time complexity: O(N ^ 2), space complexity: O(N)
    // 780 ms(12.47%), 33.4 MB(100.00%) for 25 tests
    public int countTriplets3(int[] A) {
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int a : A) {
            for (int b : A) {
                int num = a & b;
                map.put(num, map.getOrDefault(num, 0) + 1);
            }
        }
        for (int a : A) {
            for (int num : map.keySet()) {
                if ((a & num) == 0) {
                    res += map.get(num);
                }
            }
        }
        return res;
    }

    // Fast Walsh–Hadamard transform
    // https://leetcode.com/problems/triples-with-bitwise-and-equal-to-zero/discuss/226832/Fastest-Solution-Using-Fast-Walsh-Hadamard-Transform-32ms
    // https://en.wikipedia.org/wiki/Fast_Walsh%E2%80%93Hadamard_transform
    // time complexity: O(32 * 2 ^ 16 + N), space complexity: O(2 ^ 16)
    // 34 ms(98.02%), 21.2 MB(100.00%) for 25 tests
    public int countTriplets4(int[] A) {
        int n = 1 << 16;
        int[] dp = new int[n];
        for (int a : A) {
            dp[a]++;
        }
        fwht(dp, false);
        for (int i = 0; i < n; i++) {
            dp[i] *= dp[i] * dp[i];
        }
        fwht(dp, true);
        return dp[0];
    }

    private void fwht(int[] dp, boolean inv) {
        int n = dp.length;
        for (int m = 1; 2 * m <= n; m <<= 1) {
            for (int i = 0; i < n; i += 2 * m) {
                for (int j = 0; j < m; j++) {
                    int x = dp[i + j];
                    int y = dp[i + j + m];
                    if (inv) {
                        dp[i + j] = -x + y;
                        dp[i + j + m] = x;
                    } else {
                        dp[i + j] = y;
                        dp[i + j + m] = x + y;
                    }
                }
            }
        }
    }

    // Fast Walsh–Hadamard transform
    // time complexity: O(32 * 2 ^ 16 + N), space complexity: O(2 ^ 16)
    // 57 ms(90.23%), 21.2 MB(100.00%) for 25 tests
    public int countTriplets5(int[] A) {
        int n = 1 << 16;
        int[] dp = new int[n];
        for (int a : A) {
            dp[a]++;
        }
        fwht(dp, 1);
        for (int i = 0; i < n; i++) {
            dp[i] *= dp[i] * dp[i];
        }
        fwht(dp, -1);
        return dp[0];
    }

    private void fwht(int[] dp, int sign) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < dp.length; j++) {
                if ((j << ~i) >= 0) {
                    dp[j] += dp[j | 1 << i] * sign;
                }
            }
        }
    }

    // Bit Manipulation
    // time complexity: O(N * 2 ^ 16), space complexity: O(1)
    // 88 ms(86.60%), 25.8 MB(100.00%) for 25 tests
    public int countTriplets6(int[] A) {
        int res = 0;
        for (int i = (1 << 16) - 1; i >= 0; i--) {
            int count = 0;
            for (int a : A) {
                if ((a & i) == i) {
                    count++;
                }
            }
            res += ((Integer.bitCount(i) & 1) == 0 ? 1 : -1) * count * count * count;
        }
        return res;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, countTriplets(A));
        assertEquals(expected, countTriplets2(A));
        assertEquals(expected, countTriplets3(A));
        assertEquals(expected, countTriplets4(A));
        assertEquals(expected, countTriplets5(A));
        assertEquals(expected, countTriplets6(A));
    }

    @Test
    public void test() {
        test(new int[] {2, 1, 3}, 12);
        test(new int[] {12, 21, 9, 7, 3}, 36);
        test(new int[] {123, 78, 51, 852, 9, 7, 3, 316}, 180);
        test(new int[] {123, 123, 78, 51, 852, 9, 7, 3, 316}, 210);
        test(new int[] {2, 1, 3, 8, 5, 7, 10}, 216);
        test(new int[] {13, 4, 2, 1, 3, 8, 5, 7, 10}, 486);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
