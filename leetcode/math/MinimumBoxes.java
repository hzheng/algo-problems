import org.junit.Test;

import static org.junit.Assert.*;

// LC1739: https://leetcode.com/problems/building-boxes/
//
// You have a cubic storeroom where the width, length, and height of the room are all equal to n
// units. You are asked to place n boxes in this room where each box is a cube of unit side length.
// There are however some rules to placing the boxes:
// You can place the boxes anywhere on the floor.
// If box x is placed on top of the box y, then each side of the four vertical sides of the box y
// must either be adjacent to another box or to a wall. Given an integer n, return the minimum
// possible number of boxes touching the floor.
//
// Constraints:
// 1 <= n <= 10^9
public class MinimumBoxes {
    // time complexity: O(N^1/3), space complexity: O(1)
    // 1 ms(29.70%), 39.8 MB(67.55%) for 67 tests
    public int minimumBoxes(int n) {
        int pyramidSize = 1;
        int left = n;
        for (; ; pyramidSize++) {
            int nextLeft = left - pyramidSize * (pyramidSize + 1) / 2;
            if (nextLeft > 0) {
                left = nextLeft;
            } else { break; }
        }
        int extra = 0;
        for (; left > 0; left -= ++extra) {}
        return pyramidSize * (pyramidSize - 1) / 2 + extra;
    }

    // Binary Search
    // time complexity: O(N^1/3*log(N)), space complexity: O(1)
    // 15 ms(27.59%), 38.2 MB(18.23%) for 67 tests
    public int minimumBoxes2(int n) {
        int low = 0;
        for (int high = n; low < high; ) {
            int mid = (low + high) >>> 1;
            if (ok(mid, n)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean ok(int base, int n) {
        int i = 0;
        for (; (i + 1) * (i + 2) / 2 <= base; i++) {}
        int extra = base - i * (i + 1) / 2;
        for (int left = n; i > 0; i--) {
            left -= i * (i + 1) / 2 + extra;
            if (--extra < 0) {
                extra = 0;
            }
            if (left <= 0) { return true; }
        }
        return false;
    }

    // time complexity: O(N^1/3), space complexity: O(1)
    // 752 ms(5.17%), 37.6 MB(35.47%) for 67 tests
    public int minimumBoxes3(int n) {
        for (int i = 1; ; i++) {
            for (int j = 0; j <= i; j++) {
                if (i * (i + 1L) * (i + 2) / 6 + (j + 1L) * j / 2 >= n) {
                    return i * (i + 1) / 2 + j;
                }
            }
        }
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 35.9 MB(57.64%) for 67 tests
    public int minimumBoxes4(int n) {
        int pyramidSize = 0;
        for (int increase = 1; cube(pyramidSize + increase) <= n; increase = 1) {
            for (; cube(pyramidSize + increase * 2) <= n; increase *= 2) {}
            pyramidSize += increase;
        }
        n -= cube(pyramidSize);
        int extra = 0;
        for (int increase = 1; n > sum(extra); increase = 1) {
            for (; n > sum(extra + increase * 2); increase *= 2) {}
            extra += increase;
        }
        return sum(pyramidSize) + extra;
    }

    private int sum(int n) {
        return n * (n + 1) / 2;
    }

    private long cube(int n) {
        return n * (n + 1L) * (n + 2L) / 6;
    }

    // time complexity: O(1), space complexity: O(1)
    // 1 ms(89.16%), 37.5 MB(35.47%) for 67 tests
    public int minimumBoxes5(int n) {
        if (n <= 3) { return n; }

        int base1 = (int)(Math.cbrt(n * 6L)) - 1; // 1/6*x^3 <= n < 1/6(x+1)^3
        long sum = cube(base1);
        double base2 = (-1 + Math.sqrt(1 + 4 * 2 * (n - sum))) / 2; // 1/2*x(x+1)=n-sum
        return sum(base1) + (int)Math.ceil(base2);
    }

    private void test(int n, int expected) {
        assertEquals(expected, minimumBoxes(n));
        assertEquals(expected, minimumBoxes2(n));
        assertEquals(expected, minimumBoxes3(n));
        assertEquals(expected, minimumBoxes4(n));
        assertEquals(expected, minimumBoxes5(n));
    }

    @Test public void test() {
        test(3, 3);
        test(4, 3);
        test(10, 6);
        test(14, 9);
        test(20, 10);
        test(1000, 161);
        test(10000, 756);
        test(1000000000, 1650467);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
