import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1202: https://leetcode.com/problems/smallest-string-with-swaps/
//
// You are given a string s, and an array of pairs of indices in the string pairs where
// pairs[i] = [a, b] indicates 2 indices(0-indexed) of the string.
// You can swap the characters at any pair of indices in the given pairs any number of times.
// Return the lexicographically smallest string that s can be changed to after using the swaps.
//
// Constraints:
// 1 <= s.length <= 10^5
// 0 <= pairs.length <= 10^5
// 0 <= pairs[i][0], pairs[i][1] < s.length
// s only contains lower case English letters.
public class SmallestStringWithSwaps {
    // Union Find + Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 48 ms(54.57%), 89.7 MB(50.92%) for 36 tests
    public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int[] id = new int[n];
        Arrays.fill(id, -1);
        for (List<Integer> pair : pairs) {
            union(id, pair.get(0), pair.get(1));
        }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.computeIfAbsent(parent(id, i), x -> new ArrayList<>()).add(i);
        }
        char[] res = new char[n];
        for (List<Integer> indices : map.values()) {
            PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(a -> cs[a]));
            pq.addAll(indices);
            for (int i : indices) {
                res[i] = cs[pq.poll()];
            }
        }
        return String.valueOf(res);
    }

    private int parent(int[] id, int x) {
        int p = x;
        for (; id[p] >= 0; p = id[p]) {}
        return p;
    }

    private boolean union(int[] id, int x, int y) {
        int px = parent(id, x);
        int py = parent(id, y);
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

    // Union Find + Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 40 ms(81.29%), 87.9 MB(73.56%) for 36 tests
    public String smallestStringWithSwaps2(String s, List<List<Integer>> pairs) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int[] id = new int[n];
        Arrays.fill(id, -1);
        for (List<Integer> pair : pairs) {
            union(id, pair.get(0), pair.get(1));
        }
        Map<Integer, PriorityQueue<Character>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = parent(id, i);
            map.computeIfAbsent(root, x -> new PriorityQueue<>()).offer(cs[i]);
        }
        char[] res = new char[n];
        for (int i = 0; i < n; i++) {
            int root = parent(id, i);
            res[i] = map.get(root).poll();
        }
        return String.valueOf(res);
    }

    // DFS + Recursion + Sort + Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 135 ms(19.97%), 91 MB(36.01%) for 36 tests
    public String smallestStringWithSwaps3(String s, List<List<Integer>> pairs) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (List<Integer> pair : pairs) {
            int u = pair.get(0);
            int v = pair.get(1);
            graph.computeIfAbsent(u, x -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, x -> new ArrayList<>()).add(u);
        }
        int n = s.length();
        Map<Integer, List<Integer>> map = new HashMap<>();
        int[] color = new int[n];
        for (int i = 0; i < n; i++) {
            dfs(graph, i, -1, color, map);
        }
        char[] res = new char[n];
        for (List<Integer> indices : map.values()) {
            Collections.sort(indices);
            PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(s::charAt));
            pq.addAll(indices);
            for (int i : indices) {
                res[i] = s.charAt(pq.poll());
            }
        }
        return String.valueOf(res);
    }

    private void dfs(Map<Integer, List<Integer>> graph, int cur, int prev, int[] color,
                     Map<Integer, List<Integer>> map) {
        if (color[cur] > 0) { return; }

        int c = color[cur] = (prev < 0) ? (cur + 1) : color[prev];
        map.computeIfAbsent(c, x -> new ArrayList<>()).add(cur);
        for (int nei : graph.getOrDefault(cur, Collections.emptyList())) {
            dfs(graph, nei, cur, color, map);
        }
    }

    // DFS + Recursion + Sort + Heap + Hash Table
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 130 ms(20.40%), 108.6 MB(9.00%) for 36 tests
    public String smallestStringWithSwaps4(String s, List<List<Integer>> pairs) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (List<Integer> pair : pairs) {
            int u = pair.get(0);
            int v = pair.get(1);
            graph.computeIfAbsent(u, x -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, x -> new ArrayList<>()).add(u);
        }
        int n = s.length();
        boolean[] visited = new boolean[n];
        char[] res = new char[n];
        for (int i = 0; i < n; i++) {
            if (visited[i]) { continue; }

            List<Integer> indices = new ArrayList<>();
            dfs(graph, i, indices, visited);
            Collections.sort(indices);
            PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.comparingInt(s::charAt));
            pq.addAll(indices);
            for (int j : indices) {
                res[j] = s.charAt(pq.poll());
            }
        }
        return String.valueOf(res);
    }

    private void dfs(Map<Integer, List<Integer>> graph, int cur, List<Integer> indices,
                     boolean[] visited) {
        visited[cur] = true;
        indices.add(cur);
        for (int nei : graph.getOrDefault(cur, Collections.emptyList())) {
            if (!visited[nei]) {
                dfs(graph, nei, indices, visited);
            }
        }
    }

    private void test(String s, Integer[][] pairs, String expected) {
        List<List<Integer>> pairList = Utils.toList(pairs);
        assertEquals(expected, smallestStringWithSwaps(s, pairList));
        assertEquals(expected, smallestStringWithSwaps2(s, pairList));
        assertEquals(expected, smallestStringWithSwaps3(s, pairList));
        assertEquals(expected, smallestStringWithSwaps4(s, pairList));
    }

    @Test public void test() {
        test("dcab", new Integer[][] {{0, 3}, {1, 2}}, "bacd");
        test("dcab", new Integer[][] {{0, 3}, {1, 2}, {0, 2}}, "abcd");
        test("cba", new Integer[][] {{0, 1}, {1, 2}}, "abc");
        test("dcab", new Integer[][] {}, "dcab");
        test("cbacirefjdskafsdadmbnvbsucxiz",
             new Integer[][] {{0, 1}, {1, 2}, {8, 10}, {9, 6}, {10, 20}, {3, 12}, {18, 17}, {5, 25},
                              {11, 28}, {5, 21}, {7, 15}, {4, 23}, {6, 13}, {17, 13}, {14, 23}},
             "abcaicddjdnkcesfafmbsrbsuvxiz");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
