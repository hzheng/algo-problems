import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1094: https://leetcode.com/problems/car-pooling/
//
// You are driving a vehicle that has 'capacity' empty seats initially available for passengers. The
// vehicle only drives east.
// Given a list of trips, trip[i] = [num_passengers, start_location, end_location] contains
// information about the i-th trip: the number of passengers that must be picked up, and the
// locations to pick them up and drop them off.  The locations are given as the number of kilometers
// due east from your vehicle's initial location.
// Return true if and only if it is possible to pick up and drop off all passengers for all the
// given trips.
// Constraints:
// trips.length <= 1000
// trips[i].length == 3
// 1 <= trips[i][0] <= 100
// 0 <= trips[i][1] < trips[i][2] <= 1000
// 1 <= capacity <= 100000
public class CarPooling {
    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 4 ms(73.84%), 42.1 MB(100%) for 54 tests
    public boolean carPooling(int[][] trips, int capacity) {
        PriorityQueue<int[]> pq =
                new PriorityQueue<>((a, b) -> (a[1] == b[1]) ? (a[0] - b[0]) : (a[1] - b[1]));
        for (int[] trip : trips) {
            pq.offer(new int[]{trip[0], trip[1]});
            pq.offer(new int[]{-trip[0], trip[2]});
        }

        for (int passengers = 0; !pq.isEmpty(); ) {
            if ((passengers += pq.poll()[0]) > capacity) { return false; }
        }
        return true;
    }

    // SortedMap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 6 ms(64.49%), 42.3 MB(100%) for 54 tests
    public boolean carPooling2(int[][] trips, int capacity) {
        Map<Integer, Integer> map = new TreeMap<>();
        for (int[] trip : trips) {
            map.put(trip[1], map.getOrDefault(trip[1], 0) + trip[0]);
            map.put(trip[2], map.getOrDefault(trip[2], 0) - trip[0]);
        }
        for (int v : map.values()) {
            if ((capacity -= v) < 0) { return false; }
        }
        return true;
    }

    // Array
    // time complexity: O(N * log(N)), space complexity: O(M) (M: max location)
    // 1 ms(99.52%), 41.5 MB(100%) for 54 tests
    public boolean carPooling3(int[][] trips, int capacity) {
        final int n = 1001;
        int[] stops = new int[n];
        for (int t[] : trips) {
            stops[t[1]] += t[0];
            stops[t[2]] -= t[0];
        }
        for (int i = 0; i < n; i++) {
            if ((capacity -= stops[i]) < 0) { return false; }
        }
        return true;
    }

    void test(int[][] trips, int capacity, boolean expected) {
        assertEquals(expected, carPooling(trips, capacity));
        assertEquals(expected, carPooling2(trips, capacity));
        assertEquals(expected, carPooling3(trips, capacity));
    }

    @Test
    public void test() {
        test(new int[][]{{2, 1, 5}, {3, 3, 7}}, 4, false);
        test(new int[][]{{2, 1, 5}, {3, 3, 7}}, 5, true);
        test(new int[][]{{2, 1, 5}, {3, 5, 7}}, 3, true);
        test(new int[][]{{3, 2, 7}, {3, 7, 9}, {8, 3, 9}}, 11, true);
        test(new int[][]{{2, 2, 6}, {2, 4, 7}, {8, 6, 7}}, 11, true);
        test(new int[][]{{9, 3, 6}, {8, 1, 7}, {6, 6, 8}, {8, 4, 9}, {4, 2, 9}}, 28, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
