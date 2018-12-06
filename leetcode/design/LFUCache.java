import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC460: https://leetcode.com/problems/lfu-cache/
//
// Design and implement a data structure for Least Frequently Used (LFU) cache.
// It should support the following operations: get and put.
// get(key) - Get the value (will always be positive) of the key if the key
// exists in the cache, otherwise return -1.
// put(key, value) - Set or insert the value if the key is not already present.
// When the cache reaches its capacity, it should invalidate the least
// frequently used item before inserting a new item. When there is a tie,
// the least recently used key would be evicted.
// Follow up:
// Could you do both operations in O(1) time complexity?
public class LFUCache {
    static class Node<T> {
        T val;
        Node<T> prev;
        Node<T> next;

        Node(T val) {
            this.val = val;
        }
    }

    static class DoublyLinkedList<T> {
        Node<T> head = new Node<>(null);
        Node<T> tail = new Node<>(null);

        public DoublyLinkedList() {
            head.next = tail;
            tail.prev = head;
        }

        public boolean isEmpty() {
            return head.next == tail;
        }

        public void insertAfter(Node<T> context, Node<T> node) {
            Node<T> next = context.next;
            context.next = node;
            node.prev = context;
            node.next = next;
            next.prev = node;
        }

        public void putFront(Node<T> node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }

        public void remove(Node<T> node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        public Node<T> removeEnd() {
            if (isEmpty()) return null;

            Node<T> node = tail.prev;
            remove(node);
            return node;
        }
    }

    static class BucketVal {
        int key;
        int val;
        Node<Bucket> bucket;

        BucketVal(int key, int val, Node<Bucket> bucket) {
            this.key = key;
            this.val = val;
            this.bucket = bucket;
        }
    }

    static class Bucket {
        int freq;
        DoublyLinkedList<BucketVal> list = new DoublyLinkedList<>();

        Bucket(int freq) {
            this.freq = freq;
        }
    }

    // DoublyLinkedList + Hashtable
    // beats 96.35%(163 ms for 23 tests)
    class LFUCache1 {
        private int capacity;
        private int count;
        DoublyLinkedList<Bucket> freqList = new DoublyLinkedList<>();
        private Map<Integer, Node<BucketVal>> map = new HashMap<>();

        public LFUCache1(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Node<BucketVal> node = map.get(key);
            if (node == null) return -1;

            move(node);
            return node.val.val;
        }

        private void move(Node<BucketVal> node) {
            Node<Bucket> bucketNode = node.val.bucket;
            Bucket bucket = bucketNode.val;
            Node<Bucket> nextNode = bucketNode.next;
            if (nextNode.val == null || nextNode.val.freq != bucket.freq + 1) {
                nextNode = new Node<>(new Bucket(bucket.freq + 1));
                freqList.insertAfter(bucketNode, nextNode);
            }
            bucket.list.remove(node);
            if (bucket.list.isEmpty()) {
                freqList.remove(bucketNode);
            }
            nextNode.val.list.putFront(node);
            node.val.bucket = nextNode;
        }

        public void put(int key, int value) {
            if (capacity == 0) return;

            Node<BucketVal> node = map.get(key);
            if (node != null) {
                move(node);
                node.val.val = value;
                return;
            }
            Node<Bucket> firstNode = freqList.head.next;
            if (++count > capacity) {
                count = capacity;
                Node<BucketVal> end = firstNode.val.list.removeEnd();
                map.remove(end.val.key);
                if (firstNode.val.list.isEmpty()) {
                    freqList.remove(firstNode);
                }
            }
            Node<Bucket> nextNode = freqList.head.next;
            if (nextNode.val == null || nextNode.val.freq != 1) {
                nextNode = new Node<>(new Bucket(1));
                freqList.insertAfter(freqList.head, nextNode);
            }
            BucketVal bucketVal = new BucketVal(key, value, nextNode);
            node = new Node<>(bucketVal);
            map.put(key, node);
            nextNode.val.list.putFront(node);
        }
    }

