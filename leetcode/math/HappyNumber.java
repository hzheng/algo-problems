import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/happy-number/
//
// Write an algorithm to determine if a number is "happy".
// A happy number is a number defined by the following process: Starting with
// any positive integer, replace the number by the sum of the squares of its
// digits, and repeat the process until the number equals 1 (where it will stay),
// or it loops endlessly in a cycle which does not include 1. Those numbers for
// which this process ends in 1 are happy numbers.
public class HappyNumber {
    // beats 31.23%(6 ms)
    public boolean isHappy(int n) {
        Set<Integer> calculated = new HashSet<>();
        for (int i = n; i != 1; i = calculate(i)) {
            if (calculated.contains(i)) return false;

            calculated.add(i);
        }
        return true;
    }

    private int calculate(int n) {
        int res = 0;
        for (int x = n; x > 0; x /= 10) {
            int mod = x % 10;
            res += mod * mod;
        }
        return res;
    }

    void test(int n, boolean expected) {
        assertEquals(expected, isHappy(n));
    }

    @Test
    public void test1() {
        test(19, true);
        test(1, true);
        test(1234, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HappyNumber");
    }
}
