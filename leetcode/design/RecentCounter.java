import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC933: https://leetcode.com/problems/number-of-recent-calls/
//
// Write a class RecentCounter to count recent requests.
// It has one method: ping(int t), where t represents some time in milliseconds.
// Return the number of pings that have been made from 3000 milliseconds ago 
// until now. Any ping with time in [t - 3000, t] will count, including the 
// current ping. It is guaranteed that every call to ping uses a strictly larger
// value of t than before.
public class RecentCounter {
    // Queue
    // time complexity: O(N), space complexity: O(W)
    // beats %(98 ms for 68 tests)
    class RecentCounter1 {
        private Queue<Integer> queue = new LinkedList<>();

        public RecentCounter1() {
        }

        public int ping(int t) {
            queue.offer(t);
            for (; queue.peek() < t - 3000; queue.poll()) {}
            return queue.size();
        }
    }

    // SortedSet
    // time complexity: O(log(N)), space complexity: O(N)
    // beats %(684 ms for 68 tests)
    class RecentCounter2 {
        NavigableSet<Integer> pings = new TreeSet<>();

        public RecentCounter2() {
        }

        public int ping(int t) {
            pings.add(t);
            return pings.tailSet(t - 3000).size();
        }
    }

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] {className, "ping", "ping", "ping", "ping"},
             new Object[][] {new Object[] {outerObj}, {1}, {100}, {3001}, {3002}},
             new Integer[] {null, 1, 2, 3, 3});
    }

    void test(String[] methods, Object[][] args, Integer[] expected) throws Exception {
        final String name = new Object() {}.getClass().getEnclosingClass().getSimpleName();
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
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("RecentCounter1");
            test1("RecentCounter2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
