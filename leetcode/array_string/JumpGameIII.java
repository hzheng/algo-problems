import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1306: https://leetcode.com/problems/jump-game-iii/
//
// Given an array of non-negative integers arr, you are initially positioned at start index of the
// array. When you are at index i, you can jump to i + arr[i] or i - arr[i], check if you can reach
// to any index with value 0.
// Notice that you can not jump outside of the array at any time.
//
// Constraints:
// 1 <= arr.length <= 5 * 10^4
// 0 <= arr[i] < arr.length
// 0 <= start < arr.length
public class JumpGameIII {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(76.12%), 52.4 MB(24.14%) for 55 tests
    public boolean canReach(int[] arr, int start) {
        return canReach(arr, start, new boolean[arr.length]);
    }

    private boolean canReach(int[] arr, int cur, boolean[] visited) {
        if (arr[cur] == 0) { return true; }
        if (visited[cur]) { return false; }

        visited[cur] = true;
        int n = arr.length;
        for (int next : new int[] {cur + arr[cur], cur - arr[cur]}) {
            if (next >= 0 && next < n && canReach(arr, next, visited)) { return true; }
        }
        return false;
    }

    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(76.12%), 52.4 MB(24.14%) for 55 tests
    public boolean canReach2(int[] arr, int start) {
        return canReach2(arr, start, new boolean[arr.length]);
    }

    private boolean canReach2(int[] arr, int cur, boolean[] visited) {
        if (cur < 0 || cur >= arr.length || visited[cur]) { return false; }
        if (arr[cur] == 0) { return true; }

        visited[cur] = true;
        return canReach2(arr, cur + arr[cur], visited) || canReach2(arr, cur - arr[cur], visited);
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(47.87%), 46.6 MB(90.92%) for 55 tests
    public boolean canReach3(int[] arr, int start) {
        Queue<Integer> queue = new LinkedList<>();
        int n = arr.length;
        boolean[] visited = new boolean[n];
        for (queue.offer(start); !queue.isEmpty(); ) {
            int cur = queue.poll();
            if (arr[cur] == 0) { return true; }

            for (int next : new int[] {cur + arr[cur], cur - arr[cur]}) {
                if (next >= 0 && next < n && !visited[next]) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }
        return false;
    }

    private void test(int[] arr, int start, boolean expected) {
        assertEquals(expected, canReach(arr, start));
        assertEquals(expected, canReach2(arr, start));
        assertEquals(expected, canReach3(arr, start));
    }

    @Test public void test() {
        test(new int[] {4, 2, 3, 0, 3, 1, 2}, 5, true);
        test(new int[] {4, 2, 3, 0, 3, 1, 2}, 0, true);
        test(new int[] {3, 0, 2, 1, 2}, 2, false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
