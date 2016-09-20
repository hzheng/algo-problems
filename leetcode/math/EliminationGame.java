import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC390: https://leetcode.com/problems/elimination-game/
//
// There is a list of sorted integers from 1 to n. Starting from left to right,
// remove the first number and every other number afterward until you reach the
// end of the list. Repeat the previous step again, but this time from right to
// left, remove the right most number and every other number from the remaining
// numbers. We keep repeating the steps again, alternating left to right and
// right to left, until a single number remains.
// Find the last number that remains starting with a list of length n.
public class EliminationGame {
    // beats 26.73%(93 ms)
    public int lastRemaining(int n) {
        return lastRemaining(n, true);
    }

    private int lastRemaining(int n, boolean left2Right) {
        if (n == 1) return 1;

        int res = lastRemaining(n >> 1, !left2Right) << 1;
        return (left2Right || (n & 1) == 1) ? res : res - 1;
    }

    // beats 40.10%(87 ms)
    public int lastRemaining2(int n) {
        return n == 1 ? 1 : ((n >> 1) + 1 - lastRemaining2(n >> 1)) << 1;
    }

    // beats 89.60%(75 ms)
    public int lastRemaining2_2(int n) {
        if (n == 1) return 1;

        n >>= 1;
        return (n + 1 - lastRemaining(n)) << 1;
    }

    // beats 59.41%(82 ms)
    public int lastRemaining3(int n) {
        int head = 1;
        boolean left2Right = true;
        for (int count = n, step = 1; count > 1; count >>= 1, step <<= 1) {
            if (left2Right || (count & 1) == 1) {
                head += step;
            }
            left2Right = !left2Right;
        }
        return head;
    }

    void test(int n, int expected) {
        assertEquals(expected, lastRemaining(n));
        assertEquals(expected, lastRemaining2(n));
        assertEquals(expected, lastRemaining2_2(n));
        assertEquals(expected, lastRemaining3(n));
    }

    @Test
    public void test1() {
        test(9, 6);
        test(10, 8);
        test(11, 8);
        test(12, 6);
        test(19, 8);
        test(20, 6);
        test(200, 94);
        test(2000, 982);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EliminationGame");
    }
}
