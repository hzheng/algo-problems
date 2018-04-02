import org.junit.Test;
import static org.junit.Assert.*;

// LC810: https://leetcode.com/problems/chalkboard-xor-game/
//
// We are given non-negative integers nums[i] which are written on a chalkboard.
// Alice and Bob take turns erasing exactly one number from the chalkboard, with
// Alice starting first. If erasing a number causes the bitwise XOR of all the
// elements of the chalkboard to become 0, then that player loses. (Also, we'll
// say the bitwise XOR of one element is that element itself, and the bitwise
// XOR of no elements is 0.)
// Also, if any player starts their turn with the bitwise XOR of all the
// elements of the chalkboard equal to 0, then that player wins.
// Return True iff Alice wins the game, assuming both players play optimally.
public class XorGame {
    // beats %(13 ms for 168 tests)
    public boolean xorGame(int[] nums) {
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        return (xor == 0) || (nums.length % 2 == 0);
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, xorGame(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 1, 2}, false);
        test(new int[] {1, 2, 3}, true);
        test(new int[] {1, 2}, true);
        test(new int[] {1, 2, 3, 7}, true);
        test(new int[] {1, 2, 3, 7, 10}, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
