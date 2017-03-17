import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import common.Utils;

// LC314: https://leetcode.com/problems/binary-tree-vertical-order-traversal
//
// Given a binary tree, return the vertical order traversal of its nodes' values.
// (ie, from top to bottom, column by column).
// If two nodes are in the same row and column, the order should be from left to right.
public class VerticalOrder {
    // BFS + Queue + Heap + Hash Table
    // time complexity: O(N * log(N))
    // beats 22.54%(8 ms for 212 tests)
    public List<List<Integer> > verticalOrder(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Map<TreeNode, Integer> map = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                if (a[2] != b[2]) return a[2] - b[2];
                return a[1] != b[1] ? a[1] - b[1] : a[3] - b[3];
            }
        });
        map.put(root, 0);
        queue.offer(root);
        pq.offer(new int[] {root.val, 0, 0, 0});
        for (int row = 1, n = 1; !queue.isEmpty(); row++) {
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                int col = map.get(node);
                if (node.left != null) {
                    map.put(node.left, col - 1);
                    queue.offer(node.left);
                    pq.offer(new int[] {node.left.val, row, col - 1, n++});
                }
                if (node.right != null) {
                    map.put(node.right, col + 1);
                    queue.offer(node.right);
                    pq.offer(new int[] {node.right.val, row, col + 1, n++});
                }
            }
        }
        List<Integer> col = new ArrayList<>();
        for (int[] prev = pq.peek(), cur; !pq.isEmpty(); prev = cur) {
            cur = pq.poll();
            if (cur[2] != prev[2]) {
                res.add(col);
                col = new ArrayList<>();
            }
            col.add(cur[0]);
        }
        res.add(col);
        return res;
    }

    // BFS + Queue + Hash Table
    // time complexity: O(N)
    // beats 39.78%(6 ms for 212 tests)
    public List<List<Integer> > verticalOrder2(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Map<TreeNode, List<Integer> > map = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        List<Integer> col = new ArrayList<>();
        map.put(root, col);
        col.add(root.val);
        res.add(col);
        for (queue.offer(root); !queue.isEmpty(); ) {
            TreeNode node = queue.poll();
            int index = res.indexOf(map.get(node));
            List<Integer> nextCol;
            if (node.left != null) {
                if (index > 0) {
                    nextCol = res.get(index - 1);
                } else {
                    index++;
                    res.add(0, nextCol = new ArrayList<>());
                }
                nextCol.add(node.left.val);
                queue.offer(node.left);
                map.put(node.left, nextCol);
            }
            if (node.right != null) {
                if (index + 1 < res.size()) {
                    nextCol = res.get(index + 1);
                } else {
                    res.add(nextCol = new ArrayList<>());
                }
                nextCol.add(node.right.val);
                queue.offer(node.right);
                map.put(node.right, nextCol);
            }
        }
        return res;
    }

    // BFS + Queue + Hash Table
    // time complexity: O(N)
    // beats 63.00%(5 ms for 212 tests)
    public List<List<Integer> > verticalOrder3(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Map<Integer, List<Integer> > map = new HashMap<>();
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);
        Queue<Integer> colQueue = new LinkedList<>();
        colQueue.add(0);
        int minCol = 0;
        int maxCol = 0;
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.poll();
            int col = colQueue.poll();
            List<Integer> output = map.get(col);
            if (output == null) {
                map.put(col, output = new ArrayList<>());
            }
            output.add(node.val);
            if (node.left != null) {
                nodeQueue.add(node.left);
                colQueue.add(col - 1);
                minCol = Math.min(minCol, col - 1);
            }
            if (node.right != null) {
                nodeQueue.add(node.right);
                colQueue.add(col + 1);
                maxCol = Math.max(maxCol, col + 1);
            }
        }
        for (int i = minCol; i <= maxCol; i++) {
            res.add(map.get(i));
        }
        return res;
    }

    // BFS + Queue + Hash Table
    // time complexity: O(N)
    // beats 63.00%(5 ms for 212 tests)
    public List<List<Integer> > verticalOrder3_2(TreeNode root) {
        List<List<Integer> > res = new LinkedList<>();
        if (root == null) return res;

        Map<Integer, List<Integer> > map = new HashMap<>();
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);
        Queue<Integer> colQueue = new LinkedList<>();
        colQueue.add(0);
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.poll();
            int col = colQueue.poll();
            List<Integer> output = map.get(col);
            if (output == null) {
                map.put(col, output = new ArrayList<>());
            }
            output.add(node.val);
            if (node.left != null) {
                nodeQueue.add(node.left);
                colQueue.add(col - 1);
            }
            if (node.right != null) {
                nodeQueue.add(node.right);
                colQueue.add(col + 1);
            }
        }
        res.add(map.get(0));
        for (int i = -1;; i--) {
            List<Integer> col = map.get(i);
            if (col == null) break;

            res.add(0, col);
        }
        for (int i = 1;; i++) {
            List<Integer> col = map.get(i);
            if (col == null) break;

            res.add(col);
        }
        return res;
    }

    // BFS + Queue + Recursion
    // time complexity: O(N)
    // beats 63.00%(5 ms for 212 tests)
    public List<List<Integer> > verticalOrder4(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        int[] range = new int[2];
        colRange(root, range, 0);
        for (int i = range[1] - range[0]; i >= 0; i--) {
            res.add(new ArrayList<>());
        }
        Queue<TreeNode> nodeQueue = new LinkedList<>();
        nodeQueue.add(root);
        Queue<Integer> colQueue = new LinkedList<>();
        colQueue.add(-range[0]);
        while (!nodeQueue.isEmpty()) {
            TreeNode node = nodeQueue.poll();
            int col = colQueue.poll();
            res.get(col).add(node.val);
            if (node.left != null) {
                nodeQueue.add(node.left);
                colQueue.add(col - 1);
            }
            if (node.right != null) {
                nodeQueue.add(node.right);
                colQueue.add(col + 1);
            }
        }
        return res;
    }

    private void colRange(TreeNode root, int[] range, int col) {
        if (root == null) return;

        range[0] = Math.min(range[0], col);
        range[1] = Math.max(range[1], col);
        colRange(root.left, range, col - 1);
        colRange(root.right, range, col + 1);
    }

    // BFS + List
    // time complexity: O(N)
    // beats 100.00%(2 ms for 212 tests)
    public List<List<Integer> > verticalOrder5(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        List<TreeNode> nodes = new ArrayList<>();
        nodes.add(root);
        List<Integer> cols = new ArrayList<>();
        cols.add(0);
        int minCol = 0;
        int maxCol = 0;
        for (int i = 0; i < nodes.size(); i++) {
            int col = cols.get(i);
            TreeNode node = nodes.get(i);
            minCol = Math.min(minCol, col);
            maxCol = Math.max(maxCol, col);
            if (node.left != null) {
                nodes.add(node.left);
                cols.add(col - 1);
            }
            if (node.right != null) {
                nodes.add(node.right);
                cols.add(col + 1);
            }
        }
        int m = maxCol - minCol + 1;
        for (int i = 0; i < m; i++) {
            res.add(new ArrayList<>());
        }
        for (int i = 0; i < nodes.size(); i++) {
            res.get(cols.get(i) - minCol).add(nodes.get(i).val);
        }
        return res;
    }

    // TODO: DFS

    void test(Function<TreeNode, List<List<Integer>>> verticalOrder,
              String s, int[][] expected) {
        List<List<Integer> > res = verticalOrder.apply(TreeNode.of(s));
        assertArrayEquals(expected, Utils.toInts(res));
    }

    void test(String s, int[][] expected) {
        VerticalOrder v = new VerticalOrder();
        test(v::verticalOrder, s, expected);
        test(v::verticalOrder2, s, expected);
        test(v::verticalOrder3, s, expected);
        test(v::verticalOrder3_2, s, expected);
        test(v::verticalOrder4, s, expected);
        test(v::verticalOrder5, s, expected);
    }

    @Test
    public void test1() {
        test("3,1,6,#,2,4,#,#,#,#,5", new int[][] {{1}, {3, 2, 4}, {6, 5}});
        test("3,9,20,#,#,15,7", new int[][] {{9}, {3, 15}, {20}, {7}});
        test("3,9,8,4,0,1,7", new int[][] {{4}, {9}, {3, 0, 1}, {8}, {7}});
        test("3,9,8,4,0,1,7,#,#,#,2,5", new int[][] {{4}, {9, 5}, {3, 0, 1}, {8, 2}, {7}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("VerticalOrder");
    }
}
