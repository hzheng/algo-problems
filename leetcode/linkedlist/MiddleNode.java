import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC876: https://leetcode.com/problems/middle-of-the-linked-list/
//
// Given a non-empty, singly linked list with head node head, return a middle
// node of linked list. If there are 2 middle nodes, return the second one.
public class MiddleNode {
    // beats 100%(1 ms for 15 tests)
    public ListNode middleNode(ListNode head) {
        int len = 0;
        for (ListNode node = head; node != null; node = node.next, len++) {}
        int i = len / 2 + 1;
        for (ListNode node = head;; node = node.next) {
            if (--i == 0) return node;
        }
    }

    // Slow/Fast Pointer
    // beats 100%(1 ms for 15 tests)
    public ListNode middleNode2(ListNode head) {
        ListNode slow = head;
        for (ListNode fast = head; fast != null && fast.next != null; ) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    void test(ListNode head, ListNode expected) {
        assertArrayEquals(expected.toArray(), middleNode(head).toArray());
        assertArrayEquals(expected.toArray(), middleNode2(head).toArray());
    }

    @Test
    public void test() {
        test(ListNode.of(1), ListNode.of(1));
        test(ListNode.of(1, 2), ListNode.of(2));
        test(ListNode.of(1, 2, 3, 4, 5), ListNode.of(3, 4, 5));
        test(ListNode.of(1, 2, 3, 4, 5, 6), ListNode.of(4, 5, 6));
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
