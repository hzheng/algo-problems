import org.junit.Test;

import static org.junit.Assert.*;

// LC1601: https://leetcode.com/problems/maximum-number-of-achievable-transfer-requests/
//
// We have n buildings numbered from 0 to n - 1. Each building has a number of employees. It's
// transfer season, and some employees want to change the building they reside in.
// You are given an array requests where requests[i] = [fromi, toi] represents an employee's request
// to transfer from building fromi to building toi.
// All buildings are full, so a list of requests is achievable only if for each building, the net
// change in employee transfers is zero. This means the number of employees leaving is equal to the
// number of employees moving in. For example if n = 3 and two employees are leaving building 0, one
// is leaving building 1, and one is leaving building 2, there should be two employees moving to
// building 0, one employee moving to building 1, and one employee moving to building 2.
// Return the maximum number of achievable requests.
//
// Constraints:
// 1 <= n <= 20
// 1 <= requests.length <= 16
// requests[i].length == 2
// 0 <= fromi, toi < n
public class MaximumRequests {
    // Bit Manipulation
    // time complexity: O(2^R*(N+R)), space complexity: O(N)
    // 64 ms(50.00%), 38.7 MB(45.83%) for 117 tests
    public int maximumRequests(int n, int[][] requests) {
        int res = 0;
        outer:
        for (int mask = (1 << requests.length) - 1; mask > 0; mask--) {
            int bits = Integer.bitCount(mask);
            if (bits <= res) { continue; }

            int[] count = new int[n];
            for (int i = requests.length - 1; i >= 0; i--) {
                if (((mask >> i) & 1) == 0) { continue; }

                int[] req = requests[i];
                count[req[0]]--;
                count[req[1]]++;
            }
            for (int a : count) {
                if (a != 0) { continue outer; }
            }
            res = bits;
        }
        return res;
    }

    // Recursion + DFS + Backtracking
    // time complexity: O(2^R*(N+R)), space complexity: O(N)
    // 24 ms(82.14%), 36.8 MB(74.40%) for 117 tests
    public int maximumRequests2(int n, int[][] requests) {
        return dfs(requests, 0, 0, new int[n]);
    }

    private int dfs(int[][] requests, int cur, int transfers, int[] count) {
        if (cur == requests.length) {
            for (int i : count) {
                if (i != 0) { return 0; }
            }
            return transfers;
        }
        int[] req = requests[cur];
        count[req[0]]++;
        count[req[1]]--;
        int res1 = dfs(requests, cur + 1, transfers + 1, count);
        count[req[0]]--;
        count[req[1]]++;
        int res2 = dfs(requests, cur + 1, transfers, count);
        return Math.max(res1, res2);
    }

    private void test(int n, int[][] requests, int expected) {
        assertEquals(expected, maximumRequests(n, requests));
        assertEquals(expected, maximumRequests2(n, requests));
    }

    @Test public void test() {
        test(2, new int[][] {{1, 1}, {1, 0}, {0, 1}, {0, 0}, {0, 0}, {0, 1}, {0, 1}, {1, 0}, {1, 0},
                             {1, 1}, {0, 0}, {1, 0}}, 11);
        test(5, new int[][] {{0, 1}, {1, 0}, {0, 1}, {1, 2}, {2, 0}, {3, 4}}, 5);
        test(3, new int[][] {{0, 0}, {1, 2}, {2, 1}}, 3);
        test(4, new int[][] {{0, 3}, {3, 1}, {1, 2}, {2, 0}}, 4);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
