package common;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class RandomListNode {
    public int label;
    public RandomListNode next;
    public RandomListNode random;

    public RandomListNode(int x) {
        this.label = x;
    }

    public static RandomListNode of(int ... vals) {
        RandomListNode dummy = new RandomListNode(0);
        RandomListNode cur = dummy;
        for (int val : vals) {
            cur.next = new RandomListNode(val);
            cur = cur.next;
        }
        return dummy.next;
    }

    public int[] toArray() {
        List<Integer> list = new ArrayList<>();
        for (RandomListNode l = this; l != null; l = l.next) {
            list.add(l.label);
        }
        return list.stream().mapToInt(i->i).toArray();
    }

    public String toString() {
        return Arrays.toString(toArray());
    }
}
