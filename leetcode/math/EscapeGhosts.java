import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC789: https://leetcode.com/problems/escape-the-ghosts/
//
// You are playing a simplified Pacman game. You start at the point (0, 0), and 
// your destination is (target[0], target[1]). There are several ghosts on the 
// map, the i-th ghost starts at (ghosts[i][0], ghosts[i][1]). Each turn, you 
// and all ghosts simultaneously *may* move in one of 4 cardinal directions: 
// north, east, west, or south, going from the previous point to a new point 1 
// unit of distance away. You escape if and only if you can reach the target 
// before any ghost reaches you. If you reach any square (including the target)
// at the same time as a ghost, it doesn't count as an escape.
// Return True if and only if it is possible to escape.
public class EscapeGhosts {
    // beats 31.09%(7 ms for 74 tests)
    public boolean escapeGhosts(int[][] ghosts, int[] target) {
        int steps = distance(new int[2], target);
        for (int[] ghost : ghosts) {
            if (distance(ghost, target) <= steps) return false;
        }
        return true;
    }

    private int distance(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
    }

    void test(int[][] ghosts, int[] target, boolean expected) {
        assertEquals(expected, escapeGhosts(ghosts, target));
    }

    @Test
    public void test() {
        test(new int[][] { {1, 0}, {0, 3}}, new int[] {0, 1}, true);
        test(new int[][] { {1, 0}}, new int[] {2, 0}, false);
        test(new int[][] { {2, 0}}, new int[] {1, 0}, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
