import org.junit.Test;
import static org.junit.Assert.*;

import common.LinkedListNode;

/**
 * Cracking the Coding Interview(5ed) Problem 2.6:
 * Given a circular linked list, implement an algorithm which returns the node
 * at the beginning of the loop.
 */
public class ListLoop {
    public LinkedListNode loopNode(LinkedListNode head) {
        if (head == null || head.next == null) return null;

        LinkedListNode slowPtr = head;
        LinkedListNode fastPtr = head;
        while (fastPtr != null && fastPtr.next != null) {
            fastPtr = fastPtr.next.next;
            slowPtr = slowPtr.next;
            if (fastPtr == slowPtr) {
                break;
            }
        }
        if (fastPtr == null || fastPtr.next == null) {
            return null;
        }

        for (slowPtr = head; slowPtr != fastPtr; ) {
            slowPtr = slowPtr.next;
            fastPtr = fastPtr.next;
        }
        return slowPtr;
    }

    void test(int[] n, int[] expected) {
        LinkedListNode node = loopNode(LinkedListNode.uniqueList(n));
        if (expected == null) {
            assertNull(node);
        } else {
            assertEquals(expected[0], node.data);
        }
    }

    @Test
    public void test1() {
        test(new int[] {1}, null);
    }

    @Test
    public void test2() {
        test(new int[] {1, 2}, null);
    }

    @Test
    public void test3() {
        test(new int[] {1, 2, 3}, null);
    }

    @Test
    public void test4() {
        test(new int[] {1, 1}, new int[] {1});
    }

    @Test
    public void test5() {
        test(new int[] {1, 2, 1}, new int[] {1});
    }

    @Test
    public void test6() {
        test(new int[] {1, 3, 2, 7, 8, 9, 4, 5, 6, 0, 4}, new int[] {4});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ListLoop");
    }
}
