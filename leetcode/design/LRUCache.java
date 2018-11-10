import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC146: https://leetcode.com/problems/lru-cache/
//
// Design and implement a data structure for Least Recently Used (LRU) cache. It
// should support the following operations: get and set.
// get(key) - Get the value (will always be positive) of the key if the key
// exists in the cache, otherwise return -1.
// set(key, value) - Set or insert the value if the key is not already present.
// When the cache reached its capacity, it should invalidate the least recently
// used item before inserting a new item.

public class LRUCache {
    // Time Limited Exceeded
    class LRUCache0 {
        class TimedVal {
            int serialNum;
            int val;

            TimedVal(int val, int serialNum) {
                this.val = val;
                this.serialNum = serialNum;
            }
        }

        private int capacity;
        private int count;
        private int serialNum;
        Map<Integer, TimedVal> map = new HashMap<>();
        PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> map.get(a).serialNum
                                                                     - map.get(b).serialNum);

        public LRUCache0(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException();

            this.capacity = capacity;
        }

        // time complexity: O(log(N))
        public int get(int key) {
            if (!map.containsKey(key)) return -1;

            return adjust(key).val;
        }

        // time complexity: O(log(N))
        public void put(int key, int value) {
            if (map.containsKey(key)) {
                TimedVal timedVal = adjust(key);
                timedVal.val = value;
                map.put(key, timedVal);
                return;
            }

            if (++count > capacity) {
                count--;
                map.remove(queue.poll());
            }
            map.put(key, new TimedVal(value, ++serialNum));
            queue.add(key);
        }

        private TimedVal adjust(int key) {
            TimedVal timedVal = map.get(key);
            queue.remove(key);
            timedVal.serialNum = ++serialNum;
            queue.add(key);
            return timedVal;
        }
    }

    // Solution of Choice
    // LinkedList + Hashtable
    // beats 59.41%(18 ms for 17 tests)
    // beats 3.49%(158 ms for 18 tests)
    public class LRUCache1 {
        class Node {
            int key;
            int val;
            Node prev;
            Node next;

            Node(int key, int val) {
                this.key = key;
                this.val = val;
            }
        }

        private int capacity;
        Map<Integer, Node> map = new HashMap<>();
        Node head = new Node(-1, 0);
        Node tail = new Node(-1, 0);

        public LRUCache1(int capacity) {
            if (capacity <= 0) throw new IllegalArgumentException();

            this.capacity = capacity;
            head.next = tail;
            tail.prev = head;
        }

        // time complexity: O(1)
        public int get(int key) {
            Node node = map.get(key);
            if (node == null) return -1;

            remove(node);
            putFront(node);
            return node.val;
        }

        // time complexity: O(1)
        public void put(int key, int value) {
            Node node = map.get(key);
            if (node != null) {
                remove(node);
                putFront(node);
                node.val = value;
                return;
            }

            node = new Node(key, value);
            if (map.size() >= capacity) {
                map.remove(tail.prev.key);
                remove(tail.prev);
            }
            putFront(node);
            map.put(key, node);
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void putFront(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
    }

    // LinkedHashMap
    // beats 90.87%(15 ms for 17 tests)
    // beats 3.73%(157 ms for 18 tests)
    class LRUCache2 {
        private LinkedHashMap<Integer, Integer> map;

        public LRUCache2(int capacity) {
            map = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                    return size() > capacity;
                }
            };
        }

        // time complexity: O(1)
        public int get(int key) {
            return map.getOrDefault(key, -1);
        }

        // time complexity: O(1)
        public void put(int key, int value) {
            map.put(key, value);
        }
    }

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] {className, "put", "put", "get", "put", "get", "put", "get", "get",
                           "get"},
             new Object[][] {new Object[] {outerObj, 2}, {1, 1}, {2, 2}, {1}, {3, 3}, {2}, {4, 4},
                             {1}, {3}, {4}},
             new Integer[] {null, null, null, 1, null, -1, null, -1, 3, 4});

        test(new String[] {className, "put", "put", "put", "get", "put", "get", "get"},
             new Object[][] {new Object[] {outerObj, 3}, {1, 1}, {2, 4}, {3, 9}, {2}, {4, 16}, {4},
                             {1}},
             new Integer[] {null, null, null, null, 4, null, 16, -1});
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
            test1("LRUCache0");
            test1("LRUCache1");
            test1("LRUCache2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}