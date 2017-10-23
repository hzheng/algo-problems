import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC636: https://leetcode.com/problems/exclusive-time-of-functions/
//
// Given the running logs of n functions that are executed in a nonpreemptive
// single threaded CPU, find the exclusive time of these functions.
// Each function has a unique id, start from 0 to n-1. A function may be called
// recursively or by another function.
// A log is a string has this format : function_id:start_or_end:timestamp.
// For example, "0:start:0" means function 0 starts from the very beginning of
// time 0. "0:end:0" means function 0 ends to the very end of time 0.
// Exclusive time of a function is defined as the time spent within this
// function, the time spent by calling other functions should not be considered
// as this function's exclusive time. You should return the exclusive time of
// each function sorted by their function id.
public class ExclusiveTimeOfFunctions {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 92.30%(40 ms for 120 tests)
    public int[] exclusiveTime(int n, List<String> logs) {
        int[] res = new int[n];
        ArrayDeque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[] {0, 0}); // avoid empty stack
        for (String log : logs) {
            String[] sections = log.split(":");
            int time = Integer.valueOf(sections[2]);
            if ("start".equals(sections[1])) {
                int[] top = stack.peek();
                res[top[0]] += time - top[1];
                stack.push(new int[] {Integer.valueOf(sections[0]), time});
            } else {
                int[] top = stack.pop();
                res[top[0]] += time - top[1] + 1;
                stack.peek()[1] = time + 1;
            }
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 43.32%(46 ms for 120 tests)
    public int[] exclusiveTime2(int n, List < String > logs) {
        int[] res = new int[n];
        Stack<Integer> stack = new Stack<>();
        stack.push(0); // avoid empty stack
        int prev = 0;
        for (String log : logs) {
            String[] sections = log.split(":");
            int time = Integer.valueOf(sections[2]);
            if ("start".equals(sections[1])) {
                res[stack.peek()] += time - prev;
                stack.push(Integer.parseInt(sections[0]));
                prev = time;
            } else {
                res[stack.pop()] += time - prev + 1;
                prev = time + 1;
            }
        }
        return res;
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 92.30%(40 ms for 120 tests)
    public int[] exclusiveTime3(int n, List<String> logs) {
        int[] res = new int[n];
        int prev = 0;
        int running = 0;
        Stack<Integer> stack = new Stack<>();
        for (String log : logs) {
            String[] sections = log.split(":");
            int time = Integer.parseInt(sections[2]);
            boolean isStart = sections[1].equals("start");
            if (!isStart) {
                time++;
            }
            res[running] += (time - prev);
            if (isStart) {
                stack.push(running);
                running = Integer.parseInt(sections[0]);
            } else {
                running = stack.pop();
            }
            prev = time;
        }
        return res;
    }

    void test(int n, String[] logs, int[] expected) {
        assertArrayEquals(expected, exclusiveTime(n, Arrays.asList(logs)));
        assertArrayEquals(expected, exclusiveTime2(n, Arrays.asList(logs)));
        assertArrayEquals(expected, exclusiveTime3(n, Arrays.asList(logs)));
    }

    @Test
    public void test() {
        test(2, new String[] {"0:start:0", "1:start:2", "1:end:5", "0:end:6"},
             new int[] {3, 4});
        test(3, new String[] {"0:start:0", "1:start:2", "2:start:4", "2:end:7",
                              "1:end:9", "0:end:12"}, new int[] {5, 4, 4});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
