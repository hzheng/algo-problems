import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1109: https://leetcode.com/problems/corporate-flight-bookings/
//
// There are n flights, and they are labeled from 1 to n. We have a list of flight bookings. The
// i-th booking bookings[i] = [i, j, k] means that we booked k seats from flights labeled i to j
// inclusive. Return an array answer of length n, representing the number of seats booked on each
// flight in order of their label.
//
// Constraints:
// 1 <= bookings.length <= 20000
// 1 <= bookings[i][0] <= bookings[i][1] <= n <= 20000
// 1 <= bookings[i][2] <= 10000
public class CorpFlightBookings {
    // Line Sweep
    // time complexity: O(N+M), space complexity: O(N)
    // 2 ms(100.00%), 54.2 MB(78.14%) for 63 tests
    public int[] corpFlightBookings(int[][] bookings, int n) {
        int[] res = new int[n];
        for (int[] book : bookings) {
            res[book[0] - 1] += book[2];
            if (book[1] < n) {
                res[book[1]] -= book[2];
            }
        }
        for (int i = 1; i < n; i++) {
            res[i] += res[i - 1];
        }
        return res;
    }

    private void test(int[][] bookings, int n, int[] expected) {
        assertArrayEquals(expected, corpFlightBookings(bookings, n));
    }

    @Test public void test() {
        test(new int[][] {{1, 2, 10}, {2, 3, 20}, {2, 5, 25}}, 5, new int[] {10, 55, 45, 25, 25});
        test(new int[][] {{1, 2, 11}, {5, 5, 7}, {6, 7, 32}, {3, 5, 20}, {2, 5, 17}, {4, 8, 41},
                          {2, 5, 25}}, 9, new int[] {11, 53, 62, 103, 110, 73, 73, 41, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
