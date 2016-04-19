import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 9.8:
 * Given an infinite number of quarters, dimes, nickels and pennies, calculate
 * the number of ways of representing n cents.
 */
public class ChangeMaker {
    public static int makeChange(int n) {
        if (n < 0) return 0;

        n /= 5;
        // problem becomes: how many unique triples (x, y, z) s.t.
        // x * 1 + y * 2 + z * 5 <= n;
        int count = 0;
        for (int z = n / 5; z >= 0; z--) {
            // given z, find all possible pair (x, y) s.t. (x + 2y) <= (n - 5z)
            count += makeChangeFor1or2(n - 5 * z);
            // count += makeChangeFor(n - 5 * z, 2);
        }
        return count;
    }

    private static int makeChangeFor1or2(int n) {
        int count = 0;
        for (int i = n / 2; i >= 0; i--) {
            count += (n - i * 2) + 1;
        }
        return count;
    }

    private static int makeChangeFor(int n, int unit) {
        int count = 0;
        for (int i = n / unit; i >= 0; i--) {
            count += (n - i * unit) + 1;
        }
        return count;
    }

    // from the book
    public static int makeChange2(int n) {
        return makeChange2(n, 25);
    }

    private static int makeChange2(int n, int denom) {
        int next_denom = 0;
        switch (denom) {
        case 25:
            next_denom = 10;
            break;
        case 10:
            next_denom = 5;
            break;
        case 5:
            next_denom = 1;
            break;
        case 1:
            return 1;
        }
        int ways = 0;
        for (int i = 0; i * denom <= n; i++) {
            ways += makeChange2(n - i * denom, next_denom);
        }
        return ways;
    }

    // improve on <tt>makeChange2</tt>
    public static int makeChange3(int n) {
        return makeChange3(n, new int[] {25, 10, 5, 1}, 0);
    }

    private static int makeChange3(int n, int[] denoms, int k) {
        if (k + 1 >= denoms.length) return 1;

        if (denoms[k + 1] == 1) { // dramtically improve efficiency
            return n / denoms[k] + 1;
        }

        int ways = 0;
        // for (int i = n / denoms[k]; i >= 0; i--) {
        for (int i = 0; i * denoms[k] <= n; i++) {
            ways += makeChange3(n - i * denoms[k], denoms, k + 1);
        }
        return ways;
    }

    private void test(int[] n, String f, Function<Integer, Integer> changes) {
        long t1 = System.nanoTime();
        for (int i = 0; i < n.length; i++) {
            n[i] = changes.apply(i);
        }
        System.out.format("\n%s spent %.3f ms\n", f,
                          (System.nanoTime() - t1) * 1e-6);
    }

    @Test
    public void test() {
        // makeChange is 20+/300 times faster than makeChange2
        // when amount=1000 or 3000 respectively.
        // (makeChange3 is 6 times/30 times faster than makeChange2)

        int amount = 1000;
        System.out.println("amount = " + amount);
        int[] changes = new int[amount];
        test(changes, "makeChange", ChangeMaker::makeChange);
        int[] changes2 = new int[amount];
        test(changes2, "makeChange2", ChangeMaker::makeChange2);
        int[] changes3 = new int[amount];
        test(changes3, "makeChange3", ChangeMaker::makeChange3);
        assertArrayEquals(changes2, changes3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ChangeMaker");
    }
}
