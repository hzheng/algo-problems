import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// Given a sorted linked list, delete all nodes that have duplicate numbers, leaving only distinct numbers from the original list.
public class RemoveDupList2 {
    // beats 21.24%
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode lastDup = null;
        ListNode lastNode = dummy;
        for (ListNode n = head; n != null; ) {
            if (lastDup != null) {
                if (lastDup.val == n.val) {
                    lastNode.next = n.next;
                    n = n.next;
                    continue;
                }
                lastDup = null;
            }
            if (n.next == null) break;

            if (n.val == n.next.val) {
                lastDup = n;
                n = lastNode.next = n.next.next;
            } else {
                lastNode = n;
                n = n.next;
            }
        }
        return dummy.next;
    }

    // beats 21.24%
    public ListNode deleteDuplicates2(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode lastNode = dummy;
        for (ListNode n = head; n != null && n.next != null; n = n.next) {
            if (n.val != n.next.val) {
                lastNode = n;
            } else {
                do {
                    n = n.next;
                } while (n.next != null && n.val == n.next.val) ;
                lastNode.next = n.next;
            }
        }
        return dummy.next;
    }

    void test(Function<ListNode, ListNode> removeDup, int [] nums, int[] expected) {
        nums = nums.clone();
        ListNode res = removeDup.apply(ListNode.of(nums));
        assertArrayEquals(expected, res == null ? new int[0] : res.toArray());
    }

    void test(int[] nums, int[] expected) {
        RemoveDupList2 rm = new RemoveDupList2();
        test(rm::deleteDuplicates, nums, expected);
        test(rm::deleteDuplicates2, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1}, new int[] {});
        test(new int[] {1, 1, 1, 2, 3}, new int[] {2, 3});
        test(new int[] {1, 2, 3, 3, 4, 4, 5}, new int[] {1, 2, 5});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9},
             new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9});
        test(new int[] {1, 2, 2, 4, 6, 6, 8, 8, 9},
             new int[] {1, 4, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDupList2");
    }
}
