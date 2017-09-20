import org.junit.Test;
import static org.junit.Assert.*;

// LC371: https://leetcode.com/problems/sum-of-two-integers/
//
// Calculate the sum of two integers a and b, but you are not allowed to use
// the operator + and -.
public class IntegerSum {
    // Recursion
    // beats 7.55%(0 ms for 13 tests)
    public int getSum(int a, int b) {
        return b == 0 ? a : getSum(a ^ b, (a & b) << 1);
    }

    // Solution of Choice
    // beats 7.55%(0 ms for 13 tests)
    public int getSum2(int a, int b) {
        int sum = a;
        for (int second = b, carry; second != 0; sum ^= second, second = carry) {
            carry = (sum & second) << 1;
        }
        return sum;
    }

    //////////////////////Subtract/////////////////////////
    public int getSubtract(int a, int b) {
        return b == 0 ? a : getSubtract(a ^ b, (~a & b) << 1);
    }

    public int getSubtract2(int a, int b) {
        int sum = a;
        for (int second = b, borrow; second != 0; sum ^= second, second = borrow) {
            borrow = (~sum & second) << 1;
        }
        return sum;
    }

    public int getSubtract3(int a, int b) {
        return getSum(a, getSum(~b, 1));
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

    void test2(int a, int b) {
        assertEquals(a - b, getSubtract(a, b));
        assertEquals(a - b, getSubtract2(a, b));
        assertEquals(a - b, getSubtract3(a, b));
    }

    @Test
    public void test2() {
        test2(-1, 1);
        test2(1, 8);
        test2(10, 18);
        test2(113, 418);
        test2(1993, 40018);
        test2(-21993, 4990018);
        test2(-21993, -4990018);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IntegerSum");
    }
}
