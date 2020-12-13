import org.junit.Test;

import static org.junit.Assert.*;

// LC1688: https://leetcode.com/problems/count-of-matches-in-tournament/
//
// You are given an integer n, the number of teams in a tournament that has strange rules:
// If the current number of teams is even, each team gets paired with another team. A total of n / 2
// matches are played, and n / 2 teams advance to the next round.
// If the current number of teams is odd, one team randomly advances in the tournament, and the rest
// gets paired. A total of (n - 1) / 2 matches are played, and (n - 1) / 2 + 1 teams advance to the
// next round. Return the number of matches played in the tournament until a winner is decided.
//
// Constraints:
// 1 <= n <= 200
public class NumberOfMatches {
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 35.4 MB(100.00%) for 200 tests
    public int numberOfMatches(int n) {
        int res = 0;
        for (int a = n; a > 1; a = (a + 1) / 2) {
            res += a / 2;
        }
        return res;
    }

    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.4 MB(100.00%) for 200 tests
    public int numberOfMatches2(int n) {
        return n - 1;
    }

    private void test(int n, int expected) {
        assertEquals(expected, numberOfMatches(n));
        assertEquals(expected, numberOfMatches2(n));
    }

    @Test public void test() {
        test(7, 6);
        test(14, 13);
        test(24, 23);
        test(99, 98);
        test(100, 99);
        test(149, 148);
        test(200, 199);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
