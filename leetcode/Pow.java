import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Implement pow(x, n).
public class Pow {
    // beats 30.73%
    public double myPow(double x, int n) {
        if (n == 0) return 1;

        if (x == 1) return 1;

        if (n == Integer.MIN_VALUE) {
            return myPow(x, n + 2) / x / x;
        }

        if (n < 0) return myPow(1 / x, -n);

        double pow = myPow(x, n / 2);
        pow *= pow;
        if ((n % 2) == 1) {
            pow *= x;
        }
        return pow;
    }

    void test(double x, int n) {
        assertEquals(Math.pow(x, n), myPow(x, n), 1e-08);
    }

    @Test
    public void test1() {
        test(3, 5);
        test(-3, 5);
        test(-3, 6);
        test(-1, 0);
        test(0, 1);
        test(0, 0);
        test(0, -1);
        test(2, Integer.MAX_VALUE);
        test(2, Integer.MIN_VALUE);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Pow");
    }
}
