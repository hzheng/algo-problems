import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// https://leetcode.com/problems/linked-list-cycle-ii/
//
// Given a linked list, return the node where the cycle begins. If
// there is no cycle, return null.
public class LinkedListCycle2 {
    // beats 17.22%
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) break;
        }
        if (fast == null || fast.next == null) return null;

        slow = head;
        while (fast != slow) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    // beats 17.22%
    public ListNode detectCycle2(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                slow = head;
                while (fast != slow) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }

    void test(Function<ListNode, ListNode> detect,
              int[] n, Integer expected) {
        ListNode res = detect.apply(ListNode.byVals(n));
        if (expected == null) {
            assertNull(res);
        } else {
            assertEquals((int)expected, res.val);
        }
    }

    void test(int[] n, Integer expected) {
        LinkedListCycle2 l = new LinkedListCycle2();
        test(l::detectCycle, n, expected);
        test(l::detectCycle2, n, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 2}, 2);
        test(new int[] {1, 2, 3, 4}, null);
        test(new int[] {1, 2, 3, 4, 2}, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LinkedListCycle2");
    }
}
