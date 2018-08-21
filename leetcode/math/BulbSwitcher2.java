import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC672: https://leetcode.com/problems/bulb-switcher-ii/
//
// There is a room with n lights which are turned on initially and 4 buttons.
// After performing exactly m unknown operations towards buttons, you need to
// return how many different kinds of status of the n lights could be.
// Suppose n lights are labeled as number [1, 2, 3 ..., n], function of these 4
// buttons are given below:
// Flip all the lights.
// Flip lights with even numbers.
// Flip lights with odd numbers.
// Flip lights with (3k + 1) numbers, k = 0, 1, 2, ...
public class BulbSwitcher2 {
    // beats 100.00%(3 ms for 80 tests)
    public int flipLights(int n, int m) {
        if (m == 0 || n == 0) return 1;

        if (n == 1) return 2;

        if (n == 2) return (m == 1) ? 3 : 4;

        if (m == 1) return 4;

        return (m == 2) ? 7 : 8;
    }

    // Set
    // beats 20.15%(6 ms for 80 tests)
    public int flipLights2(int n, int m) {
        Set<Integer> seen = new HashSet<>();
        n = Math.min(n, 6);
        int shift = Math.max(0, 6 - n);
        for (int operation = 0; operation < 16; operation++) {
            int bits = Integer.bitCount(operation);
            if (bits % 2 == m % 2 && bits <= m) {
                int lights = 0;
                if (((operation >> 0) & 1) > 0) {
                    lights ^= 0b111111 >> shift;
                }
                if (((operation >> 1) & 1) > 0) {
                    lights ^= 0b010101 >> shift;
                }
                if (((operation >> 2) & 1) > 0) {
                    lights ^= 0b101010 >> shift;
                }
                if (((operation >> 3) & 1) > 0) {
                    lights ^= 0b100100 >> shift;
                }
                seen.add(lights);
            }
        }
        return seen.size();
    }

    // beats 71.71%(4 ms for 80 tests)
    public int flipLights3(int n, int m) {
        n = Math.min(n, 3);
        return Math.min(1 << n, m * n + 1);
    }

    void test(int n, int m, int expected) {
        assertEquals(expected, flipLights(n, m));
        assertEquals(expected, flipLights2(n, m));
        assertEquals(expected, flipLights3(n, m));
    }

    @Test
    public void test() {
        test(0, 3, 1);
        test(1, 1, 2);
        test(2, 1, 3);
        test(3, 1, 4);
        test(4, 2, 7);
        test(6, 3, 8);
        test(600, 320, 8);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
