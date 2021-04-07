import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.Utils;

// LC1637: https://leetcode.com/problems/widest-vertical-area-between-two-points-containing-no-points/
//
// Given n points on a 2D plane where points[i] = [xi, yi], Return the widest vertical area between
// two points such that no points are inside the area.
// A vertical area is an area of fixed-width extending infinitely along the y-axis (i.e., infinite
// height). The widest vertical area is the one with the maximum width.
// Note that points on the edge of a vertical area are not considered included in the area.
//
// Constraints:
// n == points.length
// 2 <= n <= 10^5
// points[i].length == 2
// 0 <= xi, yi <= 10^9
public class MaxWidthOfVerticalArea {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 41 ms(25.65%), 67.3 MB(51.47%) for 54 tests
    public int maxWidthOfVerticalArea(int[][] points) {
        Arrays.sort(points, Comparator.comparingInt(a -> a[0]));
        int res = 0;
        for (int i = 1, n = points.length; i < n; i++) {
            res = Math.max(res, points[i][0] - points[i - 1][0]);
        }
        return res;
    }

    // Heap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 50 ms(11.09%), 79.4 MB(6.41%) for 54 tests
    public int maxWidthOfVerticalArea2(int[][] points) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int[] point : points) {
            pq.offer(point[0]);
        }
        int res = 0;
        for (int prev = pq.poll(), cur; !pq.isEmpty(); prev = cur) {
            cur = pq.poll();
            res = Math.max(res, cur - prev);
        }
        return res;
    }

    // SortedSet
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 47 ms(15.42%), 74.5 MB(9.19%) for 54 tests
    public int maxWidthOfVerticalArea3(int[][] points) {
        Set<Integer> set = new TreeSet<>();
        for (int[] point : points) {
            set.add(point[0]);
        }
        int res = 0;
        var itr = set.iterator();
        for (int prev = itr.next(), cur; itr.hasNext(); prev = cur) {
            cur = itr.next();
            res = Math.max(res, cur - prev);
        }
        return res;
    }

    private void test(int[][] points, int expected) {
        assertEquals(expected, maxWidthOfVerticalArea(Utils.clone(points)));
        assertEquals(expected, maxWidthOfVerticalArea2(points));
        assertEquals(expected, maxWidthOfVerticalArea3(points));
    }

    @Test public void test() {
        test(new int[][] {{8, 7}, {9, 9}, {7, 4}, {9, 7}}, 1);
        test(new int[][] {{3, 1}, {9, 0}, {1, 0}, {1, 4}, {5, 3}, {8, 8}}, 3);
        test(new int[][] {{1, 1}, {1, 2}, {1, 3}}, 0);
        test(new int[][] {{7, 17}, {1, 3}, {2, 1}, {9, 3}, {2, 8}, {9, 4}}, 5);
        test(new int[][] {{1, 5}, {1, 70}, {3, 1000}, {55, 700}, {999876789, 53}, {987853567, 12}},
             987853512);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
