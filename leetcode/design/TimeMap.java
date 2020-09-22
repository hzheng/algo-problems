import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC981: https://leetcode.com/problems/time-based-key-value-store/
//
// Create a timebased key-value store class TimeMap, that supports 2 operations.
// 1. set(string key, string value, int timestamp)
// Stores the key and value, along with the given timestamp.
// 2. get(string key, int timestamp)
// Returns a value such that set(key, value, timestamp_prev) was called 
// previously, with timestamp_prev <= timestamp.
// If there are multiple such values, it returns the one with the largest 
// timestamp_prev. If there are no values, it returns the empty string ("").
public class TimeMap {
    // SortedMap
    // 276 ms(73.16%), 142.2 MB(100.00%) for 45 tests
    class TimeMap1 {
        private Map<String, TreeMap<Integer, String>> map = new HashMap<>();

        public TimeMap1() {
        }

        public void set(String key, String value, int timestamp) {
            TreeMap<Integer, String> v = map.get(key);
            if (v == null) {
                v = new TreeMap<>();
                map.put(key, v);
            }
            v.put(timestamp, value);
        }

        public String get(String key, int timestamp) {
            TreeMap<Integer, String> v = map.get(key);
            if (v == null) return "";

            Integer k = v.floorKey(timestamp);
            return k == null ? "" : v.get(k);
        }
    }

    private static class Pair<K, V> {
        K key;
        V value;
        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        K getKey() {
            return key;
        }

        V getValue() {
            return value;
        }
    }

    // Hash Table + Binary Search
    // 271 ms(77.74%), 137.3 MB(100.00%) for 45 tests
    class TimeMap2 {
        private Map<String, List<Pair<Integer, String>>> map = new HashMap<>();

        public TimeMap2() {
        }

        public void set(String key, String value, int timestamp) {
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(new Pair<>(timestamp, value));
        }

        public String get(String key, int timestamp) {
            if (!map.containsKey(key)) return "";

            List<Pair<Integer, String>> A = map.get(key);
            int i = Collections.binarySearch(A, new Pair<>(timestamp, "{"),
                                             (a, b) -> Integer.compare(a.getKey(), b.getKey()));

            if (i >= 0) return A.get(i).getValue();
            return (i == -1) ? "" : A.get(-i - 2).getValue();
        }
    }

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {
        }.getClass().getEnclosingClass().newInstance();
        test(new String[] {className, "set", "get", "get", "set", "get", "get"},
             new Object[][] {new Object[] {outerObj}, {"foo", "bar", 1}, {"foo", 1}, {"foo", 3},
                             {"foo", "bar2", 4}, {"foo", 4}, {"foo", 5}},
             new String[] {null, null, "bar", "bar", null, "bar2", "bar2"});

        test(new String[] {className, "set", "set", "get", "get", "get", "get", "get"},
             new Object[][] {new Object[] {outerObj}, {"love", "high", 10}, {"love", "low", 20},
                             {"love", 5}, {"love", 10}, {"love", 15}, {"love", 20}, {"love", 25}},
             new String[] {null, null, null, "", "high", "high", "low", "low"});
    }

    void test(String[] methods, Object[][] args, String[] expected) throws Exception {
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
                res = clazz.getMethod(methods[i], String.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], String.class, int.class).invoke(obj, arg);
            } else if (arg.length == 3) {
                res = clazz.getMethod(methods[i], String.class, String.class, int.class)
                           .invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("TimeMap1");
            test1("TimeMap2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
