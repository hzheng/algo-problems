import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1722: https://leetcode.com/problems/minimize-hamming-distance-after-swap-operations/
//
// You are given two integer arrays, source and target, both of length n. You are also given an
// array allowedSwaps where each allowedSwaps[i] = [ai, bi] indicates that you are allowed to swap
// the elements at index ai and index bi (0-indexed) of array source. Note that you can swap
// elements at a specific pair of indices multiple times and in any order. The Hamming distance of
// two arrays of the same length, source and target, is the number of positions where the elements
// are different. Formally, it is the number of indices i for 0 <= i <= n-1 where
// source[i] != target[i] (0-indexed). Return the minimum Hamming distance of source and target
// after performing any amount of swap operations on array source.
//
// Constraints:
// n == source.length == target.length
// 1 <= n <= 105
// 1 <= source[i], target[i] <= 105
// 0 <= allowedSwaps.length <= 105
// allowedSwaps[i].length == 2
// 0 <= ai, bi <= n - 1
// ai != bi
public class MinimumHammingDistance {
    // Union Find + Hash Table
    // time complexity: O(N+S), space complexity: O(N+S)
    // 60 ms(85.90%), 77.9 MB(92.56%) for 70 tests
    public int minimumHammingDistance(int[] source, int[] target, int[][] allowedSwaps) {
        int n = source.length;
        int[] id = new int[n];
        Arrays.fill(id, -1);
        for (int[] s : allowedSwaps) {
            union(id, s[0], s[1]);
        }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int p = root(id, i);
            map.computeIfAbsent(p, a -> new ArrayList<>()).add(i);
        }
        int res = 0;
        for (List<Integer> indices : map.values()) {
            res += diff(indices, source, target);
        }
        return res;
    }

    private int diff(List<Integer> list, int[] a, int[] b) {
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : list) {
            map.put(a[i], map.getOrDefault(a[i], 0) + 1);
            map.put(b[i], map.getOrDefault(b[i], 0) - 1);
        }
        for (int v : map.values()) {
            res += Math.abs(v);
        }
        return res / 2;
    }

    // Union Find + Hash Table
    // time complexity: O(N+S), space complexity: O(N+S)
    // 51 ms(92.01%), 82 MB(79.80%) for 70 tests
    public int minimumHammingDistance2(int[] source, int[] target, int[][] allowedSwaps) {
        int n = source.length;
        int[] id = new int[n];
        Arrays.fill(id, -1);
        for (int[] s : allowedSwaps) {
            union(id, s[0], s[1]);
        }
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int p = root(id, i);
            int src = source[i];
            Map<Integer, Integer> countMap = map.computeIfAbsent(p, a -> new HashMap<>());
            countMap.put(src, countMap.getOrDefault(src, 0) + 1);
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            int p = root(id, i);
            int tgt = target[i];
            Map<Integer, Integer> countMap = map.get(p);
            int count = countMap.getOrDefault(tgt, 0);
            if (count == 0) {
                res++;
            } else {
                countMap.put(tgt, count - 1);
            }
        }
        return res;
    }

    private int root(int[] id, int x) {
        int p = x;
        for (; id[p] >= 0; p = id[p]) {}
        return p;
    }

    private boolean union(int[] id, int x, int y) {
        int px = root(id, x);
        int py = root(id, y);
        if (px == py) { return false; }

        if (id[py] < id[px]) {
            int tmp = px;
            px = py;
            py = tmp;
        }
        id[px] += id[py];
        id[py] = px;
        return true;
    }

    public int minimumHammingDistance3(int[] source, int[] target, int[][] allowedSwaps) {
        int n = source.length;
        Set<Integer>[] graph = new Set[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new HashSet<>();
        }
        for (int[] e : allowedSwaps) {
            graph[e[0]].add(e[1]);
            graph[e[1]].add(e[0]);
        }
        boolean[] visited = new boolean[n];
        int res = 0;
        for (int i = 0; i < n; i++) {
            if (visited[i]) { continue; }

            List<Integer> indices = new ArrayList<>();
            dfs(indices, graph, i, visited);
            res += diff(indices, source, target);
        }
        return res;
    }

    private void dfs(List<Integer> indices, Set<Integer>[] graph, int cur, boolean[] visited) {
        visited[cur] = true;
        indices.add(cur);
        for (int next : graph[cur]) {
            if (!visited[next]) {
                dfs(indices, graph, next, visited);
            }
        }
    }

    private void test(int[] source, int[] target, int[][] allowedSwaps, int expected) {
        assertEquals(expected, minimumHammingDistance(source, target, allowedSwaps));
        assertEquals(expected, minimumHammingDistance2(source, target, allowedSwaps));
        assertEquals(expected, minimumHammingDistance3(source, target, allowedSwaps));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4}, new int[] {2, 1, 4, 5}, new int[][] {{0, 1}, {2, 3}}, 1);
        test(new int[] {1, 2, 3, 4}, new int[] {1, 3, 2, 4}, new int[][] {}, 2);
        test(new int[] {5, 1, 2, 4, 3}, new int[] {1, 5, 4, 2, 3},
             new int[][] {{0, 4}, {4, 2}, {1, 3}, {1, 4}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
