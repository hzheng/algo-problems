// https://leetcode.com/problems/bulb-switcher/

import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;
//
// There are n bulbs that are initially off. You first turn on all the bulbs.
// Then, you turn off every second bulb. On the third round, you toggle every
// third bulb. For the ith round, you toggle every i bulb. For the nth round,
// you only toggle the last bulb. Find how many bulbs are on after n rounds.
public class BulbSwitcher {
    // time complexity: O(N ^ 2), space complexity: O(N)
    //  Time Limit Exceeded
    public int bulbSwitch(int n) {
        boolean[] bulbs = new boolean[n];
        for (int i = 0; i < n; i++) {
            for (int j = 1; j <= n; j++) {
                if ((i + 1) % j == 0) {
                    bulbs[i] = !bulbs[i];
                }
            }
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (bulbs[i]) {
                count++;
            }
        }
        return count;
    }

    // time complexity: O(N ^ 0.5), space complexity: O(1)
    // beats 2.01%(1 ms)
    public int bulbSwitch2(int n) {
        int count = 0;
        for (int i = 1; i * i <= n; i++) {
            count++;
        }
        return count;
    }

    // time complexity: O(1), space complexity: O(1)
    // beats 28.29%(0 ms)
    public int bulbSwitch3(int n) {
        return (int)Math.sqrt(n);
    }

    void test(Function<Integer, Integer> bulbSwitch, String name,
              int n, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)bulbSwitch.apply(n));
        if (n > 5000) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int n, int expected) {
        BulbSwitcher b = new BulbSwitcher();
        if (n < 5000) {
            test(b::bulbSwitch, "bulbSwitch", n, expected);
        }
        test(b::bulbSwitch2, "bulbSwitch2", n, expected);
        test(b::bulbSwitch3, "bulbSwitch3", n, expected);
    }

    @Test
    public void test1() {
        test(0, 0);
        test(3, 1);
        test(10, 3);
        test(31, 5);
        test(52, 7);
        test(49, 7);
        test(3456, 58);
        test(99999, 316);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BulbSwitcher");
    }
}
