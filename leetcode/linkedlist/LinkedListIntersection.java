import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC160: https://leetcode.com/problems/intersection-of-two-linked-lists/
//
// Find the node at which the intersection of two singly linked lists begins.
// Your code should preferably run in O(n) time and use only O(1) memory.
public class LinkedListIntersection {
    // hashtable works, but space complexity is not O(1)

    // Solution of Choice
    // beats 37.12%(2 ms for 42 tests)
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lenA = length(headA);
        int lenB = length(headB);
        ListNode p1 = headA;
        ListNode p2 = headB;
        for (; lenA > lenB; p1 = p1.next, lenA--) {}
        for (; lenA < lenB; p2 = p2.next, lenB--) {}
        for (; p1 != p2; p1 = p1.next, p2 = p2.next) {}
        return p1;
    }

    private int length(ListNode node) {
        int length = 0;
        for (; node != null; node = node.next, length++) {}
        return length;
    }

    // Fast/Slow Pointers
    // beats 12.51%(3 ms)
    public ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        ListNode tail = headA;
        for (; tail.next != null; tail = tail.next);
        tail.next = headB;
        ListNode intersection = cycleStart(headA);
        tail.next = null;
        return intersection;
    }

    private ListNode cycleStart(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        do {
            if (fast == null || fast.next == null) return null;

            slow = slow.next;
            fast = fast.next.next;
        } while (slow != fast);

        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    // Two Pointers
    // beats 9.84%(4 ms)
    public ListNode getIntersectionNode3(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        for (ListNode n1 = headA, n2 = headB, tailA = null, tailB = null;;
             n1 = n1.next, n2 = n2.next) {
            if (n1 == null) {
                n1 = headB;
            }
            if (n2 == null) {
                n2 = headA;
            }
            if (n1 == n2) return n1;

            if (n1.next == null) {
                tailA = n1;
            }
            if (n2.next == null) {
                tailB = n2;
            }
            if (tailA != null && tailB != null && tailA != tailB) return null;
        }
    }

    // Solution of Choice
    // Two Pointers
    // beats 37.12%(2 ms)
    public ListNode getIntersectionNode4(ListNode headA, ListNode headB) {
        ListNode p1 = headA, p2 = headB;
        while (p1 != p2) {
            p1 = (p1 == null) ? headB : p1.next;
            p2 = (p2 == null) ? headA : p2.next;
        }
        return p1;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<ListNode, ListNode, ListNode> intersect, int[] nums1,
              int[] nums2, int ... expected) {
        ListNode res = intersect.apply(ListNode.byVals(nums1),
                                       ListNode.byVals(nums2));
        if (expected.length == 0) {
            assertNull(res);
        } else {
            assertArrayEquals(expected, res.toArray());
        }
    }

    void test(int[] nums1, int[] nums2, int ... expected) {
        LinkedListIntersection l = new LinkedListIntersection();
        test(l::getIntersectionNode, nums1, nums2, expected);
        test(l::getIntersectionNode2, nums1, nums2, expected);
        test(l::getIntersectionNode3, nums1, nums2, expected);
        test(l::getIntersectionNode4, nums1, nums2, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[0]);
        test(new int[] {1}, new int[] {2});
        test(new int[] {3}, new int[] {2, 3}, 3);
        test(new int[] {1, 2, 3, 4, 5, 6},
             new int[] {9, 8, 4, 5, 6}, 4, 5, 6);
        test(new int[] {1, 2, 3, 4, 5, 6},
             new int[] {12, 11, 10, 9, 8, 4, 5, 6}, 4, 5, 6);
        test(new int[] {12, 11, 10, 9, 8, 4, 5, 6},
             new int[] {1, 2, 3, 4, 5, 6}, 4, 5, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LinkedListIntersection");
    }
}
