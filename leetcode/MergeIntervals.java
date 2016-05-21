import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a collection of intervals, merge all overlapping intervals.
class Interval {
    int start;
    int end;
    Interval() {
        start = 0; end = 0;
    }

    Interval(int s, int e) {
        start = s; end = e;
    }

    public String toString() {
        return "[" + start + "," + end + "]";
    }
}

public class MergeIntervals {
    // beats 1.36%
    public List<Interval> merge(List<Interval> intervals) {
        SortedMap<Integer, Interval> map = new TreeMap<>();

        for (Interval i : intervals) {
            int start = i.start;
            int end = i.end;
            SortedMap<Integer, Interval> heads = map.headMap(start);
            if (!heads.isEmpty()) {
                Interval lastInterval = heads.get(heads.lastKey());
                if (lastInterval.end >= start) {
                    start = lastInterval.start;
                }
                end = Math.max(end, lastInterval.end);
            }
            heads = map.headMap(end + 1);
            if (!heads.isEmpty()) {
                Interval lastInterval = heads.get(heads.lastKey());
                end = Math.max(end, lastInterval.end);
            }

            SortedMap<Integer, Interval> merged = map.subMap(start, end + 1);
            for (int index : new ArrayList<>(merged.keySet())) {
                map.remove(index);
            }
            map.put(start, new Interval(start, end));
        }
        return new ArrayList<>(map.values());
    }

    void test(int[] expected, int ... intervals) {
        int [] res = new int[expected.length];
        List<Interval> intervalList = new ArrayList<>();
        for (int i = 0; i < intervals.length; i += 2) {
            intervalList.add(new Interval(intervals[i], intervals[i + 1]));
        }
        int i = 0;
        List<Interval> merged = merge(intervalList);
        Collections.sort(merged, ((a, b) -> a.start - b.start));
        for (Interval interval : merged) {
            res[i++] = interval.start;
            res[i++] = interval.end;
        }
        assertArrayEquals(expected, res);
    }

    @Test
    public void test1() {
        test(new int[] {1, 6, 8, 10, 15, 18}, 1, 3, 2, 6, 8, 10, 15, 18);
        test(new int[] {1, 18}, 1, 3, 2, 6, 8, 10, 5, 18);
        test(new int[] {1, 18}, 1, 3, 2, 6, 5, 18);
        test(new int[] {1, 8}, 1, 2, 2, 3, 3, 8);
        test(new int[] {1, 1, 2, 2}, 1, 1, 2, 2);
        test(new int[] {1, 2}, 1, 1, 1, 2);
        test(new int[] {1, 2, 3, 4}, 1, 2, 3, 4);
        test(new int[] {1, 3}, 1, 3, 3, 3);
        test(new int[] {0, 3, 10, 10, 21, 24, 28, 30, 37, 37, 41, 50, 51, 64,
                        68, 73, 74, 79, 84, 84, 91, 92, 94, 95, 96, 100},
             74, 78, 61, 63, 46, 50, 79, 79, 52, 52, 94, 95, 51, 55, 97, 100,
             57, 59, 75, 79, 0, 3, 41, 45, 45, 48, 21, 24, 69, 73, 22, 24,
             51, 54, 96, 98, 28, 28, 48, 48, 68, 70, 43, 47, 91, 92, 84, 84,
             63, 64, 10, 10, 55, 58, 46, 48, 59, 63, 37, 37, 74, 75, 28, 30,
             98, 99, 22, 22);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MergeIntervals");
    }
}
