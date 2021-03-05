import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1288: https://leetcode.com/problems/remove-covered-intervals/
//
// Given a list of intervals, remove all intervals that are covered by another interval in the list.
// Interval [a,b) is covered by interval [c,d) if and only if c <= a and b <= d.
// After doing so, return the number of remaining intervals.
//
// Constraints:
// 1 <= intervals.length <= 1000
// intervals[i].length == 2
// 0 <= intervals[i][0] < intervals[i][1] <= 10^5
// All the intervals are unique.
public class RemoveCoveredIntervals {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 4 ms(98.21%), 39.4 MB(60.90%) for 32 tests
    public int removeCoveredIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (a[0] == b[0]) ? (b[1] - a[1]) : (a[0] - b[0]));
        int n = intervals.length;
        int res = n;
        int[] prev = intervals[0];
        for (int i = 1; i < n; i++) {
            int[] cur = intervals[i];
            if (cur[0] == prev[0] || cur[1] <= prev[1]) {
                res--;
            } else {
                prev = cur;
            }
        }
        return res;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 4 ms(98.21%), 39.1 MB(86.57%) for 32 tests
    public int removeCoveredIntervals2(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (a[0] == b[0]) ? (b[1] - a[1]) : (a[0] - b[0]));
        int res = 0;
        int right = 0;
        for (int[] i : intervals) {
            if (i[1] > right) {
                right = i[1];
                res++;
            }
        }
        return res;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 4 ms(98.21%), 39.1 MB(86.57%) for 32 tests
    public int removeCoveredIntervals3(int[][] intervals) {
        int res = 0;
        int left = -1;
        int right = -1;
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        for (int[] i : intervals) {
            if (i[0] > left && i[1] > right) {
                left = i[0];
                res++;
            }
            right = Math.max(right, i[1]);
        }
        return res;
    }

    private void test(int[][] intervals, int expected) {
        assertEquals(expected, removeCoveredIntervals(intervals));
        assertEquals(expected, removeCoveredIntervals2(intervals));
        assertEquals(expected, removeCoveredIntervals3(intervals));
    }

    @Test public void test() {
        test(new int[][] {{1, 4}, {3, 6}, {2, 8}}, 2);
        test(new int[][] {{1, 4}, {2, 3}}, 1);
        test(new int[][] {{0, 10}, {5, 12}}, 2);
        test(new int[][] {{3, 10}, {4, 10}, {5, 11}}, 2);
        test(new int[][] {{1, 2}, {1, 4}, {3, 4}}, 1);
        test(new int[][] {{1, 2}, {1, 3}}, 1);
        test(new int[][] {{1, 3}, {1, 8}, {5, 8}}, 1);
        test(new int[][] {{1, 6}, {4, 6}, {4, 8}}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
