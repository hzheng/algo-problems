import org.junit.Test;

import static org.junit.Assert.*;

import common.ListNode;

// LC1669: https://leetcode.com/problems/merge-in-between-linked-lists/
//
// You are given two linked lists: list1 and list2 of sizes n and m respectively.
// Remove list1's nodes from the ath node to the bth node, and put list2 in their place.
//
// Constraints:
// 3 <= list1.length <= 10^4
// 1 <= a <= b < list1.length - 1
// 1 <= list2.length <= 10^4
public class MergeInBetween {
    // time complexity: O(M+N), space complexity: O(1)
    // 1 ms(100.00%), 42.4 MB(98.82%) for 61 tests
    public ListNode mergeInBetween(ListNode list1, int a, int b, ListNode list2) {
        ListNode end = list1;
        ListNode start = null;
        for (int i = 0; i < b; i++, end = end.next) {
            if (i == a - 1) {
                start = end;
            }
        }
        start.next = list2;
        for (; list2.next != null; list2 = list2.next) {}
        list2.next = end.next;
        end.next = null; // may or may not be necessary
        return list1;
    }

    @FunctionalInterface interface Function<A, B, C, D, E> {
        E apply(A a, B b, C c, D d);
    }

    private void test(int[] list1, int a, int b, int[] list2, int[] expected,
                      Function<ListNode, Integer, Integer, ListNode, ListNode> mergeInBetween) {
        ListNode res = mergeInBetween.apply(ListNode.of(list1), a, b, ListNode.of(list2));
        assertArrayEquals(expected, res.toArray());
    }

    private void test(int[] list1, int a, int b, int[] list2, int[] expected) {
        MergeInBetween m = new MergeInBetween();
        test(list1, a, b, list2, expected, m::mergeInBetween);
    }

    @Test public void test() {
        test(new int[] {0, 1, 2, 3, 4, 5}, 3, 4, new int[] {1000000, 1000001, 1000002},
             new int[] {0, 1, 2, 1000000, 1000001, 1000002, 5});
        test(new int[] {0, 1, 2, 3, 4, 5, 6}, 2, 5,
             new int[] {1000000, 1000001, 1000002, 1000003, 1000004},
             new int[] {0, 1, 1000000, 1000001, 1000002, 1000003, 1000004, 6});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
