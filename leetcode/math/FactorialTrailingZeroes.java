import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC172: https://leetcode.com/problems/factorial-trailing-zeroes/
//
// Given an integer n, return the number of trailing zeroes in n!
public class FactorialTrailingZeroes {
    // Solution of Choice
    // beats 5.71%(2 ms for 502 tests)
    public int trailingZeroes(int n) {
        int factorFives = 0;
        // for (int i = 5; i <= n; i *= 5) { // may overflow
        //     factorFives += n / i;
        // }
        for (int i = n / 5; i > 0; i /= 5) {
            factorFives += i;
        }
        return factorFives;
    }

    // Recursion
    // beats 5.71%(2 ms for 502 tests)
    public int trailingZeroes2(int n) {
        return n == 0 ? 0 : n / 5 + trailingZeroes2(n / 5);
    }

    void test(int x, int expected) {
        assertEquals(expected, trailingZeroes(x));
        assertEquals(expected, trailingZeroes2(x));
    }

    @Test
    public void test1() {
        test(5, 1);
        test(10, 2);
        test(99, 22);
        test(100, 24);
        test(1808548329, 452137076);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FactorialTrailingZeroes");
    }
}
