import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1665: https://leetcode.com/problems/minimum-initial-energy-to-finish-tasks/
//
// You are given an array tasks where tasks[i] = [actual_i, minimum_i]:
// actual_i is the actual amount of energy you spend to finish the ith task.
// minimum_i is the minimum amount of energy you require to begin the ith task.
// You can finish the tasks in any order you like.
// Return the minimum initial amount of energy you will need to finish all the tasks.
//
// Constraints:
// 1 <= tasks.length <= 10^5
// 1 <= actual_i <= minimum_i <= 10^4
public class MinimumEffort {
    // Sort + Binary Search
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 34 ms(49.93%), 96.5 MB(82.82%) for 34 tests
    public int minimumEffort(int[][] tasks) {
        Arrays.sort(tasks, (a, b) -> b[1] - b[0] - a[1] + a[0]);
        int low = 0;
        for (int high = 1000_000_000; low < high; ) {
            int mid = (low + high) >>> 1;
            if (ok(tasks, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean ok(int[][] tasks, int energy) {
        for (int[] task : tasks) {
            if (energy < task[1]) { return false; }

            energy -= task[0];
        }
        return true;
    }

    // Sort + Greedy
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 21 ms(89.58%), 96.9 MB(66.85%) for 34 tests
    public int minimumEffort2(int[][] tasks) {
        Arrays.sort(tasks, (a, b) -> b[1] - b[0] - a[1] + a[0]);
        int res = 0;
        int saved = 0;
        for (int[] task : tasks) {
            int min = task[1];
            if (min > saved) {
                res += (min - saved);
                saved = min;
            }
            saved -= task[0];
        }
        return res;
    }

    // Sort + Greedy
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 21 ms(89.58%), 96.6 MB(76.46%) for 34 tests
    public int minimumEffort3(int[][] tasks) {
        Arrays.sort(tasks, (a, b) -> a[1] - a[0] - b[1] + b[0]);
        int res = 0;
        for (int[] task : tasks) {
            res = Math.max(res + task[0], task[1]);
        }
        return res;
    }

    private void test(int[][] tasks, int expected) {
        assertEquals(expected, minimumEffort(tasks));
        assertEquals(expected, minimumEffort2(tasks));
        assertEquals(expected, minimumEffort3(tasks));
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {2, 4}, {4, 8}}, 8);
        test(new int[][] {{1, 3}, {2, 4}, {10, 11}, {10, 12}, {8, 9}}, 32);
        test(new int[][] {{1, 7}, {2, 8}, {3, 9}, {4, 10}, {5, 11}, {6, 12}}, 27);
        test(new int[][] {{1, 2}, {1, 7}, {2, 3}, {5, 9}, {2, 2}}, 11);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
