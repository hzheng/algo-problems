import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1381: https://leetcode.com/problems/design-a-stack-with-increment-operation/
//
// Design a stack which supports the following operations.
// CustomStack(int maxSize) Initializes the object with maxSize which is the maximum number of
// elements in the stack or do nothing if the stack reached the maxSize.
// void push(int x) Adds x to the top of the stack if the stack hasn't reached the maxSize.
// int pop() Pops and returns the top of stack or -1 if the stack is empty.
// void inc(int k, int val) Increments the bottom k elements of the stack by val. If there are less
// than k elements in the stack, just increment all the elements in the stack.
public class CustomStack {
    // Stack
    // 4 ms(99.59%), 39.9 MB(100.00%) for 34 tests
    class CustomStack1 {
        private int[] stack;
        private int end = 0;

        public CustomStack1(int maxSize) {
            stack = new int[maxSize];
        }

        // time complexity: O(1)
        public void push(int x) {
            if (end < stack.length) {
                stack[end++] = x;
            }
        }

        // time complexity: O(1)
        public int pop() {
            return (end == 0) ? -1 : stack[--end];
        }

        // time complexity: O(k)
        public void increment(int k, int val) {
            for (int i = Math.min(stack.length, k) - 1; i >= 0; i--) {
                stack[i] += val;
            }
        }
    }

    // Stack
    // 4 ms(99.59%), 40.4 MB(100.00%) for 34 tests
    class CustomStack2 {
        int[] increased;
        Stack<Integer> stack = new Stack<>();

        public CustomStack2(int maxSize) {
            increased = new int[maxSize];
        }

        // time complexity: O(1)
        public void push(int x) {
            if (stack.size() < increased.length)
                stack.push(x);
        }

        // time complexity: O(1)
        public int pop() {
            int top = stack.size() - 1;
            if (top < 0) { return -1; }
            if (top > 0) {
                increased[top - 1] += increased[top];
            }
            int res = stack.pop() + increased[top];
            increased[top] = 0;
            return res;
        }

        // time complexity: O(1)
        public void increment(int k, int val) {
            int i = Math.min(k, stack.size()) - 1;
            if (i >= 0) {
                increased[i] += val;
            }
        }
    }

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {
        }.getClass().getEnclosingClass().newInstance();
        test(new String[] {className, "push", "push", "pop", "push", "push", "push", "increment",
                           "increment", "pop", "pop", "pop", "pop"},
             new Object[][] {new Object[] {outerObj, 3}, {1}, {2}, {}, {2}, {3}, {4}, {5, 100},
                             {2, 100}, {}, {}, {}, {}},
             new Integer[] {null, null, null, 2, null, null, null, null, null, 103, 202, 201, -1});
    }

    void test(String[] methods, Object[][] args, Integer[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
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

    @Test public void test1() {
        try {
            test1("CustomStack1");
            test1("CustomStack2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
