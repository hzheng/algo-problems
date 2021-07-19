import org.junit.Test;

import static org.junit.Assert.*;

// LC1936: https://leetcode.com/problems/add-minimum-number-of-rungs/
//
// You are given a strictly increasing integer array rungs that represents the height of rungs on a
// ladder. You are currently on the floor at height 0, and you want to reach the last rung.
// You are also given an integer dist. You can only climb to the next highest rung if the distance
// between where you are currently at (the floor or on a rung) and the next rung is at most dist.
// You are able to insert rungs at any positive integer height if a rung is not already there.
// Return the minimum number of rungs that must be added to the ladder in order for you to climb to
// the last rung.
//
// Constraints:
// 1 <= rungs.length <= 10^5
// 1 <= rungs[i] <= 10^9
// 1 <= dist <= 10^9
// rungs is strictly increasing.
public class AddRungs {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 52.1 MB(28.57%) for 117 tests
    public int addRungs(int[] rungs, int dist) {
        int res = 0;
        int prev = 0;
        for (int cur : rungs) {
            res += (cur - prev - 1) / dist;
            prev = cur;
        }
        return res;
    }

    void test(int[] rungs, int dist, int expected) {
        assertEquals(expected, addRungs(rungs, dist));
    }

    @Test public void test() {
        test(new int[] {1, 3, 5, 10}, 2, 2);
        test(new int[] {3, 6, 8, 10}, 3, 0);
        test(new int[] {3, 4, 6, 7}, 2, 1);
        test(new int[] {5}, 10, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
