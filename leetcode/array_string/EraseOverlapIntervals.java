import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC435: https://leetcode.com/problems/non-overlapping-intervals/
//
// Given a collection of intervals, find the minimum number of intervals you
// need to remove to make the rest of the intervals non-overlapping.
public class EraseOverlapIntervals {
    // Heap
    // beats N/A(16 ms for 16 tests)
    public int eraseOverlapIntervals(Interval[] intervals) {
        PriorityQueue<Interval> pq = new PriorityQueue<>(
            new Comparator<Interval>() {
            public int compare(Interval a, Interval b) {
                return a.start - b.start;
            }
        });

        for (Interval i : intervals) {
            pq.offer(i);
        }
        int erased = 0;
        while (!pq.isEmpty()) {
            Interval i = pq.poll();
            if (pq.isEmpty()) break;

            Interval j = pq.peek();
            if (i.end <= j.start) continue;

            erased++;
            if (i.end < j.end) {
                pq.poll();
                pq.offer(i);
            }
        }
        return erased;
    }

    // beats N/A(4 ms for 16 tests)
    // Sort
    public int eraseOverlapIntervals2(Interval[] intervals) {
        Arrays.sort(intervals, new Comparator<Interval>() {
            public int compare(Interval a, Interval b) {
                return a.start - b.start;
            }
        });
        int erased = 0;
        int maxEnd = Integer.MIN_VALUE;
        for (Interval interval : intervals) {
            if (interval.end < maxEnd) {
                erased++;
                maxEnd = interval.end;
            } else if (interval.start < maxEnd) {
                erased++;
            } else {
                maxEnd = interval.end;
            }
        }
        return erased;
    }

    void test(Function<Interval[], Integer> eraseOverlapIntervals,
              int[][] intervalArrays, int expected) {
        Interval[] intervals = new Interval[intervalArrays.length];
        for (int i = 0; i < intervalArrays.length; i++) {
            intervals[i] = new Interval(intervalArrays[i][0], intervalArrays[i][1]);
        }
        assertEquals((Integer)expected, eraseOverlapIntervals.apply(intervals));
    }

    void test(int[][] intervalArrays, int expected) {
        EraseOverlapIntervals e = new EraseOverlapIntervals();
        test(e::eraseOverlapIntervals, intervalArrays, expected);
        test(e::eraseOverlapIntervals2, intervalArrays, expected);
    }

    @Test
    public void test() {
        test(new int[][] {{1, 2}, {2, 3}, {3, 4}, {1,3}}, 1);
        test(new int[][] {{1, 2}, {1, 2}, {1, 2}}, 2);
        test(new int[][] {{1, 2}, {2, 3}}, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EraseOverlapIntervals");
    }
}
