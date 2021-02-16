import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1146: https://leetcode.com/problems/snapshot-array/
//
// Implement a SnapshotArray that supports the following interface:
// SnapshotArray(int length) initializes an array-like data structure with the given length.
// Initially, each element equals 0.
// void set(index, val) sets the element at the given index to be equal to val.
// int snap() takes a snapshot of the array and returns the snap_id: the total number of times we
// called snap() minus 1.
// int get(index, snap_id) returns the value at the given index, at the time we took the snapshot
// with the given snap_id
//
// Constraints:
// 1 <= length <= 50000
// At most 50000 calls will be made to set, snap, and get.
// 0 <= index < length
// 0 <= snap_id < (the total number of times we call snap())
// 0 <= val <= 10^9
public class SnapshotArray {
    // Memory Limit Exceeded
    static class SnapshotArray1 {
        private int snapId;
        private boolean modified;
        private final Map<Integer, int[]> data = new HashMap<>();

        public SnapshotArray1(int length) {
            data.put(0, new int[length]);
        }

        public void set(int index, int val) {
            int[] array = data.get(snapId);
            if (!modified) {
                modified = true;
                array = array.clone();
                data.put(snapId, array);
            }
            array[index] = val;
        }

        public int snap() {
            modified = false;
            data.put(snapId + 1, data.get(snapId));
            return snapId++;
        }

        public int get(int index, int snap_id) {
            return data.get(snap_id)[index];
        }
    }

    // SortedMap
    // 39 ms(60.23%), 88.9 MB(18.41%) for 71 tests
    static class SnapshotArray2 {
        private int snapId;
        private boolean modified;
        private final TreeMap<Integer, Integer>[] data;

        public SnapshotArray2(int length) {
            data = new TreeMap[length];
            for (int i = 0; i < length; i++) {
                data[i] = new TreeMap<>();
                data[i].put(0, 0);
            }
        }

        public void set(int index, int val) {
            data[index].put(snapId, val);
        }

        public int snap() {
            return snapId++;
        }

        public int get(int index, int snap_id) {
            return data[index].floorEntry(snap_id).getValue();
        }
    }

    // Binary Search
    // 36 ms(76.15%), 75.5 MB(61.65%) for 71 tests
    static class SnapshotArray3 {
        private List<int[]>[] data;
        private int snapId;

        public SnapshotArray3(int length) {
            data = new List[length];
            for (int i = 0; i < length; i++) {
                data[i] = new ArrayList<>();
                data[i].add(new int[2]);
            }
        }

        public void set(int index, int val) {
            int[] last = data[index].get(data[index].size() - 1);
            if (last[0] == snapId) {
                last[1] = val;
            } else {
                data[index].add(new int[] {snapId, val});
            }
        }

        public int snap() {
            return snapId++;
        }

        public int get(int index, int snap_id) {
            int idx = Collections.binarySearch(data[index], new int[] {snap_id, 0},
                                               Comparator.comparingInt(a -> a[0]));
            return data[index].get(idx < 0 ? -idx - 2 : idx)[1];
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "snap", "get", "get", "set", "get", "set", "get", "set"},
             new Object[][] {{2}, {}, {1, 0}, {0, 0}, {1, 8}, {1, 0}, {0, 20}, {0, 0}, {0, 7}},
             new Object[] {null, 0, 0, 0, null, 0, null, 0, null});
        test(new String[] {className, "set", "snap", "set", "get"},
             new Object[][] {{3}, {0, 5}, {}, {0, 6}, {0, 0}},
             new Object[] {null, null, 0, null, 5});
        test(new String[] {className, "snap", "snap", "get", "set", "snap", "set"},
             new Object[][] {{4}, {}, {}, {3, 1}, {2, 4}, {}, {1, 4}},
             new Object[] {null, 0, 1, 0, null, 2, null});
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
            test1("SnapshotArray1");
            test1("SnapshotArray2");
            test1("SnapshotArray3");
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
