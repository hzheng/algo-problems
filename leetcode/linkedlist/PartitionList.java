import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// Given a linked list and a value x, partition it such that all nodes less than
// x come before nodes greater than or equal to x. You should preserve the
// original relative order of the nodes in each of the two partitions.
public class PartitionList {
    // beats 2.48%
    public ListNode partition(ListNode head, int x) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (ListNode small = dummy, prev = dummy, cur = head;
             cur != null; cur = cur.next) {
            if (cur.val >= x) {
                prev = cur;
            } else if (small.next == cur) {
                small = cur;
                prev = cur;
            } else {
                ListNode next = cur.next;
                cur.next = small.next;
                small = small.next = cur;
                prev.next = next;
                cur = prev;
            }
        }
        return dummy.next;
    }

    // http://www.jiuzhang.com/solutions/partition-list/
    // beats 2.48%
    public ListNode partition2(ListNode head, int x) {
        ListNode leftDummy = new ListNode(0);
        ListNode rightDummy = new ListNode(0);
        ListNode left = leftDummy;
        ListNode right = rightDummy;
        for (; head != null; head = head.next) {
            if (head.val < x) {
                left.next = head;
                left = head;
            } else {
                right.next = head;
                right = head;
            }
        }
        right.next = null;
        left.next = rightDummy.next;
        return leftDummy.next;
    }

    void test(int x, ListNode head, int ... expected) {
        ListNode res = partition(head, x);
        // ListNode res = partition2(head, x);
        assertArrayEquals(expected, res.toArray());
    }

    @Test
    public void test1() {
        test(3, ListNode.of(1, 4, 3, 2, 5, 2), 1, 2, 2, 4, 3, 5);
        test(3, ListNode.of(3, 1, 2), 1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PartitionList");
    }
}
