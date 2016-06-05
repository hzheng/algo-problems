import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// Given a linked list, reverse the nodes of a linked list k at a time and
// return its modified list.
public class ReverseGroupNodes {
    // beats 6.76%
    public ListNode reverseKGroup(ListNode head, int k) {
        if (k <= 1) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        head = dummy;
        while (true) {
            ListNode tail = head;
            for (int i = k; i > 0 && tail != null; i--, tail = tail.next) {}
            if (tail == null) return dummy.next;
            // reverse nodes between head and tail
            ListNode prev = tail.next;
            for (ListNode cur = head.next; cur != tail; ) {
                ListNode next = cur.next;
                cur.next = prev;
                prev = cur;
                cur = next;
            }
            tail.next = prev;
            ListNode next = head.next;
            head.next = tail;
            head = next;
        }
    }

    void test(int[] n, int k, int[] expected) {
        ListNode l = reverseKGroup(ListNode.of(n), k);
        assertArrayEquals(expected, l == null ? null : l.toArray());
    }

    @Test
    public void test1() {
        test(new int[] {}, 1, null);
        test(new int[] {1}, 2, new int[] {1});
        test(new int[] {1, 2}, 2, new int[] {2, 1});
        test(new int[] {1, 2, 3}, 2, new int[] {2, 1, 3});
        test(new int[] {1, 2, 3, 4}, 2, new int[] {2, 1, 4, 3});
        test(new int[] {1, 2, 3, 4, 5}, 2, new int[] {2, 1, 4, 3, 5});
        test(new int[] {1, 2, 3, 4, 5}, 3, new int[] {3, 2, 1, 4, 5});
        test(new int[] {1, 2, 3, 4, 5, 6}, 4, new int[] {4, 3, 2, 1, 5, 6});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseGroupNodes");
    }
}
