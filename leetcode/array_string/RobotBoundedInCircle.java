import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1041: https://leetcode.com/problems/robot-bounded-in-circle/
//
// On an infinite plane, a robot initially stands at (0, 0) and faces north.  The robot can receive
// one of three instructions:
// "G": go straight 1 unit;
// "L": turn 90 degrees to the left;
// "R": turn 90 degress to the right.
// The robot performs the instructions given in order, and repeats them forever. Return true if and
// only if there exists a circle in the plane such that the robot never leaves the circle.
public class RobotBoundedInCircle {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%),  33 MB(100%) for 110 tests
    public boolean isRobotBounded(String instructions) {
        int x = 0;
        int y = 0;
        int face = 0;
        int dir[][] = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        for (char instruction : instructions.toCharArray()) {
            if (instruction == 'R')
                face = (face + 1) % 4;
            else if (instruction == 'L')
                face = (face + 3) % 4;
            else {
                x += dir[face][0];
                y += dir[face][1];
            }
        }
        return (x == 0 && y == 0) || (face != 0);
    }

    void test(String instructions, boolean expected) {
        assertEquals(expected, isRobotBounded(instructions));
    }

    @Test
    public void test() {
        test("GGLLGG", true);
        test("GG", false);
        test("GL", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
