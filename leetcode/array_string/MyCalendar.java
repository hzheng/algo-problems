import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC729: https://leetcode.com/problems/my-calendar-i/
//
// Implement a MyCalendar class to store your events. A new event can be added
// if adding the event will not cause a double booking. Your class will have the
// method, book(int start, int end). Formally, this represents a booking on the
// interval [start, end), the range of real numbers x st. start <= x < end.
// A double booking happens when two events have some non-empty intersection
// For each call to the method MyCalendar.book, return true if the event can be
// added to the calendar successfully without causing a double booking.
// Otherwise, return false and do not add the event to the calendar.
public class MyCalendar {
    static interface IMyCalendar {
        public boolean book(int start, int end);
    }

    // SortedMap
    // beats 90.01%(153 ms for 108 tests)
    class MyCalendar1 implements IMyCalendar {
        private NavigableMap<Integer, Integer> cals = new TreeMap<>();

        public boolean book(int start, int end) {
            Integer low = cals.floorKey(start);
            if (low != null && cals.get(low) > start) return false;

            Integer high = cals.ceilingKey(start);
            if (high != null && high < end) return false;

            cals.put(start, end);
            return true;
        }
    }

    // List
    // beats 81.89%(160 ms for 108 tests)
    class MyCalendar2 implements IMyCalendar {
        private List<int[]> cals = new ArrayList<>();

        public boolean book(int start, int end) {
            for (int[] c : cals) {
                if (Math.max(c[0], start) < Math.min(c[1], end)) return false;
            }
            cals.add(new int[] {start, end});
            return true;
        }
    }

    // List
    // Time Limit Exceeded
    class MyCalendar3 implements IMyCalendar {
        private class Pair implements Comparable<Pair> {
            int first, second;

            public Pair(int a, int b) {
                first = a;
                second = b;
            }

            public int compareTo(Pair other) {
                return (first != other.first) ? first - other.first
                       : second - other.second;
            }
        }

        private SortedMap<Pair, Integer> cals = new TreeMap<>();

        public boolean book(int start, int end) {
            Pair startPair = new Pair(start, 1);
            Pair endPair = new Pair(end, -1);
            cals.put(startPair, cals.getOrDefault(startPair, 0) + 1);
            cals.put(endPair, cals.getOrDefault(endPair, 0) + 1);
            int sum = 0;
            for (Pair c : cals.keySet()) {
                sum += cals.get(c) * c.second;
                if (sum > 1) {
                    cals.put(startPair, cals.getOrDefault(startPair, 0) - 1);
                    cals.put(endPair, cals.getOrDefault(endPair, 0) - 1);
                    return false;
                }
            }
            return true;
        }
    }

    private void test(IMyCalendar[] objs, int[][] ranges, boolean[] expected) {
        for (IMyCalendar obj : objs) {
            int i = 0;
            for (int[] range : ranges) {
                assertEquals(expected[i++], obj.book(range[0], range[1]));
            }
        }
    }

    @Test
    public void test() {
        IMyCalendar[] objs = new IMyCalendar[] {
            new MyCalendar1(), new MyCalendar2(), new MyCalendar3()
        };
        test(objs, new int[][] {{10, 20}, {15, 25}, {20, 30}},
             new boolean[] {true, false, true});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
