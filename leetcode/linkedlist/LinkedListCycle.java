import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// https://leetcode.com/problems/linked-list-cycle/
//
// Given a linked list, determine if it has a cycle in it.

public class LinkedListCycle {
    // time complexity: O(N), space complexity: O(1)
    // beats 12.02%
    public boolean hasCycle(ListNode head) {
        if (head == null) return false;

        ListNode slow = head;
        // ListNode fast = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) return true;
        }
        return false;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats 12.02%
    public boolean hasCycle2(ListNode head) {
        if (head == null) return false;

        for (ListNode slow = head, fast = head.next;
             slow != fast; slow = slow.next, fast = fast.next.next) {
            if (fast == null || fast.next == null) return false;
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 2.72%
    public boolean hasCycle3(ListNode head) {
        Set<ListNode> visited = new HashSet<>();
        for (ListNode n = head; n != null; n = n.next) {
            if (visited.contains(n)) return true;

            visited.add(n);
        }
        return false;
    }

    void test(int[] n, boolean expected) {
        assertEquals(expected, hasCycle(ListNode.byVals(n)));
        assertEquals(expected, hasCycle2(ListNode.byVals(n)));
        assertEquals(expected, hasCycle3(ListNode.byVals(n)));
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 2}, true);
        test(new int[] {1, 2, 3, 4}, false);
        test(new int[] {1, 2, 3, 4, 2}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LinkedListCycle");
    }
}
