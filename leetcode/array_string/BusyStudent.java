import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1450: https://leetcode.com/problems/number-of-students-doing-homework-at-a-given-time/
//
// Given two integer arrays startTime and endTime and given an integer queryTime.
// The ith student started doing their homework at the time startTime[i] and finished it at time
// endTime[i]. Return the number of students doing their homework at time queryTime. More formally,
// return the number of students where queryTime lays in the interval [startTime[i], endTime[i]]
// inclusive.
public class BusyStudent {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 36.9 MB(92.48%) for 111 tests
    public int busyStudent(int[] startTime, int[] endTime, int queryTime) {
        int res = 0;
        for (int i = 0, n = startTime.length; i < n; i++) {
            if (startTime[i] <= queryTime && queryTime <= endTime[i]) {
                res++;
            }
        }
        return res;
    }

    void test(int[] startTime, int[] endTime, int queryTime, int expected) {
        assertEquals(expected, busyStudent(startTime, endTime, queryTime));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3}, new int[] {3, 2, 7}, 4, 1);
        test(new int[] {4}, new int[] {4}, 4, 1);
        test(new int[] {4}, new int[] {4}, 5, 0);
        test(new int[] {1, 1, 1, 1}, new int[] {1, 3, 2, 4}, 7, 0);
        test(new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1}, new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10},
             5, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
