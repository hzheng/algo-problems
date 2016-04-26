import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.1:
 * Adds two numbers without + or any arithmetic operators.
 */
public class TrickyAdd {
    private static final int MIN_INT = 1 << 31;
    private static final int NEGATIVE_MASK = ~MIN_INT;

    public static int add(int a, int b) {
        int positiveA = a;
        int positiveB = b;
        if (a < 0) {
            positiveA &= NEGATIVE_MASK;
        }
        if (b < 0) {
            positiveB &= NEGATIVE_MASK;
        }
        int sum = addNonnegative(positiveA, positiveB);
        if (a >= 0 && b >= 0) return sum;

        if (a < 0 && b < 0) {
            return (sum < 0) ? (sum | MIN_INT) : (sum & NEGATIVE_MASK);
        } else {
            return (sum >= 0) ? (sum | MIN_INT) : (sum & NEGATIVE_MASK);
        }
    }

    private static int addNonnegative(int a, int b) {
        if (a == 0) return b;
        if (b == 0) return a;

        int hiSum = addNonnegative(a >> 1, b >> 1);
        if ((a & b & 1) > 0) {
            hiSum = addNonnegative(hiSum, 1);
        }
        int lowSum = (a & 1) ^ (b & 1);
        return (hiSum << 1) | lowSum;
    }

    // from the book
    public static int add2(int a, int b) {
        if (b == 0) return a;

        int sum = a ^ b; // add without carrying
        int carry = (a & b) << 1; // carry, but don't add
        return add2(sum, carry); // recurse
    }


    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private int test(Function<Integer, Integer, Integer> add,
                     String name, int a, int b) {
        long t1 = System.nanoTime();
        int n = add.apply(a, b);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return n;
    }

    private void test(int a, int b) {
        int s1 = test(TrickyAdd::add, "add", a, b);
        int s2 = test(TrickyAdd::add2, "add2", a, b);
        assertEquals(a + b, s1);
        assertEquals(a + b, s2);
    }

    @Test
    public void test1() {
        test(0, 0);
        test(1, 0);
        test(0, 1);
        test(1, 1);
        test(3, 1);
        test(3, 2);
        test(7, 8);
        test(77, 98);
        test(7, -8);
        test(17, -8);
        test(-8, 8);
        test(-8, -8);
        test(-9, -8);
        test(-1999, -9918);
        test(-1484225525, -785383178);
    }

    private int rand() {
        return ThreadLocalRandom.current().nextInt();
    }

    @Test
    public void test2() {
        for (int i = 0; i < 100; i++) {
            test(rand(), rand());
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TrickyAdd");
    }
}
