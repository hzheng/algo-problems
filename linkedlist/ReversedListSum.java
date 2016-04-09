import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 2.5:
 * You have two numbers represented by a linked list, where each node contains
 * a single digit. The digits are stored in reverse order, such that the 1's
 * digit is at the head of the list. Write a function that adds the two numbers
 * and returns the sum as a linked list.
 */
public class ReversedListSum {
    public LinkedListNode add(LinkedListNode n1, LinkedListNode n2) {
        if (n1 == null || n2 == null) return null;

        LinkedListNode sumHead = null;
        LinkedListNode sumTail = null;
        LinkedListNode d1 = n1;
        LinkedListNode d2 = n2;
        int sum = 0;
        int carry = 0;
        for (; d1 != null && d2 != null; d1 = d1.next, d2 = d2.next) {
            sum = d1.data + d2.data + carry;
            if (sum > 9) {
                sum -= 10;
                carry = 1;
            } else {
                carry = 0;
            }
            LinkedListNode newNode = new LinkedListNode(sum);
            if (sumHead == null) {
                sumHead = sumTail = newNode;
            } else {
                sumTail.next = newNode;
                sumTail = newNode;
            }
        }
        // d1 and d2 are exhausted at the same time
        if (d1 == null && d2 == null) {
            if (carry > 0) {
                sumTail.next = new LinkedListNode(carry);
            }
            return sumHead;
        }

        // one of d1 and d2 is null, one is NOT null
        if (d1 == null) {
            d1 = d2;
        }
        for (; d1 != null; d1 = d1.next) {
            sum = d1.data + carry;
            if (sum > 9) {
                sum -= 10;
                carry = 1;
            } else {
                carry = 0;
            }
            sumTail = sumTail.next = new LinkedListNode(sum);
        }
        if (carry > 0) {
            sumTail.next = new LinkedListNode(carry);
        }
        return sumHead;
    }

    void test(int[] n1, int[] n2, int[] expected) {
        LinkedListNode node1 = new LinkedListNode(n1);
        LinkedListNode node2 = new LinkedListNode(n2);
        assertArrayEquals(expected, add(node1, node2).toArray());
    }

    @Test
    public void test1() {
        test(new int[] {3, 1}, new int[] {1, 5}, new int[] {4, 6});
    }

    @Test
    public void test2() {
        test(new int[] {3, 8}, new int[] {7, 5}, new int[] {0, 4, 1});
    }

    @Test
    public void test3() {
        test(new int[] {3, 8, 4}, new int[] {7, 5}, new int[] {0, 4, 5});
    }

    @Test
    public void test4() {
        test(new int[] {7, 5}, new int[] {3, 8, 4}, new int[] {0, 4, 5});
    }

    @Test
    public void test5() {
        test(new int[] {3, 1}, new int[] {7, 8, 9},
             new int[] {0, 0, 0, 1});
    }

    @Test
    public void test6() {
        test(new int[] {3, 1}, new int[] {7, 8, 9, 9},
             new int[] {0, 0, 0, 0, 1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReversedListSum");
    }
}
