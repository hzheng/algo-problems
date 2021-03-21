import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1798: https://leetcode.com/problems/maximum-number-of-consecutive-values-you-can-make/
//
// You are given an integer array coins of length n which represents the n coins that you own. The
// value of the ith coin is coins[i]. You can make some value x if you can choose some of your n
// coins such that their values sum up to x.
// Return the maximum number of consecutive integer values that you can make with your coins
// starting from and including 0.
// Note that you may have multiple coins of the same value.
//
// Constraints:
// coins.length == n
// 1 <= n <= 4 * 10^4
// 1 <= coins[i] <= 4 * 10^4
public class GetMaximumConsecutive {
    // Sort + Greedy
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 21 ms(%), 87.6 MB(%) for 71 tests
    public int getMaximumConsecutive(int[] coins) {
        Arrays.sort(coins);
        int res = 1;
        for (int coin : coins) {
            if (coin > res) { break; }

            res += coin;
        }
        return res;
    }

    private void test(int[] coins, int expected) {
        assertEquals(expected, getMaximumConsecutive(coins));
    }

    @Test public void test() {
        test(new int[] {1, 3}, 2);
        test(new int[] {1, 1, 1, 4}, 8);
        test(new int[] {1, 4, 10, 3, 1}, 20);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
