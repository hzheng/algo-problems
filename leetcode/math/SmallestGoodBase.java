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
            long power = 1;
            for (int i = 0; i <= len; i++, power *= mid) {
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

    // Binary Search
    // beats 66.24%(11 ms for 103 tests)
    public String smallestGoodBase3(String n) {
        long num = Long.parseLong(n);
        for (int power = (int)(Math.log(num) / Math.log(2)) + 1; power > 1; power--) {
            long base = check3(num, power);
            if (base > 0) return String.valueOf(base);
        }
        return String.valueOf(num - 1);
    }

    private long pow(long base, int exp) {
        if (exp == 0) return 1;
        if (exp == 1) return base;
        if ((exp & 1) == 0) return pow (base * base, exp >> 1);
        return base * pow(base * base, exp >> 1);
    }

    private long check3(long n, int exp) {
        for (long low = 2, high = (long)Math.pow(n, 1.0 / exp) + 1; low <= high; ) {
            long mid = (low + high) >>> 1;
            long left = n * (mid - 1); // formula: 1 + x + x ^ 2 + ... + x ^ n
            // long right = (long)Math.pow(mid, exp + 1) - 1; // WRONG!
            long right = pow(mid, exp + 1) - 1;
            if (left == right) return mid;
            if (left > right || left / n != (mid - 1)) { // check overflow!
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return 0;
    }

    // Math
    // beats 78.21%(10 ms for 103 tests)
    public String smallestGoodBase4(String n) {
        long num = Long.parseLong(n);
        for (int k = (int)(Math.log(num) / Math.log(2)); k > 1; k--) {
            // base ^ k < num < (base + 1) ^ k
            long base = (long)Math.pow(num, 1.0 / k); // the only candidate
            if ((num - 1) % base != 0) continue; // Vieta's formulas

            long left = pow(base, k + 1) - 1;
            long right = num * (base - 1); // still may overflow
            if (left == right) return String.valueOf(base);
        }
        return String.valueOf(num - 1);
    }

    void test(String n, String expected) {
        assertEquals(expected, smallestGoodBase(n));
        assertEquals(expected, smallestGoodBase2(n));
        assertEquals(expected, smallestGoodBase3(n));
        assertEquals(expected, smallestGoodBase4(n));
    }

    @Test
    public void test() {
        test("3", "2");
        test("13", "3");
        test("4681", "8");
        test("3541", "59");
        test("14919921443713777", "496");
        test("16035713712910627", "502");
        test("470988884881403701", "686286299");
        test("727004545306745403", "727004545306745402");
        test("821424692950225218", "821424692950225217");
        test("1000000000000000000", "999999999999999999");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SmallestGoodBase");
    }
}
