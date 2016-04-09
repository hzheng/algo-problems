import java.util.Stack;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 3.6:
 * Sort a stack in ascending order(biggest items on top). You may use at most
 * one additional stack to hold items, but you may not copy the elements into
 * any other data structure(such as an array).
 */
public class StackSort {
    // time complexity: O(N^2), space complexity: O(N)
    public static void sort(Stack<Integer> stack) {
        if (stack == null) return;

        Stack<Integer> buffer = new Stack<Integer>();
        while (!stack.empty()) {
            int stackTop = stack.pop();
            while (!buffer.empty() && (buffer.peek() < stackTop)) {
                stack.push(buffer.pop());
            }
            buffer.push(stackTop);
        }
        while (!buffer.empty()) {
            stack.push(buffer.pop());
        }
    }

    void test(int[] n, int[] expected) {
        Stack<Integer> stack = new Stack<Integer>();
        for (int i : n) {
            stack.push(i);
        }
        sort(stack);
        assertArrayEquals(expected, stack.stream().mapToInt(i->i).toArray());
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[] {1});
    }

    @Test
    public void test2() {
        test(new int[] {1, 2}, new int[] {1, 2});
    }

    @Test
    public void test3() {
        test(new int[] {1, 1}, new int[] {1, 1});
    }

    @Test
    public void test4() {
        test(new int[] {2, 1}, new int[] {1, 2});
    }

    @Test
    public void test5() {
        test(new int[] {3, 2, 1}, new int[] {1, 2, 3});
    }

    @Test
    public void test6() {
        test(new int[] {1, 3, 2, 7, 8, 9, 5, 6, 0, 4},
             new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
    }

    @Test
    public void test7() {
        test(new int[] {8, 3, 2, 7, 8, 9, 5, 6, 0, 4},
             new int[] {0, 2, 3, 4, 5, 6, 7, 8, 8, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StackSort");
    }
}
