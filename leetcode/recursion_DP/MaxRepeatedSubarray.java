import java.math.BigInteger;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC718: https://leetcode.com/problems/maximum-length-of-repeated-subarray/
//
// Given two integer arrays A and B, return the maximum length of an subarray
// that appears in both arrays.
public class MaxRepeatedSubarray {
    // Dynamic Programming
    // time complexity: O(|A| * |B|), space complexity: O(|A| * |B|)
    // beats %(93 ms for 54 tests)
    public int findLength(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        int[][] dp = new int[m + 1][n + 1];
        int res = 0;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (A[i - 1] != B[j - 1]) continue;

                dp[i][j] = dp[i - 1][j - 1] + 1;
                res = Math.max(res, dp[i][j]);
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(|A| * |B|), space complexity: O(|A| * |B|)
    // beats %(86 ms for 54 tests)
    public int findLength2(int[] A, int[] B) {
        int m = A.length;
        int n = B.length;
        int[][] dp = new int[m + 1][n + 1];
        int res = 0;
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (A[i] == B[j]) {
                    dp[i][j] = dp[i + 1][j + 1] + 1;
                    res = Math.max(res, dp[i][j]);
                }
            }
        }
        return res;
    }

    // Binary Search + Rolling Hash
    // https://leetcode.com/articles/maximum-length-of-repeated-subarray/ #4
    // time complexity: O((|A| + |B|) * log(min(|A|, |B|))), space complexity: O(|A|)
    // beats %(49 ms for 54 tests)
    public int findLength3(int[] A, int[] B) {
        int low = 0;
        for (int high = Math.min(A.length, B.length) + 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (check(A, B, mid)) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low - 1;
    }

    private boolean check(int[] A, int[] B, int length) {
        Map<Integer, Integer> hashes = new HashMap<>();
        int i = 0;
        for (int x : rolling(A, length)) {
            hashes.put(x, i++);
        }
        int j = 0;
        for (int x : rolling(B, length)) {
            i = hashes.getOrDefault(x, -1);
            if (i >= 0 && Arrays.equals(Arrays.copyOfRange(A, i, i + length),
                                        Arrays.copyOfRange(B, j, j + length))) {
                return true;
            }
            j++;
        }
        return false;
    }

    private int[] rolling(int[] source, int length) {
        int[] res = new int[source.length - length + 1];
        if (length == 0) return res;

        final int MOD = 1_000_000_007;
        final int P = 113;
        final int P_INV = BigInteger.valueOf(P).
                          modInverse(BigInteger.valueOf(MOD)).intValue();
        long h = 0;
        long power = 1;
        for (int i = 0; i < source.length; i++) {
            h = (h + source[i] * power) % MOD;
            if (i < length - 1) {
                power = (power * P) % MOD;
            } else {
                res[i - (length - 1)] = (int)h;
                h = (h - source[i - (length - 1)]) * P_INV % MOD;
                if (h < 0) {
                    h += MOD;
                }
            }
        }
        return res;
    }

    void test(int[] A, int[] B, int expected) {
        assertEquals(expected, findLength(A, B));
        assertEquals(expected, findLength2(A, B));
        assertEquals(expected, findLength3(A, B));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 2, 1}, new int[] {3, 2, 1, 4, 7}, 3);
        test(new int[] {1, 2, 3, 2, 3, 1}, new int[] {2, 3, 2, 3, 1, 4, 7}, 5);
        test(new int[] {0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
             new int[] {0, 0, 0, 0, 0, 0, 0, 1, 0, 0}, 9);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
