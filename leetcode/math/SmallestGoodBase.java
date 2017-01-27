import java.math.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC483: https://leetcode.com/problems/smallest-good-base/
//
// For an integer n, we call k>=2 a good base of n, if all digits of n base k are 1.
// Now given a string representing n, you should return the smallest good base
// of n in string format.
// Note:
// The range of n is [3, 10^18].
// The string representing n is always valid and will not have leading zeros.
public class SmallestGoodBase {
    // private static final int MAX_LEN = (int)(18 / Math.log10(2));

    // Binary Search
    // beats 36.34%(22 ms for 103 tests)
    public String smallestGoodBase(String n) {
        long num = Long.parseLong(n);
        for (int len = (int)(Math.log(num) / Math.log(2)) + 1;; len--) {
            long base = check(num, len);
            if (base > 0) return String.valueOf(base);
        }
    }

    private long check(long n, int len) {
        for (long low = 2, high = n - 1; low <= high; ) {
            long mid = (low + high) >>> 1;
            long res = compute(len, mid);
            if (res == n) return mid;
            if (res > n) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return 0;
    }

    private long compute(int len, long base) {
        for (long i = 0, res = 0, power = 1;; i++, power *= base) {
            res += power;
            if (i == len - 1) return res;
            if (power * base / base != power) return Long.MAX_VALUE;
            // power = Math.multiplyExact(power, base); //  Time Limit Exceeded
        }
    }

    // Binary Search
    // beats 78.21%(10 ms for 103 tests)
    public String smallestGoodBase2(String n) {
        long num = Long.parseLong(n);
        for (int len = (int)(Math.log(num) / Math.log(2)); len > 1; len--) {
            long base = check2(num, len);
            if (base > 0) return String.valueOf(base);
        }
        return String.valueOf(num - 1);
    }

    private long check2(long n, int len) {
        for (long low = 2, high = (long)Math.pow(n, 1.0 / len); low <= high; ) {
            long mid = (low + high) >>> 1;
            long sum = 0;
            int i = 0;
            for (long power = 1; i <= len; i++, power *= mid) {
                sum += power;
            }
            if (sum == n) return mid;
            if (sum > n) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    void test(String n, String expected) {
        assertEquals(expected, smallestGoodBase(n));
        assertEquals(expected, smallestGoodBase2(n));
    }

    @Test
    public void test() {
        test("3", "2");
        test("13", "3");
        test("4681", "8");
        test("3541", "59");
        test("14919921443713777", "496");
        test("727004545306745403", "727004545306745402");
        test("821424692950225218", "821424692950225217");
        test("1000000000000000000", "999999999999999999");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SmallestGoodBase");
    }
}
