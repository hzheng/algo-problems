import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1598: https://leetcode.com/problems/crawler-log-folder/
//
// The Leetcode file system keeps a log each time some user performs a change folder operation.
// The operations are described below:
// "../" : Move to the parent folder of the current folder. (If you are already in the main folder,
// remain in the same folder).
// "./" : Remain in the same folder.
// "x/" : Move to the child folder named x (This folder is guaranteed to always exist).
// You are given a list of strings logs where logs[i] is the operation performed by the user at the
// ith step.
// The file system starts in the main folder, then the operations in logs are performed.
// Return the minimum number of operations needed to go back to the main folder after the change
// folder operations.
public class MinOperations {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(90.96%), 38.4 MB(94.59%) for 98 tests
    public int minOperations(String[] logs) {
        Stack<Integer> stack = new Stack<>();
        for (String log : logs) {
            switch (log) {
            case "./":
                break;
            case "../":
                if (!stack.empty()) {
                    stack.pop();
                }
                break;
            default:
                stack.push(0);
                break;
            }
        }
        return stack.size();
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(90.96%), 38.4 MB(94.59%) for 98 tests
    public int minOperations2(String[] logs) {
        int res = 0;
        for (String log : logs) {
            switch (log) {
            case "./":
                break;
            case "../":
                res = Math.max(0, --res);
                break;
            default:
                res++;
                break;
            }
        }
        return res;
    }

    void test(String[] logs, int expected) {
        assertEquals(expected, minOperations(logs));
        assertEquals(expected, minOperations2(logs));
    }

    @Test public void test() {
        test(new String[] {"d1/", "d2/", "../", "d21/", "./"}, 2);
        test(new String[] {"d1/", "d2/", "./", "d3/", "../", "d31/"}, 3);
        test(new String[] {"d1/", "../", "../", "../"}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
