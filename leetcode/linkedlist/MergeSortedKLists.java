import java.util.*;
import java.util.stream.Stream;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.ListNode;

// LC023: https://leetcode.com/problems/merge-k-sorted-lists/
//
// Merge k sorted linked lists and return it as a new list.
public class MergeSortedKLists {
    // Solution of Choice
    // Heap
    // time complexity: O(N * log(K))
    // beats 81.84%(11 ms for 131 tests)
    public ListNode mergeKLists(ListNode[] lists) {
        int k = lists.length;
        // PriorityQueue<ListNode> headHeap
        // = new PriorityQueue<>(lists.length, (a, b)-> a.val - b.val);
        PriorityQueue<ListNode> pq = new PriorityQueue<>(k + 1, new Comparator<ListNode>() {
            public int compare(ListNode a, ListNode b) {
                return a.val - b.val;
            }
        });
        for (ListNode node : lists) {
            if (node != null) {
                pq.offer(node);
            }
        }
        ListNode dummy = new ListNode(0);
        for (ListNode cur = dummy; !pq.isEmpty(); cur = cur.next) {
            ListNode min = pq.poll();
            cur.next = min;
            if (min.next != null) {
                pq.offer(min.next);
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
        for (; l1 != null || l2 != null; cur = cur.next) {
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

    // Divide & Conquer + Recursion
    // time complexity: O(N * log(K))
    // beats 99.98%(7 ms for 131 tests)
    public ListNode mergeKLists4(ListNode[] lists) {
        return (lists.length == 0) ? null : partion(lists, 0, lists.length - 1);
    }

    private ListNode partion(ListNode[] lists, int start, int end) {
        if (start == end) return lists[start];

        int mid = (start + end) >>> 1;
        return merge(partion(lists, start, mid), partion(lists, mid + 1, end));
    }

    private ListNode merge(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        if (l1.val < l2.val) {
            l1.next = merge(l1.next, l2);
            return l1;
        } else {
            l2.next = merge(l1, l2.next);
            return l2;
        }
    }

    void test(Function<ListNode[], ListNode> merge, int[] expected, int[]... listArray) {
        ListNode[] lists = Stream.of(listArray).map(l -> ListNode.of(l)).toArray(ListNode[]::new);
        ListNode res = merge.apply(lists);
        if (expected.length == 0) {
            assertNull(res);
        } else {
            assertArrayEquals(expected, res.toArray());
        }
    }

    void test(int[] expected, int[]... listArray) {
        MergeSortedKLists l = new MergeSortedKLists();
        test(l::mergeKLists, expected, listArray);
        test(l::mergeKLists2, expected, listArray);
        test(l::mergeKLists3, expected, listArray);
        test(l::mergeKLists4, expected, listArray);
    }

    @Test
    public void test1() {
        test(new int[] {}, new int[0]);
        test(new int[] {}, new int[] {});
        test(new int[] {}, new int[] {}, new int[] {});
        test(new int[] {1}, new int[] {}, new int[] {1});
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[] {1, 3, 5}, new int[] {2, 4, 6});
        test(new int[] {2, 3, 4, 6}, new int[] {3}, new int[] {2, 4, 6});
        test(new int[] {2, 4, 6}, new int[] {}, new int[] {2, 4, 6});
        test(new int[] {2, 10}, new int[] {}, new int[] {2, 10});
        test(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, new int[] {1, 3, 5}, new int[] {2, 4, 6},
             new int[] {8, 10}, new int[] {0, 7, 9});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
