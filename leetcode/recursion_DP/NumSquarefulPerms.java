import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC996: https://leetcode.com/problems/number-of-squareful-arrays/
//
// Given an array A of non-negative integers, the array is squareful if for every pair of adjacent
// elements, their sum is a perfect square. Return the number of permutations of A that are
// squareful. Two permutations A1 and A2 differ if and only if there is some index i such that
// A1[i] != A2[i].
//
// Note:
// 1 <= A.length <= 12
// 0 <= A[i] <= 1e9
public class NumSquarefulPerms {
    // Recursion + DFS + Backtracking + Hash Table + Set
    // time complexity: O(N!), space complexity: O(N)
    // 3 ms(22.49%), 36.6 MB(34.39%) for 76 tests
    public int numSquarefulPerms(int[] A) {
        Map<Integer, Integer> count = new HashMap<>();
        int n = A.length;
        for (int a : A) {
            count.put(a, count.getOrDefault(a, 0) + 1);
        }
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isPerfect(A[i] + A[j])) {
                    addEdge(graph, A, i, j);
                    addEdge(graph, A, j, i);
                }
            }
        }
        Set<List<Integer>> res = new HashSet<>();
        for (int i : graph.keySet()) {
            dfs(res, new LinkedList<>(), A, graph, i, count);
        }
        return res.size();
    }

    private void addEdge(Map<Integer, List<Integer>> graph, int[] A, int i, int j) {
        List<Integer> nei = graph.computeIfAbsent(i, k -> new ArrayList<>());
        Set<Integer> values = new HashSet<>();
        for (int k : nei) {
            values.add(A[k]);
        }
        if (!values.contains(A[j])) {
            nei.add(j);
        }
    }

    private void dfs(Set<List<Integer>> res, Deque<Integer> path, int[] A,
                     Map<Integer, List<Integer>> graph, int cur, Map<Integer, Integer> count) {
        if (path.size() == A.length) {
            List<Integer> perm = new ArrayList<>();
            for (int p : path) {
                perm.add(A[p]);
            }
            res.add(perm);
            return;
        }
        for (int next : graph.getOrDefault(cur, Collections.emptyList())) {
            if (count.get(A[next]) <= 0) { continue; }

            count.put(A[next], count.get(A[next]) - 1);
            path.offerLast(next);
            dfs(res, path, A, graph, next, count);
            count.put(A[next], count.get(A[next]) + 1);
            path.pollLast();
        }
    }

    private boolean isPerfect(int n) {
        int root = (int)Math.sqrt(n);
        return root * root == n;
    }

    // Recursion + DFS + Backtracking + Sort + Set
    // time complexity: O(N!), space complexity: O(N)
    // 0 ms(100.00%), 36.6 MB(34.39%) for 76 tests
    public int numSquarefulPerms2(int[] A) {
        Arrays.sort(A);
        int[] res = new int[1];
        dfs(res, A, new ArrayList<>(), new boolean[A.length], -1);
        return res[0];
    }

    private void dfs(int[] res, int[] A, List<Integer> cur, boolean[] visited, int last) {
        if (cur.size() == A.length) {
            res[0]++;
            return;
        }
        for (int i = 0; i < A.length; i++) {
            if (visited[i] || (i > 0 && A[i] == A[i - 1] && !visited[i - 1])) { continue; }
            if (last >= 0 && !isPerfect(A[i] + last)) { continue; }

            visited[i] = true;
            cur.add(A[i]);
            dfs(res, A, cur, visited, A[i]);
            visited[i] = false;
            cur.remove(cur.size() - 1);
        }
    }

    // Recursion + DFS + Sort
    // time complexity: O(N!), space complexity: O(N)
    // 0 ms(100.00%), 36.6 MB(34.39%) for 76 tests
    public int numSquarefulPerms3(int[] A) {
        Arrays.sort(A);
        int[] res = new int[1];
        dfs(res, A, 0);
        return res[0];
    }

    private void dfs(int[] res, int[] A, int cur) {
        int n = A.length;
        if (cur == n) {
            res[0]++;
            return;
        }
        for (int i = cur; i < n; i++) {
            if ((i != cur) && (A[i] == A[cur])) { continue; }

            swap(A, i, cur);
            if (cur == 0 || isPerfect(A[cur] + A[cur - 1])) {
                dfs(res, A.clone(), cur + 1);
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // Recursion + DFS + Backtracking + Hash Table + Set
    // time complexity: O(N!), space complexity: O(N)
    // 1 ms(85.19%), 36.8 MB(24.87%) for 76 tests
    public int numSquarefulPerms4(int[] A) {
        Map<Integer, Integer> count = new HashMap<>();
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int a : A) {
            int cnt = count.getOrDefault(a, 0);
            count.put(a, cnt + 1);
            if (cnt == 0) {
                graph.put(a, new HashSet<>());
            }
        }
        for (int u : count.keySet()) {
            for (int v : count.keySet()) {
                if (isPerfect(u + v)) {
                    graph.get(u).add(v);
                    graph.get(v).add(u);
                }
            }
        }
        int[] res = new int[1];
        for (int num : count.keySet()) {
            countPerm(res, num, A.length - 1, graph, count);
        }
        return res[0];
    }

    private void countPerm(int[] res, int a, int left, Map<Integer, Set<Integer>> graph,
                           Map<Integer, Integer> count) {
        count.put(a, count.get(a) - 1);
        if (left == 0) {
            res[0]++;
        } else {
            for (int next : graph.get(a)) {
                if (count.get(next) > 0) {
                    countPerm(res, next, left - 1, graph, count);
                }
            }
        }
        count.put(a, count.get(a) + 1);
    }

    // Recursion + DFS + Backtracking + Sort
    // time complexity: O(N!), space complexity: O(N)
    // 0 ms(100.00%), 36.2 MB(83.60%) for 76 tests
    public int numSquarefulPerms5(int[] A) {
        Arrays.sort(A);
        int[] res = new int[1];
        dfs(res, A, A.length, -1);
        return res[0];
    }

    private void dfs(int[] res, int[] A, int left, int prev) {
        if (left == 0) {
            res[0]++;
            return;
        }
        for (int i = 0; i < A.length; i++) {
            if (A[i] < 0 || (i > 0 && A[i] == A[i - 1])) { continue; }

            if (prev >= 0 && !isPerfect(A[i] + prev)) { continue; }

            int tmp = A[i];
            A[i] = -1;
            dfs(res, A, left - 1, tmp);
            A[i] = tmp;
        }
    }

    // 2-D Dynamic Programming(Bottom-Up) + Bit Manipulation + Hash Table
    // time complexity: O(N^2*2^N), space complexity: O(N*2^N)
    // 19 ms(9.52%), 39 MB(6.35%) for 76 tests
    public int numSquarefulPerms6(int[] A) {
        int n = A.length;
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isPerfect(A[i] + A[j])) {
                    graph.computeIfAbsent(i, x -> new ArrayList<>()).add(j);
                    graph.computeIfAbsent(j, x -> new ArrayList<>()).add(i);
                }
            }
        }
        int N = 1 << n;
        int[][] dp = new int[N][n];
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 1;
        }
        for (int mask = 3; mask < N; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) { continue; }

                for (int j : graph.getOrDefault(i, Collections.emptyList())) {
                    if ((mask & (1 << j)) != 0) {
                        dp[mask][i] += dp[mask ^ (1 << i)][j];
                    }
                }
            }
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            res += dp[N - 1][i];
        }
        for (int i = 0; i < n; i++) {
            int duplicates = 1;
            for (int j = i + 1; j < n; j++) {
                if (A[i] == A[j]) {
                    duplicates++;
                }
            }
            res /= duplicates;
        }
        return res;
    }

    private void test(int[] A, int expected) {
        assertEquals(expected, numSquarefulPerms(A));
        assertEquals(expected, numSquarefulPerms2(A));
        assertEquals(expected, numSquarefulPerms3(A));
        assertEquals(expected, numSquarefulPerms4(A));
        assertEquals(expected, numSquarefulPerms5(A));
        assertEquals(expected, numSquarefulPerms6(A));
    }

    @Test public void test() {
        test(new int[] {1, 17, 8}, 2);
        test(new int[] {2, 2, 2}, 1);
        test(new int[] {1, 1}, 0);
        test(new int[] {5, 11, 5, 4, 5}, 2);
        test(new int[] {2, 2, 7, 7, 2}, 6);
        test(new int[] {2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
