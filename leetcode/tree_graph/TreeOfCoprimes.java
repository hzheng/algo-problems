import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1766: https://leetcode.com/problems/tree-of-coprimes/
//
// There is a tree (i.e., a connected, undirected graph that has no cycles) consisting of n nodes
// numbered from 0 to n - 1 and exactly n - 1 edges. Each node has a value associated with it, and
// the root of the tree is node 0. To represent this tree, you are given an integer array nums and
// a 2D array edges. Each nums[i] represents the ith node's value, and each edges[j] = [uj, vj]
// represents an edge between nodes uj and vj in the tree.
// Two values x and y are coprime if gcd(x, y) == 1 where gcd(x, y) is the greatest common divisor
// of x and y.
// An ancestor of a node i is any other node on the shortest path from node i to the root. A node
// is not considered an ancestor of itself.
// Return an array ans of size n, where ans[i] is the closest ancestor to node i such that nums[i]
// and nums[ans[i]] are coprime, or -1 if there is no such ancestor.
//
// Constraints:
// nums.length == n
// 1 <= nums[i] <= 50
// 1 <= n <= 10^5
// edges.length == n - 1
// edges[j].length == 2
// 0 <= uj, vj < n
// uj != vj
public class TreeOfCoprimes {
    // TLE
    public int[] getCoprimes(int[] nums, int[][] edges) {
        int n = nums.length;
        int[] res = new int[n];
        Arrays.fill(res, -2);
        res[0] = -1;
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            graph.computeIfAbsent(u, x -> new HashSet<>()).add(v);
            graph.computeIfAbsent(v, x -> new HashSet<>()).add(u);
        }
        Map<Integer, Set<Integer>> factorMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            for (int f : getFactors(num)) {
                factorMap.computeIfAbsent(f, x -> new HashSet<>()).add(i);
            }
        }
        Queue<Integer> queue = new LinkedList<>();
        int[] parents = new int[n];
        parents[0] = -1;
        for (int f : getFactors(nums[0])) {
            factorMap.computeIfAbsent(f, x -> new HashSet<>()).add(0);
        }
        for (queue.offer(0); !queue.isEmpty(); ) {
            for (int size = queue.size(); size > 0; size--) {
                int cur = queue.poll();
                for (int nei : graph.get(cur)) {
                    if (res[nei] >= -1) { continue; }

                    parents[nei] = cur;
                    Set<Integer> factors = getFactors(nums[nei]);
                    outer:
                    for (int i = nei; ; ) {
                        i = parents[i];
                        for (int f : factors) {
                            if (factorMap.get(f).contains(i)) { continue outer; }
                        }
                        res[nei] = i;
                        break;
                    }
                    for (int f : factors) {
                        factorMap.computeIfAbsent(f, x -> new HashSet<>()).add(nei);
                    }
                    queue.offer(nei);
                }
            }
        }
        return res;
    }

    private Set<Integer> getFactors(int n) {
        Set<Integer> set = new HashSet<>();
        for (int i = 2; i <= n; i++) {
            if (n % i == 0) {
                set.add(i);
                for (n /= i; n % i == 0; n /= i) {}
            }
        }
        return set;
    }

    // DFS + Recursion + Hash Table + Set + Stack
    // time complexity: O(N), space complexity: O(N)
    // 395 ms(50.00%), 242.4 MB(50.00%) for 36 tests
    public int[] getCoprimes2(int[] nums, int[][] edges) {
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            graph.computeIfAbsent(u, x -> new HashSet<>()).add(v);
            graph.computeIfAbsent(v, x -> new HashSet<>()).add(u);
        }
        int[] res = new int[nums.length];
        Arrays.fill(res, -2);
        final int MAX = 51;
        Stack[] paths = new Stack[MAX];
        dfs(res, graph, nums, paths, 0, 0);
        return res;
    }

    private void dfs(int[] res, Map<Integer, Set<Integer>> graph, int[] nums, Stack<int[]>[] paths,
                     int cur, int depth) {
        if (res[cur] >= -1) { return; }

        int maxDepth = -1;
        res[cur] = -1;
        for (int a = 1; a < paths.length; a++) {
            if (gcd(nums[cur], a) != 1 || paths[a] == null || paths[a].isEmpty()) { continue; }

            int[] parent = paths[a].peek();
            if (parent[1] > maxDepth) {
                maxDepth = parent[1];
                res[cur] = parent[0];
            }
        }
        Stack<int[]> path = paths[nums[cur]];
        if (path == null) {
            path = paths[nums[cur]] = new Stack<>();
        }
        path.push(new int[] {cur, depth});
        for (int nei : graph.get(cur)) {
            dfs(res, graph, nums, paths, nei, depth + 1);
        }
        path.pop();
    }

    private int gcd(int a, int b) {
        if (a < b) { return gcd(b, a); }

        return (b == 0) ? a : gcd(a % b, b);
    }

    // BFS + Queue + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 132 ms(100.00%), 102.9 MB(50.00%) for 36 tests
    public int[] getCoprimes3(int[] nums, int[][] edges) {
        final int MAX = 51;
        boolean[][] coprimes = new boolean[MAX][MAX];
        for (int i = 1; i < MAX; i++) {
            for (int j = i; j < MAX; j++) {
                coprimes[i][j] = coprimes[j][i] = (gcd(i, j) == 1);
            }
        }
        int n = nums.length;
        Map<Integer, int[][]> paths = new HashMap<>();
        int[][] path = new int[MAX][2];
        for (int[] row : path) {
            Arrays.fill(row, -1);
        }
        paths.put(0, path);
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(0);
        int[] res = new int[n];
        int[] parents = new int[n];
        parents[0] = -1;
        for (int depth = 0; !queue.isEmpty(); depth++) {
            for (int size = queue.size(); size > 0; size--) {
                int cur = queue.poll();
                for (int nei : graph.get(cur)) {
                    if (nei != parents[cur]) {
                        parents[nei] = cur;
                        queue.offer(nei);
                    }
                }
                res[cur] = -1;
                if (cur == 0) { continue; }

                int parent = parents[cur];
                path = paths.get(parent).clone();
                path[nums[parent]] = new int[] {parent, depth - 1};
                for (int i = 1, maxDepth = -1; i < MAX; i++) {
                    if (coprimes[i][nums[cur]] && path[i][0] >= 0 && path[i][1] > maxDepth) {
                        maxDepth = path[i][1];
                        res[cur] = path[i][0];
                    }
                }
                paths.put(cur, path);
            }
        }
        return res;
    }

    private void test(int[] nums, int[][] edges, int[] expected) {
        assertArrayEquals(expected, getCoprimes(nums, edges));
        assertArrayEquals(expected, getCoprimes2(nums, edges));
        assertArrayEquals(expected, getCoprimes3(nums, edges));
    }

    @Test public void test() {
        test(new int[] {9, 16, 30, 23, 33, 35, 9, 47, 39, 46, 16, 38, 5, 49, 21, 44, 17, 1, 6, 37,
                        49, 15, 23, 46, 38, 9, 27, 3, 24, 1, 14, 17, 12, 23, 43, 38, 12, 4, 8, 17,
                        11, 18, 26, 22, 49, 14, 9},
             new int[][] {{17, 0}, {30, 17}, {41, 30}, {10, 30}, {13, 10}, {7, 13}, {6, 7},
                          {45, 10}, {2, 10}, {14, 2}, {40, 14}, {28, 40}, {29, 40}, {8, 29},
                          {15, 29}, {26, 15}, {23, 40}, {19, 23}, {34, 19}, {18, 23}, {42, 18},
                          {5, 42}, {32, 5}, {16, 32}, {35, 14}, {25, 35}, {43, 25}, {3, 43},
                          {36, 25}, {38, 36}, {27, 38}, {24, 36}, {31, 24}, {11, 31}, {39, 24},
                          {12, 39}, {20, 12}, {22, 12}, {21, 39}, {1, 21}, {33, 1}, {37, 1},
                          {44, 37}, {9, 44}, {46, 2}, {4, 46}},
             new int[] {-1, 21, 17, 43, 10, 42, 7, 13, 29, 44, 17, 31, 39, 10, 10, 29, 32, 0, 40,
                        23, 12, 39, 12, 40, 25, 35, 15, 38, 40, 40, 17, 24, 5, 1, 19, 14, 17, 21,
                        25, 24, 14, 17, 40, 25, 37, 17, 10});
        test(new int[] {2, 3, 3, 2}, new int[][] {{0, 1}, {1, 2}, {1, 3}}, new int[] {-1, 0, 0, 1});
        test(new int[] {5, 6, 10, 2, 3, 6, 15},
             new int[][] {{0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {2, 6}},
             new int[] {-1, 0, -1, 0, 0, 0, -1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
