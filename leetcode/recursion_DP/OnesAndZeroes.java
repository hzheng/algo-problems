import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC474: https://leetcode.com/problems/ones-and-zeroes/
//
// Suppose you are a dominator of m 0s and n 1s respectively. On the other hand,
// there is an array with strings consisting of only 0s and 1s.
// Now your task is to find the maximum number of strings that you can form with
// given m 0s and n 1s. Each 0 and 1 can be used at most once.
// Note:
// The given numbers of 0s and 1s will both not exceed 100
// The size of given string array won't exceed 600.
public class OnesAndZeroes {
    // Dynamic Programming
    // beats N/A(29 ms for 60 tests)
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (String s : strs) {
            int zeros = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') {
                    zeros++;
                }
            }
            int ones = s.length() - zeros;
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        return dp[m][n];
    }

    void test(String[] strs, int m, int n, int expected) {
        assertEquals(expected, findMaxForm(strs, m, n));
    }

    @Test
    public void test() {
        test(new String[]{"10", "0001", "111001", "1", "0"}, 5, 3, 4);
        test(new String[]{"10", "0", "1"}, 1, 1, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("OnesAndZeroes");
    }
}
