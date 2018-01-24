import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC731: https://leetcode.com/problems/my-calendar-ii/
//
// Implement a class to store your events. A new event can be added if adding
// the event will not cause a triple booking.
// A triple booking happens when three events have some non-empty intersection.
public class MyCalendarTwo {
    static interface IMyCalendarTwo {
        public boolean book(int start, int end);
    }

    // Brute Force
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 65.32%(273 ms for 98 tests)
    class MyCalendarTwo1 implements IMyCalendarTwo {
        private List<int[]> booked = new ArrayList<>();
        private List<int[]> overlaps = new ArrayList<>();

        public boolean book(int start, int end) {
            for (int[] bound : overlaps) {
                if (bound[0] < end && start < bound[1]) return false;
            }
            for (int[] bound : booked) {
                if (bound[0] < end && start < bound[1]) {
                    overlaps.add(new int[] {Math.max(start, bound[0]),
                                            Math.min(end, bound[1])});
                }
            }
            booked.add(new int[] {start, end});
            return true;
        }
    }

    // Boundary Count + Sort
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 18.30%(440 ms for 98 tests)
    class MyCalendarTwo2 implements IMyCalendarTwo {
        private List<int[]> booked = new ArrayList<>();

        public boolean book(int start, int end) {
            Collections.sort(booked, (x, y) -> {
                return (x[0] != y[0]) ? x[0] - y[0] : x[1] - y[1];
            });
            int overlapped = 0;
            int prevBound = -1;
            for (int[] bound : booked) {
                if (overlapped >= 2 &&
                    Math.max(prevBound,
                             start) < Math.min(bound[0], end)) return false;

                overlapped += bound[1];
                prevBound = bound[0];
            }
            booked.add(new int[] {start, 1});
            booked.add(new int[] {end, -1});
            return true;
        }
    }

    // Boundary Count + SortedMap
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 10.10%(542 ms for 98 tests)
    class MyCalendarTwo3 implements IMyCalendarTwo {
        private SortedMap<Integer, Integer> delta = new TreeMap<>();

        public boolean book(int start, int end) {
            delta.put(start, delta.getOrDefault(start, 0) + 1);
            delta.put(end, delta.getOrDefault(end, 0) - 1);
            int overlapped = 0;
            for (int d : delta.values()) {
                overlapped += d;
                if (overlapped >= 3) {
                    delta.put(start, delta.get(start) - 1);
                    delta.put(end, delta.get(end) + 1);
                    if (delta.get(start) == 0) {
                        delta.remove(start);
                    }
                    return false;
                }
            }
            return true;
        }
    }

    private void test(IMyCalendarTwo obj, int[][] ranges,
                      boolean[] expected) {
        int i = 0;
        for (int[] range : ranges) {
            assertEquals(expected[i++], obj.book(range[0], range[1]));
        }
    }

    private void test1(IMyCalendarTwo obj) {
        test(obj, new int[][] {{10, 20}, {50, 60}, {10, 40}, {5, 15},
                               {5, 10}, {25, 55}},
             new boolean[] {true, true, true, false, true, true});
    }

    private void test2(IMyCalendarTwo obj) {
        test(obj, new int[][]
             {{51, 58}, {77, 85}, {35, 44}, {53, 61}, {86, 93}, {55, 61},
              {43, 50}, {64, 69}, {76, 82}, {98, 100}, {35, 40}, {25, 32},
              {8, 17}, {37, 43}, {53, 60}, {86, 91}, {97, 100}, {37, 43},
              {41, 50}, {83, 92}, {66, 75}, {42, 48}, {55, 64}, {37, 46},
              {92, 97}, {69, 76}, {85, 94}, {60, 66}, {27, 34}, {36, 44},
              {32, 38}, {56, 62}, {93, 99}, {11, 18}, {21, 30}, {81, 89},
              {18, 26}, {81, 90}, {91, 96}, {43, 49}, {3, 12}, {97, 100},
              {72, 80}, {15, 23}, {63, 70}, {8, 16}, {1, 6}, {16, 24}, {45,
                                                                        54},
              {3, 9}, {30, 36}, {29, 35}, {41, 48}, {21, 26}, {79, 87},
              {27, 32}, {88, 96}, {47, 55}, {71, 76}, {32, 40}, {68, 74},
              {51, 59}, {44, 50}, {65, 71}, {83, 90}, {86, 94}, {48, 57},
              {26, 32}, {27, 32}, {78, 83}, {27, 35}, {19, 24}, {26, 31},
              {67, 75}, {87, 92}, {6, 15}, {37, 44}, {62, 68}, {13, 18},
              {41, 46}},
             new boolean[] {true, true, true, true, true, false, true, true,
                            true, true, true, true, true, false, false,
                            true,
                            true, false, false, false, true, false, false,
                            false, true, true, false, true, true, false,
                            false,
                            false, false, true, false, false, true, false,
                            false, false, false, false, false, false, false,
                            false, true, false, false, true, false, false,
                            false, false, false, false, false, false, false,
                            false, false, false, true, false, false, false,
                            false, false, false, false, false, true, false,
                            false, false, false, false, false, false,
                            false});
    }

    @Test
    public void test1() {
        test1(new MyCalendarTwo1());
        test1(new MyCalendarTwo2());
        test1(new MyCalendarTwo3());
    }

    @Test
    public void test2() {
        test2(new MyCalendarTwo1());
        test2(new MyCalendarTwo2());
        test2(new MyCalendarTwo3());
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
