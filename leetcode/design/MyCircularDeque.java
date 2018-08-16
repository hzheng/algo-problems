import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC641: https://leetcode.com/problems/design-circular-deque/
//
// Design your implementation of the circular double-ended queue (deque).
// MyCircularDeque(k): Constructor, set the size of the deque to be k.
// insertFront(): Adds an item at the front of Deque. Return true if the 
//                operation is successful.
// insertLast(): Adds an item at the rear of Deque. Return true if the operation
//               is successful.
// deleteFront(): Deletes an item from the front of Deque. Return true if the 
//                operation is successful.
// deleteLast(): Deletes an item from the rear of Deque. Return true if the 
//               operation is successful.
// getFront(): Gets the front item from the Deque. If the deque is empty, 
//             return -1.
// getRear(): Gets the last item from Deque. If the deque is empty, return -1.
// isEmpty(): Checks whether Deque is empty or not. 
// isFull(): Checks whether Deque is full or not.
public class MyCircularDeque {
    static interface IMyCircularDeque {
        /** Adds an item at the front of Deque. Return true if the operation is successful. */
        public boolean insertFront(int value);
        /** Adds an item at the rear of Deque. Return true if the operation is successful. */
        public boolean insertLast(int value);
        /** Deletes an item from the front of Deque. Return true if the operation is successful. */
        public boolean deleteFront();
        /** Deletes an item from the rear of Deque. Return true if the operation is successful. */
        public boolean deleteLast();
        /** Get the front item from the deque. */
        public int getFront();
        /** Get the last item from the deque. */
        public int getRear();
        /** Checks whether the circular deque is empty or not. */
        public boolean isEmpty();
        /** Checks whether the circular deque is full or not. */
        public boolean isFull();
    }

    // beats 97.84%(64 ms for 51 tests)
    class MyCircularDeque1 implements IMyCircularDeque {
        private int[] buffer;
        private int len;
        private int head;
        private int tail;
        
        public MyCircularDeque1(int k) {
            len = k + 1;
            buffer = new int[len];
        }
    
        public boolean insertFront(int value) {
            if (isFull()) return false;

            head = (head + len - 1) % len;
            buffer[head] = value;
            return true;
        }
    
        public boolean insertLast(int value) {
            if (isFull()) return false;

            buffer[tail] = value;
            tail = (tail + 1) % len;
            return true;
        }
        
        public boolean deleteFront() {
            if (isEmpty()) return false;
            
            head = (head + 1) % len;
            return true;
        }
        
        public boolean deleteLast() {
            if (isEmpty()) return false;
            
            tail = (tail + len - 1) % len;
            return true;
        }
            
        public int getFront() {
            return isEmpty() ? -1 : buffer[head];
        }
        
        public int getRear() {
            return isEmpty() ? -1 : buffer[(tail + len - 1) % len];
        }
    
        public boolean isEmpty() {
            return head == tail;
        }
        
        public boolean isFull() {
            return (tail + 1 - head) % len == 0;
        }
    }

    void test1(IMyCircularDeque obj) {
    }

    @Test
    public void test1() {
        test1(new MyCircularDeque1(3));
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
