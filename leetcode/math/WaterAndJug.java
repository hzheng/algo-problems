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

    // Math
    // From above queue generation, we know all generated liters are of
    // the form a * x + b * y - (k * m) % y
    // where m = x % y, a >= 0, b <= 1, k >= 0
    // Big picture: all actions are either emptying a jug or fully filling one,
    // hence result must be linear combinations of x and y, and its sufficient
    // and necessary condition is its GCD is divisible by z.
    // beats 22.45%(0 ms)
    public boolean canMeasureWater3(int x, int y, int z) {
        return x + y == z || (x + y > z && z % gcd(x, y) == 0);
    }

    private int gcd(int a, int b) {
        if (a < b) return gcd(b, a);
        if (b == 0) return a;

        if ((a & 1) == 0) {
            if ((b & 1) == 0) return gcd(a >> 1, b >> 1) << 1;

            return gcd(a >> 1, b);
        }

        return ((b & 1) == 0) ? gcd(a, b >> 1) : gcd(a - b, b);
    }

    void test(int x, int y, int z, boolean expected) {
        if (x + y < 1000) {
            assertEquals(expected, canMeasureWater(x, y, z));
        }
        assertEquals(expected, canMeasureWater2(x, y, z));
        assertEquals(expected, canMeasureWater3(x, y, z));
    }

    @Test
    public void test1() {
        test(1, 0, 1, true);
        test(2, 0, 1, false);
        test(1, 1, 12, false);
        test(6, 4, 1, false);
        test(6, 5, 1, true);
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
