import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC107: https://leetcode.com/problems/binary-tree-level-order-traversal-ii/
//
// Given a binary tree, return the bottom-up level order traversal of its nodes'
// values. (ie, from left to right, level by level from leaf to root).
public class TreeLevelOrderTraversal2 {
    // Solution of Choice
    // BFS/Queue
    // beats 23.12%(3 ms)
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        queue.offer(null);
        List<Integer> level = new LinkedList<>();
        while (true) {
            TreeNode n = queue.poll();
            if (n == null) { // finished one level
                res.add(0, level); // or reverse the res at the end
                if (queue.isEmpty()) return res;

                level = new LinkedList<>();
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

    // BFS/Queue
    // beats 23.12%(3 ms)
    public List<List<Integer> > levelOrderBottom2(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        if (root == null) return res;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<>();
            for (int i = queue.size(); i > 0; i--) {
                TreeNode head = queue.poll();
                level.add(head.val);
                if (head.left != null) {
                    queue.offer(head.left);
                }
                if (head.right != null) {
                    queue.offer(head.right);
                }
            }
            res.add(0, level);
        }
        return res;
    }

    // Solution of Choice
    // DFS/Recursion
    // beats 67.75%(2 ms)
    public List<List<Integer> > levelOrderBottom3(TreeNode root) {
        List<List<Integer> > res = new ArrayList<>();
        levelOrderBottom3(root, 0, res);
        return res;
    }

    private void levelOrderBottom3(TreeNode root, int level, List<List<Integer>> res) {
        if (root == null) return;

        if (res.size() == level) {
            res.add(0, new ArrayList<>());
        }
        res.get(res.size() - 1 - level).add(root.val);
        levelOrderBottom3(root.left, level + 1, res);
        levelOrderBottom3(root.right, level + 1, res);
    }

    void test(Function<TreeNode, List<List<Integer>>> traversal,
              String s, int[] ... expected) {
        TreeNode root = TreeNode.of(s);
        Integer[][] res = traversal.apply(root).stream().map(
            l -> l.toArray(new Integer[0])).toArray(Integer[][]::new);
        assertArrayEquals(expected, res);
    }

    void test(String s, int[] ... expected) {
        TreeLevelOrderTraversal2 t = new TreeLevelOrderTraversal2();
        test(t::levelOrderBottom, s, expected);
        test(t::levelOrderBottom2, s, expected);
        test(t::levelOrderBottom3, s, expected);
    }

    @Test
    public void test1() {
        test("1,#,2,3", new int[][] {{3}, {2}, {1}});
        test("1,2,3,4,5", new int[][] {{4, 5}, {2, 3}, {1}});
        test("3,9,20,#,#,15,7", new int[][] {{15, 7}, {9, 20}, {3}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TreeLevelOrderTraversal2");
    }
}
