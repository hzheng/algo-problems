import java.util.*;

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
    static interface ILFUCache {
        public int get(int key);

        public void put(int key, int value);
    }

    static class Node<T> {
        T val;
        Node<T> prev;
        Node<T> next;
        Node(T val) { this.val = val; }
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
        Bucket(int freq) { this.freq = freq; }
    }

    // DoublyLinkedList + Hashtable
    // beats 96.35%(163 ms for 23 tests)
    static class LFUCache1 implements ILFUCache {
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
            if (nextNode.val == null ||  nextNode.val.freq != bucket.freq + 1) {
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
            if (nextNode.val == null ||  nextNode.val.freq != 1) {
                nextNode = new Node<>(new Bucket(1));
                freqList.insertAfter(freqList.head, nextNode);
            }
            BucketVal bucketVal = new BucketVal(key, value, nextNode);
            node = new Node<>(bucketVal);
            map.put(key, node);
            nextNode.val.list.putFront(node);
        }
    }

    static void test3(ILFUCache cache) {
        cache.put(253, 668);
        cache.put(202, 206);
        cache.put(1231,3177);
        assertEquals(-1, cache.get(465));
        assertEquals(-1, cache.get(1333));
    }

    static void test2(ILFUCache cache) {
        cache.put(0, 0);
        assertEquals(-1, cache.get(0));
    }

    static void test1(ILFUCache cache) {
        cache.put(1, 1);
        cache.put(2, 2);
        assertEquals(1, cache.get(1));
        cache.put(3, 3);    // evicts key 2
        assertEquals(-1, cache.get(2));
        assertEquals(3, cache.get(3));
        cache.put(4, 4);    // evicts key 1
        assertEquals(-1, cache.get(1));
        assertEquals(3, cache.get(3));
        assertEquals(4, cache.get(4));
        cache.put(5, 5);    // evicts key 4
        assertEquals(-1, cache.get(4));
    }

    @Test
    public void test1() {
        test1(new LFUCache1(2));
        test2(new LFUCache1(0));
        test3(new LFUCache1(1101));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LFUCache");
    }
}
