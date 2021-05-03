import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1845: https://leetcode.com/problems/seat-reservation-manager/
//
// Design a system that manages the reservation state of n seats that are numbered from 1 to n.
// Implement the SeatManager class:
// SeatManager(int n) Initializes a SeatManager object that will manage n seats numbered from 1 to n.
// All seats are initially available.
// int reserve() Fetches the smallest-numbered unreserved seat, reserves it, and returns its number.
// void unreserve(int seatNumber) Unreserves the seat with the given seatNumber.
//
// Constraints:
// 1 <= n <= 10^5
// 1 <= seatNumber <= n
// For each call to reserve, it is guaranteed that there will be at least one unreserved seat.
// For each call to unreserve, it is guaranteed that seatNumber will be reserved.
// At most 105 calls in total will be made to reserve and unreserve.
public class SeatManager {
    // SortedSet
    // 117 ms(%), 84.8 MB(%) for 71 tests
    static class SeatManager1 {
        private TreeSet<Integer> unreserved = new TreeSet<>();

        public SeatManager1(int n) {
            for (int i = 1; i <= n; i++) {
                unreserved.add(i);
            }
        }

        public int reserve() {
            return unreserved.pollFirst();
        }

        public void unreserve(int seatNumber) {
            unreserved.add(seatNumber);
        }
    }

    // Solution of Choice
    // Heap
    // 34 ms(%), 63.4 MB(%) for 71 tests
    static class SeatManager2 {
        private PriorityQueue<Integer> unreserved = new PriorityQueue<>();
        private int seq;

        public SeatManager2(int n) {
        }

        public int reserve() {
            return unreserved.isEmpty() ? ++seq : unreserved.poll();
        }

        public void unreserve(int seatNumber) {
            unreserved.offer(seatNumber);
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "reserve", "reserve", "unreserve", "reserve", "reserve",
                           "reserve", "reserve", "unreserve"},
             new Object[][] {{5}, {}, {}, {2}, {}, {}, {}, {}, {5}},
             new Object[] {null, 1, 2, null, 2, 3, 4, 5, null});
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
            test1("SeatManager1");
            test1("SeatManager2");
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
