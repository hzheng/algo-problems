import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC083: https://leetcode.com/problems/remove-duplicates-from-sorted-list/
//
// Given a sorted linked list, delete all duplicates.
public class RemoveDupList {
    // beats 16.55%(1 ms)
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;

        for (ListNode n = head; n.next != null; ) {
            if (n.val == n.next.val) {
                n.next = n.next.next;
            } else {
                n = n.next;
            }
        }
        return head;
    }

    // Recursion
    // beats 6.62%(2 ms)
    public ListNode deleteDuplicates2(ListNode head) {
        if (head == null || head.next == null) return head;

        head.next = deleteDuplicates(head.next);
        return head.val == head.next.val ? head.next : head;
    }

    void test(Function<ListNode, ListNode> removeDup, int [] nums, int[] expected) {
        nums = nums.clone();
        int[] res = removeDup.apply(ListNode.of(nums)).toArray();
        assertArrayEquals(expected, res);
    }

    void test(int[] nums, int[] expected) {
        RemoveDupList rm = new RemoveDupList();
        test(rm::deleteDuplicates, nums, expected);
        test(rm::deleteDuplicates2, nums, expected);
    }

    @Test
    public void test1() {
        // test(new int[] {}, new int[] {});
        test(new int[] {1}, new int[] {1});
        test(new int[] {1, 1}, new int[] {1});
        test(new int[] {1, 1, 2}, new int[] {1, 2});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9},
             new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        test(new int[] {1, 2, 2, 4, 6, 6, 8, 8, 9},
             new int[] {1, 2, 4, 6, 8, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDupList");
    }
}
