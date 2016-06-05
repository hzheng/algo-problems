import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Compute and return the square root of x.
public class Sqrt {
    // beats 17.23%
    public int mySqrt(int x) {
        if (x < 0) return -1;

        if (x == 0) return 0;

        if (x < 4) return 1;

        // sqrt(x) <= x / 2 if x >= 4
        long high = Math.min(x / 2, (1 << 16));
        long low = 2;
        while (low <= high) {
            long mid = (low + high) / 2;
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

    // Newton method
    // http://introcs.cs.princeton.edu/java/21function/Newton.java.html
    // beats 17.23%
    public int mySqrt2(int x) {
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

    void test(int x) {
        assertEquals((int)Math.sqrt(x), mySqrt(x));
        assertEquals((int)Math.sqrt(x), mySqrt2(x));
    }

    @Test
    public void test1() {
        for (int i = 0; i < 10000; i++) {
            test(i);
        }
    }

    @Test
    public void test2() {
        test(1073395599);
        test(2147395599);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Sqrt");
    }
}
