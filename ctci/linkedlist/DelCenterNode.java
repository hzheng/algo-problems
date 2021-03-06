import org.junit.Test;
import static org.junit.Assert.*;

import common.LinkedListNode;

/**
 * Misinterpretion of Cracking the Coding Interview(5ed) Problem 2.3:
 * Delete the center node of a singly linked list if it exists.
 */
public class DelCenterNode {
    // time complexity: O(N), space complexity: O(1)
    public void delMiddle(LinkedListNode head) {
        if (head == null) throw new IllegalArgumentException();

        LinkedListNode target = head;
        LinkedListNode tail = head.next;
        if (tail == null) return;
        tail = tail.next;
        while (tail != null) {
            if (tail.next == null) { // target found
                target.next = target.next.next;
                return;
            }

            // shift tail and target
            target = target.next;
            tail = tail.next.next;
        }
    }

    void test(int[] before, int[] expected) {
        LinkedListNode list = new LinkedListNode(before);
        delMiddle(list);
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[] {1});
    }

    @Test
    public void test2() {
        test(new int[] {1, 2}, new int[] {1, 2});
    }

    @Test
    public void test3() {
        test(new int[] {1, 2, 3}, new int[] {1, 3});
    }

    @Test
    public void test4() {
        test(new int[] {1, 2, 3, 4}, new int[] {1, 2, 3, 4});
    }

    @Test
    public void test5() {
        test(new int[] {1, 2, 3, 4, 5}, new int[] {1, 2, 4, 5});
    }

    @Test
    public void test6() {
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {1, 2, 3, 4, 5, 6});
    }

    @Test
    public void test7() {
        test(new int[] {1, 2, 3, 4, 5, 6, 7}, new int[] {1, 2, 3, 5, 6, 7});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DelCenterNode");
    }
}
