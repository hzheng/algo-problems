import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC430: https://leetcode.com/problems/flatten-a-multilevel-doubly-linked-list/
//
// You are given a doubly linked list which in addition to the next and previous
// pointers, it could have a child pointer, which may or may not point to a
// separate doubly linked list. These child lists may have one or more children
// of their own, and so on, to produce a multilevel data structure. Flatten the
// list so that all the nodes appear in a single-level, doubly linked list.
public class FlattenDoublyLinkedList {
    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;

        public Node() {}

        public Node(int _val, Node _prev, Node _next, Node _child) {
            val = _val;
            prev = _prev;
            next = _next;
            child = _child;
        }
    }

    // Recursion + Stack
    // beats 89.91%(2 ms for 22 tests)
    public Node flatten(Node head) {
        flatten(head, new Stack<>());
        return head;
    }

    private void flatten(Node head, Stack<Node> stack) {
        if (head == null) return;

        if (head.child == null) {
            if (head.next == null && !stack.empty()) {
                head.next = stack.pop();
                head.next.prev = head;
            }
        } else {
            if (head.next != null) {
                stack.push(head.next);
            }
            head.next = head.child;
            head.child = null;
            head.next.prev = head;
        }
        flatten(head.next, stack);
    }

    // Recursion
    // beats 89.91%(2 ms for 22 tests)
    public Node flatten2(Node head) {
        return flatten(head, new Node());
    }

    private Node flatten(Node head, Node dummy) {
        for (Node cur = head; cur != null; dummy.child = cur, cur = cur.next) {
            if (cur.child == null) continue;

            Node next = cur.next;
            cur.child.prev = cur;
            cur.next = flatten(cur.child, dummy);
            cur.child = null;
            if (next != null) {
                Node p = dummy.child;
                p.next = next;
                next.prev = p;
            }
        }
        return head;
    }

    // Recursion
    // beats 89.91%(2 ms for 22 tests)
    public Node flatten3(Node head) {
        doFlatten(head);
        return head;
    }

    private Node doFlatten(Node head) {
        if (head == null) return null;

        if (head.child == null) {
            return (head.next == null) ? head : doFlatten(head.next);
        }

        Node next = head.next;
        head.next = head.child;
        head.next.prev = head;
        head.child = null;
        Node tail = doFlatten(head.next);
        if (next == null) return tail;

        tail.next = next;
        next.prev = tail;
        return doFlatten(next);
    }

    // Recursion
    // beats 100.00%(1 ms for 22 tests)
    public Node flatten4(Node head) {
        return doFlatten(head, null);
    }

    private Node doFlatten(Node head, Node tail) {
        if (head == null) return tail;

        Node next = doFlatten(head.next, tail);
        if (head.child == null) {
            head.next = next;
            if (next != null) {
                next.prev = head;
            }
        } else {
            head.next = doFlatten(head.child, next);
            head.next.prev = head;
            head.child = null;
        }
        return head;
    }

    // Iteration
    // beats 89.91%(2 ms for 22 tests)
    public Node flatten5(Node head) {
        for (Node p = head; p != null; ) {
            if (p.child == null) {
                p = p.next;
                continue;
            }
            Node tail = p.child;
            for (; tail.next != null; tail = tail.next) {}
            tail.next = p.next;
            if (p.next != null) {
                p.next.prev = tail;
            }
            p.next = p.child;
            p.next.prev = p;
            p.child = null;
        }
        return head;
    }

    @Test
    public void test() {}

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
