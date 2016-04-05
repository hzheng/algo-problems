import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 2.2:
 * Find the kth to last element of a singly linked list.
 */
public class K2LastLink {
    // time complexity: O(N), space complexity: O(1)
    public LinkedListNode k2Last(LinkedListNode head, int k) {
        if (head == null) throw new IllegalArgumentException();
        if (k <= 0) throw new IndexOutOfBoundsException();

        LinkedListNode result = head;
        LinkedListNode cursor = head;
        for (int i = 0; i < k; i++) {
            if (cursor == null) throw new IndexOutOfBoundsException();
            cursor = cursor.next;
        }
        while (cursor != null) {
            result = result.next;
            cursor = cursor.next;
        }
        return result;
    }

    class Int {
        int value;
    }

    // time complexity: O(N), space complexity: O(N)
    public LinkedListNode k2LastRecursive(LinkedListNode head, int k) {
        if (head == null) throw new IllegalArgumentException();
        if (k <= 0) throw new IndexOutOfBoundsException();

        return k2LastRecursive(head, k, new Int());
    }

    LinkedListNode k2LastRecursive(LinkedListNode head, int k, Int count) {
        if (head == null) {
            return null;
        }

        LinkedListNode node = k2LastRecursive(head.next, k, count);
        if (++count.value == k) {
            return head;
        }
        return node;
    }

    void test(int[] array, int k, int expected) {
        LinkedListNode list = new LinkedListNode(array);
        LinkedListNode result = k2Last(list, k);
        LinkedListNode result2 = k2LastRecursive(list, k);
        assertEquals(expected, result.data);
        assertEquals(expected, result2.data);
    }

    @Test
    public void test0() {
        test(new int[] {3}, 1, 3);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, 3, 6);
    }

    @Test
    public void test2() {
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, 1, 8);
    }

    @Test
    public void test3() {
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, 8, 1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void test4() {
        // FIXME: can only check the first exception
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, 9, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("K2LastLink");
    }
}
