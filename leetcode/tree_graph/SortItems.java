import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1203: https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/
//
// There are n items each belonging to zero or one of m groups where group[i] is the group that the
// i-th item belongs to and it's equal to -1 if the i-th item belongs to no group. The items and the
// groups are zero indexed. A group can have no item belonging to it.
// Return a sorted list of the items such that:
// The items that belong to the same group are next to each other in the sorted list.
// There are some relations between these items where beforeItems[i] is a list containing all the
// items that should come before the i-th item in the sorted array (to the left of the i-th item).
// Return any solution if there is more than one solution and return an empty list if there is no
// solution.
//
// Constraints:
// 1 <= m <= n <= 3 * 10^4
// group.length == beforeItems.length == n
// -1 <= group[i] <= m - 1
// 0 <= beforeItems[i].length <= n - 1
// 0 <= beforeItems[i][j] <= n - 1
// i != beforeItems[i][j]
// beforeItems[i] does not contain duplicates elements.
public class SortItems {
    // Topological Sort + Hash Table
    // time complexity: O(N+M), space complexity: O(N+M)
    // 26 ms(86.52%), 52.3 MB(88.39%) for 17 tests
    public int[] sortItems(int n, int m, int[] group, List<List<Integer>> beforeItems) {
        for (int i = 0; i < n; i++) {
            int g = group[i];
            if (g < 0) {
                group[i] = m++;
            }
        }
        List<Integer>[] groupGraph = new List[m + 1];
        int[] groupIndegree = new int[m + 1];
        List<Integer>[] graph = new List[n];
        int[] indegree = new int[n];
        for (int i = 0; i <= m; i++) {
            groupGraph[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            for (int before : beforeItems.get(i)) {
                graph[before].add(i);
                indegree[i]++;
                if (group[before] != group[i]) {
                    groupGraph[group[before]].add(group[i]);
                    groupIndegree[group[i]]++;
                }
            }
        }
        List<Integer> sortedGroup = sort(groupGraph, groupIndegree);
        if (sortedGroup == null) { return new int[0]; }

        List<Integer> sorted = sort(graph, indegree);
        if (sorted == null) { return new int[0]; }

        Map<Integer, List<Integer>> sortedMap = new HashMap<>();
        for (int i : sorted) {
            sortedMap.computeIfAbsent(group[i], x -> new ArrayList<>()).add(i);
        }
        int[] res = new int[n];
        int i = 0;
        for (int g : sortedGroup) {
            for (int item : sortedMap.getOrDefault(g, Collections.emptyList())) {
                res[i++] = item;
            }
        }
        return res;
    }

    private List<Integer> sort(List<Integer>[] graph, int[] indegree) {
        int n = graph.length;
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < indegree.length; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        List<Integer> res = new ArrayList<>();
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            res.add(cur);
            for (int nei : graph[cur]) {
                if (--indegree[nei] == 0) {
                    queue.offer(nei);
                }
            }
        }
        return (res.size() == n) ? res : null;
    }

    // Topological Sort + DFS + Recursion
    // time complexity: O(N+M), space complexity: O(N+M)
    // 34 ms(74.91%), 72.7 MB(20.60%) for 17 tests
    public int[] sortItems2(int n, int m, int[] group, List<List<Integer>> beforeItems) {
        List<Integer>[] graph = new List[n + m];
        int[] indegree = new int[graph.length];
        for (int i = 0; i < graph.length; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int i = 0; i < n; i++) {
            if (group[i] >= 0) {
                graph[n + group[i]].add(i);
                indegree[i]++;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int before : beforeItems.get(i)) {
                int curGroup = group[i] < 0 ? i : n + group[i];
                int beforeGroup = group[before] < 0 ? before : n + group[before];
                if (curGroup == beforeGroup) {
                    graph[before].add(i);
                    indegree[i]++;
                } else {
                    graph[beforeGroup].add(curGroup);
                    indegree[curGroup]++;
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < graph.length; i++) {
            if (indegree[i] == 0) {
                dfs(graph, indegree, i, n, res);
            }
        }
        return (res.size() == n) ? res.stream().mapToInt(i -> i).toArray() : new int[0];
    }

    private void dfs(List<Integer>[] graph, int[] indegree, int cur, int n, List<Integer> res) {
        if (cur < n) {
            res.add(cur);
        }
        indegree[cur]--; // cannot omit this
        for (int child : graph[cur]) {
            if (--indegree[child] == 0) {
                dfs(graph, indegree, child, n, res);
            }
        }
    }

    @FunctionalInterface interface Function<A, B, C, D, E> {
        E apply(A a, B b, C c, D d);
    }

    private void test(Function<Integer, Integer, int[], List<List<Integer>>, int[]> sortItems,
                      int n, int m, int[] group, Integer[][] beforeItems, boolean expected) {
        List<List<Integer>> beforeItemList = Utils.toList(beforeItems);
        int[] res = sortItems.apply(n, m, group.clone(), beforeItemList);
        assertTrue("Expected " + (expected ? "one" : "none") + " solution",
                   expected ^ (res.length == 0));
        if (!expected) { return; }

        int[] pos = new int[n];
        for (int i = 0; i < n; i++) {
            pos[res[i]] = i;
        }
        for (int i = 0; i < n; i++) {
            for (int before : beforeItems[i]) {
                assertTrue("Item " + before + " should be before " + i, pos[before] < pos[i]);
            }
        }
        Set[] members = new Set[m];
        for (int i = 0; i < m; i++) {
            members[i] = new HashSet<>();
        }
        for (int i = 0; i < n; i++) {
            if (group[i] >= 0) {
                members[group[i]].add(i);
            }
        }
        for (Set<Integer> items : members) {
            if (items.size() <= 1) { continue; }

            List<Integer> itemPos = new ArrayList<>();
            for (int i : items) {
                itemPos.add(pos[i]);
            }
            Collections.sort(itemPos);
            assertEquals("the group should be together", itemPos.size(),
                         itemPos.get(itemPos.size() - 1) - itemPos.get(0) + 1);
        }
    }

    private void test(int n, int m, int[] group, Integer[][] beforeItems, boolean expected) {
        SortItems s = new SortItems();
        test(s::sortItems, n, m, group, beforeItems, expected);
        test(s::sortItems2, n, m, group, beforeItems, expected);
    }

    @Test public void test() {
        test(5, 5, new int[] {2, 0, -1, 3, 0}, new Integer[][] {{2, 1, 3}, {2, 4}, {}, {}, {}},
             true);
        test(8, 2, new int[] {-1, -1, 1, 0, 0, 1, 0, -1},
             new Integer[][] {{}, {6}, {5}, {6}, {3, 6}, {}, {}, {}}, true);
        test(3, 1, new int[] {-1, 0, -1}, new Integer[][] {{}, {0}, {1}}, true);
        test(8, 2, new int[] {-1, -1, 1, 0, 0, 1, 0, -1},
             new Integer[][] {{}, {6}, {5}, {6}, {3, 6}, {}, {}, {}}, true);
        test(8, 2, new int[] {-1, -1, 1, 0, 0, 1, 0, -1},
             new Integer[][] {{}, {6}, {5}, {6}, {3}, {}, {4}, {}}, false);
        test(5, 3, new int[] {0, 0, 2, 1, 0}, new Integer[][] {{3}, {}, {}, {}, {1, 3, 2}}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
