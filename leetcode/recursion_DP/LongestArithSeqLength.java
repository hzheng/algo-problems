import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1027: https://leetcode.com/problems/longest-arithmetic-subsequence/
//
// Given an array A of integers, return the length of the longest arithmetic subsequence in A.
//
// Constraints:
// 2 <= A.length <= 1000
// 0 <= A[i] <= 500
public class LongestArithSeqLength {
    // Dynamic Programming + Hash Table
    // time complexity: O(N^2), space complexity: O(N^2)
    // 506 ms(61.52%), 64.3 MB(12.48%) for 55 tests
    public int longestArithSeqLength(int[] A) {
        int n = A.length;
        Map<Integer, Integer>[] dp = new Map[n];
        int res = 0;
        for (int i = 0; i < n; i++) {
            dp[i] = new HashMap<>();
            for (int j = 0; j < i; j++) {
                int d = A[i] - A[j];
                int count = dp[j].getOrDefault(d, 1) + 1;
                dp[i].put(d, count);
                res = Math.max(res, count);
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N^2), space complexity: O(N*MAX)
    // 37 ms(96.16%), 46.8 MB(96.31%) for 55 tests
    public int longestArithSeqLength2(int[] A) {
        int n = A.length;
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int a : A) {
            min = Math.min(min, a);
            max = Math.max(max, a);
        }
        int maxDiff = max - min;
        int[][] dp = new int[n][maxDiff + max + 1];
        int res = 0;
        for (int i = 1; i < n; i++) {
            int[] map = dp[i];
            for (int j = 0; j < i; j++) {
                int d = A[i] - A[j] + maxDiff;
                int count = map[d] = dp[j][d] + 1;
                res = Math.max(res, count);
            }
        }
        return res + 1;
    }

    private void test(int[] A, int expected) {
        assertEquals(expected, longestArithSeqLength(A));
        assertEquals(expected, longestArithSeqLength2(A));
    }

    @Test public void test() {
        test(new int[] {3, 6, 9, 12}, 4);
        test(new int[] {9, 4, 7, 2, 10}, 3);
        test(new int[] {20, 1, 15, 3, 10, 5, 8}, 4);
        test(new int[] {3, 5, 6, 5, 9, 4, 10, 3, 2}, 5);
        test(new int[] {3, 3, 5, 6, 5, 3, 3, 3, 9, 4, 10, 3, 2}, 6);
        test(new int[] {3, 5, 6, 9, 10, 12}, 4);
        test(new int[] {3, 3}, 2);
        test(new int[] {3, 5}, 2);
        test(new int[] {44, 46, 22, 68, 45, 66, 43, 9, 37, 30, 50, 67, 32, 47, 44, 11, 15, 4, 11, 6,
                        20, 64, 54, 54, 61, 63, 23, 43, 3, 12, 51, 61, 16, 57, 14, 12, 55, 17, 18,
                        25, 19, 28, 45, 56, 29, 39, 52, 8, 1, 21, 17, 21, 23, 70, 51, 61, 21, 52,
                        25, 28}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
