import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC970: https://leetcode.com/problems/powerful-integers/
//
// Given two non-negative integers x and y, an integer is powerful if it is 
// equal to x^i + y^j for some integers i >= 0 and j >= 0. Return a list of all 
// powerful integers that have value less than or equal to bound.
// You may return the answer in any order, each value should occur at most once.
public class PowerfulIntegers {
    // beats 96.00%(5 ms for 90 tests)
    public List<Integer> powerfulIntegers(int x, int y, int bound) {
        Set<Integer> set = new HashSet<>();
        if (x < y) {
            int tmp = x;
            x = y;
            y = tmp;
        }
        if (x == 1) {
            if (bound > 1) {
                set.add(2);
            }
        } else {
            for (long a = 1; a < bound; a *= x) {
                for (long b = 1; a + b <= bound; b *= y) {
                    set.add((int)(a + b));
                    if (y == 1) break;
                }
            }
        }
        return new ArrayList<>(set);
    }

    // beats 44.00%(6 ms for 90 tests)
    public List<Integer> powerfulIntegers2(int x, int y, int bound) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 18 && Math.pow(x, i) <= bound; i++) {
            for (int j = 0; j < 18 && Math.pow(y, j) <= bound; j++) {
                int v = (int)Math.pow(x, i) + (int)Math.pow(y, j);
                if (v > bound) break;

                set.add(v);
            }
        }
        return new ArrayList<>(set);
    }

    // beats 96.00%(5 ms for 90 tests)
    public List<Integer> powerfulIntegers3(int x, int y, int bound) {
        Set<Integer> set = new HashSet<>();
        for (int a = 1; a < bound; a *= (x > 1) ? x : (bound + 1)) {
            for (int b = 1; a + b <= bound; b *= (y > 1) ? y : (bound + 1)) {
                set.add(a + b);
            }
        }
        return new ArrayList<>(set);
    }

    void test(int x, int y, int bound, Integer[] expected) {
        List<Integer> exp = Arrays.asList(expected);
        assertEquals(exp, powerfulIntegers(x, y, bound));
        assertEquals(exp, powerfulIntegers2(x, y, bound));
        assertEquals(exp, powerfulIntegers3(x, y, bound));
    }

    @Test
    public void test() {
        test(2, 3, 10, new Integer[] {2, 3, 4, 5, 7, 9, 10});
        test(3, 5, 15, new Integer[] {2, 4, 6, 8, 10, 14});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
