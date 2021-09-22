import org.junit.Test;

import static org.junit.Assert.*;

// LC1954: https://leetcode.com/problems/minimum-garden-perimeter-to-collect-enough-apples/
//
// In a garden represented as an infinite 2D grid, there is an apple tree planted at every integer
// coordinate. The apple tree planted at an integer coordinate (i, j) has |i| + |j| apples growing
// on it.
// You will buy an axis-aligned square plot of land that is centered at (0, 0).
// Given an integer neededApples, return the minimum perimeter of a plot such that at least
// neededApples apples are inside or on the perimeter of that plot.
//
// Constraints:
// 1 <= neededApples <= 10^15
public class MinimumPerimeter {
    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 35.9 MB(68.67%) for 306 tests
    public long minimumPerimeter(long neededApples) {
        long low = 1;
        for (long high = 1_000_000L; low < high; ) {
            long mid = (low + high) >>> 1;
            if (isEnough(neededApples, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low * 8;
    }

    private boolean isEnough(long tgt, long n) {
        // 12 * (1^2+2^2+...+n^2)
        return n * (n + 1) * (2 * n + 1) * 2 >= tgt;
    }

    private void test(long neededApples, long expected) {
        assertEquals(expected, minimumPerimeter(neededApples));
    }

    @Test public void test() {
        test(1, 8);
        test(13, 16);
        test(1000000000, 5040);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
