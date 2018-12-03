import java.util.*;

import org.junit.Test;

import common.DisjointSet;

import static org.junit.Assert.*;

// LC947: https://leetcode.com/problems/most-stones-removed-with-same-row-or-column/
//
// On a 2D plane, we place stones at some integer coordinate points.  Each 
// coordinate point may have at most one stone. Now, a move consists of removing
// a stone that shares a column or row with another stone on the grid.
// What is the largest possible number of moves we can make?
//
// Note:
// 1 <= stones.length <= 1000
// 0 <= stones[i][j] < 10000
public class RemoveStones {
    // DFS + Stack
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 65.92%(30 ms for 68 tests)
    public int removeStones(int[][] stones) {
        int n = stones.length;
        int[][] graph = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1]) {
                    graph[i][++graph[i][0]] = j;
                    graph[j][++graph[j][0]] = i;
                }
            }
        }
        int res = 0;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;

            visited[i] = true;
            res--;
            Stack<Integer> stack = new Stack<>();
            for (stack.push(i); !stack.isEmpty(); res++) {
                int node = stack.pop();
                for (int j = graph[node][0]; j > 0; j--) {
                    int nei = graph[node][j];
                    if (!visited[nei]) {
                        stack.push(nei);
                        visited[nei] = true;
                    }
                }
            }
        }
        return res;
    }

    // Union Find + Set
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 72.44%(27 ms for 68 tests)
    public int removeStones2(int[][] stones) {
        int n = stones.length;
        DisjointSet ds = new DisjointSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1]) {
                    ds.union(i, j);
                }
            }
        }
        return n - ds.count();
    }

    // Union Find + Set
    // time complexity: O(N ^ α(N)), space complexity: O(N)
    // beats 85.47%(18 ms for 68 tests)
    public int removeStones3(int[][] stones) {
        int n = stones.length;
        final int MAX = 10000;
        DisjointSet ds = new DisjointSet(MAX * 2);
        for (int[] stone : stones) {
            ds.union(stone[0], stone[1] + MAX);
        }
        Set<Integer> visited = new HashSet<>();
        for (int[] stone : stones) {
            visited.add(ds.root(stone[0]));
        }
        return n - visited.size();
    }

    // Union Find + Hash Table
    // time complexity: O(N ^ log(N)), space complexity: O(N ^ 2)
    // beats 70.02%(28 ms for 68 tests)
    public int removeStones4(int[][] stones) {
        Map<Integer, Integer> parent = new HashMap<>();
        int[] islands = new int[1];
        for (int i = 0; i < stones.length; ++i) {
            union(stones[i][0], ~stones[i][1], parent, islands);
        }
        return stones.length - islands[0];
    }

    private int find(int x, Map<Integer, Integer> parent, int[] islands) {
        if (parent.putIfAbsent(x, x) == null) {
            islands[0]++;
        }
        if (x != parent.get(x)) {
            parent.put(x, find(parent.get(x), parent, islands));
        }
        return parent.get(x);
    }

    private void union(int x, int y, Map<Integer, Integer> parent, int[] islands) {
        x = find(x, parent, islands);
        y = find(y, parent, islands);
        if (parent.get(x) != y) {
            parent.put(x, y);
            islands[0]--;
        }
    }

    void test(int[][] stones, int expected) {
        assertEquals(expected, removeStones(stones));
        assertEquals(expected, removeStones2(stones));
        assertEquals(expected, removeStones3(stones));
        assertEquals(expected, removeStones4(stones));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 0}, {0, 1}, {1, 0}, {1, 2}, {2, 1}, {2, 2}}, 5);
        test(new int[][] {{0, 0}, {0, 2}, {1, 1}, {2, 0}, {2, 2}}, 3);
        test(new int[][] {{0, 0}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
