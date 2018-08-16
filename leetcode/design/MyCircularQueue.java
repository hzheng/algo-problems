import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC622: https://leetcode.com/problems/design-circular-queue/
//
// Design your implementation of a circular queue:
// MyCircularQueue(k): Constructor, set the size of the queue to be k.
// Front: Get the front item from the queue. If the queue is empty, return -1.
// Rear: Get the last item from the queue. If the queue is empty, return -1.
// enQueue(value): Insert an element into the circular queue. Return true if 
//                 the operation is successful.
// deQueue(): Delete an element from the circular queue. Return true if the 
//            operation is successful.
// isEmpty(): Checks whether the circular queue is empty or not.
// isFull(): Checks whether the circular queue is full or not.
public class MyCircularQueue {
    static interface IMyCircularQueue {
        /** Insert an element into the circular queue. Return true if the operation is successful. */
        public boolean enQueue(int value);
        /** Delete an element from the circular queue. Return true if the operation is successful. */
        public boolean deQueue();
        /** Get the front item from the queue. */
        public int Front();
        /** Get the last item from the queue. */
        public int Rear();
        /** Checks whether the circular queue is empty or not. */
        public boolean isEmpty();
        /** Checks whether the circular queue is full or not. */
        public boolean isFull();
    }

    // beats 78.85%(66 ms for 52 tests)
    class MyCircularQueue1 implements IMyCircularQueue {
        private int[] buffer;
        private int len;
        private int head;
        private int tail;

        public MyCircularQueue1(int k) {
            len = k + 1;
            buffer = new int[len];
        }

        public boolean enQueue(int value) {
            if (isFull()) return false;

            buffer[tail] = value;
            tail = (tail + 1) % len;
            return true;
        }

        public boolean deQueue() {
            if (isEmpty()) return false;

            head = (head + 1) % len;
            return true;
        }

        public int Front() {
            return isEmpty() ? -1 : buffer[head];
        }

        public int Rear() {
            return isEmpty() ? -1 : buffer[(tail + len - 1) % len];
        }

        public boolean isEmpty() {
            return head == tail;
        }

        public boolean isFull() {
            return (tail + 1 - head) % len == 0;
        }
    }

    void test1(IMyCircularQueue obj) {
    }

    @Test
    public void test1() {
        test1(new MyCircularQueue1(3));
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
