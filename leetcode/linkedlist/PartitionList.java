import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC086: https://leetcode.com/problems/partition-list/
//
// Given a linked list and a value x, partition it such that all nodes less than
// x come before nodes greater than or equal to x. You should preserve the
// original relative order of the nodes in each of the two partitions.
public class PartitionList {
    // beats 2.48%(1 ms)
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

    // Solution of Choice
    // beats 2.48%(1 ms)
    public ListNode partition2(ListNode head, int x) {
        ListNode leftDummy = new ListNode(0);
        ListNode rightDummy = new ListNode(0);
        ListNode left = leftDummy;
        ListNode right = rightDummy;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (cur.val < x) {
                left = left.next = cur;
            } else {
                right = right.next = cur;
            }
        }
        right.next = null;
        left.next = rightDummy.next;
        return leftDummy.next;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<ListNode, Integer, ListNode> partition,
              int x, int[] nums, int ... expected) {
        ListNode res = partition.apply(ListNode.of(nums), x);
        assertArrayEquals(expected, res.toArray());
    }

    void test(int x, int[] nums, int ... expected) {
        PartitionList p = new PartitionList();
        test(p::partition, x, nums, expected);
        test(p::partition2, x, nums, expected);
    }

    @Test
    public void test1() {
        test(3, new int[]{1, 4, 3, 2, 5, 2}, 1, 2, 2, 4, 3, 5);
        test(3, new int[]{3, 1, 2}, 1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PartitionList");
    }
}
