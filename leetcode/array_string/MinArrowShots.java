import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC452: https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/
//
// There are a number of spherical balloons spread in two-dimensional space. For
// each balloon, provided input is the start and end coordinates of the
// horizontal diameter. Since it's horizontal, y-coordinates don't matter and
// hence the x-coordinates of start and end of the diameter suffice. Start is
// always smaller than end. There will be at most 10 ^ 4 balloons.
// An arrow can be shot up exactly vertically from different points along the
// x-axis. A balloon with xstart and xend bursts by an arrow shot at x if
// xstart ≤ x ≤ xend. There is no limit to the number of arrows that can be shot.
// An arrow once shot keeps travelling up infinitely. The problem is to find the
//  minimum number of arrows that must be shot to burst all balloons.
public class MinArrowShots {
    // Heap
    // beats N/A(45 ms for 43 tests)
    public int findMinArrowShots(int[][] points) {
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(
            new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        for (int[] p : points) {
            pq.offer(p);
        }
        for (int count = 0;; count++) {
            if (pq.isEmpty()) return count;

            int[] p1 = pq.poll();
            for (int end = p1[1]; !pq.isEmpty(); ) {
                int[] p2 = pq.peek();
                if (p2[0] <= end) {
                    end = Math.min(end, pq.poll()[1]);
                } else break;
            }
        }
    }

    // Sort
    // beats N/A(42 ms for 43 tests)
    public int findMinArrowShots2(int[][] points) {
        Arrays.sort(points, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        Integer end = null;
        int count = 0;
        for (int[] p : points) {
            if (end == null || end < p[0]) {
                end = p[1];
                count++;
            } else {
                end = Math.min(end, p[1]);
            }
       }
       return count;
    }

    void test(int[][] points, int expected) {
        assertEquals(expected, findMinArrowShots(points));
        assertEquals(expected, findMinArrowShots2(points));
    }

    @Test
    public void test() {
        test(new int[][] {{-2147483648, 2147483647}}, 1);
        test(new int[][] {{1, 2}, {3, 4}, {5, 6}, {7, 8}}, 4);
        test(new int[][] {}, 0);
        test(new int[][] {{10, 16}, {2, 8}, {1, 6}, {7, 12}}, 2);
        test(new int[][] {{9, 12}, {1, 10}, {4, 11}, {8, 12}, {3, 9}, {6, 9}, {6, 7}}, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinArrowShots");
    }
}
