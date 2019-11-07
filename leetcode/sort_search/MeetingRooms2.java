import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC253: https://leetcode.com/problems/meeting-rooms-ii/
//
// Given an array of meeting time intervals consisting of start and end times
// [[s1,e1],[s2,e2],...], find the minimum number of conference rooms required.
public class MeetingRooms2 {
    // Greedy + Heap + Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 50.15%(17 ms for 77 tests)
    public int minMeetingRooms(Interval[] intervals) {
        Arrays.sort(intervals, new Comparator<Interval>() {
            public int compare(Interval a, Interval b) {
                return a.start - b.start;
            }
        });
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(Integer.MAX_VALUE);
        for (Interval interval : intervals) {
            if (interval.start >= pq.peek()) {
                pq.poll(); // merge or reuse a room
            }
            pq.offer(interval.end);
        }
        return pq.size() - 1;
    }

    // Sort + Two Pointers
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 88.96%(4 ms for 77 tests)
    public int minMeetingRooms2(Interval[] intervals) {
        int n = intervals.length;
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = intervals[i].start;
            ends[i] = intervals[i].end;
        }
        Arrays.sort(starts);
        Arrays.sort(ends);
        int res = 0;
        for (int i = 0, j = 0; i < n; i++) {
            if (starts[i] < ends[j]) {
                res++;
            } else {
                j++;
            }
        }
        return res;
    }

    // Sort + Two Pointers
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats ?%(? ms for 77 tests)
    public int minMeetingRooms3(Interval[] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a.end));
        int res = 0;
        for (int i = 0, j = 0; i < intervals.length; i++) {
            if (intervals[i].start < intervals[j].end) {
                res = Math.max(res, i - j + 1);
            } else {
                j++;
            }
        }
        return res;
    }

    // time complexity: O(N + MAX_T), space complexity: O(MAX_T)
    // beats ?%(? ms for 77 tests)
    public int minMeetingRooms4(Interval[] intervals) {
        int maxEnd = Integer.MIN_VALUE;
        for (Interval i : intervals) {
            maxEnd = Math.max(maxEnd, i.end);
        }
        int timeline[] = new int[maxEnd + 1]; // could shrink interval to save space
        for (Interval i : intervals) {
            timeline[i.start]++; // open one room
            timeline[i.end]--; // close the room
        }
        int res = 0;
        int openRooms = 0;
        for (int openRoom : timeline) {
            res = Math.max(res, openRooms += openRoom);
        }
        return res;
    }

    // SortedMap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats ?%(? ms for 77 tests)
    public int minMeetingRooms5(Interval[] intervals) {
        SortedMap<Integer, Integer> timeline = new TreeMap<>();
        for (Interval i : intervals) {
            timeline.put(i.start, timeline.getOrDefault(i.start, 0) + 1);
            timeline.put(i.end, timeline.getOrDefault(i.end, 0) - 1);
        }
        int res = 0;
        int openRooms = 0;
        for (int openRoom : timeline.values()) {
            res = Math.max(res, openRooms += openRoom);
        }
        return res;
    }

    void test(int[][] intervalArray, int expected) {
        Interval[] intervals = new Interval[intervalArray.length];
        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = new Interval(intervalArray[i][0], intervalArray[i][1]);
        }
        assertEquals(expected, minMeetingRooms(intervals.clone()));
        assertEquals(expected, minMeetingRooms2(intervals));
        assertEquals(expected, minMeetingRooms3(intervals));
        assertEquals(expected, minMeetingRooms4(intervals));
        assertEquals(expected, minMeetingRooms5(intervals));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 3}, {5, 10}, {15, 20}}, 1);
        test(new int[][] {{0, 5}, {5, 10}, {15, 20}}, 1);
        test(new int[][] {{0, 30}, {5, 10}, {15, 20}}, 2);
        test(new int[][] {{1, 5}, {8, 9}, {8, 9}}, 2);
        test(new int[][] {{0, 30}, {5, 10}, {15, 20}, {8, 18}}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
