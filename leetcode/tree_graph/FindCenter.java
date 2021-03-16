import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1791: https://leetcode.com/problems/find-center-of-star-graph/
//
// There is an undirected star graph consisting of n nodes labeled from 1 to n. A star graph is a
// graph where there is one center node and exactly n - 1 edges that connect the center node with
// every other node.
// You are given a 2D integer array edges where each edges[i] = [ui, vi] indicates that there is an
// edge between the nodes ui and vi. Return the center of the given star graph.
//
// Constraints:
//
// 3 <= n <= 10^5
// edges.length == n - 1
// edges[i].length == 2
// 1 <= ui, vi <= n
// ui != vi
//The given edges represent a valid star graph.
public class FindCenter {
    // time complexity: O(E), space complexity: O(1)
    // 2 ms(%), 64.1 MB(%) for 60 tests
    public int findCenter(int[][] edges) {
        int n = 0;
        for (int[] e : edges) {
            n = Math.max(n, e[0]);
            n = Math.max(n, e[1]);
        }
        int[] degree = new int[n + 1];
        for (int[] e : edges) {
            if (++degree[e[0]] > 1) { return e[0]; }
            if (++degree[e[1]] > 1) { return e[1]; }
        }
        return 0;
    }

    // time complexity: O(1), space complexity: O(1)
    // 0 ms(%), 64.6 MB(%) for 60 tests
    public int findCenter2(int[][] edges) {
        int[] e0 = edges[0];
        int[] e1 = edges[1];
        return (e0[0] == e1[0] || e0[0] == e1[1]) ? e0[0] : e0[1];
    }

    private void test(int[][] edges, int expected) {
        assertEquals(expected, findCenter(edges));
        assertEquals(expected, findCenter2(edges));
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {2, 3}, {4, 2}}, 2);
        test(new int[][] {{1, 2}, {5, 1}, {1, 3}, {1, 4}}, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
