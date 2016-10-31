import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;

// LC436: https://leetcode.com/problems/find-right-interval/
//
// Given a set of intervals, for each of the interval i, check if there exists
// an interval j whose start point is bigger than or equal to the end point of
// the interval i, which can be called that j is on the "right" of i.
// For any interval i, you need to store the minimum interval j's index, which
// means that the interval j has the minimum start point to build the "right"
// relationship for interval i. If the interval j doesn't exist, store -1 for
// the interval i. Finally, you need output the stored value of each interval
// as an array.
public class RightInterval {
    // NavigableSet
    // beats N/A(49 ms for 15 tests)
    public int[] findRightInterval(Interval[] intervals) {
        NavigableSet<IndexedInterval> set = new TreeSet<>();
        for (int i = 0; i < intervals.length; i++) {
            set.add(new IndexedInterval(i, intervals[i].start, intervals[i].end));
        }
        int[] res = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            IndexedInterval r = set.ceiling(new IndexedInterval(i, intervals[i].end, 0));
            res[i] = (r != null) ? r.index : -1;
        }
        return res;
    }

    private static class IndexedInterval implements Comparable<IndexedInterval> {
        int start;
        int end;
        int index;
        IndexedInterval(int i, int start, int end) {
            this.index = i;
            this.start = start;
            this.end = end;
        }

        public int compareTo(IndexedInterval other) {
            return start - other.start;
        }
    }

    // NavigableMap
    // beats N/A(54 ms for 15 tests)
    public int[] findRightInterval2(Interval[] intervals) {
        NavigableMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < intervals.length; i++) {
            map.put(intervals[i].start, i);
        }
        int[] res = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            Map.Entry<Integer, Integer> r = map.ceilingEntry(intervals[i].end);
            res[i] = (r != null) ? r.getValue() : -1;
        }
        return res;
    }

    void test(Function<Interval[], int[]> rightInterval,
              int[][] intervalArrays, int ... expected) {
        Interval[] intervals = new Interval[intervalArrays.length];
        for (int i = 0; i < intervalArrays.length; i++) {
            intervals[i] = new Interval(intervalArrays[i][0], intervalArrays[i][1]);
        }
        assertArrayEquals(expected, rightInterval.apply(intervals));
    }

    void test(int[][] intervalArrays, int ... expected) {
        RightInterval r = new RightInterval();
        test(r::findRightInterval, intervalArrays, expected);
        test(r::findRightInterval2, intervalArrays, expected);
    }

    @Test
    public void test() {
        test(new int[][] {{3, 4}, {2, 3}, {1, 2}}, -1, 0, 1);
        test(new int[][] {{1, 4}, {2, 3}, {3, 4}}, -1, 2, -1);
        test(new int[][] {{1, 2}}, -1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RightInterval");
    }
}
