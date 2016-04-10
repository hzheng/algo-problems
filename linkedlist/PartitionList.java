import org.junit.Test;
import static org.junit.Assert.*;

import linkedlist.LinkedListNode;

/**
 * Cracking the Coding Interview(5ed) Problem 2.4:
 * Partition a linked list around a value x, such that all nodes less than x
 * come before all nodes greater than or equal to x.
 */
 public class PartitionList {
     public LinkedListNode partition(LinkedListNode node, int x) {
         if (node == null) return null;

         LinkedListNode beforeTail = null;
         LinkedListNode beforeHead = null;
         LinkedListNode afterTail = null;
         LinkedListNode afterHead = null;
         while (node != null) {
             LinkedListNode next = node.next;
             node.next = null;
             if (node.data < x) {
                 if (beforeHead == null) {
                     beforeHead = beforeTail = node;
                 } else {
                     beforeTail.next = node;
                     beforeTail = node;
                 }
             } else {
                 if (afterHead == null) {
                     afterHead = afterTail = node;
                 } else {
                     afterTail.next = node;
                     afterTail = node;
                 }
             }
             node = next;
         }
         if (beforeHead == null) {
             return afterHead;
         }
         beforeTail.next = afterHead;
         return beforeHead;
     }

     public LinkedListNode partition2(LinkedListNode node, int x) {
         if (node == null) return null;

         LinkedListNode beforeHead = null;
         LinkedListNode afterHead = null;
         while (node != null) {
             LinkedListNode next = node.next;
             if (node.data < x) {
                 node.next = beforeHead;
                 beforeHead = node;
             } else {
                 node.next = afterHead;
                 afterHead = node;
             }
             node = next;
         }
         if (beforeHead == null) {
             return afterHead;
         }

         LinkedListNode head = beforeHead;
         while (beforeHead.next != null) {
             beforeHead = beforeHead.next;
         }
         beforeHead.next = afterHead;
         return head;
     }

    void test(int[] before, int x, int[] expected) {
        LinkedListNode list = new LinkedListNode(before);
        list = partition(list, x);
        assertArrayEquals(expected, list.toArray());
    }

    void test_(int[] before, int x, int[] expected) {
        LinkedListNode list = new LinkedListNode(before);
        list = partition2(list, x);
        assertArrayEquals(expected, list.toArray());
    }

    @Test
    public void test1() {
        test(new int[] {1, 10, 3, 5, 2}, 4, new int[] {1, 3, 2, 10, 5});
    }

    @Test
    public void test1_() {
        test_(new int[] {1, 10, 3, 5, 2}, 4, new int[] {2, 3, 1, 5, 10});
    }

    @Test
    public void test2() {
        test(new int[] {10, 8, 3, 5, 2}, 4, new int[] {3, 2, 10, 8, 5});
    }

    @Test
    public void test2_() {
        test_(new int[] {10, 8, 3, 5, 2}, 4, new int[] {2, 3, 5, 8, 10});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PartitionList");
    }
 }
