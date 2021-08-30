import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.Interval;

// LC057: https://leetcode.com/problems/insert-interval/
//
// Given a set of non-overlapping intervals, insert a new interval into the
// intervals (merge if necessary).
// The intervals were initially sorted according to their start times.
public class InsertInterval {
    // beats 97.70%(2 ms)
    public List<Interval> insert(List<Interval> intervals, Interval newInterval) {
        int n = intervals.size();
        List<Interval> res = new ArrayList<>();
        int startPos = getIndex(intervals, 0, newInterval.start);
        if (startPos == n) { // put to last
            res.addAll(intervals);
            res.add(newInterval);
            return res;
        }

        // Interval interval = intervals.get(startPos);
        for (int i = 0; i < startPos; i++) {
            res.add(intervals.get(i));
        }

        int start = Math.min(intervals.get(startPos).start, newInterval.start);
        int endPos = getIndex(intervals, startPos, newInterval.end);

        int end = (endPos == n) ? Integer.MAX_VALUE : intervals.get(endPos).start;
        if (newInterval.end < end) {
            end = newInterval.end;
        } else { // overlap
            end = Math.max(intervals.get(endPos).end, newInterval.end);
            endPos++;
        }

        res.add(new Interval(start, end));
        for (int i = endPos; i < n; i++) {
            res.add(intervals.get(i));
        }
        return res;
    }

    // find the leftmost index of interval that covers or is behind the target
    private int getIndex(List<Interval> intervals, int offset, int target) {
        int n = intervals.size();
        if (n == 0)
            return 0;

        int high = n - 1;
        int low = offset;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midInterval = intervals.get(mid).start;
            if (midInterval == target)
                return mid;

            if (midInterval > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        if (high < 0)
            return 0;
        return (intervals.get(high).end >= target) ? high : high + 1;
    }

    // http://www.jiuzhang.com/solutions/insert-interval/
    // beats 70.43%(4 ms)
    public List<Interval> insert2(List<Interval> intervals, Interval newInterval) {
        List<Interval> res = new ArrayList<Interval>();
        int insertPos = 0;
        for (Interval interval : intervals) {
            if (interval.end < newInterval.start) {
                res.add(interval);
                insertPos++;
            } else if (interval.start > newInterval.end) {
                res.add(interval);
            } else {
                newInterval.start = Math.min(interval.start, newInterval.start);
                newInterval.end = Math.max(interval.end, newInterval.end);
            }
        }
        res.add(insertPos, newInterval);
        return res;
    }

    // Solution of Choice
    public List<Interval> insert3(List<Interval> intervals, Interval newInterval) {
        List<Interval> res = new ArrayList<>();
        int i = 0;
        int start = newInterval.start;
        int end = newInterval.end;
        for (; i < intervals.size() && intervals.get(i).end < start; i++) {
            res.add(intervals.get(i));
        }
        for (; i < intervals.size() && intervals.get(i).start <= end; i++) {
            start = Math.min(start, intervals.get(i).start);
            end = Math.max(end, intervals.get(i).end);
        }
        res.add(new Interval(start, end));
        for (; i < intervals.size(); i++) {
            res.add(intervals.get(i));
        }
        return res;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    void test(Function<List<Interval>, Interval, List<Interval>> insert, String name,
              int[] intervals, int[] newInterval, int[] expected) {
        List<Interval> intervalList = new ArrayList<>();
        for (int i = 0; i < intervals.length; i += 2) {
            intervalList.add(new Interval(intervals[i], intervals[i + 1]));
        }
        List<Interval> inserted =
                insert.apply(intervalList, new Interval(newInterval[0], newInterval[1]));
        int i = 0;
        int[] res = new int[expected.length];
        for (Interval interval : inserted) {
            res[i++] = interval.start;
            res[i++] = interval.end;
        }
        assertArrayEquals(expected, res);
    }

    void test(int[] intervals, int[] newInterval, int... expected) {
        InsertInterval i = new InsertInterval();
        test(i::insert, "insert", intervals, newInterval, expected);
        test(i::insert2, "insert2", intervals, newInterval, expected);
        test(i::insert3, "insert3", intervals, newInterval, expected);
    }

    @Test public void test1() {
        test(new int[] {1, 5}, new int[] {6, 7}, 1, 5, 6, 7);
        test(new int[] {1, 5}, new int[] {0, 3}, 0, 5);
        test(new int[] {1, 5}, new int[] {2, 3}, 1, 5);
        test(new int[] {}, new int[] {2, 5}, 2, 5);
        test(new int[] {2, 3}, new int[] {3, 3}, 2, 3);
        test(new int[] {2, 3}, new int[] {3, 5}, 2, 5);
        test(new int[] {2, 3, 6, 8}, new int[] {3, 5}, 2, 5, 6, 8);
        test(new int[] {2, 3, 6, 8}, new int[] {3, 6}, 2, 8);
        test(new int[] {1, 3, 6, 9}, new int[] {2, 5}, 1, 5, 6, 9);
        test(new int[] {1, 2, 3, 5, 6, 7, 8, 10, 12, 16}, new int[] {4, 9}, 1, 2, 3, 10, 12, 16);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
