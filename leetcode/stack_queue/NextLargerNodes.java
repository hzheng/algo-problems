import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

import common.ListNode;

// LC1019: https://leetcode.com/problems/next-greater-node-in-linked-list/
//
// We are given a linked list with head as the first node.  Let's number the nodes in the list:
// node_1, node_2, node_3, ... etc. Each node may have a next larger value: for node_i,
// next_larger(node_i) is the node_j.val such that j > i, node_j.val > node_i.val, and j is the
// smallest possible choice.  If such a j does not exist, the next larger value is 0.
// Return an array of integers answer, where answer[i] = next_larger(node_{i+1}).
public class NextLargerNodes {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 42 ms(50.13%), 39.8 MB(100%) for 76 tests
    public int[] nextLargerNodes(ListNode head) {
        List<Integer> list = new ArrayList<>();
        for (ListNode cur = head; cur != null; cur = cur.next) {
            list.add(cur.val);
        }
        int[] res = new int[list.size()];
        Stack<Integer> stack = new Stack<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            int cur = list.get(i);
            while (!stack.isEmpty() && stack.peek() <= cur) {
                stack.pop();
            }
            if (!stack.isEmpty()) {
                res[i] = stack.peek();
            }
            stack.push(cur);
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 35 ms(75.77%), 39.8 MB(100%) for 76 tests
    public int[] nextLargerNodes2(ListNode head) {
        List<Integer> list = new ArrayList<>();
        for (ListNode cur = head; cur != null; cur = cur.next) {
            list.add(cur.val);
        }
        int[] res = new int[list.size()];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0, n = list.size(); i < n; i++) {
            for (int cur = list.get(i); !stack.isEmpty() && list.get(stack.peek()) < cur; ) {
                res[stack.pop()] = cur;
            }
            stack.push(i);
        }
        return res;
    }

    void test(int[] head, int[] expected) {
        assertArrayEquals(expected, nextLargerNodes(ListNode.of(head)));
        assertArrayEquals(expected, nextLargerNodes2(ListNode.of(head)));
    }

    @Test
    public void test() {
        test(new int[]{2, 1, 5}, new int[]{5, 5, 0});
        test(new int[]{2, 7, 4, 3, 5}, new int[]{7, 0, 5, 5, 0});
        test(new int[]{1, 7, 5, 1, 9, 2, 5, 1}, new int[]{7, 9, 9, 9, 0, 5, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
