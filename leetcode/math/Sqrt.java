import org.junit.Test;
import static org.junit.Assert.*;

// LC069: https://leetcode.com/problems/sqrtx/
//
// Compute and return the square root of x.
public class Sqrt {
    // Binary Search
    // beats 21.72%(3 ms)
    public int mySqrt(int x) {
        if (x < 0) return -1;

        if (x == 0) return 0;

        if (x < 4) return 1;

        // sqrt(x) <= x / 2 if x >= 4
        long high = Math.min(x / 2, (1 << 16));
        long low = 2;
        while (low <= high) {
            long mid = (low + high) >>> 1;
            long square = mid * mid;
            if (square == x) return (int)mid;

            if (square < x) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return (int)low - 1;
    }

    // Solution of Choice
    // Binary Search
    // beats 21.72%(3 ms)
    public int mySqrt2(int x) {
        int low = 1;
        for (int high = Integer.MAX_VALUE >> 1; low <= high; ) {
            int mid = (low + high) >>> 1;
            if (mid > x / mid) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low - 1;
    }

    // Newton method
    // http://introcs.cs.princeton.edu/java/21function/Newton.java.html
    // beats 83.10%(2 ms)
    public int mySqrt3(int x) {
        if (x < 0) return -1;

        if (x == 0) return 0;

        if (x < 4) return 1;

        int sqrt = x / 2;
        int diff;
        while (true) {
            int y = x / sqrt;
            diff = sqrt - y;
            if (diff < 2 && diff > -2) break;

            sqrt = (y + sqrt) / 2;
        }
        return diff > 0 ? sqrt - 1 : sqrt;
    }

    // Newton method
    // beats 83.10%(2 ms)
    public int mySqrt4(int x) {
        long sqrt = x;
        while (sqrt * sqrt > x) {
            sqrt = (sqrt + x / sqrt) >> 1;
        }
        return (int)sqrt;
    }

    // Solution of Choice
    // Newton method(also looks like Binary Search)
    // beats 83.10%(2 ms)
    public int mySqrt5(int x) {
        if (x == 0) return 0;

        int sqrt = x;
        while (sqrt > x / sqrt) {
            sqrt = (sqrt + x / sqrt) >>> 1; // >>> avoids overflow
        }
        return sqrt;
    }

    void test(int x) {
        assertEquals((int)Math.sqrt(x), mySqrt(x));
        assertEquals((int)Math.sqrt(x), mySqrt2(x));
        assertEquals((int)Math.sqrt(x), mySqrt3(x));
        assertEquals((int)Math.sqrt(x), mySqrt4(x));
        assertEquals((int)Math.sqrt(x), mySqrt5(x));
    }

    @Test
    public void test1() {
        for (int i = 0; i < 10000; i++) {
            test(i);
        }
    }

    @Test
    public void test2() {
        test(2147483647);
        test(1073395599);
        test(2147395599);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Sqrt");
    }
}
