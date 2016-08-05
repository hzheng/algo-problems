import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/super-pow/
//
// Your task is to calculate a ^ b mod 1337 where a is a positive integer and
// b is an extremely large positive integer given in the form of an array.
public class SuperPow {
    // beats 95.65%(2 ms)
    public int superPow(int a, int[] b) {
        if (a == 1 || b.length == 1 && b[0] == 0) return 1;

        final int MOD = 1337;
        a %= MOD;
        int[] modules = new int[MOD];
        int cycle = 0;
        for (int mod = a; cycle == 0 || mod != a; mod %= MOD) {
            modules[cycle++] = mod;
            mod *= a;
        }
        int cycleIndex = 0;
        for (int num : b) {
            cycleIndex = (cycleIndex * 10 + num) % cycle;
        }
        return modules[cycleIndex - 1];
    }

    void test(int a, int[] b, int expected) {
        assertEquals(expected, superPow(a, b));
    }

    @Test
    public void test1() {
        test(7, new int[]{0}, 1);
        test(7, new int[]{4}, 1064);
        test(2, new int[]{3}, 8);
        test(2, new int[]{1, 2}, 85);
        test(2, new int[]{1, 0}, 1024);
        test(21892, new int[]{3, 0, 1, 9}, 199);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SuperPow");
    }
}
