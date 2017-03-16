import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC346: https://leetcode.com/problems/moving-average-from-data-stream
//
// Given a stream of integers and a window size, calculate the moving average of
// all integers in the sliding window.
public class MovingAverage {
    interface IMovingAverage {
        public double next(int val);
    }

    // Queue
    // beats 61.96%(160 ms for 12 tests)
    static class MovingAverage1 implements IMovingAverage {
        private int size;
        private int sum;
        private Queue<Integer> window = new LinkedList<>();

        public MovingAverage1(int size) {
            this.size = size;
        }

        public double next(int val) {
            if (window.size() == size) {
                sum -= window.poll();
            }
            sum += val;
            window.offer(val);
            return (double)sum / window.size();
        }
    }

    // Circular Array
    // beats 76.67%(152 ms for 12 tests)
    static class MovingAverage2 implements IMovingAverage {
        private int[] window;
        private int sum;
        private int cur;
        private int count;

        public MovingAverage2(int size) {
            window = new int[size];
        }

        public double next(int val) {
            if (count < window.length) {
                count++;
            } else {
                sum -= window[cur];
            }
            sum += val;
            window[cur++] = val;
            cur %= window.length;
            return (double)sum / count;
        }
    }

    private void test1(IMovingAverage m) {
        double epsilon = 1E-8;
        assertEquals(1, m.next(1), epsilon);
        assertEquals(5.5, m.next(10), epsilon);
        assertEquals(4.666666667, m.next(3), epsilon);
        assertEquals(6.0, m.next(5), epsilon);
    }

    private void test1(String className, int size) {
        try {
            Class<?> clazz = Class.forName("MovingAverage$" + className);
            Constructor<?> ctor = clazz.getConstructor(int.class);
            test1((IMovingAverage)ctor.newInstance(new Object[] {size}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test1(int size) {
        test1("MovingAverage1", size);
        test1("MovingAverage2", size);
    }

    @Test
    public void test() {
        test1(3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MovingAverage");
    }
}
