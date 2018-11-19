import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC206: https://leetcode.com/problems/reverse-linked-list/
//
// Reverse a singly linked list.
public class ReverseList {
    // Solution of Choice
    // beats 100.00%(0 ms for 27 tests)
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        for (ListNode cur = head, next; cur != null; prev = cur, cur = next) {
            next = cur.next;
            cur.next = prev;
        }
        return prev;
    }

    // Recursion
    // beats 100.00%(0 ms for 27 tests)
    public ListNode reverseList2(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode newHead = reverseList2(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    // Recursion
    // beats 100.00%(0 ms for 27 tests)
    public ListNode reverseList3(ListNode head) {
        return reverseList(head, null);
    }

    private ListNode reverseList(ListNode head, ListNode newHead) {
        if (head == null) return newHead;

        ListNode next = head.next;
        head.next = newHead;
        return reverseList(next, head);
    }

    void test(Function<ListNode, ListNode> reverse, int[] nums, int[] expected) {
        nums = nums.clone();
        int[] res = reverse.apply(ListNode.of(nums)).toArray();
        assertArrayEquals(expected, res);
    }

    void test(int[] nums, int[] expected) {
        ReverseList r = new ReverseList();
        test(r::reverseList, nums, expected);
        test(r::reverseList2, nums, expected);
        test(r::reverseList3, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1, 2}, new int[] {2, 1, 1});
        test(new int[] {1, 2, 4, 6, 8, 9}, new int[] {9, 8, 6, 4, 2, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
