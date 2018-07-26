import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC871: https://leetcode.com/problems/minimum-number-of-refueling-stops/
//
// A car travels to a destination which is 'target' miles away. Along the way,
// there are gas stations. Each station[i] represents a gas station that is
// station[i][0] miles away of the starting position, and has station[i][1]
// liters of gas. The car initially has startFuel liters of fuel in it.  It uses
// 1 liter of gas per 1 mile that it drives. When the car reaches a gas station,
// it may stop and refuel, transferring all the gas from the station into it.
// What is the least number of refueling stops the car must make in order to
// reach its destination?  If it cannot reach the destination, return -1.
public class MinRefuelStops {
    // Dynamic Programming
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 30.68%(39 ms for 198 tests)
    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        int n = stations.length;
        long[] dp = new long[n + 1];
        dp[0] = startFuel;
        for (int i = 0; i < n; i++) {
            for (int j = i; j >= 0; j--) {
                if (dp[j] >= stations[i][0]) {
                    dp[j + 1] = Math.max(dp[j + 1],
                                         dp[j] + (long) stations[i][1]);
                }
            }
        }
        for (int i = 0; i <= n; i++) {
            if (dp[i] >= target) return i;
        }
        return -1;
    }

    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 61.71%(18 ms for 198 tests)
    public int minRefuelStops2(int target, int startFuel, int[][] stations) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(
            Collections.reverseOrder());
        int res = 0;
        int prevLoc = 0;
        int fuel = startFuel;
        for (int[] station : stations) {
            int curLoc = station[0];
            for (fuel -= curLoc - prevLoc; !pq.isEmpty() && fuel < 0; res++) {
                fuel += pq.poll();
            }
            if (fuel < 0) return -1;

            pq.offer(station[1]);
            prevLoc = curLoc;
        }
        for (fuel -= target - prevLoc; !pq.isEmpty() && fuel < 0; res++) {
            fuel += pq.poll();
        }
        return (fuel < 0) ? -1 : res;
    }

    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 93.91%(13 ms for 198 tests)
    public int minRefuelStops3(int target, int startFuel, int[][] stations) {
        Queue<Integer> pq = new PriorityQueue<>();
        int res = 0;
        for (int i = 0, fuel = startFuel; fuel < target; res++) {
            while (i < stations.length && stations[i][0] <= fuel) {
                pq.offer(-stations[i++][1]);
            }
            if (pq.isEmpty()) return -1;

            fuel -= pq.poll();
        }
        return res;
    }

    void test(int target, int startFuel, int[][] stations, int expected) {
        assertEquals(expected, minRefuelStops(target, startFuel, stations));
        assertEquals(expected, minRefuelStops2(target, startFuel, stations));
        assertEquals(expected, minRefuelStops3(target, startFuel, stations));
    }

    @Test
    public void test() {
        test(999, 1000, new int[][] { { 5, 100 }, { 997, 100 }, { 998, 100 } },
             0);
        test(1, 1, new int[][] {}, 0);
        test(100, 1, new int[][] { { 10, 100 } }, -1);
        test(100, 10, new int[][] { { 10, 60 }, { 20, 30 }, { 30, 30 }, { 60,
                                                                          40 } },
             2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
