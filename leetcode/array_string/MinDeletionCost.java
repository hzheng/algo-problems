import org.junit.Test;

import static org.junit.Assert.*;

// LC1578: https://leetcode.com/problems/minimum-deletion-cost-to-avoid-repeating-letters/
//
// Given a string s and an array of integers cost where cost[i] is the cost of deleting the ith
// character in s. Return the minimum cost of deletions such that there are no two identical letters
// next to each other.
// Notice that you will delete the chosen characters at the same time, in other words, after
// deleting a character, the costs of deleting other characters will not change.
// Constraints:
// s.length == cost.length
// 1 <= s.length, cost.length <= 10^5
// 1 <= cost[i] <= 10^4
// s contains only lowercase English letters.
public class MinDeletionCost {
    // 2-level loop
    // time complexity: O(N), space complexity: O(1)
    // 6 ms(93.52%), 57.1 MB(40.05%) for 113 tests
    public int minCost(String s, int[] cost) {
        int res = 0;
        for (int n = cost.length, i = 0; i < n; i++) {
            int maxCost = cost[i];
            int total = maxCost;
            for (char c = s.charAt(i); i < n - 1 && s.charAt(i + 1) == c; i++) {
                maxCost = Math.max(maxCost, cost[i + 1]);
                total += cost[i + 1];
            }
            res += (total - maxCost);
        }
        return res;
    }

    // 1-level loop
    // time complexity: O(N), space complexity: O(1)
    // 7 ms(87.74%), 55.5 MB(85.68%) for 113 tests
    public int minCost2(String s, int[] cost) {
        int res = cost[0];
        int maxCost = cost[0];
        for (int n = cost.length, i = 1; i < n; i++) {
            if (s.charAt(i) != s.charAt(i - 1)) {
                res -= maxCost;
                maxCost = 0;
            }
            res += cost[i];
            maxCost = Math.max(maxCost, cost[i]);
        }
        return res - maxCost;
    }

    private void test(String s, int[] cost, int expected) {
        assertEquals(expected, minCost(s, cost));
        assertEquals(expected, minCost2(s, cost));
    }

    @Test public void test() {
        test("abaac", new int[] {1, 2, 3, 4, 5}, 3);
        test("abc", new int[] {1, 2, 3}, 0);
        test("aabaa", new int[] {1, 2, 3, 4, 1}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
