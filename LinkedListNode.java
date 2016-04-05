import java.util.List;
import java.util.ArrayList;

class LinkedListNode
{
    Object data;
    LinkedListNode next;

    public LinkedListNode(Object data) {
        this.data = data;
    }

    public LinkedListNode(Object[] array) {
        if (array == null || array.length == 0) return;

        data = array[0];
        LinkedListNode tail = this;
        for (int i = 1; i < array.length; i++) {
            tail.next = new LinkedListNode(array[i]);
            tail = tail.next;
        }
    }

    void appendToTail(Object data) {
        LinkedListNode n = this;
        for (; n.next != null; n = n.next) {}
        n.next = new LinkedListNode(data);
    }

    public boolean equals(LinkedListNode that) {
        if (that == null) return false;

        if (data == null && that.data == null) return true;

        if (data == null && that.data != null) return false;

        return data.equals(that.data);
    }

    public Object[] toArray() {
        List<Object> list = new ArrayList<Object>();
        for (LinkedListNode l = this; l != null; l = l.next) {
            list.add(l.data);
        }
        return list.toArray();
    }
}
