import org.junit.Test;

import static org.junit.Assert.*;

// LC991: https://leetcode.com/problems/broken-calculator/
//
// On a broken calculator that has a number showing on its display, we can perform two operations:
// Double: Multiply the number on the display by 2, or;
// Decrement: Subtract 1 from the number on the display.
// Initially, the calculator is displaying the number X.
// Return the minimum number of operations needed to display the number Y.
//
// Note:
// 1 <= X <= 10^9
// 1 <= Y <= 10^9
public class BrokenCalculator {
    // Greedy
    // time complexity: O(log(Y)), space complexity: O(1)
    // 0 ms(100.00%), 36.1 MB(10.17%) for 84 tests
    public int brokenCalc(int X, int Y) {
        for (int res = 0, y = Y; ; res++) {
            if (y <= X) { return res + X - y; }

            if (y % 2 == 1) {
                y++;
            } else {
                y /= 2;
            }
        }
    }

    // Greedy
    // time complexity: O(log(Y)), space complexity: O(1)
    // 0 ms(100.00%), 36 MB(16.31%) for 84 tests
    public int brokenCalc2(int X, int Y) {
        int res = 0;
        int powerOf2 = 1;
        for (; X * powerOf2 < Y; powerOf2 <<= 1, res++) {}
        for (int diff = X * powerOf2 - Y; diff > 0; powerOf2 >>= 1) {
            res += diff / powerOf2;
            diff -= diff / powerOf2 * powerOf2;
        }
        return res;
    }

    // Greedy + Recursion
    // time complexity: O(log(Y)), space complexity: O(1)
    // 0 ms(100.00%), 35.8 MB(51.77%) for 84 tests
    public int brokenCalc3(int X, int Y) {
        if (X >= Y) { return X - Y; }

        return (Y & 1) == 0 ? 1 + brokenCalc3(X, Y / 2) : 1 + brokenCalc3(X, Y + 1);
    }

    // Greedy + Recursion
    // time complexity: O(log(Y)), space complexity: O(1)
    // 0 ms(100.00%), 35.8 MB(51.77%) for 84 tests
    public int brokenCalc4(int X, int Y) {
        return (X >= Y) ? (X - Y) : (1 + Y % 2 + brokenCalc4(X, (Y + 1) / 2));
    }

    private void test(int X, int Y, int expected) {
        assertEquals(expected, brokenCalc(X, Y));
        assertEquals(expected, brokenCalc2(X, Y));
        assertEquals(expected, brokenCalc3(X, Y));
        assertEquals(expected, brokenCalc4(X, Y));
    }

    @Test public void test() {
        test(2, 3, 2);
        test(5, 8, 2);
        test(3, 10, 3);
        test(1024, 1, 1023);
        test(1, 1000000000, 39);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
