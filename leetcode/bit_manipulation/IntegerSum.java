import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/sum-of-two-integers/
//
// Calculate the sum of two integers a and b, but you are not allowed to use
// the operator + and -.
public class IntegerSum {
    // recursive
    // beats 7.22%(0 ms)
    public int getSum(int a, int b) {
        return b == 0 ? a : getSum(a ^ b, (a & b) << 1);
    }

    // iterative
    // beats 7.22%(0 ms)
    public int getSum2(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;
            a ^= b;
            b = carry;
        }
        return a;
    }

    void test(int a, int b) {
        assertEquals(a + b, getSum(a, b));
        assertEquals(a + b, getSum2(a, b));
    }

    @Test
    public void test1() {
        test(-1, 1);
        test(1, 8);
        test(10, 18);
        test(113, 418);
        test(1993, 40018);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IntegerSum");
    }
}
