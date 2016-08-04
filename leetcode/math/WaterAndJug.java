import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/water-and-jug-problem/
//
// You are given two jugs with capacities x and y litres. There is an infinite
// amount of water supply available. You need to determine whether it is
// possible to measure exactly z litres using these two jugs.
// If z liters of water is measurable, you must have z liters of water contained
// within one or both buckets by the end.
//
// Operations allowed:
// Fill any of the jugs completely with water.
// Empty any of the jugs.
// Pour water from one jug into another till the other jug is completely full
// or the first jug itself is empty.
public class WaterAndJug {
    // DFS
    // stack overflow when x or y is too big
    public boolean canMeasureWater(int x, int y, int z) {
        return canMeasureWater(x, y, z, 0, 0, new HashSet<>());
    }

    private boolean canMeasureWater(int x, int y, int z, int a, int b,
                                    Set<Long> visited) {
        if (a == z || b == z || a + b == z) return true;

        long key = ((long)a << 32) | (long)b;
        if (visited.contains(key)) return false;

        visited.add(key);

        if (a == x && canMeasureWater(x, y, z, 0, b, visited)) return true;

        if (b == y && canMeasureWater(x, y, z, a, 0, visited)) return true;

        if (a == 0 && canMeasureWater(x, y, z, x, b, visited)) return true;

        if (b == 0 && canMeasureWater(x, y, z, a, y, visited)) return true;

        if (a > 0 && b < y) {
            int min = Math.min(a, y - b);
            if (canMeasureWater(x, y, z, a - min, b + min, visited)) return true;
        }

        if (b > 0 && a < x) {
            int min = Math.min(b, x - a);
            if (canMeasureWater(x, y, z, a + min, b - min, visited)) return true;
        }

        return false;
    }

    // BFS
    // beats 1.38%(109 ms)
    public boolean canMeasureWater2(int x, int y, int z) {
        if (x + y < z) return false;

        if (z == 0 || x == z || y == z || x + y == z) return true;

        if (x < y) return canMeasureWater2(y, x, z);

        if (y == 0) return false;

        if (z > x) return canMeasureWater2(x, y, z - x);

        if (z > y) return canMeasureWater2(x, y, z % y);

        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(y);
        visited.add(y);
        while (!queue.isEmpty()) {
            int head = queue.poll();
            int next = y - (x - head) % y; // fill Jug A first
            if (next == z) return true;

            if (!visited.contains(next)) {
                visited.add(next);
                queue.offer(next);
            }

            next = head > y ? head - y : x + head - y; // fill Jug B first
            if (next == z) return true;

            if (!visited.contains(next)) {
                visited.add(next);
                queue.offer(next);
            }
        }
        return false;
    }

    void test(int x, int y, int z, boolean expected) {
        if (x + y < 1000) {
            assertEquals(expected, canMeasureWater(x, y, z));
        }
        assertEquals(expected, canMeasureWater2(x, y, z));
    }

    @Test
    public void test1() {
        test(3, 5, 4, true);
        test(2, 6, 5, false);
        test(3, 3, 5, false);
        test(3, 3, 3, true);
        test(3, 3, 6, true);
        test(22003, 31237, 1, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WaterAndJug");
    }
}
