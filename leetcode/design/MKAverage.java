import java.util.*;

import java.lang.reflect.Constructor;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1825: https://leetcode.com/problems/finding-mk-average/
//
// You are given two integers, m and k, and a stream of integers. You are tasked to implement a data
// structure that calculates the MKAverage for the stream.
//
// The MKAverage can be calculated using these steps:
// If the number of the elements in the stream is less than m you should consider the MKAverage to
// be -1. Otherwise, copy the last m elements of the stream to a separate container.
// Remove the smallest k elements and the largest k elements from the container.
// Calculate the average value for the rest of the elements rounded down to the nearest integer.
// Implement the MKAverage class:
// MKAverage(int m, int k) Initializes the MKAverage object with an empty stream and the two
// integers m and k.
// void addElement(int num) Inserts a new element num into the stream.
// int calculateMKAverage() Calculates and returns the MKAverage for the current stream rounded down
// to the nearest integer.
//
// Constraints:
// 3 <= m <= 10^5
// 1 <= k*2 < m
// 1 <= num <= 10^5
// At most 10^5 calls will be made to addElement and calculateMKAverage.
public class MKAverage {
    // 53 ms(98.73%), 99.2 MB(65.61%) for 17 tests
    static class MKAverage1 {
        private int min;
        private int k;
        private int sum;
        int topCount;
        int bottomCount;
        private Queue<Integer> queue = new LinkedList<>();
        private TreeMap<Integer, Integer> top = new TreeMap<>();
        private TreeMap<Integer, Integer> middle = new TreeMap<>();
        private TreeMap<Integer, Integer> bottom = new TreeMap<>();

        public MKAverage1(int m, int k) {
            min = m;
            this.k = k;
        }

        private void removeOldest() {
            if (queue.size() < min) { return; }

            int cur = queue.poll();
            if (remove(top, cur) != null) {
                topCount--;
            } else if (remove(bottom, cur) != null) {
                bottomCount--;
            } else {
                remove(middle, cur);
                sum -= cur;
            }
        }

        public void addElement(int num) {
            removeOldest();
            add(middle, num);
            queue.offer(num);
            sum += num;
            for (; topCount < k && !middle.isEmpty(); topCount++) {
                int last = middle.lastKey();
                sum -= last;
                add(top, remove(middle, last));
            }
            adjust(top, middle);
            for (; bottomCount < k && !middle.isEmpty(); bottomCount++) {
                int first = middle.firstKey();
                sum -= first;
                add(bottom, remove(middle, first));
            }
            adjust(middle, bottom);
        }

        private void adjust(TreeMap<Integer, Integer> upper, TreeMap<Integer, Integer> lower) {
            if (!upper.isEmpty() && !lower.isEmpty()) {
                int first = upper.firstKey();
                int last = lower.lastKey();
                if (last > first) {
                    sum += (last - first) * (upper == middle ? 1 : -1);
                    add(upper, remove(lower, last));
                    add(lower, remove(upper, first));
                }
            }
        }

        public int calculateMKAverage() {
            return queue.size() == min ? (sum / (min - 2 * k)) : -1;
        }

        private Integer remove(Map<Integer, Integer> map, int num) {
            Integer old = map.get(num);
            if (old == null) { return null; }

            map.put(num, map.get(num) - 1);
            if (map.get(num) == 0) {
                map.remove(num);
            }
            return num;
        }

        private void add(Map<Integer, Integer> map, int num) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
    }

    // TODO: Binary Indexed Tree
    // TODO: Binary Search Tree

    void test1(String className) throws Exception {
        test(new String[] {className, "addElement", "addElement", "calculateMKAverage",
                           "addElement", "addElement", "calculateMKAverage", "addElement",
                           "addElement", "calculateMKAverage", "addElement"},
             new Object[][] {{3, 1}, {58916}, {61899}, {}, {85406}, {49757}, {}, {27520}, {12303},
                             {}, {63945}},
             new Object[] {null, null, null, -1, null, null, 61899, null, null, 27520, null});
        test(new String[] {className, "addElement", "addElement", "calculateMKAverage",
                           "addElement", "calculateMKAverage", "addElement", "addElement",
                           "addElement", "calculateMKAverage"},
             new Object[][] {{3, 1}, {3}, {1}, {}, {10}, {}, {5}, {5}, {5}, {}},
             new Object[] {null, null, null, -1, null, 3, null, null, null, 5});
    }

    @Test public void test2() {
        test2("MKAverage1");
    }

    void test2(String className) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                String[] methods = Utils.readStrArray(scanner.nextLine());
                methods[0] = className;
                Object[][] args = Utils.readObj2Array(scanner.nextLine());
                Object[] expected = Utils.readObjArray(scanner.nextLine());
                test(methods, args, expected);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
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
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("MKAverage1");
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
