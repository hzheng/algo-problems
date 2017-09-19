import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC328: https://leetcode.com/problems/odd-even-linked-list/
//
// Given a singly linked list, group all odd nodes together followed by the even
// nodes. Please note here we are talking about the node number and not the
// value in the nodes.
public class OddEvenList {
    // beats 3.44%(1 ms)
    public ListNode oddEvenList(ListNode head) {
        ListNode oddDummy = new ListNode(0);
        ListNode evenDummy = new ListNode(0);
        ListNode odd = oddDummy;
        ListNode even = evenDummy;
        for (ListNode cur = head; cur != null; ) {
            ListNode next = cur.next;
            odd = odd.next = cur;
            cur.next = null;
            if (next == null) break;

            cur = next;
            next = cur.next;
            even = even.next = cur;
            cur.next = null;
            cur = next;
        }
        odd.next = evenDummy.next;
        return oddDummy.next;
    }

    // beats 3.44%(1 ms)
    public ListNode oddEvenList2(ListNode head) {
        ListNode oddDummy = new ListNode(0);
        ListNode evenDummy = new ListNode(0);
        ListNode[] heads = new ListNode[] {oddDummy, evenDummy};
        ListNode cur = head;
        for (int i = 0; cur != null; i ^= 1) {
            ListNode next = cur.next;
            heads[i] = heads[i].next = cur;
            cur.next = null;
            cur = next;
        }
        heads[0].next = evenDummy.next;
        return oddDummy.next;
    }

    // Solution of Choice
    // https://leetcode.com/articles/odd-even-linked-list/
    // beats 3.44%(1 ms)
    public ListNode oddEvenList3(ListNode head) {
        if (head == null) return null;

        ListNode odd = head;
        ListNode evenHead = head.next;
        for (ListNode even = evenHead; even != null && even.next != null; ) {
            odd = odd.next = even.next;
            even = even.next = odd.next;
        }
        odd.next = evenHead;
        return head;
    }

    void test(Function<ListNode, ListNode> oddEvenList,
              int [] nums, int[] expected) {
        nums = nums.clone();
        ListNode res = oddEvenList.apply(ListNode.of(nums));
        int[] resArray = res == null ? new int[] {} : res.toArray();
        assertArrayEquals(expected, resArray);
    }

    void test(int[] nums, int[] expected) {
        OddEvenList o = new OddEvenList();
        test(o::oddEvenList, nums, expected);
        test(o::oddEvenList2, nums, expected);
        test(o::oddEvenList3, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {}, new int[] {});
        test(new int[] {1}, new int[] {1});
        test(new int[] {1, 2}, new int[] {1, 2});
        test(new int[] {1, 2, 3}, new int[] {1, 3, 2});
        test(new int[] {1, 2, 3, 4}, new int[] {1, 3, 2, 4});
        test(new int[] {1, 2, 3, 4, 5}, new int[] {1, 3, 5, 2, 4});
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {1, 3, 5, 2, 4, 6});
        test(new int[] {1, 2, 3, 4, 5, 6, 7}, new int[] {1, 3, 5, 7, 2, 4, 6});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("OddEvenList");
    }
}
