import java.util.*;

import common.ListNode;

import org.junit.Test;
import static org.junit.Assert.*;

// LC445: https://leetcode.com/problems/add-two-numbers-ii/
//
// You are given two linked lists representing two non-negative numbers.
// The most significant digit comes first and each of their nodes contain a
// single digit. Add the two numbers and return it as a linked list. You may
// assume the two numbers do not contain any leading zero, except 0 itself.
// Follow up:
// What if you cannot modify the input lists? In other words, reversing the
// lists is not allowed.
public class SumNode2 {
    // Modified input
    // beats 81.24%(54 ms for 1560 tests)
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return reverse(add(reverse(l1), reverse(l2)));
    }

    private ListNode reverse(ListNode head) {
        ListNode prev = null;
        for (ListNode cur = head, next; cur != null; prev = cur, cur = next) {
            next = cur.next;
            cur.next = prev;
        }
        return prev;
    }

    // copy from SumNode.java
    private ListNode add(ListNode l1, ListNode l2) {
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

    // Look-Ahead
    // beats 50.22%(63 ms for 1563 tests)
    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {
        int len1 = length(l1);
        int len2 = length(l2);
        ListNode cur1 = l1;
        ListNode cur2 = l2;
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        int lastSum = 0;
        int nines = 0;
        for (int i = Math.max(len1, len2) + 1; i > 0; i--) {
            int first1 = 0;
            if (i <= len1) {
                first1 = cur1.val;
                cur1 = cur1.next;
            }
            int first2 = 0;
            if (i - 1 <= len1 && i > 1) {
                first2 = cur1.val;
            }
            int second1 = 0;
            if (i <= len2) {
                second1 = cur2.val;
                cur2 = cur2.next;
            }
            int second2 = 0;
            if (i - 1 <= len2 && i > 1) {
                second2 = cur2.val;
            }
            int sum1 = first1 + second1;
            int sum2 = first2 + second2;
            if (sum2 == 9 && i > 0) {
                if (nines++ == 0) {
                    lastSum = sum1;
                }
                continue;
            }
            int digit = 9;
            if (sum2 > 9) {
                sum1++;
                lastSum++;
                digit = 0;
            }
            if (nines > 0) {
                cur = cur.next = new ListNode(lastSum % 10);
                for (nines--; nines > 0; nines--) {
                    cur = cur.next = new ListNode(digit);
                }
            }
            cur = cur.next = new ListNode(sum1 % 10);
        }
        ListNode res = dummy.next;
        return (res != null && res.val == 0) ? res.next : res;
    }

    private int length(ListNode head) {
        int len = 0;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            len++;
        }
        return len;
    }

    // Stack
    // beats 34.96%(67 ms for 1563 tests)
    public ListNode addTwoNumbers3(ListNode l1, ListNode l2) {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        for (; l1 != null; l1 = l1.next) {
            stack1.push(l1.val);
        }
        for (; l2 != null; l2 = l2.next) {
            stack2.push(l2.val);
        }
        ListNode head = new ListNode(0);
        for (int sum = 0; !stack1.empty() || !stack2.empty(); sum /= 10) {
            if (!stack1.empty()) {
                sum += stack1.pop();
            }
            if (!stack2.empty()) {
                sum += stack2.pop();
            }
            head.val = sum % 10;
            ListNode newHead = new ListNode(sum / 10);
            newHead.next = head;
            head = newHead;
        }
        return head.val == 0 ? head.next : head;
    }

    void test(int[] n1, int[] n2, int ... expected) {
        ListNode l1 = ListNode.of(n1);
        ListNode l2 = ListNode.of(n2);
        assertArrayEquals(expected, addTwoNumbers2(l1, l2).toArray());
        assertArrayEquals(expected, addTwoNumbers3(l1, l2).toArray());
        assertArrayEquals(expected, addTwoNumbers(l1, l2).toArray());
    }

    @Test
    public void test1() {
        test(new int[] {3, 7}, new int[] {9, 2}, 1, 2, 9);
        test(new int[] {7, 3, 4, 3}, new int[] {5, 6, 4}, 7, 9, 0, 7);
        test(new int[] {2, 3, 6}, new int[] {5, 6, 5}, 8, 0, 1);
        test(new int[] {2, 3, 6}, new int[] {5, 6, 4}, 8, 0, 0);
        test(new int[] {2, 3, 7, 6}, new int[] {5, 6, 2, 4}, 8, 0, 0, 0);
        test(new int[] {2, 3, 7, 6, 1, 2, 8},
             new int[] {5, 6, 2, 4, 6, 7, 5}, 8, 0, 0, 0, 8, 0, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SumNode2");
    }
}
