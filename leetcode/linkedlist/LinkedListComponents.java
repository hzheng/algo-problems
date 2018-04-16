import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC817: https://leetcode.com/problems/linked-list-components/
//
// Given head, the head node of a linked list containing unique integer values.
// Also given the list G, a subset of the values in the linked list.
// Return the number of connected components in G, where two values are
// connected if they appear consecutively in the linked list.
// Note:
// If N is the length of the linked list given by head, 1 <= N <= 10000.
// The value of each node in the linked list will be in the range [0, N - 1].
// 1 <= G.length <= 10000.
// G is a subset of all values in the linked list.
public class LinkedListComponents {
    // Hash Table + Set
    // beats %(75 ms for 57 tests)
    public int numComponents(ListNode head, int[] G) {
        Map<Integer, Integer> map = new HashMap<>();
        Map<Integer, Integer> rev = new HashMap<>();
        for (ListNode p = head;; p = p.next) {
            if (p.next == null) break;

            map.put(p.val, p.next.val);
            rev.put(p.next.val, p.val);
        }
        Set<Integer> set = new HashSet<>();
        for (int g : G) {
            set.add(g);
        }
        for (int components = 0;; components++) {
            Iterator<Integer> itr = set.iterator();
            if (!itr.hasNext()) return components;

            int cur = itr.next();
            itr.remove();
            for (int node = cur;; ) {
                Integer next = map.get(node);
                if (next == null) break;

                if (set.remove(next)) {
                    node = next;
                } else break;
            }
            for (int node = cur;; ) {
                Integer prev = rev.get(node);
                if (prev == null) break;

                if (set.remove(prev)) {
                    node = prev;
                } else break;
            }
        }
    }

    // Set
    // beats %(22 ms for 57 tests)
    public int numComponents2(ListNode head, int[] G) {
        Set<Integer> set = new HashSet<>();
        for (int g : G) {
            set.add(g);
        }
        int components = 0;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            if (set.contains(cur.val)
                && (cur.next == null || !set.contains(cur.next.val))) {
                components++;
            }
        }
        return components;
    }

    void test(int[] head, int[] G, int expected) {
        assertEquals(expected, numComponents(ListNode.of(head), G));
        assertEquals(expected, numComponents2(ListNode.of(head), G));
    }

    @Test
    public void test() {
        test(new int[] {0, 1, 2, 3, 4}, new int[] {0, 3, 1, 4}, 2);
        test(new int[] {0, 1, 2, 3}, new int[] {0, 1, 3}, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
