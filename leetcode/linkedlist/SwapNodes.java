import org.junit.Test;

import static org.junit.Assert.*;

import common.ListNode;

// LC1721: https://leetcode.com/problems/swapping-nodes-in-a-linked-list/
//
// You are given the head of a linked list, and an integer k. Return the head of the linked list
// after swapping the values of the kth node from the beginning and the kth node from the end (the
// list is 1-indexed).
//
// Constraints:
// The number of nodes in the list is n.
// 1 <= k <= n <= 105
// 0 <= Node.val <= 100
public class SwapNodes {
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(87.23%), 65 MB(84.62%) for 132 tests
    public ListNode swapNodes(ListNode head, int k) {
        int len = 0;
        ListNode kth = null;
        int i = 0;
        for (ListNode cur = head; cur != null; cur = cur.next, len++) {
            if (++i == k) {
                kth = cur;
            }
        }
        i = 0;
        for (ListNode cur = head; ; cur = cur.next) {
            if (i++ == (len - k)) {
                int tmp = cur.val;
                cur.val = kth.val;
                kth.val = tmp;
                return head;
            }
        }
    }

    // One Pass
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(99.76%), 65.2 MB(77.42%) for 132 tests
    public ListNode swapNodes2(ListNode head, int k) {
        int i = 0;
        ListNode node1 = null;
        ListNode node2 = null;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (++i == k) {
                node1 = cur;
                node2 = head;
            } else if (node2 != null) {
                node2 = node2.next;
            }
        }
        int tmp = node1.val;
        node1.val = node2.val;
        node2.val = tmp;
        return head;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<ListNode, Integer, ListNode> swapNodes, int[] list, int k,
                      int[] expected) {
        ListNode res = swapNodes.apply(ListNode.of(list), k);
        assertEquals(ListNode.of(expected), res);
    }

    private void test(int[] list, int k, int[] expected) {
        SwapNodes s = new SwapNodes();
        test(s::swapNodes, list, k, expected);
        test(s::swapNodes2, list, k, expected);
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4, 5}, 2, new int[] {1, 4, 3, 2, 5});
        test(new int[] {7, 9, 6, 6, 7, 8, 3, 0, 9, 5}, 5, new int[] {7, 9, 6, 6, 8, 7, 3, 0, 9, 5});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
