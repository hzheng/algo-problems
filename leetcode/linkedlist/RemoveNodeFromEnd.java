import org.junit.Test;

import common.ListNode;

// LC019: https://leetcode.com/problems/remove-nth-node-from-end-of-list/
// 
// Given a linked list, remove the nth node from the end of list.
public class RemoveNodeFromEnd {
    // beats 18.00%(1 ms)
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode tail = head;
        for (int i = n; i > 0; i--, tail = tail.next) {
            // if (tail == null) return head; // non-null guaranteed by req.
        }
        if (tail == null) return head.next;

        ListNode oldHead = head;
        while (tail.next != null) {
            tail = tail.next;
            head = head.next;
        }
        head.next = head.next.next;
        return oldHead;
    }

    // Solution of Choice
    // beats 18.00%(1 ms)
    public ListNode removeNthFromEnd2(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        for (int i = n; i >= 0; i--) {
            first = first.next;
        }

        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }

    @Test
    public void test1() {
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveNodeFromEnd");
    }
}
