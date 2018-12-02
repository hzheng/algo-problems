import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC946: https://leetcode.com/problems/validate-stack-sequences/
//
// Given two sequences pushed and popped with distinct values, return true if 
// and only if this could have been the result of a sequence of push and pop 
// operations on an initially empty stack.
public class ValidateStackSequences {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 43.77%(12 ms for 151 tests)
    public boolean validateStackSequences(int[] pushed, int[] popped) {
        int n = pushed.length;
        if (popped.length != n) return false;

        Stack<Integer> stack = new Stack<>();
        int i = 0;
        int j = 0;
        while (i < n && j < n) {
            if (!stack.empty() && stack.peek() == popped[j]) {
                stack.pop();
                j++;
                continue;
            }
            if (pushed[i] != popped[j]) {
                stack.push(pushed[i++]);
            } else {
                i++;
                j++;
            }
        }
        if (i < n) return false;

        while (!stack.isEmpty()) {
            if (stack.pop() != popped[j++]) return false;
        }
        return true;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 86.82%(9 ms for 151 tests)
    public boolean validateStackSequences2(int[] pushed, int[] popped) {
        int n = pushed.length;
        Stack<Integer> stack = new Stack<>();
        int i = 0;
        for (int x : pushed) {
            for (stack.push(x); !stack.isEmpty() && i < n && stack.peek() == popped[i]; i++) {
                stack.pop();
            }
        }
        return i == n;
    }

    void test(int[] pushed, int[] popped, boolean expected) {
        assertEquals(expected, validateStackSequences(pushed, popped));
        assertEquals(expected, validateStackSequences2(pushed, popped));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4, 5}, new int[] {4, 5, 3, 2, 1}, true);
        test(new int[] {1, 2, 3, 4, 5}, new int[] {4, 3, 5, 1, 2}, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
