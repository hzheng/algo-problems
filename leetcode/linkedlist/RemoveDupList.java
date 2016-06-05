import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// Given a sorted linked list, delete all duplicates.
public class RemoveDupList {
    // beats 16.98%
    public ListNode deleteDuplicates(ListNode head) {
        if (head == null) return null;

        for (ListNode n = head; n.next != null; ) {
            if (n.val == n.next.val) {
                n.next = n.next.next;
            } else {
                n = n.next;
            }
        }
        return head;
    }

    void test(Function<ListNode, ListNode> removeDup, int [] nums, int[] expected) {
        nums = nums.clone();
        int[] res = removeDup.apply(ListNode.of(nums)).toArray();
        assertArrayEquals(expected, res);
    }

    void test(int[] nums, int[] expected) {
        RemoveDupList rm = new RemoveDupList();
        test(rm::deleteDuplicates, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1}, new int[] {1});
        test(new int[] {1, 1, 2}, new int[] {1, 2});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9},
             new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        test(new int[] {1, 2, 2, 4, 6, 6, 8, 8, 9},
             new int[] {1, 2, 4, 6, 8, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDupList");
    }
}
