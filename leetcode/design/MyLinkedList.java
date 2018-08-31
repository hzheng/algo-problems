import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC707: https://leetcode.com/problems/design-linked-list/
//
// Design your implementation of the linked list.
// Note:
// All values will be in the range of [1, 1000].
// The number of operations will be in the range of [1, 1000].
// Please do not use the built-in LinkedList library.
public class MyLinkedList {
    // beats 96.83%(71 ms for 53 tests)
    class MyLinkedList1 {
        class Node {
            int val;
            Node next;

            Node(int val) {
                this.val = val;
            }
        }

        private Node head = new Node(0);
        private Node tail = head;
        private int len = 0;

        public MyLinkedList1() {}

        /** If the index is invalid, return -1. */
        public int get(int index) {
            if (index < 0 || index >= len) return -1;

            int i = -1;
            for (Node cur = head; ; cur = cur.next) {
                if (i++ == index) return cur.val;
            }
        }
    
        public void addAtHead(int val) {
           Node first = head.next; 
           head.next = new Node(val);
           head.next.next = first;
           if (len++ == 0) {
               tail = head.next;
           }
        }
    
        public void addAtTail(int val) {
           tail = tail.next = new Node(val);
           len++;
        }
        
        /** Add a node of value val before the index-th node in the linked list. 
         * If index equals to the length of linked list, the node will be 
         * appended to the end of linked list. If index is greater than the 
         * length, the node will not be inserted. */
        public void addAtIndex(int index, int val) {
            if (index < 0 || index > len) return;

            int i = 0;
            for (Node cur = head; ; cur = cur.next) {
                if (i++ == index) {
                    Node next = cur.next;
                    cur.next = new Node(val);
                    cur.next.next = next;
                    if (index == len++) {
                        tail = cur.next;
                    }
                    return;
                }
            }
        }
    
        public void deleteAtIndex(int index) {
            if (index < 0 || index >= len) return;

            int i = 0;
            for (Node cur = head; ; cur = cur.next) {
                if (i++ == index) {
                    Node next = cur.next.next;
                    cur.next = next;
                    if (index == --len) {
                        tail = cur;
                    }
                    return;
                }
            }
        }
    }

    void test1(String className) throws Exception {
        Object outerObj =
            new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "addAtHead", "addAtTail", "addAtIndex",
                            "get", "deleteAtIndex", "get" },
             new Object[][] { new Object[] { outerObj }, 
                              new Integer[] { 1 }, new Integer[] { 3 },
                              new Integer[] { 1, 2 }, new Integer[] { 1 },
                              new Integer[] { 1 }, new Integer[] { 1 } },
             new Integer[] { null, null, null, null, 2, null, 3 });

        test(new String[] { className, "addAtHead", "addAtHead", "addAtHead",
                            "addAtIndex", "deleteAtIndex", "addAtHead",
                            "addAtTail", "get", "addAtHead", "addAtIndex",
                            "addAtHead"},
             new Object[][] { new Object[] { outerObj }, 
                              new Integer[] { 7 }, new Integer[] { 2 },
                              new Integer[] { 1 }, new Integer[] { 3, 1 },
                              new Integer[] { 2 }, new Integer[] { 6 },
                              new Integer[] { 4 }, new Integer[] { 4 },
                              new Integer[] { 4 }, new Integer[] { 5, 0 },
                              new Integer[] { 6 } },
             new Integer[] { null, null, null, null, null, null, null, null, 4,
                             null, null, null });

        test(new String[] { className, "addAtHead", "addAtHead", "addAtHead",
                            "addAtIndex", "get", "deleteAtIndex", "get",
                            "addAtTail", "get"},
             new Object[][] { new Object[] { outerObj }, 
                              new Integer[] { 3 }, new Integer[] { 2 },
                              new Integer[] { 1 }, new Integer[] { 3, 4 },
                              new Integer[] { 3 }, new Integer[] { 3 },
                              new Integer[] { 3 }, new Integer[] { 5 },
                              new Integer[] { 3 } },
             new Integer[] { null, null, null, null, null, 4, null, -1, 
                             null, 5 });
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
            test1("MyLinkedList1");
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
