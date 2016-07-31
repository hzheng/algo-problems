import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/self-crossing/
//
// You are given an array x of n positive numbers. You start at point (0,0) and
// moves x[0] metres to the north, then x[1] metres to the west, x[2] metres to
// the south, x[3] metres to the east and so on. In other words, after each move
// your direction changes counter-clockwise. Write a one-pass algorithm with
// O(1) extra space to determine, if your path crosses itself, or not.
public class SelfCrossing {
    // beats 60.42%(0 ms)
    public boolean isSelfCrossing(int[] x) {
        int n = x.length;
        if (n < 4) return false;

        for (int i = 2; i < n; i++) {
            if (x[i] > x[i - 2]) continue; // keep expanding

            if (++i >= n) return false;

            if (i >= 4 && (x[i - 3] - x[i - 1] <= (i >= 5 ? x[i - 5] : 0))) {
                if (x[i - 2] - x[i] <= x[i - 4]) return true;
            } else if (x[i - 2] <= x[i]) return true;
            // come to shrinking process
            while (++i < n) {
                if (x[i - 2] <= x[i]) return true;
            }
        }
        return false;
    }

    // https://discuss.leetcode.com/topic/38014/java-oms-with-explanation
    public boolean isSelfCrossing2(int[] x) {
        int n = x.length;
        if (n < 4) return false;

        for (int i = 3; i < n; i++) {
            // Fourth line crosses first line and onward
            //     i-2
            // i-1┌─┐
            //    └─┼─>i
            //     i-3
            if (x[i] >= x[i - 2] && x[i - 1] <= x[i - 3]) return true;

            // Fifth line meets first line and onward
            //      i-2
            // i-1 ┌────┐
            //     └─══>┘i-3
            //     i  i-4      (i overlapped i-4)
            if (i >= 4 && x[i - 1] == x[i - 3] && x[i] + x[i - 4] >= x[i - 2]) {
                return true;
            }

            // Sixth line crosses first line and onward
            //   i-4
            //    ┌──┐
            //    │i<┼─┐
            // i-3│ i-5│i-1
            //    └────┘
            //     i-2
            if (i >= 5 && x[i - 1] <= x[i - 3] && x[i - 4] <= x[i - 2]
                && x[i - 1] >= x[i - 3] - x[i - 5]
                && x[i] >= x[i - 2] - x[i - 4]) {
                return true;
            }
        }
        return false;
    }

    void test(boolean expected, int ... nums) {
        assertEquals(expected, isSelfCrossing(nums));
        assertEquals(expected, isSelfCrossing2(nums));
    }

    @Test
    public void test1() {
        test(true, 2, 1, 1, 2);
        test(true, 1, 1, 1, 1);
        test(false, 1, 2, 3, 4);
        test(false, 3, 3, 4, 2, 2);
        test(false, 3, 3, 4, 2, 3);
        test(true, 3, 3, 4, 2, 4);
        test(false, 1, 2, 3, 4, 2);
        test(true, 1, 2, 3, 4, 2, 2);
        test(true, 1, 2, 3, 4, 2, 3);
        test(false, 1, 2, 3, 4, 2, 1);
        test(true, 1, 1, 2, 1, 1);
        test(false, 1, 1, 3, 2, 1, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SelfCrossing");
    }
}
