import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;
import common.TreeNode;

// LC987: https://leetcode.com/problems/vertical-order-traversal-of-a-binary-tree/
//
// Given a binary tree, return the vertical order traversal of its nodes values.
// For each node at position (X, Y), its left and right children respectively
// will be at positions (X-1, Y-1) and (X+1, Y-1). Running a vertical line from 
// X = -infinity to X = +infinity, whenever the vertical line touches some nodes,
// we report the values of the nodes in order from top to bottom. If two nodes 
// have the same position, then the value of the node that is reported first is
// the value that is smaller. Return an list of non-empty reports in order of X
// coordinate.  Every report will have a list of values of nodes.
public class VerticalTraversal {
    // BFS + Queue + SortedMap
    // 6 ms(76.67%), 26.5 MB(100.00%) for 30 tests
    // time complexity: O(N * log(N)), space complexity: O(N)
    public List<List<Integer>> verticalTraversal(TreeNode root) {
        Map<Integer, List<int[]>> map = new TreeMap<>();
        Queue<Object[]> queue = new LinkedList<>();
        queue.offer(new Object[] {root, 0, 0});
        for (int level = 0; !queue.isEmpty(); level++) {
            for (int i = queue.size(); i > 0; i--) {
                Object[] curObj = queue.poll();
                TreeNode cur = (TreeNode)curObj[0];
                Integer x = (Integer)curObj[1];
                Integer y = (Integer)curObj[2];
                // map.computeIfAbsent(y, a -> new ArrayList<>()).add(new int[] {cur.val, x});
                map.putIfAbsent(y, new ArrayList<>());
                map.get(y).add(new int[] {cur.val, x});
                if (cur.left != null) {
                    queue.offer(new Object[] {cur.left, level, y - 1});
                }
                if (cur.right != null) {
                    queue.offer(new Object[] {cur.right, level, y + 1});
                }
            }
        }
        List<List<Integer>> res = new ArrayList<>();
        for (List<int[]> colSet : map.values()) {
            List<Integer> colList = new ArrayList<>();
            Collections.sort(colSet, new Comparator<int[]>() {
                public int compare(int[] a, int[] b) {
                    return (a[1] != b[1]) ? a[1] - b[1] : a[0] - b[0];
                }
            });
            for (int[] col : colSet) {
                colList.add(col[0]);
            }
            res.add(colList);
        }
        return res;
    }

    // DFS + Recursion + SortedMap
    // 6 ms(76.67%), 26.5 MB(100.00%) for 30 tests
    // time complexity: O(N * log(N)), space complexity: O(N)
    public List<List<Integer>> verticalTraversal2(TreeNode root) {
        Map<Integer, Map<Integer, Set<Integer>>> map = new TreeMap<>();
        dfs(root, 0, 0, map);
        List<List<Integer>> res = new ArrayList<>();
        for (Map<Integer, Set<Integer>> cols : map.values()) {
            List<Integer> col = new ArrayList<>();
            for (Set<Integer> nodes : cols.values()) {
                for (int node : nodes) {
                    col.add(node);
                }
            }
            res.add(col);
        }
        return res;
    }

    private void dfs(TreeNode root, int x, int y, Map<Integer, Map<Integer, Set<Integer>>> map) {
        if (root == null) return;

        if (!map.containsKey(x)) {
            map.put(x, new TreeMap<>());
        }
        if (!map.get(x).containsKey(y)) {
            map.get(x).put(y, new TreeSet<>());
        }
        map.get(x).get(y).add(root.val);
        dfs(root.left, x - 1, y + 1, map);
        dfs(root.right, x + 1, y + 1, map);
    }

    // DFS + Recursion + Sort
    // 4 ms(99.58%), 26.4 MB(100.00%) for 30 tests
    // time complexity: O(N * log(N)), space complexity: O(N)
    public List<List<Integer>> verticalTraversal3(TreeNode root) {
        List<Node> nodes = new ArrayList<>();
        dfs(root, 0, 0, nodes);
        Collections.sort(nodes);
        List<List<Integer>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        int prev = nodes.get(0).x;
        for (Node node : nodes) {
            if (node.x != prev) {
                prev = node.x;
                res.add(new ArrayList<>());
            }
            res.get(res.size() - 1).add(node.val);
        }
        return res;
    }

    private void dfs(TreeNode node, int x, int y, List<Node> locations) {
        if (node == null) return;

        locations.add(new Node(x, y, node.val));
        dfs(node.left, x - 1, y + 1, locations);
        dfs(node.right, x + 1, y + 1, locations);
    }

    class Node implements Comparable<Node> {
        int x, y, val;

        Node(int x, int y, int val) {
            this.x = x;
            this.y = y;
            this.val = val;
        }

        @Override
        public int compareTo(Node that) {
            if (this.x != that.x) return this.x - that.x;
            if (this.y != that.y) return this.y - that.y;
            return this.val - that.val;
        }
    }

    void test(String s, Integer[][] expected) {
        List<List<Integer>> exp = Utils.toList(expected);
        assertEquals(exp, verticalTraversal(TreeNode.of(s)));
        assertEquals(exp, verticalTraversal2(TreeNode.of(s)));
        assertEquals(exp, verticalTraversal3(TreeNode.of(s)));
    }

    @Test
    public void test1() {
        test("3,9,20,#,#,15,7", new Integer[][] {{9}, {3, 15}, {20}, {7}});
        test("1,2,3,4,5,6,7", new Integer[][] {{4}, {2}, {1, 5, 6}, {3}, {7}});
        test("0,2,1,3,#,5,22,9,4,12,25,#,#,13,14,8,6,#,#,#,#,#,27,24,26,#,17,7,#,28,#,#,#,#,#,19,#,11,10,#,#,#,23,16,15,20,18,#,#,#,#,#,21,#,#,29",
             new Integer[][] {{13, 28}, {9, 24, 27, 16}, {3, 8, 14, 11, 19},
                              {2, 4, 12, 7, 17, 26, 15, 20, 23}, {0, 5, 6, 10, 21, 29}, {1, 25, 18},
                              {22}});
        test("0,2,1,3,#,#,#,4,5,#,7,6,#,10,8,11,9",
             new Integer[][] {{4, 10, 11}, {3, 6, 7}, {2, 5, 8, 9}, {0}, {1}});
        test("0,5,1,9,#,2,#,#,#,#,3,4,8,6,#,#,#,7",
             new Integer[][] {{9, 7}, {5, 6}, {0, 2, 4}, {1, 3}, {8}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
