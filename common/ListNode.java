package common;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    public static ListNode of(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int val : vals) {
            cur.next = new ListNode(val);
            cur = cur.next;
        }
        return dummy.next;
    }

    public static ListNode byVals(int... vals) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        Map<Integer, ListNode> map = new HashMap<>();
        for (int val : vals) {
            if (!map.containsKey(val)) {
                map.put(val, new ListNode(val));
            }
            cur.next = map.get(val);
            cur = cur.next;
        }
        return dummy.next;
    }

    public int[] toArray() {
        List<Integer> list = new ArrayList<>();
        for (ListNode l = this; l != null; l = l.next) {
            list.add(l.val);
        }
        return list.stream().mapToInt(i->i).toArray();
    }

    public String toString() {
        return Arrays.toString(toArray());
    }
}
