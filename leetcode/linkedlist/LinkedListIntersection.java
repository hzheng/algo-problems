import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// https://leetcode.com/problems/intersection-of-two-linked-lists/
//
// Find the node at which the intersection of two singly linked lists begins.
// Your code should preferably run in O(n) time and use only O(1) memory.
public class LinkedListIntersection {
    // hashtable works, but space complexity is not O(1)

    // beats 33.93%
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        int lenA = 0;
        ListNode tail = headA;
        for (; tail.next != null; tail = tail.next) {
            lenA++;
        }
        int lenB = 0;
        ListNode nodeB = headB;
        for (; nodeB != null && nodeB != tail; nodeB = nodeB.next) {
            lenB++;
        }
        if (nodeB == null) return null;

        nodeB = headB;
        ListNode nodeA = headA;
        for (int i = lenA; i < lenB; i++) {
            nodeB = nodeB.next;
        }
        for (int i = lenB; i < lenA; i++) {
            nodeA = nodeA.next;
        }
        while (nodeA != nodeB) {
            nodeA = nodeA.next;
            nodeB = nodeB.next;
        }
        return nodeA;
    }

    // beats 12.51%
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

    // from leetcode answer
    // beats 9.84%
    public ListNode getIntersectionNode3(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;

        ListNode tailA = null;
        ListNode tailB = null;
        for (ListNode n1 = headA, n2 = headB; ; n1 = n1.next, n2 = n2.next) {
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

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<ListNode, ListNode, ListNode> intersect, int[] nums1,
              int[] nums2, int ... expected) {
        int[] res = intersect.apply(ListNode.byVals(nums1),
                                    ListNode.byVals(nums2)).toArray();
        assertArrayEquals(expected, res);
    }

    void test(int[] nums1, int[] nums2, int ... expected) {
        LinkedListIntersection l = new LinkedListIntersection();
        test(l::getIntersectionNode, nums1, nums2, expected);
        test(l::getIntersectionNode2, nums1, nums2, expected);
        test(l::getIntersectionNode3, nums1, nums2, expected);
    }

    @Test
    public void test1() {
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
