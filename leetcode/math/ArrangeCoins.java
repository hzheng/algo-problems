import org.junit.Test;
import static org.junit.Assert.*;

// LC441: https://leetcode.com/problems/arranging-coins/
//
// You have a total of n coins that you want to form in a staircase shape, where
// every k-th row must have exactly k coins.
// Given n, find the total number of full staircase rows that can be formed.
// n is a non-negative integer and fits within the range of a 32-bit signed integer.
public class ArrangeCoins {
    // beats N/A(40 ms for 1336 tests)
    public int arrangeCoins(int n) {
        long root = (int)Math.sqrt(2 * (double)n);
        for (long res = root, sum = res * (res + 1) / 2; ; sum -= res, res--) {
            if (sum <= n) return (int)res;
        }
    }

    // beats N/A(48 ms for 1336 tests)
    public int arrangeCoins2(int n) {
        return (int)((Math.sqrt(n * 8L + 1) - 1) / 2);
    }

    void test(int n, int expected) {
        assertEquals(expected, arrangeCoins(n));
        assertEquals(expected, arrangeCoins2(n));
    }

    @Test
    public void test() {
        test(1804289383, 60070);
        test(5, 2);
        test(8, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ArrangeCoins");
    }
}
