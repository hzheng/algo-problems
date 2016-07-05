import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/the-skyline-problem/
//
// A city's skyline is the outer contour of the silhouette formed by all the
// buildings in that city when viewed from a distance. Now suppose you are given
// the locations and height of all the buildings as shown on a cityscape photo,
// write a program to output the skyline formed by these buildings collectively.
// The geometric information of each building is represented by a triplet of
// integers [Li, Ri, Hi], where Li and Ri are the x coordinates of the left and
// right edge of the ith building, respectively, and Hi is its height.
// The output is a list of "key points" (red dots in Figure B) in the format of
// [ [x1,y1], [x2, y2], [x3, y3], ... ] that uniquely defines a skyline. A key
// point is the left endpoint of a horizontal line segment.
//
// Notes:
// The number of buildings in any input list are in the range [0, 10000].
// The input list is already sorted in ascending order by the left x position Li.
// The output list must be sorted by the x position.
public class Skyline {
    // beats 95.26%(13 ms)
    public List<int[]> getSkyline(int[][] buildings) {
        int n = buildings.length;
        if (n == 0) return Collections.emptyList();

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        Deque<int[]> skylines = new LinkedList<>();
        skylines.offer(buildings[0]);
        for (int i = 1; i < n || !pq.isEmpty(); ) {
            int[] last = skylines.peekLast();
            int[] next;
            int[] head = pq.peek();
            if (head != null && (i >= n || head[0] < buildings[i][0])) {
                next = pq.poll();
            } else {
                next = buildings[i++];
            }

            if (last[1] <= next[0]) { // separate part
                if (last[1] < next[0]) {
                    skylines.offer(new int[] {last[1], next[0], 0});
                    skylines.offer(next);
                } else {
                    if (next[2] == last[2]) {
                        last[1] = Math.max(last[1], next[1]);
                    } else {
                        skylines.offer(next);
                    }
                }
                continue;
            }

            // overlap
            if (next[2] < last[2]) {
                if (next[1] > last[1]) { // not fully covered
                    pq.offer(new int[] {last[1], next[1], next[2]});
                }
            } else if (next[2] == last[2]) {
                if (next[1] > last[1]) { // extend
                    last[1] = next[1];
                }
            } else {
                if (next[1] < last[1]) { // strech
                    pq.offer(new int[] {next[1], last[1], last[2]});
                }
                last[1] = next[0];
                if (last[0] == last[1]) {
                    skylines.pollLast();
                }
                skylines.offer(next);
            }

        }
        List<int[]> res = new ArrayList<>(skylines.size());
        for (int[] skyline : skylines) {
            res.add(new int[] {skyline[0], skyline[2]});
        }
        res.add(new int[] {skylines.peekLast()[1], 0});
        return res;
    }

    void test(int[][] buildings, int[][] expected) {
        int[][] res = getSkyline(buildings).toArray(new int[0][0]);
        assertArrayEquals(expected, res);
    }

    @Test
    public void test1() {
        test(new int[][] {{3, 10, 20}, {3, 9, 19}, {3, 8, 18}, {3, 7, 17},
                          {3, 6, 16}, {3, 5, 15}, {3, 4, 14}},
             new int[][] {{3, 20}, {10, 0}});
        test(new int[][] {{0, 2, 3}, {2, 5, 3}},
             new int[][] {{0, 3}, {5, 0}});
        test(new int[][] {{2, 9, 10}, {3, 7, 15}, {5, 12, 12},
                          {15, 20, 10}, {19, 24, 8}},
             new int[][] {{2, 10}, {3, 15}, {7, 12}, {12, 0},
                          {15, 10}, {20, 8}, {24, 0}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Skyline");
    }
}
