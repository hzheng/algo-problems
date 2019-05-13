import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1014: https://leetcode.com/problems/best-sightseeing-pair/
//
// Given an array A of positive integers, A[i] represents the value of the i-th sightseeing spot,
// and two sightseeing spots i and j have distance j - i between them. The score of a pair (i < j)
// of sightseeing spots is (A[i] + A[j] + i - j) : the sum of the values of the sightseeing spots,
// minus the distance between them. Return the maximum score of a pair of sightseeing spots.
// Note:
// 2 <= A.length <= 50000
// 1 <= A[i] <= 1000
public class BestSightseeingPair {
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(68.01%), 46.4 MB(100%) for 52 tests
    public int maxScoreSightseeingPair(int[] A) {
        int n = A.length;
        int[] dp = new int[n + 1];
        for (int i = n - 1; i > 0; i--) {
            dp[i] = Math.max(dp[i + 1], A[i] - i);
        }
        int res = 0;
        for (int i = 0; i < n - 1; i++) {
            res = Math.max(A[i] + i + dp[i + 1], res);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 4 ms(68.01%), 46.4 MB(100%) for 52 tests
    public int maxScoreSightseeingPair2(int[] A) {
        int res = 0;
        int cur = 0;
        for (int a : A) {
            res = Math.max(res, cur + a);
            cur = Math.max(cur, a) - 1; // best value decreased by 1 for the next position
        }
        return res;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, maxScoreSightseeingPair(A));
        assertEquals(expected, maxScoreSightseeingPair2(A));
    }

    @Test
    public void test() {
        test(new int[]{8, 1, 5, 2, 6}, 11);
        test(new int[]{81, 21, 5, 12, 2, 6, 99, 12, 96, 134, 12, 25, 31, 48, 91}, 230);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
