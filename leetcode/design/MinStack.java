import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC155: https://leetcode.com/problems/min-stack/
//
// Design a stack that supports push, pop, top, and retrieving the
// minimum element in constant time.
public class MinStack {
    static interface IMinStack {
        public void pop();
        public int top();
        public void push(int x);
        public int getMin();
    }

    // Linked List + Heap
    // beats 13.61%(138 ms)
    static class MinStack1 implements IMinStack {
        private class Node {
            int val;
            Node next;
            Node prev;
            Node(int x) {
                val = x;
            }
        }

        private Node head = new Node(0);

        private Node tail = head;

        private PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        public MinStack1() {
        }

        public void push(int x) {
            tail.next = new Node(x);
            tail.next.prev = tail;
            tail = tail.next;
            minHeap.add(x);
        }

        public void pop() {
            if (tail == head) throw new EmptyStackException();

            minHeap.remove(tail.val);
            Node prev = tail.prev;
            prev.next = null;
            tail.prev = null;
            tail = prev;
        }

        public int top() {
            if (tail == head) throw new EmptyStackException();

            return tail.val;
        }

        public int getMin() {
            return minHeap.peek();
        }
    }

    // Two Stacks
    // beats 27.97%(131 ms)
    static class MinStack2 implements IMinStack {
        private Stack<Integer> stack = new Stack<>();
        private Stack<Integer> minStack = new Stack<>();

        public void push(int x) {
            stack.push(x);
            if (minStack.isEmpty() || x <= minStack.peek()) {
                minStack.push(x);
            }
        }

        public void pop() {
            if (stack.peek() <= minStack.peek()) {
                // use <= instead of == to avoid Integer object equality
                minStack.pop();
            }
            stack.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    // Linked List
    // beats 73.88%(120 ms)
    static class MinStack3 implements IMinStack {
        static class Node {
            int val;
            int min;
            Node next;

            Node(int val, int min){
                this.val = val;
                this.min = min;
            }
        }

        private Node top;

        public MinStack3() {
        }

        public void push(int x) {
            if (top == null) {
                top = new Node(x, x);
            } else {
                Node n = new Node(x, Math.min(x, top.min));
                n.next = top;
                top = n;
            }
        }

        public void pop() {
            Node next = top.next;
            top.next = null;
            top = next;
        }

        public int top() {
            return top.val;
        }

        public int getMin() {
            return top.min;
        }
    }

    // Solution of Choice
    // One Stack
    // beats 26.64%(131 ms for 18 tests)
    static class MinStack4 implements IMinStack {
        private Stack<Integer> stack = new Stack<>();
        private int min = Integer.MAX_VALUE;

        public void push(int x) {
            if (x <= min) {
                stack.push(min);
                min = x;
            }
            stack.push(x);
        }

        public void pop() {
            if (stack.pop() == min) {
                min = stack.pop();
            }
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return min;
        }
    }

    void test1(IMinStack obj) {
        obj.push(4);
        obj.push(2);
        obj.push(3);
        obj.push(1);
        assertEquals(1, obj.getMin());
        obj.pop();
        assertEquals(2, obj.getMin());
        obj.push(0);
        obj.push(-1);
        obj.push(2);
        assertEquals(-1, obj.getMin());
        assertEquals(2, obj.top());
        obj.pop();
        obj.pop();
        assertEquals(0, obj.getMin());
        obj.pop();
        obj.pop();
        assertEquals(2, obj.getMin());
        obj.pop();
        assertEquals(4, obj.getMin());
        obj.pop();
    }

    void test2(IMinStack obj) {
        obj.push(512);
        obj.push(-1024);
        obj.push(-1024);
        obj.push(512);
        obj.pop();
        assertEquals(-1024, obj.getMin());
        obj.pop();
        assertEquals(-1024, obj.getMin());
        obj.pop();
        assertEquals(512, obj.getMin());
    }

    @Test
    public void test1() {
        test1(new MinStack1());
        test1(new MinStack2());
        test1(new MinStack3());
        test1(new MinStack4());
    }

    @Test
    public void test2() {
        test2(new MinStack1());
        test2(new MinStack2());
        test2(new MinStack3());
        test2(new MinStack4());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinStack");
    }
}
