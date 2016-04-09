package linkedlist;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class LinkedListNode
{
    public int data;
    public LinkedListNode next;

    public LinkedListNode(int data) {
        this.data = data;
    }

    public LinkedListNode(int[] array) {
        if (array == null || array.length == 0) return;

        data = array[0];
        LinkedListNode tail = this;
        for (int i = 1; i < array.length; i++) {
            tail.next = new LinkedListNode(array[i]);
            tail = tail.next;
        }
    }

    /**
     * Create <code>LinkedListNode</code> from an interger array, with same
     * value representing the same node.
     * This is used to create a linked list that may have loop
     */
    public static LinkedListNode uniqueList(int[] array) {
        if (array == null || array.length == 0) return null;

        Map<Integer, LinkedListNode> map
            = new HashMap<Integer, LinkedListNode>();
        LinkedListNode head = new LinkedListNode(array[0]);
        map.put(head.data, head);

        LinkedListNode tail = head;
        for (int i = 1; i < array.length; i++) {
            int val = array[i];
            LinkedListNode node = map.get(val);
            if (node == null) {
                node = new LinkedListNode(val);
                map.put(val, node);
            }
            tail.next = node;
            tail = tail.next;
        }
        return head;
    }

    void appendToTail(int data) {
        LinkedListNode n = this;
        for (; n.next != null; n = n.next) {}
        n.next = new LinkedListNode(data);
    }

    public int length() {
        int i = 0;
        for (LinkedListNode l = this; l != null; l = l.next) {
            ++i;
        }
        return i;
    }

    public int[] toArray() {
        List<Integer> list = new ArrayList<Integer>();
        for (LinkedListNode l = this; l != null; l = l.next) {
            list.add(l.data);
        }
        return list.stream().mapToInt(i->i).toArray();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        for (LinkedListNode l = this; l != null; l = l.next) {
            if (l != this) {
                sb.append(",");
            }
            sb.append(l.data);
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
    }
}