    // https://discuss.leetcode.com/topic/69737/java-o-1-very-easy-solution-using-3-hashmaps-and-linkedhashset
    // DoublyLinkedSet + Hashtable
    // beats 66.44%(199 ms for 23 tests)
    class LFUCache2 {
        Map<Integer, Integer> vals = new HashMap<>();
        Map<Integer, Integer> visits = new HashMap<>();
        Map<Integer, Set<Integer>> lists = new HashMap<>();
        int capacity;
        int min = -1;

        public LFUCache2(int capacity) {
            this.capacity = capacity;
            lists.put(1, new LinkedHashSet<>());
        }

        public int get(int key) {
            if (!vals.containsKey(key)) return -1;

            int visit = visits.get(key);
            visits.put(key, visit + 1);
            lists.get(visit).remove(key);
            if (visit == min && lists.get(visit).isEmpty()) {
                min++;
            }
            if (!lists.containsKey(++visit)) {
                lists.put(visit, new LinkedHashSet<>());
            }
            lists.get(visit).add(key);
            return vals.get(key);
        }

        public void put(int key, int value) {
            if (capacity <= 0) return;

            if (vals.containsKey(key)) {
                vals.put(key, value);
                get(key);
                return;
            }
            if (vals.size() >= capacity) {
                int evict = lists.get(min).iterator().next();
                lists.get(min).remove(evict);
                vals.remove(evict);
            }
            vals.put(key, value);
            visits.put(key, 1);
            min = 1;
            lists.get(1).add(key);
        }
    }

    // TODO: use LinkedHashMap to implement

    // DoublyLinkedList + Hashtable
    // beats 96.57%(88 ms for 23 tests)
    class LFUCache3 {
        private Map<Integer, Node> map = new HashMap<>();
        private List<DoublyLinkedList> keyLists = new ArrayList<>();
        private int capacity;

        public LFUCache3(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Node node = map.get(key);
            if (node == null) return -1;

            visitNode(node);
            return node.value;
        }

        public void put(int key, int value) {
            Node node = map.get(key);
            if (node == null) {
                node = new Node(key);
                if (map.size() >= capacity) {
                    evict();
                }
                if (capacity > 0) {
                    map.put(key, node);
                }
            }
            node.value = value;
            visitNode(node);
        }

        private void visitNode(Node node) {
            if (node.visited >= 0) {
                keyLists.get(node.visited).remove(node);
            }
            if (++node.visited >= keyLists.size()) {
                keyLists.add(new DoublyLinkedList());
            }
            keyLists.get(node.visited).append(node);
        }

        private void evict() {
            for (DoublyLinkedList list : keyLists) {
                if (!list.isEmpty()) {
                    map.remove(list.head.next.key);
                    list.remove(list.head.next);
                    return;
                }
            }
        }

        private class DoublyLinkedList {
            Node head = new Node(0);
            Node tail = new Node(0);

            DoublyLinkedList() {
                head.next = tail;
                tail.prev = head;
            }

            void append(Node node) {
                tail.prev.next = node;
                node.prev = tail.prev;
                tail.prev = node;
                node.next = tail;
            }

            void remove(Node node) {
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }

            boolean isEmpty() {
                return head.next == tail;
            }
        }

        private class Node {
            int key;
            int value;
            int visited = -1;
            Node prev;
            Node next;

            Node(int key) {
                this.key = key;
            }
        }
    }

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {
        }.getClass().getEnclosingClass().newInstance();
        test(new String[] {className, "put", "put", "get", "put", "get", "get", "put", "get", "get",
                           "get", "put", "get"},
             new Object[][] {new Object[] {outerObj, 2}, {1, 1}, {2, 2}, {1}, {3, 3}, {2}, {3},
                             {4, 4}, {1}, {3}, {4}, {5, 5}, {4}},
             new Integer[] {null, null, null, 1, null, -1, 3, null, -1, 3, 4, null, -1});
        test(new String[] {className, "put", "get"},
             new Object[][] {new Object[] {outerObj, 0}, {0, 0}, {0}},
             new Integer[] {null, null, -1});
        test(new String[] {className, "put", "put", "put", "get", "get", "get"},
             new Object[][] {new Object[] {outerObj, 1101}, {253, 668}, {202, 206}, {1231, 3177},
                             {465}, {1333}, {1231}},
             new Integer[] {null, null, null, null, -1, -1, 3177});
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
            test1("LFUCache1");
            test1("LFUCache2");
            test1("LFUCache3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
