import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC206: https://leetcode.com/problems/reverse-linked-list/
//
// Reverse a singly linked list.
public class ReverseList {
    // Solution of Choice
    // beats 33.03%(0 ms)
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        for (ListNode cur = head, next; cur != null; cur = next) {
            next = cur.next;
            cur.next = prev;
            prev = cur;
        }
        return prev;
    }

    // Recursion
    // beats 4.66%(1 ms)
    public ListNode reverseList2(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode newHead = reverseList2(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    void test(Function<ListNode, ListNode> reverse, int [] nums, int[] expected) {
        nums = nums.clone();
        int[] res = reverse.apply(ListNode.of(nums)).toArray();
        assertArrayEquals(expected, res);
    }

    void test(int[] nums, int[] expected) {
        ReverseList r = new ReverseList();
        test(r::reverseList, nums, expected);
        test(r::reverseList2, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1, 2}, new int[] {2, 1, 1});
        test(new int[] {1, 2, 4, 6, 8, 9},
             new int[] {9, 8, 6, 4, 2, 1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseList");
    }
}
