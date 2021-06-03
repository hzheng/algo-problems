import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1882: https://leetcode.com/problems/process-tasks-using-servers/
//
// You are given two 0-indexed integer arrays servers and tasks of lengths n and m respectively.
// servers[i] is the weight of the ith server, and tasks[j] is the time needed to process the jth
// task in seconds.
// You are running a simulation system that will shut down after all tasks are processed. Each
// server can only process one task at a time. You will be able to process the jth task starting
// from the jth second beginning with the 0th task at second 0. To process task j, you assign it to
// the server with the smallest weight that is free, and in case of a tie, choose the server with
// the smallest index. If a free server gets assigned task j at second t, it will be free again at
// the second t + tasks[j].
// If there are no free servers, you must wait until one is free and execute the free tasks as soon
// as possible. If multiple tasks need to be assigned, assign them in order of increasing index.
// You may assign multiple tasks at the same second if there are multiple free servers.
// Build an array ans of length m, where ans[j] is the index of the server the jth task will be
// assigned to.
// Return the array ans.
//
// Constraints:
// servers.length == n
// tasks.length == m
// 1 <= n, m <= 2 * 10^5
// 1 <= servers[i], tasks[j] <= 2 * 10^5
public class AssignTasks {
    // Heap
    // time complexity: O((M+N)*log(N)), space complexity: O(M+N)
    // 455 ms(24.27%), 175.5 MB(5.23%) for 36 tests
    public int[] assignTasks(int[] servers, int[] tasks) {
        PriorityQueue<int[]> freeServers =
                new PriorityQueue<>((a, b) -> (a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]));
        for (int i = servers.length - 1; i >= 0; i--) {
            freeServers.offer(new int[] {0, servers[i], i});
        }
        PriorityQueue<int[]> runningServers = new PriorityQueue<>(
                (a, b) -> a[0] != b[0] ? a[0] - b[0] : (a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]));
        int m = tasks.length;
        int[] res = new int[m];
        for (int i = 0, time = 0; i < m; i++) {
            time = Math.max(time, i);
            while (!runningServers.isEmpty()) { // release all finished servers
                int[] cur = runningServers.peek();
                if (cur[0] > time) { break; }

                freeServers.offer(runningServers.poll());
            }
            if (freeServers.isEmpty()) { // if no free servers, jump to the next time
                int[] cur = runningServers.poll();
                time = cur[0];
                freeServers.offer(cur);
            }
            int[] cur = freeServers.poll();
            res[i] = cur[2];
            cur[0] = time + tasks[i];
            runningServers.offer(cur);
        }
        return res;
    }

    // Heap
    // time complexity: O((M+N)*log(N)), space complexity: O(M+N)
    // 417 ms(31.38%), 139.7 MB(36.84%) for 36 tests
    public int[] assignTasks2(int[] servers, int[] tasks) {
        PriorityQueue<int[]> freeServers =
                new PriorityQueue<>((a, b) -> (a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]));
        PriorityQueue<int[]> runningServers = new PriorityQueue<>(
                (a, b) -> a[0] != b[0] ? a[0] - b[0] : (a[1] != b[1] ? a[1] - b[1] : a[2] - b[2]));
        for (int i = servers.length - 1; i >= 0; i--) {
            freeServers.offer(new int[] {0, servers[i], i});
        }
        int m = tasks.length;
        int[] res = new int[m];
        for (int i = 0; i < m; i++) {
            while (!runningServers.isEmpty() && runningServers.peek()[0] <= i) {
                freeServers.offer(runningServers.poll());
            }
            int[] cur = freeServers.poll();
            if (cur != null) {
                cur[0] = i + tasks[i];
            } else {
                cur = runningServers.poll();
                cur[0] += tasks[i];
            }
            res[i] = cur[2];
            runningServers.offer(cur);
        }
        return res;
    }

    private void test(int[] servers, int[] tasks, int[] expected) {
        assertArrayEquals(expected, assignTasks(servers, tasks));
        assertArrayEquals(expected, assignTasks2(servers, tasks));
    }

    @Test public void test1() {
        test(new int[] {3, 3, 2}, new int[] {1, 2, 3, 2, 1, 2}, new int[] {2, 2, 0, 2, 1, 2});
        test(new int[] {5, 1, 4, 3, 2}, new int[] {2, 1, 2, 4, 5, 2, 1},
             new int[] {1, 4, 1, 4, 1, 3, 2});
        test(new int[] {5, 5}, new int[] {3, 5, 3, 1}, new int[] {0, 1, 0, 0});
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(MaxFrequency.Function<int[], int[], int[]> assignTasks) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int[] servers = Utils.readIntArray(scanner.nextLine());
                int[] tasks = Utils.readIntArray(scanner.nextLine());
                int[] res = assignTasks.apply(servers, tasks);
                int[] expected = Utils.readIntArray(scanner.nextLine());
                assertArrayEquals(expected, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test2() {
        AssignTasks a = new AssignTasks();
        test(a::assignTasks);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
