import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1172: https://leetcode.com/problems/dinner-plate-stacks/
//
// You have an infinite number of stacks arranged in a row and numbered (left to right) from 0, each
// of the stacks has the same maximum capacity.
// Implement the DinnerPlates class:
// DinnerPlates(int capacity) Initializes the object with the maximum capacity of the stacks.
// void push(int val) Pushes the given positive integer val into the leftmost stack with size less
// than capacity.
// int pop() Returns the value at the top of the rightmost non-empty stack and removes it from that
// stack, and returns -1 if all stacks are empty.
// int popAtStack(int index) Returns the value at the top of the stack with the given index and
// removes it from that stack, and returns -1 if the stack with that given index is empty.
//
// Constraints:
// 1 <= capacity <= 20000
// 1 <= val <= 20000
// 0 <= index <= 100000
// At most 200000 calls will be made to push, pop, and popAtStack.
public class DinnerPlates {
    // SortedSet
    // 123 ms(32.60%), 121.1 MB(26.43%) for 19 tests
    static class DinnerPlates1 {
        private final int capacity;
        private final List<Stack<Integer>> stacks = new ArrayList<>();
        private final SortedSet<Integer> vacantStacks = new TreeSet<>();
        private final SortedSet<Integer> nonEmptyStacks = new TreeSet<>();

        public DinnerPlates1(int capacity) {
            this.capacity = capacity;
        }

        // time complexity: O(log(N))
        public void push(int val) {
            if (vacantStacks.isEmpty()) {
                vacantStacks.add(stacks.size());
                stacks.add(new Stack<>());
            }
            int index = vacantStacks.first();
            Stack<Integer> stack = stacks.get(index);
            stack.push(val);
            nonEmptyStacks.add(index);
            if (stack.size() == capacity) {
                vacantStacks.remove(index);
            }
        }

        // time complexity: O(log(N))
        public int pop() {
            if (nonEmptyStacks.isEmpty()) { return -1; }

            int index = nonEmptyStacks.last();
            return popAtStack(index);
        }

        // time complexity: O(log(N))
        public int popAtStack(int index) {
            if (index >= stacks.size()) { return -1; }

            Stack<Integer> stack = stacks.get(index);
            int res = stack.isEmpty() ? -1 : stack.pop();
            vacantStacks.add(index);
            if (stack.isEmpty()) {
                nonEmptyStacks.remove(index);
            }
            return res;
        }
    }

    // SortedSet
    // 114 ms(35.68%), 216.3 MB(5.29%) for 19 tests
    static class DinnerPlates2 {
        private final int capacity;
        private final List<Stack<Integer>> stacks = new ArrayList<>();
        private final NavigableSet<Integer> vacantStacks = new TreeSet<>();

        public DinnerPlates2(int capacity) {
            this.capacity = capacity;
        }

        // time complexity: O(log(N))
        public void push(int val) {
            if (vacantStacks.isEmpty()) {
                vacantStacks.add(stacks.size());
                stacks.add(new Stack<>());
            }
            int index = vacantStacks.first();
            Stack<Integer> stack = stacks.get(index);
            stack.push(val);
            if (stack.size() == capacity) {
                vacantStacks.remove(index);
            }
        }

        // time complexity: O(log(N))
        public int pop() {
            return popAtStack(stacks.size() - 1);
        }

        // time complexity: O(log(N))
        public int popAtStack(int index) {
            if (index < 0 || index >= stacks.size()) { return -1; }

            Stack<Integer> stack = stacks.get(index);
            int res = stack.isEmpty() ? -1 : stack.pop();
            vacantStacks.add(index);
            while (!stacks.isEmpty() && stacks.get(stacks.size() - 1).isEmpty()) {
                vacantStacks.pollLast();
                stacks.remove(stacks.size() - 1);
            }
            return res;
        }
    }

    // Hash Table
    // 84 ms(40.53%), 120.4 MB(26.43%) for 19 tests
    static class DinnerPlates3 {
        private final Map<Integer, Stack<Integer>> stacks = new HashMap<>();
        private final int capacity;
        private int pushIndex;
        private int popIndex;

        public DinnerPlates3(int capacity) {
            this.capacity = capacity;
        }

        // amortized time complexity: O(1)
        public void push(int val) {
            for (; stacks.containsKey(pushIndex)
                   && stacks.get(pushIndex).size() == capacity; pushIndex++) {}
            stacks.computeIfAbsent(pushIndex, a -> new Stack<>()).push(val);
            popIndex = Math.max(popIndex, pushIndex);
        }

        // amortized time complexity: O(1)
        public int pop() {
            if (stacks.isEmpty()) { return -1; }

            for (; popIndex >= 0 && stacks.get(popIndex).isEmpty(); popIndex--) {}
            if (popIndex < 0) return -1;

            pushIndex = Math.min(pushIndex, popIndex);
            return stacks.get(popIndex).pop();
        }

        // time complexity: O(1)
        public int popAtStack(int index) {
            Stack<Integer> stack = stacks.get(index);
            if (stack == null || stack.isEmpty()) { return -1; }

            pushIndex = Math.min(pushIndex, index);
            return stack.pop();
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "push", "push", "push", "push", "push", "popAtStack", "push",
                           "push", "popAtStack", "popAtStack", "pop", "pop", "pop", "pop", "pop"},
             new Object[][] {{2}, {1}, {2}, {3}, {4}, {7}, {8}, {20}, {21}, {0}, {2}, {}, {}, {},
                             {}, {}},
             new Object[] {null, null, null, null, null, null, -1, null, null, 2, 20, 21, 7, 4, 3,
                           1});
        test(new String[] {className, "push", "push", "push", "push", "push", "popAtStack", "push",
                           "push", "popAtStack", "popAtStack", "pop", "pop", "pop", "pop", "pop"},
             new Object[][] {{2}, {1}, {2}, {3}, {4}, {5}, {0}, {20}, {21}, {0}, {2}, {}, {}, {},
                             {}, {}},
             new Object[] {null, null, null, null, null, null, 2, null, null, 20, 21, 5, 4, 3, 1,
                           -1});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
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
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("DinnerPlates1");
            test1("DinnerPlates2");
            test1("DinnerPlates3");
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
