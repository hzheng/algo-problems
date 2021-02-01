import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1743: https://leetcode.com/problems/restore-the-array-from-adjacent-pairs/
//
// There is an integer array nums that consists of n unique elements, but you have forgotten it.
// However, you do remember every pair of adjacent elements in nums. You are given a 2D integer
// array adjacentPairs of size n - 1 where each adjacentPairs[i] = [ui, vi] indicates that the
// elements ui and vi are adjacent in nums. It is guaranteed that every adjacent pair of elements
// nums[i] and nums[i+1] will exist in adjacentPairs, either as [nums[i], nums[i+1]] or [nums[i+1],
// nums[i]]. The pairs can appear in any order.
// Return the original array nums. If there are multiple solutions, return any of them.
//
// Constraints:
// nums.length == n
// adjacentPairs.length == n - 1
// adjacentPairs[i].length == 2
// 2 <= n <= 1^05
// -1^05 <= nums[i], ui, vi <= 10^5
// There exists some nums that has adjacentPairs as its pairs.
public class RestoreArray {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 137 ms(87.62%), 169.6 MB(54.97%) for 46 tests
    public int[] restoreArray(int[][] adjacentPairs) {
        int n = adjacentPairs.length + 1;
        int[] res = new int[n];
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] adj : adjacentPairs) {
            map.computeIfAbsent(adj[0], x -> new ArrayList<>()).add(adj[1]);
            map.computeIfAbsent(adj[1], x -> new ArrayList<>()).add(adj[0]);
        }
        for (int k : map.keySet()) {
            if (map.get(k).size() == 1) {
                res[0] = k;
                break;
            }
        }
        for (int i = 1; i < n; i++) {
            List<Integer> neis = map.get(res[i - 1]);
            res[i] = neis.get(0);
            neis = map.get(res[i]);
            if (i + 1 < n && neis.get(0) == res[i - 1]) {
                neis.set(0, neis.get(1));
            }
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 102 ms(97.42%), 74.7 MB(99.75%) for 46 tests
    public int[] restoreArray2(int[][] adjacentPairs) {
        int n = adjacentPairs.length + 1;
        int[] res = new int[n];
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] adj : adjacentPairs) {
            map.computeIfAbsent(adj[0], x -> new ArrayList<>()).add(adj[1]);
            map.computeIfAbsent(adj[1], x -> new ArrayList<>()).add(adj[0]);
        }
        for (int k : map.keySet()) {
            List<Integer> nei = map.get(k);
            if (nei.size() == 1) {
                res[0] = k;
                res[1] = nei.get(0);
                break;
            }
        }
        for (int i = 2; i < n; i++) {
            List<Integer> next = map.get(res[i - 1]);
            res[i] = next.get(next.get(0) == res[i - 2] ? 1 : 0);
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 92 ms(98.67%), 84.9 MB(87.21%) for 46 tests
    public int[] restoreArray3(int[][] adjacentPairs) {
        int n = adjacentPairs.length + 1;
        int[] res = new int[n];
        final int MAX = 100000 * 3 + 1; // -10^5 <= ui, vi <= 10^5
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] adj : adjacentPairs) {
            int u = adj[0];
            int v = adj[1];
            map.put(u, map.getOrDefault(u, 0) + v + MAX);
            map.put(v, map.getOrDefault(v, 0) + u + MAX);
        }
        for (int u : map.keySet()) {
            if (map.get(u) < MAX * 4 / 3) { // only one neighbor
                res[0] = u;
                break;
            }
        }
        for (int i = 1; ; i++) {
            res[i] = map.get(res[i - 1]) - MAX;
            if (i + 1 == n) { return res; }

            map.put(res[i], map.get(res[i]) - res[i - 1] - MAX);
        }
    }

    // Recursion + DFS + Hash Table + Set
    // time complexity: O(N), space complexity: O(N)
    // 253 ms(37.79%), 382.8 MB(5.05%) for 46 tests
    public int[] restoreArray4(int[][] adjacentPairs) {
        int n = adjacentPairs.length + 1;
        int[] res = new int[n];
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int[] adj : adjacentPairs) {
            map.computeIfAbsent(adj[0], x -> new ArrayList<>()).add(adj[1]);
            map.computeIfAbsent(adj[1], x -> new ArrayList<>()).add(adj[0]);
        }
        for (int k : map.keySet()) {
            List<Integer> nei = map.get(k);
            if (nei.size() == 1) {
                res[0] = k;
                break;
            }
        }
        dfs(res, map, 0, res[0], new HashSet<>());
        return res;
    }

    private void dfs(int[] res, Map<Integer, List<Integer>> map, int cur, int u,
                     Set<Integer> visited) {
        if (!visited.add(u)) { return; }

        res[cur] = u;
        for (int v : map.get(u)) {
            dfs(res, map, cur + 1, v, visited);
        }
    }

    void test(int[][] adjacentPairs, int[] expected) {
        assertArrayEquals(expected, restoreArray(adjacentPairs));
        assertArrayEquals(expected, restoreArray2(adjacentPairs));
        assertArrayEquals(expected, restoreArray3(adjacentPairs));
        assertArrayEquals(expected, restoreArray4(adjacentPairs));
    }

    @Test public void test() {
        test(new int[][] {{2, 1}, {3, 4}, {3, 2}}, new int[] {1, 2, 3, 4});
        test(new int[][] {{4, -2}, {1, 4}, {-3, 1}}, new int[] {-2, 4, 1, -3});
        test(new int[][] {{100000, -100000}}, new int[] {100000, -100000});
        test(new int[][] {{-42960, 49495}, {-33846, 54818}, {20809, -58527}, {-18641, -90061},
                          {-18641, 20809}, {-58527, 46988}, {84555, -42960}, {-33846, -65819},
                          {-21913, -55038}, {9097, 49495}, {-97777, 94993}, {-97777, -21913},
                          {-55038, 46988}, {84555, -90061}, {9097, -65819}, {54818, 48095}},
             new int[] {94993, -97777, -21913, -55038, 46988, -58527, 20809, -18641, -90061, 84555,
                        -42960, 49495, 9097, -65819, -33846, 54818, 48095});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
