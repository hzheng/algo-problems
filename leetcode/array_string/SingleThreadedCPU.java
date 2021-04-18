import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1834: https://leetcode.com/problems/single-threaded-cpu/
//
// You are given n tasks labeled from 0 to n - 1 represented by a 2D integer array tasks, where
// tasks[i] = [enqueueTimei, processingTimei] means that the ith task will be available to process
// at enqueueTimei and will take processingTimei to finish processing.
// You have a single-threaded CPU that can process at most one task at a time and will act in the
// following way:
// If the CPU is idle and there are no available tasks to process, the CPU remains idle.
// If the CPU is idle and there are available tasks, the CPU will choose the one with the shortest
// processing time. If multiple tasks have the same shortest processing time, it will choose the
// task with the smallest index.
// Once a task is started, the CPU will process the entire task without stopping.
// The CPU can finish a task then start a new one instantly.
// Return the order in which the CPU will process the tasks.
//
// Constraints:
// tasks.length == n
// 1 <= n <= 10^5
// 1 <= enqueueTimei, processingTimei <= 10^9
public class SingleThreadedCPU {
    // Heap + SortedMap
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 174 ms(14.29%), 86.3 MB(14.29%) for 34 tests
    public int[] getOrder(int[][] tasks) {
        int n = tasks.length;
        int[] res = new int[n];
        TreeMap<Integer, List<int[]>> map = new TreeMap<>();
        PriorityQueue<int[]> pq =
                new PriorityQueue<>((a, b) -> (a[0] == b[0]) ? a[1] - b[1] : a[0] - b[0]);
        for (int i = 0; i < n; i++) {
            int[] task = tasks[i];
            int t = task[0];
            int duration = task[1];
            map.computeIfAbsent(t, x -> new ArrayList<>()).add(new int[] {duration, i});
        }
        for (int cpuTime = map.firstKey(), i = 0; i < n; i++) {
            while (!map.isEmpty()) {
                var cur = map.firstEntry();
                int t = cur.getKey();
                if (t > cpuTime) {
                    if (!pq.isEmpty()) { break; }

                    cpuTime = t;
                }
                map.pollFirstEntry();
                pq.addAll(cur.getValue());
            }
            int[] top = pq.poll();
            cpuTime += top[0];
            res[i] = top[1];
        }
        return res;
    }

    // Heap + Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 103 ms(57.14%), 96.9 MB(14.29%) for 34 tests
    public int[] getOrder2(int[][] tasks) {
        int n = tasks.length;
        int[] res = new int[n];
        int[][] indexedTasks = new int[n][];
        for (int i = 0; i < n; i++) {
            indexedTasks[i] = Arrays.copyOf(tasks[i], 3);
            indexedTasks[i][2] = i;
        }
        Arrays.sort(indexedTasks, Comparator.comparingInt(a -> a[0]));
        PriorityQueue<int[]> pq =
                new PriorityQueue<>((a, b) -> a[1] == b[1] ? a[2] - b[2] : a[1] - b[1]);
        for (int i = 0, j = 0, time = 0; i < n; ) {
            for (; j < n && indexedTasks[j][0] <= time; j++) {
                pq.offer(indexedTasks[j]);
            }
            if (pq.isEmpty()) {
                time = indexedTasks[j][0];
                continue;
            }
            int[] top = pq.poll();
            time += top[1];
            res[i++] = top[2];
        }
        return res;
    }

    private void test(int[][] tasks, int[] expected) {
        assertArrayEquals(expected, getOrder(tasks));
        assertArrayEquals(expected, getOrder2(tasks));
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {2, 4}, {3, 2}, {4, 1}}, new int[] {0, 2, 3, 1});
        test(new int[][] {{7, 10}, {7, 12}, {7, 5}, {7, 4}, {7, 2}}, new int[] {4, 3, 2, 0, 1});
        test(new int[][] {{100, 100}, {1000000000, 1000000000}}, new int[] {0, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
