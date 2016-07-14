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

    // beats 8.82%(102 ms)
    static class MedianFinder1 implements IMedianFinder {
        PriorityQueue<Integer> minMaxQ = new PriorityQueue<>((p, q) -> q - p);
        PriorityQueue<Integer> maxMinQ = new PriorityQueue<>();

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

        public double findMedian() {
            if (minMaxQ.size() > maxMinQ.size()) return minMaxQ.peek();

            return (minMaxQ.peek() + maxMinQ.peek()) / 2d;
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

    void test(Integer ... nums) {
        test1(new MedianFinder1());
    }

    @Test
    public void test1() {
        test(1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MedianFinder");
    }
}
