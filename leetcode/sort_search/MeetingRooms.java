import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC252: https://leetcode.com/problems/meeting-rooms/
//
// Given an array of meeting time intervals consisting of start and end times
// [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.
public class MeetingRooms {
    // beats 33.66%(13 ms for 76 tests)
    public boolean canAttendMeetings(Interval[] intervals) {
        Arrays.sort(intervals, new Comparator<Interval>() {
            public int compare(Interval a, Interval b) {
                return a.start - b.start;
            }
        });
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i].start < intervals[i - 1].end) return false;
        }
        return true;
    }

    void test(int[][] intervalArray, boolean expected) {
        Interval[] intervals = new Interval[intervalArray.length];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = new Interval(intervalArray[i][0], intervalArray[i][1]);
        }
        assertEquals(expected, canAttendMeetings(intervals));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 3}, {5, 10}, {15, 20}}, true);
        test(new int[][] {{0, 5}, {5, 10}, {15, 20}}, true);
        test(new int[][] {{0, 30}, {5, 10}, {15, 20}}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MeetingRooms");
    }
}
