import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1883: https://leetcode.com/problems/minimum-skips-to-arrive-at-meeting-on-time/
//
// You are given an integer hoursBefore, the number of hours you have to travel to your meeting. To
// arrive at your meeting, you have to travel through n roads. The road lengths are given as an
// integer array dist of length n, where dist[i] describes the length of the ith road in kilometers.
// In addition, you are given an integer speed, which is the speed (in km/h) you will travel at.
// After you travel road i, you must rest and wait for the next integer hour before you can begin
// traveling on the next road. Note that you do not have to rest after traveling the last road
// because you are already at the meeting.
// For example, if traveling a road takes 1.4 hours, you must wait until the 2 hour mark before
// traveling the next road. If traveling a road takes exactly 2 hours, you do not need to wait.
// However, you are allowed to skip some rests to be able to arrive on time, meaning you do not need
// to wait for the next integer hour. Note that this means you may finish traveling future roads at
// different hour marks.
// For example, suppose traveling the first road takes 1.4 hours and traveling the second road takes
// 0.6 hours. Skipping the rest after the first road will mean you finish traveling the second road
// right at the 2 hour mark, letting you start traveling the third road immediately.
// Return the minimum number of skips required to arrive at the meeting on time, or -1 if it is
// impossible.
//
// Constraints:
// n == dist.length
// 1 <= n <= 1000
// 1 <= dist[i] <= 10^5
// 1 <= speed <= 10^6
// 1 <= hoursBefore <= 10^7
public class MinSkips {
    // 2D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 180 ms(26.41%), 112.3 MB(13.21%) for 56 tests
    public int minSkips(int[] dist, int speed, int hoursBefore) {
        int n = dist.length;
        long[][] dp = new long[n + 1][n + 1];
        long max = (long)speed * hoursBefore;
        for (long[] a : dp) {
            Arrays.fill(a, max + 1);
        }
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k <= i; k++) {
                dp[i + 1][k] = Math.min(dp[i + 1][k],
                                        (long)(Math.ceil((double)(dp[i][k] + dist[i]) / speed))
                                        * speed);
                dp[i + 1][k + 1] = Math.min(dp[i + 1][k + 1], dp[i][k] + dist[i]);
            }
        }
        for (int k = 0; k < n; k++) {
            if (dp[n][k] <= max) { return k; }
        }
        return -1;
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N)
    // 77 ms(63.21%), 40.2 MB(73.58%) for 56 tests
    public int minSkips2(int[] dist, int speed, int hoursBefore) {
        int n = dist.length;
        int[] dp = new int[n + 1];
        for (int i = 0; i < n; i++) {
            for (int k = n - 1; k >= 0; k--) {
                dp[k] += dist[i];
                if (i < n - 1) {
                    dp[k] = (dp[k] + speed - 1) / speed * speed;
                }
                if (k > 0) {
                    dp[k] = Math.min(dp[k], dp[k - 1] + dist[i]);
                }
            }
        }
        for (int k = 0; k < n; k++) {
            if (dp[k] <= (long)speed * hoursBefore) { return k; }
        }
        return -1;
    }

    private void test(int[] dist, int speed, int hoursBefore, int expected) {
        assertEquals(expected, minSkips(dist, speed, hoursBefore));
        assertEquals(expected, minSkips2(dist, speed, hoursBefore));
    }

    @Test public void test1() {
        test(new int[] {1, 3, 2}, 4, 2, 1);
        test(new int[] {7, 3, 5, 5}, 2, 10, 2);
        test(new int[] {7, 3, 5, 5}, 1, 10, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
