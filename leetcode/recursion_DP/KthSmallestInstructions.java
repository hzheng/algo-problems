import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1643: https://leetcode.com/problems/kth-smallest-instructions/
//
// Bob is standing at cell (0, 0), and he wants to reach destination: (row, column). He can only
// travel right and down. You are going to help Bob by providing instructions for him to reach
// destination. The instructions are represented as a string, where each character is either:
// 'H', meaning move horizontally (go right),
// or 'V', meaning move vertically (go down).
// Multiple instructions will lead Bob to destination. However, Bob is very picky. Bob has a lucky
// number k, and he wants the kth lexicographically smallest instructions that will lead him to
// destination. k is 1-indexed.
// Given an integer array destination and an integer k, return the kth lexicographically smallest
// instructions that will take Bob to destination.
// Constraints:
// destination.length == 2
// 1 <= row, column <= 15
// 1 <= k <= nCr(row + column, row), where nCr(a, b) denotes a choose b
public class KthSmallestInstructions {
    // Dynamic Programming
    // time complexity: O(N^2), space complexity: O(N)
    // 1 ms(100%), 38.6 MB(10%) for 462 tests
    public String kthSmallestPath(int[] destination, int k) {
        int v = destination[0];
        int n = destination[1] + v;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            C[i][0] = 1;
            for (int j = 1; j <= i; j++) {
                C[i][j] = C[i - 1][j - 1] + C[i - 1][j];
            }
        }
        char[] res = new char[n];
        for (int i = 0, j = v, count = k; i < n; i++) {
            int remaining = count - C[n - i - 1][j];
            if (remaining <= 0) {
                res[i] = 'H';
            } else {
                res[i] = 'V';
                count = remaining;
                j--;
            }
        }
        return String.valueOf(res);
    }

    private void test(int[] destination, int k, String expected) {
        assertEquals(expected, kthSmallestPath(destination, k));
    }

    @Test public void test() {
        test(new int[] {2, 3}, 2, "HHVHV");
        test(new int[] {2, 3}, 1, "HHHVV");
        test(new int[] {2, 3}, 3, "HHVVH");
        test(new int[] {4, 7}, 9, "HHHHHVHVVVH");
        test(new int[] {7, 10}, 99, "HHHHHHHVVVHVVVHVH");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
