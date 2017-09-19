import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC147: https://leetcode.com/problems/insertion-sort-list/
//
// Sort a linked list using insertion sort.
public class InsertionSortList {
    // beats 79.42%(35 ms)
    public ListNode insertionSortList(ListNode head) {
        if (head == null) return null;

        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (ListNode last = head, n = head.next, next; n != null; n = next) {
            next = n.next;
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
        }
        return dummy.next;
    }

    // Solution of Choice
    // beats 9.47%(52 ms for 21 tests)
    public ListNode insertionSortList2(ListNode head) {
        ListNode dummy = new ListNode(0);
        for (ListNode cur = head, next; cur != null; cur = next) {
            next = cur.next;
            ListNode prev = dummy;
            for (; prev.next != null && prev.next.val < cur.val; prev = prev.next) {}
            cur.next = prev.next;
            prev.next = cur;
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
