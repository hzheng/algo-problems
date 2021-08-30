import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC715: https://leetcode.com/problems/range-module/
//
// A Range Module is a module that tracks ranges of numbers. Your task is to design and implement
// the following interfaces in an efficient manner.
// addRange(int left, int right) Adds the half-open interval [left, right), tracking every real
// number in that interval. Adding an interval that partially overlaps with currently tracked
// numbers should add any numbers in the interval [left, right) that are not already tracked.
// queryRange(int left, int right) Returns true if and only if every real number in the interval
// [left, right) is currently being tracked.
// removeRange(int left, int right) Stops tracking every real number currently being tracked in the
// interval [left, right).
public class RangeModule {
    // SortedMap
    // 37 ms(93.06%), 48.3 MB(100.00%) for 53 tests
    static class RangeModule1 {
        private NavigableMap<Integer, Integer> map = new TreeMap<>();

        public RangeModule1() {
        }

        public void addRange(int left, int right) {
            int newRight = right;
            int newLeft = left;
            for (Integer cur = map.floorKey(right), next; cur != null; cur = next) {
                int prevRight = map.get(cur);
                if (prevRight < left) { break; }

                next = map.lowerKey(cur);
                map.remove(cur);
                newRight = Math.max(newRight, prevRight);
                newLeft = Math.min(newLeft, cur);
            }
            map.put(newLeft, newRight);
        }

        public void addRange2(int left, int right) {
            Integer prevLeft = map.floorKey(left);
            Integer prevRight = (prevLeft == null) ? null : map.get(prevLeft);
            int newLeft = left;
            int newRight = right;
            if (prevRight != null && prevRight >= left) {
                newLeft = prevLeft;
                newRight = Math.max(right, prevRight);
            }
            map.put(newLeft, newRight);
            for (Integer cur = map.higherKey(newLeft), next;
                 cur != null && cur <= newRight; cur = next) {
                next = map.higherKey(cur);
                newRight = Math.max(newRight, map.remove(cur));
                map.put(newLeft, newRight);
            }
        }

        public boolean queryRange(int left, int right) {
            Integer prevLeft = map.floorKey(left);
            return prevLeft != null && map.get(prevLeft) >= right;
        }

        public void removeRange(int left, int right) {
            for (Integer cur = map.floorKey(right), next; cur != null; cur = next) {
                int prevRight = map.get(cur);
                if (prevRight <= left) { break; }

                next = map.lowerKey(cur);
                if (cur >= left) {
                    map.remove(cur);
                } else {
                    map.put(cur, left);
                }
                if (prevRight > right) {
                    map.put(right, prevRight);
                }
            }
        }
    }

    // SortedMap
    // 87 ms(16.68%), 83.6 MB(9.68%) for 53 tests
    static class RangeModule2 {
        private final NavigableMap<Integer, Integer> intervals = new TreeMap<>();

        public RangeModule2() {
        }

        public void addRange(int left, int right) {
            Integer start = intervals.floorKey(left);
            Integer end = intervals.floorKey(right);
            if (start != null && intervals.get(start) >= left) {
                left = start;
            }
            if (end != null && intervals.get(end) > right) {
                right = intervals.get(end);
            }
            intervals.put(left, right);
            intervals.subMap(left, false, right, true).clear();
        }

        public boolean queryRange(int left, int right) {
            Integer prevLeft = intervals.floorKey(left);
            return prevLeft != null && intervals.get(prevLeft) >= right;
        }

        public void removeRange(int left, int right) {
            Integer start = intervals.floorKey(left);
            Integer end = intervals.floorKey(right);
            if (end != null && intervals.get(end) > right) {
                intervals.put(right, intervals.get(end));
            }
            if (start != null && intervals.get(start) > left) {
                intervals.put(start, left);
            }
            intervals.subMap(left, true, right, false).clear();
        }
    }

    // TODO: Segment Tree

    void test1(String className) throws Exception {
        test(new String[] {className, "removeRange", "addRange", "queryRange", "addRange",
                           "removeRange", "queryRange", "queryRange", "addRange", "removeRange"},
             new Object[][] {new Object[] {}, {4, 8}, {1, 10}, {1, 7}, {2, 3}, {2, 3}, {8, 9},
                             {6, 9}, {2, 3}, {1, 8}},
             new Boolean[] {null, null, null, true, null, null, true, true, null, null});

        test(new String[] {className, "addRange", "queryRange", "removeRange", "removeRange",
                           "addRange", "queryRange", "addRange", "queryRange", "removeRange"},
             new Object[][] {new Object[] {}, {5, 8}, {3, 4}, {5, 6}, {3, 6}, {1, 3}, {2, 3},
                             {4, 8}, {2, 3}, {4, 9}},
             new Boolean[] {null, null, false, null, null, null, true, null, true, null});

        test(new String[] {className, "addRange", "removeRange", "queryRange", "queryRange",
                           "queryRange"},
             new Object[][] {new Object[] {}, {10, 20}, {14, 16}, {10, 14}, {13, 15}, {16, 17}},
             new Boolean[] {null, null, null, true, false, true});

        test(new String[] {className, "addRange", "addRange", "addRange", "queryRange",
                           "queryRange", "queryRange", "removeRange", "queryRange"},
             new Object[][] {new Object[] {}, {10, 180}, {150, 200}, {250, 500}, {50, 100},
                             {180, 300}, {600, 1000}, {50, 150}, {50, 100}},
             new Boolean[] {null, null, null, null, true, false, false, null, false});
    }

    void test(String[] methods, Object[][] args, Boolean[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        System.out.println(clazz);
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
            test1("RangeModule1");
            test1("RangeModule2");
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
