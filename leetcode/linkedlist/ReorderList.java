import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC143: https://leetcode.com/problems/reorder-list/
//
// Given a singly linked list L: L0→L1→…→Ln-1→Ln,
// reorder it to: L0→Ln→L1→Ln-1→L2→Ln-2→…
public class ReorderList {
    // Stack
    // beats 1.02%(10 ms)
    public void reorderList(ListNode head) {
        Stack<ListNode> stack = new Stack<>();
        for (ListNode n = head; n != null; n = n.next) {
            stack.push(n);
        }
        for (ListNode begin = head; begin != null; begin = begin.next.next) {
            ListNode end = stack.pop();
            if (begin == end || begin.next == end) return;

            end.next = begin.next;
            begin.next = end;
            stack.peek().next = null;
        }
    }

    // Recursion
    // Time Limited Exceeded
    public void reorderList2(ListNode head) {
        if (head == null || head.next == null) return;

        ListNode end = head;
        for (; end.next != null && end.next.next != null; end = end.next);
        if (head != end) {
            end.next.next = head.next;
            head.next = end.next;
            end.next = null;
            reorderList2(head.next.next);
        }
    }

    // beats 77.74%(2 ms)
    public void reorderList3(ListNode head) {
        if (head == null || head.next == null) return;

        // find middle pointer
        ListNode slow = head;
        for (ListNode fast = head.next; fast != null && fast.next != null; ) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode half = slow.next;
        slow.next = null;
        // reverse half
        ListNode reversedHalf = null;
        for (ListNode cur = half, next; cur != null; cur = next) {
            next = cur.next;
            cur.next = reversedHalf;
            reversedHalf = cur;
        }
        // merge
        for (ListNode n = head, m = reversedHalf, next; m != null; m = next) {
            next = n.next;
            n = n.next = m;
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<ListNode> reorder, int [] nums, int[] expected) {
        ListNode list = ListNode.of(nums);
        reorder.apply(list);
        assertArrayEquals(expected, list.toArray());
    }

    void test(int[] nums, int[] expected) {
        ReorderList r = new ReorderList();
        test(r::reorderList, nums, expected);
        test(r::reorderList2, nums, expected);
        test(r::reorderList3, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 4}, new int[] {1, 4, 2, 3});
        test(new int[] {1, 2, 3, 4, 5, 6},
             new int[] {1, 6, 2, 5, 3, 4});
        test(new int[] {1, 2, 3, 4, 5, 6, 7},
             new int[] {1, 7, 2, 6, 3, 5, 4});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReorderList");
    }
}
