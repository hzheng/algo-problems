import org.junit.Test;
import static org.junit.Assert.*;

// LC367: https://leetcode.com/problems/valid-perfect-square/
//
// Given a positive integer num, write a function which returns True if num is
// a perfect square else False.
public class PerfectSquare {
    // Newton method
    // time complexity: O(log(N))
    // beats 35.86%(0 ms for 67 tests)
    public boolean isPerfectSquare(int num) {
        double a = num / 2d;
        for (double b = 1; b >= 1; a -= b) {
            b = (a * a - num) / (2 * a);
        }
        return (int)a * (int)a == num;
    }

    // Solution of Choice
    // Newton method
    // https://en.wikipedia.org/wiki/Integer_square_root#Using_only_integer_division
    // time complexity: O(log(N))
    // beats 35.86%(0 ms for 67 tests)
    public boolean isPerfectSquare2(int num) {
        for (int a = num / 2 + 1, b;; a = (a + b) / 2) {
            b = num / a;
            if (a <= b) return a * a == num;
        }
    }

    // Binary Search
    // time complexity: O(log(N))
    // beats 35.86%(0 ms for 67 tests)
    public boolean isPerfectSquare3(int num) {
        for (int low = 1, high = num / 2 + 1; low <= high; ) {
            int mid = (low + high) >>> 1;
            int inverse = num / mid; // avoid overflow(optionally, we can use long)
            if (mid == inverse) return num % mid == 0;

            if (mid < inverse) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return false;
    }

    // time complexity: O(N ^ 1/2)
    // beats 18.23%(1 ms)
    public boolean isPerfectSquare4(int num) {
        for (int i = 1; num > 0; num -= i, i += 2) {}
        return num == 0;
    }

    void test(int num, boolean expected) {
        assertEquals(expected, isPerfectSquare(num));
        assertEquals(expected, isPerfectSquare2(num));
        assertEquals(expected, isPerfectSquare3(num));
        assertEquals(expected, isPerfectSquare4(num));
    }

    @Test
    public void test1() {
        test(1, true);
        test(2, false);
        test(3, false);
        test(808201, true);
        for (int i = 2; i < 100; i++) {
            int square = i * i;
            test(square, true);
            test(square - 1, false);
            test(square + 1, false);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PerfectSquare");
    }
}
