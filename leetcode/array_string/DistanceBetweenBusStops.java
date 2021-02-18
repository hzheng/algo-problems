import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1184: https://leetcode.com/problems/distance-between-bus-stops/
//
// A bus has n stops numbered from 0 to n - 1 that form a circle. We know the distance between all
// pairs of neighboring stops where distance[i] is the distance between the stops number i and
// (i + 1) % n. The bus goes along both directions i.e. clockwise and counterclockwise.
// Return the shortest distance between the given start and destination stops.
//
// Constraints:
// 1 <= n <= 10^4
// distance.length == n
// 0 <= start, destination < n
// 0 <= distance[i] <= 10^4
public class DistanceBetweenBusStops {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 39 MB(26.79%) for 37 tests
    public int distanceBetweenBusStops(int[] distance, int start, int destination) {
        int dist1 = 0;
        int n = distance.length;
        for (int i = start; i != destination; i = (i + 1) % n) {
            dist1 += distance[i];
        }
        int dist2 = 0;
        for (int i = destination; i != start; i = (i + 1) % n) {
            dist2 += distance[i];
        }
        return Math.min(dist1, dist2);
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.6 MB(86.71%) for 37 tests
    public int distanceBetweenBusStops2(int[] distance, int start, int destination) {
        if (start > destination) {
            int tmp = start;
            start = destination;
            destination = tmp;;
        }
        int dist1 = 0;
        int dist2 = 0;
        for (int i = 0; i < distance.length; i++) {
            if (i >= start && i < destination) {
                dist1 += distance[i];
            } else {
                dist2 += distance[i];
            }
        }
        return Math.min(dist1, dist2);
    }

    private void test(int[] distance, int start, int destination, int expected) {
        assertEquals(expected, distanceBetweenBusStops(distance, start, destination));
        assertEquals(expected, distanceBetweenBusStops2(distance, start, destination));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4}, 0, 1, 1);
        test(new int[] {1, 2, 3, 4}, 0, 2, 3);
        test(new int[] {1, 2, 3, 4}, 0, 3, 4);
        test(new int[] {1, 2, 3, 4, 2, 5, 7}, 1, 4, 9);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
