import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1670: https://leetcode.com/problems/design-front-middle-back-queue/
//
// Design a queue that supports push and pop operations in the front, middle, and back.
// Implement the FrontMiddleBack class:
// FrontMiddleBack() Initializes the queue.
// void pushFront(int val) Adds val to the front of the queue.
// void pushMiddle(int val) Adds val to the middle of the queue.
// void pushBack(int val) Adds val to the back of the queue.
// int popFront() Removes the front element of the queue and returns it. If the queue is empty,
// return -1.
// int popMiddle() Removes the middle element of the queue and returns it. If the queue is empty,
// return -1.
// int popBack() Removes the back element of the queue and returns it. If the queue is empty,
// return -1.
// Notice that when there are two middle position choices, the operation is performed on the
// frontmost middle position choice. For example:
// Pushing 6 into the middle of [1, 2, 3, 4, 5] results in [1, 2, 6, 3, 4, 5].
// Popping the middle from [1, 2, 3, 4, 5, 6] returns 3 and results in [1, 2, 4, 5, 6].
//
// Constraints:
// 1 <= val <= 10^9
// At most 1000 calls will be made to pushFront, pushMiddle, pushBack, popFront, popMiddle, and
// popBack.
public class FrontMiddleBackQueue {
    // Deque
    // 7 ms(77.11%), 39.5 MB(51.74%) for 94 tests
    static class FrontMiddleBackQueue1 {
        private Deque<Integer> front = new LinkedList<>();
        private Deque<Integer> end = new LinkedList<>();

        public FrontMiddleBackQueue1() {
        }

        public void pushFront(int val) {
            front.offerFirst(val);
            moveToEnd();
        }

        public void pushMiddle(int val) {
            front.offerLast(val);
            moveToEnd();
        }

        public void pushBack(int val) {
            end.offerLast(val);
            moveToFront();
        }

        public int popFront() {
            Integer res = front.pollFirst();
            if (res == null) {
                res = end.pollFirst();
                return (res == null) ? -1 : res;
            }
            moveToFront();
            return res;
        }

        public int popMiddle() {
            return (front.size() == end.size()) ? (front.isEmpty() ? -1 : front.pollLast()) :
                   end.pollFirst();
        }

        public int popBack() {
            Integer res = end.pollLast();
            if (res == null) { return -1; }

            moveToEnd();
            return res;
        }

        private void moveToFront() {
            if (end.size() > front.size() + 1) {
                front.offerLast(end.pollFirst());
            }
        }

        private void moveToEnd() {
            if (front.size() > end.size()) {
                end.offerFirst(front.pollLast());
            }
        }

        public void print() {
            System.out.println(front + ";" + end);
        }
    }

    // TODO: DoublyLinkedList

    void test1(String className) throws Exception {
        test(new String[] {className, "pushFront", "pushBack", "pushMiddle", "pushMiddle",
                           "popFront", "popMiddle", "popMiddle", "popBack", "popFront"},
             new Object[][] {{}, {1}, {2}, {3}, {4}, {}, {}, {}, {}, {}},
             new Object[] {null, null, null, null, null, 1, 3, 4, 2, -1});
        test(new String[] {className, "popMiddle", "popMiddle", "pushMiddle", "popBack", "popFront",
                           "popMiddle"}, new Object[][] {{}, {}, {}, {8}, {}, {}, {}},
             new Object[] {null, -1, -1, null, 8, -1, -1});
        test(new String[] {className, "popMiddle", "popMiddle", "pushMiddle", "pushMiddle",
                           "popMiddle", "popMiddle", "popMiddle", "popBack", "popMiddle",
                           "popFront", "pushBack", "popFront", "pushMiddle", "pushMiddle",
                           "popMiddle", "popBack", "pushFront", "popMiddle", "pushMiddle",
                           "pushMiddle", "pushMiddle", "popMiddle", "pushMiddle", "popBack",
                           "pushMiddle", "popMiddle", "popMiddle", "popMiddle", "popMiddle",
                           "popFront", "pushMiddle", "pushMiddle", "pushMiddle", "pushFront"},
             new Object[][] {{}, {}, {}, {773222}, {279355}, {}, {}, {}, {}, {}, {}, {448905}, {},
                             {168284}, {874541}, {}, {}, {15656}, {}, {803226}, {720129}, {626048},
                             {}, {860306}, {}, {630886}, {}, {}, {}, {}, {}, {837735}, {414354},
                             {404946}, {88719}},
             new Object[] {null, -1, -1, null, null, 279355, 773222, -1, -1, -1, -1, null, 448905,
                           null, null, 874541, 168284, null, 15656, null, null, null, 626048, null,
                           803226, null, 630886, 720129, 860306, -1, -1, null, null, null, null});
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
            test1("FrontMiddleBackQueue1");
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
