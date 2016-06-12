import java.util.*;
import java.util.function.Function;

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
    // time complexity: O(N * log(N)) space complexity: O(N)
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

    // beats 86.52%
    // time complexity: O(N * log(N)) space complexity: O(N)
    public List<Interval> merge2(List<Interval> intervals) {
        List<Interval> res = new ArrayList<>();
        if (intervals.size() == 0) return res;

        PriorityQueue<Interval> queue
            = new PriorityQueue<>((a, b) -> a.start - b.start);
        queue.addAll(intervals);
        Interval prev = queue.poll();
        for (Interval next = queue.poll(); next != null; next = queue.poll()) {
            if (next.start > prev.end) {
                res.add(prev);
                prev = next;
            } else if (next.end > prev.end) {
                prev = new Interval(prev.start, next.end);
            }
        }
        res.add(prev);
        return res;
    }

    // beats 86.52%
    // time complexity: O(N * log(N)) space complexity: O(N)
    public List<Interval> merge3(List<Interval> intervals) {
        int n = intervals.size();
        if (n < 2) return intervals;

        List<Interval> res = new ArrayList<>();
        Collections.sort(intervals, ((a, b) -> a.start - b.start));
        Interval prev = intervals.get(0);
        for (int i = 1; i < n; i++) {
            Interval next = intervals.get(i);
            if (next.start > prev.end) {
                res.add(prev);
                prev = next;
            } else if (next.end > prev.end) {
                prev = new Interval(prev.start, next.end);
            }
        }
        res.add(prev);
        return res;
    }

    // http://www.geeksforgeeks.org/merging-intervals/
    // beats 0%(?)
    // time complexity: O(N * log(N)) space complexity: O(1)
    public List<Interval> merge4(List<Interval> intervals) {
        // reverse sort
        Collections.sort(intervals, ((a, b) -> b.start - a.start));
        int endIndex = 0;
        for (Interval i : intervals) {
            if (endIndex == 0 || intervals.get(endIndex - 1).start > i.end) {
                intervals.set(endIndex++, i);
                continue;
            }
            // overlapped
            do {
                Interval j = intervals.get(endIndex - 1);
                j.start = i.start;
                j.end = Math.max(j.end, i.end);
                endIndex--;
            } while (endIndex > 0 && intervals.get(endIndex - 1).start <= i.end);
            endIndex++;
        }
        return intervals.subList(0, endIndex);
    }

    // beats 38.02%
    // time complexity: O(N * log(N)) space complexity: O(1)
    public List<Interval> merge5(List<Interval> intervals) {
        int n = intervals.size();
        if (n < 2) return intervals;

        Collections.sort(intervals, ((a, b) -> a.start - b.start));
        int endIndex = 0;
        for (int i = 1; i < n; i++) {
            Interval cur = intervals.get(i);
            Interval last = intervals.get(endIndex);
            if (last.end < cur.start) {
                intervals.set(++endIndex, cur);
            } else { // overlapped
                last.end = Math.max(cur.end, last.end);
            }
        }
        return intervals.subList(0, endIndex + 1);
    }

    void test(int[] expected, int ... intervals) {
        MergeIntervals m = new MergeIntervals();
        test(m::merge, "merge", expected, intervals);
        test(m::merge2, "merge2", expected, intervals);
        test(m::merge3, "merge3", expected, intervals);
        test(m::merge4, "merge4", expected, intervals);
        test(m::merge5, "merge5", expected, intervals);
    }

    void test(Function<List<Interval>, List<Interval> > merge, String name,
              int[] expected, int ... intervals) {
        int [] res = new int[expected.length];
        List<Interval> intervalList = new ArrayList<>();
        for (int i = 0; i < intervals.length; i += 2) {
            intervalList.add(new Interval(intervals[i], intervals[i + 1]));
        }
        int i = 0;
        List<Interval> merged = merge.apply(intervalList);
        Collections.sort(merged, ((a, b) -> a.start - b.start));
        for (Interval interval : merged) {
            res[i++] = interval.start;
            res[i++] = interval.end;
        }
        assertArrayEquals(expected, res);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2}, 1, 2);
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
