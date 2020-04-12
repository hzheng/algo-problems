import org.junit.Test;

import static org.junit.Assert.*;

// LC1411: https://leetcode.com/problems/number-of-ways-to-paint-n-3-grid/
// You have a grid of size n x 3 and you want to paint each cell of the grid with exactly one of the three colours: Red,
// Yellow or Green while making sure that no two adjacent cells have the same colour (i.e no two cells that share
// vertical or horizontal sides have the same colour).
// You are given n the number of rows of the grid. Return the number of ways you can paint this grid. As the answer may
// grow large, the answer must be computed modulo 10^9 + 7.
public class Paint3Grid {
    private static final int MOD = 1000_000_000 + 7;

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(100.00%), 36.3 MB(100%) for 50 tests
    public int numOfWays(int n) {
        long aba = 6; // ABA-type
        long abc = 6; // ABC-type
        for (int i = 1; i < n; i++) {
            long nextAba = 3 * aba + 2 * abc;
            long nextAbc = 2 * aba + 2 * abc;
            aba = nextAba % MOD;
            abc = nextAbc % MOD;
        }
        return (int) ((aba + abc) % MOD);
    }

    private void test(int n, int expected) {
        assertEquals(expected, numOfWays(n));
    }

    @Test
    public void test() {
        test(1,12);
        test(2,54);
        test(3,246);
        test(7,106494);
        test(5000,30228214);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
