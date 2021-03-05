import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;

import common.ListNode;

// LC1290: https://leetcode.com/problems/convert-binary-number-in-a-linked-list-to-integer/
//
// Given head which is a reference node to a singly-linked list. The value of each node in the
// linked list is either 0 or 1. The linked list holds the binary representation of a number.
// Return the decimal value of the number in the linked list.
//
// Constraints:
// The Linked List is not empty.
// Number of nodes will not exceed 30.
// Each node's value is either 0 or 1.
public class GetDecimalValue {
    // Sort
    // time complexity: O(N)), space complexity: O(1)
    // 0 ms(100.00%), 36.2 MB(91.95%) for 102 tests
    public int getDecimalValue(ListNode head) {
        int res = 0;
        for (ListNode p = head; p != null; p = p.next) {
            res <<= 1;
            res |= p.val;
        }
        return res;
    }

    private void test(int[] head, int expected) {
        assertEquals(expected, getDecimalValue(ListNode.of(head)));
    }

    @Test public void test() {
        test(new int[] {1, 0, 1}, 5);
        test(new int[] {0}, 0);
        test(new int[] {1}, 1);
        test(new int[] {0, 0}, 0);
        test(new int[] {1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0}, 18880);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
