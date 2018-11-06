import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC102: https://leetcode.com/problems/binary-tree-level-order-traversal/
//
// Given a binary tree, return the level order traversal of its nodes' values.
public class TreeLevelOrderTraversal {
    // BFS + Queue
    // beats 36.62%(2 ms)
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        List<Integer> level = new ArrayList<>();
        while (true) {
            TreeNode n = queue.poll();
            if (n == null) { // finished one level
                res.add(level);
                if (queue.isEmpty()) return res;

                level = new ArrayList<>();
                queue.offer(null);
            } else {
                level.add(n.val);
                if (n.left != null) {
                    queue.offer(n.left);
                }
                if (n.right != null) {
                    queue.offer(n.right);
                }
            }
        }
    }

    // Solution of Choice
    // BFS + Queue
    // beats 89.75%(1 ms for 34 tests)
    public List<List<Integer>> levelOrder2(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        for (queue.offer(root); !queue.isEmpty();) {
            List<Integer> level = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode cur = queue.poll();
                level.add(cur.val);
                if (cur.left != null) {
                    queue.offer(cur.left);
                }
                if (cur.right != null) {
                    queue.offer(cur.right);
                }
            }
            res.add(level);
        }
        return res;
    }

    // Solution of Choice
    // DFS + Recursion
    // beats 89.75%(1 ms for 34 tests)
    public List<List<Integer>> levelOrder3(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        levelOrder3(root, 0, res);
        return res;
    }

    private void levelOrder3(TreeNode root, int level, List<List<Integer>> res) {
        if (root == null) return;

        if (res.size() == level) {
            res.add(new ArrayList<>());
        }
        res.get(level).add(root.val);
        levelOrder3(root.left, level + 1, res);
        levelOrder3(root.right, level + 1, res);
    }

    // Recursion
    // beats 5.51%(4 ms)
    public List<List<Integer>> levelOrder4(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root != null) {
            levelOrder4(Arrays.asList(root), res);
        }
        return res;
    }

    private void levelOrder4(List<TreeNode> level, List<List<Integer>> res) {
        List<Integer> levelVal = new LinkedList<>();
        List<TreeNode> nextLevel = new LinkedList<>();
        for (TreeNode node : level) {
            levelVal.add(node.val);
            if (node.left != null) {
                nextLevel.add(node.left);
            }
            if (node.right != null) {
                nextLevel.add(node.right);
            }
        }
        res.add(levelVal);
        if (!nextLevel.isEmpty()) {
            levelOrder4(nextLevel, res);
        }
    }

    void test(Function<TreeNode, List<List<Integer>>> traversal, String s, int[]... expected) {
        TreeNode root = TreeNode.of(s);
        Integer[][] res = traversal.apply(root).stream().map(l -> l.toArray(new Integer[0]))
                                   .toArray(Integer[][]::new);
        assertArrayEquals(expected, res);
    }

    void test(String s, int[]... expected) {
        TreeLevelOrderTraversal t = new TreeLevelOrderTraversal();
        test(t::levelOrder, s, expected);
        test(t::levelOrder2, s, expected);
        test(t::levelOrder3, s, expected);
        test(t::levelOrder4, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", new int[][] {{1}, {2}, {3}});
        test("3,9,20,#,#,15,7", new int[][] {{3}, {9, 20}, {15, 7}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
