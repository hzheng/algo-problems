import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC849: https://leetcode.com/problems/maximize-distance-to-closest-person/
//
// In a row of seats, 1 represents a person sitting in that seat, and 0
// represents that the seat is empty. There is at least one empty seat, and at
// least one person sitting. Alex wants to sit in the seat such that the
// distance between him and the closest person to him is maximized.
// Return that maximum distance to closest person.
public class MaxDistToClosest {
    // time complexity: O(N), space complexity: O(1)
    // beats 11.31%(11 ms for 79 tests)
    public int maxDistToClosest(int[] seats) {
        int n = seats.length;
        int i = 0;
        for (i = 0; i < n; i++) {
            if (seats[i] == 1) break;
        }
        int max = i;
        int prev = i;
        for (i++; i < n; i++) {
            int seat = seats[i];
            if (seat == 1) {
                prev = i;
            } else {
                max = Math.max(max, (i - prev + 1) / 2);
            }
        }
        return Math.max(max, (n - 1 - prev));
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // beats 24.63%(9 ms for 79 tests)
    public int maxDistToClosest2(int[] seats) {
        int n = seats.length;
        int res = 0;
        for (int i = 0, prev = -1, next = 0; i < n; i++) {
            if (seats[i] == 1) {
                prev = i;
            } else {
                for (; next < n && seats[next] == 0 || next < i; next++) {}
                int left = (prev == -1) ? n : i - prev;
                int right = (next == n) ? n : next - i;
                res = Math.max(res, Math.min(left, right));
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 16.25%(10 ms for 79 tests)
    public int maxDistToClosest3(int[] seats) {
        int n = seats.length;
        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(left, n);
        Arrays.fill(right, n);
        for (int i = 0; i < n; i++) {
            if (seats[i] == 1) {
                left[i] = 0;
            } else if (i > 0) {
                left[i] = left[i - 1] + 1;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            if (seats[i] == 1) {
                right[i] = 0;
            } else if (i < n - 1) {
                right[i] = right[i + 1] + 1;
            }
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (seats[i] == 0) {
                res = Math.max(res, Math.min(left[i], right[i]));
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 16.25%(10 ms for 79 tests)
    public int maxDistToClosest4(int[] seats) {
        int n = seats.length;
        int vacants = 0;
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (seats[i] == 1) {
                vacants = 0;
            } else {
                res = Math.max(res, (++vacants + 1) / 2);
            }
        }
        for (int i = 0; i < n; i++)  {
            if (seats[i] == 1) {
                res = Math.max(res, i);
                break;
            }
        }
        for (int i = n - 1; i >= 0; i--)  {
            if (seats[i] == 1) {
                res = Math.max(res, n - 1 - i);
                break;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 38.19%(8 ms for 79 tests)
    public int maxDistToClosest5(int[] seats) {
        int res = 0;
        boolean first = true;
        int vacants = 0;
        for (int i : seats) {         
            if (i == 0) {
                vacants++;
            } else {
                res = Math.max(res , first ? vacants : (vacants + 1) / 2);
                vacants = 0;
                first = false;
            }
        }
        return Math.max(vacants, res);
    }

    void test(int[] seats, int expected) {
        assertEquals(expected, maxDistToClosest(seats));
        assertEquals(expected, maxDistToClosest2(seats));
        assertEquals(expected, maxDistToClosest3(seats));
        assertEquals(expected, maxDistToClosest4(seats));
        assertEquals(expected, maxDistToClosest5(seats));
    }

    @Test
    public void test() {
        test(new int[] { 1, 0, 0, 0, 1, 0, 1 }, 2);
        test(new int[] { 1, 0, 0, 0 }, 3);
        test(new int[] { 0, 0, 0, 1, 0, 0, 0, 1 }, 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
