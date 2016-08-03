import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/count-numbers-with-unique-digits/
//
// Given a non-negative integer n, count all numbers with unique digits, x,
// where 0 ≤ x < 10 ^ n.
public class CountNumberswithUniqueDigits {
    // Combinatorics
    // beats 10.58%(0 ms)
    public int countNumbersWithUniqueDigits(int n) {
        if (n == 0) return 1;

        int count = 10;
        int lastPerm = 9;
        int min = 11 - Math.min(n, 10);
        for (int i = 9; i >= min; i--) {
            lastPerm *= i;
            count += lastPerm;
        }
        return count;
    }

    // Combinatorics
    // https://discuss.leetcode.com/topic/47983/java-dp-o-1-solution/2
    // beats 10.58%(0 ms)
    public int countNumbersWithUniqueDigits2(int n) {
        if (n == 0) return 1;

        int count = 10;
        for (int avail = 9, unique = 9; n > 1 && avail > 0; n--, avail--) {
            unique *= avail;
            count += unique;
        }
        return count;
    }

    // TODO: DP
    // TODO: Backtracking

    void test(int n, int expected) {
        assertEquals(expected, countNumbersWithUniqueDigits(n));
        assertEquals(expected, countNumbersWithUniqueDigits2(n));
    }

    @Test
    public void test1() {
        test(0, 1);
        test(1, 10);
        test(2, 91);
        test(3, 739);
        test(4, 5275);
        test(10, 8877691);
        test(11, 8877691);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountNumberswithUniqueDigits");
    }
}
