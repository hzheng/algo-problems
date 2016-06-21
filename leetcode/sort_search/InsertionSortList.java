import java.util.*;
import java.util.stream.Stream;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// https://leetcode.com/problems/insertion-sort-list/
//
// Sort a linked list using insertion sort.
public class InsertionSortList {
    // beats 79.42%(35 ms)
    public ListNode insertionSortList(ListNode head) {
        if (head == null) return null;

        ListNode last = head;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (ListNode n = head.next; n != null; ) {
            ListNode next = n.next;
            for (ListNode m = dummy; ; m = m.next) {
                if (m.next == n) {
                    last = n;
                    break;
                }
                if (n.val <= m.next.val) {
                    last.next = n.next;
                    n.next = m.next;
                    m.next = n;
                    break;
                }
            }
            n = next;
        }
        return dummy.next;
    }

    // beats 23.09%(44 ms)
    public ListNode insertionSortList2(ListNode head) {
        ListNode dummy = new ListNode(0);
        for (ListNode n = head; n != null; ) {
            ListNode m = dummy;
            while (m.next != null && m.next.val < n.val) {
                m = m.next;
            }
            ListNode next = n.next;
            n.next = m.next;
            m.next = n;
            n = next;
        }
        return dummy.next;
    }

    void test(Function<ListNode, ListNode> sort,
              int[] nums, int ... expected) {
        ListNode res = sort.apply(ListNode.of(nums));
        assertArrayEquals(expected, res.toArray());
    }

    void test(int[] expected, int ... listArray) {
        InsertionSortList i = new InsertionSortList();
        test(i::insertionSortList, expected, listArray);
        test(i::insertionSortList2, expected, listArray);
    }

    @Test
    public void test1() {
        test(new int[] {2, 1}, 1, 2);
        test(new int[] {2, 1, 3, 6, 5, 4}, 1, 2, 3, 4, 5, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("InsertionSortList");
    }
}
