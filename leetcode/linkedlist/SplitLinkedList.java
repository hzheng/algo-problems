import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC725: https://leetcode.com/problems/split-linked-list-in-parts/
//
// Split the linked list into k consecutive linked list "parts".
// The length of each part should be as equal as possible
public class SplitLinkedList {
    // beats 19.22%(5 ms for 41 tests)
    public ListNode[] splitListToParts(ListNode root, int k) {
        int length = 0;
        for (ListNode node = root; node != null; node = node.next, length++) {}
        int mean = length / k;
        int remaining = length % k;
        ListNode globalCur = root;
        ListNode[] res = new ListNode[k];
        for (int i = 0; i < k; i++) {
            int count = mean;
            if (remaining > 0) {
                count++;
                remaining--;
            }
            ListNode dummy = new ListNode(0);
            ListNode cur = dummy;
            for (int j = 0; j < count; j++) {
                cur = cur.next = new ListNode(globalCur.val);
                globalCur = globalCur.next;
            }
            res[i] = dummy.next;
        }
        return res;
    }

    // Modify the original list
    // beats 59.09%(4 ms for 41 tests)
    public ListNode[] splitListToParts2(ListNode root, int k) {
        int length = 0;
        for (ListNode node = root; node != null; node = node.next, length++) {}
        int mean = length / k;
        int remaining = length % k;
        ListNode[] res = new ListNode[k];
        ListNode cur = root;
        for (int i = 0; i < k; ++i) {
            ListNode head = cur;
            for (int j = mean + (i < remaining ? 0 : -1); j > 0; j--) {
                cur = cur.next;
            }
            if (cur != null) {
                ListNode prev = cur;
                cur = cur.next;
                prev.next = null;
            }
            res[i] = head;
        }
        return res;
    }

    void test(int[] list, int k, int[][] expected) {
        SplitLinkedList s = new SplitLinkedList();
        test(list, k, expected, s::splitListToParts);
        test(list, k, expected, s::splitListToParts2);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(int[] list, int k, int[][] expected,
              Function<ListNode, Integer, ListNode[]> splitListToParts) {
        ListNode[] res = splitListToParts.apply(ListNode.of(list), k);
        assertArrayEquals(expected,
                          Arrays.stream(res).map(
                              x -> (x == null) ? new int[0] : x.toArray()).toArray());
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3}, 5, new int[][] {{1}, {2}, {3}, {}, {}});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 3,
             new int[][] {{1, 2, 3, 4}, {5, 6, 7}, {8, 9, 10}});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
