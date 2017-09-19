import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC369: https://leetcode.com/problems/plus-one-linked-list
//
// Given a non-negative integer represented as non-empty a singly linked list of
// digits, plus one to the integer.
// You may assume the integer do not contain any leading zero, except the number 0 itself.
// The digits are stored such that the most significant digit is at the head of the list.
public class PlusOneLinkedList {
    // beats 59.31%(0 ms for 108 tests)
    public ListNode plusOne(ListNode head) {
        if (head == null) return null;

        ListNode res = reverse(head);
        for (ListNode cur = res; cur != null; cur.val = 0, cur = cur.next) {
            if (++cur.val < 10) return reverse(res);
        }
        res = reverse(res);
        ListNode one = new ListNode(1);
        one.next = res;
        return one;
    }

    private ListNode reverse(ListNode head) {
        ListNode prev = null;
        for (ListNode cur = head, next; cur != null; prev = cur, cur = next) {
            next = cur.next;
            cur.next = prev;
        }
        return prev;
    }

    // beats 10.26%(1 ms for 108 tests)
    public ListNode plusOne2(ListNode head) {
        if (head == null) return null;

        ListNode lastNon9 = null;
        ListNode tail = head;
        for (ListNode cur = head; cur != null; tail = cur, cur = cur.next) {
            if (cur.val < 9) {
                lastNon9 = cur;
            }
        }
        if (++tail.val < 10) return head;

        tail.val -= 10;
        if (lastNon9 != null) {
            lastNon9.val++;
        } else {
            ListNode one = new ListNode(1);
            one.next = head;
            lastNon9 = head = one;
        }
        for (ListNode cur = lastNon9.next; cur != tail; cur = cur.next) {
            cur.val = 0;
        }
        return head;
    }

    // beats 59.31%(0 ms for 108 tests)
    public ListNode plusOne3(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode lastNon9 = dummy;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (cur.val != 9) {
                lastNon9 = cur;
            }
        }
        lastNon9.val++;
        for (ListNode cur = lastNon9.next; cur != null; cur = cur.next) {
            cur.val = 0;
        }
        return dummy.val == 1 ? dummy : dummy.next;
    }

    // beats 59.31%(0 ms for 108 tests)
    public ListNode plusOne4(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        plus(dummy); // or: plus2(dummy)
        return dummy.val == 1 ? dummy : dummy.next;
    }

    private int plus(ListNode head) {
        if (head == null) return 1;
        if ((head.val += plus(head.next)) < 10) return 0;

        head.val = 0;
        return 1;
    }

    private int plus2(ListNode head) {
        int sum = head.val + (head.next == null ? 1 : plus2(head.next));
        head.val = sum % 10;
        return sum / 10;
    }

    void test(int[] nums, int[] expected) {
        assertArrayEquals(expected, plusOne(ListNode.of(nums)).toArray());
        assertArrayEquals(expected, plusOne2(ListNode.of(nums)).toArray());
        assertArrayEquals(expected, plusOne3(ListNode.of(nums)).toArray());
        assertArrayEquals(expected, plusOne4(ListNode.of(nums)).toArray());
    }

    @Test
    public void test() {
        test(new int[] {0}, new int[] {1});
        test(new int[] {1}, new int[] {2});
        test(new int[] {1, 2, 3}, new int[] {1, 2, 4});
        test(new int[] {1, 2, 9}, new int[] {1, 3, 0});
        test(new int[] {9}, new int[] {1, 0});
        test(new int[] {1, 9}, new int[] {2, 0});
        test(new int[] {9, 9, 9}, new int[] {1, 0, 0, 0});
        test(new int[] {9, 9, 9, 9}, new int[] {1, 0, 0, 0, 0});
        test(new int[] {8, 9, 9, 9}, new int[] {9, 0, 0, 0});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PlusOneLinkedList");
    }
}
