import java.util.*;
import java.util.stream.Stream;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// https://leetcode.com/problems/sort-list/
//
// Sort a linked list in O(n log n) time using constant space complexity.
public class SortList {
    // beats 15.88%
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) return head;

        ListNode half = halve(head);
        return merge(sortList(head),  sortList(half));
    }

    private ListNode halve(ListNode head) {
        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode half = slow.next;
        slow.next = null;
        return half;
    }

    private ListNode merge(ListNode n1, ListNode n2) {
        if (n1 == null) return n2;

        if (n2 == null) return n1;

        ListNode dummy = new ListNode(0);
        ListNode end = dummy;
        while (n1 != null && n2 != null) {
            if (n1.val < n2.val) {
                end.next = n1;
                n1 = n1.next;
            } else {
                end.next = n2;
                n2 = n2.next;
            }
            end = end.next;
        }
        end.next = (n1 == null) ? n2 : n1;
        return dummy.next;
    }

    void test(Function<ListNode, ListNode> sort,
              int[] nums, int ... expected) {
        ListNode res = sort.apply(ListNode.of(nums));
        assertArrayEquals(expected, res.toArray());
    }

    void test(int[] expected, int ... listArray) {
        SortList s = new SortList();
        test(s::sortList, expected, listArray);
    }

    @Test
    public void test1() {
        test(new int[] {1}, 1);
        test(new int[] {2, 1}, 1, 2);
        test(new int[] {2, 1, 3, 6, 5, 4}, 1, 2, 3, 4, 5, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SortList");
    }
}
