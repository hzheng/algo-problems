import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

import common.ListNode;

// LC237: https://leetcode.com/problems/delete-node-in-a-linked-list/
//
// Write a function to delete a node (except the tail) in a singly linked list, given only access to that node.
public class DeleteNode {
    // Solution of Choice
    // beats 2.86%(1 ms)
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<ListNode> delete, int [] nums, int[] expected) {
        ListNode list = ListNode.of(nums);
        delete.apply(list);
        assertArrayEquals(expected, list.toArray());
    }

    void test(int[] nums, int[] expected) {
        DeleteNode d = new DeleteNode();
        test(d::deleteNode, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 4}, new int[] {2, 3, 4});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DeleteNode");
    }
}
