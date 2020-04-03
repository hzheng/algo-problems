import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1362: https://leetcode.com/problems/closest-divisors/
//
// Given an integer num, find the closest two integers in absolute difference whose product equals num + 1 or num + 2.
// Return the two integers in any order.
// Constraints:
// 1 <= num <= 10^9
public class ClosestDivisors {
    // time complexity: O(N^1/2), space complexity: O(1)
    // 7 ms(81.50%), 37.4 MB(100%) for 113 tests
    public int[] closestDivisors(int num) {
        int[] res1 = closeDivisor(num + 1);
        int[] res2 = closeDivisor(num + 2);
        return (res1[0] - res1[1] >= res2[0] - res2[1]) ? res2 : res1;
    }

    private int[] closeDivisor(int num) {
        for (int i = (int) Math.sqrt(num); ; i--) {
            if (num % i == 0) {
                return new int[]{num / i, i};
            }
        }
    }

    // time complexity: O(N^1/2), space complexity: O(1)
    // 5 ms(98.61%), 36.9 MB(100%) for 113 tests
    public int[] closestDivisors2(int num) {
        for (int i = (int) Math.sqrt(num + 2); ; i--) {
            if ((num + 1) % i == 0) return new int[]{i, (num + 1) / i};

            if ((num + 2) % i == 0) return new int[]{i, (num + 2) / i};
        }
    }

    void test(int num, int[] expected, Function<Integer, int[]> f) {
        int[] res = f.apply(num);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(int num, int[] expected) {
        ClosestDivisors c = new ClosestDivisors();
        test(num, expected, c::closestDivisors);
        test(num, expected, c::closestDivisors2);
    }

    @Test
    public void test() {
        test(8, new int[]{3, 3});
        test(123, new int[]{5, 25});
        test(999, new int[]{25, 40});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
