import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC103: https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/
//
// Given a binary tree, return the zigzag level order traversal of its nodes'
// values. (ie, from left to right, then right to left for the next level and
// alternate between).
public class TreeZigzagTraversal {
    // BFS/Queue
    // beats 41.18%(2 ms)
    public List<List<Integer> > zigzagLevelOrder(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        boolean direction = false;
        List<Integer> level = new LinkedList<>(); // ArrayList<>();
        while (true) {
            TreeNode n = queue.poll();
            if (n == null) { // finished one level
                res.add(level);
                if (queue.isEmpty()) return res;

                level = new ArrayList<>();
                queue.offer(null);
                direction = !direction;
            } else {
                if (direction) {
                    level.add(0, n.val);
                } else {
                    level.add(n.val);
                }
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
    // BFS/Queue
    // beats 41.18%(2 ms)
    public List<List<Integer> > zigzagLevelOrder2(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        for (boolean direction = false; !queue.isEmpty(); direction = !direction) {
            List<Integer> level = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode head = queue.poll();
                if (direction) {
                    level.add(0, head.val);
                } else {
                    level.add(head.val);
                }
                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
            }
            res.add(level);
        }
        return res;
    }

    // Solution of Choice
    // DFS/Recursion
    // beats 41.18%(2 ms)
    public List<List<Integer> > zigzagLevelOrder3(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        zigzagLevelOrder3(root, 0, res);
        return res;
    }

    private void zigzagLevelOrder3(TreeNode root, int level, List<List<Integer> > res) {
        if (root == null) return;

        if (res.size() == level) {
            res.add(new LinkedList<>());
        }
        if ((level & 1) == 0) {
            res.get(level).add(root.val);
        } else {
            res.get(level).add(0, root.val);
        }
        zigzagLevelOrder3(root.left, level + 1, res);
        zigzagLevelOrder3(root.right, level + 1, res);
    }

    void test(Function<TreeNode, List<List<Integer>>> traversal,
              String s, int[] ... expected) {
        TreeNode root = TreeNode.of(s);
        Integer[][] res = traversal.apply(root).stream().map(
            l -> l.toArray(new Integer[0])).toArray(Integer[][]::new);
        assertArrayEquals(expected, res);
    }

    void test(String s, int[] ... expected) {
        TreeZigzagTraversal t = new TreeZigzagTraversal();
        test(t::zigzagLevelOrder, s, expected);
        test(t::zigzagLevelOrder2, s, expected);
        test(t::zigzagLevelOrder3, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", new int[][] {{1}, {2}, {3}});
        test("1,2,3,4,#,#,5", new int[][] {{1}, {3, 2}, {4, 5}});
        test("3,9,20,#,#,15,7", new int[][] {{3}, {20, 9}, {15, 7}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeZigzagTraversal");
    }
}
