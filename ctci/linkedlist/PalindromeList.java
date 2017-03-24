import java.util.Stack;

import org.junit.Test;
import static org.junit.Assert.*;

import common.LinkedListNode;

/**
 * Cracking the Coding Interview(5ed) Problem 2.7:
 * Implement a function to check if a linked list is a palindrome,
 */
public class PalindromeList {
    // time complexity: O(N^2), space complexity: O(N)
    // simple and but not time-efficient
    public boolean isPalindromeRecursive1(LinkedListNode head) {
        if (head == null) return false;
        if (head.next == null) return true;

        return isPalindromeRecursive1(head, head.length());
    }

    public boolean isPalindromeRecursive1(LinkedListNode head, int len) {
        if (len < 2) return true;

        if (!isPalindromeRecursive1(head.next, len - 2)) {
            return false;
        }

        LinkedListNode tail = head;
        for (; --len > 0; tail = tail.next) {}
        return head.data == tail.data;
    }

    // time complexity: O(N), space complexity: O(N)
    // simple and time-efficient, only possible drawback is it modifies input
    // node in the process(although undoes modification at the end)
    public boolean isPalindromeRecursive2(LinkedListNode head) {
        if (head == null) return false;
        if (head.next == null) return true;

        LinkedListNode cur = head;
        for (; cur.next.next != null; cur = cur.next) {}

        LinkedListNode tail = cur.next;
        if (tail.data != head.data) return false;
        if (head.next == tail) return true;

        cur.next = null; // remove the tail
        boolean result = isPalindromeRecursive2(head.next);
        cur.next = tail; // put back the old tail
        return result;
    }

    // time complexity: O(N), space complexity: O(N)
    // time-efficient but not that simple
    public boolean isPalindromeRecursive3(LinkedListNode head) {
        if (head == null) return false;
        if (head.next == null) return true;

        return isPalindromeRecursive3(head, head.length()).value;
    }

    private class Result {
        boolean value;
        LinkedListNode next;
        Result(boolean value, LinkedListNode next) {
            this.value = value;
            this.next = next;
        }
    }

    public Result isPalindromeRecursive3(LinkedListNode head, int len) {
        if (len == 1) {
            return new Result(true, head.next);
        }

        if (len == 2) {
            return new Result(head.data == head.next.data, head.next.next);
        }

        Result result = isPalindromeRecursive3(head.next, len - 2);
        if (result.value) {
            result.value = (head.data == result.next.data);
            result.next = result.next.next;
        }
        return result;
    }

    // time complexity: O(N), space complexity: O(N)
    public boolean isPalindromeStack(LinkedListNode head) {
        if (head == null) return false;

        LinkedListNode slowPtr = head;
        LinkedListNode fastPtr = head.next;
        Stack<Integer> stack = new Stack<Integer>();
        while (fastPtr != null) {
            stack.push(slowPtr.data);
            if (fastPtr.next == null) {
                break;
            }
            fastPtr = fastPtr.next.next;
            slowPtr = slowPtr.next;
        }
        for (slowPtr = slowPtr.next; slowPtr != null; slowPtr = slowPtr.next) {
            if (slowPtr.data != stack.pop()) {
                return false;
            }
        }
        return true;
    }

    void test(int[] n, boolean expected) {
        LinkedListNode node = new LinkedListNode(n);
        assertEquals(expected, isPalindromeRecursive1(node));
        assertEquals(expected, isPalindromeRecursive2(node));
        assertEquals(expected, isPalindromeRecursive3(node));
        assertEquals(expected, isPalindromeStack(node));
    }

    @Test
    public void test1() {
        test(new int[] {1}, true);
    }

    @Test
    public void test2() {
        test(new int[] {1, 1}, true);
    }

    @Test
    public void test3() {
        test(new int[] {1, 2, 1}, true);
    }

    @Test
    public void test4() {
        test(new int[] {1, 2, 2, 1}, true);
    }

    @Test
    public void test5() {
        test(new int[] {1, 2, 3, 2, 1}, true);
    }

    @Test
    public void test6() {
        test(new int[] {0, 1, 2, 3, 3, 2, 1, 0}, true);
    }

    @Test
    public void test7() {
        test(new int[] {0, 1, 2, 3, 4, 3, 2, 1, 0}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PalindromeList");
    }
}
