import java.util.Stack;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 3.5:
 * Implement a MyQueue class which implements a queue using two stacks.
 */
interface IQueue {
    void add(int i);
    Integer remove();
    Integer peek();
}

public class MyQueue implements IQueue {
    private IQueue queue;

    public MyQueue(IQueue impl) {
        this.queue = impl;
    }

    public void add(int i) {
        queue.add(i);
    }

    public Integer remove() {
        return queue.remove();
    }

    public Integer peek() {
        return queue.peek();
    }

    static class QueueImpl1 implements IQueue {
        Stack<Integer> inputStack = new Stack<Integer>();
        Stack<Integer> outputStack = new Stack<Integer>();

        public void add(int i) {
            while (!outputStack.empty()) {
                inputStack.push(outputStack.pop());
            }
            inputStack.push(i);
        }

        private void inputToOutput() {
            while (!inputStack.empty()) {
                outputStack.push(inputStack.pop());
            }
        }

        public Integer remove() {
            inputToOutput();
            return outputStack.empty() ? null : outputStack.pop();
        }

        public Integer peek() {
            inputToOutput();
            return outputStack.empty() ? null : outputStack.peek();
        }
    }

    static class QueueImpl2 implements IQueue {
        Stack<Integer> inputStack = new Stack<Integer>();
        Stack<Integer> outputStack = new Stack<Integer>();

        public void add(int i) {
            inputStack.push(i);
        }

        private void inputToOutput() {
            if (outputStack.empty()) {
                while (!inputStack.empty()) {
                    outputStack.push(inputStack.pop());
                }
            }
        }

        public Integer remove() {
            inputToOutput();
            return outputStack.empty() ? null : outputStack.pop();
        }

        public Integer peek() {
            inputToOutput();
            return outputStack.empty() ? null : outputStack.peek();
        }
    }

    public static void test(IQueue queueImpl) {
        MyQueue queue = new MyQueue(queueImpl);
        assertNull(queue.remove());
        int n = 100;
        for (int i = 0; i < n; i++) {
            queue.add(i);
        }
        for (int i = 0; i < n; i++) {
            assertEquals((Integer)i, queue.peek());
            assertEquals((Integer)i, queue.remove());
        }
        assertNull(queue.remove());
    }

    public static void main(String[] args) {
        test(new QueueImpl1());
        test(new QueueImpl2());
    }
}
