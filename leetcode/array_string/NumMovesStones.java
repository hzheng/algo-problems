import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1033: https://leetcode.com/problems/moving-stones-until-consecutive/
//
// Three stones are on a number line at positions a, b, and c. Each turn, let's say the stones are
// currently at positions x, y, z with x < y < z.  You pick up the stone at either position x or
// position z, and move that stone to an integer position k, with x < k < z and k != y.
// The game ends when you cannot make any more moves, ie. the stones are in consecutive positions.
// When the game ends, what is the minimum and maximum number of moves that you could have made?
public class NumMovesStones {
    // 1 ms(75.42%),  33 MB(100%) for 187 tests
    public int[] numMovesStones(int a, int b, int c) {
        int[] pos = new int[]{a, b, c};
        Arrays.sort(pos);
        a = pos[0];
        b = pos[1];
        c = pos[2];

        int max = (b - a - 1) + (c - b - 1);
        int min = ((b - a - 1) == 0 ? 0 : 1) + ((c - b - 1) == 0 ? 0 : 1);
        if (min == 2 && (b - a == 2 || c - b == 2)) {
            min = 1;
        }
        return new int[]{min, max};
    }

    // 1 ms(75.42%),  33 MB(100%) for 187 tests
    public int[] numMovesStones2(int a, int b, int c) {
        int[] pos = new int[]{a, b, c};
        Arrays.sort(pos);
        if (pos[2] - pos[0] == 2) {
            return new int[]{0, 0};
        }
        return new int[]{Math.min(pos[1] - pos[0], pos[2] - pos[1]) <= 2 ? 1 : 2,
                         pos[2] - pos[0] - 2};
    }

    void test(int a, int b, int c, int[] expected) {
        assertArrayEquals(expected, numMovesStones(a, b, c));
        assertArrayEquals(expected, numMovesStones2(a, b, c));
    }

    @Test
    public void test() {
        test(1, 2, 5, new int[]{1, 2});
        test(4, 3, 2, new int[]{0, 0});
        test(1, 4, 3, new int[]{1, 1});
        test(3, 2, 1, new int[]{0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
