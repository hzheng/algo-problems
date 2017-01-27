import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC024: https://leetcode.com/problems/swap-nodes-in-pairs/
//
// Given a linked list, swap every two adjacent nodes and return its head.
public class SwapNodePairs {
    // Solution of Choice
    // beats 22.60%(0 ms)
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

    // Recursion
    // beats 1.59%(6 ms)
    public ListNode swapPairs2(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode newHead = head.next;
        head.next = swapPairs(head.next.next);
        newHead.next = head;
        return newHead;
    }

    void test(Function<ListNode, ListNode> swap, int[] n, int[] expected) {
        ListNode l = swap.apply(ListNode.of(n));
        assertArrayEquals(expected, l == null ? null : l.toArray());
    }

    void test(int[] n, int[] expected) {
        SwapNodePairs s = new SwapNodePairs();
        test(s::swapPairs, n, expected);
        test(s::swapPairs2, n, expected);
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
