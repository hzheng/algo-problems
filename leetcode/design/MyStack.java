import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC225: https://leetcode.com/problems/implement-stack-using-queues/
//
// Implement a stack using queues.
// You must use only standard operations of a queue -- which means only push
// to back, peek/pop from front, size, and is empty operations are valid.
// You may assume that all operations are valid.
public class MyStack {
    static interface IMyStack {
        public void push(int x);

        public void pop();

        public int top();

        public boolean empty();
    }

    // beats 85.58%(106 ms)
    static class MyStack1 implements IMyStack {
        Queue<Integer> queue1 = new LinkedList<>();
        Queue<Integer> queue2 = new LinkedList<>();

        // time complexity: O(1)
        public void push(int x) {
            queue1.offer(x);
        }

        // time complexity: O(N)
        public void pop() {
            shift();
            queue1.poll();
        }

        public int top() {
            shift();
            return queue1.peek();
        }

        private void shift() {
            int size1 = queue1.size();
            if (size1 == 1) return;

            if (size1 == 0) {
                Queue<Integer> tmp = queue1;
                queue1 = queue2;
                queue2 = tmp;
                shift();
            } else {
                for (int i = 1; i < size1; i++) {
                    queue2.offer(queue1.poll());
                }
            }
        }

        public boolean empty() {
            return queue1.isEmpty() && queue2.isEmpty();
        }
    }

    // beats 35.94%(117 ms)
    static class MyStack1_2 implements IMyStack {
        Queue<Integer> queue1 = new LinkedList<>();
        Queue<Integer> queue2 = new LinkedList<>();
        int top;

        // time complexity: O(1)
        public void push(int x) {
            top = x;
            queue1.offer(x);
        }

        // time complexity: O(N)
        public void pop() {
            for (int i = queue1.size(); i > 1; i--) {
                top = queue1.poll();
                queue2.offer(top);
            }
            queue1.poll();
            Queue<Integer> tmp = queue1;
            queue1 = queue2;
            queue2 = tmp;
        }

        public int top() {
            return top;
        }

        public boolean empty() {
            return queue1.isEmpty();
        }
    }

    // Solution of Choice
    // Two Queues
    // beats 99.93%(92 ms)
    static class MyStack2 implements IMyStack {
        Queue<Integer> queue1 = new LinkedList<>();
        Queue<Integer> queue2 = new LinkedList<>();

        // time complexity: O(N)
        public void push(int x) {
            queue2.offer(x);
            while (!queue1.isEmpty()) {
                queue2.offer(queue1.poll());
            }
            // for (int i = queue1.size() - 1; i >= 0; i--) { // may be faster
            //     queue2.offer(queue1.poll());
            // }
            Queue<Integer> tmp = queue1;
            queue1 = queue2;
            queue2 = tmp;
        }

        // time complexity: O(1)
        public void pop() {
            queue1.poll();
        }

        // time complexity: O(1)
        public int top() {
            return queue1.peek();
        }

        public boolean empty() {
            return queue1.isEmpty();
        }
    }

    // Single Queue
    // beats 52.11%(113 ms)
    static class MyStack3 implements IMyStack {
        Queue<Integer> queue = new LinkedList<>();

        // time complexity: O(N)
        public void push(int x) {
            queue.offer(x);
            for (int size = queue.size(); size > 1; size--) {
                queue.offer(queue.poll());
            }
        }

        // time complexity: O(1)
        public void pop() {
            queue.poll();
        }

        // time complexity: O(1)
        public int top() {
            return queue.peek();
        }

        public boolean empty() {
            return queue.isEmpty();
        }
    }

    void test1(IMyStack obj) {
        obj.push(4);
        obj.push(3);
        obj.push(2);
        obj.push(1);
        assertEquals(1, obj.top());
        obj.pop();
        assertEquals(2, obj.top());
        obj.push(0);
        obj.push(-1);
        obj.push(-2);
        assertEquals(-2, obj.top());
        obj.pop();
        obj.pop();
        assertEquals(0, obj.top());
        obj.pop();
        obj.pop();
        assertEquals(3, obj.top());
        assertEquals(false, obj.empty());
        obj.pop();
        assertEquals(4, obj.top());
        obj.pop();
        assertEquals(true, obj.empty());
    }

    void test2(IMyStack obj) {
        obj.push(512);
        obj.push(-1024);
        obj.push(-1024);
        obj.push(512);
        assertEquals(512, obj.top());
        obj.pop();
        obj.pop();
        assertEquals(-1024, obj.top());
        obj.pop();
        assertEquals(512, obj.top());
        assertEquals(false, obj.empty());
        obj.pop();
        assertEquals(true, obj.empty());
    }

    @Test
    public void test1() {
        test1(new MyStack1());
        test1(new MyStack1_2());
        test1(new MyStack2());
        test1(new MyStack3());
    }

    @Test
    public void test2() {
        test2(new MyStack1());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MyStack");
    }
}
