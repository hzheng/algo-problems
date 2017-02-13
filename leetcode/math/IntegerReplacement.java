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
    // Solution of Choice
    // Bit Manipulation
    // beats 50.78%(4 ms)
    public int integerReplacement(int n) {
        int count = 0;
        for (int i = n; i > 1; i >>>= 1, count++) {
            if ((i & 1) != 0) {
                count++;
                if (i > 3 && (i & 3) == 3) {
                // or: if (i != 3 && (i & 2) == 2) {
                    i++;
                }
            }
        }
        return count;
    }

    // Recursion
    // beats 29.42%(6 ms for 47 tests)
    public int integerReplacement2(int n) {
        if (n <= 1) return 0;

        if (n == Integer.MAX_VALUE) return 32;
        // if (n == Integer.MAX_VALUE) return integerReplacement2(n >> 1) + 1;

        if ((n & 1) == 0) return integerReplacement2(n >> 1) + 1;

        return Math.min(integerReplacement2((n + 1) >> 1), integerReplacement2(n >> 1)) + 2;
        // should be slower
        // return Math.min(integerReplacement2((n + 1)), integerReplacement2(n - 1)) + 1;
    }

    // Dynamic Programming(Top-Down)
    // beats 0.74%(70 ms for 47 tests)
    public int integerReplacement2_2(int n) {
        if (n == Integer.MAX_VALUE) return 32;

        return integerReplacement(n, new HashMap<>());
    }

    private int integerReplacement(int n, Map<Integer, Integer> memo) {
        if (n <= 1) return 0;

        Integer res = memo.get(n);
        if (res != null) return res;

        if ((n & 1) == 0) {
            res = integerReplacement(n >> 1) + 1;
        } else {
            res = Math.min(integerReplacement((n + 1) >> 1, memo),
                           integerReplacement(n >> 1, memo)) + 2;
        }
        memo.put(n, res);
        return res;
    }

    // Recursion
    // beats 29.42%(6 ms for 47 tests)
    public int integerReplacement3(int n) {
        return integerReplacement3((long)n);
    }

    private int integerReplacement3(long n) {
        if (n <= 1) return 0;

        if ((n & 1) == 0) return integerReplacement3(n >> 1) + 1;

        return Math.min(integerReplacement3((n + 1) >> 1), integerReplacement3(n >> 1)) + 2;
        // should be slower
        // return Math.min(integerReplacement3((n + 1)), integerReplacement3(n - 1)) + 1;
    }

    void test(int n, int expected) {
        assertEquals(expected, integerReplacement(n));
        assertEquals(expected, integerReplacement2(n));
        assertEquals(expected, integerReplacement2_2(n));
        assertEquals(expected, integerReplacement3(n));
    }

    @Test
    public void test1() {
        test(0, 0);
        test(1, 0);
        test(2, 1);
        test(3, 2);
        test(4, 2);
        test(5, 3);
        test(6, 3);
        test(7, 4);
        test(8, 3);
        test(9, 4);
        test(10, 4);
        test(11, 5);
        test(12, 4);
        test(15, 5);
        test(16, 4);
        test(65535, 17);
        test(2147483646, 32);
        test(2147483647, 32);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IntegerReplacement");
    }
}
