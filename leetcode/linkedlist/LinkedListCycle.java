import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC141: https://leetcode.com/problems/linked-list-cycle/
//
// Given a linked list, determine if it has a cycle in it.
public class LinkedListCycle {
    // time complexity: O(N), space complexity: O(1)
    // beats 12.02%(1 ms)
    public boolean hasCycle(ListNode head) {
        if (head == null) return false;

        ListNode slow = head;
        ListNode fast = head.next;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) return true;
        }
        return false;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // beats 100.00%(0 ms for 16 tests)
    public boolean hasCycle2(ListNode head) {
        if (head == null) return false;

        for (ListNode slow = head, fast = head.next; slow != fast;
             slow = slow.next, fast = fast.next.next) {
            if (fast == null || fast.next == null) return false;
        }
        return true;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // beats 13.69%(11 ms for 16 tests)
    public boolean hasCycle3(ListNode head) {
        Set<ListNode> visited = new HashSet<>();
        for (ListNode cur = head;; cur = cur.next) {
            if (cur == null) return false;

            if (!visited.add(cur)) return true;
        }
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
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
