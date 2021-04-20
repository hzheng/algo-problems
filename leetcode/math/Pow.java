import org.junit.Test;
import static org.junit.Assert.*;

// LC050: https://leetcode.com/problems/powx-n/
//
// Implement pow(x, n).
public class Pow {
    // Recursion
    // beats 11.79%(23 ms) // old percentage: beats 30.73%(1 ms)
    public double myPow(double x, int n) {
        if (n == 0) return 1;

        if (x == 1) return 1;

        if (n == Integer.MIN_VALUE) return myPow(x, n + 1) / x;

        if (n < 0) return myPow(1 / x, -n);

        double pow = myPow(x, n / 2);
        pow *= pow;
        return ((n % 2) == 1) ? pow * x : pow;
    }

    // Iteration
    // beats 11.79%(23 ms)
    public double myPow2(double x, int n) {
        if (n == 0) return 1;

        if (x == 1) return 1;

        double base = x;
        int exp = n;
        if (n == Integer.MIN_VALUE) {
            exp++; // avoid overflow
        }
        if (exp < 0) {
            base = 1 / x;
            exp = -exp;
        }
        double[] powers = new double[32];
        powers[0] = base;
        for (int i = 1, powerOf2 = 2; powerOf2 < exp && i < 32; powerOf2 <<= 1, i++) {
            powers[i] = powers[i - 1] * powers[i - 1];
        }
        double pow = base;
        exp--;
        for (int i = 0, powerOf2 = 1; powerOf2 <= exp && i < 32; powerOf2 <<= 1, i++) {
            if ((exp & powerOf2) != 0) {
                pow *= powers[i];
            }
        }
        return n == Integer.MIN_VALUE ? pow / x : pow;
    }

    // Solution of Choice
    // Iteration
    // beats 38.10%(19 ms)
    public double myPow3(double x, int n) {
        double pow = 1;
        double base = x;
        for (long exp = Math.abs((long)n); exp > 0; base *= base, exp >>= 1) {
            if ((exp & 1) != 0) {
                pow *= base;
            }
        }
        return n < 0 ?  1 / pow : pow;
    }

    void test(double x, int n) {
        assertEquals(Math.pow(x, n), myPow(x, n), 1e-08);
        assertEquals(Math.pow(x, n), myPow2(x, n), 1e-08);
        assertEquals(Math.pow(x, n), myPow3(x, n), 1e-08);
    }

    @Test
    public void test1() {
        test(3, 9);
        test(3, 5);
        test(-3, 5);
        test(-3, 6);
        test(-1, 0);
        test(3, 15);
        test(30.12, 10);
        test(0, 1);
        test(0, 2);
        test(0, 0);
        test(0, -1);
        test(2, Integer.MAX_VALUE);
        test(-1, Integer.MIN_VALUE);
        test(2, Integer.MIN_VALUE);
        // test(30.12, 15); // only work in myPow?
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
