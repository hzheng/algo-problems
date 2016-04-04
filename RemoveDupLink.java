import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 2.1:
 * Write code to remove duplicates from an unsorted linked list.
 */
class LinkedListNode {
    Object data;
    LinkedListNode next;

    public LinkedListNode(Object data) {
        this.data = data;
    }

    public LinkedListNode(Object[] array) {
        if (array == null || array.length == 0) return;

        data = array[0];
        LinkedListNode tail = this;
        for (int i = 1; i < array.length; i++) {
            tail.next = new LinkedListNode(array[i]);
            tail = tail.next;
        }
    }

    public boolean equals(LinkedListNode that) {
        if (that == null) return false;

        if (data == null && that.data == null) return true;

        if (data == null && that.data != null) return false;

        return data.equals(that.data);
    }

    public Object[] toArray() {
        List<Object> list = new ArrayList<Object>();
        for (LinkedListNode l = this; l != null; l = l.next) {
            list.add(l.data);
        }
        return list.toArray();
    }
}

public class RemoveDupLink {
    /**
     * time complexity: O(N^2) space complexity: O(1)
     */
    void removeDupLink(LinkedListNode head) {
        if (head == null) return;

        for (LinkedListNode cursor = head; cursor != null; cursor = cursor.next) {
            // remove all nodes after checkHead that are equal to the cursor
            for (LinkedListNode checkHead = cursor; checkHead.next != null; ) {
                if (cursor.equals(checkHead.next)) {
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
    void removeDupLinkByHash(LinkedListNode head) {
        if (head == null) return;

        Map<Object, Boolean> map = new HashMap<Object, Boolean>();
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

    void test(Object[] before, Object[] expected) {
        LinkedListNode list = new LinkedListNode(before);
        removeDupLink(list);
        assertArrayEquals(expected, list.toArray());

        LinkedListNode list2 = new LinkedListNode(before);
        removeDupLinkByHash(list2);
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void test1() {
        test(new Object[] {1}, new Object[] {1});
    }

    @Test
    public void test2() {
        test(new Object[] {1, 1}, new Object[] {1});
    }

    @Test
    public void test3() {
        test(new Object[] {1, 2, 1}, new Object[] {1, 2});
    }

    @Test
    public void test4() {
        test(new Object[] {1, 2, 1, 2}, new Object[] {1, 2});
    }

    // @Test
    public void test5() {
        test(new Object[] {1, 2, 5, 2, 8, 16, 1, 1, 5, 3},
             new Object[] {1, 2, 5, 8, 16, 3});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDupLink");
    }
}
