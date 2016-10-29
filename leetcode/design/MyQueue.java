import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/implement-queue-using-stacks/
//
// Implement the following operations of a queue using stacks.
public class MyQueue {
    static interface IMyQueue {
        public void push(int x);

        public void pop();

        public int peek();

        public boolean empty();
    }

    // beats 70.35%(111 ms)
    static class MyQueue1 implements IMyQueue {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();

        // time complexity: O(1)
        public void push(int x) {
            stack1.push(x);
        }

        // time complexity: Amortized O(1), Worst-case O(n).
        public void pop() {
            shift();
            stack2.pop();
        }

        // time complexity: O(N)
        public int peek() {
            shift();
            return stack2.peek();
        }

        private void shift() {
            if (stack2.isEmpty()) {
                while (!stack1.isEmpty()) {
                    stack2.push(stack1.pop());
                }
            }
        }

        public boolean empty() {
            return stack1.isEmpty() && stack2.isEmpty();
        }
    }

    // beats 55.56%(114 ms)
    static class MyQueue2 implements IMyQueue {
        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();

        // time complexity: O(N)
        public void push(int x) {
            while (!stack1.isEmpty()) {
                stack2.push(stack1.pop());
            }
            stack1.push(x);
            while (!stack2.isEmpty()) {
                stack1.push(stack2.pop());
            }
        }

        // time complexity: O(1)
        public void pop() {
            stack1.pop();
        }

        // time complexity: O(1)
        public int peek() {
            return stack1.peek();
        }

        public boolean empty() {
            return stack1.isEmpty();
        }
    }

    // recursion
    // beats 60.51%(113 ms)
    static class MyQueue3 implements IMyQueue {
        Stack<Integer> stack = new Stack<>();

        // time complexity: O(N)
        public void push(int x) {
            if (stack.empty()) {
                stack.push(x);
            } else {
                int top = stack.pop();
                push(x);
                stack.push(top);
            }
        }

        // time complexity: O(1)
        public void pop() {
            stack.pop();
        }

        // time complexity: O(1)
        public int peek() {
            return stack.peek();
        }

        public boolean empty() {
            return stack.isEmpty();
        }
    }

    static class MyQueue4 implements IMyQueue {
        Stack<Integer> inputStack = new Stack<>();
        Stack<Integer> outputStack = new Stack<>();

        public void push(int x) {
            while (!outputStack.empty()) {
                inputStack.push(outputStack.pop());
            }
            inputStack.push(x);
        }

        private void inputToOutput() {
            while (!inputStack.empty()) {
                outputStack.push(inputStack.pop());
            }
        }

        public void pop() {
            inputToOutput();
            outputStack.pop();
        }

        public int peek() {
            inputToOutput();
            return outputStack.peek();
        }

        public boolean empty() {
            return inputStack.isEmpty() && outputStack.isEmpty();
        }
    }

    void test1(IMyQueue obj) {
        obj.push(4);
        obj.push(3);
        obj.push(2);
        obj.push(1);
        assertEquals(4, obj.peek());
        obj.pop();
        assertEquals(3, obj.peek());
        obj.push(0);
        obj.push(-1);
        obj.push(-2);
        assertEquals(3, obj.peek());
        obj.pop();
        obj.pop();
        assertEquals(1, obj.peek());
        obj.pop();
        obj.pop();
        assertEquals(-1, obj.peek());
        assertEquals(false, obj.empty());
        obj.pop();
        assertEquals(-2, obj.peek());
        obj.pop();
        assertEquals(true, obj.empty());
    }

    void test2(IMyQueue obj) {
        obj.push(512);
        obj.push(-1024);
        obj.push(-1024);
        obj.push(512);
        assertEquals(512, obj.peek());
        obj.pop();
        obj.pop();
        assertEquals(-1024, obj.peek());
        obj.pop();
        assertEquals(512, obj.peek());
        assertEquals(false, obj.empty());
        obj.pop();
        assertEquals(true, obj.empty());
    }

    @Test
    public void test1() {
        test1(new MyQueue1());
        test1(new MyQueue2());
        test1(new MyQueue3());
    }

    @Test
    public void test2() {
        test2(new MyQueue1());
        test2(new MyQueue2());
        test1(new MyQueue3());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MyQueue");
    }
}
