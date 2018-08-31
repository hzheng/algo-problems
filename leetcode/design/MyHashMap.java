import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC706: https://leetcode.com/problems/design-hashmap/
//
// Design a HashMap without using any built-in hash table libraries.
// Note:
// All keys and values will be in the range of [0, 1000000].
// The number of operations will be in the range of [1, 10000].
public class MyHashMap {
    // Hash + List
    // beats 78.66%(92 ms for 33 tests)
    class MyHashMap1 {
        private static final int BUCKETS = 10001;

        @SuppressWarnings("unchecked")
        private List<int[]>[] keyVals = new List[BUCKETS];

        public MyHashMap1() {}

        private int getIndex(int key) {
            // return Objects.hash(key) % BUCKETS;
            return Integer.hashCode(key) % BUCKETS;
        }

        public void put(int key, int value) {
            int index = getIndex(key);
            List<int[]> pairs = keyVals[index];
            if (pairs == null) {
                pairs = keyVals[index] = new ArrayList<>(); // or LinkedList
            } else {
                for (int[] pair : pairs) {
                    if (pair[0] == key) {
                        pair[1] = value;
                        return;
                    }
                }
            }
            pairs.add(new int[] { key, value });
        }

        public int get(int key) {
            int index = getIndex(key);
            List<int[]> pairs = keyVals[index];
            if (pairs != null) {
                for (int[] pair : pairs) {
                    if (pair[0] == key) return pair[1];
                }
            }
            return -1;
        }

        public void remove(int key) {
            int index = getIndex(key);
            List<int[]> pairs = keyVals[index];
            if (pairs == null) return;

            for (Iterator<int[]> itr = pairs.iterator(); itr.hasNext(); ) {
                int[] pair = itr.next();
                if (pair[0] == key) {
                    itr.remove();
                    return;
                }
            }
        }
    }

    // Hash + LinkedList
    // beats 94.16%(83 ms for 33 tests)
    class MyHashMap2 {
        private static final int BUCKETS = 10001;

        class Node {
            int key;
            int val;
            Node next;

            Node(int key, int val) {
                this.key = key;
                this.val = val;
            }

            Node prevNode(int key) {
                Node prev = null;
                for (Node cur = this; cur != null && cur.key != key; ) {
                    prev = cur;
                    cur = cur.next;
                }
                return prev;
            }
        }
        private Node[] buckets = new Node[BUCKETS];

        public MyHashMap2() {}

        private int getIndex(int key) {
            return Integer.hashCode(key) % BUCKETS;
        }

        public void put(int key, int value) {
            int index = getIndex(key);
            if (buckets[index] == null) {
                buckets[index] = new Node(-1, -1);
            }
            Node prev = buckets[index].prevNode(key);
            if (prev.next == null) {
                prev.next = new Node(key, value);
            } else {
                prev.next.val = value;
            }
        }

        public int get(int key) {
            int index = getIndex(key);
            if (buckets[index] == null) return -1;

            Node prev = buckets[index].prevNode(key);
            return (prev.next == null) ? -1 : prev.next.val;
        }

        public void remove(int key) {
            int index = getIndex(key);
            if (buckets[index] == null) return;

            Node prev = buckets[index].prevNode(key);
            if (prev.next != null) {
                prev.next = prev.next.next;
            }
        }
    }

