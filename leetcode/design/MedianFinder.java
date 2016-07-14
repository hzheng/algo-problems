import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/find-median-from-data-stream/
//
// Design a data structure that supports the following two operations:
// Add a integer number from the data stream to the data structure.
// double findMedian() - Return the median of all elements so far.
public class MedianFinder {
    static interface IMedianFinder {
        // Adds a number into the data structure.
        public void addNum(int num);

        // Returns the median of current data stream
        public double findMedian();
    }

    // beats 63.85%(36 ms)
    static class MedianFinder1 implements IMedianFinder {
        // lambda expression make beat rate drop to 8.82%(102 ms)
        // PriorityQueue<Integer> minMaxQ = new PriorityQueue<>((p, q) -> q - p);
        PriorityQueue<Integer> minMaxQ = new PriorityQueue<>(
            Collections.reverseOrder());
        PriorityQueue<Integer> maxMinQ = new PriorityQueue<>();

        // time complexity: O(log(N))
        public void addNum(int num) {
            int size = minMaxQ.size();
            if (size == 0 || (size == maxMinQ.size())) {
                if (size > 0) {
                    int maxMin = maxMinQ.peek();
                    if (num > maxMin) {
                        maxMinQ.poll();
                        maxMinQ.offer(num);
                        num = maxMin;
                    }
                }
                minMaxQ.offer(num);
            } else {
                int minMax = minMaxQ.peek();
                if (num < minMax) {
                    minMaxQ.poll();
                    minMaxQ.offer(num);
                    num = minMax;
                }
                maxMinQ.offer(num);
            }
        }

        // time complexity: O(1)
        public double findMedian() {
            if (minMaxQ.size() > maxMinQ.size()) return minMaxQ.peek();

            return (minMaxQ.peek() + maxMinQ.peek()) / 2d;
        }
    }

    // beats 28.07%(48 ms)
    static class MedianFinder2 implements IMedianFinder {
        PriorityQueue<Integer> minMaxQ = new PriorityQueue<>(
            Collections.reverseOrder());
        PriorityQueue<Integer> maxMinQ = new PriorityQueue<>();

        // time complexity: O(log(N))
        public void addNum(int num) {
            minMaxQ.offer(num);
            maxMinQ.offer(minMaxQ.poll());
            if (minMaxQ.size() < maxMinQ.size()) {
                minMaxQ.offer(maxMinQ.poll());
            }
        }

        // time complexity: O(1)
        public double findMedian() {
            if (minMaxQ.size() != maxMinQ.size()) return minMaxQ.peek();

            return (minMaxQ.peek() + maxMinQ.peek()) / 2d;
        }
    }

    // Use negative number for minMaxQ without reverseOrder
    // problem: may overflow
    static class MedianFinder3 implements IMedianFinder {
        PriorityQueue<Integer> small = new PriorityQueue<>();
        PriorityQueue<Integer> large = new PriorityQueue<>();

        // time complexity: O(log(N))
        public void addNum(int num) {
            small.offer(num);
            large.offer(-small.poll());
            if (small.size() < large.size()) {
                small.offer(-large.poll());
            }
        }

        // time complexity: O(1)
        public double findMedian() {
            if (small.size() != large.size()) return small.peek();

            return (small.peek() - large.peek()) / 2d;
        }
    }

    void test1(IMedianFinder obj) {
        double delta = 1e-6;
        obj.addNum(1);
        obj.addNum(2);
        assertEquals(1.5, obj.findMedian(), delta);
        obj.addNum(3);
        assertEquals(2, obj.findMedian(), delta);
        obj.addNum(4);
        assertEquals(2.5, obj.findMedian(), delta);
        obj.addNum(5);
        assertEquals(3, obj.findMedian(), delta);
        obj.addNum(0);
        assertEquals(2.5, obj.findMedian(), delta);
        obj.addNum(-1);
        assertEquals(2, obj.findMedian(), delta);
    }

    // @Test
    public void test1() {
        test1(new MedianFinder1());
        test1(new MedianFinder2());
        test1(new MedianFinder3());
    }

    void test2(IMedianFinder obj) {
        double delta = 1e-6;
        obj.addNum(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, obj.findMedian(), delta);
        obj.addNum(0);
        obj.addNum(1);
        assertEquals(0, obj.findMedian(), delta);
    }

    @Test
    public void test2() {
        test2(new MedianFinder1());
        test2(new MedianFinder2());
        // test2(new MedianFinder3());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MedianFinder");
    }
}
