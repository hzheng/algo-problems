import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

import common.ListNode;

// LC1171: https://leetcode.com/problems/remove-zero-sum-consecutive-nodes-from-linked-list/
//
// Given the head of a linked list, we repeatedly delete consecutive sequences of nodes that sum to
// 0 until there are no such sequences. After doing so, return the head of the final linked list.
// You may return any such answer.
//
// Constraints:
// The given linked list will contain between 1 and 1000 nodes.
// Each node in the linked list has -1000 <= node.val <= 1000.
public class RemoveZeroSumSublists {
    // Hash Table + Stack
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(31.95%), 38.6 MB(80.53%) for 105 tests
    public ListNode removeZeroSumSublists(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        Map<Integer, ListNode> sums = new HashMap<>();
        sums.put(0, dummy);
        Stack<ListNode> stack = new Stack<>();
        stack.push(dummy);
        int sum = 0;
        for (ListNode cur = dummy.next; cur != null; cur = cur.next) {
            sum += cur.val;
            ListNode old = sums.get(sum);
            if (old == null) {
                stack.push(cur);
                sums.put(sum, cur);
            } else {
                sum -= cur.val;
                for (; stack.peek() != old; sum -= stack.pop().val) {
                    sums.remove(sum);
                }
                old.next = cur.next;
            }
        }
        return dummy.next;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(82.20%), 38.4 MB(89.52%) for 105 tests
    public ListNode removeZeroSumSublists2(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        Map<Integer, ListNode> sums = new HashMap<>();
        int sum = 0;
        for (ListNode cur = dummy; cur != null; cur = cur.next) {
            sum += cur.val;
            ListNode old = sums.get(sum);
            if (old == null) {
                sums.put(sum, cur);
            } else {
                cur = old.next;
                for (int s = sum + cur.val; s != sum; cur = cur.next, s += cur.val) {
                    sums.remove(s);
                }
                old.next = cur.next;
            }
        }
        return dummy.next;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(82.20%), 38.4 MB(89.52%) for 105 tests
    public ListNode removeZeroSumSublists3(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        Map<Integer, ListNode> sums = new HashMap<>();
        int sum = 0;
        for (ListNode cur = dummy; cur != null; cur = cur.next) {
            sum += cur.val;
            ListNode old = sums.get(sum);
            if (old == null) {
                sums.put(sum, cur);
            } else {
                int s = sum;
                for (ListNode prev = old.next; prev != cur; prev = prev.next) {
                    s += prev.val;
                    sums.remove(s);
                }
                old.next = cur.next;
            }
        }
        return dummy.next;
    }

    // Two Passes
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 38.9 MB(47.59%) for 105 tests
    public ListNode removeZeroSumSublists4(ListNode head) {
        int sum = 0;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        Map<Integer, ListNode> latestSums = new HashMap<>();
        latestSums.put(0, dummy);
        for (ListNode cur = dummy; cur != null; cur = cur.next) {
            sum += cur.val;
            latestSums.put(sum, cur);
        }
        sum = 0;
        for (ListNode cur = dummy; cur != null; cur = cur.next) {
            sum += cur.val;
            cur.next = latestSums.get(sum).next;
        }
        return dummy.next;
    }

    // Recursion
    // time complexity: O(N^2), space complexity: O(N)
    // 1 ms(100.00%), 38.9 MB(47.59%) for 105 tests
    public ListNode removeZeroSumSublists5(ListNode head) {
        return removeZeroSumSublists(new ListNode(0), head);
    }

    private ListNode removeZeroSumSublists(ListNode dummy, ListNode head) {
        dummy.next = head;
        int sum = 0;
        for (ListNode cur = head; cur != null; cur = cur.next) {
            sum += cur.val;
            if (sum == 0) {
                dummy.next = cur.next;
            }
        }
        ListNode res = dummy.next;
        if (res != null) {
            res.next = removeZeroSumSublists(dummy, res.next);
        }
        return res;
    }

    // Recursion
    // time complexity: O(N^2), space complexity: O(N)
    // 2 ms(82.20%), 39 MB(30.12%) for 105 tests
    public ListNode removeZeroSumSublists6(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        for (ListNode cur = dummy; cur != null; cur = cur.next) {
            int sum = 0;
            for (ListNode next = cur.next; next != null; next = next.next) {
                sum += next.val;
                if (sum == 0) {
                    cur.next = next.next;
                }
            }
        }
        return dummy.next;
    }

    private void test(Function<ListNode, ListNode> removeZeroSumSublists, int[] list,
                      Integer[]... expected) {
        ListNode l = removeZeroSumSublists.apply(ListNode.of(list));
        if (expected.length == 0) {
            assertNull(l);
        } else {
            List<List<Integer>> expectedList =
                    Arrays.stream(expected).map(Arrays::asList).collect(Collectors.toList());
            assertThat(l == null ? null : l.toList(), in(expectedList));
        }
    }

    private void test(int[] list, Integer[]... expected) {
        RemoveZeroSumSublists r = new RemoveZeroSumSublists();
        test(r::removeZeroSumSublists, list, expected);
        test(r::removeZeroSumSublists2, list, expected);
        test(r::removeZeroSumSublists3, list, expected);
        test(r::removeZeroSumSublists4, list, expected);
        test(r::removeZeroSumSublists5, list, expected);
        test(r::removeZeroSumSublists6, list, expected);
    }

    @Test public void test() {
        test(new int[] {1, 3, 2, -3, -2, 5, 5, -5, 1}, new Integer[] {1, 5, 1});
        test(new int[] {1, 2, -3, 3, 1}, new Integer[] {3, 1}, new Integer[] {1, 2, 1});
        test(new int[] {1, 2, 3, -3, 4}, new Integer[] {1, 2, 4});
        test(new int[] {1, 2, 3, -3, -2}, new Integer[] {1});
        test(new int[] {0, 0});
        test(new int[] {1, -1});
        test(new int[] {2, 2, -2, 1, -1, -1}, new Integer[] {2, -1});
        test(new int[] {-1, 1, 0, 1}, new Integer[] {1});
        test(new int[] {1, 3, 2, -3, -2, 5, 100, -100, 1}, new Integer[] {1, 5, 1},
             new Integer[] {1, 3, 2, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
