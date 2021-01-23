import org.junit.Test;

import static org.junit.Assert.*;

// LC1732: https://leetcode.com/problems/find-the-highest-altitude/
//
// There is a biker going on a road trip. The road trip consists of n + 1 points at different
// altitudes. The biker starts his trip on point 0 with altitude equal 0.
// You are given an integer array gain of length n where gain[i] is the net gain in altitude between
// points i and i + 1 for all (0 <= i < n). Return the highest altitude of a point.
// Constraints:
// n == gain.length
// 1 <= n <= 100
// -100 <= gain[i] <= 100
public class LargestAltitude {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.5 MB(100%) for 80 tests
    public int largestAltitude(int[] gain) {
        int res = 0;
        int cur = 0;
        for (int g : gain) {
            cur += g;
            res = Math.max(res, cur);
        }
        return res;
    }

    private void test(int[] gain, int expected) {
        assertEquals(expected, largestAltitude(gain));
    }

    @Test public void test() {
        test(new int[] {-5, 1, 5, 0, -7}, 1);
        test(new int[] {-4, -3, -2, -1, 4, 3, 2}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
