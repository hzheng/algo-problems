import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC148: https://leetcode.com/problems/sort-list/
//
// Sort a linked list in O(n log n) time using constant space complexity.
public class SortList {
    // Merge Sort + Recursion
    // beats 15.88% (9 ms)
    // time complexity: O(N * log(N)), space complexity: O(log(N))
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
        ListNode end;
        for (end = dummy; n1 != null && n2 != null; end = end.next) {
            if (n1.val < n2.val) {
                end.next = n1;
                n1 = n1.next;
            } else {
                end.next = n2;
                n2 = n2.next;
            }
        }
        end.next = (n1 == null) ? n2 : n1;
        return dummy.next;
    }

    // Merge Sort + Recursion
    // beats 64.75% (7 ms)
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    public ListNode sortList2(ListNode head) {
        if (head == null) return head;

        int len = 0;
        for (ListNode n = head; n != null; n = n.next) {
            len++;
        }

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        return sort(dummy, len);
    }

    private ListNode sort(ListNode dummy, int len) {
        if (len == 1) {
            ListNode head = dummy.next;
            dummy.next = head.next;
            head.next = null;
            return head;
        }

        return merge(sort(dummy, len / 2), sort(dummy, len - len / 2));
    }

    // Solution of Choice
    // beats 1.75% (24 ms)
    // time complexity: O(N * log(N)), space complexity: O(1))
    public ListNode sortList3(ListNode head) {
        int len = 0;
        for (ListNode n = head; n != null; n = n.next) {
            len++;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (int size = 1; size <= len; size <<= 1) {
            for (ListNode prev = dummy; prev != null; ) {
                ListNode p = prev.next;
                ListNode q = p;
                for (int i = 0; i < size && q != null; i++, q = q.next) {}
                prev = (q == null) ? p : merge(p, q, prev, size);
            }
        }
        return dummy.next;
    }

    private ListNode merge(ListNode n1, ListNode n2, ListNode prev, int len) {
        int count1 = 0;
        int count2 = 0;
        for (; count1 < len && count2 < len && n2 != null; prev = prev.next) {
            if (n1.val < n2.val) {
                prev.next = n1;
                n1 = n1.next;
                count1++;
            } else {
                prev.next = n2;
                n2 = n2.next;
                count2++;
            }
        }
        if (count1 == len) {
            for (prev.next = n2; count2 < len - 1 && n2 != null; count2++, n2 = n2.next) {}
            return n2;
        }
        for (prev.next = n1; count1 < len - 1; count1++, n1 = n1.next) {}
        n1.next = n2;
        return n1;
    }

    void test(Function<ListNode, ListNode> sort,
              int[] nums, int ... expected) {
        ListNode res = sort.apply(ListNode.of(nums));
        if (expected.length == 0) {
            assertNull(res);
        } else {
            assertArrayEquals(expected, res.toArray());
        }
    }

    void test(int[] expected, int ... listArray) {
        SortList s = new SortList();
        test(s::sortList, expected, listArray);
        test(s::sortList2, expected, listArray);
        test(s::sortList3, expected, listArray);
    }

    @Test
    public void test1() {
        test(new int[] {});
        test(new int[] {1}, 1);
        test(new int[] {2, 1}, 1, 2);
        test(new int[] {3, 2, 1}, 1, 2, 3);
        test(new int[] {2, 1, 3, 6, 5, 4}, 1, 2, 3, 4, 5, 6);
        test(new int[] {2, 7, 1, 3, 6, 5, 4}, 1, 2, 3, 4, 5, 6, 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SortList");
    }
}
