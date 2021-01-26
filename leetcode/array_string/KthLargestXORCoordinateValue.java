import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1738: https://leetcode.com/problems/find-kth-largest-xor-coordinate-value/
//
// You are given a 2D matrix of size m x n, consisting of non-negative integers. You are also given
// an integer k. The value of coordinate (a, b) of the matrix is the XOR of all matrix[i][j] where
// 0 <= i <= a < m and 0 <= j <= b < n (0-indexed). Find the kth largest value (1-indexed) of all
// the coordinates of matrix.
//
// Constraints:
// m == matrix.length
// n == matrix[i].length
// 1 <= m, n <= 1000
// 0 <= matrix[i][j] <= 10^6
// 1 <= k <= m * n
public class KthLargestXORCoordinateValue {
    // Heap + Dynamic Programming
    // time complexity: O(M*N*log(K)), space complexity: O(M*N)
    // 215 ms(82.98%), 198.7 MB(38.83%) for 49 tests
    public int kthLargestValue(int[][] matrix, int k) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] dp = new int[m + 1][n + 1];
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int a = dp[i + 1][j + 1] = dp[i][j + 1] ^ dp[i + 1][j] ^ dp[i][j] ^ matrix[i][j];
                pq.offer(a);
                if (pq.size() > k) {
                    pq.poll();
                }
            }
        }
        return pq.peek();
    }

    private void test(int[][] matrix, int k, int expected) {
        assertEquals(expected, kthLargestValue(matrix, k));
    }

    @Test public void test() {
        test(new int[][] {{5, 2}, {1, 6}}, 1, 7);
        test(new int[][] {{5, 2}, {1, 6}}, 2, 5);
        test(new int[][] {{5, 2}, {1, 6}}, 3, 4);
        test(new int[][] {{5, 2}, {1, 6}}, 4, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
