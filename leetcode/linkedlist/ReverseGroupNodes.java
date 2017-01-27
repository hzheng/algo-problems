import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC025: https://leetcode.com/problems/reverse-nodes-in-k-group/
//
// Given a linked list, reverse the nodes of a linked list k at a time and
// return its modified list.
public class ReverseGroupNodes {
    // beats 6.06%(9 ms)
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        head = dummy;
        while (true) {
            ListNode tail = head;
            for (int i = k; i > 0 && tail != null; i--, tail = tail.next) {}
            if (tail == null) return dummy.next;
            // reverse nodes between head(exclusive) and tail(exclusive)
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
            head = next; // move forward head
        }
    }

    // Solution of Choice
    // beats 12.75%(7 ms)
    public ListNode reverseKGroup2(ListNode head, int k) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        head = dummy;
        while (true) {
            ListNode tail = head;
            for (int i = k; i > 0 && tail != null; i--, tail = tail.next) {}
            if (tail == null) return dummy.next;
            // reverse nodes between head(exclusive) and tail(inclusive)
            ListNode next;
            for (ListNode cur = head.next, prev = tail.next; ;
                 prev = cur, cur = next) {
                next = cur.next;
                cur.next = prev;
                if (cur == tail) break;
            }
            next = head.next; // record the next head
            head.next = tail; // switch head's next to the reversed head i.e. 'tail'
            head = next; // move forward head
        }
    }

    // Recursion
    // beats 8.66%(8 ms)
    public ListNode reverseKGroup3(ListNode head, int k) {
        int count = 0;
        ListNode cur = head;
        for (; cur != null && count < k; cur = cur.next, count++) {}
        if (count < k) return head;

        cur = reverseKGroup3(cur, k);
        while (count-- > 0) {
            ListNode next = head.next;
            head.next = cur; // prepend head to the reversed part
            cur = head; // adjust(move back) the head of the reversed part
            head = next; // adjust(move forward) the head of the front part
        }
        return cur;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<ListNode, Integer, ListNode> reverseKGroup,
              int [] n, int k, int[] expected) {
        ListNode l = reverseKGroup.apply(ListNode.of(n), k);
        assertArrayEquals(expected, l == null ? null : l.toArray());
    }

    void test(int[] n, int k, int[] expected) {
        ReverseGroupNodes r = new ReverseGroupNodes();
        test(r::reverseKGroup, n, k, expected);
        test(r::reverseKGroup2, n, k, expected);
        test(r::reverseKGroup3, n, k, expected);
    }

    @Test
    public void test1() {
        test(new int[] {}, 1, null);
        test(new int[] {1}, 1, new int[] {1});
        test(new int[] {1}, 2, new int[] {1});
        test(new int[] {1, 2}, 2, new int[] {2, 1});
        test(new int[] {1, 2, 3}, 2, new int[] {2, 1, 3});
        test(new int[] {1, 2, 3, 4}, 2, new int[] {2, 1, 4, 3});
        test(new int[] {1, 2, 3, 4, 5}, 2, new int[] {2, 1, 4, 3, 5});
        test(new int[] {1, 2, 3, 4, 5}, 3, new int[] {3, 2, 1, 4, 5});
        test(new int[] {1, 2, 3, 4, 5, 6}, 4, new int[] {4, 3, 2, 1, 5, 6});
        test(new int[] {1, 2, 3, 4, 5, 6, 7}, 3, new int[] {3, 2, 1, 6, 5, 4, 7});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, 3,
             new int[] {3, 2, 1, 6, 5, 4, 9, 8, 7});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, 3,
             new int[] {3, 2, 1, 6, 5, 4, 9, 8, 7, 10, 11});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseGroupNodes");
    }
}
