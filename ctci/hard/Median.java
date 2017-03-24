import java.util.PriorityQueue;
import java.util.Comparator;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.9:
 * Numbers are randomly generated and passed to a method. Find and maintain the
 * median value as new values are generated.
 */
public class Median {
    private static PriorityQueue<Integer> maxHeap
        = new PriorityQueue<Integer>((a, b) -> b - a);
    private static PriorityQueue<Integer> minHeap
        = new PriorityQueue<Integer>((a, b) -> a - b);

    public static void reset() {
        maxHeap.clear();
        minHeap.clear();
    }

    public static void addNew(int n) {
        if (maxHeap.size() == minHeap.size()) {
            Integer minTop = minHeap.peek();
            if (minTop != null && minTop < n) {
                maxHeap.offer(minHeap.poll());
                minHeap.offer(n);
            } else {
                maxHeap.offer(n);
            }
        } else { // |maxHeap| > |minHeap|
            if (n < maxHeap.peek()) {
                minHeap.offer(maxHeap.poll());
                maxHeap.offer(n);
            } else {
                minHeap.offer(n);
            }
        }
    }

    public static double getMedian() {
        int maxSize = maxHeap.size();
        if (maxSize == 0) return 0;

        if (maxSize > minHeap.size()) {
            return maxHeap.peek();
        } else {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private void test(int[] a, double expected) {
        reset();
        for (int n : a) {
            addNew(n);
        }
        assertEquals(expected, getMedian(), 1e-8);
    }

    @Test
    public void test1() {
        test(new int[] {}, 0);
        test(new int[] {1}, 1);
        test(new int[] {1, 2}, 1.5);
        test(new int[] {13, 5, 7, 9, 10}, 9);
        test(new int[] {103, 9, 10, 2}, 9.5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Median");
    }
}
