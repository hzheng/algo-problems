import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1206: https://leetcode.com/problems/design-skiplist/
//
// Design a Skiplist without using any built-in libraries.
// A Skiplist is a data structure that takes O(log(n)) time to add, erase and search. Comparing with
// treap and red-black tree which has the same function and performance, the code length of Skiplist
// can be comparatively short and the idea behind Skiplists are just simple linked lists.
// For example: we have a Skiplist containing [30,40,50,60,70,90] and we want to add 80 and 45 into
// it. The Skiplist works this way:
// https://commons.wikimedia.org/wiki/File:Skip_list_add_element-en.gif
// You can see there are many layers in the Skiplist. Each layer is a sorted linked list. With the
// help of the top layers, add, erase and search can be faster than O(n). It can be proven that the
// average time complexity for each operation is O(log(n)) and space complexity is O(n).
// To be specific, your design should include these functions:
// bool search(int target) : Return whether the target exists in the Skiplist or not.
// void add(int num): Insert a value into the SkipList.
// bool erase(int num): Remove a value in the Skiplist. If num does not exist in the Skiplist, do nothing and return false. If there exists multiple num values, removing any one of them is fine.
// See more about Skiplist : https://en.wikipedia.org/wiki/Skip_list
// Note that duplicates may exist in the Skiplist, your code needs to handle this situation.
//
// Constraints:
//
// 0 <= num, target <= 20000
// At most 50000 calls will be made to search, add, and erase.
public class Skiplist {
    // 15 ms(88.71%), 46.6 MB(33.23%) for 18 tests
    static class Skiplist1 {
        private static final int MAX_LAYER = 32;

        private static class Node {
            private final int val;
            private final Node[] next = new Node[MAX_LAYER + 1];
            private final int height;
            private int count = 1;

            Node(int val, int height) {
                this.val = val;
                this.height = height;
            }
        }

        private final Node head = new Node(Integer.MIN_VALUE, MAX_LAYER);
        private int topLevel;
        private static final double P = 0.5;
        private final Node[] prevNodes = new Node[MAX_LAYER + 1];
        private final Random rand = new Random();

        public Skiplist1() {
        }

        // time complexity: O(log(N))
        public boolean search(int target) {
            Node prev = lowestPrevNode(target);
            return prev.next[0] != null && prev.next[0].val == target;
        }

        // time complexity: O(log(N))
        public void add(int num) {
            Node prev = lowestPrevNode(num);
            if (prev.next[0] != null && prev.next[0].val == num) {
                prev.next[0].count++;
                return;
            }
            Node newNode = new Node(num, flipCoin());
            while (topLevel < newNode.height) {
                prevNodes[++topLevel] = head;
            }
            for (int i = 0; i <= newNode.height; i++) {
                newNode.next[i] = prevNodes[i].next[i];
                prevNodes[i].next[i] = newNode;
            }
        }

        // time complexity: O(log(N))
        public boolean erase(int num) {
            Node prev = lowestPrevNode(num);
            if (prev.next[0] == null || prev.next[0].val != num) { return false; }
            if (--prev.next[0].count > 0) { return true; }

            for (int i = topLevel; i >= 0; i--) {
                Node cur = prevNodes[i];
                if (cur.next[i] != null && cur.next[i].val == num) {
                    cur.next[i] = cur.next[i].next[i];
                }
                if (cur == head && cur.next[i] == null) {
                    topLevel--;
                }
            }
            return true;
        }

        private int flipCoin() {
            return Integer.numberOfTrailingZeros(rand.nextInt());
        }

        private int flipCoin2() {
            int res = 1;
            for (; ThreadLocalRandom.current().nextFloat() < P && res < MAX_LAYER; res++) {}
            return res;
        }

        private Node lowestPrevNode(int num) {
            Node cur = head;
            for (int i = topLevel; i >= 0; i--) {
                for (; cur.next[i] != null && cur.next[i].val < num; cur = cur.next[i]) {}
                prevNodes[i] = cur;
            }
            return cur;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "add", "add", "add", "search", "add", "search", "erase",
                           "erase", "search"},
             new Object[][] {null, {1}, {2}, {3}, {0}, {4}, {1}, {0}, {1}, {1}},
             new Object[] {null, null, null, null, false, null, true, false, true, false});
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
            test1("Skiplist1");
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
