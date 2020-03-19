import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1376: https://leetcode.com/problems/time-needed-to-inform-all-employees/
//
// A company has n employees with a unique ID for each employee from 0 to n - 1. The head of the company has is the one
// with headID. Each employee has one direct manager given in the manager array where manager[i] is the direct manager
// of the i-th employee, manager[headID] = -1. Also it's guaranteed that the subordination relationships have a tree
// structure. The head of the company wants to inform all the employees of the company of an urgent piece of news. He
// will inform his direct subordinates and they will inform their subordinates and so on until all employees know about
// the urgent news. The i-th employee needs informTime[i] minutes to inform all of his direct subordinates (i.e After
// informTime[i] minutes, all his direct subordinates can start spreading the news).
// Return the number of minutes needed to inform all the employees about the urgent news.
//
// Constraints:
// 1 <= n <= 10^5
// 0 <= headID < n
// manager.length == n
// 0 <= manager[i] < n
// manager[headID] == -1
// informTime.length == n
// 0 <= informTime[i] <= 1000
// informTime[i] == 0 if employee i has no subordinates.
public class TimeNeededToInformAllEmployees {
    // DFS + Recursion
    // time complexity: O(N), space complexity: O(N)
    // 91 ms(25.00%), 71.4 MB(100%) for 39 tests
    public int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int parent = manager[i];
            if (parent >= 0) {
                graph.computeIfAbsent(parent, x -> new ArrayList<>()).add(i);
            }
        }
        return dfs(graph, headID, informTime);
    }

    private int dfs(Map<Integer, List<Integer>> graph, int cur, int[] informTime) {
        List<Integer> employees = graph.get(cur);
        if (employees == null) return 0;

        int max = 0;
        for (int employee : employees) {
            max = Math.max(max, dfs(graph, employee, informTime));
        }
        return max + informTime[cur];
    }

    // BFS + Queue
    // time complexity: O(N), space complexity: O(N)
    // 68 ms(100%), 58.3 MB(100%) for 39 tests
    public int numOfMinutes2(int n, int headID, int[] manager, int[] informTime) {
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            if (manager[i] >= 0) {
                graph[manager[i]].add(i);
            }
        }
        Queue<int[]> queue = new LinkedList<>();
        int res = 0;
        for (queue.offer(new int[]{headID, 0}); !queue.isEmpty(); ) {
            int[] cur = queue.poll();
            int u = cur[0];
            int w = cur[1];
            res = Math.max(w, res);
            for (int v : graph[u]) {
                queue.offer(new int[]{v, w + informTime[u]});
            }
        }
        return res;
    }

    private void test(int n, int headID, int[] manager, int[] informTime, int expected) {
        assertEquals(expected, numOfMinutes(n, headID, manager, informTime));
        assertEquals(expected, numOfMinutes2(n, headID, manager, informTime));
    }

    @Test
    public void test() {
        test(1, 0, new int[]{-1}, new int[]{0}, 0);
        test(6, 2, new int[]{2, 2, -1, 2, 2, 2}, new int[]{0, 0, 1, 0, 0, 0}, 1);
        test(7, 6, new int[]{1, 2, 3, 4, 5, 6, -1}, new int[]{0, 6, 5, 4, 3, 2, 1}, 21);
        test(15, 0, new int[]{-1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6}, new int[]{1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0}, 3);
        test(4, 2, new int[]{3, 3, -1, 2}, new int[]{0, 0, 162, 914}, 1076);
        test(11, 4, new int[]{5, 9, 6, 10, -1, 8, 9, 1, 9, 3, 4}, new int[]{0, 213, 0, 253, 686, 170, 975, 0, 261, 309, 337}, 2560);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
