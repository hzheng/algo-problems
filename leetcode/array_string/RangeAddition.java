import org.junit.Test;
import static org.junit.Assert.*;

// LC370: https://leetcode.com/problems/range-addition/
//
// Assume you have an array of length n initialized with all 0's and are given k
// update operations. Each operation is represented as a triplet:
// [startIndex, endIndex, inc]
// Return the modified array after all k operations were executed.
public class RangeAddition {
    // Brute Force
    // time complexity: O(N * K), space complexity: O(1)
    // Time Limit Exceeded
    public int[] getModifiedArray(int length, int[][] updates) {
        int[] res = new int[length];
        for (int[] update : updates) {
            for (int i = update[0]; i <= update[1]; i++) {
                res[i] += update[2];
            }
        }
        return res;
    }

    private static class Node {
        int start;
        int end;
        int inc;
        Node next;
        Node(int start, int end, int inc) {
            this.start = start;
            this.end = end;
            this.inc = inc;
        }
    }

    // LinkedList
    // time complexity: O(N * K), space complexity: O(N)
    // Time Limit Exceeded
    public int[] getModifiedArray2(int length, int[][] updates) {
        int[] res = new int[length];
        Node head = new Node(0, 0, 0);
        for (int[] update : updates) {
            add(head, update[0], update[1], update[2]);
        }
        for (Node node = head.next; node != null; node = node.next) {
            for (int i = node.start; i <= node.end; i++) {
                res[i] += node.inc;
            }
        }
        return res;
    }

    private void add(Node head, int start, int end, int inc) {
        if (inc == 0) return;

        Node prev = head;
        Node node = head.next;
        for ( ; node != null; prev = node, node = node.next) {
            if (start < node.start) {
                prev.next = new Node(start, Math.min(end, node.start - 1), inc);
                prev.next.next = node;
                if (end < node.start) return;

                start = node.start;
                continue;
            }
            if (start > node.end) {
                int newEnd = node.next == null ? end : Math.min(end, node.next.start - 1);
                Node next = node.next;
                node.next = new Node(start, newEnd, inc);
                node.next.next = next;
                if (newEnd == end) return;

                node = node.next;
                start = node.next.start;
                continue;
            }
            prev = prev.next = new Node(node.start, start - 1, node.inc);
            if (end < node.end) {
                prev = prev.next = new Node(start, end, node.inc + inc);
                prev.next = node;
                node.start = end + 1;
                return;
            }
            node.start = start;
            node.inc += inc;
            prev.next = node;
            if (end == node.end) return;

            start = node.end + 1;
        }
        prev.next = new Node(start, end, inc);
    }

    // Trick
    // time complexity: O(N + K), space complexity: O(1)
    // beats 34.73%(3 ms for 18 tests)
    public int[] getModifiedArray3(int length, int[][] updates) {
        int[] res = new int[length];
        for (int[] update : updates) {
            if (update[0] < length) {
                res[update[0]] += update[2];
            }
            if (update[1] + 1 < length) {
                res[update[1] + 1] -= update[2];
            }
        }
        for (int i = 1; i < length; i++) {
            res[i] += res[i - 1];
        }
        return res;
    }

    void test(int length, int[][] updates, int[] expected) {
        assertArrayEquals(expected, getModifiedArray(length, updates));
        assertArrayEquals(expected, getModifiedArray2(length, updates));
        assertArrayEquals(expected, getModifiedArray3(length, updates));
    }

    @Test
    public void test() {
        test(5, new int[][] {{1,  3,  2}, {2,  4,  3}, {1, 2, 1}, {1, 2, 1}},
         new int[] {0, 4, 7, 5, 3});
        test(5, new int[][] {{1,  3,  2}, {2,  4,  3}}, new int[] {0, 2, 5, 5, 3});
        test(5, new int[][] {{1,  3,  2}, {2,  4,  3}, {0,  2, -2}}, new int[] {-2, 0, 3, 5, 3});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RangeAddition");
    }
}
