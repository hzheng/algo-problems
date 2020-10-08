import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1561: https://leetcode.com/problems/maximum-number-of-coins-you-can-get/
//
// There are 3n piles of coins of varying size, you and your friends will take piles of coins as
// follows:
// In each step, you will choose any 3 piles of coins (not necessarily consecutive).
// Of your choice, Alice will pick the pile with the maximum number of coins.
// You will pick the next pile with maximum number of coins.
// Your friend Bob will pick the last pile.
// Repeat until there are no more piles of coins.
// Given an array of integers piles where piles[i] is the number of coins in the ith pile.
// Return the maximum number of coins which you can have.
// Constraints:
// 3 <= piles.length <= 10^5
// piles.length % 3 == 0
// 1 <= piles[i] <= 10^4
public class MaxCoins {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 24 ms(95.59%), 48.3 MB(98.87%) for 116 tests
    public int maxCoins(int[] piles) {
        Arrays.sort(piles);
        int res = 0;
        for (int n = piles.length, i = n - 2, j = 0; i > j; i -= 2, j++) {
            res += piles[i];
        }
        return res;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 25 ms(86.90%), 48.0 MB(99.69%) for 116 tests
    public int maxCoins2(int[] piles) {
        Arrays.sort(piles);
        int res = 0;
        for (int n = piles.length, i = n / 3; i < n; i += 2) {
            res += piles[i];
        }
        return res;
    }

    void test(int[] piles, int expected) {
        assertEquals(expected, maxCoins(piles));
        assertEquals(expected, maxCoins2(piles));
    }

    @Test public void test() {
        test(new int[] {2, 4, 1, 2, 7, 8}, 9);
        test(new int[] {2, 4, 5}, 4);
        test(new int[] {9, 8, 7, 6, 5, 1, 2, 3, 4}, 18);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
