import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC397: https://leetcode.com/problems/integer-replacement/
//
// Given a positive integer n and you can do operations as follow:
// If n is even, replace n with n/2.
// If n is odd, you can replace n with either n + 1 or n - 1.
// What is the minimum number of replacements needed for n to become 1?
public class IntegerReplacement {
    // iteration
    // beats N/A(5 ms)
    public int integerReplacement(int n) {
        if (n == Integer.MAX_VALUE) return 32;

        int count = 0;
        for (; n > 1; n >>= 1, count++) {
            if ((n & 1) != 0) {
                count++;
                if (n > 3 && (n & 3) == 3) {
                    n++;
                }
            }
        }
        return count;
    }

    // recursion
    // beats N/A(7 ms)
    public int integerReplacement2(int n) {
        if (n <= 1) return 0;

        if (n == Integer.MAX_VALUE) return 32;
        // if (n == Integer.MAX_VALUE) return integerReplacement2(n >> 1) + 1;

        if ((n & 1) == 0) return integerReplacement2(n >> 1) + 1;

        return Math.min(integerReplacement2((n + 1)), integerReplacement2(n - 1)) + 1;
    }

    // recursion
    // beats N/A(7 ms)
    public int integerReplacement3(int n) {
        return integerReplacement3((long)n);
    }

    private int integerReplacement3(long n) {
        if (n <= 1) return 0;

        if ((n & 1) == 0) return integerReplacement3(n >> 1) + 1;

        return Math.min(integerReplacement3((n + 1)), integerReplacement3(n - 1)) + 1;
    }

    void test(int n, int expected) {
        assertEquals(expected, integerReplacement(n));
        assertEquals(expected, integerReplacement2(n));
        assertEquals(expected, integerReplacement3(n));
    }

    @Test
    public void test1() {
        test(2147483647, 32);
        // test(15, 5);
        // test(0, 0);
        // test(1, 0);
        // test(2, 1);
        // test(3, 2);
        // test(4, 2);
        // test(5, 3);
        // test(6, 3);
        // test(7, 4);
        // test(8, 3);
        // test(9, 4);
        // test(10, 4);
        // test(16, 4);
        // test(65535, 17);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IntegerReplacement");
    }
}
