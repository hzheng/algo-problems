import java.util.List;
import java.util.ArrayList;

class LinkedListNode
{
    int data;
    LinkedListNode next;

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

    void appendToTail(int data) {
        LinkedListNode n = this;
        for (; n.next != null; n = n.next) {}
        n.next = new LinkedListNode(data);
    }

    public int[] toArray() {
        List<Integer> list = new ArrayList<Integer>();
        for (LinkedListNode l = this; l != null; l = l.next) {
            list.add(l.data);
        }
        return list.stream().mapToInt(i->i).toArray();
    }

    public static void main(String[] args) {
    }
}
