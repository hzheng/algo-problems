import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC705: https://leetcode.com/problems/design-hashset/
//
// Design a HashSet without using any built-in hash table libraries.
// Note:
// All values will be in the range of [0, 1000000].
// The number of operations will be in the range of [1, 10000].
public class MyHashSet {
    // Bitmap
    // beats 12.10%(139 ms for 28 tests)
    class MyHashSet1 {
        private int[] data = new int[1000000 >> 5];

        public MyHashSet1() {}

        public void add(int key) {
            data[key >> 5] |= 1 << (key & 31);
        }
        
        public void remove(int key) {
            data[key >> 5] &= ~(1 << (key & 31));
        }
        
        public boolean contains(int key) {
            return (data[key >> 5] & (1 << (key & 31))) != 0;
        }
    }

    // Hash
    // beats 31.630%(105 ms for 28 tests)
    class MyHashSet2 {
        private static final int BUCKETS = 10001;
        @SuppressWarnings("unchecked")
        private List<Integer>[] lists = new List[BUCKETS];

        public MyHashSet2() {}

        public void add(int key) {
            int index = Objects.hash(key) % BUCKETS;
            List<Integer> list = lists[index];
            if (list == null) {
                list = lists[index] = new ArrayList<>();
                list.add(key);
            } else if (!list.contains(key)) {
                list.add(key);
            }
        }
        
        public void remove(int key) {
            int index = Objects.hash(key) % BUCKETS;
            List<Integer> list = lists[index];
            if (list != null) {
                list.remove((Object)key);
            }
        }
        
        public boolean contains(int key) {
            int index = Objects.hash(key) % BUCKETS;
            List<Integer> list = lists[index];
            return (list != null) && list.contains(key);
        }
    }

    void test1(String className) throws Exception {
        Object outerObj = new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "add", "remove", "add", "contains", 
                            "add", "remove", "add", "add", "add", "add"},
             new Object[][] { new Object[] { outerObj },
                              new Integer[] { 6 }, new Integer[] { 4 }, 
                              new Integer[] { 17 }, new Integer[] { 14 }, 
                              new Integer[] { 14 }, new Integer[] { 17 }, 
                              new Integer[] { 14 }, new Integer[] { 14 }, 
                              new Integer[] { 18 }, new Integer[] { 14 } }, 
                new Boolean[] { null, null, null, null, false, null, null, null,
                                null, null, null });
        test(new String[] { className, "add", "add", "contains", "contains", 
                            "add", "contains", "remove", "contains"},
             new Object[][] { new Object[] { outerObj },
                              new Integer[] { 1 }, new Integer[] { 2 }, 
                              new Integer[] { 1 }, new Integer[] { 3 }, 
                              new Integer[] { 2 }, new Integer[] { 2 }, 
                              new Integer[] { 2 }, new Integer[] { 2 } }, 
                new Boolean[] { null, null, null, true, false, null,
                                true, null, false });
    }

    void test(String[] methods, Object[][] args, Boolean[] expected) 
        throws Exception {
        final String name = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        final Object[] VOID = new Object[]{};
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("MyHashSet1");
            test1("MyHashSet2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
