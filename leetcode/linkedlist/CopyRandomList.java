import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.RandomListNode;

// LC138: https://leetcode.com/problems/copy-list-with-random-pointer/
//
// A linked list is given such that each node contains an additional random
// pointer which could point to any node in the list or null.
// Return a deep copy of the list.
public class CopyRandomList {
    // Solution of Choice
    // Hashtable
    // time complexity: O(N), space complexity: O(N)
    // beats 63.15%(3 ms for 12 tests)
    public RandomListNode copyRandomList(RandomListNode head) {
        Map<RandomListNode, RandomListNode> map = new HashMap<>();
        RandomListNode dummy = new RandomListNode(0);
        for (RandomListNode cur = head, copy = dummy; cur != null; cur = cur.next) {
            copy = copy.next = new RandomListNode(cur.label);
            map.put(cur, copy);
        }
        for (RandomListNode cur = head; cur != null; cur = cur.next) {
            map.get(cur).random = map.get(cur.random);
        }
        return dummy.next;
    }

    // Hashtable
    // one pass(or loop)
    // time complexity: O(N), space complexity: O(N)
    // beats 63.15%(3 ms for 12 tests)
    public RandomListNode copyRandomList2(RandomListNode head) {
        Map<RandomListNode, RandomListNode> map = new HashMap<>();
        RandomListNode dummy = new RandomListNode(0);
        for (RandomListNode cur = head, copy = dummy; cur != null; cur = cur.next) {
            copy = copy.next = new RandomListNode(cur.label);
            if (cur.random != null && !map.containsKey(cur.random)) {
                map.put(cur.random, new RandomListNode(cur.random.label));
            }
            copy.random = map.get(cur.random);
        }
        return dummy.next;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // Extra space complexity: O(1) (con: temporarily modify input)
    // beats 99.76%(1 ms for 12 tests)
    public RandomListNode copyRandomList3(RandomListNode head) {
        if (head == null) return null;

        // copy next pointers
        for (RandomListNode n = head, cloned; n != null; n = cloned.next) {
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
            RandomListNode cloned = n.next;
            n.next = cloned.next;
            if (n.next != null) {
                cloned.next = n.next.next;
            }
        }
        return clonedHead;
    }

    // Hashtable
    // time complexity: O(N), space complexity: O(N)
    // beats 38.40%(4 ms for 12 tests)
    public RandomListNode copyRandomList4(RandomListNode head) {
        Map<RandomListNode, RandomListNode> map = new HashMap<>();
        for (RandomListNode cur = head; cur != null; cur = cur.next) {
            map.put(cur, new RandomListNode(cur.label));
        }
        for (RandomListNode cur = head; cur != null; cur = cur.next) {
            map.get(cur).next = map.get(cur.next);
            map.get(cur).random = map.get(cur.random);
        }
        return map.get(head);
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // beats 38.40%(4 ms for 12 tests)
    public RandomListNode copyRandomList5(RandomListNode head) {
        return copyRandomList5(head, new HashMap<>());
    }

    private RandomListNode copyRandomList5(RandomListNode head,
                                           Map<RandomListNode, RandomListNode> map) {
        if (head == null) return null;

        if (map.containsKey(head)) return map.get(head);

        RandomListNode cloned = new RandomListNode(head.label);
        map.put(head, cloned);
        cloned.next = copyRandomList5(head.next, map);
        cloned.random = copyRandomList5(head.random, map);
        return cloned;
    }

    void test(Function<RandomListNode, RandomListNode> copy, int[] nums) {
        nums = nums.clone();
        int[] res = copy.apply(RandomListNode.of(nums)).toArray();
        assertArrayEquals(nums, res);
    }

    void test(int[] nums) {
        CopyRandomList c = new CopyRandomList();
        test(c::copyRandomList, nums);
        test(c::copyRandomList2, nums);
        test(c::copyRandomList3, nums);
        test(c::copyRandomList4, nums);
        test(c::copyRandomList5, nums);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1});
        test(new int[] {1, 1, 2});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
