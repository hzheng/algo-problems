import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// L203: https://leetcode.com/problems/remove-linked-list-elements/
//
// Remove all elements from a linked list of integers that have value val.
public class RemoveListElements {
    // Solution of Choice
    // beats 3.47%(2 ms)
    public ListNode removeElements(ListNode head, int val) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (ListNode n = dummy; n.next != null; ) {
            if (n.next.val == val) {
                n.next = n.next.next;
            } else {
                n = n.next;
            }
        }
        return dummy.next;
    }

    // Recursion
    // beats 0.71%(3 ms for 63 tests)
    public ListNode removeElements2(ListNode head, int val) {
        if (head == null) return null;

        head.next = removeElements2(head.next, val);
        return head.val == val ? head.next : head;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<ListNode, Integer, ListNode> remove, int [] nums,
              int val, int[] expected) {
        int[] res = remove.apply(ListNode.of(nums), val).toArray();
        assertArrayEquals(expected, res);
    }

    void test(int[] nums, int val, int[] expected) {
        RemoveListElements r = new RemoveListElements();
        test(r::removeElements, nums, val, expected);
        test(r::removeElements2, nums, val, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1, 2}, 1, new int[] {2});
        test(new int[] {1, 2, 4, 6, 8, 9}, 4, new int[] {1, 2, 6, 8, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveListElements");
    }
}
