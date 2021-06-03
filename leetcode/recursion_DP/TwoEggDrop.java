import org.junit.Test;

import static org.junit.Assert.*;

// LC1884: https://leetcode.com/problems/egg-drop-with-2-eggs-and-n-floors/
//
// You are given two identical eggs and you have access to a building with n floors labeled from 1
// to n.
// You know that there exists a floor f where 0 <= f <= n such that any egg dropped at a floor
// higher than f will break, and any egg dropped at or below floor f will not break.
// In each move, you may take an unbroken egg and drop it from any floor x (where 1 <= x <= n). If
// the egg breaks, you can no longer use it. However, if the egg does not break, you may reuse it
// in future moves.
// Return the minimum number of moves that you need to determine with certainty what the value of f
// is.
//
// Constraints:
// 1 <= n <= 1000
public class TwoEggDrop {
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N), space complexity: O(N)
    // 76 ms(23.91%), 38.8 MB(13.04%) for 138 tests
    public int twoEggDrop(int n) {
        final int eggs = 2;
        return minMove(n, eggs, new int[n + 1][eggs + 1]);
    }

    private int minMove(int floors, int eggs, int[][] dp) {
        if (eggs == 1 || floors < 2) { return floors; }
        if (dp[floors][eggs] > 0) { return dp[floors][eggs]; }

        int res = floors;
        for (int drop = 1; drop <= floors; drop++) {
            res = Math.min(res, Math.max(minMove(drop - 1, eggs - 1, dp),
                                         minMove(floors - drop, eggs, dp)));
        }
        return dp[floors][eggs] = res + 1;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 37.6 MB(73.91%) for 138 tests
    public int twoEggDrop2(int n) {
        int move = 1;
        for (int floor = 1; floor < n; ) {
            floor += ++move;
        }
        return move;
    }

    // Math
    // time complexity: O(1), space complexity: O(1)
    // 1 ms(80.43%), 35.7 MB(93.48%) for 138 tests
    public int twoEggDrop3(int n) {
        // smallest x s.t. (x * (x + 1) / 2) >= n
        return (int)Math.ceil((Math.sqrt(1 + 8 * n) - 1) / 2);
    }

    private void test(int n, int expected) {
        assertEquals(expected, twoEggDrop(n));
        assertEquals(expected, twoEggDrop2(n));
        assertEquals(expected, twoEggDrop3(n));
    }

    @Test public void test1() {
        test(2, 2);
        test(100, 14);
        test(1000, 45);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
