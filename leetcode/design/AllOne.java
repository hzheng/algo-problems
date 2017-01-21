import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC432: https://leetcode.com/problems/all-oone-data-structure/
//
// Implement a data structure supporting the following operations.
// Challenge: Perform all these in O(1) time complexity.
public class AllOne {
    static interface IAllOne {
        /** Inserts a new key <Key> with value 1. Or increments an existing key by 1. */
        public void inc(String key);

        /** Decrements an existing key by 1. If Key's value is 1, remove it from the data structure. */
        public void dec(String key);

        /** Returns one of the keys with maximal value. */
        public String getMaxKey();

        /** Returns one of the keys with Minimal value. */
        public String getMinKey();
    }

    static class DoublyLinkedNode {
        DoublyLinkedNode prev;
        DoublyLinkedNode next;
        int val;

        DoublyLinkedNode(int val) { this.val = val; }
    }

    static class DoublyLinkedList {
        DoublyLinkedNode head;
        DoublyLinkedNode tail;

        public void insertAfter(DoublyLinkedNode context, DoublyLinkedNode node) {
            if (context == null) {
                if (head != null) {
                    head.prev = node;
                } else {
                    tail = node;
                }
                node.next = head;
                head = node;
                return;
            }

            DoublyLinkedNode next = context.next;
            context.next = node;
            node.prev = context;
            node.next = next;
            if (next != null) {
                next.prev = node;
            } else {
                tail = node;
            }
        }

        public void remove(DoublyLinkedNode node) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }
            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }
        }
    }

    // DoublyLinkedList
    // beats 70.76%(148 ms for 15 tests)
    static class AllOne1 implements IAllOne {
        private DoublyLinkedList valList = new DoublyLinkedList();
        private Map<Integer, Set<String>> valMap = new HashMap<>();
        private Map<String, DoublyLinkedNode> keyMap = new HashMap<>();

        public void inc(String key) {
            change(key, 1);
        }

        public void dec(String key) {
            change(key, -1);
        }

        private void change(String key, int diff) {
            DoublyLinkedNode valNode = keyMap.get(key);
            if (diff < 0 && valNode == null) return;

            int val = (valNode == null) ? 0 : valNode.val;
            Set<String> newKeySet = valMap.get(val + diff);
            if (newKeySet != null) {
                newKeySet.add(key);
                keyMap.put(key, diff < 0 ? valNode.prev
                                         : (valNode == null) ? valList.head : valNode.next);
            } else if (diff < 0 && val == 1) {
                keyMap.remove(key);
            } else {
                newKeySet = new HashSet<>();
                newKeySet.add(key);
                valMap.put(val + diff, newKeySet);
                DoublyLinkedNode node = new DoublyLinkedNode(val + diff);
                keyMap.put(key, node);
                valList.insertAfter(diff > 0 ? valNode : valNode.prev, node);
            }
            // remove old if necessary
            Set<String> oldKeySet = valMap.get(val);
            if (oldKeySet != null) {
                oldKeySet.remove(key);
                if (oldKeySet.isEmpty()) {
                    valList.remove(valNode);
                    valMap.remove(val);
                }
            }
        }

        public String getMaxKey() {
            return (valList.tail == null) ? "" : valMap.get(valList.tail.val).iterator().next();
        }

        public String getMinKey() {
            return (valList.head == null) ? "" : valMap.get(valList.head.val).iterator().next();
        }
    }

    // if Java's ListIterator supported clone, we could use keyed iterators
    // like https://discuss.leetcode.com/topic/63827/c-solution-with-comments

    void test1(IAllOne obj) {
        assertEquals("", obj.getMaxKey());
        assertEquals("", obj.getMinKey());
        obj.inc("a");
        assertEquals("a", obj.getMaxKey());
        assertEquals("a", obj.getMinKey());
        obj.inc("a");
        obj.inc("b");
        obj.inc("b");
        obj.inc("b");
        obj.inc("c");
        assertEquals("b", obj.getMaxKey());
        assertEquals("c", obj.getMinKey());
        obj.dec("b");
        obj.inc("a");
        assertEquals("a", obj.getMaxKey());
        assertEquals("c", obj.getMinKey());
        obj.inc("c");
        obj.dec("b");
        assertEquals("b", obj.getMinKey());
        obj.dec("b");
        assertEquals("c", obj.getMinKey());
    }

    @Test
    public void test1() {
        test1(new AllOne1());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AllOne");
    }
}
