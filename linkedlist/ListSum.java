import linkedlist.LinkedListNode;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 2.5(follow up):
 * You have two numbers represented by a linked list, where each node contains
 * a single digit. The digits are stored in forward order, such that the 1's
 * digit is at the end of the list. Write a function that adds the two numbers
 * and returns the sum as a linked list.
 */
public class ListSum {
    private class DigitProducer {
        private LinkedListNode node;
        private int len = 0;
        private LinkedListNode cur;
        private LinkedListNode lastCur;
        private int delay = 0;
        private int lastDelay;

        DigitProducer(LinkedListNode node) {
            cur = this.node = node;
            for (LinkedListNode n = node; n != null; n = n.next) {
                len++;
            }
        }

        void align(DigitProducer that) {
            if (len > that.len) {
                that.delay = len - that.len;
            } else if (len < that.len) {
                delay = that.len - len;
            }
        }

        boolean hasNext() {
            return (delay > 0) || (cur != null);
        }

        int next() {
            if (delay > 0) {
                delay--;
                return 0;
            }
            int digit = cur.data;
            cur = cur.next;
            return digit;
        }

        void mark() {
            lastDelay = delay;
            lastCur = cur;
        }

        void reset() {
            delay = lastDelay;
            cur = lastCur;
        }

        int nextSum(DigitProducer that) {
            int sum = next() + that.next();
            // look ahead for carry
            // record the current state
            mark();
            that.mark();

            while (hasNext()) {
                int lookaheadSum = next() + that.next();
                if (lookaheadSum > 9) {
                    sum++; // carry
                    break;
                } else if (lookaheadSum < 9) {
                    break;
                }
            }
            // restore the old state
            reset();
            that.reset();
            return sum;
        }
    }

    public LinkedListNode add(LinkedListNode n1, LinkedListNode n2) {
        if (n1 == null || n2 == null) return null;

        DigitProducer dp1 = new DigitProducer(n1);
        DigitProducer dp2 = new DigitProducer(n2);
        // align digits
        dp1.align(dp2);

        LinkedListNode sumHead = null;
        LinkedListNode sumCur = null;
        int sum = dp1.nextSum(dp2);
        if (sum < 10) {
            sumHead = sumCur = new LinkedListNode(sum);
        } else {
            sumHead = sumCur = new LinkedListNode(1);
            sumCur.next = new LinkedListNode(sum - 10);
            sumCur = sumHead.next;
        }

        while (dp1.hasNext()) {
            sumCur.next = new LinkedListNode(dp1.nextSum(dp2) % 10);
            sumCur = sumCur.next;
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
        test(new int[] {1, 3}, new int[] {5, 1}, new int[] {6, 4});
    }

    @Test
    public void test2() {
        test(new int[] {8, 3}, new int[] {5, 7}, new int[] {1, 4, 0});
    }

    @Test
    public void test3() {
        test(new int[] {4, 8, 3}, new int[] {5, 7}, new int[] {5, 4, 0});
    }

    @Test
    public void test4() {
        test(new int[] {5, 7}, new int[] {4, 8, 3}, new int[] {5, 4, 0});
    }

    @Test
    public void test5() {
        test(new int[] {1, 3}, new int[] {9, 8, 7},
             new int[] {1, 0, 0, 0});
    }

    @Test
    public void test6() {
        test(new int[] {1, 3}, new int[] {9, 9, 8, 7},
             new int[] {1, 0, 0, 0, 0});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ListSum");
    }
}
