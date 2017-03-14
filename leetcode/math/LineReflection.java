import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC356: https://leetcode.com/problems/line-reflection
//
// Given n points on a 2D plane, find if there is such a line parallel to y-axis
// that reflect the given points.
// Follow up:
// Could you do better than O(n2)?
public class LineReflection {
    // Hash Table + SortedSet
    // beats 57.55%(20 ms for 37 tests)
    public boolean isReflected(int[][] points) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[] point : points) {
            Set<Integer> sameY = map.get(point[1]);
            if (sameY == null) {
                map.put(point[1], sameY = new TreeSet<>());
            }
            sameY.add(point[0]);
        }
        Integer last = null;
        for (Set<Integer> set : map.values()) {
            Integer sum = sum(new ArrayList<>(set));
            if (sum == null) return false;
            if (last == null) {
                last = sum;
            } else if (!last.equals(sum)) return false;
        }
        return true;
    }

    private Integer sum(List<Integer> points) {
        // Collections.sort(points); // need sort if list is from unsorted Set
        int n = points.size();
        int sum = points.get(0) + points.get(n - 1);
        for (int i = 1, j = n - 2; i <= j; i++, j--) {
            if (points.get(i) + points.get(j) != sum) return null;
        }
        return sum;
    }

    // Hash Table
    // time complexity: O(N)
    // beats 86.21%(13 ms for 37 tests)
    public boolean isReflected2(int[][] points) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        Set<Long> set = new HashSet<>();
        for (int[] point : points) {
            minX = Math.min(minX, point[0]);
            maxX = Math.max(maxX, point[0]);
            set.add((((long)point[0]) << 32) | (point[1] & 0xFFFFFFFFL));
        }
        long sum = minX + maxX;
        for (long point : set) {
            if (!set.contains(((sum - (point >> 32)) << 32) | (point & 0xFFFFFFFFL))) return false;
        }
        // or:
        // for (int[] point : points) {
        //     if (!set.contains(((sum - point[0]) << 32) | (point[1] & 0xFFFFFFFFL))) return false;
        // }
        return true;
    }

    // Hash Table
    // time complexity: O(N)
    // beats 46.61%(21 ms for 37 tests)
    public boolean isReflected2_2(int[][] points) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        Set<String> set = new HashSet<>();
        for (int[] point : points) {
            minX = Math.min(minX, point[0]);
            maxX = Math.max(maxX, point[0]);
            set.add(point[0] + "," + point[1]);
        }
        long sum = minX + maxX;
        for (int[] point : points) {
            if (!set.contains((sum - point[0]) + "," + point[1])) return false;
        }
        return true;
    }

    void test(int[][] points, boolean expected) {
        assertEquals(expected, isReflected(points));
        assertEquals(expected, isReflected2(points));
        assertEquals(expected, isReflected2_2(points));
    }

    @Test
    public void test() {
        test(new int[][] {{-2, 0}, {0, 0}, {1, 0}, {2, 0}}, false); // try to contribute Testcase for Leetcode
        test(new int[][] {{-16, 1}, {16, 1}, {16, 1}}, true);
        test(new int[][] {{0, 0}, {1, 0}, {3, 0}}, false);
        test(new int[][] {{1, 1}, {-1, 1}}, true);
        test(new int[][] {{1, 1}, {-1, -1}}, false);
        test(new int[][] {{1, 1}, {0, 1}, {-1, 1}, {0, 0}}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LineReflection");
    }
}
