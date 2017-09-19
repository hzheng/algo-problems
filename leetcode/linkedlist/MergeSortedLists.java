import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC021: https://leetcode.com/problems/merge-two-sorted-lists/
//
// Merge two sorted linked lists and return it as a new list.
public class MergeSortedLists {
    // Solution of Choice
    // beats 11.81%(1 ms)
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

    // Recursion
    // beats 7.09%(15 ms)
    public ListNode mergeTwoLists2(ListNode l1, ListNode l2){
        if (l1 == null) return l2;

        if (l2 == null) return l1;

        if (l1.val < l2.val) {
            l1.next = mergeTwoLists2(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists2(l1, l2.next);
            return l2;
        }
    }

    void test(Function<ListNode, ListNode, ListNode> merge,
              int[] n1, int[] n2, int[] expected) {
        ListNode l1 = ListNode.of(n1);
        ListNode l2 = ListNode.of(n2);
        ListNode l = merge.apply(l1, l2);
        assertArrayEquals(expected, l.toArray());
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(int[] n1, int[] n2, int[] expected) {
        MergeSortedLists m = new MergeSortedLists();
        test(m::mergeTwoLists, n1, n2, expected);
        test(m::mergeTwoLists2, n1, n2, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 3, 5}, new int[] {2, 4, 6},
             new int[] {1, 2, 3, 4, 5, 6});
        test(new int[] {3}, new int[] {2, 4, 6}, new int[] {2, 3, 4, 6});
        test(new int[] {}, new int[] {2, 4, 6}, new int[] {2, 4, 6});
        test(new int[] {2, 10}, new int[] {}, new int[] {2, 10});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MergeSortedLists");
    }
}
