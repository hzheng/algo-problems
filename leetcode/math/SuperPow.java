import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/super-pow/
//
// Your task is to calculate a ^ b mod 1337 where a is a positive integer and
// b is an extremely large positive integer given in the form of an array.
public class SuperPow {
    static final int MOD = 1337;

    // beats 94.57%(3 ms)
    public int superPow(int a, int[] b) {
        if (a == 1 || b.length == 1 && b[0] == 0) return 1;

        a %= MOD;
        int[] modules = new int[MOD];
        int cycle = 0;
        for (int mod = a; cycle == 0 || mod != a; mod %= MOD) {
            modules[cycle++] = mod;
            mod *= a;
        }
        int modIndex = 0;
        for (int num : b) {
            modIndex = (modIndex * 10 + num) % cycle;
        }
        return modules[(modIndex > 0 ? modIndex : cycle) - 1];
    }

    // beats 84.70%(6 ms)
    public int superPow2(int a, int[] b) {
        int pow = 1;
        a %= MOD;
        for (int i = b.length - 1; i >= 0; i--) {
            pow = pow * fastPow(a, b[i], MOD) % MOD;
            a = fastPow(a, 10, MOD);
        }
        return pow;
    }

    private int fastPow(int a, int b, int mod) {
        int pow = 1;
        for (int x = a % mod, y = b; y > 0; y >>= 1) {
            if ((y & 1) != 0) {
                pow = pow * x % mod;
            }
            x = x * x % mod;
        }
        return pow;
    }

    // Fermat Litter Theorem and Chinese Remainder Theorem
    // https://discuss.leetcode.com/topic/50591/fermat-and-chinese-remainder
    // beats 94.57%(3 ms)
    public int superPow3(int a, int[] b) {
        if (a == 1 || b.length == 1 && b[0] == 0) return 1;

        return (764 * superPow(a, b, 7) + 574 * superPow(a, b, 191)) % MOD;
    }

    private int superPow(int a, int[] b, int prime) {
        if ((a %= prime) == 0) return 0;

        int mod = prime - 1;
        int exp = 0;
        for (int num : b) {
            exp = (exp * 10 + num) % mod;
        }
        return fastPow(a, exp, prime);
    }

    // recursive
    // beats 41.30%(11 ms)
    public int superPow4(int a, int[] b) {
        int pow = 1;
        for (int num : b) {
            pow = modPow(pow, 10) * modPow(a, num) % MOD;
        }
        return pow;
    }

    private int modPow(int x, int n) {
        if (n == 0) return 1;

        if (n == 1) return x % MOD;

        return modPow(x % MOD, n / 2) * modPow(x % MOD, n - n / 2) % MOD;
    }

    // Euler Theorem: https://en.wikipedia.org/wiki/Euler%27s_theorem
    // beats 95.65%(2 ms)
    public int superPow5(int a, int[] b) {
        if (a == 1 || b.length == 1 && b[0] == 0) return 1;

        final int mod = MOD;
        final int factor1 = 7; // one of 1337's prime factor
        final int factor2 = 191; // mod / factor1
        // phi(Euler totient value): 1337 - 191 - 7 + 1
        // or phi(1337) = phi(191) * phi(7) = (191 - 1) * (7 - 1)
        final int phi = 1140;

        int exp = 0;
        for (int num : b) {
            exp = (exp * 10 + num) % phi;
        }
        if (exp == 0) {
            if (a % factor1 != 0 && a % factor2 != 0) return 1;

            // exceptional case: a is NOT coprime to 1337 and b is divisible by phi
            exp = phi;
        }
        return fastPow(a, exp, mod);
    }

    void test(int a, int[] b, int expected) {
        assertEquals(expected, superPow(a, b));
        assertEquals(expected, superPow2(a, b));
        assertEquals(expected, superPow3(a, b));
        assertEquals(expected, superPow4(a, b));
        assertEquals(expected, superPow5(a, b));
    }

    @Test
    public void test1() {
        test(7, new int[] {0}, 1);
        test(7, new int[] {4}, 1064);
        test(2, new int[] {3}, 8);
        test(2, new int[] {1, 2}, 85);
        test(2, new int[] {1, 0}, 1024);
        test(23, new int[] {3, 1, 9}, 450);
        test(21892, new int[] {3, 0, 1, 9}, 199);
        test(9821892, new int[] {9, 8, 7, 6, 8, 6, 0, 2, 4, 3, 0, 1, 9}, 1028);
        // test cases: a is NOT coprime to 1337
        test(14, new int[] {2, 2, 8, 0}, 574);
        test(14, new int[] {1, 1, 4, 0}, 574);
        test(14, new int[] {1, 1, 4, 1}, 14);
        test(14, new int[] {1, 0, 0, 0, 0}, 889);
        test(191, new int[] {2, 2, 8, 0}, 764);
        test(191, new int[] {2, 2, 8, 1}, 191);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SuperPow");
    }
}
