import org.junit.Test;

import static org.junit.Assert.*;

// LC960: https://leetcode.com/problems/delete-columns-to-make-sorted-iii/
//
// We are given an array A of N lowercase letter strings, all of the same length. Now, we may choose
// any set of deletion indices, and for each string, we delete all the characters in those indices.
// Suppose we chose a set of deletion indices D such that after deletions, the final array has every
// element (row) in lexicographic order. Return the minimum possible value of D.length.
//
// Note:
// 1 <= A.length <= 100
// 1 <= A[i].length <= 100
public class MinDeletionSize3 {
    // Dynamic Programming
    // time complexity: O(N*M^2), space complexity: O(N)
    // 7 ms(70.73%), 39 MB(30.49%) for 130 tests
    public int minDeletionSize(String[] A) {
        int m = A[0].length();
        int[] dp = new int[m];
        int maxLen = 0;
        for (int i = 0; i < m; i++) {
            dp[i] = 1;
            outer:
            for (int j = 0; j < i; j++) {
                for (String a : A) {
                    if (a.charAt(i) < a.charAt(j)) { continue outer; }
                }
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
            maxLen = Math.max(maxLen, dp[i]);
        }
        return m - maxLen;
    }

    private void test(String[] A, int expected) {
        assertEquals(expected, minDeletionSize(A));
    }

    @Test public void test() {
        test(new String[] {"babca", "bbazb"}, 3);
        test(new String[] {"edcba"}, 4);
        test(new String[] {"ghi", "def", "abc"}, 0);
        test(new String[] {"baabab"}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
