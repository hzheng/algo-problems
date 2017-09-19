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
    // Recursion
    // beats 23.43%(95 ms for 3377 tests)
    public int lastRemaining(int n) {
        return lastRemaining(n, true);
    }

    private int lastRemaining(int n, boolean left2Right) {
        if (n == 1) return 1;

        int res = lastRemaining(n >> 1, !left2Right) << 1;
        return (left2Right || (n & 1) == 1) ? res : res - 1;
    }

    // Solution of Choice
    // Recursion
    // beats 48.14%(85 ms for 3377 tests)
    public int lastRemaining2(int n) {
        return n == 1 ? 1 : ((n >> 1) + 1 - lastRemaining2(n >> 1)) << 1;
    }

    // Solution of Choice
    // Bit Manipulation
    // beats 84.86%(77 ms for 3377 tests)
    public int lastRemaining3(int n) {
        int head = 1;
        for (int left = n, step = 1, dir = 1; left > 1; left >>= 1, step <<= 1, dir ^= 1) {
            // if (dir == 1 || (left & 1) == 1) {
            if ((dir | (left & 1)) != 0) {
                head += step;
            }
        }
        return head;
    }

    // Solution of Choice
    // Bit Manipulation
    // https://en.wikipedia.org/wiki/Josephus_problem
    // beats 80.00%(78 ms for 3377 tests)
    public int lastRemaining4(int n) {
        return ((Integer.highestOneBit(n) - 1) & (n | 0x55555555)) + 1;
    }

    void test(int n, int expected) {
        assertEquals(expected, lastRemaining(n));
        assertEquals(expected, lastRemaining2(n));
        assertEquals(expected, lastRemaining3(n));
        assertEquals(expected, lastRemaining4(n));
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
