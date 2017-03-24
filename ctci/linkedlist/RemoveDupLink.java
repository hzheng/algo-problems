import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

import common.LinkedListNode;

/**
 * Cracking the Coding Interview(5ed) Problem 2.1:
 * Write code to remove duplicates from an unsorted linked list.
 */

public class RemoveDupLink {
    /**
     * time complexity: O(N^2) space complexity: O(1)
     */
    public void removeDupLink(LinkedListNode head) {
        if (head == null) return;

        for (LinkedListNode cursor = head; cursor != null; cursor = cursor.next) {
            // remove all nodes after checkHead that are equal to the cursor
            for (LinkedListNode checkHead = cursor; checkHead.next != null; ) {
                if (cursor.data == checkHead.next.data) {
                    checkHead.next = checkHead.next.next; // remove the duplicate
                } else {
                    checkHead = checkHead.next;
                }
            }
        }
    }

    /**
     * time complexity: O(N) space complexity: O(N)
     */
    public void removeDupLinkByHash(LinkedListNode head) {
        if (head == null) return;

        Map<Integer, Boolean> map = new HashMap<Integer, Boolean>();
        LinkedListNode prev = null;
        for (LinkedListNode cursor = head; cursor != null; cursor = cursor.next) {
            if (map.containsKey(cursor.data)) {
                prev.next = cursor.next;
            } else {
                map.put(cursor.data, true);
                prev = cursor;
            }
        }
    }

    void test(int[] before, int[] expected) {
        LinkedListNode list = new LinkedListNode(before);
        removeDupLink(list);
        assertArrayEquals(expected, list.toArray());

        LinkedListNode list2 = new LinkedListNode(before);
        removeDupLinkByHash(list2);
        assertArrayEquals(expected, list2.toArray());
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[] {1});
    }

    @Test
    public void test2() {
        test(new int[] {1, 1}, new int[] {1});
    }

    @Test
    public void test3() {
        test(new int[] {1, 2, 1}, new int[] {1, 2});
    }

    @Test
    public void test4() {
        test(new int[] {1, 2, 1, 2}, new int[] {1, 2});
    }

    @Test
    public void test5() {
        test(new int[] {1, 2, 5, 2, 8, 16, 1, 1, 5, 3},
             new int[] {1, 2, 5, 8, 16, 3});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDupLink");
    }
}
