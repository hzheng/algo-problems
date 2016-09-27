import common.ListNode;

import org.junit.Test;
import static org.junit.Assert.*;

// LC061: https://leetcode.com/problems/rotate-list/
//
// Given a list, rotate the list to the right by k places
public class RotateList {
    // beats 40.06%(15 ms)
    public ListNode rotateRight(ListNode head, int k) {
        if (k == 0) return head;

        ListNode first = head;
        int len = 0;
        for (int i = k - 1; i >= 0 && first != null; i--) {
            first = first.next;
            len++;
        }
        if (first == null) {
            if (len < 2) return head;

            k %= len;
            if (k == 0) return head;

            first = head;
            for (int i = k - 1; i >= 0 && first != null; i--) {
                first = first.next;
            }
        }
        ListNode second = head;
        while (first.next != null) {
            first = first.next;
            second = second.next;
        }

        ListNode res = second.next;
        second.next = null;
        first.next = head;
        return res;
    }

    // Solution of Choice
    // beats 21.88%(17 ms)
    public ListNode rotateRight2(ListNode head, int k) {
        if (head == null || k == 0) return head;

        int i;
        ListNode fast = head;
        for (i = 1; fast.next != null; i++, fast = fast.next) {}
        ListNode slow = head;
        for (int j = i - k % i - 1; j > 0; j--, slow = slow.next) {}
        fast.next = head;
        ListNode res = slow.next;
        slow.next = null;
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<ListNode, Integer, ListNode> rotateRight, int k,
              int[] n, int ... expected) {
        ListNode l = rotateRight.apply(ListNode.of(n), k);
        assertArrayEquals(expected, l == null ? null : l.toArray());
    }

    void test(int k, int[] n, int ... expected) {
        RotateList r = new RotateList();
        test(r::rotateRight, k, n, expected);
        test(r::rotateRight2, k, n, expected);
    }

    @Test
    public void test1() {
        test(2, new int[] {1, 2}, 1, 2);
        test(1, new int[] {}, null);
        test(1, new int[] {1}, 1);
        test(0, new int[] {1, 2}, 1, 2);
        test(3, new int[] {1, 2}, 2, 1);
        test(2, new int[] {1, 2, 3, 4, 5}, 4, 5, 1, 2, 3);
        test(7, new int[] {1, 2, 3, 4, 5}, 4, 5, 1, 2, 3);
        test(6, new int[] {1, 2, 3, 4, 5}, 5, 1, 2, 3, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RotateList");
    }
}
