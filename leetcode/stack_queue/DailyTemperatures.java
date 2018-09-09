import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC739: https://leetcode.com/problems/daily-temperatures/
//
// Given a list of daily temperatures, produce a list that, for each day in the
// input, tells you how many days you would have to wait until a warmer
// temperature. If there is no such future day, put 0 instead.
// Note: The length of temperatures will be in the range [1, 30000]. 
// Each temperature will be an integer in the range [30, 100].
public class DailyTemperatures {
    // Stack
    // time complexity: O(N), space complexity: O(W)
    // beats 23.45%(89 ms for 37 tests)
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] res = new int[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            int cur = temperatures[i];
            while (!stack.isEmpty()) {
                int j = stack.peek();
                if (temperatures[j] < cur) {
                    stack.pop();
                    res[j] = i - j;
                } else break;
            }
            stack.push(i);
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(W)
    // beats 40.01%(73 ms for 37 tests)
    public int[] dailyTemperatures2(int[] temperatures) {
        int n = temperatures.length;
        int[] res = new int[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty()
                   && temperatures[i] >= temperatures[stack.peek()]) {
                stack.pop();
            }
            res[i] = stack.isEmpty() ? 0 : (stack.peek() - i);
            stack.push(i);
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(W)
    // beats 53.11%(63 ms for 37 tests)
    public int[] dailyTemperatures3(int[] temperatures) {
        int n = temperatures.length;
        int[] res = new int[n];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while(!stack.isEmpty()
                  && temperatures[i] > temperatures[stack.peek()]) {
                int j = stack.pop();
                res[j] = i - j;
            }
            stack.push(i);
        }
        return res;
    }

    // Stack in Array
    // time complexity: O(N), space complexity: O(W)
    // beats 92.28%(10 ms for 37 tests)
    public int[] dailyTemperatures4(int[] temperatures) {
        int n = temperatures.length;
        int[] stack = new int[n];
        int[] res = new int[n];
        for (int i = 0, top = -1; i < n; i++) {
            while (top > -1 && temperatures[i] > temperatures[stack[top]]) {
                int j = stack[top--];
                res[j] = i - j;
            }
            stack[++top] = i;
        }
        return res;
    }

    // Next Array
    // time complexity: O(N * W), space complexity: O(N + W)
    // beats 90.50%(12 ms for 37 tests)
    public int[] dailyTemperatures5(int[] temperatures) {
        int n = temperatures.length;
        int[] res = new int[n];
        int[] next = new int[101];
        Arrays.fill(next, Integer.MAX_VALUE);
        for (int i = n - 1; i >= 0; --i) {
            int warmer = Integer.MAX_VALUE;
            for (int t = temperatures[i] + 1; t <= 100; t++) {
                warmer = Math.min(warmer, next[t]);
            }
            if (warmer < Integer.MAX_VALUE) {
                res[i] = warmer - i;
            }
            next[temperatures[i]] = i;
        }
        return res;
    }

    void test(int[] temperatures, int[] expected) {
        assertArrayEquals(expected, dailyTemperatures(temperatures));
        assertArrayEquals(expected, dailyTemperatures2(temperatures));
        assertArrayEquals(expected, dailyTemperatures3(temperatures));
        assertArrayEquals(expected, dailyTemperatures4(temperatures));
        assertArrayEquals(expected, dailyTemperatures5(temperatures));
    }

    @Test
    public void test() {
        test(new int[] { 73, 74, 75, 71, 69, 72, 76, 73 }, 
             new int[] { 1, 1, 4, 2, 1, 1, 0, 0 });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
