import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1656: https://leetcode.com/problems/design-an-ordered-stream/
//
// There are n (id, value) pairs, where id is an integer between 1 and n and value is a string. No
// two pairs have the same id.
// Design a stream that takes the n pairs in an arbitrary order, and returns the values over several
// calls in increasing order of their ids.
// Implement the OrderedStream class:
// OrderedStream(int n) Constructs the stream to take n values and sets a current ptr to 1.
// String[] insert(int id, String value) Stores the new (id, value) pair in the stream. After
// storing the pair: If the stream has stored a pair with id = ptr, then find the longest contiguous
// incrementing sequence of ids starting with id = ptr and return a list of the values associated
// with those ids in order. Then, update ptr to the last id + 1.
// Otherwise, return an empty list.
public class OrderedStream {
    // Hash Table
    // 70 ms(50.00%), 40.9 MB(50.00%) for 101 tests
    static class OrderedStream1 {
        private final Map<Integer, String> map = new HashMap<>();
        private int ptr = 1;

        public OrderedStream1(int n) {
        }

        public List<String> insert(int id, String value) {
            map.put(id, value);
            List<String> res = new ArrayList<>();
            for (String v; ; res.add(v), ptr++) {
                v = map.get(ptr);
                if (v == null) { return res; }
            }
        }
    }

    // 64 ms(100.00%), 40.6 MB(50.00%) for 101 tests
    static class OrderedStream2 {
        private final String[] values;
        private int ptr;

        public OrderedStream2(int n) {
            values = new String[n];
        }

        public List<String> insert(int id, String value) {
            values[id - 1] = value;
            List<String> res = new ArrayList<>();
            for (; ptr < values.length && values[ptr] != null; ptr++) {
                res.add(values[ptr]);
            }
            return res;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "insert", "insert", "insert", "insert", "insert"},
             new Object[][] {{5}, {3, "ccccc"}, {1, "aaaaa"}, {2, "bbbbb"}, {5, "eeeee"},
                             {4, "ddddd"}},
             new String[][] {null, {}, {"aaaaa"}, {"bbbbb", "ccccc"}, {}, {"ddddd", "eeeee"}});
    }

    void test(String[] methods, Object[][] args, String[][] expected) throws Exception {
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
                res = clazz.getMethod(methods[i], String.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, String.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(Arrays.asList(expected[i]), res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("OrderedStream1");
            test1("OrderedStream2");
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