    void test1(String className) throws Exception {
        Object outerObj =
            new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "put", "put", "get", "get", "put", "get",
                            "remove", "get" },
             new Object[][] { new Object[] { outerObj }, new Integer[] { 1, 1 },
                              new Integer[] { 2, 2 },
                              new Integer[] { 1 }, new Integer[] { 3 },
                              new Integer[] { 2, 1 }, new Integer[] { 2 },
                              new Integer[] { 2 }, new Integer[] { 2 } },
             new Integer[] { null, null, null, 1, -1, null, 1, null, -1 });
        test(new String[] { className, "get", "put", "put", "put", "remove",
                            "put", "put", "put", "get", "put", "put",
                            "put", "put", "get", "put", "get", "put", "put",
                            "put", "put", "remove", "put", "put", "put", "put",
                            "put", "put", "put", "get", "put", "put", "put",
                            "put", "put", "put", "put", "put", "put", "put",
                            "put",
                            "put", "put", "remove", "put", "remove", "put",
                            "remove", "put", "remove", "put", "put", "put",
                            "remove", "put", "put", "put", "put", "get", "put",
                            "put", "put", "get", "remove", "put", "put", "put",
                            "put", "remove", "put", "put", "put", "get", "put",
                            "put", "get", "get", "put", "put", "put", "put",
                            "put", "put", "put", "put", "get", "put", "put",
                            "put", "get", "get", "remove", "remove", "put",
                            "get",
                            "get", "put", "get", "put", "put", "get" },
             new Object[][] { new Object[] { outerObj }, new Integer[] { 79 },
                              new Integer[] { 72, 7 },
                              new Integer[] { 77, 1 }, new Integer[] { 10, 21 },
                              new Integer[] { 26 },
                              new Integer[] { 94, 5 }, new Integer[] { 53, 35 },
                              new Integer[] { 34, 9 },
                              new Integer[] { 94 }, new Integer[] { 96, 8 },
                              new Integer[] { 73, 79 },
                              new Integer[] { 7, 60 }, new Integer[] { 84, 79 },
                              new Integer[] { 94 },
                              new Integer[] { 18, 13 }, new Integer[] { 18 },
                              new Integer[] { 69, 34 },
                              new Integer[] { 21, 82 }, new Integer[] { 57, 64 },
                              new Integer[] { 23, 60 },
                              new Integer[] { 0 }, new Integer[] { 12, 97 },
                              new Integer[] { 56, 90 },
                              new Integer[] { 44, 57 }, new Integer[] { 30, 12 },
                              new Integer[] { 17, 10 }, new Integer[] { 42, 13 },
                              new Integer[] { 62, 6 }, new Integer[] { 34 },
                              new Integer[] { 70, 16 }, new Integer[] { 51, 39 },
                              new Integer[] { 22, 98 },
                              new Integer[] { 82, 42 }, new Integer[] { 84, 7 },
                              new Integer[] { 29, 32 },
                              new Integer[] { 96, 54 }, new Integer[] { 57, 36 },
                              new Integer[] { 85, 82 },
                              new Integer[] { 49, 33 }, new Integer[] { 22, 14 },
                              new Integer[] { 63, 8 },
                              new Integer[] { 56, 8 }, new Integer[] { 94 },
                              new Integer[] { 78, 77 }, new Integer[] { 51 },
                              new Integer[] { 20, 89 }, new Integer[] { 51 },
                              new Integer[] { 9, 38 }, new Integer[] { 20 },
                              new Integer[] { 29, 64 }, new Integer[] { 92, 69 },
                              new Integer[] { 72, 25 },
                              new Integer[] { 73 }, new Integer[] { 6, 90 },
                              new Integer[] { 1, 67 },
                              new Integer[] { 70, 83 }, new Integer[] { 58, 49 },
                              new Integer[] { 79 },
                              new Integer[] { 73, 2 }, new Integer[] { 56, 16 },
                              new Integer[] { 58, 26 },
                              new Integer[] { 53 }, new Integer[] { 7 },
                              new Integer[] { 27, 17 }, new Integer[] { 55, 40 },
                              new Integer[] { 55, 13 }, new Integer[] { 89, 32 },
                              new Integer[] { 49 },
                              new Integer[] { 75, 75 }, new Integer[] { 64, 52 },
                              new Integer[] { 94, 74 },
                              new Integer[] { 81 }, new Integer[] { 39, 82 },
                              new Integer[] { 47, 36 }, new Integer[] { 57 },
                              new Integer[] { 66 }, new Integer[] { 3, 7 },
                              new Integer[] { 54, 34 },
                              new Integer[] { 56, 46 }, new Integer[] { 58, 64 },
                              new Integer[] { 22, 81 },
                              new Integer[] { 3, 1 }, new Integer[] { 21, 96 },
                              new Integer[] { 6, 19 }, new Integer[] { 77 },
                              new Integer[] { 60, 66 }, new Integer[] { 48, 85 },
                              new Integer[] { 77, 16 },
                              new Integer[] { 78 }, new Integer[] { 23 },
                              new Integer[] { 72 }, new Integer[] { 27 },
                              new Integer[] { 20, 80 }, new Integer[] { 30 },
                              new Integer[] { 94 }, new Integer[] { 74, 85 },
                              new Integer[] { 49 }, new Integer[] { 79, 59 },
                              new Integer[] { 15, 15 }, new Integer[] { 26 } },
             new Integer[] { null, -1, null, null, null, null, null, null, null,
                             5, null, null, null, null, 5, null,
                             13, null, null, null, null, null, null, null, null,
                             null, null, null, null, 9, null, null, null,
                             null, null, null, null, null, null, null, null,
                             null, null, null, null, null, null, null, null,
                             null, null, null, null, null, null, null, null,
                             null, -1, null, null, null, 35, null, null,
                             null, null, null, null, null, null, null, -1, null,
                             null, 36, -1, null, null, null, null, null,
                             null, null, null, 1, null, null, null, 77, 60,
                             null, null, null, 12, 74, null, -1, null, null,
                             -1 });
    }

    void test(String[] methods, Object[][] args,
              Integer[] expected) throws Exception {
        final String name =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        final Object[] VOID = new Object[] {};
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
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(
                    obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("MyHashMap1");
            test1("MyHashMap2");
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
