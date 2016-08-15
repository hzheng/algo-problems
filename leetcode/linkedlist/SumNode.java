import common.ListNode;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/add-two-numbers/
//
// You are given two linked lists representing two non-negative numbers. The
// digits are stored in reverse order and each of their nodes contain a single
// digit. Add the two numbers and return it as a linked list.
public class SumNode {
    // beats 35.18%(4 ms)
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null || l2 == null) return null;

        ListNode sumNode = null;
        ListNode sumCur = null;
        int carry = 0;
        while (l1 != null || l2 != null) {
            int v1 = (l1 == null) ? 0 : l1.val;
            int v2 = (l2 == null) ? 0 : l2.val;
            int sum = v1 + v2 + carry;
            carry = sum / 10;
            ListNode partialSum = new ListNode(sum % 10);
            if (sumNode == null) {
                sumNode = partialSum;
            } else {
                sumCur.next = partialSum;
            }
            sumCur = partialSum;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (carry > 0) {
            sumCur.next = new ListNode(1);
        }
        return sumNode;
    }

    // Solution of Choice
    // https://leetcode.com/articles/add-two-numbers/
    // beats 31.63%(4 ms)
    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        ListNode dummyHead = new ListNode(0);
        ListNode cur = dummyHead;
        int carry = 0;
        for (ListNode p = l1, q = l2; p != null || q != null; cur = cur.next) {
            int x = 0;
            if (p != null) {
                x = p.val;
                p = p.next;
            }
            int y = 0;
            if (q != null) {
                y = q.val;
                q = q.next;
            }
            int sum = carry + x + y;
            carry = sum / 10;
            cur.next = new ListNode(sum % 10);
        }
        if (carry > 0) {
            cur.next = new ListNode(carry);
        }
        return dummyHead.next;
    }

    void test(int[] n1, int[] n2, int ... expected) {
        ListNode l1 = ListNode.of(n1);
        ListNode l2 = ListNode.of(n2);
        assertArrayEquals(expected, addTwoNumbers(l1, l2).toArray());
        assertArrayEquals(expected, addTwoNumbers2(l1, l2).toArray());
    }

    @Test
    public void test1() {
        test(new int[] {2, 4, 3}, new int[] {5, 6, 4}, 7, 0, 8);
        test(new int[] {8, 4, 3}, new int[] {5, 6, 4}, 3, 1, 8);
        test(new int[] {2, 4, 7}, new int[] {5, 6, 4}, 7, 0, 2, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SumNode");
    }
}
