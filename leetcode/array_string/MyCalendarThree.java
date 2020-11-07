import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC732: https://leetcode.com/problems/my-calendar-iii/
//
// Implement a MyCalendarThree class to store your events. A new event can always be added.
// Your class will have one method, book(int start, int end). Formally, this represents a booking on
// the half open interval [start, end), the range of real numbers x such that start <= x < end.
// A K-booking happens when K events have some non-empty intersection (ie., there is some time that
// is common to all K events.) For each call to the method MyCalendar.book, return an integer K
// representing the largest integer such that there exists a K-booking in the calendar.
//
// Note:
// The number of calls to MyCalendarThree.book per test case will be at most 400.
// In calls to MyCalendarThree.book(start, end), start and end are integers in the range [0, 10^9].
public class MyCalendarThree {
    // SortedMap
    // time complexity: O(N^log(N)), space complexity: O(N)
    // 39 ms(74.12%), 39.4 MB(8.43%) for 98 tests
    static class MyCalendarThree1 {
        private final NavigableMap<Integer, int[]> intervals = new TreeMap<>();
        private int maxEvents;

        public MyCalendarThree1() {
            intervals.put(-1, new int[] {-1, 0});
        }

        public int book(int start, int end) {
            int from = processEnds(start, true);
            int to = processEnds(end, false);

            Map<Integer, int[]> newIntervals = new HashMap<>();
            Set<Integer> subsets = intervals.subMap(from, true, to, true).keySet();
            Iterator<Integer> itr = subsets.iterator();
            int prev = start;
            for (int right; itr.hasNext(); prev = right) { // check all subset intervals
                int left = itr.next();
                int[] val = intervals.get(left);
                right = val[0];
                if (right <= start) { continue; }

                if (left > prev) { // fill blank
                    newIntervals.put(Math.max(start, prev), new int[] {left, 1});
                }
                maxEvents = Math.max(maxEvents, ++val[1]);
            }
            if (end > prev) { // fill blank
                newIntervals.put(Math.max(start, prev), new int[] {end, 1});
            }
            for (int left : newIntervals.keySet()) {
                int[] val = newIntervals.get(left);
                intervals.put(left, val);
                maxEvents = Math.max(maxEvents, val[1]);
            }
            return maxEvents;
        }

        private int processEnds(int end, boolean isStart) {
            int from = intervals.floorKey(isStart ? end : end - 1);
            int[] val = intervals.get(from);
            int right = val[0];
            if (right > end) { // split
                intervals.put(end, new int[] {right, val[1]});
                val[0] = end;
            }
            return from;
        }
    }

    // Solution of Choice
    // SortedMap
    // time complexity: O(N^log(N)), space complexity: O(N)
    // 25 ms(84.15%), 39.6 MB(8.43%) for 98 tests
    static class MyCalendarThree2 {
        private final NavigableMap<Integer, Integer> events = new TreeMap<>();

        private int maxEvents;

        public MyCalendarThree2() {
            events.put(-1, 0);
        }

        public int book(int start, int end) {
            events.put(start, events.floorEntry(start).getValue()); // start event
            events.put(end, events.floorEntry(end).getValue()); // end event
            for (Map.Entry<Integer, Integer> entry : events.subMap(start, end).entrySet()) {
                int val = entry.getValue() + 1;
                entry.setValue(val);
                maxEvents = Math.max(maxEvents, val);
            }
            return maxEvents;
        }
    }

    // Solution of Choice
    // SortedMap
    // time complexity: O(N^log(N)), space complexity: O(N)
    // 99 ms(63.33%), 39.8 MB(8.43%) for 98 tests
    static class MyCalendarThree3 {
        private final NavigableMap<Integer, Integer> events = new TreeMap<>();

        public MyCalendarThree3() {
        }

        public int book(int start, int end) {
            events.put(start, events.getOrDefault(start, 0) + 1); // an event starts
            events.put(end, events.getOrDefault(end, 0) - 1); // an event ends
            int curEvents = 0;
            int res = 0;
            for (int v : events.values()) {
                res = Math.max(res, curEvents += v);
            }
            return res;
        }
    }

    // TODO: Segment Tree

    void test1(String className) throws Exception {
        test(new String[] {className, "book", "book", "book", "book", "book", "book"},
             new Object[][] {{}, {10, 20}, {50, 60}, {10, 40}, {5, 15}, {5, 10}, {25, 55}},
             new Integer[] {null, 1, 1, 2, 3, 3, 3});
        test(new String[] {className, "book", "book", "book", "book", "book", "book", "book",
                           "book", "book", "book", "book", "book", "book", "book", "book", "book",
                           "book", "book", "book", "book", "book", "book", "book", "book", "book",
                           "book", "book", "book", "book", "book"},
             new Object[][] {{}, {47, 50}, {1, 10}, {27, 36}, {40, 47}, {20, 27}, {15, 23},
                             {10, 18}, {27, 36}, {17, 25}, {8, 17}, {24, 33}, {23, 28}, {21, 27},
                             {47, 50}, {14, 21}, {26, 32}, {16, 21}, {2, 7}, {24, 33}, {6, 13},
                             {44, 50}, {33, 39}, {30, 36}, {6, 15}, {21, 27}, {49, 50}, {38, 45},
                             {4, 12}, {46, 50}, {13, 21}},
             new Integer[] {null, 1, 1, 1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6,
                            6, 6, 7, 7, 7, 7, 7, 7});
        test(new String[] {className, "book", "book", "book", "book", "book", "book", "book",
                           "book", "book", "book", "book", "book", "book", "book", "book", "book",
                           "book", "book", "book", "book", "book", "book", "book", "book", "book",
                           "book", "book", "book", "book", "book", "book", "book", "book", "book",
                           "book", "book", "book", "book", "book", "book"},
             new Object[][] {{}, {97, 100}, {51, 65}, {27, 46}, {90, 100}, {20, 32}, {15, 28},
                             {60, 73}, {77, 91}, {67, 85}, {58, 72},

                             {74, 93}, {73, 83}, {71, 87}, {97, 100}, {14, 31}, {26, 37}, {66, 76},
                             {52, 67}, {24, 43}, {6, 23}, {94, 100}, {33, 44}, {30, 46}, {6, 20},
                             {71, 87}, {49, 59}, {38, 55}, {4, 17}, {46, 61}, {13, 31}, {94, 100},
                             {47, 65}, {9, 25}, {4, 20}, {2, 17}, {28, 42}, {26, 38}, {72, 83},
                             {43, 61}, {18, 35}},
             new Integer[] {null, 1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6,
                            6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 8, 9, 9, 9, 9, 9, 10});
    }

    void test(String[] methods, Object[][] args, Integer[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("MyCalendarThree1");
            test1("MyCalendarThree2");
            test1("MyCalendarThree3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
