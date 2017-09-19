import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC092: https://leetcode.com/problems/reverse-linked-list-ii/
//
// Reverse a linked list from position m to n. Do it in-place and in one-pass.
public class ReverseList2 {
    // beats 2.61%(1 ms)
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (m >= n) return head;

        ListNode prev = new ListNode(0);
        prev.next = head;
        for (int i = 1; i < m; i++) {
            prev = prev.next;
        }
        ListNode left = prev;
        ListNode cur = prev.next;
        for (int i = m; i < n; i++) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        left.next.next = cur.next;
        left.next = cur;
        cur.next = prev;
        return (m == 1) ? cur : head;
    }

    // Solution of Choice
    // https://discuss.leetcode.com/topic/15034/12-lines-4ms-c
    // beats 13.66%(0 ms)
    public ListNode reverseBetween2(ListNode head, int m, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode start = dummy;
        for (int i = 1; i < m; i++) {
            start = start.next;
        }
        ListNode end = start.next;
        for (int i = m; i < n; i++) {
            // detach the node(nextMove) which is immediately after 'end'
            ListNode nextMove = end.next;
            end.next = nextMove.next;
            // insert the node immediately after 'start'
            nextMove.next = start.next;
            start.next = nextMove;
        }
        return dummy.next;
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<ListNode, Integer, Integer, ListNode> reverse,
              int[] nums, int m, int n, int[] expected) {
        nums = nums.clone();
        ListNode resList = reverse.apply(ListNode.of(nums), m, n);
        int[] res = (resList == null) ? new int[0] : resList.toArray();
        assertArrayEquals(expected, res);
    }

    void test(int[] nums, int m, int n, int[] expected) {
        ReverseList2 r = new ReverseList2();
        test(r::reverseBetween, nums, m, n, expected);
        test(r::reverseBetween2, nums, m, n, expected);
    }

    @Test
    public void test1() {
        test(new int[] {}, 0, 0, new int[] {});
        test(new int[] {1, 2, 3, 4, 5}, 2, 2, new int[] {1, 2, 3, 4, 5});
        test(new int[] {1, 2, 3, 4, 5}, 2, 3, new int[] {1, 3, 2, 4, 5});
        test(new int[] {1, 2, 3, 4, 5}, 2, 4, new int[] {1, 4, 3, 2, 5});
        test(new int[] {1, 2, 3, 4, 5}, 2, 5, new int[] {1, 5, 4, 3, 2});
        test(new int[] {1, 2, 3, 4, 5}, 1, 5, new int[] {5, 4, 3, 2, 1});
        test(new int[] {1, 2, 3, 4, 5}, 1, 2, new int[] {2, 1, 3, 4, 5});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, 4, 7,
             new int[] {1, 2, 3, 7, 6, 5, 4, 8, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseList2");
    }
}
