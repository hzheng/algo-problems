import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

import common.ListNode;

// LC382: https://leetcode.com/problems/linked-list-random-node/
//
// Given a singly linked list, return a random node's value from the linked
// list. Each node must have the same probability of being chosen.
// Follow up:
// What if the linked list is extremely large and its length is unknown to you?
// Could you solve this efficiently without using extra space?
public class RandomNode {
    private Random rand = new Random();

    // Solution of Choice
    // beats 37.53%(145 ms)
    public int getRandom(ListNode head) {
        ListNode chosen = null;
        int i = 0;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (rand.nextInt(++i) == 0) {
                chosen = cur;
            }
        }
        return chosen.val;
    }

    void test(int[] nums) {
        ListNode head = ListNode.of(nums);
        int[] counts = new int[nums.length];
        int samplingSize = 1000000;
        for (int i = 0; i < samplingSize; i++) {
            counts[getRandom(head) - 1]++;
        }
        for (int i = 0; i < nums.length; i++) {
            int count = counts[i];
            int expected = samplingSize / nums.length;
            int diff = samplingSize / 100;
            assertTrue("diff is out of range: " + count,
                       count <= expected + diff && count >= expected - diff);
        }
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 4});
        test(new int[] {1, 2, 3, 4, 5, 6});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RandomNode");
    }
}
