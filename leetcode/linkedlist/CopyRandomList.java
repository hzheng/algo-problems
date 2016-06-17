import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.RandomListNode;

// https://leetcode.com/problems/copy-list-with-random-pointer/
// A linked list is given such that each node contains an additional random
// pointer which could point to any node in the list or null.
// Return a deep copy of the list.
public class CopyRandomList {
    // space complexity: O(N)
    // beats 44.16%
    public RandomListNode copyRandomList(RandomListNode head) {
        if (head == null) return null;

        Map<RandomListNode, RandomListNode> map = new HashMap<>();
        RandomListNode dummy = new RandomListNode(0);
        RandomListNode last = dummy;
        for (RandomListNode n = head; n != null; n = n.next, last = last.next) {
            RandomListNode cloned = new RandomListNode(n.label);
            map.put(n, cloned);
            last.next = cloned;
        }
        for (RandomListNode n = head; n != null; n = n.next) {
            if (n.random != null) {
                map.get(n).random = map.get(n.random);
            }
        }
        return dummy.next;
    }

    // one pass(or loop)
    // space complexity: O(N)
    // beats 14.06%
    public RandomListNode copyRandomList2(RandomListNode head) {
        if (head == null) return null;

        Map<RandomListNode, RandomListNode> map = new HashMap<>();
        RandomListNode dummy = new RandomListNode(0);
        RandomListNode last = dummy;
        RandomListNode cloned;
        for (RandomListNode n = head; n != null; n = n.next, last = last.next) {
            if (map.containsKey(n)) {
                cloned = map.get(n);
            } else {
                cloned = new RandomListNode(n.label);
                map.put(n, cloned);
            }
            last.next = cloned;
            if (n.random != null) {
                if (map.containsKey(n.random)) {
                    cloned.random = map.get(n.random);
                } else {
                    cloned.random = new RandomListNode(n.random.label);
                    map.put(n.random, cloned.random);
                }
            }
        }
        return dummy.next;
    }

    // space complexity: O(2)
    // beats 66.29%
    public RandomListNode copyRandomList3(RandomListNode head) {
        if (head == null) return null;

        RandomListNode cloned = null;
        // copy next pointers
        for (RandomListNode n = head; n != null; n = cloned.next) {
            cloned = new RandomListNode(n.label);
            cloned.next = n.next;
            n.next = cloned;
        }
        // copy random pointers
        for (RandomListNode n = head; n != null; n = n.next.next) {
            if (n.random != null) {
                n.next.random = n.random.next;
            }
        }
        // splice list
        RandomListNode clonedHead = head.next;
        for (RandomListNode n = head; n != null; n = n.next) {
            cloned = n.next;
            n.next = cloned.next;
            if (n.next != null) {
                cloned.next = n.next.next;
            }
        }
        return clonedHead;
    }

    void test(Function<RandomListNode, RandomListNode> copy, int [] nums) {
        nums = nums.clone();
        int[] res = copy.apply(RandomListNode.of(nums)).toArray();
        assertArrayEquals(nums, res);
    }

    void test(int[] nums) {
        CopyRandomList c = new CopyRandomList();
        test(c::copyRandomList, nums);
        test(c::copyRandomList2, nums);
        test(c::copyRandomList3, nums);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1, 2});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CopyRandomList");
    }
}
