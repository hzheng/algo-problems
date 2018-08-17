import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC657: https://leetcode.com/problems/judge-route-circle/
//
// Initially, there is a Robot at position (0, 0). Given a sequence of its
// moves, judge if this robot makes a circle, which means it moves back to the
// original place.
public class JudgeCircle {
    // beats 30.71%(13 ms for 62 tests)
    public boolean judgeCircle(String moves) {
        int xShift = 0;
        int yShift = 0;
        for (char dir : moves.toCharArray()) {
            switch (dir) {
            case 'U':
                yShift++;
                break;
            case 'D':
                yShift--;
                break;
            case 'L':
                xShift--;
                break;
            case 'R':
                xShift++;
                break;
            }
        }
        return (xShift == 0) && (yShift == 0);
    }

    void test(String moves, boolean expected) {
        assertEquals(expected, judgeCircle(moves));
    }

    @Test
    public void test() {
        test("UD", true);
        test("LL", false);
        test("LLDDRURU", true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
