import common.ListNode;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a list, rotate the list to the right by k places
public class RotateList {
    // beats 14.33%
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

    void test(int k, int[] n, int ... expected) {
        ListNode l = rotateRight(ListNode.of(n), k);
        assertArrayEquals(expected, l == null ? null : l.toArray());
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
