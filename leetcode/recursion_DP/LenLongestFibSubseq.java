import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC873: https://leetcode.com/problems/length-of-longest-fibonacci-subsequence/
//
// Given a strictly increasing array A of positive integers forming a sequence, 
// find the length of the longest fibonacci-like subsequence of A.  If one does 
// not exist, return 0.
public class LenLongestFibSubseq {
    // Set
    // time complexity: O(N ^ 2 * log(Max(A))), space complexity: O(N)
    // beats 90.70%(74 ms for 33 tests)
    public int lenLongestFibSubseq(int[] A) {
        Set<Integer> set = new HashSet<>();
        for (int a : A) {
            set.add(a);
        }
        int n = A.length;
        int res = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int l = 2;
                for (int a = A[i], b = A[j], c = a + b; set.contains(c); l++) {
                    a = b;
                    b = c;
                    c = a + b;
                }
                res = Math.max(res, l);
            }
        }
        return res > 2 ? res : 0;
    }

    // Dynamic Programming + Hash Table(Longest Increasing Subsequence)
    // time complexity: O(N ^ 2), space complexity: O(N * log(Max(A)))
    // beats 75.03%(81 ms for 33 tests)
    public int lenLongestFibSubseq2(int[] A) {
        int n = A.length;
        Map<Integer, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(A[i], i);
        }
        Map<Integer, Integer> longest = new HashMap<>();
        int res = 0;
        for (int k = 1; k < n; k++) {
            for (int j = 0; j < k; j++) {
                int i = index.getOrDefault(A[k] - A[j], -1);
                if (i >= 0 && i < j) {
                    int cand = longest.getOrDefault(i * n + j, 2) + 1;
                    longest.put(j * n + k, cand);
                    res = Math.max(res, cand);
                }
            }
        }
        return res >= 3 ? res : 0;
    }

    // Dynamic Programming(Longest Increasing Subsequence)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 92.13%(70 ms for 33 tests)
    public int lenLongestFibSubseq3(int[] A) {
        int n = A.length;
        Map<Integer, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(A[i], i);
        }
        int[][] dp = new int[n][n];
        int res = 0;
        for (int k = 1; k < n; k++) {
            for (int j = 0; j < k; j++) {
                int i = index.getOrDefault(A[k] - A[j], -1);
                if (i >= 0 && i < j) {
                    dp[i][j] = Math.max(2, dp[i][j]);
                    res = Math.max(res, dp[j][k] = dp[i][j] + 1);
                }
            }
        }
        return res >= 3 ? res : 0;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, lenLongestFibSubseq(A));
        assertEquals(expected, lenLongestFibSubseq2(A));
        assertEquals(expected, lenLongestFibSubseq3(A));
    }

    @Test
    public void test() {
        test(new int[] { 1, 2, 3, 4, 5, 6, 7, 8 }, 5);
        test(new int[] { 1, 3, 7, 11, 12, 14, 18 }, 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
