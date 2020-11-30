import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1672: https://leetcode.com/problems/richest-customer-wealth/
//
// You are given an m x n integer grid accounts where accounts[i][j] is the amount of money the
// ith customer has in the jth bank. Return the wealth that the richest customer has.
// A customer's wealth is the amount of money they have in all their bank accounts. The richest
// customer is the customer that has the maximum wealth.
//
// Constraints:
// m == accounts.length
// n == accounts[i].length
// 1 <= m, n <= 50
// 1 <= accounts[i][j] <= 100
public class MaximumWealth {
    // time complexity: O(M*N), space complexity: O(1)
    // 0 ms(100.00%), 39 MB(50.00%) for 34 tests
    public int maximumWealth(int[][] accounts) {
        int res = 0;
        for (int[] account : accounts) {
            int total = 0;
            for (int a : account) {
                total += a;
            }
            res = Math.max(res, total);
        }
        return res;
    }

    // time complexity: O(M*N), space complexity: O(1)
    // 3 ms(100.00%), 38.3 MB(100.00%) for 34 tests
    public int maximumWealth2(int[][] accounts) {
        return Arrays.stream(accounts).mapToInt(a -> Arrays.stream(a).sum()).max().getAsInt();
    }

    private void test(int[][] accounts, int expected) {
        assertEquals(expected, maximumWealth(accounts));
        assertEquals(expected, maximumWealth2(accounts));
    }

    @Test public void test() {
        test(new int[][] {{1, 2, 3}, {3, 2, 1}}, 6);
        test(new int[][] {{1, 5}, {7, 3}, {3, 5}}, 10);
        test(new int[][] {{2, 8, 7}, {7, 1, 3}, {1, 9, 5}}, 17);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
