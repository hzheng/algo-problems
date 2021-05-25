import org.junit.Test;

import static org.junit.Assert.*;

// LC1870: https://leetcode.com/problems/minimum-speed-to-arrive-on-time/
//
// You are given a floating-point number hour, representing the amount of time you have to reach the
// office. To commute to the office, you must take n trains in sequential order. You are also given
// an integer array dist of length n, where dist[i] describes the distance (in kilometers) of the
// ith train ride.
// Each train can only depart at an integer hour, so you may need to wait in between each train
// ride.
// For example, if the 1st train ride takes 1.5 hours, you must wait for an additional 0.5 hours
// before you can depart on the 2nd train ride at the 2 hour mark.
// Return the minimum positive integer speed (in kilometers per hour) that all the trains must
// travel at for you to reach the office on time, or -1 if it is impossible to be on time.
// Tests are generated such that the answer will not exceed 10^7 and hour will have at most two
// digits after the decimal point.
//
// Constraints:
// n == dist.length
// 1 <= n <= 10^5
// 1 <= dist[i] <= 10^5
// 1 <= hour <= 10^9
// There will be at most two digits after the decimal point in hour.
public class MinSpeedOnTime {
    // Binary Search
    // time complexity: O(log(MAX)), space complexity: O(1)
    // 132 ms(84.68%), 53.4 MB(80.54%) for 53 tests
    public int minSpeedOnTime(int[] dist, double hour) {
        int low = 1;
        int max = (int)1e7 + 1;
        for (int high = max, n = dist.length; low < high; ) {
            int mid = (low + high) >>> 1;
            double time = dist[n - 1] / (double)mid;
            for (int i = 0; i < n - 1; i++) {
                time += (Math.ceil((double)dist[i] / mid));
            }
            if (time <= hour) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low == max ? -1 : low;
    }

    // Binary Search
    // time complexity: O(log(MAX)), space complexity: O(1)
    // 126 ms(86.39%), 52.9 MB(97.08%) for 53 tests
    public int minSpeedOnTime2(int[] dist, double hour) {
        int res = -1;
        for (int low = 1, high = (int)1e7, n = dist.length; low <= high; ) {
            int mid = (low + high) >>> 1;
            double time = dist[n - 1] / (double)mid;
            for (int i = 0; i < n - 1; i++) {
                time += (Math.ceil((double)dist[i] / mid));
            }
            if (time <= hour) {
                high = mid - 1;
                res = mid;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    private void test(int[] dist, double hour, int expected) {
        assertEquals(expected, minSpeedOnTime(dist, hour));
        assertEquals(expected, minSpeedOnTime2(dist, hour));
    }

    @Test public void test1() {
        test(new int[] {1, 3, 2}, 6, 1);
        test(new int[] {1, 3, 2}, 2.7, 3);
        test(new int[] {1, 3, 2}, 1.9, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
