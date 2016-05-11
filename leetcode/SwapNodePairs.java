import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;
// Given a linked list, swap every two adjacent nodes and return its head.

public class SwapNodePairs {
    // beats 13.35%
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (ListNode cur = dummy; cur.next != null && cur.next.next != null; ) {
            ListNode tmp = cur.next;

            cur.next = tmp.next;
            tmp.next = cur.next.next;
            cur.next.next = tmp;
            // replace the following code, which works but need one more tmp
            // ListNode tmp2 = tmp.next;
            // tmp.next = tmp2.next;
            // cur.next = tmp2;
            // tmp2.next = tmp;

            cur = tmp;
        }
        return dummy.next;
    }

    public ListNode swapPairs2(ListNode head) {
        if (head == null) return head;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (ListNode cur = dummy; cur.next != null && cur.next.next != null; ) {
        }
        return dummy.next;
    }

    void test(int[] n, int[] expected) {
        ListNode l = swapPairs(ListNode.of(n));
        assertArrayEquals(expected, l == null ? null : l.toArray());
    }

    @Test
    public void test1() {
        test(new int[] {}, null);
        test(new int[] {1}, new int[] {1});
        test(new int[] {1, 2}, new int[] {2, 1});
        test(new int[] {1, 2, 3}, new int[] {2, 1, 3});
        test(new int[] {1, 2, 3, 4}, new int[] {2, 1, 4, 3});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SwapNodePairs");
    }
}
