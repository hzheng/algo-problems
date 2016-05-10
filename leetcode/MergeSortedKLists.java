import java.util.*;
import java.util.stream.Stream;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Merge k sorted linked lists and return it as a new list.
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) {
        val = x;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ListNode l = this; l != null; l = l.next) {
            sb.append("->" + l.val);
        }
        return sb.toString();
    }
}

public class MergeSortedKLists {
    // beats 12.37% (1.31% if not set PriorityQueue's size)
    // time complexity: O(N * log(K))
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        ListNode dummy = new ListNode(0);
        PriorityQueue<ListNode> headHeap
            = new PriorityQueue<>(lists.length, (a, b)-> a.val - b.val);
        for (ListNode node : lists) {
            if (node != null) {
                headHeap.offer(node);
            }
        }
        for (ListNode cur = dummy; !headHeap.isEmpty(); cur = cur.next) {
            ListNode max = headHeap.poll();
            cur.next = max;
            if (max.next != null) {
                headHeap.offer(max.next);
            }
        }
        return dummy.next;
    }

    // Time Limit Exceeded
    // time complexity: O(N * K ^ 2)
    public ListNode mergeKLists2(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        ListNode dummy = new ListNode(0);
        ListNode first = lists[0];
        for (int i = 1; i < lists.length; i++) {
            mergeTwoLists(dummy, first, lists[i]);
            first = dummy.next;
        }
        return dummy.next;
    }

    private void mergeTwoLists(ListNode cur, ListNode l1, ListNode l2) {
        for ( ; l1 != null || l2 != null; cur = cur.next) {
            if (l1 == null || (l2 != null && l1.val > l2.val)) {
                cur.next = l2;
                l2 = l2.next;
            } else {
                cur.next = l1;
                l1 = l1.next;
            }
        }
    }

    // Time Limit Exceeded
    // time complexity: O(N * K)
    public ListNode mergeKLists3(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;

        List<ListNode> listSet = Arrays.asList(lists);
        ListNode dummy = new ListNode(0);
        while (listSet.size() > 1) {
            List<ListNode> mergedSet = new ArrayList<>();
            int size = listSet.size();
            for (int i = 0; i < size - 1; i += 2) {
                mergeTwoLists(dummy, listSet.get(i), listSet.get(i + 1));
                mergedSet.add(dummy.next);
            }
            if (size % 2 == 1) {
                mergedSet.add(listSet.get(size - 1));
            }
            listSet = mergedSet;
        }
        return listSet.get(0);
    }

    ListNode createList(int[] l) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (int i : l) {
            cur.next = new ListNode(i);
            cur = cur.next;
        }
        return dummy.next;
    }

    int[] toArray(ListNode l) {
        List<Integer> list = new ArrayList<>();
        for (; l != null; l = l.next) {
            list.add(l.val);
        }
        return list.stream().mapToInt(i->i).toArray();
    }

    void test(Function<ListNode[], ListNode> merge,
              int[] expected, int[] ... listArray) {
        ListNode[] lists = Stream.of(listArray)
                           .map(l->createList(l)).toArray(ListNode[]::new);
        ListNode res = merge.apply(lists);
        assertArrayEquals(expected, toArray(res));
    }

    void test(int[] expected, int[] ... listArray) {
        MergeSortedKLists l = new MergeSortedKLists();
        test(l::mergeKLists, expected, listArray);
        test(l::mergeKLists2, expected, listArray);
        test(l::mergeKLists3, expected, listArray);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3, 4, 5, 6},
             new int[] {1, 3, 5}, new int[] {2, 4, 6});
        test(new int[] {2, 3, 4, 6}, new int[] {3}, new int[] {2, 4, 6});
        test(new int[] {2, 4, 6}, new int[] {}, new int[] {2, 4, 6});
        test(new int[] {2, 10}, new int[] {}, new int[] {2, 10});
        test(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
             new int[] {1, 3, 5}, new int[] {2, 4, 6},
             new int[] {8, 10}, new int[] {0, 7, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MergeSortedKLists");
    }
}
