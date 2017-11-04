import org.junit.Test;
import static org.junit.Assert.*;

// LC625: https://leetcode.com/problems/minimum-factorization/
//
// Given a positive integer a, find the smallest positive integer b whose
// multiplication of each digit equals to a. Return 0 if there is no answer or
// the answer is not fit in 32-bit signed integer, then return 0.
public class MinimumFactorization {
    // time complexity: O(log(a))
    // beats 49.40%(11 ms for 146 tests)
    public int smallestFactorization(int a) {
        if (a < 10) return a;

        int n = a;
        String res = "";
        for (int i = 9; i > 1 && n != 1; i--) {
            if (n % i == 0) {
                n /= i;
                res = i + res;
                i++;
            }
        }
        if (n > 1) return 0;

        try {
            return Integer.valueOf(res);
        } catch (Exception e) {
            return 0;
        }
    }

    // time complexity: O(log(a))
    // beats 82.53%(10 ms for 146 tests)
    public int smallestFactorization2(int a) {
        if (a < 10) return a;

        long res = 0;
        long power = 1;
        for (int i = 9; i > 1; i--) {
            for (; a % i == 0; power *= 10) {
                a /= i;
                res += power * i;
            }
        }
        return a == 1 && res <= Integer.MAX_VALUE ? (int)res : 0;
    }

    // time complexity: O(log(a))
    // beats 82.53%(10 ms for 146 tests)
    public int smallestFactorization3(int a) {
        if (a < 10) return a;

        long res = 0;
        for (long i = 9, power = 1; i > 1; i--) {
            for (; a % i == 0; a /= i, power *= 10) {
                res += i * power;
                if (res > Integer.MAX_VALUE) return 0;
            }
        }
        return a == 1 ? (int)res : 0;
    }

    void test(int a, int expected) {
        assertEquals(expected, smallestFactorization(a));
        assertEquals(expected, smallestFactorization2(a));
        assertEquals(expected, smallestFactorization3(a));
    }

    @Test
    public void test() {
        test(1, 1);
        test(8, 8);
        test(48, 68);
        test(15, 35);
        test(22, 0);
        test(18000000, 0);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
