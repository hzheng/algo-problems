import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC447: https://leetcode.com/problems/number-of-boomerangs/
//
// Given n points in the plane that are all pairwise distinct, a "boomerang" is
// a tuple of points (i, j, k) such that the distance between i and j equals the
// distance between i and k (the order of the tuple matters).
// Find the number of boomerangs.
public class CountBoomerangs {
    // Hash Table
    // beats N/A(265 ms for 30 tests)
    public int numberOfBoomerangs(int[][] points) {
        int n = points.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            Map<Integer, Integer> map = new HashMap<>();
            int[] p1 = points[i];
            for (int j = 0; j < n; j++) {
                if (i == j) continue;

                int[] p2 = points[j];
                int d = (p1[0] - p2[0]) * (p1[0] - p2[0])
                        + (p1[1] - p2[1]) * (p1[1] - p2[1]);
                map.put(d, map.getOrDefault(d, 0) + 1);
            }
            for (int v : map.values()) {
                count += v * (v - 1);
            }
        }
        return count;
    }

    // Hash Table
    // beats N/A(175 ms for 30 tests)
    public int numberOfBoomerangs2(int[][] points) {
        int n = points.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            int[] p1 = points[i];
            Map<Integer, Integer> map = new HashMap<>();
            for (int j = 0; j < n; j++) {
                int[] p2 = points[j];
                int d = (p1[0] - p2[0]) * (p1[0] - p2[0])
                        + (p1[1] - p2[1]) * (p1[1] - p2[1]);
                int v = map.getOrDefault(d, 0);
                count += v;
                map.put(d, v + 1);
            }
        }
        return count * 2;
    }

    void test(int[][] points, int expected) {
        assertEquals(expected, numberOfBoomerangs(points));
        assertEquals(expected, numberOfBoomerangs2(points));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 0}, {0, 0}, {2, 0}}, 2);
        test(new int[][] {{0, 0}, {0, 1}, {1, 0}, {2, 0}, {3, 0}}, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountBoomerangs");
    }
}
