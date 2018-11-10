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
    // beats 93.82%(81 ms for 18 tests)
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
        private Map<Integer, Node> map = new HashMap<>();
        private Node head = new Node(-1, 0);
        private Node tail = new Node(-1, 0);

        public LRUCache1(int capacity) {
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
            node.prev = node.next = null; // allow garbage collect
        }

        private void putFront(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
    }

    // beats 93.82%(81 ms for 18 tests)
    class LRUCache1_2 {
        class Node {
            int val;
            Node prev;
            Node next;

            Node(int val) {
                this.val = val;
            }
        }

        private int capacity;
        private int count;
        private Map<Integer, Node> map = new HashMap<>();
        private Map<Node, Integer> revMap = new HashMap<>();
        private Node head = new Node(0);
        private Node tail = new Node(0);

        public LRUCache1_2(int capacity) {
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
                node.val = value;
                remove(node);
            } else {
                if (++count > capacity) {
                    count--;
                    map.remove(revMap.remove(tail.prev));
                    remove(tail.prev);
                }
                node = new Node(value);
                map.put(key, node);
                revMap.put(node, key);
            }
            putFront(node);
        }

        private void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            node.prev = node.next = null; // allow garbage collect
        }

        private void putFront(Node node) {
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
            node.prev = head;
        }
    }

    // Solution of Choice
    // LinkedHashMap
    // beats 96.53%(79 ms for 18 tests)
    class LRUCache2 {
        private LinkedHashMap<Integer, Integer> map;

        @SuppressWarnings("serial")
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

        test(new String[] {className, "put", "put", "put", "put", "put", "get", "put", "get", "get",
                           "put", "get", "put", "put", "put", "get", "put", "get", "get", "get",
                           "get", "put", "put", "get", "get", "get", "put", "put", "get", "put",
                           "get", "put", "get", "get", "get", "put", "put", "put", "get", "put",
                           "get", "get", "put", "put", "get", "put", "put", "put", "put", "get",
                           "put", "put", "get", "put", "put", "get", "put", "put", "put", "put",
                           "put", "get", "put", "put", "get", "put", "get", "get", "get", "put",
                           "get", "get", "put", "put", "put", "put", "get", "put", "put", "put",
                           "put", "get", "get", "get", "put", "put", "put", "get", "put", "put",
                           "put", "get", "put", "put", "put", "get", "get", "get", "put", "put",
                           "put", "put", "get", "put", "put", "put", "put", "put", "put", "put"},
             new Object[][] {new Object[] {outerObj, 10}, {10, 13}, {3, 17}, {6, 11}, {10, 5},
                             {9, 10}, {13}, {2, 19}, {2}, {3}, {5, 25}, {8}, {9, 22}, {5, 5},
                             {1, 30}, {11}, {9, 12}, {7}, {5}, {8}, {9}, {4, 30}, {9, 3}, {9}, {10},
                             {10}, {6, 14}, {3, 1}, {3}, {10, 11}, {8}, {2, 14}, {1}, {5}, {4},
                             {11, 4}, {12, 24}, {5, 18}, {13}, {7, 23}, {8}, {12}, {3, 27}, {2, 12},
                             {5}, {2, 9}, {13, 4}, {8, 18}, {1, 7}, {6}, {9, 29}, {8, 21}, {5},
                             {6, 30}, {1, 12}, {10}, {4, 15}, {7, 22}, {11, 26}, {8, 17}, {9, 29},
                             {5}, {3, 4}, {11, 30}, {12}, {4, 29}, {3}, {9}, {6}, {3, 4}, {1}, {10},
                             {3, 29}, {10, 28}, {1, 20}, {11, 13}, {3}, {3, 12}, {3, 8}, {10, 9},
                             {3, 26}, {8}, {7}, {5}, {13, 17}, {2, 27}, {11, 15}, {12}, {9, 19},
                             {2, 15}, {3, 16}, {1}, {12, 17}, {9, 1}, {6, 19}, {4}, {5}, {5},
                             {8, 1}, {11, 7}, {5, 2}, {9, 28}, {1}, {2, 2}, {7, 4}, {4, 22},
                             {7, 24}, {9, 26}, {13, 28}, {11, 26}},

             new Integer[] {null, null, null, null, null, null, -1, null, 19, 17, null, -1, null,
                            null, null, -1, null, -1, 5, -1, 12, null, null, 3, 5, 5, null, null, 1,
                            null, -1, null, 30, 5, 30, null, null, null, -1, null, -1, 24, null,
                            null, 18, null, null, null, null, -1, null, null, 18, null, null, -1,
                            null, null, null, null, null, 18, null, null, -1, null, 4, 29, 30, null,
                            12, -1, null, null, null, null, 29, null, null, null, null, 17, 22, 18,
                            null, null, null, -1, null, null, null, 20, null, null, null, -1, 18,
                            18, null, null, null, null, 20, null, null, null, null, null, null,
                            null});
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
            test1("LRUCache1_2");
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