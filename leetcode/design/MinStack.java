import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC155: https://leetcode.com/problems/min-stack/
//
// Design a stack that supports push, pop, top, and retrieving the
// minimum element in constant time.
public class MinStack {
    // Linked List + Heap
    // beats 13.61%(138 ms)
    class MinStack1 {
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
    // beats 99.38%(61 ms for 12 tests)
    class MinStack2 {
        private Stack<Integer> stack = new Stack<>();
        private Stack<Integer> minStack = new Stack<>();

        public MinStack2() {
        }

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

    // Two Stacks
    // beats 27.97%(131 ms)
    // beats 99.91%(59 ms for 12 tests)
    class MinStack2_2 {
        private Stack<Integer> stack = new Stack<>();
        private Stack<Integer> minStack = new Stack<>();

        public MinStack2_2() {
            stack.push(Integer.MAX_VALUE);
            minStack.push(Integer.MAX_VALUE);
        }

        public void push(int x) {
            minStack.push(Math.min(x, minStack.peek()));
            stack.push(x);
        }

        public void pop() {
            stack.pop();
            minStack.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return minStack.peek();
        }
    }

    // Linked List
    // beats 87.86%(66 ms for 12 tests)
    class MinStack3 {
        class Node {
            int val;
            int min;
            Node next;

            Node(int val, int min) {
                this.val = val;
                this.min = min;
            }
        }

        private Node top = new Node(0, Integer.MAX_VALUE);

        public MinStack3() {
        }

        public void push(int x) {
            Node cur = new Node(x, Math.min(x, top.min));
            cur.next = top;
            top = cur;
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
    // beats 48.53%(74 ms for 12 tests)
    class MinStack4 {
        private Stack<Integer> stack = new Stack<>();
        private int min = Integer.MAX_VALUE;

        public MinStack4() {
        }

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

    void test2(MinStack2 obj) {
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

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] {className, "push", "push", "push", "getMin", "pop", "top", "getMin"},
             new Object[][] {new Object[] {outerObj}, {-2}, {0}, {-3}, {}, {}, {}, {}},
             new Integer[] {null, null, null, null, -3, null, 0, -2});

        test(new String[] {className, "push", "push", "push", "push", "getMin", "pop", "getMin",
                           "push", "push", "push", "getMin", "top", "pop", "pop", "getMin", "pop",
                           "pop", "getMin", "pop", "getMin", "top"},
             new Object[][] {new Object[] {outerObj}, {4}, {2}, {3}, {1}, {}, {}, {}, {0}, {-1},
                             {2}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
             new Integer[] {null, null, null, null, null, 1, null, 2, null, null, null, -1, 2, null,
                            null, 0, null, null, 2, null, 4, 4});
    }

    void test(String[] methods, Object[][] args, Integer[] expected) throws Exception {
        final String name = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("MinStack1");
            test1("MinStack2");
            test1("MinStack2_2");
            test1("MinStack3");
            test1("MinStack4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
