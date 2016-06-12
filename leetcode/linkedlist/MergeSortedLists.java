import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// https://leetcode.com/problems/merge-two-sorted-lists/
// Merge two sorted linked lists and return it as a new list.

public class MergeSortedLists {
    // beats 11.81%
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        for (ListNode cur = dummy; l1 != null || l2 != null; cur = cur.next) {
            if (l1 == null || (l2 != null && l1.val > l2.val)) {
                cur.next = l2;
                l2 = l2.next;
            } else {
                cur.next = l1;
                l1 = l1.next;
            }
        }
        return dummy.next;
    }

    void test(int[] n1, int[] n2, int[] expected) {
        ListNode l1 = ListNode.of(n1);
        ListNode l2 = ListNode.of(n2);
        ListNode l = mergeTwoLists(l1, l2);
        assertArrayEquals(expected, l.toArray());
    }

    @Test
    public void test1() {
        test(new int[]{1, 3, 5}, new int[]{2, 4, 6},
            new int[] {1, 2, 3, 4, 5, 6});
        test(new int[]{3}, new int[]{2, 4, 6}, new int[] {2, 3, 4, 6});
        test(new int[]{}, new int[]{2, 4, 6}, new int[] {2, 4, 6});
        test(new int[]{2, 10}, new int[]{}, new int[] {2, 10});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MergeSortedLists");
    }
}
