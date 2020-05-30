import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1396: https://leetcode.com/problems/design-underground-system/
//
// Implement the class UndergroundSystem that supports three methods:
// 1. checkIn(int id, string stationName, int t)
// A customer with id card equal to id, gets in the station stationName at time t.
// A customer can only be checked into one place at a time.
// 2. checkOut(int id, string stationName, int t)
// A customer with id card equal to id, gets out from the station stationName at time t.
// 3. getAverageTime(string startStation, string endStation)
// Returns the average time to travel between the startStation and the endStation.
// The average time is computed from all the previous traveling from startStation to endStation that
// happened directly.
// Call to getAverageTime is always valid.
// Assume all calls to checkIn and checkOut methods are consistent. That is, if a customer gets in
// at time t1 at some station, then it gets out at time t2 with t2 > t1. All events happen in
// chronological order.
public class UndergroundSystem {
    // Hashtable
    // 77 ms(41.92%), 52.9 MB(100.00%) for 52 tests
    class UndergroundSystem1 {
        Map<String, int[]> travels = new HashMap<>();
        Map<Integer, Object[]> record = new HashMap<>();

        public UndergroundSystem1() {
        }

        public void checkIn(int id, String stationName, int t) {
            record.put(id, new Object[] {stationName, t});
        }

        public void checkOut(int id, String stationName, int t) {
            Object[] info = record.get(id);
            String fromStation = (String)info[0];
            int fromTime = (int)info[1];
            String key = fromStation + "-" + stationName;
            int time = t - fromTime;
            travels.putIfAbsent(key, new int[2]);
            int[] val = travels.get(key);
            val[0] += time;
            val[1]++;
        }

        public double getAverageTime(String startStation, String endStation) {
            String key = startStation + "-" + endStation;
            int[] val = travels.get(key);
            return val[0] / ((double)val[1]);
        }
    }

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {
        }.getClass().getEnclosingClass().newInstance();
        test(new String[] {className, "checkIn", "checkIn", "checkIn", "checkOut", "checkOut",
                           "checkOut", "getAverageTime", "getAverageTime", "checkIn",
                           "getAverageTime", "checkOut", "getAverageTime"},
             new Object[][] {new Object[] {outerObj}, {45, "Leyton", 3}, {32, "Paradise", 8},
                             {27, "Leyton", 10}, {45, "Waterloo", 15}, {27, "Waterloo", 20},
                             {32, "Cambridge", 22}, {"Paradise", "Cambridge"},
                             {"Leyton", "Waterloo"}, {10, "Leyton", 24}, {"Leyton", "Waterloo"},
                             {10, "Waterloo", 38}, {"Leyton", "Waterloo"}},
             new Double[] {null, null, null, null, null, null, null, 14.00000, 11.00000, null,
                           11.00000, null, 12.00000});

        test(new String[] {className, "checkIn", "checkOut", "getAverageTime", "checkIn",
                           "checkOut", "getAverageTime", "checkIn", "checkOut", "getAverageTime"},
             new Object[][] {new Object[] {outerObj}, {10, "Leyton", 3}, {10, "Paradise", 8},
                             {"Leyton", "Paradise"}, {5, "Leyton", 10}, {5, "Paradise", 16},
                             {"Leyton", "Paradise"}, {2, "Leyton", 21}, {2, "Paradise", 30},
                             {"Leyton", "Paradise"}},
             new Double[] {null, null, null, 5.00000, null, null, 5.50000, null, null, 6.66667});
    }

    void test(String[] methods, Object[][] args, Double[] expected) throws Exception {
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
                res = clazz.getMethod(methods[i], String.class, String.class).invoke(obj, arg);
            } else if (arg.length == 3) {
                res = clazz.getMethod(methods[i], int.class, String.class, int.class)
                           .invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], (Double)res, 1E-5);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("UndergroundSystem1");
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
