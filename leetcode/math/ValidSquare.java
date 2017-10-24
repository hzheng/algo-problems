import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC593: https://leetcode.com/problems/valid-square/
//
// Given the coordinates of four points in 2D space, return whether the four points
// could construct a square.
public class ValidSquare {
    // beats 57.67%(19 ms for 244 tests)
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        return valid(p1, p2, p3, p4) && valid(p2, p1, p3, p4)
               && valid(p3, p1, p2, p4);
    }

    private boolean valid(int[] p1, int[] p2, int[] p3, int[] p4) {
        int d1 = distance(p1, p2);
        int d2 = distance(p1, p3);
        int d3 = distance(p1, p4);
        if (d1 == d2) return d1 > 0 && d3 == d1 * 2;
        if (d1 == d3) return d2 > 0 && d2 == d1 * 2;
        return d2 == d3 && d1 > 0 && d1 == d2 * 2;
    }

    // Sort
    // beats 57.67%(19 ms for 244 tests)
    public boolean validSquare2(int[] p1, int[] p2, int[] p3, int[] p4) {
        int[] lens = {distance(p1, p2), distance(p2, p3), distance(p3, p4),
                      distance(p4, p1), distance(p1, p3), distance(p2, p4)};
        Arrays.sort(lens);
        return (lens[0] == lens[3]) && (lens[4] == lens[5])
               && (lens[4] > lens[0]);
    }

    // Sort
    // beats 4.09%(91 ms for 244 tests)
    public boolean validSquare3(int[] p1, int[] p2, int[] p3, int[] p4) {
        int[][] p = {p1, p2, p3, p4};
        Arrays.sort(p, (a, b) -> b[0] == a[0] ? a[1] - b[1] : a[0] - b[0]);
        return distance(p[0], p[1]) != 0
               && distance(p[0], p[1]) == distance(p[1], p[3])
               && distance(p[1], p[3]) == distance(p[3], p[2])
               && distance(p[3], p[2]) == distance(p[2], p[0])
               && distance(p[0], p[3]) == distance(p[1], p[2]);
    }

    // Set
    // beats 37.83%(21 ms for 244 tests)
    public boolean validSquare4(int[] p1, int[] p2, int[] p3, int[] p4) {
        Set<Integer> set = new HashSet<>();
        set.add(distance(p1, p2));
        set.add(distance(p2, p3));
        set.add(distance(p3, p4));
        set.add(distance(p4, p1));
        set.add(distance(p1, p3));
        set.add(distance(p2, p4));
        if (set.size() != 2) return false;

        Iterator<Integer> itr = set.iterator();
        return itr.next() > 0 && itr.next() > 0;
    }

    private int distance(int[] p1, int[] p2) {
        int a = p1[0] - p2[0];
        int b = p1[1] - p2[1];
        return a * a + b * b;
    }

    void test(int[] p1, int[] p2, int[] p3, int[] p4, boolean expected) {
        assertEquals(expected, validSquare(p1, p2, p3, p4));
        assertEquals(expected, validSquare2(p1, p2, p3, p4));
        assertEquals(expected, validSquare3(p1, p2, p3, p4));
        assertEquals(expected, validSquare4(p1, p2, p3, p4));
    }

    @Test
    public void test() {
        test(new int[] {0, 0}, new int[] {1, 1}, new int[] {0, 0},
             new int[] {0, 0}, false);
        test(new int[] {0, 0}, new int[] {1, 1}, new int[] {1, 0},
             new int[] {0, 1}, true);
        test(new int[] {0, 0}, new int[] {0, 0}, new int[] {0, 0},
             new int[] {0, 0}, false);
        test(new int[] {0, 0}, new int[] {1, 1}, new int[] {0, 0},
             new int[] {1, 1}, false);
        test(new int[] {0, 1}, new int[] {1, 2}, new int[] {0, 2},
             new int[] {0, 0}, false);
        test(new int[] {1, 1}, new int[] {0, 1}, new int[] {1, 2},
             new int[] {0, 0}, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
