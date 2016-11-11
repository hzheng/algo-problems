import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC292: https://leetcode.com/problems/nim-game/
//
// You are playing the following Nim Game with your friend: There is a heap of
// stones on the table, each time one of you take turns to remove 1 to 3 stones.
// The one who removes the last stone will be the winner. You will take the
// first turn to remove the stones. Write a function to determine whether you
// can win the game given the number of stones in the heap.
public class NimGame {
    // beats 7.72%(0 ms)
    public boolean canWinNim(int n) {
        return (n & 3) != 0;
        // return (n % 4) != 0;
    }

    void test(int n, boolean expected) {
        assertEquals(expected, canWinNim(n));
    }

    @Test
    public void test1() {
        test(3, true);
        test(4, false);
        test(5, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NimGame");
    }
}
