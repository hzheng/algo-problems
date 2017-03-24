import common.LinkedListNode;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 2.3:
 * Delete a node in the middle of a singly linked list,
 * given only access to that node.
 */
public class DelMiddleNode {
    // time complexity: O(1), space complexity: O(1)
    public void delMiddle(LinkedListNode middle) {
        if (middle == null || middle.next == null)
            throw new IllegalArgumentException();

        middle.data = middle.next.data;
        middle.next = middle.next.next;
    }

    public static void main(String[] args) {
    }
}
