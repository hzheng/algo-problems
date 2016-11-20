import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC464: https://leetcode.com/problems/can-i-win/
//
// In the "100 game," two players take turns adding, to a running total, any
// integer from 1..10. The player who first causes the running total to reach or
// exceed 100 wins.
// What if we change the game so that players cannot re-use integers?
// Given an integer maxChoosableInteger and another integer desiredTotal,
// determine if the first player to move can force a win, assuming both players
// play optimally.
// Assume maxChoosableInteger will not be larger than 20 and desiredTotal will
// not be larger than 300.
public class CanIWin {
    // Recursion + Dynamic Programming(Top-Down)
    // beats N/A(76 ms for 52 tests)
    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        int total = maxChoosableInteger * (maxChoosableInteger + 1) / 2;
        if (desiredTotal > total) return false;
        if (desiredTotal == total) return (maxChoosableInteger & 1) == 1;

        return canIWin(maxChoosableInteger, desiredTotal,
                       (1 << maxChoosableInteger) - 1, new HashMap<>());
    }

    private boolean canIWin(int max, int total,
                            int chosen, Map<Integer, Boolean> memo) {
        int key = chosen; // or? : (chosen << 10) | total;
        if (memo.containsKey(key)) return memo.get(key);

        int i = max - 1;
        for (; i >= 0 && ((chosen >> i) & 1) == 0; i--) {}
        if (i + 1 >= total) return true;

        boolean res = false;
        for (; i >= 0; i--) {
            if (((chosen >> i) & 1) != 0
                && !canIWin(max, total - i - 1, chosen ^ (1 << i), memo)) {
                res = true;
                break;
            }
        }
        memo.put(key, res);
        return res;
    }

    // TODO: Dynamic Programming(Bottom-Up)
    // TODO: more efficient(e.g. take advantage of i + 1 - total > 0)

    void test(int maxChoosableInteger, int desiredTotal, boolean expected) {
        // assertEquals(expected, canIWin0(maxChoosableInteger, desiredTotal));
        assertEquals(expected, canIWin(maxChoosableInteger, desiredTotal));
    }

    @Test
    public void test() {
        test(20, 209, false);
        test(2, 3, false);
        test(3, 6, true);
        test(20, 210, false);
        test(10, 40, false);
        test(4, 6, true);
        test(18, 79, true);
        test(10, 11, false);
        test(12, 11, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CanIWin");
    }
}
